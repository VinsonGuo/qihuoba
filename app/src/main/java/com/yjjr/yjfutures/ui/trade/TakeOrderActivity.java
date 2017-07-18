package com.yjjr.yjfutures.ui.trade;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.event.SendOrderEvent;
import com.yjjr.yjfutures.model.CommonResponse;
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
    private ProgressDialog mProgressDialog;
    private String mSymbol;
    private int mType;
    private RadioGroup mRgHand;
    private CustomPromptDialog mDialog;

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
        mDialog= new CustomPromptDialog.Builder(mContext)
                .setMessage((mType == TYPE_BUY ? "买入" : "卖出")+"委托成功")
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mContext.finish();
                    }
                })
                .create();
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        mRgHand = (RadioGroup) findViewById(R.id.rg_hand);
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.online_transaction_in_order));
        headerView.bindActivity(mContext);
        headerView.setMainTitle(mType == TYPE_BUY ? "买入" : "卖出" + "委托");
        Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setSelected(true);
        btnConfirm.setOnClickListener(this);
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

                HttpManager.getHttpService().sendOrder(BaseApplication.getInstance().getTradeToken(), mSymbol, mType == TYPE_BUY ? "买入" : "卖出", 0, qty, "市价")
                        .map(new Function<CommonResponse, CommonResponse>() {
                            @Override
                            public CommonResponse apply(@NonNull CommonResponse commonResponse) throws Exception {
                                if (commonResponse.getReturnCode() < 0) {
                                    throw new RuntimeException(commonResponse.getMessage());
                                }
                                return commonResponse;
                            }
                        })
                        .delay(1, TimeUnit.SECONDS)
                        .compose(RxUtils.<CommonResponse>applySchedulers())
                        .compose(this.<CommonResponse>bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(new Consumer<CommonResponse>() {
                            @Override
                            public void accept(@NonNull CommonResponse commonResponse) throws Exception {
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
        }
    }
}
