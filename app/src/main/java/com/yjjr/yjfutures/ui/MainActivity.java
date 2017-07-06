package com.yjjr.yjfutures.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yinglan.alphatabs.AlphaTabsIndicator;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.GetSymbolsRequest;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.model.Symbol;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.ui.found.FoundFragment;
import com.yjjr.yjfutures.ui.home.HomePageFragment;
import com.yjjr.yjfutures.ui.market.MarketPriceFragment;
import com.yjjr.yjfutures.ui.mine.MineFragment;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.widget.LoadingView;
import com.yjjr.yjfutures.widget.NoTouchScrollViewpager;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

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
        /*UserLoginRequest model = new UserLoginRequest("test001",
        "8DDB9745A9FCC3874D14779C4AC7B20CD183DE95258C5354DE7A0E075D4CB706",
        "Trader;dell|||50:9A:4C:08:5D:73|||192.168.1.16.fe80::61d3:acb3:a9f5:bdb|||",
        "3.29");
        RxUtils.createSoapObservable("UserLogin",model, UserLoginResponse.class)
                .compose(RxUtils.<UserLoginResponse>applySchedulers())
                .compose(this.<UserLoginResponse>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<UserLoginResponse>() {
                    @Override
                    public void accept(@NonNull UserLoginResponse userLoginResponse) throws Exception {
                        LogUtils.d(userLoginResponse.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                    }
                });*/
    }

    private void loadData() {
        RxUtils.createSoapObservable2("GetSymbols", new GetSymbolsRequest(BaseApplication.getInstance().getAccount()))
                .map(new Function<SoapObject, List<Symbol>>() {
                    @Override
                    public List<Symbol> apply(@NonNull SoapObject soapObject) throws Exception {
                        List<Symbol> symbols = new ArrayList<Symbol>();
                        SoapObject s = (SoapObject) ((SoapObject) soapObject.getProperty(1)).getProperty(0);
                        for (int i = 0; i < s.getPropertyCount(); i++) {
                            symbols.add(RxUtils.soapObject2Model((SoapObject) s.getProperty(i), Symbol.class));
                        }
                        return symbols;
                    }
                })
                .flatMap(new Function<List<Symbol>, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(@NonNull List<Symbol> symbols) throws Exception {
                        SoapObject soapObject = new SoapObject(HttpConfig.NAME_SPACE, "GetQuoteList");
                        SoapObject symList = new SoapObject("", "SymList");
                        for (int i = 0; i < symbols.size(); i++) {
                            SoapObject s = new SoapObject("", "mySym");
                            Symbol symbol = symbols.get(i);
                            s.addAttribute("xmlns", "http://schemas.datacontract.org/2004/07/IBManager.QuoteServer");
                            s.addProperty("Exchange", symbol.getExchange());
                            s.addProperty("Symbol", symbol.getSymbol());
                            symList.addProperty("mySym", s);
                        }
                        soapObject.addProperty("SymList", symList);
                        return RxUtils.createSoapObservable3("GetQuoteList", soapObject)
                                .map(new Function<SoapObject, Boolean>() {
                                    @Override
                                    public Boolean apply(@NonNull SoapObject soapObject) throws Exception {
                                        for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                                            Quote quote = RxUtils.soapObject2Model((SoapObject) soapObject.getProperty(i), Quote.class);
                                            StaticStore.sQuoteMap.put(quote.getSymbol(), quote);
                                        }
                                        return true;
                                    }
                                })
                                .compose(RxUtils.<Boolean>applySchedulers());
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
