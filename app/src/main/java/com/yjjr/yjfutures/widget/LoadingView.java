package com.yjjr.yjfutures.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yjjr.yjfutures.R;


/**
 * Created by hou on 2015/6/16.
 * <p>
 * 加载进度view
 */
public class LoadingView extends RelativeLayout {

    private LinearLayout mContainerPrompt;
    private ProgressBar mProgressBar;
    private TextView mTextPrompt;
    private Context mContext;
    private OnReloadListener mListener;

    private String NoDataPromptText;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        NoDataPromptText = mContext.getString(R.string.data_is_null);
        View view = LayoutInflater.from(context).inflate(R.layout.view_loading, this);
        mContainerPrompt = (LinearLayout) view.findViewById(R.id.view_loading_prompt);
        mProgressBar = (ProgressBar) view.findViewById(R.id.view_loading_prompt_image);
        mTextPrompt = (TextView) view.findViewById(R.id.view_loading_prompt_text);
    }


    /**
     * 数据为空
     * 数据取失败
     */
    public void loadFail() {
        mProgressBar.setVisibility(View.GONE);
        mTextPrompt.setText(NoDataPromptText);
    }


    private void reload() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTextPrompt.setText("");
    }

    public void setOnReloadListener(OnReloadListener l) {
        mListener = l;
        mContainerPrompt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
                mListener.onReload();
            }
        });
    }

    public interface OnReloadListener{
        void onReload();
    }
}
