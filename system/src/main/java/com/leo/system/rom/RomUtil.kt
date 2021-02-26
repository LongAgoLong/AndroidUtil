package com.leo.system.rom

import com.leo.system.rom.RomTarget
import com.leo.system.rom.BuildProperties
import com.leo.system.rom.RomUtil
import java.io.IOException

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

    /**
     * 华为rom
     *
     * @return
     */
    private val isEMUI: Boolean
        get() = try {
            val prop = BuildProperties.newInstance()
            prop.containsKey(KEY_EMUI_API_LEVEL) || prop.containsKey(KEY_EMUI_VERSION_CODE)
        } catch (e: IOException) {
            false
        }.also {
            if (it) {
                mRomTarget = RomTarget.EMUI
            }
        }

    /**
     * 小米rom
     *
     * @return
     */
    private val isMIUI: Boolean
        get() = try {
            val prop = BuildProperties.newInstance()
            (prop.containsKey(KEY_MIUI_VERSION_CODE)
                    || prop.containsKey(KEY_MIUI_VERSION_NAME)
                    || prop.containsKey(KEY_MIUI_REAL_BLUR)
                    || prop.containsKey(KEY_MIUI_HANDY_MODE_SF))
        } catch (e: IOException) {
            false
        }.also {
            if (it) {
                mRomTarget = RomTarget.MIUI
            }
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

    fun rom(): RomTarget? {
        if (mRomTarget != null) return mRomTarget
        synchronized(this) {
            if (isEMUI || isMIUI || isFlyme) {
                return mRomTarget
            }
            mRomTarget = RomTarget.OTHER
        }
        return mRomTarget
    }
}