package com.yjjr.yjfutures.ui.found;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.HideRedDotEvent;
import com.yjjr.yjfutures.event.ShowRedDotEvent;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.ui.WebActivity;
import com.yjjr.yjfutures.utils.http.HttpConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 发现页面
 */
public class FoundFragment extends BaseFragment implements View.OnClickListener {


    private View mRedDot;

    public FoundFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_found, container, false);
        v.findViewById(R.id.root_view1).setOnClickListener(this);
        v.findViewById(R.id.root_view2).setOnClickListener(this);
        v.findViewById(R.id.root_view3).setOnClickListener(this);
        v.findViewById(R.id.root_view4).setOnClickListener(this);
        mRedDot = v.findViewById(R.id.reddot);
        return v;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ShowRedDotEvent event) {
        mRedDot.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.root_view1:
                mRedDot.setVisibility(View.GONE);
                EventBus.getDefault().post(new HideRedDotEvent());
                NoticeActivity.startActivity(mContext);
                break;
            case R.id.root_view2:
                WebActivity.startActivity(mContext, "http://www.baidu.com");
                break;
            case R.id.root_view3:
                WebActivity.startActivity(mContext, HttpConfig.URL_CSCENTER, WebActivity.TYPE_CSCENTER);
                break;
            case R.id.root_view4:
                WebActivity.startActivity(mContext, HttpConfig.URL_PROMOTION);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
