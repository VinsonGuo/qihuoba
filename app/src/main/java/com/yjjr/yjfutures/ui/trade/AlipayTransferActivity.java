package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.utils.AlipayUtil;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.widget.HeaderView;

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
        Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setSelected(true);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AlipayUtil.hasInstalledAlipayClient(mContext)) {
                    AlipayUtil.startAlipayClient(mContext, HttpConfig.ALIPAY_ACCOUNT_CODE);
                }else {
                    ToastUtils.show(mContext,"您还没安装支付宝，请在应用市场下载");
                }
            }
        });
    }
}
