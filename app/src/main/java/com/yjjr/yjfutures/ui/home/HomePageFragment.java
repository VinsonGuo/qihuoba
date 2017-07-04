package com.yjjr.yjfutures.ui.home;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.ui.trade.TradeActivity;
import com.yjjr.yjfutures.utils.imageloader.ImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoaderInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends BaseFragment {


    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_page, container, false);
        HomePageListFragment fragment = new HomePageListFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();
        fragment.setUserVisibleHint(true);
        return v;
    }
}
