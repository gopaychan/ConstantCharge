package com.hengchongkeji.constantcharge;

import android.content.Intent;

import com.hengchongkeji.constantcharge.base.BaseActivity;
import com.hengchongkeji.constantcharge.main.MainActivity;
import com.hengchongkeji.constantcharge.utils.PreferenceUtils;

/**
 * Created by gopayChan on 2017/5/4.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void initData() {
        super.initData();
//        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Intent intent = new Intent();
        if (PreferenceUtils.getHasOpenApp(this)) {
            intent.setClass(this, MainActivity.class);
        } else {
            intent.setClass(this, IntroductionActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
