package com.yjjr.yjfutures.ui.home;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.RefreshEvent;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.model.Symbol;
import com.yjjr.yjfutures.model.UserLoginResponse;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.UserInfo;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.ui.WebActivity;
import com.yjjr.yjfutures.ui.trade.DemoTradeActivity;
import com.yjjr.yjfutures.ui.trade.TradeActivity;
import com.yjjr.yjfutures.utils.DialogUtils;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.utils.imageloader.ImageLoader;
import com.yjjr.yjfutures.widget.CustomPromptDialog;
import com.yjjr.yjfutures.widget.LoadingView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * "主页"
 */
public class HomePageFragment extends BaseFragment implements View.OnClickListener {


    private ConvenientBanner<String> mBanner;
    private HomePageAdapter mAdapter;
    private CustomPromptDialog mCustomServiceDialog;
    private LoadingView mLoadingView;

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
        mCustomServiceDialog = DialogUtils.createCustomServiceDialog(mContext);

        mLoadingView = (LoadingView) v.findViewById(R.id.load_view);
        mLoadingView.setOnReloadListener(new LoadingView.OnReloadListener() {
            @Override
            public void onReload() {
                loadData();
            }
        });
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
        headerView.findViewById(R.id.tv_title2).setOnClickListener(this);
        headerView.findViewById(R.id.tv_title3).setOnClickListener(this);
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
        v.findViewById(R.id.tv_customer_service).setOnClickListener(this);
        return v;
    }

    @Override
    protected void initData() {
        super.initData();
        mAdapter.setNewData(new ArrayList<>(StaticStore.sQuoteMap.values()));
        loadData();
    }

    private void loadData() {
        //如果交易token为null，先获取token
        if (TextUtils.isEmpty(BaseApplication.getInstance().getTradeToken())) {
            final String account = UserSharePrefernce.getAccount(mContext);
            final String password = UserSharePrefernce.getPassword(mContext);
            HttpManager.getBizService().login(account, password)
                    .flatMap(new Function<BizResponse<UserInfo>, ObservableSource<UserLoginResponse>>() {
                        @Override
                        public ObservableSource<UserLoginResponse> apply(@NonNull BizResponse<UserInfo> loginBizResponse) throws Exception {
                            if (loginBizResponse.getRcode() != 0) {
                                throw new RuntimeException("登录失败");
                            }
                            BaseApplication.getInstance().setUserInfo(loginBizResponse.getResult());
                            return HttpManager.getHttpService().userLogin(account, password);
                        }
                    })
                    .flatMap(new Function<UserLoginResponse, ObservableSource<List<Symbol>>>() {
                        @Override
                        public ObservableSource<List<Symbol>> apply(@NonNull UserLoginResponse userLoginResponse) throws Exception {
                            if (userLoginResponse.getReturnCode() != 1) {
                                BaseApplication.getInstance().logout(mContext);
                            }
                            BaseApplication.getInstance().setTradeToken(userLoginResponse.getCid());
                            return HttpManager.getHttpService().getSymbols(BaseApplication.getInstance().getTradeToken());
                        }
                    })
                    .flatMap(new Function<List<Symbol>, ObservableSource<List<Quote>>>() {
                        @Override
                        public ObservableSource<List<Quote>> apply(@NonNull List<Symbol> symbols) throws Exception {
                            StringBuilder symbol = new StringBuilder();
                            StringBuilder exchange = new StringBuilder();
                            for (int i = 0; i < symbols.size(); i++) {
                                symbol.append(symbols.get(i).getSymbol());
                                exchange.append(symbols.get(i).getExchange());
                                if (i < symbols.size() - 1) {
                                    symbol.append(",");
                                    exchange.append(",");
                                }
                            }
                            StaticStore.sSymbols = symbol.toString();
                            StaticStore.sExchange = exchange.toString();
                            return HttpManager.getHttpService().getQuoteList(symbol.toString(), exchange.toString());
                        }
                    })
                    .map(new Function<List<Quote>, Boolean>() {
                        @Override
                        public Boolean apply(@NonNull List<Quote> quotes) throws Exception {
                            for (Quote quote : quotes) {
                                StaticStore.sQuoteMap.put(quote.getSymbol(), quote);
                            }
                            return true;
                        }
                    })
                    .compose(RxUtils.<Boolean>applySchedulers())
                    .compose(this.<Boolean>bindUntilEvent(FragmentEvent.DESTROY))
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@NonNull Boolean symbols) throws Exception {
                            mAdapter.setNewData(new ArrayList<>(StaticStore.sQuoteMap.values()));
                            mLoadingView.setVisibility(View.GONE);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            LogUtils.e(throwable);
                            mLoadingView.loadFail();
                        }
                    });
        } else {
            HttpManager.getHttpService().getSymbols(BaseApplication.getInstance().getTradeToken())
                    .flatMap(new Function<List<Symbol>, ObservableSource<List<Quote>>>() {
                        @Override
                        public ObservableSource<List<Quote>> apply(@NonNull List<Symbol> symbols) throws Exception {
                            StringBuilder symbol = new StringBuilder();
                            StringBuilder exchange = new StringBuilder();
                            for (int i = 0; i < symbols.size(); i++) {
                                symbol.append(symbols.get(i).getSymbol());
                                exchange.append(symbols.get(i).getExchange());
                                if (i < symbols.size() - 1) {
                                    symbol.append(",");
                                    exchange.append(",");
                                }
                                StaticStore.sSymbols = symbol.toString();
                                StaticStore.sExchange = exchange.toString();
                            }
                            return HttpManager.getHttpService().getQuoteList(symbol.toString(), exchange.toString());
                        }
                    })
                    .map(new Function<List<Quote>, Boolean>() {
                        @Override
                        public Boolean apply(@NonNull List<Quote> quotes) throws Exception {
                            for (Quote quote : quotes) {
                                StaticStore.sQuoteMap.put(quote.getSymbol(), quote);
                            }
                            return true;
                        }
                    })
                    .compose(RxUtils.<Boolean>applySchedulers())
                    .compose(this.<Boolean>bindUntilEvent(FragmentEvent.DESTROY))
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@NonNull Boolean symbols) throws Exception {
                            mAdapter.setNewData(new ArrayList<>(StaticStore.sQuoteMap.values()));
                            mLoadingView.setVisibility(View.GONE);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            LogUtils.e(throwable);
                            mLoadingView.loadFail();
                        }
                    });
        }
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
        switch (v.getId()) {
            case R.id.tv_customer_service:
                mCustomServiceDialog.show();
                break;
            case R.id.tv_title1:
                DemoTradeActivity.startActivity(mContext);
                break;
            case R.id.tv_title2:
                WebActivity.startActivity(mContext, HttpConfig.URL_SUPERVISE);
                break;
            case R.id.tv_title3:
                WebActivity.startActivity(mContext, HttpConfig.URL_DISCLOSURE);
                break;
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
