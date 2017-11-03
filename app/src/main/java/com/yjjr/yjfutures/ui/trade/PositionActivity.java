package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.widget.HeaderView;

public class PositionActivity extends BaseActivity {

    private boolean mIsDemo;

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
        headerView.bindActivity(mContext);
        headerView.setMainTitle("我的持仓");
        PositionListFragment fragment = PositionListFragment.newInstance(mIsDemo);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, fragment)
                .commitAllowingStateLoss();
        fragment.setUserVisibleHint(true);
    }
}
