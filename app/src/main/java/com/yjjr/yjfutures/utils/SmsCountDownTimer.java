package com.yjjr.yjfutures.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.widget.RegisterInput;

import java.util.regex.Pattern;

/**
 * Created by dell on 2017/7/24.
 */

public class SmsCountDownTimer extends CountDownTimer {

    private final RegisterInput mRiPhone;
    private TextView mTextView;
    private Pattern pattern = Pattern.compile(HttpConfig.REG_PHONE);

    public SmsCountDownTimer(TextView tv, RegisterInput riPhone) {
        super(HttpConfig.SMS_TIME, 1000);
        mTextView = tv;
        mRiPhone = riPhone;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setText(String.format("请重试(%s)", (int) (millisUntilFinished / 1000)));
    }

    @Override
    public void onFinish() {
        mTextView.setEnabled(pattern.matcher(mRiPhone.getValue()).matches());
        mTextView.setText(R.string.get_confirm_code);
    }
}
