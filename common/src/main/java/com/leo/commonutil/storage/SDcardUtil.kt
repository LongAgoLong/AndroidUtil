package com.leo.commonutil.storage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.StatFs
import android.util.Base64
import com.leo.commonutil.asyn.threadPool.ThreadPoolHelp
import java.io.*

/**
 * @author Ari
 */
object SDcardUtil {
    /**
     * SD卡是否存在
     */
    val isSDCardExists: Boolean
        get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    /**
     * SD卡根路径
     */
    val root: File
        get() = Environment.getExternalStorageDirectory()


    /**
     * SD卡剩余存储空间
     */
    val freeSpace: Long
        get() {
            val root = root
            val stat = StatFs(root.path)
            return stat.availableBlocks.toLong()
        }

    /**
     * 保存到SD卡
     *
     * @param filename           文件名称(带扩展名)
     * @param filePath           存储目录路径
     * @param filecontent        存储内容
     * @param isPICSendBroadcast 是否需要发送文件更新广播
     */
    fun saveToSDCard(context: Context, filename: String, filePath: String, filecontent: String, isPICSendBroadcast: Boolean): Boolean {
        return saveToSDCard(context, filename, filePath, filecontent.toByteArray(), isPICSendBroadcast)
    }

    /**
     * 保存到SD卡
     *
     * @param filename           文件名称(带扩展名)
     * @param filePath           存储目录路径
     * @param filecontent        存储内容
     * @param isPICSendBroadcast 是否需要发送文件更新广播
     */
    fun saveToSDCard(context: Context, filename: String, filePath: String, filecontent: ByteArray, isPICSendBroadcast: Boolean): Boolean {
        try {
            val file = File(filePath, filename)
            if (file.exists()) {
                file.delete()
            }
            val outStream = FileOutputStream(file)
            outStream.write(filecontent)
            IOUtil.closeQuietly(outStream)
            if (isPICSendBroadcast) {
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)//发送更新图片信息广播
                val uri = Uri.fromFile(file)
                intent.data = uri
                context.sendBroadcast(intent)
            }
            return true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return false
        } catch (e: IOException) {
            e.printStackTrace()
            return false
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
    fun saveToSDCard(context: Context, filename: String, filePath: String, filecontent: File, isPICSendBroadcast: Boolean): Boolean {
        try {
            var bytesum = 0
            var byteread: Int
            val file = File(filePath, filename)
            if (file.exists()) {
                file.delete()
            }
            val inStream = FileInputStream(filecontent) //读入原文件
            val outStream = FileOutputStream(file)
            val buffer = ByteArray(1024)
            while (inStream.read(buffer).also { byteread = it } != -1) {
                bytesum += byteread
                outStream.write(buffer, 0, byteread)
            }
            IOUtil.closeQuietly(inStream, outStream)
            if (isPICSendBroadcast) {
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)//发送更新图片信息广播
                val uri = Uri.fromFile(file)
                intent.data = uri
                context.sendBroadcast(intent)
            }
            return true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
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
    @Synchronized
    fun saveText(filePath: String, fileName: String, content: String, append: Boolean) {
        ThreadPoolHelp.execute {
            val file = File(filePath, fileName)
            if (!append && file.exists()) {
                file.delete()
            }
            val encode = Base64.encode(content.toByteArray(), Base64.NO_WRAP)
            val s1 = String(encode)
            //Write the file to disk
            var writer: OutputStreamWriter? = null
            var out: OutputStream? = null
            try {
                out = FileOutputStream(file, append)
                writer = OutputStreamWriter(out, "UTF-8")
                writer.write(s1)
                writer.flush()
                IOUtil.closeQuietly(writer)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                writer?.run {
                    IOUtil.closeQuietly(this)
                }
                out?.run {
                    IOUtil.closeQuietly(this)
                }
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
    fun getText(filePath: String, fileName: String): String {
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
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                reader?.run {
                    IOUtil.closeQuietly(this)
                }
                input?.run {
                    IOUtil.closeQuietly(this)
                }
            }
        }
        val s = jsonString.toString()
        val decode = Base64.decode(s, Base64.NO_WRAP)
        return if (null == decode || decode.size == 0) {
            ""
        } else {
            String(decode)
        }
    }

    /**
     * 从指定文件获取文本并解密（异步）
     *
     * @param filePath
     * @param fileName
     */
    fun getTextAsyn(filePath: String, fileName: String): String? {
        val future = ThreadPoolHelp.submit { getText(filePath, fileName) }
        try {
            return future.get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
