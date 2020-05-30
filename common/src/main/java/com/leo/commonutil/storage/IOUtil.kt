package com.leo.commonutil.storage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Base64
import com.leo.commonutil.asyn.threadPool.ThreadPoolHelp
import com.leo.system.LogUtil
import java.io.*

object IOUtil {
    private val TAG = IOUtil::class.java.simpleName

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

    /**
     * 保存到SD卡
     *
     * @param filePath           存储目录路径
     * @param filename           文件名称(带扩展名)
     * @param fileContent        存储内容
     * @param isNotifyScanFile 是否需要发送文件更新广播
     */
    @JvmOverloads
    fun writeDiskFile(context: Context, filePath: String = SDcardUtil.fileFolder!!.absolutePath, filename: String,
                      fileContent: String, isNotifyScanFile: Boolean = false): Boolean {
        return writeDiskFile(context, filePath, filename, fileContent.toByteArray(), isNotifyScanFile)
    }

    /**
     * 保存到SD卡
     *
     * @param filePath           存储目录路径
     * @param filename           文件名称(带扩展名)
     * @param fileContent        存储内容
     * @param isNotifyScanFile 是否需要发送文件更新广播
     */
    @JvmOverloads
    fun writeDiskFile(context: Context, filePath: String = SDcardUtil.fileFolder!!.absolutePath, filename: String,
                      fileContent: ByteArray, isNotifyScanFile: Boolean = false): Boolean {
        if (!SDcardUtil.isDiskExists) {
            LogUtil.e(TAG, "Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED")
            return false
        }
        var outStream: FileOutputStream? = null
        try {
            val file = File(filePath, filename)
            if (file.exists()) {
                file.delete()
            }
            outStream = FileOutputStream(file)
            outStream.write(fileContent)
            closeQuietly(outStream)
            if (isNotifyScanFile) {
                // 发送更新文件信息广播
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                val uri = Uri.fromFile(file)
                intent.data = uri
                context.sendBroadcast(intent)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            outStream?.let { closeQuietly(it) }
        }
        return false

    }

    /**
     * 保存到SD卡
     *
     * @param filePath           存储目录路径
     * @param filename           文件名称(带扩展名)
     * @param fileContent        存储内容
     * @param isNotifyScanFile 是否需要发送文件更新广播
     */
    @JvmOverloads
    fun writeDiskFile(context: Context, filePath: String = SDcardUtil.fileFolder!!.absolutePath, filename: String,
                      fileContent: File, isNotifyScanFile: Boolean = false): Boolean {
        if (!SDcardUtil.isDiskExists) {
            LogUtil.e(TAG, "Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED")
            return false
        }
        var inStream: FileInputStream? = null
        var outStream: FileOutputStream? = null
        try {
            var bytesum = 0
            var byteread: Int
            val file = File(filePath, filename)
            if (file.exists()) {
                file.delete()
            }
            inStream = FileInputStream(fileContent) //读入原文件
            outStream = FileOutputStream(file)
            val buffer = ByteArray(1024)
            while (inStream.read(buffer).also { byteread = it } != -1) {
                bytesum += byteread
                outStream.write(buffer, 0, byteread)
            }
            closeQuietly(inStream, outStream)
            if (isNotifyScanFile) {
                // 发送更新文件信息广播
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                val uri = Uri.fromFile(file)
                intent.data = uri
                context.sendBroadcast(intent)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inStream?.let { closeQuietly(it) }
            outStream?.let { closeQuietly(it) }
        }
        return false
    }

    /**
     * 保存并加密文本到指定文件
     *
     * @param filePath 文件路径
     * @param fileName 文件名字
     * @param content  内容
     * @param append   是否累加
     */
    @JvmOverloads
    fun writeDiskText(filePath: String = SDcardUtil.fileFolder!!.absolutePath, fileName: String, content: String,
                      base64Encode: Boolean = true, append: Boolean = false) {
        if (!SDcardUtil.isDiskExists) {
            LogUtil.e(TAG, "Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED")
            return
        }
        ThreadPoolHelp.execute {
            val file = File(filePath, fileName)
            if (!append && file.exists()) {
                file.delete()
            }
            val s1 = if (base64Encode) {
                val encode = Base64.encode(content.toByteArray(), Base64.NO_WRAP)
                String(encode)
            } else {
                content
            }
            // Write the file to disk
            var writer: OutputStreamWriter? = null
            var out: OutputStream? = null
            try {
                out = FileOutputStream(file, append)
                writer = OutputStreamWriter(out, "UTF-8")
                writer.write(s1)
                writer.flush()
                closeQuietly(writer)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                writer?.let { closeQuietly(it) }
                out?.let { closeQuietly(it) }
            }
        }
    }

    /**
     * 从指定文件获取文本并解密（同步）
     *
     * @param filePath
     * @param fileName
     * @return
     */
    @JvmOverloads
    fun getDiskText(filePath: String = SDcardUtil.fileFolder!!.absolutePath, fileName: String,
                    base64Decode: Boolean = true): String? {
        if (!SDcardUtil.isDiskExists) {
            LogUtil.e(TAG, "Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED")
            return null
        }
        val jsonString = StringBuilder()
        val file = File(filePath, fileName)
        if (file.exists()) {
            var input: InputStreamReader? = null
            var reader: BufferedReader? = null
            try {
                input = InputStreamReader(FileInputStream(file), "UTF-8")
                reader = BufferedReader(input)
                var empString: String?
                while (reader.readLine().also { empString = it } != null) {
                    jsonString.append(empString)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                reader?.let { closeQuietly(it) }
                input?.let { closeQuietly(it) }
            }
        }
        val s = jsonString.toString()
        return if (base64Decode) {
            val decode = Base64.decode(s, Base64.NO_WRAP)
            if (null == decode || decode.isEmpty()) {
                null
            } else {
                String(decode)
            }
        } else {
            s
        }
    }

    /**
     * 从指定文件获取文本并解密（异步）
     *
     * @param filePath
     * @param fileName
     */
    @JvmOverloads
    fun getDiskTextAsyn(filePath: String = SDcardUtil.fileFolder!!.absolutePath, fileName: String): String? {
        if (!SDcardUtil.isDiskExists) {
            LogUtil.e(TAG, "Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED")
            return null
        }
        val future = ThreadPoolHelp.submit { getDiskText(filePath, fileName) }
        try {
            return future.get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
