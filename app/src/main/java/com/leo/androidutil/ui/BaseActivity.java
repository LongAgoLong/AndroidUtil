package com.leo.androidutil.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.leo.commonutil.app.AppStackManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

public class BaseActivity extends RxAppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppStackManager.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppStackManager.getInstance().killActivity(this);
    }

    /**
     * 初始化状态栏
     */
    protected void initActionBar() {

    }
}
