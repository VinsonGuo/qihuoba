package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.WebActivity;
import com.yjjr.yjfutures.utils.AlipayUtil;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.widget.HeaderView;

import java.net.Socket;

public class DepositActivity extends BaseActivity implements View.OnClickListener {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, DepositActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        headerView.bindActivity(mContext);
        Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                if(AlipayUtil.hasInstalledAlipayClient(mContext)) {
                    AlipayUtil.startAlipayClient(mContext, "FKX02655U4OLYIE59HJ9CD");
                }else {
                    ToastUtils.show(mContext,"您还没安装支付宝，请在应用市场下载");
                }
                break;
        }
    }
}
