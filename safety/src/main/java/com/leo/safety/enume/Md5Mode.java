package com.leo.safety.enume;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IntDef({Md5Mode.LOWERCASE,
        Md5Mode.UPPERCASE})
@Target({ElementType.PARAMETER, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
public @interface Md5Mode {
    int LOWERCASE = 0;
    int UPPERCASE = 1;
}
