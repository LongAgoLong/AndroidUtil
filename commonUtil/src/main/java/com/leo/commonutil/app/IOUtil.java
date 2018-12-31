package com.leo.commonutil.app;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.InputStream;

public class IOUtil {
    private IOUtil() {
    }

    public static void closeQuietly(Closeable cursor) {
        try {
            if (null != cursor) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //从流中读取数据
    public static byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
}
