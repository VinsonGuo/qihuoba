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
import com.yjjr.yjfutures.event.RefreshEvent;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.ui.trade.TradeActivity;
import com.yjjr.yjfutures.utils.imageloader.ImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoaderInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends BaseFragment implements View.OnClickListener {


    private Banner mBanner;
    private HomePageAdapter mAdapter;

    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_page, container, false);
        RecyclerView rvList = (RecyclerView) v.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new HomePageAdapter(null);
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.header_home_page, rvList, false);
        mBanner = (Banner) headerView.findViewById(R.id.banner);
        headerView.findViewById(R.id.tv_title1).setOnClickListener(this);
        mAdapter.addHeaderView(headerView);
        mBanner.setImageLoader(new ImageLoaderInterface() {
            @Override
            public void displayImage(Context context, Object path, View imageView) {
                ImageLoader.load(context, (String) path, (ImageView) imageView);
            }

            @Override
            public View createImageView(Context context) {
                return new ImageView(context);
            }
        });
        List<String> images = new ArrayList<>();
        images.add("http://img1.imgtn.bdimg.com/it/u=1089399937,1684001946&fm=26&gp=0.jpg");
        images.add("http://img3.imgtn.bdimg.com/it/u=3241219306,1400876595&fm=26&gp=0.jpg");
        images.add("http://img4.imgtn.bdimg.com/it/u=787324823,4149955059&fm=26&gp=0.jpg");
        mBanner.setImages(images);
        mBanner.isAutoPlay(true);
        mBanner.start();

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TradeActivity.startActivity(mContext, mAdapter.getData().get(position).getSymbol());
            }
        });
        rvList.setAdapter(mAdapter);

        return v;
    }

    @Override
    protected void initData() {
        super.initData();
        mAdapter.setNewData(new ArrayList<>(StaticStore.sQuoteMap.values()));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBanner != null) {
            mBanner.startAutoPlay();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBanner != null) {
            mBanner.stopAutoPlay();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_title1) {
            TradeActivity.startActivity(mContext, "CLQ7");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshEvent event) {
        mAdapter.setNewData(new ArrayList<>(StaticStore.sQuoteMap.values()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
