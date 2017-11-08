package com.yjjr.yjfutures.ui.publish;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.Info;
import com.yjjr.yjfutures.model.publish.News;
import com.yjjr.yjfutures.ui.ListFragment;
import com.yjjr.yjfutures.ui.WebActivity;
import com.yjjr.yjfutures.ui.home.ImageHolderView;
import com.yjjr.yjfutures.utils.DisplayUtils;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by dell on 2017/11/6.
 */

public class NewsFragment extends ListFragment<News> {

    private ConvenientBanner mBanner;

    @Override
    protected void loadData() {
        HttpManager.getBizService().getNewsList()
                .compose(this.<List<News>>bindUntilEvent(FragmentEvent.DESTROY))
                .compose(RxUtils.<List<News>>applySchedulers())
                .subscribe(new Consumer<List<News>>() {
                    @Override
                    public void accept(List<News> news) throws Exception {
                        loadDataFinish();
                        mAdapter.setNewData(news);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                        loadFailed();
                    }
                });

    }

    @Override
    public BaseQuickAdapter<News, BaseViewHolder> getAdapter() {
        mRvList.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        final NewsAdapter adapter = new NewsAdapter(null);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter a, View view, int position) {
                News news = adapter.getData().get(position);
                WebActivity.startActivity(mContext, "http://www.junchengtz.com/news/newsDetail.htm?id=" + news.getId());
            }
        });
        mBanner = new ConvenientBanner(mContext);
        mBanner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtils.dip2px(mContext, 165)));
        mBanner.setCanLoop(true);
        mBanner.startTurning(2000);
        adapter.addHeaderView(mBanner);
        loadBanner();
        return adapter;
    }

    private void loadBanner() {
        //获得banner
        HttpManager.getBizService().getBanner()
                .compose(RxUtils.<BizResponse<List<Info>>>applyBizSchedulers())
                .compose(this.<BizResponse<List<Info>>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<BizResponse<List<Info>>>() {
                    @Override
                    public void accept(@NonNull BizResponse<List<Info>> listBizResponse) throws Exception {
                        List<Info> images = new ArrayList<>();
                        images.addAll(listBizResponse.getResult());
                        mBanner.setPages(new CBViewHolderCreator() {
                            @Override
                            public Object createHolder() {
                                return new ImageHolderView();
                            }
                        }, images);
                    }
                }, RxUtils.commonErrorConsumer());
    }
}
