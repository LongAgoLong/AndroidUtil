package com.leo.commonutil.app

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.text.DecimalFormat
import java.util.regex.Pattern


/**
 * Created by LEO
 * on 2017/4/21.
 * uri获取文件路径
 */
object FileUtils {
    fun getPath(context: Context, uri: Uri): String? {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(
                context,
                uri
            )
        ) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), id.toLong()
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                // MediaProvider
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if (ContentResolver.SCHEME_CONTENT.equals(uri.scheme, ignoreCase = true)) {
            // MediaStore (and general)
            return getDataColumn(context, uri, null, null)
        } else if (ContentResolver.SCHEME_FILE.equals(uri.scheme, ignoreCase = true)) {
            // File
            return uri.path
        }
        return null
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    fun getDataColumn(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String {
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(
                uri!!, arrayOf(MediaStore.Images.ImageColumns.DATA),
                selection, selectionArgs, null
            )
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    return cursor.getString(index)
                }
                cursor.close()
            }
        } finally {
            cursor?.close()
        }
        return ""
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * 获取指定文件大小
     */
    fun getFileSize(file: File): Long {
        var size: Long = 0
        if (file.exists()) {
            try {
                val fis = FileInputStream(file)
                size = fis.available().toLong()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return size
    }

    /**
     * 格式化文件大小
     *
     * @param fileS
     * @return
     */
    fun formatFileSize(fileS: Long): String { //转换文件大小
        val df = DecimalFormat("#.00")
        var fileSizeString = ""
        fileSizeString = if (fileS < 1024) {
            df.format(fileS.toDouble()) + "B"
        } else if (fileS < 1048576) {
            df.format(fileS.toDouble() / 1024) + "K"
        } else if (fileS < 1073741824) {
            df.format(fileS.toDouble() / 1048576) + "M"
        } else {
            df.format(fileS.toDouble() / 1073741824) + "G"
        }
        return fileSizeString
    }

    /**
     * 获取MIME类型
     *
     * @param url file path or whatever suitable URL you want.
     * @return
     */
    fun getMimeType(url: String?): String? {
        if (!TextUtils.isEmpty(url)) {
            val extension = MimeTypeMap.getFileExtensionFromUrl(url)
            if (extension != null) {
                return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            }
        }
        return null
    }

    /**
     * @param url file path or whatever suitable URL you want.
     * @return 获取文件名(带扩展名)
     */
    fun getFileName(url: String): String? {
        var url = url
        if (!TextUtils.isEmpty(url)) {
            val fragment = url.lastIndexOf('#')
            if (fragment > 0) {
                url = url.substring(0, fragment)
            }
            val query = url.lastIndexOf('?')
            if (query > 0) {
                url = url.substring(0, query)
            }

            /*多加一步!匹配腾讯云压缩图片带参数链接*/
            val param = url.lastIndexOf('!')
            if (param > 0) {
                url = url.substring(0, param)
            }
            val filenamePos = url.lastIndexOf('/')
            val filename = if (0 <= filenamePos) url.substring(filenamePos + 1) else url
            if (!TextUtils.isEmpty(filename)) {
                return filename
            }
        }
        return null
    }

    /**
     * @param url file path or whatever suitable URL you want.
     * @return 获取文件名(不带扩展名)
     */
    fun getFileNameNoExtension(url: String): String? {
        val fileName = getFileName(url)
        if (!TextUtils.isEmpty(fileName)) {
            val dotPos = fileName!!.lastIndexOf('.')
            if (-1 != dotPos) {
                return fileName.substring(0, dotPos)
            }
        }
        return null
    }

    /**
     * @param url file path or whatever suitable URL you want.
     * @return 获取文件扩展名
     */
    fun getFileExtension(url: String): String? {
        val fileName = getFileName(url)
        if (!TextUtils.isEmpty(fileName)) {
            // if the filename contains special characters, we don't
            // consider it valid for our matching purposes:
            if (Pattern.matches("[a-zA-Z_0-9\\.\\-\\(\\)\\%]+", fileName)) {
                val dotPos = fileName!!.lastIndexOf('.')
                if (0 <= dotPos) {
                    return fileName.substring(dotPos + 1)
                }
            }
        }
        return null
    }

    fun getUri(context: Context, file: File): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } else {
            Uri.fromFile(file)
        }
    }
}