package com.leo.system.rom

import android.os.Build
import java.io.IOException
import java.util.*

/**
 * Created by LEO
 * on 2017/7/3.
 */
object RomUtil {
    private var mRomTarget: RomTarget? = null
    private const val KEY_EMUI_VERSION_CODE = "ro.build.version.emui"
    private const val KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level"
    private const val KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code"

    private const val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
    private const val KEY_MIUI_HANDY_MODE_SF = "ro.miui.has_handy_mode_sf"
    private const val KEY_MIUI_REAL_BLUR = "ro.miui.has_real_blur"

    private const val KEY_FLYME_ICON = "persist.sys.use.flyme.icon"
    private const val KEY_FLYME_PUBLISHED = "ro.flyme.published"
    private const val KEY_FLYME_FLYME = "ro.meizu.setupwizard.flyme"

    private const val KEY_OPPO = "ro.build.version.opporom"

    private const val KEY_VIVO = "ro.vivo.os.version"

    /**
     * 华为rom
     *
     * @return
     */
    private val isEMUI: Boolean
        get() = try {
            val prop = BuildProperties.newInstance()
            isEMUIByVendor()
                    || prop.containsKey(KEY_EMUI_API_LEVEL)
                    || prop.containsKey(KEY_EMUI_VERSION_CODE)
        } catch (e: IOException) {
            false
        }.also {
            if (it) {
                mRomTarget = RomTarget.EMUI
            }
        }

    private fun isEMUIByVendor(): Boolean {
        val vendor = Build.MANUFACTURER
        val vendorUpperCase = vendor.toUpperCase(Locale.ENGLISH)
        return vendorUpperCase.contains("HUAWEI")
    }

    /**
     * 小米rom
     *
     * @return
     */
    private val isMIUI: Boolean
        get() = try {
            val prop = BuildProperties.newInstance()
            isMIUIByVendor()
                    || prop.containsKey(KEY_MIUI_VERSION_CODE)
                    || prop.containsKey(KEY_MIUI_VERSION_NAME)
                    || prop.containsKey(KEY_MIUI_REAL_BLUR)
                    || prop.containsKey(KEY_MIUI_HANDY_MODE_SF)
        } catch (e: IOException) {
            false
        }.also {
            if (it) {
                mRomTarget = RomTarget.MIUI
            }
        }

    private fun isMIUIByVendor(): Boolean {
        val vendor = Build.MANUFACTURER
        val vendorUpperCase = vendor.toUpperCase(Locale.ENGLISH)
        return vendorUpperCase.contains("XIAOMI")
    }

    /**
     * 魅族rom
     *
     * @return
     */
    private val isFlyme: Boolean
        get() = try {
            val prop = BuildProperties.newInstance()
            (prop.containsKey(KEY_FLYME_ICON)
                    || prop.containsKey(KEY_FLYME_PUBLISHED)
                    || prop.containsKey(KEY_FLYME_FLYME))
        } catch (e: IOException) {
            false
        }.also {
            if (it) {
                mRomTarget = RomTarget.FLYME
            }
        }

    private val isOPPO: Boolean
        get() = try {
            val prop = BuildProperties.newInstance()
            prop.containsKey(KEY_OPPO)
        } catch (e: IOException) {
            false
        }.also {
            if (it) {
                mRomTarget = RomTarget.OPPO
            }
        }

    private val isVIVO: Boolean
        get() = try {
            val prop = BuildProperties.newInstance()
            prop.containsKey(KEY_VIVO)
        } catch (e: IOException) {
            false
        }.also {
            if (it) {
                mRomTarget = RomTarget.VIVO
            }
        }

    fun rom(): RomTarget? {
        if (mRomTarget != null) return mRomTarget
        synchronized(this) {
            if (isEMUI || isMIUI || isFlyme || isOPPO || isVIVO) {
                return mRomTarget
            }
            mRomTarget = RomTarget.OTHER
        }
        return mRomTarget
    }
}