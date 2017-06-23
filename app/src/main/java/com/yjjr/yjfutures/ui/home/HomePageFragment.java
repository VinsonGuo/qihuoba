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
public class HomePageFragment extends BaseFragment implements View.OnClickListener {


    private RecyclerView mRecyclerView;
    private Banner mBanner;

    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_page, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        HomePageAdapter adapter = new HomePageAdapter(null);
        mRecyclerView.setAdapter(adapter);
        View headerView = inflater.inflate(R.layout.header_home_page, mRecyclerView, false);
        mBanner = (Banner) headerView.findViewById(R.id.banner);
        headerView.findViewById(R.id.tv_title1).setOnClickListener(this);
        adapter.addHeaderView(headerView);
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

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TradeActivity.startActivity(mContext);
            }
        });

        for (int i = 0; i < 10; i++) {
            adapter.addData("item" + i);
        }
        return v;
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
            TradeActivity.startActivity(mContext);
        }
    }
}
