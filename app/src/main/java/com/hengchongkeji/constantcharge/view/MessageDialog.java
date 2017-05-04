package com.hengchongkeji.constantcharge.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.utils.ScreenUtils;


public class MessageDialog {
    private Dialog mDialog;
    private MessageDialog mMessageDialog;
    private String title;
    private TextView mTitleTv, mMessageTv, text_detail;
    private Button mCancelBtn, mConfirmBtn;
    private Context mContext;
    private View mBtnDivider;
    private View mTitleDivider;
    private View root;

    public MessageDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(context, R.style.messageDialog);
        Window window = mDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.commonDialogAnimation);  //添加动画
        initView();
        mDialog.setContentView(root);
        mDialog.setCancelable(false);
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        root = inflater.inflate(R.layout.message_dialog, null);
        mTitleTv = (TextView) root.findViewById(R.id.messageDialogTitleTvId);
        mMessageTv = (TextView) root.findViewById(R.id.messageDialogMsgTvId);
        mCancelBtn = (Button) root.findViewById(R.id.messageDialogCancelBtnId);
        mConfirmBtn = (Button) root.findViewById(R.id.messageDialogConfirmBtnId);
        mBtnDivider = root.findViewById(R.id.divider);
        mTitleDivider = root.findViewById(R.id.messageDialogTitleDivider);
        text_detail = (TextView) root.findViewById(R.id.text_detail);
        mCancelBtn.setOnClickListener(mOnClickListener);
        mConfirmBtn.setOnClickListener(mOnClickListener);
    }

    public void setDialogTitle(String title) {
        mTitleTv.setText(title);
    }

    public void setDialogMessage(String message) {
        mMessageTv.setText(message);
    }

    public void setDialogMessage(int messageId) {
        mMessageTv.setText(mContext.getResources().getString(messageId));
    }

    public void setDialogMessageDetail(String detail) {
        text_detail.setText(detail);
    }

    private onDialogBtnPosClick btnPosClick;

    public void setDialogPosClick(onDialogBtnPosClick click) {
        btnPosClick = click;
    }

    public interface onDialogBtnPosClick {
        public void onClick();
    }

    private onDialogBtnNegClick btnNegClick;

    public void setDialogNegClick(onDialogBtnNegClick btnNegClick) {
        this.btnNegClick = btnNegClick;
    }

    public interface onDialogBtnNegClick {
        public void onClick();
    }

    public void setPosText(String text) {
        mConfirmBtn.setText(text);
    }

    public void setNegText(String text) {
        mCancelBtn.setText(text);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.messageDialogCancelBtnId:
                    mDialog.dismiss();
                    if (btnNegClick != null)
                        btnNegClick.onClick();
                    break;
                case R.id.messageDialogConfirmBtnId:
                    mDialog.dismiss();
                    if (btnPosClick != null)
                        btnPosClick.onClick();
                    break;
            }
        }
    };

    public void showDialog() {
        mDialog.show();
        // 设置dialog宽度
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        params.width = ScreenUtils.getScreenWidth(mContext);
        mDialog.getWindow().setAttributes(params);
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public void hideCancelBtn() {
        mCancelBtn.setVisibility(View.GONE);
        mBtnDivider.setVisibility(View.GONE);
    }

    public void hideTitleDivider() {
        mTitleDivider.setVisibility(View.GONE);
    }

    public void hideTitle() {
        mTitleTv.setVisibility(View.GONE);
        mTitleDivider.setVisibility(View.GONE);
    }

    public void hideTextDetail() {
        text_detail.setVisibility(View.GONE);
    }

    public void hideMessage(){
        mMessageTv.setVisibility(View.GONE);
    }

    public static class Builder {
        private AlertParams p;

        public Builder(Context context) {
            p = new AlertParams(context);
        }

        public Builder setTitle(String title) {
            p.mTitle = title;
            return this;
        }

        public Builder setTitle(@StringRes int titleRes) {
            p.mTitle = p.mContext.getString(titleRes);
            return this;
        }

        public Builder setMessage(String message) {
            p.mMessage = message;
            return this;
        }

        public Builder setMessage(@StringRes int messageRes) {
            p.mMessage = p.mContext.getString(messageRes);
            return this;
        }

        public Builder setNegClick(onDialogBtnNegClick negClick) {
            p.mBtnNegClick = negClick;
            return this;
        }

        public Builder setPosClick(onDialogBtnPosClick posClick) {
            p.mBtnPosClick = posClick;
            return this;
        }

        public MessageDialog create() {
            MessageDialog messageDialog = new MessageDialog(p.mContext);
            if (p.mMessage != null) {
                messageDialog.setDialogMessage(p.mMessage);
            } else {
                messageDialog.hideMessage();
                messageDialog.hideTitleDivider();
                messageDialog.hideTextDetail();
            }
            if (p.mTitle != null) {
                messageDialog.setDialogTitle(p.mTitle);
            } else {
                messageDialog.hideTitle();
                messageDialog.hideTextDetail();
            }
            if (p.mBtnNegClick != null)
                messageDialog.setDialogNegClick(p.mBtnNegClick);
            if (p.mBtnPosClick != null)
                messageDialog.setDialogPosClick(p.mBtnPosClick);
            return messageDialog;
        }
    }

    private static class AlertParams {
        final Context mContext;
        String mTitle;
        String mMessage;
        onDialogBtnNegClick mBtnNegClick;
        onDialogBtnPosClick mBtnPosClick;

        AlertParams(Context mContext) {
            this.mContext = mContext;
        }
    }

}
