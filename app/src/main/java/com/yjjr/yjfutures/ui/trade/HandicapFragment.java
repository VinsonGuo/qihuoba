package com.yjjr.yjfutures.ui.trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.event.RefreshEvent;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.utils.DoubleUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by dell on 2017/6/27.
 */

public class HandicapFragment extends BaseFragment {

    private String mSymbol;
    private TextView tvLastPrice;
    private TextView tvOpen;
    private TextView tvChange;
    private TextView tvHigh;
    private TextView tvChangeRate;
    private TextView tvLow;
    private TextView tvVol;
    private TextView tvLastClose;

    public static HandicapFragment newInstance(String symbol) {
        HandicapFragment fragment = new HandicapFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.CONTENT_PARAMETER, symbol);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void findViews(View v) {
        tvLastPrice = (TextView) v.findViewById(R.id.tv_last_price);
        tvOpen = (TextView) v.findViewById(R.id.tv_open);
        tvChange = (TextView) v.findViewById(R.id.tv_change);
        tvHigh = (TextView) v.findViewById(R.id.tv_high);
        tvChangeRate = (TextView) v.findViewById(R.id.tv_change_rate);
        tvLow = (TextView) v.findViewById(R.id.tv_low);
        tvVol = (TextView) v.findViewById(R.id.tv_vol);
        tvLastClose = (TextView) v.findViewById(R.id.tv_last_close);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mSymbol = getArguments().getString(Constants.CONTENT_PARAMETER);
        }
    }


    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_handicap, container, false);
        findViews(v);
        fillView();
        return v;
    }

    private void fillView() {
        Quote quote = StaticStore.sQuoteMap.get(mSymbol);
        tvHigh.setText(DoubleUtil.format2Decimal(quote.getHigh()));
        tvLastPrice.setText(DoubleUtil.format2Decimal(quote.getLastPrice()));
        tvOpen.setText(DoubleUtil.format2Decimal(quote.getOpen()));
        tvChange.setText(DoubleUtil.format2Decimal(quote.getChange()));
        tvHigh.setText(DoubleUtil.format2Decimal(quote.getHigh()));
        tvChangeRate.setText(DoubleUtil.format2Decimal(quote.getChangeRate()) + "%");
        tvLow.setText(DoubleUtil.format2Decimal(quote.getLow()));
        tvVol.setText(quote.getVol() + "");
        tvLastClose.setText(DoubleUtil.format2Decimal(quote.getLastclose()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshEvent event) {
        fillView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
