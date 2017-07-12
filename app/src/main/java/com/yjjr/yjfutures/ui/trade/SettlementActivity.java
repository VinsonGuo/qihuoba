package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.widget.HeaderView;

public class SettlementActivity extends BaseActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SettlementActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        headerView.bindActivity(mContext);
        SettlementListFragment fragment = new SettlementListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();
        fragment.setUserVisibleHint(true);
    }
}
