package com.leo.commonutil.enume;

import androidx.annotation.StringDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@StringDef({
        DataDecimal.ONE_DECIMAL,
        DataDecimal.TWO_DECIMAL,
        DataDecimal.THREE_DECIMAL,
        DataDecimal.FOUR_DECIMAL,
        DataDecimal.FIVE_DECIMAL,
        DataDecimal.SIX_DECIMAL
})
@Target({ElementType.PARAMETER, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
public @interface DataDecimal {
    String ONE_DECIMAL = "#.0";
    String TWO_DECIMAL = "#.00";
    String THREE_DECIMAL = "#.000";
    String FOUR_DECIMAL = "#.0000";
    String FIVE_DECIMAL = "#.00000";
    String SIX_DECIMAL = "#.000000";
}
