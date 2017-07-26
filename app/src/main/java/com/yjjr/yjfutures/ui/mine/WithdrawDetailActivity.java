package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.widget.HeaderView;

public class WithdrawDetailActivity extends BaseActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, WithdrawDetailActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_detail);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        headerView.bindActivity(mContext);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.withdraw_detail);
        fragment.setUserVisibleHint(true);
    }
}
