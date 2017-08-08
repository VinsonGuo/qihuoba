package com.yjjr.yjfutures.ui.trade;

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
import com.yjjr.yjfutures.event.FastTakeOrderEvent;
import com.yjjr.yjfutures.model.FastTakeOrderConfig;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.ContractInfo;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.WebActivity;
import com.yjjr.yjfutures.utils.DisplayUtils;
import com.yjjr.yjfutures.utils.DoubleUtil;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.CustomPromptDialog;
import com.yjjr.yjfutures.widget.HeaderView;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class FastTakeOrderActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {


    private CustomPromptDialog mDialog;
    private String mSymbol;
    private TextView mTvStopWin;
    private TextView mTvMargin;
    private TextView mTvTradeFee;
    private TextView mTvExchange;
    private RadioGroup mRgSl;
    private TextView mTvInfo;
    private ContractInfo mContractInfo;

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
        mSymbol = getIntent().getStringExtra(Constants.CONTENT_PARAMETER);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        final Button btnOpen = (Button) findViewById(R.id.btn_open);
        final RadioGroup rg = (RadioGroup) findViewById(R.id.rg_hand);
        mTvStopWin = (TextView) findViewById(R.id.tv_stop_win);
        mTvMargin = (TextView) findViewById(R.id.bzj_value);
        mTvTradeFee = (TextView) findViewById(R.id.trade_fee_value);
        mTvInfo = (TextView) findViewById(R.id.tv_info);
        mTvExchange = (TextView) findViewById(R.id.tv_exchange);
        mRgSl = (RadioGroup) findViewById(R.id.rg_sl);
        mRgSl.setOnCheckedChangeListener(this);
        AppCompatCheckBox checkBox = (AppCompatCheckBox) findViewById(R.id.cb_check);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btnOpen.setSelected(isChecked);
            }
        });
        checkBox.setChecked(true);
        findViewById(R.id.tv_agreement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.startActivity(mContext, HttpConfig.URL_AGREEMENT1);
            }
        });
        final FastTakeOrderConfig fastTakeOrder = UserSharePrefernce.getFastTakeOrder(mContext, mSymbol);
        btnOpen.setText(fastTakeOrder != null ? "关闭" : "开启");
        headerView.bindActivity(mContext);
        headerView.setMainTitle(mSymbol);
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnOpen.isSelected() || mContractInfo == null) {
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
                    double sl = Double.parseDouble(mTvMargin.getText().toString().replaceAll(",", "").trim());
                    double stopWin = sl * mContractInfo.getMaxProfitMultiply();
                    config.setStopLose(sl);
                    config.setStopWin(stopWin);
                }
                UserSharePrefernce.setFastTakeOrder(mContext, mSymbol, config);
                EventBus.getDefault().post(new FastTakeOrderEvent(config != null));
            }
        });
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
        HttpManager.getBizService().getContractInfo(mSymbol)
                .compose(RxUtils.<BizResponse<ContractInfo>>applyBizSchedulers())
                .compose(mContext.<BizResponse<ContractInfo>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<BizResponse<ContractInfo>>() {
                    @Override
                    public void accept(@NonNull BizResponse<ContractInfo> response) throws Exception {
                        mContractInfo = response.getResult();
                        mTvInfo.setText(String.format("持仓至%s自动平仓", mContractInfo.getEndTradeTime()));
                        mTvStopWin.setText(String.format("=触发止损*%s", mContractInfo.getMaxProfitMultiply()));
                        Map<String, Double> map = mContractInfo.getLossLevel();
                        for (Map.Entry<String, Double> next : map.entrySet()) {
                            mRgSl.addView(createRadioButton(next.getKey(), next.getValue()));
                        }
                        ((RadioButton) mRgSl.getChildAt(1)).setChecked(true);
                        mTvTradeFee.setText(DoubleUtil.formatDecimal(mContractInfo.getTransactionFee()));
                        Quote quote = StaticStore.sQuoteMap.get(mSymbol);
                        mTvExchange.setText("" + mContractInfo.getCnyExchangeRate());
                    }
                }, RxUtils.commonErrorConsumer());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            RadioButton rb = (RadioButton) group.getChildAt(i);
            if (rb.isChecked()) {
                Double d = (Double) rb.getTag();
                mTvMargin.setText(DoubleUtil.formatDecimal(d));
                break;
            }
        }
    }
}
