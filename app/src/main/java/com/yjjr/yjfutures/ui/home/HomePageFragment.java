package com.yjjr.yjfutures.ui.home;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.yjjr.yjfutures.event.PriceRefreshEvent;
import com.yjjr.yjfutures.event.ReloginDialogEvent;
import com.yjjr.yjfutures.event.SendOrderEvent;
import com.yjjr.yjfutures.event.ShowRedDotEvent;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.model.Symbol;
import com.yjjr.yjfutures.model.UserLoginResponse;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.Holds;
import com.yjjr.yjfutures.model.biz.Info;
import com.yjjr.yjfutures.model.biz.UserInfo;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.ui.WebActivity;
import com.yjjr.yjfutures.ui.mine.ChatActivity;
import com.yjjr.yjfutures.ui.trade.DemoTradeActivity;
import com.yjjr.yjfutures.ui.trade.TradeActivity;
import com.yjjr.yjfutures.utils.DialogUtils;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.utils.imageloader.ImageLoader;
import com.yjjr.yjfutures.widget.LoadingView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * "主页"
 */
public class HomePageFragment extends BaseFragment implements View.OnClickListener {


    private ConvenientBanner<Info> mBanner;
    private HomePageAdapter mAdapter;
    private LoadingView mLoadingView;
    private View mDemoView;

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
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        mLoadingView = (LoadingView) view.findViewById(R.id.load_view);
        mLoadingView.setOnReloadListener(new LoadingView.OnReloadListener() {
            @Override
            public void onReload() {
                loadData();
            }
        });
        RecyclerView rvList = (RecyclerView) view.findViewById(R.id.rv_list);
//        RecyclerView.ItemAnimator animator = rvList.getItemAnimator();
//        if (animator instanceof SimpleItemAnimator) {
//            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
//        }
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new HomePageAdapter(null);
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.header_home_page, rvList, false);
        mBanner = (ConvenientBanner<Info>) headerView.findViewById(R.id.banner);
        mDemoView = headerView.findViewById(R.id.tv_header_title1);
        mDemoView.setOnClickListener(this);
        headerView.findViewById(R.id.tv_title2).setOnClickListener(this);
        headerView.findViewById(R.id.tv_title3).setOnClickListener(this);

        mAdapter.addHeaderView(headerView);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TradeActivity.startActivity(mContext, mAdapter.getData().get(position).getSymbol(), false);
            }
        });
        mAdapter.bindToRecyclerView(rvList);
        view.findViewById(R.id.tv_customer_service).setOnClickListener(this);
        view.findViewById(R.id.tv_guide).setOnClickListener(this);
        return view;
    }


    @Override
    protected void initData() {
        super.initData();
        mAdapter.setNewData(new ArrayList<>(StaticStore.getQuoteValues(false)));
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
                                if (loginBizResponse.getRcode() == 1) { // 账号密法错误，重新登录
                                    EventBus.getDefault().post(new ReloginDialogEvent());
                                }
                                throw new RuntimeException("登录失败");
                            }

                            // 如果有未读通知，点亮小红点
                            if (loginBizResponse.getResult().isExistUnreadNotice()) {
                                EventBus.getDefault().post(new ShowRedDotEvent());
                            }
                            BaseApplication.getInstance().setUserInfo(loginBizResponse.getResult());
                            return HttpManager.getHttpService().userLogin(account, password);
                        }
                    })
                    .flatMap(new Function<UserLoginResponse, ObservableSource<List<Symbol>>>() {
                        @Override
                        public ObservableSource<List<Symbol>> apply(@NonNull UserLoginResponse userLoginResponse) throws Exception {
                            if (userLoginResponse.getReturnCode() != 1) {// 账号密法错误，重新登录
                                ToastUtils.show(mContext, R.string.please_login_again);
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
                                StaticStore.putQuote(quote, false);
                            }
                            return true;
                        }
                    })
                    .compose(RxUtils.<Boolean>applySchedulers())
                    .compose(this.<Boolean>bindUntilEvent(FragmentEvent.DESTROY))
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@NonNull Boolean symbols) throws Exception {
                            mAdapter.setNewData(new ArrayList<>(StaticStore.getQuoteValues(false)));
                            mLoadingView.setVisibility(View.GONE);
                            DialogUtils.showGuideView(getActivity(), mDemoView);
                            getHolding();
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
                                StaticStore.putQuote(quote, false);
                            }
                            return true;
                        }
                    })
                    .compose(RxUtils.<Boolean>applySchedulers())
                    .compose(this.<Boolean>bindUntilEvent(FragmentEvent.DESTROY))
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@NonNull Boolean symbols) throws Exception {
                            getHolding();
                            mAdapter.setNewData(new ArrayList<>(StaticStore.getQuoteValues(false)));
                            mLoadingView.setVisibility(View.GONE);
                            DialogUtils.showGuideView(getActivity(), mDemoView);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            LogUtils.e(throwable);
                            mLoadingView.loadFail();
                        }
                    });
        }
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

    private void getHolding() {
        HttpManager.getBizService().getHolding()
                .compose(RxUtils.<BizResponse<List<Holds>>>applySchedulers())
                .compose(this.<BizResponse<List<Holds>>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<BizResponse<List<Holds>>>() {
                    @Override
                    public void accept(@NonNull BizResponse<List<Holds>> response) throws Exception {
                        //将持仓的品种保存起来
                        StaticStore.sHoldSet = new HashSet<>();
                        for (Holds holding : response.getResult()) {
                            StaticStore.sHoldSet.add(holding.getSymbol());
                        }
                    }
                }, RxUtils.commonErrorConsumer());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SendOrderEvent event) {
        getHolding();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBanner != null) {
            mBanner.startTurning(3000);
        }
        if (mAdapter != null) {
            mAdapter.replaceData(StaticStore.getQuoteValues(false));
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
//                WebActivity.startActivity(mContext, HttpConfig.URL_CSCENTER, WebActivity.TYPE_CSCENTER);
                ChatActivity.startActivity(mContext);
                break;
            case R.id.tv_guide:
                WebActivity.startActivity(mContext, HttpConfig.URL_GUIDE);
                break;
            case R.id.tv_header_title1:
                DemoTradeActivity.startActivity(mContext);
                break;
            case R.id.tv_title2:
                WebActivity.startActivity(mContext, HttpConfig.URL_QUALIFICATION);
                break;
            case R.id.tv_title3:
                WebActivity.startActivity(mContext, HttpConfig.URL_WARNING);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PriceRefreshEvent event) {
        if (isResumed()) {
//            mAdapter.getData().clear();
//            mAdapter.getData().addAll(StaticStore.getQuoteValues(false));
//            mAdapter.notifyItemRangeChanged(mAdapter.getHeaderLayoutCount(), StaticStore.getQuoteValues(false).size());
            Quote quote = StaticStore.getQuote(event.getSymbol(), false);
            int position = mAdapter.getData().indexOf(quote);
            mAdapter.notifyItemChanged(mAdapter.getHeaderLayoutCount() + position);
//            mAdapter.notifyItemInserted();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public class ImageHolderView implements Holder<Info> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, final Info data) {
            ImageLoader.load(context, HttpConfig.BIZ_HOST + data.getName(), imageView);
            if (!TextUtils.isEmpty(data.getValue())) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebActivity.startActivity(mContext, data.getValue());
                    }
                });
            }
        }
    }
}
