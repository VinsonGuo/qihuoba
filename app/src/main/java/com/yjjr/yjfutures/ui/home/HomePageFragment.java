package com.yjjr.yjfutures.ui.home;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.RefreshEvent;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.ui.trade.TradeActivity;
import com.yjjr.yjfutures.utils.imageloader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * "主页"
 */
public class HomePageFragment extends BaseFragment implements View.OnClickListener {


    private ConvenientBanner<String> mBanner;
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
        RecyclerView.ItemAnimator animator = rvList.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new HomePageAdapter(null);
        List<String> images = new ArrayList<>();
        images.add("http://static.rong360.com/gl/uploads/160408/128-16040Q4452CV.jpg");
        images.add("http://p3.ifengimg.com/a/2016_47/a03456cb56dc1ad_size158_w538_h300.jpg");
        images.add("http://pic.pimg.tw/chihlee8182/1429852839-2894784929.jpg");
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.header_home_page, rvList, false);
        mBanner = (ConvenientBanner<String>) headerView.findViewById(R.id.banner);
        headerView.findViewById(R.id.tv_title1).setOnClickListener(this);
        mBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new ImageHolderView();
            }
        }, images);


        mAdapter.addHeaderView(headerView);
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
            mBanner.startTurning(3000);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBanner != null) {
            mBanner.stopTurning();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_title1) {
            if (mAdapter.getData().size() > 0) {
                TradeActivity.startActivity(mContext, mAdapter.getData().get(0).getSymbol());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshEvent event) {
        mAdapter.getData().clear();
        mAdapter.getData().addAll(StaticStore.sQuoteMap.values());
        mAdapter.notifyItemRangeChanged(mAdapter.getHeaderLayoutCount(), StaticStore.sQuoteMap.size());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public class ImageHolderView implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, String data) {
            ImageLoader.load(context, data, imageView);
        }
    }
}
