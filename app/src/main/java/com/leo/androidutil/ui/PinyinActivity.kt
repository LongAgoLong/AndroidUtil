package com.leo.androidutil.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
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
        mBinding = DataBindingUtil.setContentView(this, com.leo.androidutil.R.layout.activity_pinyin)
        initView()
    }

    override fun initActionBar() {
        super.initActionBar()
        val actionBar = supportActionBar ?: return
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.title = "拼音转换"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        mBinding.searchStrEt?.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    mBinding.searchStrEt?.run {
                        val s = text.toString().trim()
                        if (s.isEmpty()) {
                            return@run
                        }
                        mBinding.resultTv ?: return@run
                        val pinyin = PinyinHelp.getInstance().parsePinyin(s)
                        val allFirstLetter = PinyinHelp.getInstance().parsePinyinAllFirstLetter(s)
                        val firstLetter = PinyinHelp.getInstance().parsePinyinFirstLetter(s)
                        mBinding.resultTv.text = "拼音：${pinyin}\n首字母：${allFirstLetter}\n第一个文字首字母：${firstLetter}"
                    }
                }
                true
            }
            false
        }
    }
}
