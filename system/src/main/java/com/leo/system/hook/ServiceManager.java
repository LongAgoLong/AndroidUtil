package com.leo.system.hook;

import android.os.IBinder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Description:
 *
 * @author Shawn_Dut
 * @since 2017-02-21
 */
public class ServiceManager {

    private static Method sGetServiceMethod;
    private static Map<String, IBinder> sCacheService;
    private static Class<?> cServiceManager;

    static {
        try {
            cServiceManager = Class.forName("android.os.ServiceManager");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static IBinder getService(String serviceName) {
        if (cServiceManager == null) {
            return null;
        }

        if (sGetServiceMethod == null) {
            try {
                sGetServiceMethod = cServiceManager.getDeclaredMethod("getService", String.class);
                sGetServiceMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        if (sGetServiceMethod != null) {
            try {
                return (IBinder) sGetServiceMethod.invoke(null, serviceName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static void setService(String serviceName, IBinder service) {
        if (cServiceManager == null) {
            return;
        }

        if (sCacheService == null) {
            try {
                Field sCache = cServiceManager.getDeclaredField("sCache");
                sCache.setAccessible(true);
                sCacheService = (Map<String, IBinder>) sCache.get(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (sCacheService != null) {
            sCacheService.remove(serviceName);
            sCacheService.put(serviceName, service);
        }
    }
}
