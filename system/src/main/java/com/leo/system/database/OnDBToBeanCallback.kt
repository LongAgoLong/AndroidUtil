package com.leo.system.database

import android.database.Cursor

interface OnDBToBeanCallback {
    fun <T> onTrans(cursor: Cursor?, cls: Class<T>?): T
}