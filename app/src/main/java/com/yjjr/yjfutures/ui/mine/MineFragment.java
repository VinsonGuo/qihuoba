package com.yjjr.yjfutures.ui.mine;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.Funds;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.ui.WebActivity;
import com.yjjr.yjfutures.ui.trade.DepositActivity;
import com.yjjr.yjfutures.utils.ActivityTools;
import com.yjjr.yjfutures.utils.DialogUtils;
import com.yjjr.yjfutures.utils.DoubleUtil;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.CustomPromptDialog;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * mine fragment
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvYue;
    private TextView tvMargin;
    private TextView tvNet;
    private CustomPromptDialog mCustomServiceDialog;

    public MineFragment() {
        // Required empty public constructor
    }

    private void findViews(View v) {
        TextView tvOne = (TextView) v.findViewById(R.id.tv_one);
        TextView tvTwo = (TextView) v.findViewById(R.id.tv_two);
        TextView tvThree = (TextView) v.findViewById(R.id.tv_three);
        TextView tvFour = (TextView) v.findViewById(R.id.tv_four);
        tvYue = (TextView) v.findViewById(R.id.tv_yue);
        tvMargin = (TextView) v.findViewById(R.id.tv_margin);
        tvNet = (TextView) v.findViewById(R.id.tv_net);
        final SwipeRefreshLayout refresh = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HttpManager.getBizService().getFunds()
                        .compose(RxUtils.<BizResponse<Funds>>applyBizSchedulers())
                        .compose(MineFragment.this.<BizResponse<Funds>>bindUntilEvent(FragmentEvent.DESTROY))
                        .subscribe(new Consumer<BizResponse<Funds>>() {
                            @Override
                            public void accept(@NonNull BizResponse<Funds> fundsBizResponse) throws Exception {
                                Funds result = fundsBizResponse.getResult();
                                tvYue.setText(DoubleUtil.format2Decimal(result.getAvailableFunds()));
                                tvMargin.setText(DoubleUtil.format2Decimal(result.getFrozenMargin()));
                                tvNet.setText(DoubleUtil.format2Decimal(result.getNetAssets()));
                                refresh.setRefreshing(false);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                LogUtils.e(throwable);
                                refresh.setRefreshing(false);
                            }
                        });
            }
        });

        tvOne.setOnClickListener(this);
        tvTwo.setOnClickListener(this);
        tvThree.setOnClickListener(this);
        tvFour.setOnClickListener(this);
    }

    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mine, container, false);
        mCustomServiceDialog = DialogUtils.createCustomServiceDialog(mContext);
        findViews(v);
        v.findViewById(R.id.btn_login).setOnClickListener(this);
        v.findViewById(R.id.btn_register).setOnClickListener(this);
        v.findViewById(R.id.tv_setting).setOnClickListener(this);
        v.findViewById(R.id.btn_deposit).setOnClickListener(this);
        v.findViewById(R.id.btn_withdraw).setOnClickListener(this);
        v.findViewById(R.id.tv_customer_service).setOnClickListener(this);
        return v;
    }

    @Override
    protected void initData() {
        super.initData();
        HttpManager.getBizService().getFunds()
                .compose(RxUtils.<BizResponse<Funds>>applyBizSchedulers())
                .compose(this.<BizResponse<Funds>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<BizResponse<Funds>>() {
                    @Override
                    public void accept(@NonNull BizResponse<Funds> fundsBizResponse) throws Exception {
                        Funds result = fundsBizResponse.getResult();
                        tvYue.setText(DoubleUtil.format2Decimal(result.getAvailableFunds()));
                        tvMargin.setText(DoubleUtil.format2Decimal(result.getFrozenMargin()));
                        tvNet.setText(DoubleUtil.format2Decimal(result.getNetAssets()));
                    }
                }, RxUtils.commonErrorConsumer());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                LoginActivity.startActivity(mContext);
                break;
            case R.id.btn_register:
                RegisterActivity.startActivity(mContext);
                break;
            case R.id.tv_setting:
                SettingActivity.startActivity(mContext);
                break;
            case R.id.tv_one:
                FundDetailActivity.startActivity(mContext);
                break;
            case R.id.tv_two:
                UserInfoActivity.startActivity(mContext);
                break;
            case R.id.tv_three:
                WebActivity.startActivity(mContext, "http://www.baidu.com");
                break;
            case R.id.tv_four:
                WebActivity.startActivity(mContext, "http://www.baidu.com");
                break;
            case R.id.btn_deposit:
                DepositActivity.startActivity(mContext);
                break;
            case R.id.btn_withdraw:
                WithdrawActivity.startActivity(mContext);
                break;
            case R.id.tv_customer_service:
                mCustomServiceDialog.show();
                break;
        }
    }
}
