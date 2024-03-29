package com.leo.safety

import android.util.Base64
import android.util.Log
import com.leo.safety.enume.AESType
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by LEO
 * on 2017/4/30.
 * AES对称加解密工具类
 */
object AESUtil {

    private const val TAG = "AESUtil"

    /**
     * 加密
     *
     * @param sSrc
     * @param sKey
     * @param sIv
     * @param aesType
     * @return
     */
    fun encrypt(sSrc: String, sKey: String?, sIv: String?, @AESType aesType: String): String? {
        if (sKey == null) {
            Log.e(TAG, "Key为空null")
            return null
        }
        /**
         * 判断Key是否为16位
         * java openSdk只能支持16位
         */
        if (sKey.length != 16) {
            Log.e(TAG, "Key长度不是16位")
            return null
        }
        val raw = sKey.toByteArray()
        val secretKeySpec = SecretKeySpec(raw, "AES")
        return try {
            val cipher = Cipher.getInstance(aesType)//"算法/模式/补码方式"
            if (aesType == AESType.CBC) {
                if (sIv.isNullOrEmpty()) {
                    throw RuntimeException("sIv can't be null or empty when CBC")
                }
                val iv = IvParameterSpec(sIv.toByteArray())// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv)
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
            }
            val encrypted = cipher.doFinal(sSrc.toByteArray())
            Base64.encodeToString(encrypted, Base64.NO_WRAP)//此处使用BASE64做转码功能，同时能起到2次加密的作用。
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
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
    fun decrypt(sSrc: String, sKey: String?, sIv: String?, @AESType aesType: String): String? {
        return try {
            // 判断Key是否正确
            if (sKey == null) {
                Log.e(TAG, "Key为空null")
                return null
            }
            // 判断Key是否为16位
            if (sKey.length != 16) {
                Log.e(TAG, "Key长度不是16位")
                return null
            }
            val raw = sKey.toByteArray(charset("UTF-8"))
            val skeySpec = SecretKeySpec(raw, "AES")
            val cipher = Cipher.getInstance(aesType)
            if (aesType == AESType.CBC) {
                if (sIv.isNullOrEmpty()) {
                    throw RuntimeException("sIv can't be null or empty when CBC")
                }
                val iv = IvParameterSpec(sIv.toByteArray())// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)
            } else {
                cipher.init(Cipher.DECRYPT_MODE, skeySpec)
            }
            val encrypted1 = Base64.decode(sSrc, Base64.NO_WRAP)
            val original = cipher.doFinal(encrypted1)
            String(original)
        } catch (e: Exception) {
            null
        }
    }
}