package com.hengchongkeji.constantcharge.manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
    EditText mPrePswEdt;
    AlertDialog mAlertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(getString(R.string.settings_title));
    }

    @Override
    protected void initView() {
        super.initView();
        if (!ChargeApplication.getInstance().getIsLogin()) {
            mLogoutBtn.setVisibility(View.GONE);
        } else {
            initChangePswDialog();
        }

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
        if (ChargeApplication.getInstance().getIsLogin()) {
            intent.setClass(this, PersonalInfoActivity.class);
        } else {
            intent.setClass(this, LoginActivity.class);
        }
        startActivity(intent);
    }

    @OnClick(R.id.settingChangePswFytId)
    public void onClickChangePswItem() {
        Intent intent = new Intent();
        if (ChargeApplication.getInstance().getIsLogin()) {
//            intent.setClass(this, PersonalInfoActivity.class);
            mAlertDialog.show();
        } else {
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void initChangePswDialog() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_change_psw_layout, null);
        mPrePswEdt = (EditText) v.findViewById(R.id.changePswDialogEdtId);
        mAlertDialog = new AlertDialog.Builder(this).setView(v).setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String prePsw = mPrePswEdt.getText().toString().trim();
                if (TextUtils.equals(prePsw, ChargeApplication.getInstance().getUser().getPassword())) {
                    dialog.dismiss();
                    Intent intent = new Intent(SettingsActivity.this, ChangePswActivity.class);
                    startActivity(intent);
                    mPrePswEdt.setText("");
                } else {
                    showSnackbar("原密码错误，请重新输入");
                    mPrePswEdt.setText("");
                }

            }
        }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();

    }
}
