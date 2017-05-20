package com.hengchongkeji.constantcharge.main.home.map;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.utils.ScreenUtils;

/**
 * Created by gopayChan on 2017/5/11.
 */

public class StationDetailDialog {
    private Dialog mDialog;
    private StationDetailDialog mStationDetailDialog;
    private TextView mAddressTv, mQuickTv, mSlowTv, mDistance, mChargeMoneyTv, mServiceMoneyTv, mParkMoneyTv, mNaviTv;
    private Context mContext;
    private View root;

    private static StationDetailDialog mInstance;

    private StationDetailDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(context, R.style.messageDialog);
        Window window = mDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.commonDialogAnimation);  //添加动画
        initView();
        mDialog.setContentView(root);
    }

    public static StationDetailDialog getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new StationDetailDialog(context);
        }
        return mInstance;
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        root = inflater.inflate(R.layout.dialog_station_detail, null);
        mAddressTv = (TextView) root.findViewById(R.id.stationDetailDialogAddressTvId);
        mQuickTv = (TextView) root.findViewById(R.id.stationDetailDialogQuickTvId);
        mSlowTv = (TextView) root.findViewById(R.id.stationDetailDialogSLowTvId);
        mDistance = (TextView) root.findViewById(R.id.stationDetailDialogDistanceTvId);
        mChargeMoneyTv = (TextView) root.findViewById(R.id.stationDetailDialogChargeMoneyTvId);
        mServiceMoneyTv = (TextView) root.findViewById(R.id.stationDetailDialogServiceMoneyTvId);
        mParkMoneyTv = (TextView) root.findViewById(R.id.stationDetailDialogParkMoneyTvId);
        mNaviTv = (TextView) root.findViewById(R.id.mapStationDetailNaviTvId);
        mNaviTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick();
                }
            }
        });

        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mCancelListener != null) {
                    mCancelListener.onCancel();
                }
            }
        });
    }

    public void setAddress(String address) {
        mAddressTv.setText(address);
    }

    public void setDistance(String distance) {
        mDistance.setText(distance);
    }

    public void setQuickText(String quickFree, String quickTotal) {
        mQuickTv.setText(mContext.getString(R.string.charge_map_pile_text, quickFree, quickTotal));
    }

    public void setSlowText(String slowFree, String slowTotal) {
        mSlowTv.setText(mContext.getString(R.string.charge_map_pile_text, slowFree, slowTotal));
    }

    public void setChargeMoney(String chargeMoney) {
        mChargeMoneyTv.setText(mContext.getString(R.string.charge_map_fee,chargeMoney));
    }

    public void setServiceMoney(String serviceMoney) {
        mServiceMoneyTv.setText(mContext.getString(R.string.charge_map_fee,serviceMoney));
    }

    public void setParkMoney(String parkMoney) {
        mParkMoneyTv.setText(mContext.getString(R.string.charge_map_fee,parkMoney));
    }

    interface OnNaviClickListener {
        void onClick();
    }

    private OnNaviClickListener mListener;

    public void setNaviClickListener(OnNaviClickListener listener) {
        mListener = listener;
    }

    interface OnDialogCancelListener {
        void onCancel();
    }

    OnDialogCancelListener mCancelListener;

    public void setOnDialogCancelListener(OnDialogCancelListener listener) {
        mCancelListener = listener;
    }

    public void showDialog() {
        try {
            mDialog.show();
            // 设置dialog宽度
            WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
            params.width = ScreenUtils.getScreenWidth(mContext);
            mDialog.getWindow().setAttributes(params);
        } catch (WindowManager.BadTokenException e) {
            mInstance = null;
        }
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public static class Builder {
        private AlertParams p;

        public Builder(Context context) {
            p = new AlertParams(context);
        }

        public Builder setAddress(String address) {
            p.mAddress = address;
            return this;
        }

        public Builder setQuickFree(String quickFree) {
            p.mQuickFree = quickFree;
            return this;
        }

        public Builder setSlowFree(String slowFree) {
            p.mSlowFree = slowFree;
            return this;
        }

        public Builder setQuickTotal(String quickTotal) {
            p.mQuickTotal = quickTotal;
            return this;
        }

        public Builder setSlowTotal(String slowTotal) {
            p.mSlowTotal = slowTotal;
            return this;
        }

        public Builder setDistance(String distance) {
            p.mDistance = distance;
            return this;
        }

        public Builder setChargeMoney(String chargeMoney) {
            p.mChargeMoney = chargeMoney;
            return this;
        }

        public Builder setServiceMoney(String serviceMoney) {
            p.mServiceMoney = serviceMoney;
            return this;
        }

        public Builder setParkMoney(String parkMoney) {
            p.mParkMoney = parkMoney;
            return this;
        }

        public Builder setNaviClickListener(OnNaviClickListener listener) {
            p.mListener = listener;
            return this;
        }

        public Builder setOnDialogCancelListener(OnDialogCancelListener listener) {
            p.mCancelListener = listener;
            return this;
        }

        public StationDetailDialog create() {
            StationDetailDialog detailDialog = StationDetailDialog.getInstance(p.mContext);
            if (p.mAddress != null) {
                detailDialog.setAddress(p.mAddress);
            }
            if (p.mQuickFree != null && p.mQuickTotal != null) {
                detailDialog.setQuickText(p.mQuickFree, p.mQuickTotal);
            }
            if (p.mSlowFree != null && p.mSlowTotal != null) {
                detailDialog.setSlowText(p.mSlowFree, p.mSlowTotal);
            }
            if (p.mChargeMoney != null) {
                detailDialog.setChargeMoney(p.mChargeMoney);
            }
            if (p.mServiceMoney != null) {
                detailDialog.setServiceMoney(p.mServiceMoney);
            }
            if (p.mParkMoney != null) {
                detailDialog.setParkMoney(p.mParkMoney);
            }
            if (p.mDistance != null) {
                detailDialog.setDistance(p.mDistance);
            }
            if (p.mListener != null) {
                detailDialog.setNaviClickListener(p.mListener);
            }
            if (p.mCancelListener != null) {
                detailDialog.setOnDialogCancelListener(p.mCancelListener);
            }
            return detailDialog;
        }
    }

    private static class AlertParams {
        final Context mContext;
        String mAddress;
        String mQuickFree;
        String mQuickTotal;
        String mSlowFree;
        String mSlowTotal;
        String mDistance;
        String mChargeMoney;
        String mServiceMoney;
        String mParkMoney;
        OnNaviClickListener mListener;
        OnDialogCancelListener mCancelListener;

        AlertParams(Context mContext) {
            this.mContext = mContext;
        }
    }

}
