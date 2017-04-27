package com.gopaychan.constantcharge.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by gopayChan on 2017/4/26.
 */

public class ChargeViewpager extends ViewPager {
    private boolean mScrollable;

    public ChargeViewpager(Context context) {
        super(context);
    }

    public ChargeViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollAble(boolean scrollAble) {
        mScrollable = scrollAble;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mScrollable)
            return false;
        else
            return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mScrollable) {
            return false;
        } else
            return super.onTouchEvent(ev);
    }
}
