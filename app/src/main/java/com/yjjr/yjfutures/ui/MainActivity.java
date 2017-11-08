package com.yjjr.yjfutures.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.umeng.analytics.MobclickAgent;
import com.yinglan.alphatabs.AlphaTabView;
import com.yinglan.alphatabs.AlphaTabsIndicator;
import com.yjjr.yjfutures.BuildConfig;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.CSUnreadEvent;
import com.yjjr.yjfutures.event.HideRedDotEvent;
import com.yjjr.yjfutures.event.OneMinuteEvent;
import com.yjjr.yjfutures.event.PollRefreshEvent;
import com.yjjr.yjfutures.event.ShowRedDotEvent;
import com.yjjr.yjfutures.event.UpdateUserInfoEvent;
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
import com.yjjr.yjfutures.ui.publish.PublishActivity;
import com.yjjr.yjfutures.ui.trade.TradeGuideActivity;
import com.yjjr.yjfutures.utils.ActivityTools;
import com.yjjr.yjfutures.utils.DateUtils;
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

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

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
        initViews();
        startPoll();
//        if (ActivityTools.isNeedShowGuide(mContext)) {
//            TradeGuideActivity.startActivity(mContext);
//        }
        // 统计用户登录
        MobclickAgent.onProfileSignIn(UserSharePrefernce.getAccount(this));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ShowRedDotEvent event) {
        mBottomBar.getTabView(2).showPoint();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HideRedDotEvent event) {
        mBottomBar.getTabView(2).removeShow();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CSUnreadEvent event) {
        AlphaTabView tabView = mBottomBar.getTabView(2);
        if (event.getCount() == 0) {
            UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
            if (userInfo == null || !userInfo.isExistUnreadNotice()) {
                tabView.removeShow();
            }
        } else {
            tabView.showPoint();
        }
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
                DateTime dateTime = DateUtils.nowDateTime();
                if (dateTime.getSecondOfMinute() == 10) {
                    EventBus.getDefault().post(new OneMinuteEvent());
                }
                if (BaseApplication.getInstance().isBackground()) {
                    return;
                }
                EventBus.getDefault().post(new PollRefreshEvent());

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
        }, 1000, 1000);
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
        HttpManager.getBizService().login(account, password, ActivityTools.getDeviceAndVerson())
                .compose(RxUtils.<BizResponse<UserInfo>>applyBizSchedulers())
                .compose(this.<BizResponse<UserInfo>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<BizResponse<UserInfo>>() {
                    @Override
                    public void accept(@NonNull BizResponse<UserInfo> response) throws Exception {
                        BaseApplication.getInstance().setUserInfo(response.getResult());
                    }
                }, RxUtils.commonErrorConsumer());
//        HttpManager.getHttpService().userLogin(account, password, ActivityTools.getIpAddressString())
        RxUtils.createZTLoginObservable(account, password, false)
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
        // 统计用户登出
        MobclickAgent.onProfileSignOff();
    }
}
