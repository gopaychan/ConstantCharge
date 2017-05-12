package com.hengchongkeji.constantcharge.main.scanning;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hengchongkeji.constantcharge.ActionBarActivity;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.charge.ChargeDetailActivity;
import com.hengchongkeji.constantcharge.data.entity.Station;

import butterknife.OnClick;

import static com.hengchongkeji.constantcharge.charge.ChargeDetailActivity.TO_CHARGE_DETAIL_ACTIVITY_ARGS;

/**
 * Created by gopayChan on 2017/4/27.
 */

public class EditPileCodeActivity extends ActionBarActivity {

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
        Intent intent = new Intent(this, ChargeDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TO_CHARGE_DETAIL_ACTIVITY_ARGS, new Station());
        startActivity(intent);
    }
}
