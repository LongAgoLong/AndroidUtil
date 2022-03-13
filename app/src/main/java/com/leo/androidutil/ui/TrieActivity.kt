package com.leo.androidutil.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.leo.androidutil.R
import com.leo.androidutil.databinding.ActivityTrieBinding
import com.leo.commonutil.asyn.threadPool.ThreadPoolHelp
import com.leo.trie.TrieHelper

class TrieActivity : BaseActivity() {
    private lateinit var mBinding: ActivityTrieBinding
    private lateinit var trieHelp: TrieHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trieHelp = TrieHelper()

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_trie)
        mBinding.ensureBtn.setOnClickListener {
            val text = mBinding.inputEt.text
            if (text.isNullOrEmpty()) {
                return@setOnClickListener
            }
            val filter = trieHelp.filter(text.toString())
            mBinding.resultTv.append("$filter\n")
        }

        ThreadPoolHelp.execute {
            trieHelp.addWord("左全", "左前")
            trieHelp.addWord("右全", "右前")
            trieHelp.addWord("carplay", "CarPlay")
        }
    }

    override fun initActionBar() {
        super.initActionBar()
        val actionBar = supportActionBar ?: return
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.title = "文本替换"
    }
}