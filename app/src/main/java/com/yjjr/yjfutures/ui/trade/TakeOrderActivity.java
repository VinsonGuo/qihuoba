package com.yjjr.yjfutures.ui.trade;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.event.SendOrderEvent;
import com.yjjr.yjfutures.model.SendOrderResponse;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.CustomPromptDialog;
import com.yjjr.yjfutures.widget.HeaderView;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class TakeOrderActivity extends BaseActivity implements View.OnClickListener {

    public static final int TYPE_BUY = 0;
    public static final int TYPE_SELL = 1;
    private CustomPromptDialog mDialog;
    private ProgressDialog mProgressDialog;
    private String mSymbol;
    private int mType;
    private RadioGroup mRgHand;

    public static void startActivity(Context context, String symbol, int type) {
        Intent intent = new Intent(context, TakeOrderActivity.class);
        intent.putExtra(Constants.CONTENT_PARAMETER, symbol);
        intent.putExtra(Constants.CONTENT_PARAMETER_2, type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_order);
        Intent intent = getIntent();
        mSymbol = intent.getStringExtra(Constants.CONTENT_PARAMETER);
        mType = intent.getIntExtra(Constants.CONTENT_PARAMETER_2, TYPE_BUY);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        mRgHand = (RadioGroup) findViewById(R.id.rg_hand);
        mDialog = new CustomPromptDialog.Builder(mContext)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mContext.finish();
                    }
                })
                .create();
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.online_transaction_in_order));
        headerView.bindActivity(mContext);
        headerView.setMainTitle(mType == TYPE_BUY ? "买入" : "卖出" + "委托");
        findViewById(R.id.btn_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
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
               /* SoapObject rpc = new SoapObject(HttpConfig.NAME_SPACE, "SendOrder");
                rpc.addProperty("Account", BaseApplication.getInstance().getTradeToken());
                rpc.addProperty("symbol", mSymbol);
                rpc.addProperty("BuyType", mType == TYPE_BUY ? "买入" : "卖出");
                rpc.addProperty("Price", 0);
                rpc.addProperty("Qty", qty);
                rpc.addProperty("OrderType", "市价");
                RxUtils.createSoapObservable3("SendOrder", rpc)
                        .map(new Function<SoapObject, SoapObject>() {
                            @Override
                            public SoapObject apply(@NonNull SoapObject soapObject) throws Exception {
                                if (Integer.parseInt(soapObject.getProperty("ReturnCode").toString()) < 0) {
                                    throw new RuntimeException("下单失败");
                                }
                                return soapObject;
                            }
                        })
                        .compose(RxUtils.<SoapObject>applySchedulers())
                        .compose(this.<SoapObject>bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(new Consumer<SoapObject>() {
                            @Override
                            public void accept(@NonNull SoapObject soapObject) throws Exception {
                                mProgressDialog.dismiss();
                                ToastUtils.show(mContext, "成功");
                                EventBus.getDefault().post(new SendOrderEvent());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                mProgressDialog.dismiss();
                                ToastUtils.show(mContext, throwable.getMessage());
                            }
                        });*/

                HttpManager.getHttpService().sendOrder(BaseApplication.getInstance().getTradeToken(), mSymbol, mType == TYPE_BUY ? "买入" : "卖出", 0, qty, "市价")
                        .map(new Function<SendOrderResponse, SendOrderResponse>() {
                            @Override
                            public SendOrderResponse apply(@NonNull SendOrderResponse sendOrderResponse) throws Exception {
                                if (sendOrderResponse.getReturnCode() < 0) {
                                    throw new RuntimeException(sendOrderResponse.getMessage());
                                }
                                return sendOrderResponse;
                            }
                        })
                        .delay(1, TimeUnit.SECONDS)
                        .compose(RxUtils.<SendOrderResponse>applySchedulers())
                        .compose(this.<SendOrderResponse>bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(new Consumer<SendOrderResponse>() {
                            @Override
                            public void accept(@NonNull SendOrderResponse sendOrderResponse) throws Exception {
                                mProgressDialog.dismiss();
                                ToastUtils.show(mContext, sendOrderResponse.getMessage());
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
        }
    }
}
