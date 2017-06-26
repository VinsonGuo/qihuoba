package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.FastTakeOrderEvent;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.widget.HeaderView;

import org.greenrobot.eventbus.EventBus;

public class FastTakeOrderActivity extends BaseActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, FastTakeOrderActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_take_order);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        final Button btnOpen = (Button) findViewById(R.id.btn_open);
        btnOpen.setText(UserSharePrefernce.isFastTakeOrder(mContext) ? "关闭" : "开启");
        headerView.bindActivity(mContext);
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFastTakeOrder = UserSharePrefernce.isFastTakeOrder(mContext);
                btnOpen.setText(!isFastTakeOrder ? "关闭" : "开启");
                boolean bool = !isFastTakeOrder;
                UserSharePrefernce.setFastTakeOrder(mContext, bool);
                EventBus.getDefault().post(new FastTakeOrderEvent(bool));
            }
        });
    }
}
