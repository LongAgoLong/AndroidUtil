package com.leo.commonutil.app;


import com.leo.commonutil.enume.DataDecimal;

import java.text.DecimalFormat;

public final class FormatUtil {
    private static final int THOUSAND = 1000;
    private static final String UNIT_THOUSAND = "千";
    private static final int TEN_THOUSAND = 10000;
    private static final String UNIT_TEN_THOUSAND = "万";

    private FormatUtil() {
    }

    /**
     * 格式化数字
     *
     * @param o
     * @param format
     * @return
     */
    public static String formatUnit(Object o, @DataDecimal String format) {
        try {
            DecimalFormat df = new DecimalFormat(format);
            if (o instanceof Integer) {
                int i = (int) o;
                if (i > TEN_THOUSAND) {
                    float result = (float) i / TEN_THOUSAND;
                    return df.format(result) + UNIT_TEN_THOUSAND;
                } else if (i > THOUSAND) {
                    float result = (float) i / THOUSAND;
                    return df.format(result) + UNIT_THOUSAND;
                } else {
                    return String.valueOf((float) i);
                }
            } else if (o instanceof Long) {
                Long l = (long) o;
                float data = l.floatValue();
                if (data > TEN_THOUSAND) {
                    float result = data / TEN_THOUSAND;
                    return df.format(result) + UNIT_TEN_THOUSAND;
                } else if (data > THOUSAND) {
                    float result = data / THOUSAND;
                    return df.format(result) + UNIT_THOUSAND;
                } else {
                    return String.valueOf(data);
                }
            } else if (o instanceof Double) {
                double data = (double) o;
                if (data > TEN_THOUSAND) {
                    double result = data / TEN_THOUSAND;
                    return df.format(result) + UNIT_TEN_THOUSAND;
                } else if (data > THOUSAND) {
                    double result = data / THOUSAND;
                    return df.format(result) + UNIT_THOUSAND;
                } else {
                    return String.valueOf(data);
                }
            } else if (o instanceof Float) {
                float data = (float) o;
                if (data > TEN_THOUSAND) {
                    double result = data / TEN_THOUSAND;
                    return df.format(result) + UNIT_TEN_THOUSAND;
                } else if (data > THOUSAND) {
                    double result = data / THOUSAND;
                    return df.format(result) + UNIT_THOUSAND;
                } else {
                    return String.valueOf(data);
                }
            } else if (o instanceof Short) {
                Short s = (short) o;
                float data = s.floatValue();
                if (data > TEN_THOUSAND) {
                    double result = data / TEN_THOUSAND;
                    return df.format(result) + UNIT_TEN_THOUSAND;
                } else if (data > THOUSAND) {
                    double result = data / THOUSAND;
                    return df.format(result) + UNIT_THOUSAND;
                } else {
                    return String.valueOf(data);
                }
            } else if (o instanceof String) {
                String s = (String) o;
                float data = Float.valueOf(s);
                if (data > TEN_THOUSAND) {
                    float result = data / TEN_THOUSAND;
                    return df.format(result) + UNIT_TEN_THOUSAND;
                } else if (data > THOUSAND) {
                    float result = data / THOUSAND;
                    return df.format(result) + UNIT_THOUSAND;
                } else {
                    return s;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 格式化-小数位数
     *
     * @param o
     * @param format
     * @return
     */
    public static String formatDecimal(Object o, @DataDecimal String format) {
        DecimalFormat df = new DecimalFormat(format);
        try {
            if (o instanceof Integer) {
                int data = (int) o;
                return df.format(data);
            } else if (o instanceof Long) {
                long data = (long) o;
                return df.format(data);
            } else if (o instanceof Double) {
                double data = (double) o;
                return df.format(data);
            } else if (o instanceof Float) {
                float data = (float) o;
                return df.format(data);
            } else if (o instanceof Short) {
                short data = (short) o;
                return df.format(data);
            } else if (o instanceof String) {
                String s = (String) o;
                float data = Float.valueOf(s);
                return df.format(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
