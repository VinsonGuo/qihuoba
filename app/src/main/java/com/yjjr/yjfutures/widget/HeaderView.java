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
        View view = LayoutInflater.from(context).inflate(R.layout.view_header, this);
        mImage = (ImageView) view.findViewById(R.id.view_header_image_back);
        mTextMainTitle = (TextView) view.findViewById(R.id.view_header_text_maintitle);
        mTextSubtitle = (TextView) view.findViewById(R.id.view_header_text_subtitle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.headerView, defStyleAttr, 0);

        String mainTitle = a.getString(R.styleable.headerView_main_title);
        String subTitle = a.getString(R.styleable.headerView_sub_title);
        int backgroundRes = a.getResourceId(R.styleable.headerView_sub_backgroud, R.drawable.transport);
        boolean showImage = a.getBoolean(R.styleable.headerView_sub_back_icon, true);
        int enableColor = a.getColor(R.styleable.headerView_sub_title_color, getResources().getColor(R.color.main_color));
        int disableColor = a.getColor(R.styleable.headerView_sub_title_color, getResources().getColor(R.color.third_text_color));
        int visibility = showImage ? View.VISIBLE : View.GONE;
        mImage.setVisibility(visibility);
        mImage.setOnClickListener(this);

        //暂时没夹功能，先隐藏
        //mImage.setVisibility(View.GONE);

        mTextMainTitle.setText(mainTitle);
        mTextSubtitle.setText(subTitle);
        mTextSubtitle.setBackgroundResource(backgroundRes);
            mTextSubtitle.setTextColor(ContextCompat.getColor(context, R.color.color_666666));

        a.recycle();
    }


    public void setMainTitle(int textRes) {
        mTextMainTitle.setText(textRes);
    }

    public void setMainTitleVisibility(int visibility) {
        mTextMainTitle.setVisibility(visibility);
    }

    public void setMainTitle(String text) {
        mTextMainTitle.setText(text);
    }

    public void setSubtitleVisible(int visible) {
        mTextSubtitle.setVisibility(visible);
    }

    public void setSubtitleText(int textRes) {
        mTextSubtitle.setText(textRes);
    }

    public void setSubtitleText(String textRes) {
        mTextSubtitle.setText(textRes);
    }

    public String getSubtitleText() {
        return mTextSubtitle.getText().toString();
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

    public void setSubtitleClickListener(OnClickListener subtitleClickListener) {
        mTextSubtitle.setOnClickListener(subtitleClickListener);
    }
}
