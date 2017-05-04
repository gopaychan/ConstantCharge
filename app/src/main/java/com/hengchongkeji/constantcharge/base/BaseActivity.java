package com.hengchongkeji.constantcharge.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hengchongkeji.constantcharge.ActivityModule;
import com.hengchongkeji.constantcharge.ApplicationComponent;
import com.hengchongkeji.constantcharge.ChargeApplication;

import butterknife.ButterKnife;

/**
 * Created by gopaychan on 2017/3/25.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected void initView() {

    }

    protected void initData() {

    }

    protected void inject() {

    }

    protected boolean isBindAuto = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
//        inject();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        if (isBindAuto)
            ButterKnife.bind(this);
        inject();
        initView();
    }

    protected ApplicationComponent getApplicationComponent() {
        return ChargeApplication.getApplicationComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }
}
