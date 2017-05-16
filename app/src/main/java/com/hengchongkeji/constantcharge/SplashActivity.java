package com.hengchongkeji.constantcharge;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.hengchongkeji.constantcharge.base.BaseActivity;
import com.hengchongkeji.constantcharge.data.entity.User;
import com.hengchongkeji.constantcharge.http.IHttpRequest;
import com.hengchongkeji.constantcharge.main.MainActivity;
import com.hengchongkeji.constantcharge.main.home.map.BaiduNaviManager;
import com.hengchongkeji.constantcharge.manager.ManagerAction;
import com.hengchongkeji.constantcharge.utils.PreferenceUtils;

/**
 * Created by gopayChan on 2017/5/4.
 */

public class SplashActivity extends BaseActivity {
    public static final int WAIT_TIME = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        User user = ChargeApplication.getInstance().getUser();
        if (ChargeApplication.getInstance().getIsLogin()) {

            ManagerAction.login(this, user.getPhone(), user.getPassword(), new IHttpRequest.OnResponseListener() {
                @Override
                public void onSuccess(Object o) {

                }

                @Override
                public void onFail(String errorMsg) {
                    ChargeApplication.getInstance().logout();
                }
            });
        }

        Handler handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                BaiduNaviManager.getInstance().initNavi(SplashActivity.this, new BaiduNaviManager.onInitResponse() {
                    @Override
                    public void onResponse(int result) {

                    }
                });
                long diff = System.currentTimeMillis() - start;
                if (diff > WAIT_TIME - 1000) {
                    showNextActivity();
                } else {
                    new Handler(getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showNextActivity();
                        }
                    }, WAIT_TIME - diff);
                }
            }
        }, 1000);
    }

    private void showNextActivity() {
        Intent intent = new Intent();
        if (PreferenceUtils.getHasOpenApp(SplashActivity.this)) {
            intent.setClass(SplashActivity.this, MainActivity.class);
        } else {
            intent.setClass(SplashActivity.this, IntroductionActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
