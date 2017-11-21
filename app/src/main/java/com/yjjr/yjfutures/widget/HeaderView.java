package com.yjjr.yjfutures.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.mine.InputPayPwdActivity;


/**
 * Created by hou on 2015/6/3.
 * <p>
 * 通用头view
 *
 * @attr ref R.styleable$headerView_main_size
 */
public class HeaderView extends LinearLayout implements View.OnClickListener {

    private ImageView mImage;
    private TextView mTextMainTitle;
    private TextView mTextSubtitle;

    private Activity mBindActivity;
    private View mReddot;

    public HeaderView(Context context) {
        this(context, null);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.view_header, this);
        View rootView = findViewById(R.id.root_view);
        mImage = (ImageView) findViewById(R.id.view_header_image_back);
        if (context instanceof InputPayPwdActivity) {
            mImage.setImageResource(R.drawable.ic_back_black);
        }
        mTextMainTitle = (TextView) findViewById(R.id.view_header_text_maintitle);
        mTextSubtitle = (TextView) findViewById(R.id.view_header_text_subtitle);
        mReddot = findViewById(R.id.reddot);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.headerView, defStyleAttr, 0);

        String mainTitle = a.getString(R.styleable.headerView_main_title);
        String subTitle = a.getString(R.styleable.headerView_sub_title);
        boolean isSingleLine = a.getBoolean(R.styleable.headerView_singleline, true);
        int bgColor = a.getColor(R.styleable.headerView_background_color, 0);
        int mainTitleColor = a.getColor(R.styleable.headerView_main_title_color, ContextCompat.getColor(context, R.color.main_text_color));
        if (bgColor != 0) {
            rootView.setBackgroundColor(bgColor);
        }

        mImage.setOnClickListener(this);


        mTextMainTitle.setText(mainTitle);
        mTextMainTitle.setTextColor(mainTitleColor);
        mTextMainTitle.setLines(isSingleLine ? 1 : 2);
        mTextSubtitle.setText(subTitle);

        a.recycle();
    }


    public void setMainTitle(int textRes) {
        mTextMainTitle.setText(textRes);
    }

    public void setMainTitleVisibility(int visibility) {
        mTextMainTitle.setVisibility(visibility);
    }

    public void setSubtitleVisible(int visible) {
        mTextSubtitle.setVisibility(visible);
    }

    public void setSubtitleText(int textRes) {
        mTextSubtitle.setText(textRes);
    }

    public String getSubtitleText() {
        return mTextSubtitle.getText().toString();
    }

    public void setSubtitleText(String textRes) {
        mTextSubtitle.setText(textRes);
    }

    public void setSubtitleBackground(int imageRes) {
        mTextSubtitle.setBackgroundResource(imageRes);
    }

    public TextView getSubTitle() {
        return mTextSubtitle;
    }

    public void setSubtitleTextColor(int color) {
        mTextSubtitle.setTextColor(getResources().getColor(color));
    }

    public String getMainTitle() {
        return mTextMainTitle.getText().toString().trim();
    }

    public void setMainTitle(CharSequence text) {
        mTextMainTitle.setText(text);
    }

    public void setOperateClickListener(OnClickListener listener) {
        mTextSubtitle.setOnClickListener(listener);
    }

    public void setBackImageClickListener(OnClickListener listener) {
        mImage.setOnClickListener(listener);
    }

    public void setOperateEnable(boolean enable) {
        mTextSubtitle.setEnabled(enable);
        int colorRes = enable ? R.color.main_color : R.color.third_text_color;
        setSubtitleTextColor(colorRes);
    }

    public void bindActivity(Activity activity) {
        mBindActivity = activity;
    }

    @Override
    public void onClick(View v) {
        if (null != mBindActivity && !mBindActivity.isFinishing()) {
            mBindActivity.finish();
        }
    }

    public void showRedDot(boolean isShow) {
        mReddot.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void setSubtitleClickListener(OnClickListener subtitleClickListener) {
        mTextSubtitle.setOnClickListener(subtitleClickListener);
    }
}
