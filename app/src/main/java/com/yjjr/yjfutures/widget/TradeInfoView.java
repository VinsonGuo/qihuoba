package com.yjjr.yjfutures.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.utils.DoubleUtil;

/**
 * Created by dell on 2017/7/26.
 */

public class TradeInfoView extends LinearLayout {

    private TextView mTvValue1;
    private TextView mTvValue2;
    private TextView mTvValue3;
    private TextView mTvName1;
    private TextView mTvName2;
    private TextView mTvName3;

    public TradeInfoView(Context context) {
        this(context, null);
    }

    public TradeInfoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TradeInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_trade_info, this);
        mTvName1 = (TextView) findViewById(R.id.tv_name1);
        mTvName2 = (TextView) findViewById(R.id.tv_name2);
        mTvName3 = (TextView) findViewById(R.id.tv_name3);
        mTvValue1 = (TextView) findViewById(R.id.tv_value1);
        mTvValue2 = (TextView) findViewById(R.id.tv_value2);
        mTvValue3 = (TextView) findViewById(R.id.tv_value3);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TradeInfoView);
        boolean isDemo = typedArray.getBoolean(R.styleable.TradeInfoView_isDemo, false);
        if (isDemo) {
            mTvName1.setText("保证金币");
            mTvName2.setText("可用金币");
            mTvName3.setText("总金币");
        }
        typedArray.recycle();
    }

    public void setValues(double value1, double value2, double value3) {
        mTvValue1.setText("￥"+ DoubleUtil.format2Decimal(value1));
        mTvValue2.setText("￥"+ DoubleUtil.format2Decimal(value2));
        mTvValue3.setText("￥"+ DoubleUtil.format2Decimal(value3));
    }

    public void setValues(boolean isDemo, double value1, double value2, double value3) {
        if (isDemo) {
            mTvName1.setText("保证金币");
            mTvName2.setText("可用金币");
            mTvName3.setText("总金币");
        }
        mTvValue1.setText("￥"+ DoubleUtil.format2Decimal(value1));
        mTvValue2.setText("￥"+ DoubleUtil.format2Decimal(value2));
        mTvValue3.setText("￥"+ DoubleUtil.format2Decimal(value3));
    }
}
