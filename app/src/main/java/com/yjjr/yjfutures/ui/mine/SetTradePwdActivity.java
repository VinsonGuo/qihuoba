package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import com.jungly.gridpasswordview.GridPasswordView;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.widget.HeaderView;

public class SetTradePwdActivity extends BaseActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, SetTradePwdActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_trade_pwd);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        final GridPasswordView pwdView = (GridPasswordView) findViewById(R.id.pwdView);
        final AppCompatCheckBox cbCheck = (AppCompatCheckBox) findViewById(R.id.cb_check);
        final Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        headerView.bindActivity(mContext);
        cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btnConfirm.setSelected(isChecked && pwdView.getPassWord().length() == 6);
            }
        });
        pwdView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                btnConfirm.setSelected(cbCheck.isChecked() && psw.length() == 6);
            }

            @Override
            public void onInputFinish(String psw) {

            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnConfirm.isSelected()) {

                }
            }
        });
    }
}
