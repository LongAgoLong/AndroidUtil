package com.leo.commonutil.app


import com.leo.commonutil.enume.DataDecimal

import java.text.DecimalFormat

object FormatUtil {
    private const val THOUSAND = 1000
    private const val UNIT_THOUSAND = "千"
    private const val TEN_THOUSAND = 10000
    private const val UNIT_TEN_THOUSAND = "万"

    /**
     * 格式化数字
     *
     * @param o
     * @param format
     * @return
     */
    fun formatUnit(o: Any, @DataDecimal format: String): String {
        try {
            val df = DecimalFormat(format)
            when (o) {
                is Int -> return when {
                    o > TEN_THOUSAND -> {
                        val result = o.toFloat() / TEN_THOUSAND
                        df.format(result.toDouble()) + UNIT_TEN_THOUSAND
                    }
                    o > THOUSAND -> {
                        val result = o.toFloat() / THOUSAND
                        df.format(result.toDouble()) + UNIT_THOUSAND
                    }
                    else -> o.toFloat().toString()
                }
                is Long -> {
                    val data = o.toFloat()
                    return when {
                        data > TEN_THOUSAND -> {
                            val result = data / TEN_THOUSAND
                            df.format(result.toDouble()) + UNIT_TEN_THOUSAND
                        }
                        data > THOUSAND -> {
                            val result = data / THOUSAND
                            df.format(result.toDouble()) + UNIT_THOUSAND
                        }
                        else -> data.toString()
                    }
                }
                is Double -> return when {
                    o > TEN_THOUSAND -> {
                        val result = o / TEN_THOUSAND
                        df.format(result) + UNIT_TEN_THOUSAND
                    }
                    o > THOUSAND -> {
                        val result = o / THOUSAND
                        df.format(result) + UNIT_THOUSAND
                    }
                    else -> o.toString()
                }
                is Float -> return when {
                    o > TEN_THOUSAND -> {
                        val result = (o / TEN_THOUSAND).toDouble()
                        df.format(result) + UNIT_TEN_THOUSAND
                    }
                    o > THOUSAND -> {
                        val result = (o / THOUSAND).toDouble()
                        df.format(result) + UNIT_THOUSAND
                    }
                    else -> o.toString()
                }
                is Short -> {
                    val data = o.toFloat()
                    return when {
                        data > TEN_THOUSAND -> {
                            val result = (data / TEN_THOUSAND).toDouble()
                            df.format(result) + UNIT_TEN_THOUSAND
                        }
                        data > THOUSAND -> {
                            val result = (data / THOUSAND).toDouble()
                            df.format(result) + UNIT_THOUSAND
                        }
                        else -> data.toString()
                    }
                }
                is String -> {
                    val data = java.lang.Float.valueOf(o)
                    return when {
                        data > TEN_THOUSAND -> {
                            val result = data / TEN_THOUSAND
                            df.format(result.toDouble()) + UNIT_TEN_THOUSAND
                        }
                        data > THOUSAND -> {
                            val result = data / THOUSAND
                            df.format(result.toDouble()) + UNIT_THOUSAND
                        }
                        else -> o
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 格式化-小数位数
     *
     * @param o
     * @param format
     * @return
     */
    fun formatDecimal(o: Any, @DataDecimal format: String): String {
        val df = DecimalFormat(format)
        try {
            return when (o) {
                is Int -> df.format(o.toLong())
                is Long -> df.format(o)
                is Double -> df.format(o)
                is Float -> df.format(o.toDouble())
                is Short -> df.format(o.toLong())
                is String -> {
                    val data = java.lang.Float.valueOf(o)
                    df.format(data.toDouble())
                }
                else -> ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}
