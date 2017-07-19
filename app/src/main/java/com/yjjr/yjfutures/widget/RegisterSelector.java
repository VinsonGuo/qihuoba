package com.yjjr.yjfutures.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yjjr.yjfutures.R;


/**
 * Created by Administrator on 2016/11/28.
 */

public class RegisterSelector extends RelativeLayout {

    private TextView mTvInput;
    private TextView mTvError;
    private TextView mTvValue;


    public RegisterSelector(Context context) {
        this(context, null);
    }

    public RegisterSelector(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RegisterSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            LayoutInflater.from(context).inflate(R.layout.view_register_selector, this);
            final View rootView = findViewById(R.id.root_view);
            mTvInput = (TextView) findViewById(R.id.tv_input);
            mTvError = (TextView) findViewById(R.id.tv_error);
            mTvValue = (TextView) findViewById(R.id.tv_value);
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RegisterSelector);
            String name = typedArray.getString(R.styleable.RegisterSelector_name_text);
            String error = typedArray.getString(R.styleable.RegisterSelector_error_text);

            mTvInput.setText(name);
            mTvError.setText(error);
            typedArray.recycle();
        }
    }

    public void showError(String msg) {
        if (mTvError.getVisibility() == INVISIBLE) {
            mTvError.setText(msg);
            mTvError.setVisibility(VISIBLE);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTvError.setText(null);
                    mTvError.setVisibility(INVISIBLE);
                }
            }, 3000);
        }
    }

    public void setName(String name) {
        mTvInput.setText(name);
    }

    public void setText(String text) {
        mTvValue.setText(text);
    }

    public void setText(@StringRes int res) {
        mTvValue.setText(res);
    }

    public TextView getTvValue() {
        return mTvValue;
    }

    public String getValue() {
        return mTvValue.getText().toString();
    }

    public void setEditable(boolean b) {
        if(b) {
            mTvInput.setTextColor(ContextCompat.getColor(getContext(), R.color.color_333333));
        }else {
            mTvInput.setTextColor(ContextCompat.getColor(getContext(), R.color.color_737375));
        }
    }

    public void setOnFocusChangeListener(OnFocusChangeListener f){
        mTvInput.setOnFocusChangeListener(f);
    }
}
