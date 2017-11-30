package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.event.PriceRefreshEvent;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.SimpleFragmentPagerAdapter;
import com.yjjr.yjfutures.utils.SpannableUtil;
import com.yjjr.yjfutures.utils.StringUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.widget.NestRadioGroup;
import com.yjjr.yjfutures.widget.NoTouchScrollViewpager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

public class FullScreenChartActivity extends BaseActivity {

    private String mSymbol;
    private boolean mIsDemo;
    private NoTouchScrollViewpager mViewpager;

    private TextView tvOpen;
    private TextView tvHigh;
    private TextView tvLow;
    private TextView tvVol;
    private TextView tvLastClose;
    private TextView mTvSymbol;
    private TextView mTvInfo;

    public static void startActivity(Context context, String symbol, boolean isDemo, int index) {
        Intent intent = new Intent(context, FullScreenChartActivity.class);
        intent.putExtra(Constants.CONTENT_PARAMETER, symbol);
        intent.putExtra(Constants.CONTENT_PARAMETER_2, isDemo);
        intent.putExtra(Constants.CONTENT_PARAMETER_3, index);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_chart);
        mSymbol = getIntent().getStringExtra(Constants.CONTENT_PARAMETER);
        mIsDemo = getIntent().getBooleanExtra(Constants.CONTENT_PARAMETER_2, false);
        int index = getIntent().getIntExtra(Constants.CONTENT_PARAMETER_3, 0);
        mViewpager = (NoTouchScrollViewpager) findViewById(R.id.viewpager);
        mTvSymbol = (TextView) findViewById(R.id.tv_symbol);
        mTvInfo = (TextView) findViewById(R.id.tv_info);

        tvOpen = (TextView) findViewById(R.id.tv_open);
        tvHigh = (TextView) findViewById(R.id.tv_high);
        tvLow = (TextView) findViewById(R.id.tv_low);
        tvVol = (TextView) findViewById(R.id.tv_vol);
        tvLastClose = (TextView) findViewById(R.id.tv_last_close);

        mViewpager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(), new Fragment[]{
                FullScreenLineChartFragment.newInstance(mSymbol, mIsDemo),
                FullScreenKLineChartFragment.newInstance(mSymbol, mIsDemo, HttpConfig.MIN),
                FullScreenKLineChartFragment.newInstance(mSymbol, mIsDemo, HttpConfig.MIN5),
                FullScreenKLineChartFragment.newInstance(mSymbol, mIsDemo, HttpConfig.MIN15),
                FullScreenKLineChartFragment.newInstance(mSymbol, mIsDemo, HttpConfig.HOUR),
                FullScreenKLineChartFragment.newInstance(mSymbol, mIsDemo, HttpConfig.DAY),
                FullScreenKLineChartFragment.newInstance(mSymbol, mIsDemo, HttpConfig.WEEK),
                FullScreenKLineChartFragment.newInstance(mSymbol, mIsDemo, HttpConfig.MONTH)}));
        NestRadioGroup rgNav = (NestRadioGroup) findViewById(R.id.rg_nav);
        rgNav.setOnCheckedChangeListener(new NestRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(NestRadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_chart1:
                        mViewpager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_chart2:
                        mViewpager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_chart3:
                        mViewpager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_chart4:
                        mViewpager.setCurrentItem(3, false);
                        break;
                    case R.id.rb_chart5:
                        mViewpager.setCurrentItem(4, false);
                        break;
                    case R.id.rb_chart6:
                        mViewpager.setCurrentItem(5, false);
                        break;
                    case R.id.rb_chart7:
                        mViewpager.setCurrentItem(6, false);
                        break;
                    case R.id.rb_chart8:
                        mViewpager.setCurrentItem(7, false);
                        break;
                }
            }
        });
        ((RadioButton) rgNav.getChildAt(index)).setChecked(true);
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Quote quote = StaticStore.getQuote(mSymbol, mIsDemo);
        fillViews(quote);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PriceRefreshEvent event) {
        if (TextUtils.equals(event.getSymbol(), mSymbol)) {
            Quote quote = StaticStore.getQuote(mSymbol, mIsDemo);
            fillViews(quote);
        }
    }


    private void fillViews(Quote quote) {
        if (quote == null) return;
        int profitColor = StringUtils.getProfitColor(mContext, quote.getChange());
        mTvInfo.setTextColor(profitColor);
        mTvSymbol.setText(quote.getSymbolname());
        mTvInfo.setText(TextUtils.concat(SpannableUtil.getStringBySize(StringUtils.getStringByTick(quote.getLastPrice(), quote.getTick()), 1.5f),
                "\n",
                String.format(Locale.getDefault(), "%+.2f", quote.getChange()),
                String.format(Locale.getDefault(), "(%+.2f%%)", quote.getChangeRate())));
        tvOpen.setText(StringUtils.getStringByTick(quote.getOpen(), quote.getTick()));
        tvHigh.setText(StringUtils.getStringByTick(quote.getHigh(), quote.getTick()));
        tvLow.setText(StringUtils.getStringByTick(quote.getLow(), quote.getTick()));
        tvVol.setText(quote.getVol() + "");
        tvLastClose.setText(StringUtils.getStringByTick(quote.getLastclose(), quote.getTick()));
    }

}
