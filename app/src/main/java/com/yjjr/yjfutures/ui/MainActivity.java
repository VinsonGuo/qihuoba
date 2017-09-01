package com.yjjr.yjfutures.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yinglan.alphatabs.AlphaTabsIndicator;
import com.yjjr.yjfutures.BuildConfig;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.HideRedDotEvent;
import com.yjjr.yjfutures.event.OneMinuteEvent;
import com.yjjr.yjfutures.event.RefreshEvent;
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
import com.yjjr.yjfutures.utils.DialogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.NoTouchScrollViewpager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MainActivity extends BaseActivity {

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private Timer mTimer = new Timer();
    private long mBackPressed;
    private AlphaTabsIndicator mBottomBar;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        initViews();
        checkUpdate();
        startPoll();
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
                EventBus.getDefault().post(new RefreshEvent());
                DateTime dateTime = new DateTime();
                if (dateTime.getSecondOfMinute() == 1) {
                    EventBus.getDefault().post(new OneMinuteEvent());
                }
                if (TextUtils.isEmpty(StaticStore.sSymbols)) {
                    return;
                }
                if (BaseApplication.getInstance().isBackground()) {
                    return;
                }
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
        EventBus.getDefault().unregister(this);
        mTimer.cancel();
    }
}
