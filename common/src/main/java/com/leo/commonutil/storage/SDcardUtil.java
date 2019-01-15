package com.leo.commonutil.storage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * @author Ari
 */
public class SDcardUtil {
    /**
     * SD卡是否存在
     */
    public static boolean isSDCardExists() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * SD卡根路径
     */
    public static File getRoot() {
        return Environment.getExternalStorageDirectory();
    }


    /**
     * SD卡剩余存储空间
     */
    public static long getFreeSpace() {
        File root = getRoot();
        StatFs stat = new StatFs(root.getPath());
        return stat.getAvailableBlocks();
    }

    /**
     * 保存到SD卡
     *
     * @param filename           文件名称(带扩展名)
     * @param filePath           存储目录路径
     * @param filecontent        存储内容
     * @param isPICSendBroadcast 是否需要发送文件更新广播
     */
    public static boolean saveToSDCard(Context context, String filename, String filePath, String filecontent, boolean isPICSendBroadcast) {
        return saveToSDCard(context, filename, filePath, filecontent.getBytes(), isPICSendBroadcast);
    }

    /**
     * 保存到SD卡
     *
     * @param filename           文件名称(带扩展名)
     * @param filePath           存储目录路径
     * @param filecontent        存储内容
     * @param isPICSendBroadcast 是否需要发送文件更新广播
     */
    public static boolean saveToSDCard(Context context, String filename, String filePath, byte[] filecontent, boolean isPICSendBroadcast) {
        try {
            final File file = new File(filePath, filename);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(filecontent);
            outStream.close();
            if (isPICSendBroadcast) {
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);//发送更新图片信息广播
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                context.sendBroadcast(intent);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 保存到SD卡
     *
     * @param filename           文件名称(带扩展名)
     * @param filePath           存储目录路径
     * @param filecontent        存储内容
     * @param isPICSendBroadcast 是否需要发送文件更新广播
     */
    public static boolean saveToSDCard(Context context, String filename, String filePath, File filecontent, boolean isPICSendBroadcast) {
        try {
            int bytesum = 0;
            int byteread;
            final File file = new File(filePath, filename);
            if (file.exists()) {
                file.delete();
            }
            InputStream inStream = new FileInputStream(filecontent); //读入原文件
            FileOutputStream outStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                outStream.write(buffer, 0, byteread);
            }
            inStream.close();
            outStream.close();
            if (isPICSendBroadcast) {
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);//发送更新图片信息广播
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                context.sendBroadcast(intent);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static synchronized void save(Context context, @NonNull String s, String pathName) {
        File file = new File(pathName);
        byte[] encode = Base64.encode(s.getBytes(), Base64.NO_WRAP);
        String s1 = new String(encode);
        //Write the file to disk
        OutputStreamWriter writer = null;
        try {
            OutputStream out = new FileOutputStream(file);
            writer = new OutputStreamWriter(out, "UTF-8");
            writer.write(s1);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(Context context, @NonNull String pathName) {
        StringBuilder jsonString = new StringBuilder("");
        File file = new File(pathName);
        if (file.exists()) {
            try {
                InputStreamReader input = new InputStreamReader(new FileInputStream(file), "UTF-8");
                BufferedReader reader = new BufferedReader(input);
                String empString = null;
                while ((empString = reader.readLine()) != null) {
                    jsonString.append(empString);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String s = jsonString.toString();
        byte[] decode = Base64.decode(s, Base64.NO_WRAP);
        if (null == decode || decode.length == 0) {
            return "";
        } else {
            return new String(decode);
        }
    }
}
