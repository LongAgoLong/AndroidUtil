package com.leo.androidutil.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.View;
import android.widget.Button;

import com.leo.androidutil.R;
import com.leo.system.IntentUtil;

public class RootActivity extends BaseActivity implements View.OnClickListener {
    private Button mLocationBtn;
    private Button mPinyinBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        initView();
    }

    private void initView() {
        mLocationBtn = findViewById(R.id.locationBtn);
        mLocationBtn.setOnClickListener(this);
        mPinyinBtn = findViewById(R.id.pinyinBtn);
        mPinyinBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.locationBtn:
                IntentUtil.startActivity(this, LocationActivity.class);
                break;
            case R.id.pinyinBtn:
                IntentUtil.startActivity(this, PinyinActivity.class);
                break;
        }
    }
}
