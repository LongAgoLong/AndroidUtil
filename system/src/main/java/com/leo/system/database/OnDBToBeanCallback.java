package com.leo.system.database;

import android.database.Cursor;

public interface OnDBToBeanCallback {
    <T> T onTrans(Cursor cursor, Class<T> cls);
}
