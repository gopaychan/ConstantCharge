package com.gopaychan.constantcharge.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.gopaychan.constantcharge.R;
import com.gopaychan.constantcharge.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by gopaychan on 2017/3/26.
 */

public class SearchActivity extends BaseActivity {

    @Bind(R.id.searchViewId)
    FloatingSearchView mSearchView;
    @Bind(R.id.left_action)
    ImageView mBackView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void initView() {
        mSearchView.setSearchFocused(true);
        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                //get suggestions based on newQuery

                //pass them on to the search view
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void inject() {

    }

}
