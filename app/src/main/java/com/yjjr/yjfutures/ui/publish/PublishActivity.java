package com.yjjr.yjfutures.ui.publish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.umeng.analytics.MobclickAgent;
import com.yinglan.alphatabs.AlphaTabsIndicator;
import com.yinglan.alphatabs.OnTabChangedListner;
import com.yjjr.yjfutures.BuildConfig;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.PriceRefreshEvent;
import com.yjjr.yjfutures.event.ReloginDialogEvent;
import com.yjjr.yjfutures.event.ShowRedDotEvent;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.model.UserLoginResponse;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.Update;
import com.yjjr.yjfutures.model.biz.UserInfo;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.SimpleFragmentPagerAdapter;
import com.yjjr.yjfutures.utils.ActivityTools;
import com.yjjr.yjfutures.utils.DialogUtils;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.SocketUtils;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.NoTouchScrollViewpager;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class PublishActivity extends BaseActivity {

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    private AlphaTabsIndicator mBottomBar;
    private Gson mGson = new Gson();

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, PublishActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        initViews();
//        loadData();
//        checkUpdate();
        initSocket();
        // 统计用户登录
        MobclickAgent.onProfileSignIn(UserSharePrefernce.getAccount(this));
    }

    private void loadData() {
        //如果交易token为null，先获取token
        if (TextUtils.isEmpty(BaseApplication.getInstance().getTradeToken())) {
            final String account = UserSharePrefernce.getAccount(mContext);
            final String password = UserSharePrefernce.getPassword(mContext);
            HttpManager.getBizService().login(account, password, ActivityTools.getDeviceAndVerson())
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
                            return HttpManager.getHttpService().userLogin(account, password, ActivityTools.getIpByNetwork());
                        }
                    })
                    .map(new Function<UserLoginResponse, Boolean>() {
                        @Override
                        public Boolean apply(@NonNull UserLoginResponse userLoginResponse) throws Exception {
                            if (userLoginResponse.getReturnCode() != 1) {// 账号密法错误，重新登录
                                ToastUtils.show(mContext, R.string.please_login_again);
                                BaseApplication.getInstance().logout(mContext);
                            }
                            BaseApplication.getInstance().setTradeToken(userLoginResponse.getCid());
                            return true;
                        }
                    })
                    .compose(RxUtils.<Boolean>applySchedulers())
                    .compose(this.<Boolean>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@NonNull Boolean symbols) throws Exception {
                            initSocket();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            LogUtils.e(throwable);
                        }
                    });
        } else {
            initSocket();
        }

    }

    private void initSocket() {
        SocketUtils.init();
        if (SocketUtils.getSocket() == null) {
            return;
        }
        //监听事件获取服务端的返回数据
        SocketUtils.getSocket().on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogUtils.d("socket io connect");
            }
        }).on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogUtils.d("socket io connect timeout");
                ToastUtils.show(mContext, R.string.socket_timeout);
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
                try {
                    if (BaseApplication.getInstance().isBackground()) {
                        return;
                    }
                    String data = (String) args[0];
                    Quote quote = mGson.fromJson(data, Quote.class);
                    // 真实
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
                        oldQuote.setHigh(quote.getHigh());
                        oldQuote.setLow(quote.getLow());
                        oldQuote.setVol(quote.getVol());
                        oldQuote.setHolding(StaticStore.sHoldSet.contains(quote.getSymbol()));
                    }

                    // 模拟
                    Quote demoQuote = StaticStore.getQuote(quote.getSymbol(), true);
                    if (demoQuote != null) {
                        demoQuote.setAskPrice(quote.getAskPrice());
                        demoQuote.setBidPrice(quote.getBidPrice());
                        demoQuote.setChange(quote.getChange());
                        demoQuote.setChangeRate(quote.getChangeRate());
                        demoQuote.setLastclose(quote.getLastclose());
                        demoQuote.setLastPrice(quote.getLastPrice());
                        demoQuote.setLastSize(quote.getLastSize());
                        demoQuote.setAskSize(quote.getAskSize());
                        demoQuote.setBidSize(quote.getBidSize());
                        demoQuote.setHigh(quote.getHigh());
                        demoQuote.setLow(quote.getLow());
                        demoQuote.setVol(quote.getVol());
                        demoQuote.setHolding(StaticStore.sDemoHoldSet.contains(quote.getSymbol()));
                    }
                    EventBus.getDefault().post(new PriceRefreshEvent(quote.getSymbol()));
                } catch (Exception e) {
                    LogUtils.e(e);
                }
            }
        }).on("getSymbolList", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogUtils.d("getSymbolList -> %s", args[0]);
                try {
                    List<Quote> list = mGson.fromJson(args[0].toString(), new TypeToken<List<Quote>>() {
                    }.getType());
                    for (Quote quote : list) {
                        quote.setHolding(StaticStore.sHoldSet.contains(quote.getSymbol()));
                        StaticStore.putQuote(quote, false);
                    }

                    List<Quote> demoList = mGson.fromJson(args[0].toString(), new TypeToken<List<Quote>>() {
                    }.getType());
                    for (Quote quote : demoList) {
                        quote.setHolding(StaticStore.sDemoHoldSet.contains(quote.getSymbol()));
                        StaticStore.putQuote(quote, true);
                    }
                } catch (Exception e) {
                    LogUtils.e(e);
                }
            }
        })/*.on("topMarketDepth1", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogUtils.d("topMarketDepth1 -> %s", args[0]);
            }
        })*/;
        SocketUtils.getSocket().emit("getSymbolList");
    }


    private void checkUpdate() {
        HttpManager.getBizService().checkUpdate(BuildConfig.VERSION_NAME, BuildConfig.APPLICATION_ID + "," + ActivityTools.getChannelName(mContext))
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


    private void initViews() {
        final TextView tvHeader = (TextView) findViewById(R.id.tv_header);
        mBottomBar = (AlphaTabsIndicator) findViewById(R.id.alphaIndicator);
        final NoTouchScrollViewpager viewPager = (NoTouchScrollViewpager) findViewById(R.id.viewpager);
        Fragment[] fragments = {new NewsFragment(), new MarketFragment()};
        viewPager.setOffscreenPageLimit(fragments.length);
        viewPager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(), fragments));
        mBottomBar.setViewPager(viewPager);
        mBottomBar.setOnTabChangedListner(new OnTabChangedListner() {
            @Override
            public void onTabSelected(int tabNum) {
                tvHeader.setText(tabNum == 0 ? "首页" : "行情");
            }
        });
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 统计用户登出
        MobclickAgent.onProfileSignOff();
    }
}
