package com.leo.commonutil.http;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.leo.commonutil.storage.IOUtil;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by LEO
 * on 2018/12/28
 * 基于HttpUrlConnect的简单封装
 */
public class SimpleHttpUtil {
    private static SimpleHttpUtil httpUtil;
    private int readTimeOut;
    private int connectTimeOut;
    private HashMap<String, String> requestProPertyMap;

    private SimpleHttpUtil() {
        readTimeOut = 8000;
        connectTimeOut = 8000;
        requestProPertyMap = new HashMap<>();
    }

    public static SimpleHttpUtil getInstance() {
        if (null == httpUtil) {
            synchronized (SimpleHttpUtil.class) {
                if (null == httpUtil) {
                    httpUtil = new SimpleHttpUtil();
                }
            }
        }
        return httpUtil;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public void addRequestProPerty(@NonNull String key, @NonNull String value) {
        requestProPertyMap.put(key, value);
    }

    public void removeRequestProPerty(@NonNull String key) {
        if (requestProPertyMap.containsKey(key)) {
            requestProPertyMap.remove(key);
        }
    }

    public Respond post(@NonNull String urlPath, @Nullable HashMap<String, String> hashMap) {
        Respond respond = new Respond();
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        InputStream is = null;
        try {
            conn = (HttpURLConnection) new URL(urlPath).openConnection();
            //设置请求方式,请求超时信息
            conn.setRequestMethod("POST");
            conn.setReadTimeout(readTimeOut);
            conn.setConnectTimeout(connectTimeOut);
            //设置运行输入,输出:
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //Post方式不能缓存,需手动设置为false
            conn.setUseCaches(false);
            //我们请求的数据
            String data;
            if (null != hashMap && !hashMap.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                List<String> keys = new ArrayList<>(hashMap.keySet());//B是set型的
                for (int i = 0; i < keys.size(); i++) {
                    String key = keys.get(i);
                    builder.append(key).append("=").append(URLEncoder.encode(hashMap.get(key), "UTF-8"));
                    if (i != keys.size() - 1) {
                        builder.append("&");
                    }
                }
                data = builder.toString().trim();
            } else {
                data = "";
            }
            //这里可以写一些请求头的东东...
            if (!requestProPertyMap.isEmpty()) {
                Iterator iter = requestProPertyMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    conn.setRequestProperty(key, value);
                }
            }
            //获取输出流
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            respond.setCode(conn.getResponseCode());
            if (respond.getCode() == 200) {
                StringBuilder sb = new StringBuilder();
                // 获取响应的输入流对象
                is = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String strRead = null;
                while ((strRead = reader.readLine()) != null) {
                    sb.append(strRead);
                }
                // 释放资源
                is.close();
                // 返回字符串
                respond.setBody(sb.toString().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.INSTANCE.closeQuietly(reader, is);
            if (null != conn) {
                conn.disconnect();
            }
        }
        return respond;
    }

    public Respond get(@NonNull String urlPath) {
        Respond respond = new Respond();
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        InputStream is = null;
        try {
            //使用该地址创建一个 URL 对象
            URL url = new URL(urlPath);
            //使用创建的URL对象的openConnection()方法创建一个HttpURLConnection对象
            httpURLConnection = (HttpURLConnection) url.openConnection();
            /**
             * 设置HttpURLConnection对象的参数
             */
            // 设置请求方法为 GET 请求
            httpURLConnection.setRequestMethod("GET");
            //使用输入流
            httpURLConnection.setDoInput(true);
            //GET 方式，不需要使用输出流
            httpURLConnection.setDoOutput(false);
            //设置超时
            httpURLConnection.setConnectTimeout(connectTimeOut);
            httpURLConnection.setReadTimeout(readTimeOut);
            //这里可以写一些请求头的东东...
            if (!requestProPertyMap.isEmpty()) {
                Iterator iter = requestProPertyMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    httpURLConnection.setRequestProperty(key, value);
                }
            }
            //连接
            httpURLConnection.connect();
            //还有很多参数设置 请自行查阅
            //连接后，创建一个输入流来读取response
            respond.setCode(httpURLConnection.getResponseCode());
            if (respond.getCode() == 200) {
                is = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                //每次读取一行，若非空则添加至 stringBuilder
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                //读取所有的数据后，赋值给 response
                respond.setBody(stringBuilder.toString().trim());
            }
            httpURLConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.INSTANCE.closeQuietly(bufferedReader, is);
            if (null != httpURLConnection) {
                httpURLConnection.disconnect();
            }
        }
        return respond;
    }
}
