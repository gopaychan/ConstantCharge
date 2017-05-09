package com.hengchongkeji.constantcharge.main.found;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hengchongkeji.constantcharge.ChargeApplication;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.base.BaseFragment;
import com.hengchongkeji.constantcharge.data.source.DataFactory;
import com.hengchongkeji.constantcharge.executor.ThreadExecutor;
import com.hengchongkeji.constantcharge.http.IHttpRequest;
import com.hengchongkeji.constantcharge.utils.ScreenUtils;
import com.hengchongkeji.constantcharge.utils.ThreadUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;

/**
 * Created by gopayChan on 2017/4/30.
 */

public class FoundFragment extends BaseFragment {
    public static FoundFragment getInstance() {
        return new FoundFragment();
    }

    @Bind(R.id.foundAdVpId)
    ViewPager mViewPager;
    @Bind(R.id.actionBarBackIvId)
    ImageView mBackIv;
    @Bind(R.id.actionBarTitleId)
    TextView mTitleTv;
    @Bind(R.id.foundAdRgId)
    RadioGroup mRadioGroup;
    @Bind(R.id.foundSpaceViewId)
    View view;
    RadioButton[] mRadioButtons;
    int[] mImgUrls;
    ThreadExecutor mThreadExecutor;
    private PagerAdapter mAdapter;
    private Context mContext;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_found;
    }

    @Override
    protected void postOnCreateView() {
        super.postOnCreateView();
        mContext = getActivity();
        mBackIv.setVisibility(View.GONE);
        mTitleTv.setText(getString(R.string.bottom_navigation_bar_txt2));
        initPagerView();
        mThreadExecutor = ChargeApplication.getApplicationComponent().threadExecutor();
        mThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                DataFactory.getInstance().getDataSource(true).getFoundAdImgUrl(new IHttpRequest.OnResponseListener<int[]>() {
                    @Override
                    public void onSuccess(int[] ints) {
                        mImgUrls = ints;
                    }

                    @Override
                    public void onFail(String errorMsg) {

                    }
                });
                mAdapter.notifyDataSetChanged();
                mRadioButtons = new RadioButton[mImgUrls.length];
                for (int i = 0; i < mImgUrls.length; i++) {
                    final RadioButton radioButton = new RadioButton(mContext);
                    mRadioButtons[i] = radioButton;
                    radioButton.setButtonDrawable(null);
                    radioButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.found_ad_point_bg));
                    RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(ScreenUtils.dip2px(mContext, 10), ScreenUtils.dip2px(mContext, 10));

                    radioButton.setLayoutParams(lp);
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mRadioGroup.addView(radioButton);
                        }
                    });
                }
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mRadioButtons[0].setChecked(true);
                    }
                });
            }
        });
        if (ScreenUtils.canChangeStatusColor())
            setSpaceViewHeight();
    }

    private void setSpaceViewHeight() {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = ScreenUtils.getStatusHeight(getActivity());
    }

    private void initPagerView() {
        mAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return mImgUrls == null ? 0 : mImgUrls.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView iv = new ImageView(mContext);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                int img_url = mImgUrls[position];
                Picasso.with(mContext).load(img_url).into(iv);
                container.addView(iv);
                return iv;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
//                super.destroyItem(container, position, object);
                container.removeView((View) object);
            }
        };
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mRadioButtons[position].setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setAdapter(mAdapter);
    }
}
