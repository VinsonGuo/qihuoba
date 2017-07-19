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
import com.yjjr.yjfutures.model.AccountInfo;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.ui.WebActivity;
import com.yjjr.yjfutures.ui.trade.DepositActivity;
import com.yjjr.yjfutures.utils.DoubleUtil;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
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
    private CustomPromptDialog mLogoutDialog;

    public MineFragment() {
        // Required empty public constructor
    }

    private void findViews(View v) {
        mLogoutDialog = new CustomPromptDialog.Builder(mContext)
                .setMessage("您确定退出当前账号吗？")
                .isShowClose(true)
                .setMessageDrawableId(R.drawable.ic_info)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BaseApplication.getInstance().logout(getActivity());
                    }
                })
                .create();
        TextView tvOne = (TextView) v.findViewById(R.id.tv_one);
        TextView tvTwo = (TextView) v.findViewById(R.id.tv_two);
        TextView tvThree = (TextView) v.findViewById(R.id.tv_three);
        TextView tvFour = (TextView) v.findViewById(R.id.tv_four);
        TextView tvFive = (TextView) v.findViewById(R.id.tv_five);
        tvYue = (TextView) v.findViewById(R.id.tv_yue);
        tvMargin = (TextView) v.findViewById(R.id.tv_margin);
        final SwipeRefreshLayout refresh = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HttpManager.getHttpService().getAccountInfo(BaseApplication.getInstance().getTradeToken())
                        .compose(RxUtils.<AccountInfo>applySchedulers())
                        .compose(MineFragment.this.<AccountInfo>bindUntilEvent(FragmentEvent.DESTROY))
                        .subscribe(new Consumer<AccountInfo>() {
                            @Override
                            public void accept(@NonNull AccountInfo accountInfo) throws Exception {
                                refresh.setRefreshing(false);
                                tvYue.setText(DoubleUtil.format2Decimal(accountInfo.getAvailableFund()));
                                tvMargin.setText(DoubleUtil.format2Decimal(accountInfo.getFrozenMargin()));
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
        tvFive.setOnClickListener(this);
    }

    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mine, container, false);
        findViews(v);
        v.findViewById(R.id.btn_login).setOnClickListener(this);
        v.findViewById(R.id.btn_register).setOnClickListener(this);
        v.findViewById(R.id.tv_logout).setOnClickListener(this);
        v.findViewById(R.id.btn_deposit).setOnClickListener(this);
        v.findViewById(R.id.btn_withdraw).setOnClickListener(this);
        return v;
    }

    @Override
    protected void initData() {
        super.initData();
        HttpManager.getHttpService().getAccountInfo(BaseApplication.getInstance().getTradeToken())
                .retry()
                .compose(RxUtils.<AccountInfo>applySchedulers())
                .compose(this.<AccountInfo>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<AccountInfo>() {
                    @Override
                    public void accept(@NonNull AccountInfo accountInfo) throws Exception {
                        tvYue.setText(DoubleUtil.format2Decimal(accountInfo.getAvailableFund()));
                        tvMargin.setText(DoubleUtil.format2Decimal(accountInfo.getFrozenMargin()));
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
            case R.id.tv_logout:
                mLogoutDialog.show();
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
            case R.id.tv_five:
                WebActivity.startActivity(mContext, "http://www.baidu.com");
                break;
            case R.id.btn_deposit:
                DepositActivity.startActivity(mContext);
                break;
            case R.id.btn_withdraw:
                break;
        }
    }
}
