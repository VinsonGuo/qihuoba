package com.yjjr.yjfutures.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yinglan.alphatabs.AlphaTabsIndicator;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.OneMinuteEvent;
import com.yjjr.yjfutures.event.RefreshEvent;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.model.Symbol;
import com.yjjr.yjfutures.model.UserLoginResponse;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.Login;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.found.FoundFragment;
import com.yjjr.yjfutures.ui.home.HomePageFragment;
import com.yjjr.yjfutures.ui.market.MarketPriceFragment;
import com.yjjr.yjfutures.ui.mine.MineFragment;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.LoadingView;
import com.yjjr.yjfutures.widget.NoTouchScrollViewpager;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MainActivity extends BaseActivity {

    private LoadingView mLoadingView;
    private Timer mTimer = new Timer();

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingView = (LoadingView) findViewById(R.id.load_view);
        mLoadingView.setOnReloadListener(new LoadingView.OnReloadListener() {
            @Override
            public void onReload() {
                loadData();
            }
        });
        loadData();
    }

    private void loadData() {
        //如果交易token为null，先获取token
        if (TextUtils.isEmpty(BaseApplication.getInstance().getTradeToken())) {
            final String account = UserSharePrefernce.getAccount(mContext);
            final String password = UserSharePrefernce.getPassword(mContext);
//            HttpManager.getHttpService().userLogin(account, password )
            HttpManager.getBizService().login(account, password)
                    .flatMap(new Function<BizResponse<Login>, ObservableSource<UserLoginResponse>>() {
                        @Override
                        public ObservableSource<UserLoginResponse> apply(@NonNull BizResponse<Login> loginBizResponse) throws Exception {
                            if(loginBizResponse.getRcode() != 0) {
                                throw new RuntimeException("登录失败");
                            }
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
                    .compose(this.<Boolean>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@NonNull Boolean symbols) throws Exception {
                            initViews();
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
                    .compose(this.<Boolean>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@NonNull Boolean symbols) throws Exception {
                            initViews();
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

    private void initViews() {
        AlphaTabsIndicator bottomBar = (AlphaTabsIndicator) findViewById(R.id.alphaIndicator);
        final NoTouchScrollViewpager viewPager = (NoTouchScrollViewpager) findViewById(R.id.viewpager);
        mLoadingView.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        Fragment[] fragments = {new HomePageFragment(), new MarketPriceFragment(), new FoundFragment(), new MineFragment()};
        viewPager.setOffscreenPageLimit(fragments.length);
        viewPager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(), fragments));
        bottomBar.setViewPager(viewPager);

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                DateTime dateTime = new DateTime();
                if(dateTime.getSecondOfMinute() == 1) {
                    EventBus.getDefault().post(new OneMinuteEvent());
                }
                HttpManager.getHttpService().getQuoteList(StaticStore.sSymbols, StaticStore.sExchange)
                        .map(new Function<List<Quote>, List<Quote>>() {
                            @Override
                            public List<Quote> apply(@NonNull List<Quote> quotes) throws Exception {
                                for (Quote quote : quotes) {
                                    StaticStore.sQuoteMap.put(quote.getSymbol(), quote);
                                }
                                return quotes;
                            }
                        })
                        .compose(RxUtils.<List<Quote>>applySchedulers())
                        .compose(mContext.<List<Quote>>bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(new Consumer<List<Quote>>() {
                            @Override
                            public void accept(@NonNull List<Quote> quotes) throws Exception {
                                EventBus.getDefault().post(new RefreshEvent());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                LogUtils.e(throwable);
                            }
                        });
            }
        }, 3000, 1000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }
}
