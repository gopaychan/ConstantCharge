package com.hengchongkeji.constantcharge.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hengchongkeji.constantcharge.R;


/**
 * Created by L、on 2015/11/30.
 */
public class TwoOptionsDialog {
    private AlertDialog mdl;
    private TextView tv_title;
    private LinearLayout ll_title;
    private TextView tv_content1;
    private TextView tv_content2;
    private OnContent1ClickListener listener1;
    private OnContent2ClickListener listener2;
    private Window window;

    public TwoOptionsDialog(Context context) {
        mdl = new AlertDialog.Builder(context).create();
        mdl.setCanceledOnTouchOutside(true);
    }

    public TwoOptionsDialog setTitle(String title) {
        ll_title.setVisibility(View.VISIBLE);
        tv_title.setText(title);
        return this;
    }

    public TwoOptionsDialog setText(String text1, String text2) {
        tv_content1.setText(text1);
        tv_content2.setText(text2);
        return this;
    }

    interface OnContent1ClickListener {
        void onClick(View v);
    }

    public TwoOptionsDialog setContent1Click(OnContent1ClickListener listener) {
        listener1 = listener;
        return this;
    }

    interface OnContent2ClickListener {
        void onClick(View v);
    }

    public TwoOptionsDialog setContent2Click(OnContent2ClickListener listener) {
        listener2 = listener;
        return this;
    }

    public TwoOptionsDialog showDialog() {
        mdl.show();
        window = mdl.getWindow();
        window.setContentView(R.layout.two_option_dialog);//要在show之后才能调用
        ll_title = (LinearLayout) window
                .findViewById(R.id.ll_title);
        tv_title = (TextView) window.findViewById(R.id.tv_title);
        tv_content1 = (TextView) window.findViewById(R.id.tv_content1);
        tv_content2 = (TextView) window.findViewById(R.id.tv_content2);
        tv_content1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener1 != null)
                    listener1.onClick(v);
                mdl.dismiss();
            }
        });
        tv_content2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener2 != null)
                    listener2.onClick(v);
                mdl.dismiss();
            }
        });
        return this;
    }

    public boolean isShowing() {
        return mdl.isShowing();
    }

    public void dismiss() {
        if (mdl != null && mdl.isShowing())
            mdl.dismiss();
    }
}
