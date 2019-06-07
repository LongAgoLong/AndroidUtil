package com.leo.safety.enume;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({Md5Mode.LOWERCASE,
        Md5Mode.UPPERCASE})
@Retention(RetentionPolicy.SOURCE)
public @interface Md5Mode {
    int LOWERCASE = 0;
    int UPPERCASE = 1;
}
