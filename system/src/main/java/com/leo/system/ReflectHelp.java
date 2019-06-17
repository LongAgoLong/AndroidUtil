package com.leo.system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by LEO
 * On 2019/6/16
 * Description:反射工具类
 */
public final class ReflectHelp {
    private ReflectHelp() {
    }

    /**
     * 通过反射获取实例对象
     *
     * @param className
     * @return
     * @throws Exception
     */
    public static Class<?> reflexObject(@NonNull String className) throws Exception {
        //转义指定类型class对象
        //反射获取实例对象
        Class<?> c = Class.forName(className);
        return c;
    }

    /**
     * 通过反射获取实例对象的指定方法对象
     * 调用方通过invoke方法使用，如果方法有返回值，就返回值，没有返回值，比如void就返回null
     * Object invoke = ctd.invoke(实例对象obj,方法参数等);
     */
    public static Method reflexMethod(Class<?> cls, @NonNull String methodName, @Nullable Class<?>... parameterTypes) throws Exception {
        return cls.getMethod(methodName, parameterTypes);
    }

    /**
     * 反射调用方法
     *
     * @param className      完整路径的类名
     * @param methodName     方法名
     * @param key
     * @param defaultValue   默认值
     * @param parameterTypes 方法传参类型
     * @param <T>
     * @return
     */
    public static <T> T excuteMethod(@NonNull String className, @NonNull String methodName, @NonNull String key, T defaultValue, @Nullable Class<?>... parameterTypes) {
        try {
            Class<?> cls = reflexObject(className);
            Method method = reflexMethod(cls, methodName, parameterTypes);
            return (T) method.invoke(cls, key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    /**
     * 通过反射修改实例对象的私有属性值
     *
     * @param cls
     * @param field
     * @param fieldValue
     * @param obj
     * @throws Exception
     */
    public static void reflexProperty(Class cls, String field, Object fieldValue, Object obj) {
        try {
            //获取字段对象
            Field fieldObj = cls.getDeclaredField(field);
            //设置私有属性可以修改
            fieldObj.setAccessible(true);
            //修改私有属性值
            fieldObj.set(obj, fieldValue);
            fieldObj.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
