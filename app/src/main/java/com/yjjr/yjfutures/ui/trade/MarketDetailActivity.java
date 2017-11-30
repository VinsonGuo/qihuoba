package com.yjjr.yjfutures.ui.trade;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.RxActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.event.PriceRefreshEvent;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.utils.DoubleUtil;
import com.yjjr.yjfutures.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MarketDetailActivity extends RxActivity {

    private String mSymbol;
    private TextView tvOpen;
    private TextView tvChange;
    private TextView tvHigh;
    private TextView tvLow;
    private TextView tvVol;
    private TextView tvLastClose;

    public static void startActivity(Context context, String symbol) {
        Intent intent = new Intent(context, MarketDetailActivity.class);
        intent.putExtra(Constants.CONTENT_PARAMETER, symbol);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_market_detail);
        EventBus.getDefault().register(this);
        mSymbol = getIntent().getStringExtra(Constants.CONTENT_PARAMETER);
        findViews();
        Quote quote = StaticStore.getQuote(mSymbol, false);
        fillView(quote);
    }

    private void fillView(Quote quote) {
        tvHigh.setText(StringUtils.getStringByTick(quote.getHigh(), quote.getTick()));
        tvOpen.setText(StringUtils.getStringByTick(quote.getOpen(), quote.getTick()));
        tvChange.setText(StringUtils.getStringByTick(quote.getChange(), quote.getTick()));
        tvLow.setText(StringUtils.getStringByTick(quote.getLow(), quote.getTick()));
        tvVol.setText(quote.getVol() + "");
        tvLastClose.setText(StringUtils.getStringByTick(quote.getLastclose(), quote.getTick()));
    }

    private void findViews() {
        tvOpen = (TextView)findViewById(R.id.tv_open);
        tvChange = (TextView) findViewById(R.id.tv_change);
        tvHigh = (TextView) findViewById(R.id.tv_high);
        tvLow = (TextView) findViewById(R.id.tv_low);
        tvVol = (TextView) findViewById(R.id.tv_vol);
        tvLastClose = (TextView) findViewById(R.id.tv_last_close);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PriceRefreshEvent event) {
        Quote quote = StaticStore.getQuote(mSymbol, false);
        fillView(quote);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
