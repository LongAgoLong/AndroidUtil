package com.leo.safety.enume;

import androidx.annotation.StringDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 自定义一个注解Mode
@StringDef({AESType.ECB,
        AESType.CBC})
@Target({ElementType.PARAMETER, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
public @interface AESType {
    String ECB = "AES/ECB/PKCS5Padding";
    String CBC = "AES/CBC/PKCS5Padding";
}
