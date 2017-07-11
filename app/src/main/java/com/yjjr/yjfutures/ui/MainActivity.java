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
import com.yjjr.yjfutures.event.RefreshEvent;
import com.yjjr.yjfutures.model.GetSymbolsRequest;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.model.Symbol;
import com.yjjr.yjfutures.model.UserLoginRequest;
import com.yjjr.yjfutures.model.UserLoginResponse;
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
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class MainActivity extends BaseActivity {

    private LoadingView mLoadingView;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingView = (LoadingView) findViewById(R.id.load_view);
        mLoadingView.setOnReloadListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        loadData();
    }

    private void loadData() {
        //如果交易token为null，先获取token
        if (TextUtils.isEmpty(BaseApplication.getInstance().getTradeToken())) {
            HttpManager.getHttpService().userLogin(UserSharePrefernce.getAccount(mContext), UserSharePrefernce.getPassword(mContext))
                    .flatMap(new Function<UserLoginResponse, ObservableSource<SoapObject>>() {
                        @Override
                        public ObservableSource<SoapObject> apply(@NonNull UserLoginResponse userLoginResponse) throws Exception {
                            LogUtils.d(userLoginResponse.toString());
                            if (userLoginResponse.getReturnCode() != 1) {
                                BaseApplication.getInstance().logout(mContext);
                            }
                            BaseApplication.getInstance().setTradeToken(userLoginResponse.getCid());
                            return RxUtils.createSoapObservable2("GetSymbols", new GetSymbolsRequest(BaseApplication.getInstance().getTradeToken()));
                        }
                    })
                    .map(new Function<SoapObject, List<Symbol>>() {
                        @Override
                        public List<Symbol> apply(@NonNull SoapObject soapObject) throws Exception {
                            List<Symbol> symbols = new ArrayList<>();
                            SoapObject s = (SoapObject) ((SoapObject) soapObject.getProperty(1)).getProperty(0);
                            for (int i = 0; i < s.getPropertyCount(); i++) {
                                symbols.add(RxUtils.soapObject2Model((SoapObject) s.getProperty(i), Symbol.class));
                            }
                            return symbols;
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
            RxUtils.createSoapObservable2("GetSymbols", new GetSymbolsRequest(BaseApplication.getInstance().getTradeToken()))
                    .map(new Function<SoapObject, List<Symbol>>() {
                        @Override
                        public List<Symbol> apply(@NonNull SoapObject soapObject) throws Exception {
                            List<Symbol> symbols = new ArrayList<>();
                            SoapObject s = (SoapObject) ((SoapObject) soapObject.getProperty(1)).getProperty(0);
                            for (int i = 0; i < s.getPropertyCount(); i++) {
                                symbols.add(RxUtils.soapObject2Model((SoapObject) s.getProperty(i), Symbol.class));
                            }
                            return symbols;
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
        Observable.interval(5, TimeUnit.SECONDS)
                .filter(new Predicate<Long>() {
                    @Override
                    public boolean test(@NonNull Long aLong) throws Exception {
                        return !(TextUtils.isEmpty(StaticStore.sSymbols) || TextUtils.isEmpty(StaticStore.sExchange));
                    }
                })
                .flatMap(new Function<Long, ObservableSource<List<Quote>>>() {
                    @Override
                    public ObservableSource<List<Quote>> apply(@NonNull Long aLong) throws Exception {
                        return HttpManager.getHttpService().getQuoteList(StaticStore.sSymbols, StaticStore.sExchange);
                    }
                })
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
                .compose(this.<List<Quote>>bindUntilEvent(ActivityEvent.DESTROY))
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

    private void initViews() {
        AlphaTabsIndicator bottomBar = (AlphaTabsIndicator) findViewById(R.id.alphaIndicator);
        final NoTouchScrollViewpager viewPager = (NoTouchScrollViewpager) findViewById(R.id.viewpager);
        mLoadingView.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        Fragment[] fragments = {new HomePageFragment(), new MarketPriceFragment(), new FoundFragment(), new MineFragment()};
        viewPager.setOffscreenPageLimit(fragments.length);
        viewPager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(), fragments));
        bottomBar.setViewPager(viewPager);
    }


}
