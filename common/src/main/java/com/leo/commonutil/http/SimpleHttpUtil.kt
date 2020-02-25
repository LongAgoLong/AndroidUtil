package com.leo.commonutil.http

import com.leo.commonutil.storage.IOUtil
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.*

/**
 * Created by LEO
 * on 2018/12/28
 * 基于HttpUrlConnect的简单封装
 */
class SimpleHttpUtil private constructor() {
    private var readTimeOut = 8000
    private var connectTimeOut = 8000
    private val requestProPertyMap: HashMap<String, String> = HashMap()
    fun setReadTimeOut(readTimeOut: Int) {
        this.readTimeOut = readTimeOut
    }

    fun setConnectTimeOut(connectTimeOut: Int) {
        this.connectTimeOut = connectTimeOut
    }

    fun addRequestProPerty(key: String, value: String) {
        requestProPertyMap[key] = value
    }

    fun removeRequestProPerty(key: String) {
        requestProPertyMap.remove(key)
    }

    fun post(urlPath: String, hashMap: HashMap<String?, String?>?): Respond {
        val respond = Respond()
        var conn: HttpURLConnection? = null
        var reader: BufferedReader? = null
        var inputStream: InputStream? = null
        try {
            conn = URL(urlPath).openConnection() as HttpURLConnection
            //设置请求方式,请求超时信息
            conn.requestMethod = "POST"
            conn.readTimeout = readTimeOut
            conn.connectTimeout = connectTimeOut
            //设置运行输入,输出:
            conn.doOutput = true
            conn.doInput = true
            //Post方式不能缓存,需手动设置为false
            conn.useCaches = false
            //我们请求的数据
            val data: String
            data = if (null != hashMap && !hashMap.isEmpty()) {
                val builder = StringBuilder()
                val keys: List<String?> = ArrayList(hashMap.keys) //B是set型的
                for (i in keys.indices) {
                    val key = keys[i]
                    builder.append(key).append("=").append(URLEncoder.encode(hashMap[key], "UTF-8"))
                    if (i != keys.size - 1) {
                        builder.append("&")
                    }
                }
                builder.toString().trim { it <= ' ' }
            } else {
                ""
            }
            //这里可以写一些请求头的东东...
            if (!requestProPertyMap.isEmpty()) {
                val iter: Iterator<*> = requestProPertyMap.entries.iterator()
                while (iter.hasNext()) {
                    val entry = iter.next() as Map.Entry<*, *>
                    val key = entry.key as String
                    val value = entry.value as String
                    conn.setRequestProperty(key, value)
                }
            }
            //获取输出流
            val out = conn.outputStream
            out.write(data.toByteArray())
            out.flush()
            respond.code = conn.responseCode
            if (respond.code == 200) {
                val sb = StringBuilder()
                // 获取响应的输入流对象
                inputStream = conn.inputStream
                reader = BufferedReader(InputStreamReader(inputStream, Charset.forName("UTF-8")))
                var strRead: String? = null
                while (reader.readLine().also { strRead = it } != null) {
                    sb.append(strRead)
                }
                // 释放资源
                inputStream.close()
                // 返回字符串
                respond.body = sb.toString().trim { it <= ' ' }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            reader?.let { IOUtil.closeQuietly(it) }
            inputStream?.let {
                IOUtil.closeQuietly(it)
            }
            conn?.disconnect()
        }
        return respond
    }

    operator fun get(urlPath: String): Respond {
        val respond = Respond()
        var httpURLConnection: HttpURLConnection? = null
        var bufferedReader: BufferedReader? = null
        var inputStream: InputStream? = null
        try { //使用该地址创建一个 URL 对象
            val url = URL(urlPath)
            //使用创建的URL对象的openConnection()方法创建一个HttpURLConnection对象
            httpURLConnection = url.openConnection() as HttpURLConnection
            /**
             * 设置HttpURLConnection对象的参数
             */
            // 设置请求方法为 GET 请求
            httpURLConnection.requestMethod = "GET"
            //使用输入流
            httpURLConnection.doInput = true
            //GET 方式，不需要使用输出流
            httpURLConnection.doOutput = false
            //设置超时
            httpURLConnection.connectTimeout = connectTimeOut
            httpURLConnection.readTimeout = readTimeOut
            //这里可以写一些请求头的东东...
            if (!requestProPertyMap.isEmpty()) {
                val iter: Iterator<*> = requestProPertyMap.entries.iterator()
                while (iter.hasNext()) {
                    val entry = iter.next() as Map.Entry<*, *>
                    val key = entry.key as String
                    val value = entry.value as String
                    httpURLConnection.setRequestProperty(key, value)
                }
            }
            //连接
            httpURLConnection.connect()
            //还有很多参数设置 请自行查阅
            //连接后，创建一个输入流来读取response
            respond.code = httpURLConnection.responseCode
            if (respond.code == 200) {
                inputStream = httpURLConnection.inputStream
                bufferedReader = BufferedReader(InputStreamReader(inputStream, Charset.forName("UTF-8")))
                var line: String? = ""
                val stringBuilder = StringBuilder()
                //每次读取一行，若非空则添加至 stringBuilder
                while (bufferedReader.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }
                //读取所有的数据后，赋值给 response
                respond.body = stringBuilder.toString().trim { it <= ' ' }
            }
            httpURLConnection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            bufferedReader?.let { IOUtil.closeQuietly(it) }
            inputStream?.let { IOUtil.closeQuietly(it) }
            httpURLConnection?.disconnect()
        }
        return respond
    }

    companion object {
        @Volatile
        private var httpUtil: SimpleHttpUtil? = null

        fun getInstance(): SimpleHttpUtil {
            return httpUtil ?: synchronized(this) {
                httpUtil ?: SimpleHttpUtil().also { httpUtil = it }
            }
        }
    }

}