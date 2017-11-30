package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.PollRefreshEvent;
import com.yjjr.yjfutures.event.SendOrderEvent;
import com.yjjr.yjfutures.model.UserLoginResponse;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.Funds;
import com.yjjr.yjfutures.model.biz.Holds;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.market.MarketPriceFragment;
import com.yjjr.yjfutures.utils.BizSocketUtils;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.TradeInfoView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class DemoTradeActivity extends BaseActivity {
    private Timer mTimer = new Timer();
    private TradeInfoView mTradeInfoView;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, DemoTradeActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_trade);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        headerView.bindActivity(mContext);
        mTradeInfoView = (TradeInfoView) findViewById(R.id.trade_info);
        loadData();
        startTimer();
    }

    private void startTimer() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (BaseApplication.getInstance().isBackground()) {
                    return;
                }
               /* HttpManager.getHttpService(true).getQuoteList(StaticStore.sDemoSymbols, StaticStore.sDemoExchange)
                        .map(new Function<List<Quote>, List<Quote>>() {
                            @Override
                            public List<Quote> apply(@NonNull List<Quote> quotes) throws Exception {
                                for (Quote quote : quotes) {
                                    //设置一下商品是否持仓
                                    quote.setHolding(StaticStore.sDemoHoldSet.contains(quote.getSymbol()));
                                    StaticStore.putQuote(quote, true);
                                }
                                return quotes;
                            }
                        })
                        .compose(RxUtils.<List<Quote>>applySchedulers())
                        .compose(mContext.<List<Quote>>bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(new Consumer<List<Quote>>() {
                            @Override
                            public void accept(@NonNull List<Quote> quotes) throws Exception {
//                                EventBus.getDefault().post(new PollRefreshEvent());
                            }
                        }, RxUtils.commonErrorConsumer());*/
                HttpManager.getBizService(true).getFunds()
                        .compose(RxUtils.<BizResponse<Funds>>applyBizSchedulers())
                        .compose(mContext.<BizResponse<Funds>>bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(new Consumer<BizResponse<Funds>>() {
                            @Override
                            public void accept(@NonNull BizResponse<Funds> fundsBizResponse) throws Exception {
                                Funds result = fundsBizResponse.getResult();
                                StaticStore.setFunds(true, result);
                                mTradeInfoView.setValues(result.getFrozenMargin(), result.getAvailableFunds(), result.getNetAssets());
                            }
                        }, RxUtils.commonErrorConsumer());
            }
        }, 3000, 1000);

    }

    private void loadData() {
        final String account = UserSharePrefernce.getAccount(mContext);
        final String password = /*UserSharePrefernce.getPassword(mContext)*/"123456";
        if(TextUtils.isEmpty(BaseApplication.getInstance().getTradeToken(true))) {
            RxUtils.createZTLoginObservable(account, password, true)
                    .map(new Function<UserLoginResponse, Boolean>() {
                        @Override
                        public Boolean apply(@NonNull UserLoginResponse userLoginResponse) throws Exception {
                            if (userLoginResponse.getReturnCode() != 1) {// 账号密法错误，重新登录
                                throw new RuntimeException("账号密码错误");
                            }
                            BaseApplication.getInstance().setDemoTradeToken(userLoginResponse.getCid());
                            return true;
                        }
                    })
                    .compose(RxUtils.<Boolean>applySchedulers())
                    .compose(this.<Boolean>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@NonNull Boolean symbols) throws Exception {
                            // 设置fragment
                            MarketPriceFragment fragment = MarketPriceFragment.newInstance(false);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();
//                        fragment.setUserVisibleHint(true);
                            getHolding();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            LogUtils.e(throwable);
                        }
                    });
        }else{
            // 设置fragment
            MarketPriceFragment fragment = MarketPriceFragment.newInstance(false);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();
//                        fragment.setUserVisibleHint(true);
            getHolding();
        }
    }

    private void getHolding() {
//        HttpManager.getBizService(true).getHolding()
        BizSocketUtils.getHolding(true)
                .compose(RxUtils.<BizResponse<List<Holds>>>applyBizSchedulers())
                .compose(this.<BizResponse<List<Holds>>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<BizResponse<List<Holds>>>() {
                    @Override
                    public void accept(@NonNull BizResponse<List<Holds>> response) throws Exception {
                        //将持仓的品种保存起来
                        StaticStore.sDemoHoldSet = new HashSet<>();
                        for (Holds holding : response.getResult()) {
                            if (holding.getQty() == 0) continue;
                            StaticStore.sDemoHoldSet.add(holding.getSymbol());
                        }
                    }
                }, RxUtils.commonErrorConsumer());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SendOrderEvent event) {
        getHolding();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PollRefreshEvent event) {
        if (mTradeInfoView == null) return;
        Funds result = StaticStore.getFunds(true);
        mTradeInfoView.setValues(result.getFrozenMargin(), result.getAvailableFunds(), result.getNetAssets());
    }

}
