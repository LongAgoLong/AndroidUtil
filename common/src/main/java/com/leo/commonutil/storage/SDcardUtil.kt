package com.leo.commonutil.storage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.util.Base64
import com.leo.commonutil.asyn.threadPool.ThreadPoolHelp
import com.leo.system.ContextHelp
import java.io.*

/**
 * @author Ari
 */
object SDcardUtil {
    /**
     * SD卡是否存在
     */
    val isDiskExists: Boolean
        get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    /**
     * SD卡根路径
     * /storage/emulated/0
     */
    val diskFolder: File?
        get() {
            return if (isDiskExists) {
                Environment.getExternalStorageDirectory()
            } else {
                null
            }
        }

    /**
     * 应用分区存储专有目录-会随着应用卸载而删除
     * /storage/emulated/0/Android/data/{packageName}/files
     * android Q 之后会强制使用分区存储
     */
    val fileFolder: File?
        get() = ContextHelp.context.getExternalFilesDir("")


    /**
     * SD卡剩余存储空间
     */
    val freeSpaceSize: Long
        get() {
            val root = diskFolder
            root ?: return 0
            val stat = StatFs(root.path)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) stat.availableBlocksLong
            else stat.availableBlocks.toLong()
        }

    /**
     * 保存到SD卡
     *
     * @param filePath           存储目录路径
     * @param filename           文件名称(带扩展名)
     * @param fileContent        存储内容
     * @param isPICSendBroadcast 是否需要发送文件更新广播
     */
    @JvmOverloads
    fun write(context: Context, filePath: String = fileFolder!!.absolutePath, filename: String,
              fileContent: String, isPICSendBroadcast: Boolean = false): Boolean {
        return write(context, filePath, filename, fileContent.toByteArray(), isPICSendBroadcast)
    }

    /**
     * 保存到SD卡
     *
     * @param filePath           存储目录路径
     * @param filename           文件名称(带扩展名)
     * @param fileContent        存储内容
     * @param isPICSendBroadcast 是否需要发送文件更新广播
     */
    @JvmOverloads
    fun write(context: Context, filePath: String = fileFolder!!.absolutePath, filename: String,
              fileContent: ByteArray, isPICSendBroadcast: Boolean = false): Boolean {
        try {
            val file = File(filePath, filename)
            if (file.exists()) {
                file.delete()
            }
            val outStream = FileOutputStream(file)
            outStream.write(fileContent)
            IOUtil.closeQuietly(outStream)
            if (isPICSendBroadcast) {
                // 发送更新图片信息广播
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                val uri = Uri.fromFile(file)
                intent.data = uri
                context.sendBroadcast(intent)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false

    }

    /**
     * 保存到SD卡
     *
     * @param filePath           存储目录路径
     * @param filename           文件名称(带扩展名)
     * @param fileContent        存储内容
     * @param isPICSendBroadcast 是否需要发送文件更新广播
     */
    @JvmOverloads
    fun write(context: Context, filePath: String = fileFolder!!.absolutePath, filename: String,
              fileContent: File, isPICSendBroadcast: Boolean = false): Boolean {
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
            IOUtil.closeQuietly(inStream, outStream)
            if (isPICSendBroadcast) {
                // 发送更新图片信息广播
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                val uri = Uri.fromFile(file)
                intent.data = uri
                context.sendBroadcast(intent)
            }
            return true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inStream?.let { IOUtil.closeQuietly(it) }
            outStream?.let { IOUtil.closeQuietly(it) }
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
    fun writeText(filePath: String = fileFolder!!.absolutePath, fileName: String, content: String,
                  append: Boolean = false) {
        ThreadPoolHelp.execute {
            val file = File(filePath, fileName)
            if (!append && file.exists()) {
                file.delete()
            }
            val encode = Base64.encode(content.toByteArray(), Base64.NO_WRAP)
            val s1 = String(encode)
            // Write the file to disk
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
                writer?.let { IOUtil.closeQuietly(it) }
                out?.let { IOUtil.closeQuietly(it) }
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
    fun getText(filePath: String = fileFolder!!.absolutePath, fileName: String): String {
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
                reader?.let { IOUtil.closeQuietly(it) }
                input?.let { IOUtil.closeQuietly(it) }
            }
        }
        val s = jsonString.toString()
        val decode = Base64.decode(s, Base64.NO_WRAP)
        return if (null == decode || decode.isEmpty()) {
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
    @JvmOverloads
    fun getTextAsyn(filePath: String = fileFolder!!.absolutePath, fileName: String): String? {
        val future = ThreadPoolHelp.submit { getText(filePath, fileName) }
        try {
            return future.get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
