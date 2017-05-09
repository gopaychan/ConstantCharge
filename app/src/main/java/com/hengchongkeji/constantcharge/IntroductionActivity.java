package com.hengchongkeji.constantcharge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hengchongkeji.constantcharge.base.BaseActivity;
import com.hengchongkeji.constantcharge.data.source.DataFactory;
import com.hengchongkeji.constantcharge.executor.ThreadExecutor;
import com.hengchongkeji.constantcharge.http.IHttpRequest;
import com.hengchongkeji.constantcharge.main.MainActivity;
import com.hengchongkeji.constantcharge.utils.PreferenceUtils;
import com.hengchongkeji.constantcharge.utils.ScreenUtils;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by gopayChan on 2017/5/1.
 */

public class IntroductionActivity extends BaseActivity {

    @Bind(R.id.introductionViewPagerId)
    ViewPager mViewPager;
    @Bind(R.id.introductionRootLyt)
    LinearLayout mRootLyt;
    PagerAdapter mAdapter;
    Button mShowMainActivityBtn;
    int[] mIntroductionUrl;
    private Context mContext;
    @Inject
    ThreadExecutor mThreadExecutor;

//    @Override
//    public void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        if (PreferenceUtils.getHasOpenApp(this)) {
//            Intent intent = new Intent(IntroductionActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        mContext = this;
        mThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                DataFactory.getInstance().getDataSource(true).getIntroductionImgUrl(new IHttpRequest.OnResponseListener<int[]>() {
                    @Override
                    public void onSuccess(int[] ints) {
                        mIntroductionUrl = ints;
                    }

                    @Override
                    public void onFail(String errorMsg) {

                    }
                });
                mAdapter.notifyDataSetChanged();
            }
        });
        if (ScreenUtils.canChangeStatusColor())
            mRootLyt.setPadding(0, 0, 0, ScreenUtils.getBottomStatusHeight(this));
    }

    @Override
    protected void inject() {
        super.inject();
        DaggerActivityComponent.builder().applicationComponent(getApplicationComponent()).activityModule(getActivityModule()).build().inject(this);
    }

    @Override
    protected void initView() {
        super.initView();
        mAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return mIntroductionUrl == null ? 0 : mIntroductionUrl.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView iv = null;
                int img_url = mIntroductionUrl[position];
                if (position == mIntroductionUrl.length - 1) {
                    View root = LayoutInflater.from(mContext).inflate(R.layout.introduction_end_layout, container, false);
                    iv = (ImageView) root.findViewById(R.id.introductionEndLyIvId);
                    mShowMainActivityBtn = (Button) root.findViewById(R.id.introductionShowMainBtnId);
                    mShowMainActivityBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PreferenceUtils.setHasOpenApp(mContext);
                            Intent intent = new Intent(IntroductionActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    mShowMainActivityBtn.setText(R.string.introduction_show_main_text);
                    Picasso.with(mContext).load(img_url).into(iv);
                    container.addView(root);
                    return root;
                } else {
                    iv = new ImageView(mContext);
                    iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    Picasso.with(mContext).load(img_url).into(iv);
                    container.addView(iv);
                    return iv;
                }
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        };
        mViewPager.setAdapter(mAdapter);
    }
}
