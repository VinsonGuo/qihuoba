package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.widget.RegisterInput;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        RegisterInput riPhone = (RegisterInput) findViewById(R.id.ri_phone);
        RegisterInput riSmscode = (RegisterInput) findViewById(R.id.ri_smscode);
        RegisterInput riPassword = (RegisterInput) findViewById(R.id.ri_password);
        TextView operaButton = riSmscode.getOperaButton();
        operaButton.setSelected(true);
        findViewById(R.id.iv_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
