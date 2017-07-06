package com.yjjr.yjfutures.ui.market;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.GetSymbolsRequest;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.model.Symbol;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.ui.trade.TradeActivity;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 行情页面
 */
public class MarketPriceFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {


    private MarketPriceAdapter mAdapter;

    public MarketPriceFragment() {
        // Required empty public constructor
    }


    @Override
    public View initViews(LayoutInflater inflater, ViewGroup container,
                          Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_market_price, container, false);
        RecyclerView rvList = (RecyclerView) v.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new MarketPriceAdapter(null);
        rvList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        return v;
    }

    @Override
    protected void initData() {
       /* RxUtils.createSoapObservable2("GetSymbols", new GetSymbolsRequest(BaseApplication.getInstance().getAccount()))
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
                }) .compose(RxUtils.<List<Symbol>>applySchedulers())
                .compose(this.<List<Symbol>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<List<Symbol>>() {
                    @Override
                    public void accept(@NonNull List<Symbol> symbols) throws Exception {
                        LogUtils.d(symbols.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                    }
                });*/
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
                .flatMap(new Function<List<Symbol>, ObservableSource<List<Quote>>>() {
                    @Override
                    public ObservableSource<List<Quote>> apply(@NonNull List<Symbol> symbols) throws Exception {
                        SoapObject soapObject = new SoapObject(HttpConfig.NAME_SPACE, "GetQuoteList");
                        SoapObject symList = new SoapObject("", "SymList");
                        for (int i = 0; i < symbols.size(); i++) {
                            SoapObject s = new SoapObject("", "mySym");
                            Symbol symbol = symbols.get(i);
                            s.addAttribute("xmlns","http://schemas.datacontract.org/2004/07/IBManager.QuoteServer");
                            s.addProperty("Exchange",symbol.getExchange());
                            s.addProperty("Symbol",symbol.getSymbol());
                            symList.addProperty("mySym", s);
                        }
                        soapObject.addProperty("SymList",symList);
                        return  RxUtils.createSoapObservable3("GetQuoteList", soapObject)
                                .map(new Function<SoapObject, List<Quote>>() {
                                    @Override
                                    public List<Quote> apply(@NonNull SoapObject soapObject) throws Exception {
                                        List<Quote> quotes = new ArrayList<Quote>();
                                        for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                                            quotes.add(RxUtils.soapObject2Model((SoapObject) soapObject.getProperty(i), Quote.class));
                                        }
                                        return quotes;
                                    }
                                })
                                .compose(RxUtils.<List<Quote>>applySchedulers());
                    }
                })
                .compose(RxUtils.<List<Quote>>applySchedulers())
                .compose(this.<List<Quote>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<List<Quote>>() {
                    @Override
                    public void accept(@NonNull List<Quote> symbols) throws Exception {
                        LogUtils.d(symbols.toString());
                        mAdapter.addData(symbols);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                    }
                });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        TradeActivity.startActivity(mContext);
    }
}
