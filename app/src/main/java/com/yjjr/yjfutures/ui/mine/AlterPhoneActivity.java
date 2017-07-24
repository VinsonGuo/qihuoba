package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.FinishEvent;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.RegisterInput;
import com.yjjr.yjfutures.widget.listener.TextWatcherAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.regex.Pattern;

public class AlterPhoneActivity extends BaseActivity {
    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, AlterPhoneActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_phone);
        EventBus.getDefault().register(this);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        final RegisterInput riPhone = (RegisterInput) findViewById(R.id.ri_phone);
        EditText etPhone = riPhone.getEtInput();
        final TextView operaButton = riPhone.getOperaButton();
        headerView.bindActivity(mContext);
        final Pattern pattern = Pattern.compile(HttpConfig.REG_PHONE);
        etPhone.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                boolean matches = pattern.matcher(s.toString()).matches();
                operaButton.setEnabled(matches);

            }
        });
        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonSuccessActivity.startActivity(mContext,"手机号码更改成功","手机号码",riPhone.getValue());
            }
        });
        operaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
