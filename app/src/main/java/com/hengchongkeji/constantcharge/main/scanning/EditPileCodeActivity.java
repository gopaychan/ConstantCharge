package com.hengchongkeji.constantcharge.main.scanning;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;

import com.hengchongkeji.constantcharge.ActionBarActivity;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.charge.ChargeDetailActivity;
import com.hengchongkeji.constantcharge.data.entity.Equipment;
import com.hengchongkeji.constantcharge.data.source.DataFactory;
import com.hengchongkeji.constantcharge.http.IHttpRequest;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by gopayChan on 2017/4/27.
 */

public class EditPileCodeActivity extends ActionBarActivity {
    @Bind(R.id.editPileCodeEdtId)
    EditText mPileCodeEdt;

    @Override
    protected void initView() {
        setTitle(getString(R.string.edit_pile_code_title));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pile_code);
    }

    @OnClick(R.id.editPileCodeBackBtnId)
    public void returnScanning() {
        finish();
    }

    @OnClick(R.id.editPileCodeCertainBtnId)
    public void certainCode() {
        final String pileCode = mPileCodeEdt.getText().toString().trim();
        if (!TextUtils.isEmpty(pileCode)) {
            mPd.show();
            DataFactory.getInstance().getDataSource(true).getEquipmentData(this, pileCode, new IHttpRequest.OnResponseListener<Equipment>() {
                @Override
                public void onSuccess(Equipment equipment) {
                    mPd.dismiss();
                    Intent intent = new Intent(EditPileCodeActivity.this, ChargeDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ChargeDetailActivity.EQUIPMENT_ID, pileCode);
                    startActivity(intent);
                }

                @Override
                public void onFail(String errorMsg) {
                    mPd.dismiss();
                    showSnackbar(errorMsg);
                }
            });
        }else{
            showSnackbar("桩号不能为空");
        }

    }
}
