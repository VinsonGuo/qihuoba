package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.event.FastTakeOrderEvent;
import com.yjjr.yjfutures.model.FastTakeOrderConfig;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.widget.CustomPromptDialog;
import com.yjjr.yjfutures.widget.HeaderView;

import org.greenrobot.eventbus.EventBus;

public class FastTakeOrderActivity extends BaseActivity {


    private CustomPromptDialog mDialog;

    public static void startActivity(Context context, String symbol) {
        Intent intent = new Intent(context, FastTakeOrderActivity.class);
        intent.putExtra(Constants.CONTENT_PARAMETER, symbol);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_take_order);
        mDialog = new CustomPromptDialog.Builder(mContext)
                .setMessage("开启加速成功")
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create();
        final String symbol = getIntent().getStringExtra(Constants.CONTENT_PARAMETER);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        final Button btnOpen = (Button) findViewById(R.id.btn_open);
        final RadioGroup rg = (RadioGroup) findViewById(R.id.rg_hand);
        AppCompatCheckBox checkBox = (AppCompatCheckBox) findViewById(R.id.cb_check);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btnOpen.setSelected(isChecked);
            }
        });
        final FastTakeOrderConfig fastTakeOrder = UserSharePrefernce.getFastTakeOrder(mContext, symbol);
        btnOpen.setText(fastTakeOrder != null ? "关闭" : "开启");
        headerView.bindActivity(mContext);
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnOpen.isSelected()) {
                    return;
                }
                if (TextUtils.equals(btnOpen.getText(), "关闭")) {
                    btnOpen.setText("开启");
                } else {
                    btnOpen.setText("关闭");
                    mDialog.show();
                }

                FastTakeOrderConfig config = null;
                if (fastTakeOrder == null) {
                    config = new FastTakeOrderConfig();
                    int qty = 1;
                    switch (rg.getCheckedRadioButtonId()) {
                        case R.id.rb_hand_1:
                            qty = 1;
                            break;
                        case R.id.rb_hand_2:
                            qty = 2;
                            break;
                        case R.id.rb_hand_3:
                            qty = 3;
                            break;
                        case R.id.rb_hand_4:
                            qty = 4;
                            break;
                        case R.id.rb_hand_5:
                            qty = 5;
                            break;
                    }
                    config.setQty(qty);
                }
                UserSharePrefernce.setFastTakeOrder(mContext, symbol, config);
                EventBus.getDefault().post(new FastTakeOrderEvent(config != null));
            }
        });
    }
}
