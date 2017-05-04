package com.hengchongkeji.constantcharge;

import android.support.annotation.StringRes;
import android.widget.ImageView;
import android.widget.TextView;

import com.hengchongkeji.constantcharge.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gopaychan on 2017/3/26.
 */

public abstract class ActionBarActivity extends BaseActivity {

    @Bind(R.id.actionBarTitleId)
    TextView mTitle;
    @Bind(R.id.actionBarBackIvId)
    ImageView mBackIv;

    public void setTitle(@StringRes int strRes){
        mTitle.setText(getString(strRes));
    }

    public void setTitle(String title){
        mTitle.setText(title);
    }

    @OnClick(R.id.actionBarBackIvId)
    public void back(){
        onBackPressed();
    }
}
