package com.gopaychan.constantcharge.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.gopaychan.constantcharge.R;
import com.gopaychan.constantcharge.utils.ThreadUtils;

import static android.content.ContentValues.TAG;

/**
 * Created by gopayChan on 2017/4/16.
 */

public class RingProgress extends View {

    private int mRingProgressColor;
    private int mRingBackgroundColor;
    private int mProgress;
    private int mProgressMax;
    private float mRingStrokeWidth;
    private int DEFAULT_PROGRESS_SIZE = 200;
    private float DEFAULT_STROKE_WIDTH = 10;
    private Paint mPaint;
    private RectF mRingRect;

    public RingProgress(Context context) {
        this(context, null);
    }

    public RingProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RingProgress);
        mRingProgressColor = a.getColor(R.styleable.RingProgress_ring_color, context.getResources().getColor(R.color.colorTheme));
        mRingBackgroundColor = a.getColor(R.styleable.RingProgress_ring_background, context.getResources().getColor(R.color.dark_gray));
        mRingStrokeWidth = a.getDimension(R.styleable.RingProgress_ring_stroke_width, DEFAULT_STROKE_WIDTH);
        a.recycle();
        mProgress = 0;
        mProgressMax = 360;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mRingBackgroundColor);
        mPaint.setStrokeWidth(mRingStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        Log.e(TAG, "onMeasure: " + wSize);
        Log.e(TAG, "onMeasure: " + hSize);
        if (wMode == MeasureSpec.AT_MOST && hMode == MeasureSpec.AT_MOST) {
            wSize = DEFAULT_PROGRESS_SIZE;
            hSize = DEFAULT_PROGRESS_SIZE;
        } else if (wSize > hSize) {
            wSize = hSize;
        } else if (wSize < hSize) {
            hSize = wSize;
        }
        if (mRingRect == null) {
            float left = getLeft() + getPaddingLeft() + mRingStrokeWidth / 2;
            float top = getTop() + getPaddingTop() + mRingStrokeWidth / 2;
            float right = wSize - getPaddingRight() - getRight() - mRingStrokeWidth / 2;
            float bottom = hSize - getPaddingBottom() - getBottom() - mRingStrokeWidth / 2;
            float progressSize = Math.min(right - left, bottom - top);
            left = left + (right - left - progressSize) / 2;
            top = top + (bottom - top - progressSize) / 2;
            mRingRect = new RectF(left, top, left + progressSize, top + progressSize);
        }
        setMeasuredDimension(wSize, hSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRingBackground(canvas);
        drawRingColor(canvas);

    }

    private void drawRingBackground(Canvas canvas) {
        mPaint.setColor(mRingBackgroundColor);
        canvas.drawRoundRect(mRingRect, getMeasuredWidth() / 2, getMeasuredHeight() / 2, mPaint);
    }

    private void drawRingColor(Canvas canvas) {
        mPaint.setColor(mRingProgressColor);
        canvas.drawArc(mRingRect, -90, mProgress, false, mPaint);
    }

    public void setProgress(int progress) {
        if (progress > mProgressMax) {
            mProgress = mProgressMax;
        } else if (progress < 0) {
            mProgress = 0;
        } else if (mProgress != progress) {
            mProgress = progress;
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            });
        }
    }
}
