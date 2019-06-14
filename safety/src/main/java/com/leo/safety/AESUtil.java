package com.leo.safety;

import android.util.Base64;

import androidx.annotation.Nullable;

import com.leo.safety.enume.AESType;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by LEO
 * on 2017/4/30.
 * AES对称加解密工具类
 */
public final class AESUtil {

    /**
     * 加密
     *
     * @param sSrc
     * @param sKey
     * @param sIv
     * @param aesType
     * @return
     */
    public static String encrypt(String sSrc, String sKey, @Nullable String sIv, @AESType String aesType) {
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
                if (null == sIv || sIv.equals("")) {
                    throw new RuntimeException("sIv can't be null or empty when CBC");
                }
                IvParameterSpec iv = new IvParameterSpec(sIv.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            }
            byte[] encrypted = cipher.doFinal(sSrc.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param sSrc
     * @param sKey
     * @param sIv
     * @param aesType
     * @return
     */
    public static String decrypt(String sSrc, String sKey, @Nullable String sIv, @AESType String aesType) {
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
                if (null == sIv || sIv.equals("")) {
                    throw new RuntimeException("sIv can't be null or empty when CBC");
                }
                IvParameterSpec iv = new IvParameterSpec(sIv.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            }
            byte[] encrypted1 = Base64.decode(sSrc, Base64.DEFAULT);
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