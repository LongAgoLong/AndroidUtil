package com.leo.commonutil.enumerate;

import android.support.annotation.StringDef;

// 自定义一个注解Mode
@StringDef({AESType.ECB,
        AESType.CBC})
public @interface AESType {
    String ECB = "AES/ECB/PKCS5Padding";
    String CBC = "AES/CBC/PKCS5Padding";
}
