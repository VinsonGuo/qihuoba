package com.yjjr.yjfutures.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.widget.listener.TextWatcherAdapter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2016/11/28.
 */

public class RegisterInput extends FrameLayout {

    private TextView mTvInput;
    private EditText mEtInput;
    private TextView mTvError;
    private TextView mTvOpera;
    private ImageView mIvDel;
    private boolean editable = true;
    private CountDownTimer countDownTimer;

    public RegisterInput(Context context) {
        this(context, null);
    }

    public RegisterInput(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RegisterInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            LayoutInflater.from(context).inflate(R.layout.view_register_input, this);
            final View rootView = findViewById(R.id.root_view);
            mTvInput = (TextView) findViewById(R.id.tv_input);
            mEtInput = (EditText) findViewById(R.id.et_input);
            mTvError = (TextView) findViewById(R.id.tv_error);
            mTvOpera = (TextView) findViewById(R.id.tv_opera);
            mIvDel = (ImageView) findViewById(R.id.iv_del);
            rootView.setSelected(mEtInput.hasFocus());
            RxView.focusChanges(mEtInput)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(RxView.selected(rootView));
            mEtInput.addTextChangedListener(new TextWatcherAdapter() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (editable) {
                        boolean empty = TextUtils.isEmpty(s.toString());
                        mIvDel.setVisibility(empty ? GONE : VISIBLE);
                    }
                }
            });
            RxView.clicks(mIvDel)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {
                            mEtInput.setText(null);
                        }
                    });
            RxView.focusChanges(mEtInput)
                    .map(new Function<Boolean, Boolean>() {
                        @Override
                        public Boolean apply(@NonNull Boolean aBoolean) throws Exception {
                            return aBoolean && !TextUtils.isEmpty(mEtInput.getText());
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(RxView.visibility(mIvDel));
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RegisterInput);
            String name = typedArray.getString(R.styleable.RegisterInput_name);
            String error = typedArray.getString(R.styleable.RegisterInput_error_name);
            String btnText = typedArray.getString(R.styleable.RegisterInput_button_text);
            String hint = typedArray.getString(R.styleable.RegisterInput_hint);
            String value = typedArray.getString(R.styleable.RegisterInput_value);

            mTvInput.setText(name);
            mTvError.setText(error);
            mTvOpera.setText(btnText);
            mTvOpera.setVisibility(!TextUtils.isEmpty(btnText) ? VISIBLE : GONE);
            mEtInput.setHint(hint);
            mEtInput.setText(value);
            typedArray.recycle();

        }
    }

    public void showError(String msg) {
        if (null != countDownTimer)
            countDownTimer.cancel();
        mTvError.setText(msg);
        mTvError.setVisibility(VISIBLE);
        countDownTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                mTvError.setText(null);
                mTvError.setVisibility(INVISIBLE);
            }
        };
        countDownTimer.start();
    }

    public void setName(String name) {
        mTvInput.setText(name);
    }

    public void setText(String text) {
        mEtInput.setText(text);
    }

    public void setText(@StringRes int res) {
        mEtInput.setText(res);
    }

    public EditText getEtInput() {
        return mEtInput;
    }

    public void setValue(String value) {
        mEtInput.setText(value);
    }

    public String getValue() {
        return mEtInput.getText().toString();
    }

    public void setValue(long value) {
        mEtInput.setText("" + value);
    }

    public TextView getOperaButton() {
        return mTvOpera;
    }

    /**
     * 将编辑框设置为不可编辑
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
        mEtInput.setFocusable(editable);
        mEtInput.setFocusableInTouchMode(editable);
        mEtInput.setEnabled(editable);
        if (!editable) {
            mIvDel.setVisibility(GONE);
            mEtInput.setTextColor(ContextCompat.getColor(getContext(), R.color.color_737375));
            mTvInput.setTextColor(ContextCompat.getColor(getContext(), R.color.color_737375));
        } else {
            mIvDel.setVisibility(TextUtils.isEmpty(mEtInput.getText().toString()) ? GONE : VISIBLE);
            mEtInput.setTextColor(ContextCompat.getColor(getContext(), R.color.color_333333));
            mTvInput.setTextColor(ContextCompat.getColor(getContext(), R.color.color_333333));
        }
    }


    public void setOnFocusChangeListener(OnFocusChangeListener f) {
        mEtInput.setOnFocusChangeListener(f);
    }

}
