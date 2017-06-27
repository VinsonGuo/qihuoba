package com.yjjr.yjfutures.ui.trade;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.widget.CustomPromptDialog;
import com.yjjr.yjfutures.widget.HeaderView;

public class TakeOrderActivity extends BaseActivity implements View.OnClickListener {

    private CustomPromptDialog mDialog;
    private ProgressDialog mProgressDialog;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, TakeOrderActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_order);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        mDialog = new CustomPromptDialog.Builder(mContext)
                .setMessage("买入委托已成交")
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
        findViewById(R.id.btn_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                mProgressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog.dismiss();
                        mDialog.show();
                    }
                }, 3000);
                break;
        }
    }
}
