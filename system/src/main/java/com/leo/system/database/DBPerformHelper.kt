package com.leo.system.database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by LEO
 * On 2019/6/13
 * Description:线程安全的数据库操作辅助类
 */
class DBPerformHelper(
    private val sqLiteOpenHelper: SQLiteOpenHelper,
    private val idbCursorToBean: IDBCursorToBean
) : IDBPerformDefine {
    private var sqliteDb: SQLiteDatabase? = null

    // 计数器-完美解决 打开/关闭 数据库连接
    private val mOpenCounter = AtomicInteger()

    companion object {
        const val ROWS_NOT_LIMIT = -1L
    }

    /**
     * 打开数据库操作
     */
    @Synchronized
    override fun openDB() {
        if (mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            sqliteDb = sqLiteOpenHelper.writableDatabase
        }
    }

    /**
     * 关闭数据库
     */
    @Synchronized
    override fun closeDB() {
        if (mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            if (null != sqliteDb && sqliteDb!!.isOpen) {
                sqliteDb!!.close()
            }
        }
    }

    /**
     * 开启事务
     * 连续插入大量数据时必须启用，可显著提高插入效率
     */
    override fun beginTransaction() {
        sqliteDb!!.beginTransaction()
    }

    /**
     * 关闭事务
     */
    override fun endTransaction() {
        sqliteDb!!.run {
            setTransactionSuccessful()
            endTransaction()
        }
    }

    /**
     * 增
     *
     * @param tableName 表名
     * @param values    数据
     */
    override fun insert(tableName: String, values: ContentValues) {
        sqliteDb!!.insert(tableName, null, values)
    }

    override fun insert(tableName: String, values: List<ContentValues>) {
        sqliteDb!!.run {
            beginTransaction()
            values.forEach {
                insert(tableName, it)
            }
            setTransactionSuccessful()
            endTransaction()
        }
    }

    /**
     * 删
     *
     * @param tableName     表名
     * @param selection     筛选条件
     * @param selectionArgs 筛选值
     */
    override fun delete(tableName: String, selection: String?, vararg selectionArgs: String?) {
        sqliteDb!!.delete(tableName, selection, selectionArgs)
    }

    /**
     * 改
     *
     * @param tableName 表名
     * @param values    数据
     * @param where     筛选条件
     * @param whereArgs 筛选值
     */
    override fun update(
        tableName: String,
        values: ContentValues,
        where: String,
        vararg whereArgs: String
    ) {
        sqliteDb!!.update(tableName, values, where, whereArgs)
    }

    /**
     * 查
     *
     * @param tableName     表名
     * @param selection     筛选条件
     * @param selectionArgs 筛选值
     * @return
     */
    override fun query(
        tableName: String,
        selection: String?,
        vararg selectionArgs: String?
    ): Cursor {
        return query(tableName, ROWS_NOT_LIMIT, selection, *selectionArgs)
    }

    override fun query(
        tableName: String,
        limit: Long,
        selection: String?,
        vararg selectionArgs: String?
    ): Cursor {
        return if (limit == ROWS_NOT_LIMIT) {
            sqliteDb!!.query(tableName, null, selection, selectionArgs, null, null, null)
        } else {
            sqliteDb!!.query(
                tableName,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null,
                limit.toString()
            )
        }
    }

    override fun <T> query(
        tableName: String,
        cls: Class<T>?,
        selection: String?,
        vararg selectionArgs: String?
    ): List<T> {
        return query(tableName, cls, ROWS_NOT_LIMIT, selection, *selectionArgs)
    }

    override fun <T> query(
        tableName: String,
        cls: Class<T>?,
        limit: Long,
        selection: String?,
        vararg selectionArgs: String?
    ): List<T> {
        val list: MutableList<T> = ArrayList()
        val cursor = query(tableName, limit, selection, *selectionArgs)
        while (cursor.moveToNext()) {
            list.add(idbCursorToBean.onTrans(cursor, cls))
        }
        cursor.close()
        return list
    }

    /**
     * 某条数据是否存在
     *
     * @param tableName     表名
     * @param selection     筛选条件
     * @param selectionArgs 筛选值
     * @return
     */
    override fun isDataExist(
        tableName: String,
        selection: String?,
        vararg selectionArgs: String?
    ): Boolean {
        val cursor = query(tableName, selection, *selectionArgs)
        val moveToNext = cursor.moveToNext()
        if (!cursor.isClosed) {
            cursor.close()
        }
        return moveToNext
    }

    /**
     * 无则增/有则改
     *
     * @param tableName     表名
     * @param values        数据
     * @param selection     筛选条件
     * @param selectionArgs 筛选值
     */
    override fun insertOrUpdate(
        tableName: String,
        values: ContentValues,
        selection: String,
        vararg selectionArgs: String
    ) {
        if (isDataExist(tableName, selection, *selectionArgs)) {
            update(tableName, values, selection, *selectionArgs)
        } else {
            insert(tableName, values)
        }
    }
}