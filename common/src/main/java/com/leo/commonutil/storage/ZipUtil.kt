package com.leo.commonutil.storage

import android.text.TextUtils
import java.io.*
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

/**
 * Java utils 实现的Zip工具
 *
 * @author once
 */
object ZipUtil {
    private const val BUFF_SIZE = 1024 * 1024 // 1M Byte
    @Volatile
    private var isStopZipFlag: Boolean = false

    /**
     * 批量压缩文件（夹）
     *
     * @param resFileList 要压缩的文件（夹）列表
     * @param zipFile 生成的压缩文件
     * @param comment 压缩文件的注释
     * @param zipListener    zipListener
     */
    @JvmOverloads
    fun zipFiles(resFileList: Collection<File>, zipFile: File, comment: String? = null, zipListener: ZipListener) {
        isStopZipFlag = false
        var zipout: ZipOutputStream? = null
        try {
            zipout = ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile), BUFF_SIZE))
            for (resFile in resFileList) {
                if (isStopZipFlag) {
                    if (zipFile.exists()) {
                        zipFile.delete()
                    }
                    break
                }
                zipFile(resFile, zipout, "", zipListener)
            }
            if (!TextUtils.isEmpty(comment)) {
                zipout.setComment(comment)
            }
            IOUtil.closeQuietly(zipout)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            zipout?.let { IOUtil.closeQuietly(zipout) }
        }

    }

    /**
     * 解压缩一个文件
     *
     * @param zipFile 压缩文件
     * @param folderPath 解压缩的目标目录
     */
    fun upZipFile(zipFile: File, folderPath: String) {
        val desDir = File(folderPath)
        if (!desDir.exists()) {
            desDir.mkdirs()
        }
        val zf: ZipFile?
        try {
            zf = ZipFile(zipFile)
            val entries = zf.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement() as ZipEntry
                val inputStream = zf.getInputStream(entry)
                var str = folderPath + File.separator + entry.name
                str = String(str.toByteArray(charset("8859_1")), charset("GB2312"))
                val desFile = File(str)
                if (!desFile.exists()) {
                    val fileParentDir = desFile.parentFile
                    if (!fileParentDir.exists()) {
                        fileParentDir.mkdirs()
                    }
                    desFile.createNewFile()
                }
                val outputStream = FileOutputStream(desFile)
                val buffer = ByteArray(BUFF_SIZE)
                var realLength: Int
                while ((inputStream.read(buffer).also { realLength = it }) > 0) {
                    outputStream.write(buffer, 0, realLength)
                }
                IOUtil.closeQuietly(inputStream, outputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 解压文件名包含传入文字的文件
     *
     * @param zipFile 压缩文件
     * @param folderPath 目标文件夹
     * @param nameContains 传入的文件匹配名
     * @return   返回的集合
     */
    fun upZipSelectedFile(zipFile: File, folderPath: String,
                          nameContains: String): ArrayList<File>? {

        val fileList = ArrayList<File>()

        val desDir = File(folderPath)
        if (!desDir.exists()) {
            desDir.mkdir()
        }

        var zf: ZipFile? = null
        try {
            zf = ZipFile(zipFile)
            val entries = zf.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement() as ZipEntry
                if (entry.name.contains(nameContains)) {
                    val inputStream = zf.getInputStream(entry)
                    var str = folderPath + File.separator + entry.name
                    str = String(str.toByteArray(charset("8859_1")), charset("GB2312"))
                    // str.getBytes("GB2312"),"8859_1" 输出
                    // str.getBytes("8859_1"),"GB2312" 输入
                    val desFile = File(str)
                    if (!desFile.exists()) {
                        val fileParentDir = desFile.parentFile
                        if (!fileParentDir.exists()) {
                            fileParentDir.mkdirs()
                        }
                        desFile.createNewFile()
                    }
                    val outputStream = FileOutputStream(desFile)
                    val buffer = ByteArray(BUFF_SIZE)
                    var realLength: Int
                    while (inputStream.read(buffer).also { realLength = it } > 0) {
                        outputStream.write(buffer, 0, realLength)
                    }
                    IOUtil.closeQuietly(inputStream, outputStream)
                    fileList.add(desFile)
                }
            }
            return fileList
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 获得压缩文件内文件列表
     *
     * @param zipFile 压缩文件
     * @return 压缩文件内文件名称
     */
    fun getEntriesNames(zipFile: File): ArrayList<String>? {
        val entryNames = ArrayList<String>()
        var entries: Enumeration<*>? = null
        try {
            entries = getEntriesEnumeration(zipFile)
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement() as ZipEntry
                entryNames.add(String(getEntryName(entry)!!.toByteArray(charset("GB2312")), charset("8859_1")))
            }
            return entryNames
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 获得压缩文件内压缩文件对象以取得其属性
     *
     * @param zipFile 压缩文件
     * @return 返回一个压缩文件列表
     */
    fun getEntriesEnumeration(zipFile: File): Enumeration<*> {
        var zf: ZipFile? = null
        try {
            zf = ZipFile(zipFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return zf!!.entries()

    }

    /**
     * 取得压缩文件对象的注释
     *
     * @param entry 压缩文件对象
     * @return 压缩文件对象的注释
     */
    fun getEntryComment(entry: ZipEntry): String? {
        try {
            return String(entry.comment.toByteArray(charset("GB2312")), charset("8859_1"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 取得压缩文件对象的名称
     *
     * @param entry 压缩文件对象
     * @return 压缩文件对象的名称
     */
    fun getEntryName(entry: ZipEntry): String? {
        try {
            return String(entry.name.toByteArray(charset("GB2312")), charset("8859_1"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 压缩文件
     *
     * @param resFile 需要压缩的文件（夹）
     * @param zipout 压缩的目的文件
     * @param rootpath 压缩的文件路径
     */
    private fun zipFile(resFile: File, zipout: ZipOutputStream, rootpath: String, zipListener: ZipListener) {
        var rootpath = rootpath
        try {
            rootpath = (rootpath + (if (rootpath.trim { it <= ' ' }.length == 0) "" else File.separator)
                    + resFile.name)
            rootpath = String(rootpath.toByteArray(charset("8859_1")), charset("GB2312"))
            if (resFile.isDirectory) {
                val fileList = resFile.listFiles()
                val length = fileList.size
                // Log.e("zipprogress", (int)((1 / (float) (length+1))*100)+"%");
                zipListener.zipProgress((1 / (length + 1).toFloat() * 100).toInt())
                for (i in 0 until length) {
                    if (isStopZipFlag) {
                        break
                    }
                    val file = fileList[i]
                    zipFile(file, zipout, rootpath, zipListener)
                    // Log.e("zipprogress", (int)(((i+2) / (float) (length+1))*100)+"%");
                    zipListener.zipProgress(((i + 2) / (length + 1).toFloat() * 100).toInt())
                }
            } else {
                val buffer = ByteArray(BUFF_SIZE)
                val inputStream = BufferedInputStream(FileInputStream(resFile),
                        BUFF_SIZE)
                zipout.putNextEntry(ZipEntry(rootpath))
                var realLength: Int
                while (inputStream.read(buffer).also { realLength = it } != -1) {
                    if (isStopZipFlag) {
                        break
                    }
                    zipout.write(buffer, 0, realLength)
                }
                IOUtil.closeQuietly(inputStream)
                zipout.flush()
                zipout.closeEntry()
            }
        } catch (e: Exception) {

        }

    }

    interface ZipListener {
        fun zipProgress(zipProgress: Int)
    }
}