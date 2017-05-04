package com.hengchongkeji.constantcharge.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.hengchongkeji.constantcharge.ActionBarActivity;
import com.hengchongkeji.constantcharge.ChargeApplication;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.view.MessageDialog;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gopayChan on 2017/4/29.
 */

public class SettingsActivity extends ActionBarActivity {

    @Bind(R.id.settingLogoutTvId)
    TextView mLogoutBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(getString(R.string.settings_title));
    }

    @Override
    protected void initView() {
        super.initView();
        if (!ChargeApplication.isLogin) mLogoutBtn.setVisibility(View.GONE);
    }

    @OnClick(R.id.settingLogoutTvId)
    public void logout() {
        new MessageDialog.Builder(this).setTitle(getString(R.string.settings_confirm_logout)).setPosClick(new MessageDialog.onDialogBtnPosClick() {
            @Override
            public void onClick() {
                ChargeApplication.getInstance().logout();
                finish();
            }
        }).create().showDialog();
    }

    @OnClick(R.id.settingShowPersonalInfoFytId)
    public void onClickPersonalInfoItem() {
        Intent intent = new Intent();
        if (ChargeApplication.isLogin) {
            intent.setClass(this, PersonalInfoActivity.class);
        } else {
            intent.setClass(this, LoginActivity.class);
        }
        startActivity(intent);
    }

    @OnClick(R.id.settingChangePswFytId)
    public void onClickChangePswItem(){
        Intent intent = new Intent();
        if (ChargeApplication.isLogin) {
//            intent.setClass(this, PersonalInfoActivity.class);
        } else {
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
