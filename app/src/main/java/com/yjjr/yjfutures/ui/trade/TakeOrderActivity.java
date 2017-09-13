package com.yjjr.yjfutures.ui.trade;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.event.PollRefreshEvent;
import com.yjjr.yjfutures.event.PriceRefreshEvent;
import com.yjjr.yjfutures.event.SendOrderEvent;
import com.yjjr.yjfutures.model.CommonResponse;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.ContractInfo;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.WebActivity;
import com.yjjr.yjfutures.utils.ArithUtils;
import com.yjjr.yjfutures.utils.DisplayUtils;
import com.yjjr.yjfutures.utils.DoubleUtil;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.StringUtils;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.CustomPromptDialog;
import com.yjjr.yjfutures.widget.HeaderView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class TakeOrderActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    public static final int TYPE_BUY = 0;
    public static final int TYPE_SELL = 1;
    private ProgressDialog mProgressDialog;
    private String mSymbol;
    private int mType;
    private RadioGroup mRgHand;
    private CustomPromptDialog mDialog;
    private TextView mTvSymbol;
    private TextView mTvInfo;
    private TextView mTvStopWin;
    private RadioGroup mRgSl;
    private TextView mTvMargin;
    private TextView mTvMarginDollar;
    private TextView mTvTradeFee;
    private TextView mTvExchange;
    private TextView mTvPrice;
    private String mBuySell;
    private boolean mIsDemo;
    private ContractInfo mContractInfo;

    private int mHand = 1;

    public static void startActivity(Context context, String symbol, int type, boolean isDemo) {
        Intent intent = new Intent(context, TakeOrderActivity.class);
        intent.putExtra(Constants.CONTENT_PARAMETER, symbol);
        intent.putExtra(Constants.CONTENT_PARAMETER_2, type);
        intent.putExtra(Constants.CONTENT_PARAMETER_3, isDemo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_order);
        Intent intent = getIntent();
        mSymbol = intent.getStringExtra(Constants.CONTENT_PARAMETER);
        mType = intent.getIntExtra(Constants.CONTENT_PARAMETER_2, TYPE_BUY);
        mIsDemo = intent.getBooleanExtra(Constants.CONTENT_PARAMETER_3, false);
        mBuySell = mType == TYPE_BUY ? "买入" : "卖出";
        mDialog = new CustomPromptDialog.Builder(mContext)
                .setMessage(mBuySell + "委托成功")
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mContext.finish();
                    }
                })
                .create();
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        mTvSymbol = (TextView) findViewById(R.id.tv_symbol);
        mTvInfo = (TextView) findViewById(R.id.tv_info);
        mTvStopWin = (TextView) findViewById(R.id.tv_stop_win);
        mTvMargin = (TextView) findViewById(R.id.bzj_value);
        mTvMarginDollar = (TextView) findViewById(R.id.bzj_dollar_value);
        mTvTradeFee = (TextView) findViewById(R.id.trade_fee_value);
        mTvPrice = (TextView) findViewById(R.id.tv_price);
        mTvExchange = (TextView) findViewById(R.id.tv_exchange);
        mRgHand = (RadioGroup) findViewById(R.id.rg_hand);
        mRgSl = (RadioGroup) findViewById(R.id.rg_sl);
        TextView tvDesc = (TextView) findViewById(R.id.tv_desc);
        tvDesc.setText(mIsDemo ? "实盘交易实时为您自动匹配合作投资人，执行您的指令，并与您共享收益共担风险。" : StringUtils.randomTrader() + "为您本笔交易合作投资人，执行您的交易指令，并与您共享收益共担风险。");
        mRgSl.setOnCheckedChangeListener(this);
        mRgHand.setOnCheckedChangeListener(this);
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.online_transaction_in_order));
        headerView.bindActivity(mContext);
        headerView.setMainTitle(mBuySell + "委托");
        final Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setText(mType == TYPE_BUY ? "确定买涨" : "确定买跌");
        AppCompatCheckBox cbCheck = (AppCompatCheckBox) findViewById(R.id.cb_check);
        cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btnConfirm.setSelected(isChecked);
            }
        });
        cbCheck.setChecked(true);
        btnConfirm.setOnClickListener(this);
        findViewById(R.id.tv_agreement).setOnClickListener(this);
        requestData();
    }

    /**
     * <RadioButton
     * android:id="@+id/rb_sl_1"
     * android:layout_width="46dp"
     * android:layout_height="17dp"
     * android:background="@drawable/selector_trade_rb_bg"
     * android:button="@null"
     * android:gravity="center"
     * android:text="$170"
     * android:textColor="@color/selector_trade_rb_text_color"
     * android:textSize="12sp"/>
     */
    private RadioButton createRadioButton(String name, Double tag) {
        RadioButton rb = new RadioButton(mContext);
        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(DisplayUtils.dip2px(mContext, 46), DisplayUtils.dip2px(mContext, 17));
        lp.setMargins(DisplayUtils.dip2px(mContext, 8), 0, 0, 0);
        rb.setLayoutParams(lp);
        rb.setBackgroundResource(R.drawable.selector_trade_rb_bg);
        rb.setButtonDrawable(null);
        rb.setGravity(Gravity.CENTER);
        rb.setText(name);
        rb.setTextColor(ContextCompat.getColorStateList(mContext, R.color.selector_trade_rb_text_color));
        rb.setTextSize(12);
        rb.setTag(tag);
        return rb;
    }

    private void requestData() {
        HttpManager.getBizService(mIsDemo).getContractInfo(mSymbol)
                .compose(RxUtils.<BizResponse<ContractInfo>>applyBizSchedulers())
                .compose(mContext.<BizResponse<ContractInfo>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<BizResponse<ContractInfo>>() {
                    @Override
                    public void accept(@NonNull BizResponse<ContractInfo> response) throws Exception {
                        mContractInfo = response.getResult();
                        mTvSymbol.setText(mContractInfo.getSymbolName());
                        mTvInfo.setText(String.format("持仓至%s自动平仓", mContractInfo.getEndTradeTime()));
                        Map<String, Double> map = mContractInfo.getLossLevel();
                        for (Map.Entry<String, Double> next : map.entrySet()) {
                            mRgSl.addView(createRadioButton(next.getKey(), Double.parseDouble(next.getKey())));
                        }
                        ((RadioButton) mRgSl.getChildAt(1)).setChecked(true);
                        Quote quote = StaticStore.getQuote(mSymbol, mIsDemo);
                        mTvExchange.setText(quote.getSymbolname() + "按" + StringUtils.currency2Word(quote.getCurrency()) + "交易，平台按人民币结算，汇率为 " + StringUtils.getCurrencySymbol(quote.getCurrency()) + "1 = ￥" + mContractInfo.getCnyExchangeRate());
                        mTvPrice.setText(String.format("即时%s(最新%s价%s)", mBuySell, mBuySell, TextUtils.equals("买入", mBuySell) ? quote.getAskPrice() : quote.getBidPrice()));

//                        DialogUtils.showGuideView(mContext, R.id.rb_hand_1, mRgSl.getCheckedRadioButtonId(), R.id.btn_confirm);
                    }
                }, RxUtils.commonErrorConsumer());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PriceRefreshEvent event) {
        if (TextUtils.equals(event.getSymbol(), mSymbol)) {
            Quote quote = StaticStore.getQuote(mSymbol, mIsDemo);
            mTvPrice.setText(String.format("即时%s(最新%s价%s)", mBuySell, mBuySell, TextUtils.equals("买入", mBuySell) ? quote.getAskPrice() : quote.getBidPrice()));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PollRefreshEvent event) {
        Quote quote = StaticStore.getQuote(mSymbol, mIsDemo);
        mTvPrice.setText(String.format("即时%s(最新%s价%s)", mBuySell, mBuySell, TextUtils.equals("买入", mBuySell) ? quote.getAskPrice() : quote.getBidPrice()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                if (!v.isSelected() || mContractInfo == null) return;
                mProgressDialog.show();
                int qty = 1;
                int id = mRgHand.getCheckedRadioButtonId();
                if (id == R.id.rb_hand_1) {
                    qty = 1;
                } else if (id == R.id.rb_hand_2) {
                    qty = 2;
                } else if (id == R.id.rb_hand_3) {
                    qty = 3;
                } else if (id == R.id.rb_hand_4) {
                    qty = 4;
                } else if (id == R.id.rb_hand_5) {
                    qty = 5;
                }

                Double sl = (Double) mRgSl.findViewById(mRgSl.getCheckedRadioButtonId()).getTag();
                RxUtils.createSendOrderObservable(mIsDemo, mSymbol, mType == TYPE_BUY ? "买入" : "卖出", qty,
                        sl, sl * mContractInfo.getMaxProfitMultiply(), (double) mTvTradeFee.getTag(), (double) mTvMargin.getTag())
                        .delay(1, TimeUnit.SECONDS)
                        .compose(RxUtils.<BizResponse<CommonResponse>>applyBizSchedulers())
                        .compose(this.<BizResponse<CommonResponse>>bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(new Consumer<BizResponse<CommonResponse>>() {
                            @Override
                            public void accept(@NonNull BizResponse<CommonResponse> commonResponse) throws Exception {
                                mProgressDialog.dismiss();
                                mDialog.show();
                                EventBus.getDefault().post(new SendOrderEvent());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                LogUtils.e(throwable);
                                mProgressDialog.dismiss();
                                ToastUtils.show(mContext, throwable.getMessage());
                            }
                        });
                break;
            case R.id.tv_agreement:
                WebActivity.startActivity(mContext, HttpConfig.URL_AGREEMENT1);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        int childCount = group.getChildCount();
        if (group == mRgSl) {
            // 止损
            for (int i = 0; i < childCount; i++) {
                RadioButton rb = (RadioButton) group.getChildAt(i);
                if (rb.isChecked()) {
                    double sl = Double.parseDouble(rb.getText().toString());
                    double margin = sl * mHand;
//                    Double marginDollar = ArithUtils.mul((Double) rb.getTag(), mHand);
                    Double marginDollar = mContractInfo.getLossLevel().get(rb.getText().toString()) * mHand;
                    Double tradeFee = ArithUtils.mul(mContractInfo.getTransactionFee(), mContractInfo.getCnyExchangeRate(), mHand);
                    mTvStopWin.setText(DoubleUtil.formatDecimal(sl * mContractInfo.getMaxProfitMultiply()));
                    mTvMargin.setText(getString(R.string.rmb_symbol) + DoubleUtil.formatDecimal(margin));
                    mTvMargin.setTag(margin);
                    Quote quote = StaticStore.getQuote(mSymbol, mIsDemo);
                    mTvMarginDollar.setText(String.format("(%s%s)", StringUtils.getCurrencySymbol(quote.getCurrency()), DoubleUtil.formatDecimal(marginDollar)));
                    mTvMarginDollar.setTag(marginDollar);
                    mTvTradeFee.setText(DoubleUtil.format2Decimal(tradeFee) + "元");
                    mTvTradeFee.setTag(tradeFee);
                    break;
                }
            }
        } else if (group == mRgHand) {
            RadioButton rb = (RadioButton) mRgSl.findViewById(mRgSl.getCheckedRadioButtonId());
            // 手数
            for (int i = 0; i < childCount; i++) {
                RadioButton radioButton = (RadioButton) group.getChildAt(i);
                if (radioButton.isChecked()) {
                    mHand = i + 1;
                    Double tradeFee = ArithUtils.mul(mContractInfo.getTransactionFee(), mContractInfo.getCnyExchangeRate(), mHand);
                    double sl = Double.parseDouble(rb.getText().toString());
                    double margin = sl * mHand;
                    Double marginDollar = mContractInfo.getLossLevel().get(rb.getText().toString()) * mHand;
//                    Double marginDollar = mContractInfo.getLossjb().get(margin);
                    mTvMargin.setText(getString(R.string.rmb_symbol) + DoubleUtil.formatDecimal(margin));
                    mTvMargin.setTag(margin);
                    mTvMarginDollar.setText(String.format("($%s)", DoubleUtil.formatDecimal(marginDollar)));
                    mTvTradeFee.setText(DoubleUtil.format2Decimal(tradeFee) + "元");
                    mTvTradeFee.setTag(tradeFee);
                    break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
