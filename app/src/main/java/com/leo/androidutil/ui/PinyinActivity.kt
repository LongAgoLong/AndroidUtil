package com.leo.androidutil.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.leo.androidutil.databinding.ActivityPinyinBinding
import com.leo.pinyinlib.PinyinHelp
import com.leo.system.LogUtil


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

    private fun initView() {
        mBinding.searchStrEt?.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER
                    && event.action == KeyEvent.ACTION_DOWN) {
                mBinding.searchStrEt?.run {
                    var s = text.toString().trim()
                    if (s.isEmpty()) {
                        return@run
                    }
//                    s = s.replace(Regex("[`~!@#\$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]+"), "")
                    /**
                     * 利用正则移除所有标点符号
                     * 小写 p 是 property 的意思，表示 Unicode 属性，用于 Unicode 正表达式的前缀
                     * 大写表示 Unicode 字符集七个字符属性之一：
                     * P：标点字符；
                     * L：字母；
                     * M：标记符号（一般不会单独出现）；
                     * Z：分隔符（比如空格、换行等）；
                     * S：符号（比如数学符号、货币符号等）；
                     * N：数字（比如阿拉伯数字、罗马数字等）；
                     * C：其他字符
                     */
                    s = s.replace(Regex("[\\p{P}\\s]+"), "")
                    LogUtil.d(TAG, "纠正后字符串 : $s")
                    mBinding.resultTv ?: return@run
                    val pinyin = PinyinHelp.getInstance().parsePinyin(s)
                    val allFirstLetter = PinyinHelp.getInstance().parsePinyinAllFirstLetter(s)
                    val firstLetter = PinyinHelp.getInstance().parsePinyinFirstLetter(s)
                    val result = "拼音：${pinyin}\n首字母：${allFirstLetter}\n第一个文字首字母：${firstLetter}"
                    mBinding.resultTv.text = result
                }
                true
            }
            false
        }
    }
}
