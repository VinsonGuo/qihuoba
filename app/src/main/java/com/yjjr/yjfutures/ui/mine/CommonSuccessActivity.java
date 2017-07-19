package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.MainActivity;

public class CommonSuccessActivity extends BaseActivity {

    public static void startActivity(Context context,String info,String name,String value) {
        Intent intent = new Intent(context, CommonSuccessActivity.class);
        intent.putExtra(Constants.CONTENT_PARAMETER,info);
        intent.putExtra(Constants.CONTENT_PARAMETER_2,name);
        intent.putExtra(Constants.CONTENT_PARAMETER_3,value);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_success);
        final Intent intent = getIntent();
        String info = intent.getStringExtra(Constants.CONTENT_PARAMETER);
        String name = intent.getStringExtra(Constants.CONTENT_PARAMETER_2);
        String value = intent.getStringExtra(Constants.CONTENT_PARAMETER_3);
        TextView tvFinish = (TextView) findViewById(R.id.tv_finish);
        TextView tvInfo = (TextView) findViewById(R.id.tv_info);
        TextView tvName = (TextView) findViewById(R.id.tv_name);
        TextView tvValue = (TextView) findViewById(R.id.tv_value);

        tvInfo.setText(info);
        tvName.setText(name);
        tvValue.setText(value);
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.startActivity(mContext);
            }
        });
    }
}
