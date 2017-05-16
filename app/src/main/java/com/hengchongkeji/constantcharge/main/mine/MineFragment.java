package com.hengchongkeji.constantcharge.main.mine;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hengchongkeji.constantcharge.ChargeApplication;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.base.BaseFragment;
import com.hengchongkeji.constantcharge.data.entity.User;
import com.hengchongkeji.constantcharge.manager.LoginActivity;
import com.hengchongkeji.constantcharge.manager.RechargeActivity;
import com.hengchongkeji.constantcharge.manager.RegisterActivity;
import com.hengchongkeji.constantcharge.manager.SettingsActivity;
import com.hengchongkeji.constantcharge.utils.ScreenUtils;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by gopayChan on 2017/4/28.
 */

public class MineFragment extends BaseFragment {

    @Bind(R.id.mineUnLoginLayout)
    LinearLayout mUnLoginLayout;
    @Bind(R.id.mineUserNickTvId)
    TextView mUserNickTv;
    @Bind(R.id.mineSpaceViewId)
    View view;
    @Bind(R.id.mineDetailMoneyTvId)
    TextView moneyTv;


    public static MineFragment getInstance() {
        return new MineFragment();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_mine;

    }

    @Override
    protected void postOnCreateView() {
        super.postOnCreateView();
        checkLoginState();
        if (ScreenUtils.canChangeStatusColor())
            setSpaceViewHeight();
    }

    private void setSpaceViewHeight() {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = ScreenUtils.getStatusHeight(getActivity());
    }

    @OnClick(R.id.mineSettingsIvId)
    public void showSettingsActivity() {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.mineLoginTvId)
    public void showLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.mineRegisterTvId)
    public void showRegisterActivity() {
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.mineItemRechargeFlId)
    public void showRechargeActivity() {
        if (ChargeApplication.getInstance().getIsLogin()) {
            Intent intent = new Intent(getActivity(), RechargeActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }

    private void checkLoginState() {
        if (ChargeApplication.getInstance().getIsLogin()) {
            userIsLogin();
        } else {
            userUnLogin();
        }
    }

    private void userIsLogin() {
        mUserNickTv.setVisibility(View.VISIBLE);
        mUnLoginLayout.setVisibility(View.GONE);
        User user = ChargeApplication.getInstance().getUser();
        if (user != null) {
            mUserNickTv.setText(user.getUsername());
            moneyTv.setText(user.getBalance());
        } else {
            ChargeApplication.getInstance().logout();
        }
    }

    private void userUnLogin() {
        mUserNickTv.setVisibility(View.GONE);
        mUnLoginLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mUserNickTv != null)
            checkLoginState();
    }
}
