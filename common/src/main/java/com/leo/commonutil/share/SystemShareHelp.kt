package com.leo.commonutil.share

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import androidx.core.content.FileProvider
import com.leo.commonutil.app.AppStackManager
import java.io.File
import java.util.*

object SystemShareHelp {
    /**
     * 分享文件
     */
    fun shareFile(title: String, filePath: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.addCategory("android.intent.category.DEFAULT")
        val pdfUri = Uri.fromFile(File(filePath))
        intent.putExtra(Intent.EXTRA_STREAM, pdfUri)
        intent.type = "application/pdf"
        try {
            AppStackManager.getInstance().currentActivity!!
                    .startActivity(Intent.createChooser(intent, title))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 分享文字
     * @param shareText
     */
    fun shareText(shareText: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        shareIntent.type = "text/plain"
        try {
            AppStackManager.getInstance().currentActivity!!
                    .startActivity(Intent.createChooser(shareIntent, "分享到"))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 分享单张图片
     *
     * @param imagePath
     */
    fun shareImage(imagePath: String) {
        try {
            val activity = AppStackManager.getInstance().currentActivity
            //由文件得到uri
            val imageUri: Uri
            val imgFile = File(imagePath)
            //判断当前手机版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                imageUri = FileProvider.getUriForFile(activity!!, activity.packageName + ".fileprovider", imgFile)
            } else {
                imageUri = Uri.fromFile(imgFile)
            }
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            shareIntent.type = "image/*"
            activity!!
                    .startActivity(Intent.createChooser(shareIntent, "分享到"))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 分享多张图片
     *
     * @param imgPaths
     */
    fun shareImages(imgPaths: ArrayList<String>) {
        try {
            val activity = AppStackManager.getInstance().currentActivity
            val uriList = ArrayList<Uri>()
            for (img in imgPaths) {
                if (!TextUtils.isEmpty(img)) {
                    val imageUri: Uri
                    val imgFile = File(img)
                    //判断当前手机版本
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        imageUri = FileProvider.getUriForFile(activity!!, activity.packageName + ".fileprovider", imgFile)
                    } else {
                        imageUri = Uri.fromFile(imgFile)
                    }
                    uriList.add(imageUri)
                }
            }
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND_MULTIPLE
            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList)
            shareIntent.type = "image/*"
            activity!!.startActivity(Intent.createChooser(shareIntent, "分享到"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
