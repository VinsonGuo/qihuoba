package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.FinishEvent;
import com.yjjr.yjfutures.event.RefreshEvent;
import com.yjjr.yjfutures.model.biz.Funds;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.utils.DoubleUtil;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.RegisterInput;
import com.yjjr.yjfutures.widget.listener.TextWatcherAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class WithdrawActivity extends BaseActivity {

    private Button mBtnConfirm;
    private TextView mTvYue;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, WithdrawActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        EventBus.getDefault().register(this);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        mTvYue = (TextView) findViewById(R.id.tv_yue);
        headerView.bindActivity(mContext);
        final RegisterInput riMoney = (RegisterInput) findViewById(R.id.ri_money);
        EditText etMoney = riMoney.getEtInput();
        etMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etMoney.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                Funds funds = StaticStore.getFunds(false);
                mBtnConfirm.setSelected(!TextUtils.isEmpty(s) && Double.parseDouble(s.toString()) != 0 && funds.getAvailableFunds() >= Double.parseDouble(s.toString()));
            }
        });
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBtnConfirm.isSelected()) {
                    InputPayPwdActivity.startActivity(mContext, riMoney.getValue());
                }
            }
        });

        headerView.setOperateClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WithdrawDetailActivity.startActivity(mContext);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FinishEvent event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshEvent event) {
        Funds funds = StaticStore.getFunds(false);
        mTvYue.setText(getString(R.string.rmb_symbol) + DoubleUtil.format2Decimal(funds.getAvailableFunds()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
