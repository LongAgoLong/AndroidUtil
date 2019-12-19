package com.leo.androidutil.ui

import android.os.Bundle
import android.view.KeyEvent
import androidx.databinding.DataBindingUtil
import com.leo.androidutil.R
import com.leo.androidutil.databinding.ActivityPinyinBinding
import com.leo.pinyinlib.PinyinHelp

/**
 * Created by LEO
 * On 2019/7/28
 * Description:汉字转拼音
 */
class PinyinActivity : BaseActivity() {
    private lateinit var mBinding: ActivityPinyinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pinyin)
        initView()
    }

    private fun initView() {
        mBinding.searchStrEt?.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                mBinding.searchStrEt?.run {
                    val s = text.toString().trim()
                    if (s.isNotEmpty()) {
                        mBinding.resultTv?.run {
                            text = PinyinHelp.getInstance().parsePinyin(s)
                            append("\n")
                            append(PinyinHelp.getInstance().parsePinyinAllFirstLetter(s))
                            append("\n")
                            append(PinyinHelp.getInstance().parsePinyinFirstLetter(s))
                        }
                    }
                }
                true
            }
            false
        }
    }
}
