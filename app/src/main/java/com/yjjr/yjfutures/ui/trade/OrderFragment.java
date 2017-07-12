package com.yjjr.yjfutures.ui.trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.widget.HeaderView;

public class OrderFragment extends BaseFragment {

    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, container, false);
        final HeaderView headerView = (HeaderView) v.findViewById(R.id.header_view);
        headerView.bindActivity(getActivity());
        headerView.setOperateClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettlementActivity.startActivity(mContext);
            }
        });
        return v;
    }


    @Override
    protected void initData() {
        super.initData();
        PositionListFragment positionListFragment = new PositionListFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, positionListFragment)
                .commit();
        positionListFragment.setUserVisibleHint(true);
    }
}
