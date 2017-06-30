package com.yjjr.yjfutures.ui.trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseFragment;

/**
 * Created by dell on 2017/6/27.
 */

public class HandicapFragment extends BaseFragment {

    private TextView tvTodayPosition;
    private TextView tvYesterdayPosition;
    private TextView tvTodayCount;
    private TextView tvYesterdayCount;
    private TextView tvTotalHand;
    private TextView tvAmount;
    private TextView tvLimitUp;
    private TextView tvLimitDown;

    private void findViews(View v) {
        tvTodayPosition = (TextView) v.findViewById(R.id.tv_today_position);
        tvYesterdayPosition = (TextView) v.findViewById(R.id.tv_yesterday_position);
        tvTodayCount = (TextView) v.findViewById(R.id.tv_today_count);
        tvYesterdayCount = (TextView) v.findViewById(R.id.tv_yesterday_count);
        tvTotalHand = (TextView) v.findViewById(R.id.tv_total_hand);
        tvAmount = (TextView) v.findViewById(R.id.tv_amount);
        tvLimitUp = (TextView) v.findViewById(R.id.tv_limit_up);
        tvLimitDown = (TextView) v.findViewById(R.id.tv_limit_down);
    }

    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_handicap, container, false);
        findViews(v);
        tvTodayPosition.setText("123.5");
        tvYesterdayPosition.setText("123.5");
        tvTodayCount.setText("123.5");
        tvYesterdayCount.setText("123.5");
        tvTotalHand.setText("123.5");
        tvAmount.setText("123.5");
        tvLimitUp.setText("123.5");
        tvLimitDown.setText("123.5");
        return v;
    }
}
