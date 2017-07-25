package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.ChargeResult;
import com.yjjr.yjfutures.model.biz.Info;
import com.yjjr.yjfutures.model.biz.NumberResult;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.utils.AlipayUtil;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.HeaderView;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class AlipayTransferActivity extends BaseActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, AlipayTransferActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay_transfer);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        headerView.bindActivity(mContext);
        final Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        final TextView tvAccountValue = (TextView) findViewById(R.id.tv_account_value);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!btnConfirm.isSelected())return;
                if(AlipayUtil.hasInstalledAlipayClient(mContext)) {
                    AlipayUtil.startAlipayClient(mContext, HttpConfig.ALIPAY_ACCOUNT_CODE);
                }else {
                    ToastUtils.show(mContext,"您还没安装支付宝，请在应用市场下载");
                }
            }
        });

        HttpManager.getBizService().getChargeInfo()
                .compose(RxUtils.<BizResponse<ChargeResult>>applyBizSchedulers())
                .compose(this.<BizResponse<ChargeResult>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<BizResponse<ChargeResult>>() {
                    @Override
                    public void accept(@NonNull BizResponse<ChargeResult> numberResultBizResponse) throws Exception {
                        btnConfirm.setSelected(true);
                        Info info = numberResultBizResponse.getResult().getAlipay();
                        HttpConfig.ALIPAY_ACCOUNT_CODE = info.getNameEn();
                        tvAccountValue.setText(info.getName());
                    }
                },RxUtils.commonErrorConsumer());
    }
}
