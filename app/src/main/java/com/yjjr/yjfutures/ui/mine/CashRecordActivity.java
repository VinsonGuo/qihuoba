package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.widget.HeaderView;

public class CashRecordActivity extends BaseActivity {

    /**
     * 充值
     */
    public static final int DEPOSIT = 1;
    /**
     * 提现
     */
    public static final int WITHDRAW = 2;


    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, CashRecordActivity.class);
        intent.putExtra(Constants.CONTENT_PARAMETER, type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_record);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        headerView.bindActivity(mContext);
        int type = getIntent().getIntExtra(Constants.CONTENT_PARAMETER, DEPOSIT);
        headerView.setMainTitle(type == DEPOSIT ? "充值记录" : "提现记录");
        CashRecordFragment fragment = CashRecordFragment.newInstance(type);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();
        fragment.setUserVisibleHint(true);
    }
}
