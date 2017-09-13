package com.yjjr.yjfutures.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yinglan.alphatabs.AlphaTabsIndicator;
import com.yjjr.yjfutures.BuildConfig;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.HideRedDotEvent;
import com.yjjr.yjfutures.event.OneMinuteEvent;
import com.yjjr.yjfutures.event.PollRefreshEvent;
import com.yjjr.yjfutures.event.PriceRefreshEvent;
import com.yjjr.yjfutures.event.ShowRedDotEvent;
import com.yjjr.yjfutures.event.UpdateUserInfoEvent;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.model.UserLoginResponse;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.Funds;
import com.yjjr.yjfutures.model.biz.Update;
import com.yjjr.yjfutures.model.biz.UserInfo;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.found.FoundFragment;
import com.yjjr.yjfutures.ui.home.HomePageFragment;
import com.yjjr.yjfutures.ui.market.MarketPriceFragment;
import com.yjjr.yjfutures.ui.mine.MineFragment;
import com.yjjr.yjfutures.ui.trade.TradeGuideActivity;
import com.yjjr.yjfutures.utils.ActivityTools;
import com.yjjr.yjfutures.utils.DialogUtils;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.NoTouchScrollViewpager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends BaseActivity {

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private Timer mTimer = new Timer();
    private long mBackPressed;
    private AlphaTabsIndicator mBottomBar;
    private Gson mGson = new Gson();

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        checkUpdate();
        startPoll();
//        testSocketIO();
        if (ActivityTools.isNeedShowGuide(mContext)) {
            TradeGuideActivity.startActivity(mContext);
        }
    }

    private void testSocketIO() {
        try {
            IO.Options options = new IO.Options();
            options.forceNew = true;
            options.reconnection = true;
            final Socket socket = IO.socket("http://192.168.1.67:9092", options);//创建连接
            //监听事件获取服务端的返回数据
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    LogUtils.d("socket io connect");
                }
            }).on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    LogUtils.d("socket io connect timeout");
                }
            }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    LogUtils.d("socket io connect error %s", Arrays.toString(args));
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    LogUtils.d("socket io disconnect");
                }
            }).on("singleTopMarketData", new Emitter.Listener() {//获取行情数据事件。连接打开后由服务端自动推送数据到这个监听方法，不用APP发生请求
                @Override
                public void call(Object... args) {
                    if (BaseApplication.getInstance().isBackground()) {
                        return;
                    }
                    String data = (String) args[0];
                    Quote quote = mGson.fromJson(data, Quote.class);
                    Quote oldQuote = StaticStore.getQuote(quote.getSymbol(), false);
                    if (oldQuote != null) {
                        oldQuote.setAskPrice(quote.getAskPrice());
                        oldQuote.setBidPrice(quote.getBidPrice());
                        oldQuote.setChange(quote.getChange());
                        oldQuote.setChangeRate(quote.getChangeRate());
                        oldQuote.setLastclose(quote.getLastclose());
                        oldQuote.setLastPrice(quote.getLastPrice());
                        oldQuote.setLastSize(quote.getLastSize());
                        oldQuote.setAskSize(quote.getAskSize());
                        oldQuote.setBidSize(quote.getBidSize());
                        oldQuote.setVol(quote.getVol());
                        LogUtils.d("socket io 收到报价信息：%s", oldQuote.toString());
                        EventBus.getDefault().post(new PriceRefreshEvent(quote.getSymbol()));
                    }

                }
            });
            socket.connect();
        } catch (Exception e) {
            LogUtils.e(e);
        }

    }

    private void checkUpdate() {
        HttpManager.getBizService().checkUpdate(BuildConfig.VERSION_NAME)
                .compose(RxUtils.<BizResponse<Update>>applyBizSchedulers())
                .compose(this.<BizResponse<Update>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<BizResponse<Update>>() {
                    @Override
                    public void accept(@NonNull BizResponse<Update> response) throws Exception {
                        Update result = response.getResult();
                        if (result.getUpdateOS() != 0) {
                            DialogUtils.createUpdateDialog(mContext, result).show();
                        }
                    }
                }, RxUtils.commonErrorConsumer());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ShowRedDotEvent event) {
        mBottomBar.getTabView(2).showPoint();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HideRedDotEvent event) {
        mBottomBar.getTabView(2).removeShow();
    }

    private void initViews() {
        mBottomBar = (AlphaTabsIndicator) findViewById(R.id.alphaIndicator);
        mBottomBar.setVisibility(HttpConfig.IS_OPEN_TRADE ? View.VISIBLE : View.GONE);
        final NoTouchScrollViewpager viewPager = (NoTouchScrollViewpager) findViewById(R.id.viewpager);
        Fragment[] fragments = {new HomePageFragment(), MarketPriceFragment.newInstance(true), new FoundFragment(), new MineFragment()};
        viewPager.setOffscreenPageLimit(fragments.length);
        viewPager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(), fragments));
        mBottomBar.setViewPager(viewPager);
    }

    private void startPoll() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                DateTime dateTime = new DateTime();
                if (dateTime.getSecondOfMinute() == 5) {
                    EventBus.getDefault().post(new OneMinuteEvent());
                }
                if (TextUtils.isEmpty(StaticStore.sSymbols)) {
                    return;
                }
                if (BaseApplication.getInstance().isBackground()) {
                    return;
                }
                EventBus.getDefault().post(new PollRefreshEvent());
                 HttpManager.getHttpService().getQuoteList(StaticStore.sSymbols, StaticStore.sExchange)
                        .map(new Function<List<Quote>, List<Quote>>() {
                            @Override
                            public List<Quote> apply(@NonNull List<Quote> quotes) throws Exception {
                                for (Quote quote : quotes) {
                                    //设置一下商品是否持仓
                                    quote.setHolding(StaticStore.sHoldSet.contains(quote.getSymbol()));
                                    StaticStore.putQuote(quote, false);
                                }
                                return quotes;
                            }
                        })
                        .compose(RxUtils.<List<Quote>>applySchedulers())
                        .compose(mContext.<List<Quote>>bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(new Consumer<List<Quote>>() {
                            @Override
                            public void accept(@NonNull List<Quote> quotes) throws Exception {
                            }
                        }, RxUtils.commonErrorConsumer());

                HttpManager.getBizService().getFunds()
                        .compose(RxUtils.<BizResponse<Funds>>applyBizSchedulers())
                        .compose(mContext.<BizResponse<Funds>>bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(new Consumer<BizResponse<Funds>>() {
                            @Override
                            public void accept(@NonNull BizResponse<Funds> fundsBizResponse) throws Exception {
                                Funds result = fundsBizResponse.getResult();
                                StaticStore.setFunds(false, result);
                            }
                        }, RxUtils.commonErrorConsumer());
            }
        }, 0, 1000);
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            BaseApplication.getInstance().closeApplication();
            return;
        } else {
            ToastUtils.show(mContext, R.string.click_more_exit);
        }

        mBackPressed = System.currentTimeMillis();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateUserInfoEvent event) {
        final String account = UserSharePrefernce.getAccount(mContext);
        final String password = UserSharePrefernce.getPassword(mContext);
        HttpManager.getBizService().login(account, password)
                .compose(RxUtils.<BizResponse<UserInfo>>applyBizSchedulers())
                .compose(this.<BizResponse<UserInfo>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<BizResponse<UserInfo>>() {
                    @Override
                    public void accept(@NonNull BizResponse<UserInfo> response) throws Exception {
                        BaseApplication.getInstance().setUserInfo(response.getResult());
                    }
                }, RxUtils.commonErrorConsumer());
        HttpManager.getHttpService().userLogin(account, password)
                .compose(RxUtils.<UserLoginResponse>applySchedulers())
                .compose(this.<UserLoginResponse>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<UserLoginResponse>() {
                    @Override
                    public void accept(@NonNull UserLoginResponse userLoginResponse) throws Exception {
                        BaseApplication.getInstance().setTradeToken(userLoginResponse.getCid());
                    }
                }, RxUtils.commonErrorConsumer());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }
}
