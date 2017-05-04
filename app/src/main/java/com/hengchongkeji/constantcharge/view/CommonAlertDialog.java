package com.hengchongkeji.constantcharge.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;

import com.hengchongkeji.constantcharge.R;

/**
 * Created by gopayChan on 2017/4/29.
 */

public class CommonAlertDialog {

    private CommonAlertDialog(){}

    public static AlertDialog getCommonAlertDialog(Context context){
        AlertDialog dialog = new AlertDialog.Builder(context).setView(R.layout.common_dialog).create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.commonDialogAnimation);  //添加动画
        return dialog;
    }

}
