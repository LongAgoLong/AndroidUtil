package com.leo.system;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.leo.system.callback.OnDBToBeanCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by LEO
 * On 2019/6/13
 * Description:线程安全的数据库操作辅助类
 */
public class DBExcuteHelp {
    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase sqliteDb;
    // 计数器-完美解决 打开/关闭 数据库连接
    private AtomicInteger mOpenCounter = new AtomicInteger();
    private OnDBToBeanCallback onDBToBeanCallback;

    public DBExcuteHelp(@NonNull SQLiteOpenHelper helper, @NonNull OnDBToBeanCallback onDBToBeanCallback) {
        this.sqLiteOpenHelper = helper;
        this.onDBToBeanCallback = onDBToBeanCallback;
    }

    /**
     * 打开数据库操作
     */
    public synchronized void openDB() {
        if (mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            sqliteDb = sqLiteOpenHelper.getWritableDatabase();
        }
    }

    /**
     * 关闭数据库
     */
    public synchronized void closeDB() {
        if (mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            if (null != sqliteDb && sqliteDb.isOpen()) {
                sqliteDb.close();
            }
        }
    }

    /**
     * 开启事务
     * 连续插入大量数据时必须启用，可显著提高插入效率
     */
    public void beginTransaction() {
        if (null != sqliteDb) {
            sqliteDb.beginTransaction();
        }
    }

    /**
     * 关闭事务
     */
    public void endTransaction() {
        if (null != sqliteDb) {
            sqliteDb.setTransactionSuccessful();
            sqliteDb.endTransaction();
        }
    }

    /**
     * 增
     *
     * @param tableName 表名
     * @param values    数据
     */
    public void insert(@NonNull String tableName, @NonNull ContentValues values) {
        sqliteDb.insert(tableName, null, values);
    }

    /**
     * 删
     *
     * @param tableName     表名
     * @param selection     筛选条件
     * @param selectionArgs 筛选值
     */
    public void delete(@NonNull String tableName, @Nullable String selection, @Nullable String... selectionArgs) {
        sqliteDb.delete(tableName, selection, selectionArgs);
    }

    /**
     * 改
     *
     * @param tableName 表名
     * @param values    数据
     * @param where     筛选条件
     * @param whereArgs 筛选值
     */
    public void update(@NonNull String tableName, @NonNull ContentValues values, @NonNull String where, @NonNull String... whereArgs) {
        sqliteDb.update(tableName, values, where, whereArgs);
    }

    /**
     * 查
     *
     * @param tableName     表名
     * @param selection     筛选条件
     * @param selectionArgs 筛选值
     * @return
     */
    public Cursor query(@NonNull String tableName, @Nullable String selection, @Nullable String... selectionArgs) {
        return sqliteDb.query(tableName, null, selection, selectionArgs, null, null, null);
    }

    public <T> List<T> query(@NonNull String tableName, Class<T> cls, @Nullable String selection, @Nullable String... selectionArgs) {
        List<T> list = new ArrayList<>();
        Cursor cursor = query(tableName, selection, selectionArgs);
        while (cursor.moveToNext()) {
            if (null != onDBToBeanCallback) {
                T t = onDBToBeanCallback.onTrans(cursor, cls);
                list.add(t);
            }
        }
        cursor.close();
        return list;
    }

    /**
     * 某条数据是否存在
     *
     * @param tableName     表名
     * @param selection     筛选条件
     * @param selectionArgs 筛选值
     * @return
     */
    public boolean isDataExist(@NonNull String tableName, @Nullable String selection, @Nullable String... selectionArgs) {
        Cursor cursor = query(tableName, selection, selectionArgs);
        boolean moveToNext = cursor.moveToNext();
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return moveToNext;
    }

    /**
     * 无则增/有则改
     *
     * @param tableName     表名
     * @param values        数据
     * @param selection     筛选条件
     * @param selectionArgs 筛选值
     */
    public void insertOrUpdate(@NonNull String tableName, @NonNull ContentValues values, @NonNull String selection, @NonNull String... selectionArgs) {
        if (isDataExist(tableName, selection, selectionArgs)) {
            update(tableName, values, selection, selectionArgs);
        } else {
            insert(tableName, values);
        }
    }
}
