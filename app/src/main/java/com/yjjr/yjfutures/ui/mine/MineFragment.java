package com.yjjr.yjfutures.ui.mine;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvOne;
    private TextView tvTwo;
    private TextView tvThree;
    private TextView tvFour;
    private TextView tvFive;
    private TextView tvSix;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-06-21 15:12:33 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews(View v) {
        tvOne = (TextView)v.findViewById( R.id.tv_one );
        tvTwo = (TextView)v.findViewById( R.id.tv_two );
        tvThree = (TextView)v.findViewById( R.id.tv_three );
        tvFour = (TextView)v.findViewById( R.id.tv_four );
        tvFive = (TextView)v.findViewById( R.id.tv_five );
        tvSix = (TextView)v.findViewById( R.id.tv_six );
    }

    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mine, container, false);
        findViews(v);
        v.findViewById(R.id.btn_login).setOnClickListener(this);
        v.findViewById(R.id.btn_register).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                LoginActivity.startActivity(mContext);
                break;
            case R.id.btn_register:
                break;
        }
    }
}
