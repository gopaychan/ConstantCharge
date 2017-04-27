package com.gopaychan.constantcharge.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gopaychan.constantcharge.ActivityModule;
import com.gopaychan.constantcharge.ApplicationComponent;
import com.gopaychan.constantcharge.ChargeApplication;

import butterknife.ButterKnife;

/**
 * Created by gopaychan on 2017/3/25.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected abstract void initView();

    protected abstract void initData();

    protected abstract void inject();

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
