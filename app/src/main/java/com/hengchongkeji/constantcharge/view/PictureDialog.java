package com.hengchongkeji.constantcharge.view;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.hengchongkeji.constantcharge.common.FileHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by L、on 2015/11/30.
 * <p>
 * 拍照上传,本地选择dialog
 */
public abstract class PictureDialog {

    public final static int START_CAMERA = 105;
    public final static int START_LOCAL_GALLERY = 106;
    public final static String OPEN_CAMERA_SUCCESS = "success";

    private TwoOptionsDialog mDl;
    private String photoUrl;
    private Activity activity;


    public PictureDialog(final Activity activity) {
        this.activity = activity;
        mDl = new TwoOptionsDialog(activity);
    }

    public abstract void onOpenCameraClick();

    public abstract void onOpenGalleryClick();

    public void showDialog() {
        mDl.showDialog();
        mDl.setTitle("选择图片来源").setText("拍照上传", "本地选择")
                .setContent1Click(new TwoOptionsDialog.OnContent1ClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOpenCameraClick();
                    }
                })
                .setContent2Click(new TwoOptionsDialog.OnContent2ClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOpenGalleryClick();
                    }
                });
    }

    public String openCamera() {
        String state = Environment
                .getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //文件名
            SimpleDateFormat timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA);
            String fileName = timeStamp.format(new Date());
            photoUrl = FileHelper.getFileSavePath(activity, "image");
            File path = new File(photoUrl);
            if (!path.exists()) {
                path.mkdirs();
            }
            photoUrl = photoUrl + fileName;
            //保存路径
            File fileURL = new File(photoUrl);
            openCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileURL));
            activity.startActivityForResult(openCamera, START_CAMERA);
            return OPEN_CAMERA_SUCCESS;
        } else {
            return "请插入SD卡";
        }
    }

    public void openGallery() {
        Intent openPicture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(openPicture, START_LOCAL_GALLERY);
    }

    public String getPhotoUrl(Intent data) {
        if (data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.MediaColumns.DATA};
            Cursor c = activity.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            photoUrl = c.getString(columnIndex);
            c.close();
        }
        return photoUrl;
    }

    public boolean isDialogShowing() {
        return mDl.isShowing();
    }

    public void dismissDialog() {
        if (mDl != null && mDl.isShowing())
            mDl.dismiss();
    }
}
