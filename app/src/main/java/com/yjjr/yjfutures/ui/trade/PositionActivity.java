package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.event.PollRefreshEvent;
import com.yjjr.yjfutures.model.biz.Funds;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.utils.DisplayUtils;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.TradeInfoView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PositionActivity extends BaseActivity {

    private boolean mIsDemo;
    private TradeInfoView mTradeInfoView;

    public static void startActivity(Context context, boolean isDemo) {
        Intent intent = new Intent(context, PositionActivity.class);
        intent.putExtra(Constants.CONTENT_PARAMETER, isDemo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_common_with_fragment);
        mIsDemo = getIntent().getBooleanExtra(Constants.CONTENT_PARAMETER, false);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        LinearLayout layout = (LinearLayout) findViewById(R.id.root_view);
        mTradeInfoView = new TradeInfoView(mContext);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = DisplayUtils.dip2px(mContext, 10);
        lp.setMargins(margin, margin, margin, margin);
        mTradeInfoView.setLayoutParams(lp);
        layout.addView(mTradeInfoView);
        Funds result = StaticStore.getFunds(mIsDemo);
        mTradeInfoView.setValues(mIsDemo, result.getFrozenMargin(), result.getAvailableFunds(), result.getNetAssets());
        headerView.bindActivity(mContext);
        headerView.setMainTitle("我的持仓");
        headerView.setSubtitleText("订单记录");
        headerView.setOperateClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettlementActivity.startActivity(mContext, mIsDemo);
            }
        });
        PositionListFragment fragment = PositionListFragment.newInstance(mIsDemo);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, fragment)
                .commitAllowingStateLoss();
        fragment.setUserVisibleHint(true);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PollRefreshEvent event) {
        Funds result = StaticStore.getFunds(mIsDemo);
        mTradeInfoView.setValues(mIsDemo, result.getFrozenMargin(), result.getAvailableFunds(), result.getNetAssets());
    }
}
