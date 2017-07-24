package com.yjjr.yjfutures.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.utils.http.HttpConfig;

/**
 * Created by dell on 2017/7/24.
 */

public class SmsCountDownTimer extends CountDownTimer {
    private TextView mTextView;
    public SmsCountDownTimer(TextView tv) {
        super(HttpConfig.SMS_TIME, 1000);
        mTextView = tv;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setText(((int) (millisUntilFinished / 1000)) + "秒");
    }

    @Override
    public void onFinish() {
        mTextView.setEnabled(true);
        mTextView.setText(R.string.phone_verify_code);
    }
}
