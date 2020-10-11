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
class DBExcuteHelp(private val sqLiteOpenHelper: SQLiteOpenHelper, private val onDBToBeanCallback: OnDBToBeanCallback) {
    private var sqliteDb: SQLiteDatabase? = null

    // 计数器-完美解决 打开/关闭 数据库连接
    private val mOpenCounter = AtomicInteger()

    /**
     * 打开数据库操作
     */
    @Synchronized
    fun openDB() {
        if (mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            sqliteDb = sqLiteOpenHelper.writableDatabase
        }
    }

    /**
     * 关闭数据库
     */
    @Synchronized
    fun closeDB() {
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
    fun beginTransaction() {
        if (null == sqliteDb) {
            throw RuntimeException("must transfer openDB() first")
        }
        sqliteDb!!.beginTransaction()
    }

    /**
     * 关闭事务
     */
    fun endTransaction() {
        if (null == sqliteDb) {
            throw RuntimeException("must transfer openDB() first")
        }
        sqliteDb!!.setTransactionSuccessful()
        sqliteDb!!.endTransaction()
    }

    /**
     * 增
     *
     * @param tableName 表名
     * @param values    数据
     */
    fun insert(tableName: String, values: ContentValues) {
        if (null == sqliteDb) {
            throw RuntimeException("must transfer openDB() first")
        }
        sqliteDb!!.insert(tableName, null, values)
    }

    /**
     * 删
     *
     * @param tableName     表名
     * @param selection     筛选条件
     * @param selectionArgs 筛选值
     */
    fun delete(tableName: String, selection: String?, vararg selectionArgs: String?) {
        if (null == sqliteDb) {
            throw RuntimeException("must transfer openDB() first")
        }
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
    fun update(tableName: String, values: ContentValues, where: String, vararg whereArgs: String) {
        if (null == sqliteDb) {
            throw RuntimeException("must transfer openDB() first")
        }
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
    fun query(tableName: String, selection: String?, vararg selectionArgs: String?): Cursor {
        if (null == sqliteDb) {
            throw RuntimeException("must transfer openDB() first")
        }
        return sqliteDb!!.query(tableName, null, selection, selectionArgs, null, null, null)
    }

    fun <T> query(tableName: String, cls: Class<T>?, selection: String?, vararg selectionArgs: String?): List<T> {
        val list: MutableList<T> = ArrayList()
        val cursor = query(tableName, selection, *selectionArgs)
        while (cursor.moveToNext()) {
            onDBToBeanCallback.run {
                list.add(onTrans(cursor, cls))
            }
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
    fun isDataExist(tableName: String, selection: String?, vararg selectionArgs: String?): Boolean {
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
    fun insertOrUpdate(tableName: String, values: ContentValues, selection: String, vararg selectionArgs: String) {
        if (isDataExist(tableName, selection, *selectionArgs)) {
            update(tableName, values, selection, *selectionArgs)
        } else {
            insert(tableName, values)
        }
    }
}