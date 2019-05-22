package com.leo.commonutil.enume;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// 自定义一个注解Mode
@StringDef({AESType.ECB,
        AESType.CBC})
@Retention(RetentionPolicy.SOURCE)
public @interface AESType {
    String ECB = "AES/ECB/PKCS5Padding";
    String CBC = "AES/CBC/PKCS5Padding";
}
