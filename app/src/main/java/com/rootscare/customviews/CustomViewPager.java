package com.rootscare.customviews;

import android.content.Context;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

import com.rootscare.serviceprovider.R;

public class CustomViewPager extends ViewPager {
    private boolean swipeable;
    public CustomViewPager(Context context) {
        super(context);
    }
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.swipeable = true;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomViewPager);
        try {
            swipeable = a.getBoolean(R.styleable.CustomViewPager_swipeable, true);
        } finally {
            a.recycle();
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.swipeable) {
            return super.onTouchEvent(event);
        }
        return false;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.swipeable) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }
    public void setPagingEnabled(boolean enabled) {
        this.swipeable = enabled;
    }
}