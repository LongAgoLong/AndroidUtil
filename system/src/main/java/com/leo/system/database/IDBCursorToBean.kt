package com.leo.system.database

import android.database.Cursor

interface IDBCursorToBean {
    fun <T> onTrans(cursor: Cursor?, cls: Class<T>?): T
}