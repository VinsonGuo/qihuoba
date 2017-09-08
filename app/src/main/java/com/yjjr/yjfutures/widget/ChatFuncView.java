package com.yjjr.yjfutures.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.yjjr.yjfutures.R;

/**
 * Created by dell on 2017/9/8.
 */

public class ChatFuncView extends LinearLayout {

    private OnSelectListener mListener;

    public ChatFuncView(Context context) {
        this(context, null);
    }

    public ChatFuncView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_chat_func, this);
        init();
    }

    protected void init() {
        findViewById(R.id.tv_take_photo).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null) {
                    mListener.onSelect(1);
                }
            }
        });
        findViewById(R.id.tv_select_photo).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null) {
                    mListener.onSelect(0);
                }
            }
        });
    }

    public void setListener(OnSelectListener listener) {
        mListener = listener;
    }

    public interface OnSelectListener {
        void onSelect(int position);
    }
}