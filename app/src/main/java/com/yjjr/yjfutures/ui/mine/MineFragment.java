package com.yjjr.yjfutures.ui.mine;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.AccountInfo;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.utils.DoubleUtil;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * mine fragment
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvOne;
    private TextView tvTwo;
    private TextView tvThree;
    private TextView tvFour;
    private TextView tvFive;
    private TextView tvYue;
    private TextView tvMargin;

    public MineFragment() {
        // Required empty public constructor
    }

    private void findViews(View v) {
        tvOne = (TextView) v.findViewById(R.id.tv_one);
        tvTwo = (TextView) v.findViewById(R.id.tv_two);
        tvThree = (TextView) v.findViewById(R.id.tv_three);
        tvFour = (TextView) v.findViewById(R.id.tv_four);
        tvFive = (TextView) v.findViewById(R.id.tv_five);
        tvYue = (TextView) v.findViewById(R.id.tv_yue);
        tvMargin = (TextView) v.findViewById(R.id.tv_margin);

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
                BaseApplication.getInstance().logout(getActivity());
                break;
            case R.id.tv_one:
                FundDetailActivity.startActivity(mContext);
                break;
            case R.id.tv_two:
                UserInfoActivity.startActivity(mContext);
                break;
        }
    }
}
