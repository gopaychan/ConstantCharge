package com.gopaychan.constantcharge.charge;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gopaychan.constantcharge.ActionBarActivity;
import com.gopaychan.constantcharge.R;

/**
 * Created by gopaychan on 2017/3/26.
 */

public class ChargeCollectionActivity extends ActionBarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_collection);
    }

    @Override
    protected void initView() {
        setTitle(R.string.home_charge_collection_txt);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void inject() {

    }
}
