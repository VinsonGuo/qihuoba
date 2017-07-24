package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.RegisterInput;
import com.yjjr.yjfutures.widget.listener.TextWatcherAdapter;

public class WithdrawActivity extends BaseActivity {

    private Button mBtnConfirm;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, WithdrawActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        headerView.bindActivity(mContext);
        RegisterInput riMoney = (RegisterInput) findViewById(R.id.ri_money);
        riMoney.getEtInput().addTextChangedListener(new TextWatcherAdapter(){
            @Override
            public void afterTextChanged(Editable s) {
                mBtnConfirm.setSelected(!TextUtils.isEmpty(s));
            }
        });
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBtnConfirm.isSelected()) {
                    InputPayPwdActivity.startActivity(mContext);
                }
            }
        });

    }
}
