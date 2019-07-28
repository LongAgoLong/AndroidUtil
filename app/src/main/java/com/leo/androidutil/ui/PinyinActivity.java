package com.leo.androidutil.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.leo.androidutil.R;
import com.leo.pinyinlib.PinyinHelp;

/**
 * Created by LEO
 * On 2019/7/28
 * Description:汉字转拼音
 */
public class PinyinActivity extends BaseActivity {
    private EditText mSearchStrEt;
    private TextView mResultTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinyin);
        initView();
    }

    private void initView() {
        mSearchStrEt = findViewById(R.id.searchStrEt);
        mResultTv = findViewById(R.id.resultTv);
        mSearchStrEt.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                String s = mSearchStrEt.getText().toString().trim();
                if (!TextUtils.isEmpty(s)) {
                    mResultTv.setText(PinyinHelp.Companion.getInstance().parsePinyin(s));
                    mResultTv.append("\n");
                    mResultTv.append(PinyinHelp.Companion.getInstance().parsePinyinAllFirstLetter(s));
                    mResultTv.append("\n");
                    mResultTv.append(PinyinHelp.Companion.getInstance().parsePinyinFirstLetter(s));
                }
                return true;
            }
            return false;
        });
    }
}
