package com.yjjr.yjfutures.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * Created by hou on 2015/6/3.
 */
public class NoTouchScrollViewpager extends ViewPager {

    public NoTouchScrollViewpager(Context context) {
        this(context, null);
    }

    public NoTouchScrollViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return false;
    }

}
