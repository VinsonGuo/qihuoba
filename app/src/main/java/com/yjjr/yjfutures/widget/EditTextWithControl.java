package com.yjjr.yjfutures.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.utils.ArithUtils;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.StringUtils;


/**
 * Created by guoziwei on 2016/3/28.
 */
public class EditTextWithControl extends LinearLayout implements View.OnClickListener, View.OnFocusChangeListener {

    private EditText mEtContent;
    private float mStep = 0.01f;
    private String mEmptyValue = "0.01";
    private OnClickListener mOnBtnClickListener;
    private OnVerifyListener mOnVerifyListener;
    private TextView mTvError;
    private int mDigitsAfterZero = 2;
    private ImageView mIvPlus;
    private ImageView mIvMinus;

    public EditTextWithControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_edittext_with_control, this);
        mIvPlus = (ImageView) findViewById(R.id.iv_plus);
        mIvMinus = (ImageView) findViewById(R.id.iv_minus);
        mEtContent = (EditText) findViewById(R.id.et_content);
        mTvError = (TextView) findViewById(R.id.tv_error);
        mEtContent.setOnFocusChangeListener(this);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EditTextWithControl);
        mStep = array.getFloat(R.styleable.EditTextWithControl_etwc_step, getDefaultStep());
        float value = array.getFloat(R.styleable.EditTextWithControl_etwc_value, 0.01f);
        String errorText = array.getString(R.styleable.EditTextWithControl_etwc_error_text);
        mTvError.setText(errorText);
        if (value == 0) {
            mEtContent.setText("");
        } else {
            mEtContent.setText(String.valueOf(value).replace(",", ""));
        }
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (s.toString().charAt(0) == '.') {
                        mEtContent.setText(s.toString().replace(".", ""));
                    }
                }
            }
        });
        array.recycle();
        mIvMinus.setOnClickListener(this);
        mIvPlus.setOnClickListener(this);
        setDigitsAfterZero(2);
    }

    private float getDefaultStep() {
        return 0.01f;
    }


    public EditTextWithControl(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditTextWithControl(Context context) {
        this(context, null);
    }

    @Override
    public void onClick(View v) {
        String content = mEtContent.getText().toString().trim().replace(",", "");

        if (TextUtils.isEmpty(content)) {
            mEtContent.setText(mEmptyValue);
            mEtContent.setSelection(mEtContent.length());
            return;
        }
        mEtContent.requestFocus(); // 用于触发focus事件

        switch (v.getId()) {
            case R.id.iv_plus: {
                if (!TextUtils.isDigitsOnly(content)) {
                    float f = Float.parseFloat(content);
                    mEtContent.setText(StringUtils.getStringByDigits(ArithUtils.add(f, mStep), getDigits()));
                } else {
                    int i = Integer.parseInt(content);
                    mEtContent.setText(String.valueOf(i + 1));
                }
                mEtContent.setSelection(mEtContent.length());
                if (mOnBtnClickListener != null) mOnBtnClickListener.onPlusClick();
                break;
            }
            case R.id.iv_minus: {
                if (!TextUtils.isDigitsOnly(content)) {
                    float f = Float.parseFloat(content);
                    float result = ArithUtils.sub(f, mStep);
                    if (result <= 0) return;
                    mEtContent.setText(StringUtils.getStringByDigits(result, getDigits()));
                } else {
                    int i = Integer.parseInt(content);
                    int result = i - 1;
                    if (result <= 0) return;
                    mEtContent.setText(String.valueOf(result));
                }
                mEtContent.setSelection(mEtContent.length());
                if (mOnBtnClickListener != null) mOnBtnClickListener.onMiusClick();
                break;
            }
        }
    }

    /**
     * 加
     */
    public void plus() {
        if (mIvPlus != null && getValue() != 0) {
            String content = mEtContent.getText().toString().trim().replace(",", "");
            if (!TextUtils.isDigitsOnly(content)) {
                float f = Float.parseFloat(content);
                mEtContent.setText(StringUtils.getStringByDigits(ArithUtils.add(f, mStep), getDigits()));
            } else {
                int i = Integer.parseInt(content);
                mEtContent.setText(String.valueOf(i + 1));
            }
        }
    }

    /**
     * 减
     */
    public void mius() {
        if (mIvMinus != null && getValue() != 0) {
            String content = mEtContent.getText().toString().trim().replace(",", "");
            if (!TextUtils.isDigitsOnly(content)) {
                float f = Float.parseFloat(content);
                float result = ArithUtils.sub(f, mStep);
                if (result <= 0) return;
                mEtContent.setText(StringUtils.getStringByDigits(result, getDigits()));
            } else {
                int i = Integer.parseInt(content);
                int result = i - 1;
                if (result <= 0) return;
                mEtContent.setText(String.valueOf(result));
            }
        }
    }

    public double getValue() {
        String s = mEtContent.getText().toString().trim();
        if (TextUtils.isEmpty(s)) {
            return 0;
        }
        try {
            double d = Double.valueOf(s);
            return d;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void setValue(String s, int digits) {
        mEtContent.setText(StringUtils.getStringByDigits(s.replaceAll(",", ""), digits));
    }

    public String getValueStr() {
        return mEtContent.getText().toString().trim();
    }

    public void setValue(double f, int digits) {
        mEtContent.setText(StringUtils.getStringByDigits(f, digits));
    }

    public void setValue(double f) {
        mEtContent.setText(StringUtils.getStringByDigits(f, 2));
    }

    /**
     * 针对止损和止盈，因为这两个选项默认为空，点击加减然后再设置一个数值
     *
     * @param s
     */
    public void setEmptyValue(String s) {
        mEmptyValue = s;
    }


    public void setStep(float f) {
        mStep = f;
    }

    public void setOnBtnClickListener(OnClickListener onBtnClickListener) {
        mOnBtnClickListener = onBtnClickListener;
    }

    public void setOnVerifyListener(OnVerifyListener listener) {
        mOnVerifyListener = listener;
    }

    /**
     * 设置错误信息的文本
     *
     * @param s
     */
    public void setError(String s) {
        if (mTvError == null) return;
        mTvError.setText(s);
    }

    public void setError(int s) {
        if (mTvError == null) return;
        mTvError.setText(s);
    }

    /**
     * 显示错误，在2s后自动消失
     */
    public void showError() {
        if (mTvError == null) return;
        mTvError.setVisibility(VISIBLE);
        mTvError.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTvError.setVisibility(INVISIBLE);
            }
        }, 3000);
    }

    private int getDigits() {
        return mDigitsAfterZero;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // 点击输入框设置默认的止损止盈
        if (hasFocus && TextUtils.isEmpty(mEtContent.getText().toString().trim())) {
            mEtContent.setText(mEmptyValue);
        }
        if (mOnVerifyListener != null) {
            if (!hasFocus && !TextUtils.isEmpty(mEtContent.getText().toString().trim())) {
                mOnVerifyListener.onVerify();
            }
        }

    }

    /**
     * 设置小数点后面
     *
     * @param digitsAfterZero
     */
    public void setDigitsAfterZero(int digitsAfterZero) {
        mDigitsAfterZero = digitsAfterZero;
        try {
            mEtContent.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(mDigitsAfterZero)});
        } catch (Exception e) {
            LogUtils.e(e);
//            mEtContent.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10, 1)});
        }
    }


    public interface OnClickListener {
        void onPlusClick();

        void onMiusClick();
    }

    public interface OnVerifyListener {
        void onVerify();
    }

    public static class DecimalDigitsInputFilter implements InputFilter {

        private final int decimalDigits;

        /**
         * Constructor.
         *
         * @param decimalDigits maximum decimal digits
         */
        public DecimalDigitsInputFilter(int decimalDigits) {
            this.decimalDigits = decimalDigits;
        }

        @Override
        public CharSequence filter(CharSequence source,
                                   int start,
                                   int end,
                                   Spanned dest,
                                   int dstart,
                                   int dend) {


            int dotPos = -1;
            int len = dest.length();
            for (int i = 0; i < len; i++) {
                char c = dest.charAt(i);
                if (c == '.' || c == ',') {
                    dotPos = i;
                    break;
                }
            }
            if (dotPos >= 0) {

                // protects against many dots
                if (source.equals(".") || source.equals(",")) {
                    return "";
                }
                // if the text is entered before the dot
                if (dend <= dotPos) {
                    return null;
                }
                if (len - dotPos > decimalDigits) {
                    return "";
                }
            }

            return null;
        }
    }
}
