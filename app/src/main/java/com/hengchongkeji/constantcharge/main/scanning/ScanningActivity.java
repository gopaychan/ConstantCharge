package com.hengchongkeji.constantcharge.main.scanning;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.base.BaseActivity;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.OnClick;

/**
 * Created by gopayChan on 2017/4/27.
 */

public class ScanningActivity extends BaseActivity {

    private CaptureFragment captureFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.scanning_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();

        initView();
    }

    public static boolean isOpen = false;

    @OnClick(R.id.scanningLightBtnId)
    public void toggleLight() {
        if (!isOpen) {
            CodeUtils.isLightEnable(true);
            isOpen = true;
        } else {
            CodeUtils.isLightEnable(false);
            isOpen = false;
        }
    }

    @OnClick(R.id.actionBarBackIvId)
    public void back() {
        onBackPressed();
    }

    @OnClick(R.id.scanningEditCodeBtnId)
    public void showEditCodeActivity() {
        Intent intent = new Intent(this, EditPileCodeActivity.class);
        startActivity(intent);
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            ScanningActivity.this.setResult(RESULT_OK, resultIntent);
            ScanningActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            ScanningActivity.this.setResult(RESULT_OK, resultIntent);
            ScanningActivity.this.finish();
        }
    };
}
