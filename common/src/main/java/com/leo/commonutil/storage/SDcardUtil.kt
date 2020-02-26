package com.leo.commonutil.storage

import android.os.Build
import android.os.Environment
import android.os.StatFs
import com.leo.system.ContextHelp
import java.io.File

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
     * 应用分区存储专有目录-会随着应用卸载而删除
     * /storage/emulated/0/Android/data/{packageName}/files/Pictures
     * android Q 之后会强制使用分区存储
     */
    val picFileFolder: File?
        get() = ContextHelp.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

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
}
