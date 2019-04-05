package com.leo.commonutil.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;

public class ShareUtil {
    /**
     * 分享
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

    public static void shareText(@NonNull String title, @NonNull String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        try {
            AppStackManager.getInstance().getCurrentActivity()
                    .startActivity(Intent.createChooser(intent, title));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
