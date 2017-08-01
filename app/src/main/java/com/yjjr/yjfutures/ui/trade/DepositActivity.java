package com.yjjr.yjfutures.ui.trade;

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

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.ContractInfo;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.utils.DialogUtils;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.SpannableUtil;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.CustomPromptDialog;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.RegisterInput;
import com.yjjr.yjfutures.widget.listener.TextWatcherAdapter;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class DepositActivity extends BaseActivity implements View.OnClickListener {

    private Button mBtnConfirm;
    private CustomPromptDialog mServiceDialog;
    private RegisterInput mRiMoney;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, DepositActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        mServiceDialog = DialogUtils.createCustomServiceDialog(mContext);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        headerView.bindActivity(mContext);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mBtnConfirm.setOnClickListener(this);
        mRiMoney = (RegisterInput) findViewById(R.id.ri_money);
        EditText etMoney = mRiMoney.getEtInput();
        etMoney.setInputType(InputType.TYPE_CLASS_NUMBER);
        etMoney.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                mBtnConfirm.setSelected(!TextUtils.isEmpty(s));
            }
        });
        TextView tvService = (TextView) findViewById(R.id.tv_contact_service);
        tvService.setText(TextUtils.concat(tvService.getText(), SpannableUtil.getStringByColor(mContext, "联系客服", R.color.main_color)));
        tvService.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                if (mBtnConfirm.isSelected()) {
                    mBtnConfirm.setSelected(false);
                    HttpManager.getBizService().rechargeApply(mRiMoney.getValue(), "alipay")
                            .compose(RxUtils.applyBizSchedulers())
                            .compose(mContext.<BizResponse>bindUntilEvent(ActivityEvent.DESTROY))
                            .subscribe(new Consumer<BizResponse>() {
                                @Override
                                public void accept(@NonNull BizResponse response) throws Exception {
                                    AlipayTransferActivity.startActivity(mContext);
                                    finish();
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    LogUtils.e(throwable);
                                    mBtnConfirm.setSelected(true);
                                    ToastUtils.show(mContext, throwable.getMessage());
                                }
                            });
                }
                break;
            case R.id.tv_contact_service:
                mServiceDialog.show();
                break;
        }
    }
}
