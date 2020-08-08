package com.leo.commonutil.enume;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Target
 * 用于描述注解的使用范围（即：被描述的注解可以用在什么地方）
 * 1.CONSTRUCTOR:用于描述构造器
 * 2.FIELD:用于描述域
 * 3.LOCAL_VARIABLE:用于描述局部变量
 * 4.METHOD:用于描述方法
 * 5.PACKAGE:用于描述包
 * 6.PARAMETER:用于描述参数
 * 7.TYPE:用于描述类、接口(包括注解类型) 或enum声明
 */
@IntDef({
        UnitWeek.SUNDAY,
        UnitWeek.MONDAY,
        UnitWeek.TUESDAY,
        UnitWeek.WEDNESDAY,
        UnitWeek.THURSDAY,
        UnitWeek.FRIDAY,
        UnitWeek.SATURDAY,
})
@Target({ElementType.PARAMETER, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
public @interface UnitWeek {
    int SUNDAY = 0;
    int MONDAY = 1;
    int TUESDAY = 2;
    int WEDNESDAY = 3;
    int THURSDAY = 4;
    int FRIDAY = 5;
    int SATURDAY = 6;
}
