package com.leo.usblibrary;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IntDef({SearchType.ANY,
        SearchType.FILE,
        SearchType.FOLDER})
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
public @interface SearchType {
    int ANY = 0;
    int FILE = 1;
    int FOLDER = 2;
}
