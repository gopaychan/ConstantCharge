package com.hengchongkeji.constantcharge.manager;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.widget.EditText;

import com.hengchongkeji.constantcharge.ActionBarActivity;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.utils.PermissionUtils;
import com.hengchongkeji.constantcharge.utils.PreferenceUtils;
import com.hengchongkeji.constantcharge.view.PictureDialog;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by gopayChan on 2017/5/3.
 */

@RuntimePermissions
public class PersonalInfoActivity extends ActionBarActivity {
    private PictureDialog mDialog;
    @Bind(R.id.personalInfoNickEdt)
    EditText mNickEdt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        setTitle(R.string.personal_info_title);
        mDialog = new PictureDialog(this) {
            @Override
            public void onOpenCameraClick() {
                PersonalInfoActivityPermissionsDispatcher.openCameraNeedsPermissionWithCheck(PersonalInfoActivity.this);
            }

            @Override
            public void onOpenGalleryClick() {
                mDialog.openGallery();
            }
        };
        mNickEdt.setText(PreferenceUtils.getUserNick(this));
    }

    @OnClick(R.id.personalInfoAvatarRytId)
    public void showAvatarChoose() {
        mDialog.showDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null && mDialog.isDialogShowing())
            mDialog.dismissDialog();
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    void openCameraNeedsPermission() {
        mDialog.openCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PersonalInfoActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    void openCameraOnShowRationale(final PermissionRequest request) {
        PermissionUtils.showRationaleDialog(this,"拍照上传需要摄像头、写文件权限，是否弹出权限申请",request);
    }

    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    void openCameraOnPermissionDenied() {
        showSnackbar("拒绝摄像头、写文件权限将无法拍照上传");
    }

    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    void openCameraOnNeverAskAgain() {
        showSnackbar("摄像头、写文件权限请求将不再弹出，如需正常使用功能请到手机设置中打开权限");
    }

    private void showSnackbar(String message) {
        Snackbar.make(mNickEdt, message, Snackbar.LENGTH_LONG).show();
    }
}
