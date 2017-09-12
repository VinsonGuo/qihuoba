package com.yjjr.yjfutures.ui.mine;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yjjr.yjfutures.BuildConfig;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.store.ConfigSharePrefernce;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.ui.MainActivity;
import com.yjjr.yjfutures.widget.CustomPromptDialog;

public class GuideFragment extends BaseFragment implements View.OnClickListener {

    private int index;

    public GuideFragment() {
        // Required empty public constructor
    }

    public static GuideFragment newInstance(int param) {
        GuideFragment fragment = new GuideFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.CONTENT_PARAMETER, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(Constants.CONTENT_PARAMETER);
        }
    }

    @Override
    public View initViews(LayoutInflater inflater, ViewGroup container,
                          Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_guide, container, false);
        ImageView ivBg = (ImageView) v.findViewById(R.id.iv_bg);
        View tvTop = v.findViewById(R.id.tv_top);
        View tvBottom = v.findViewById(R.id.tv_bottom);
        switch (index) {
            case 1:
                tvTop.setVisibility(View.VISIBLE);
                tvBottom.setVisibility(View.GONE);
                ivBg.setImageResource(R.drawable.step1);
                break;
            case 2:
                tvTop.setVisibility(View.VISIBLE);
                tvBottom.setVisibility(View.GONE);
                ivBg.setImageResource(R.drawable.step2);
                break;
            case 3:
                tvTop.setVisibility(View.VISIBLE);
                tvBottom.setVisibility(View.GONE);
                ivBg.setImageResource(R.drawable.step3);
                break;
            case 4:
                tvTop.setVisibility(View.VISIBLE);
                tvBottom.setVisibility(View.GONE);
                ivBg.setImageResource(R.drawable.step4);
                break;
            case 5:
                tvTop.setVisibility(View.GONE);
                tvBottom.setVisibility(View.VISIBLE);
                ivBg.setImageResource(R.drawable.step5);
                break;
        }
        tvTop.setOnClickListener(this);
        tvBottom.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if (BaseApplication.getInstance().isLogin()) {
            MainActivity.startActivity(mContext);
        } else {
            LoginActivity.startActivity(mContext);
        }
        getActivity().finish();
    }
}
