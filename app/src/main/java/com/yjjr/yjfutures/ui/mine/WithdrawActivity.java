package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.FinishEvent;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.Funds;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.utils.DoubleUtil;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.RegisterInput;
import com.yjjr.yjfutures.widget.listener.TextWatcherAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

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
        riMoney.getEtInput().addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                mBtnConfirm.setSelected(!TextUtils.isEmpty(s));
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
        requestData();
    }

    private void requestData() {

        HttpManager.getBizService().getFunds()
                .compose(RxUtils.<BizResponse<Funds>>applyBizSchedulers())
                .compose(this.<BizResponse<Funds>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<BizResponse<Funds>>() {
                    @Override
                    public void accept(@NonNull BizResponse<Funds> fundsBizResponse) throws Exception {
                        Funds result = fundsBizResponse.getResult();
                        mTvYue.setText(getString(R.string.rmb_symbol)+ DoubleUtil.format2Decimal(result.getAvailableFunds()));
                    }
                }, RxUtils.commonErrorConsumer());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FinishEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
