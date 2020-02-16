package com.leo.commonutil.storage

import com.leo.commonutil.asyn.threadPool.ThreadPoolHelp
import java.io.*

object IOUtil {

    /**
     * 从流中读取数据
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    @Throws(IOException::class)
    fun read(inStream: InputStream): ByteArray {
        val outStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var len: Int
        while (inStream.read(buffer).also { len = it } != -1) {
            outStream.write(buffer, 0, len)
        }
        closeQuietly(inStream)
        return outStream.toByteArray()
    }

    /**
     * 从流中读取数据(异步)
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    fun readAsyn(inStream: InputStream): ByteArray? {
        val future = ThreadPoolHelp.submit { read(inStream) }
        try {
            return future.get()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 删除目录下所有文件
     *
     * @param Path 路径
     */
    fun delAllFile(Path: String) {
        val path = File(Path)
        val files = path.listFiles()
        if (files != null && files.isNotEmpty()) {
            for (tfi in files) {
                if (tfi.isDirectory) {
                    delAllFile(tfi.path)
                } else {
                    tfi.delete()
                }
            }
        }
    }

    /**
     * 安全关闭流
     */
    fun <T : Closeable> closeQuietly(vararg closeables: T) {
        for (c in closeables) {
            c ?: continue
            try {
                c.close()
            } catch (e: IOException) {
            }
        }
    }
}
