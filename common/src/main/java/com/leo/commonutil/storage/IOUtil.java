package com.leo.commonutil.storage;

import com.leo.commonutil.asyn.threadPool.ThreadPoolHelp;
import com.leo.commonutil.asyn.threadPool.ThreadPoolRunnable;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IOUtil {
    private static ExecutorService mThreadPool = Executors.newFixedThreadPool(5);

    private IOUtil() {
    }

    public static ExecutorService getThreadPool() {
        return mThreadPool;
    }

    /**
     * 从流中读取数据
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] read(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        closeQuietly(inStream);
        return outStream.toByteArray();
    }

    /**
     * 从流中读取数据(异步)
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readAsyn(InputStream inStream) {
        return ThreadPoolHelp.submit(new ThreadPoolRunnable<byte[]>() {
            @Override
            public byte[] run() throws IOException {
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                closeQuietly(inStream);
                return outStream.toByteArray();
            }
        });
    }

    /**
     * Close closable object and wrap {@link IOException} with {@link
     * RuntimeException}
     *
     * @param closeables closeable object
     */
    public static void close(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * Close closable object and wrap {@link IOException} with {@link
     * RuntimeException}
     *
     * @param cursors closeable object
     */
    public static void closeQuietly(Closeable... cursors) {
        for (Closeable cursor : cursors) {
            try {
                if (null != cursor) {
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除目录下所有文件
     *
     * @param Path 路径
     */
    public static void delAllFile(String Path) {
        File path = new File(Path);
        File files[] = path.listFiles();
        if (files != null && files.length != 0) {
            for (File tfi : files) {
                if (tfi.isDirectory()) {
                    delAllFile(tfi.getPath());
                } else {
                    tfi.delete();
                }
            }
        }
    }
}
