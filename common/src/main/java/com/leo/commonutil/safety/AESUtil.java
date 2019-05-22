package com.leo.commonutil.safety;

import android.util.Base64;

import com.leo.commonutil.enume.AESType;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by LEO
 * on 2017/4/30.
 * AES对称加解密工具类
 */
public final class AESUtil {
    public static String DEFAULT_KEY = "3d74ddfa93e536e";
    public static String DEFAULT_IV = "ZnSQKCKP5R3RP5bJ";

    public static void setKey(String defaultKey) {
        DEFAULT_KEY = defaultKey;
    }

    public static void setIv(String defaultIv) {
        DEFAULT_IV = defaultIv;
    }

    public static String encrypt(String sSrc, String sKey, @AESType String aesType) {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        try {
            Cipher cipher = Cipher.getInstance(aesType);//"算法/模式/补码方式"
            if (aesType.equals(AESType.CBC)) {
                IvParameterSpec iv = new IvParameterSpec(DEFAULT_IV.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            }
            byte[] encrypted = cipher.doFinal(sSrc.getBytes());
            return Base64.encodeToString(encrypted, android.util.Base64.DEFAULT);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 解密
    public static String decrypt(String sSrc, String sKey, @AESType String aesType) {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(aesType);
            if (aesType.equals(AESType.CBC)) {
                IvParameterSpec iv = new IvParameterSpec(DEFAULT_IV.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            }
            byte[] encrypted1 = Base64.decode(sSrc, android.util.Base64.DEFAULT);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                return new String(original);
            } catch (Exception e) {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }
}