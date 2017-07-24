package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.FinishEvent;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.RegisterInput;
import com.yjjr.yjfutures.widget.listener.TextWatcherAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AlterLoginPwdActivity extends BaseActivity {
    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, AlterLoginPwdActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_login_pwd);
        EventBus.getDefault().register(this);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        final RegisterInput riPwd = (RegisterInput) findViewById(R.id.ri_pwd);
        headerView.bindActivity(mContext);
        final Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        EditText etPassword = riPwd.getEtInput();
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etPassword.addTextChangedListener(new TextWatcherAdapter(){
            @Override
            public void afterTextChanged(Editable s) {
                btnConfirm.setSelected(!TextUtils.isEmpty(s));
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnConfirm.isSelected()) {
                    AlterLoginPwdActivity2.startActivity(mContext, riPwd.getValue());
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FinishEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
