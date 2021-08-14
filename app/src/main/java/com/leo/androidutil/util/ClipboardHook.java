package com.leo.androidutil.util;

import android.content.ClipData;
import android.content.Context;
import android.os.IBinder;
import android.util.Log;

import com.leo.system.hook.ServiceHook;
import com.leo.system.hook.ServiceManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ClipboardHook {

    private static final String TAG = ClipboardHook.class.getSimpleName();

    public static void hookService(Context context) {
        IBinder clipboardService = ServiceManager.getService(Context.CLIPBOARD_SERVICE);
        String IClipboard = "android.content.IClipboard";

        if (clipboardService != null) {
            IBinder hookClipboardService =
                    (IBinder) Proxy.newProxyInstance(clipboardService.getClass().getClassLoader(),
                            clipboardService.getClass().getInterfaces(),
                            new ServiceHook(clipboardService, IClipboard, true, new ClipboardHookHandler()));
            ServiceManager.setService(Context.CLIPBOARD_SERVICE, hookClipboardService);
        } else {
            Log.e(TAG, "ClipboardService hook failed!");
        }
    }

    public static class ClipboardHookHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            int argsLength = args.length;
            if ("setPrimaryClip".equals(methodName)) {
                if (argsLength >= 2 && args[0] instanceof ClipData) {
                    ClipData data = (ClipData) args[0];
                    String text = data.getItemAt(0).getText().toString();
                    text += "\n分享自：xxx";
                    args[0] = ClipData.newPlainText(data.getDescription().getLabel(), text);
                }
            }
            return method.invoke(proxy, args);
        }
    }
}
