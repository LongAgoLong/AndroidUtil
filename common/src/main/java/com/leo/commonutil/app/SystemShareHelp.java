package com.leo.commonutil.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;

public class SystemShareHelp {
    /**
     * 分享文件
     */
    public static void shareFile(@NonNull String title, @NonNull String filePath) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.addCategory("android.intent.category.DEFAULT");
        Uri pdfUri;
        pdfUri = Uri.fromFile(new File(filePath));
        intent.putExtra(Intent.EXTRA_STREAM, pdfUri);
        intent.setType("application/pdf");
        try {
            AppStackManager.getInstance().getCurrentActivity()
                    .startActivity(Intent.createChooser(intent, title));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //分享文字
    public static void shareText(@NonNull String shareText) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.setType("text/plain");
        try {
            AppStackManager.getInstance().getCurrentActivity()
                    .startActivity(Intent.createChooser(shareIntent, "分享到"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享单张图片
     *
     * @param imagePath
     */
    public static void shareImage(@NonNull String imagePath) {
        try {
            Activity activity = AppStackManager.getInstance().getCurrentActivity();
            //由文件得到uri
            Uri imageUri;
            File imgFile = new File(imagePath);
            //判断当前手机版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                imageUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", imgFile);
            } else {
                imageUri = Uri.fromFile(imgFile);
            }
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/*");
            activity
                    .startActivity(Intent.createChooser(shareIntent, "分享到"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享多张图片
     *
     * @param imgPaths
     */
    public static void shareImages(@NonNull ArrayList<String> imgPaths) {
        try {
            Activity activity = AppStackManager.getInstance().getCurrentActivity();
            ArrayList<Uri> uriList = new ArrayList<>();
            for (String img : imgPaths) {
                if (!TextUtils.isEmpty(img)) {
                    Uri imageUri;
                    File imgFile = new File(img);
                    //判断当前手机版本
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        imageUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", imgFile);
                    } else {
                        imageUri = Uri.fromFile(imgFile);
                    }
                    uriList.add(imageUri);
                }
            }
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
            shareIntent.setType("image/*");
            activity
                    .startActivity(Intent.createChooser(shareIntent, "分享到"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
