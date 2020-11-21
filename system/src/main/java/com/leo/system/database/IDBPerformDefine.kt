package com.leo.system.database

import android.content.ContentValues
import android.database.Cursor

interface IDBPerformDefine {
    fun openDB()

    /**
     * 关闭数据库
     */
    fun closeDB()

    /**
     * 开启事务
     * 连续插入大量数据时必须启用，可显著提高插入效率
     */
    fun beginTransaction()

    /**
     * 关闭事务
     */
    fun endTransaction()

    /**
     * 增
     *
     * @param tableName 表名
     * @param values    数据
     */
    fun insert(tableName: String, values: ContentValues)
    fun insert(tableName: String, values: List<ContentValues>)

    /**
     * 删
     *
     * @param tableName     表名
     * @param selection     筛选条件
     * @param selectionArgs 筛选值
     */
    fun delete(tableName: String, selection: String?, vararg selectionArgs: String?)

    /**
     * 改
     *
     * @param tableName 表名
     * @param values    数据
     * @param where     筛选条件
     * @param whereArgs 筛选值
     */
    fun update(tableName: String, values: ContentValues, where: String, vararg whereArgs: String)

    /**
     * 查
     *
     * @param tableName     表名
     * @param selection     筛选条件
     * @param selectionArgs 筛选值
     * @return
     */
    fun query(tableName: String, selection: String?, vararg selectionArgs: String?): Cursor
    fun query(tableName: String, limit: Long, selection: String?, vararg selectionArgs: String?): Cursor
    fun <T> query(tableName: String, cls: Class<T>?, selection: String?, vararg selectionArgs: String?): List<T>
    fun <T> query(tableName: String, cls: Class<T>?, limit: Long, selection: String?, vararg selectionArgs: String?): List<T>

    /**
     * 某条数据是否存在
     *
     * @param tableName     表名
     * @param selection     筛选条件
     * @param selectionArgs 筛选值
     * @return
     */
    fun isDataExist(tableName: String, selection: String?, vararg selectionArgs: String?): Boolean

    /**
     * 无则增/有则改
     *
     * @param tableName     表名
     * @param values        数据
     * @param selection     筛选条件
     * @param selectionArgs 筛选值
     */
    fun insertOrUpdate(tableName: String, values: ContentValues, selection: String, vararg selectionArgs: String)
}