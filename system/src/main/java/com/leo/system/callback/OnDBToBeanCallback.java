package com.leo.system.callback;

import android.database.Cursor;

public interface OnDBToBeanCallback {
    <T> T onTrans(Cursor cursor, Class<T> cls);
}
