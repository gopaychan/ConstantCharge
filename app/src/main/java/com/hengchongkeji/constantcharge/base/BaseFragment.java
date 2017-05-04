package com.hengchongkeji.constantcharge.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by gopaychan on 2017/3/26.
 */

public abstract class BaseFragment extends Fragment {

    protected abstract
    @LayoutRes
    int getFragmentLayout();

    protected void postOnCreateView(){

    }

    protected View mRoot;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(getFragmentLayout(), container, false);
        ButterKnife.bind(this, mRoot);
        postOnCreateView();
        return mRoot;
    }
}
