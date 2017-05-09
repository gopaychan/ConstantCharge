package com.hengchongkeji.constantcharge.main.scanning;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.base.BaseFragment;
import com.hengchongkeji.constantcharge.main.MainActivity;
import com.hengchongkeji.constantcharge.utils.PermissionUtils;
import com.hengchongkeji.constantcharge.utils.ScreenUtils;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.hengchongkeji.constantcharge.main.MainActivity.SCANNING_REQUEST_CODE;

/**
 * Created by gopayChan on 2017/4/27.
 */

@RuntimePermissions
public class ScanningFragment extends BaseFragment {

    @Bind(R.id.actionBarBackIvId)
    ImageView mBackIv;
    @Bind(R.id.actionBarTitleId)
    TextView mTitleTv;
    @Bind(R.id.scanningSpaceViewId)
    View view;

    public static ScanningFragment getInstance() {
        return new ScanningFragment();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_scanning;
    }

    @Override
    protected void postOnCreateView() {
        super.postOnCreateView();
        mBackIv.setVisibility(View.GONE);
        mTitleTv.setText(getString(R.string.scanning));
        if (ScreenUtils.canChangeStatusColor())
            setSpaceViewHeight();
    }

    private void setSpaceViewHeight() {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = ScreenUtils.getStatusHeight(getActivity());
    }

    @OnClick(R.id.scanningBtnId)
    public void showScanningActivity() {
        ScanningFragmentPermissionsDispatcher.needsPermissionWithCheck(this);
    }

    private void showScanningActivityInternal(){
        Intent intent = new Intent(getActivity(), ScanningActivity.class);
        getActivity().startActivityForResult(intent, SCANNING_REQUEST_CODE);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void needsPermission() {
        showScanningActivityInternal();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ScanningFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    void onShowRationable(final PermissionRequest request) {
        PermissionUtils.showRationaleDialog(getActivity(), R.string.scanning_permission_show_rationale,request);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void onPermissionDenied() {
        ((MainActivity)getActivity()).mMainPresenter.mMainView.showSnackbar(getString(R.string.scanning_permission_denied));
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void onNeverAskAgain() {
        ((MainActivity)getActivity()).mMainPresenter.mMainView.showSnackbar(getString(R.string.scanning_permission_never_ask));
    }


}
