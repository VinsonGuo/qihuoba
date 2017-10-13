package com.yjjr.yjfutures.ui.home;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.CSUnreadEvent;
import com.yjjr.yjfutures.event.PriceRefreshEvent;
import com.yjjr.yjfutures.event.ReloginDialogEvent;
import com.yjjr.yjfutures.event.SendOrderEvent;
import com.yjjr.yjfutures.event.ShowRedDotEvent;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.model.UserLoginResponse;
import com.yjjr.yjfutures.model.biz.Active;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.Holds;
import com.yjjr.yjfutures.model.biz.Info;
import com.yjjr.yjfutures.model.biz.UserInfo;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.ui.WebActivity;
import com.yjjr.yjfutures.ui.trade.DemoTradeActivity;
import com.yjjr.yjfutures.ui.trade.TradeActivity;
import com.yjjr.yjfutures.utils.ActivityTools;
import com.yjjr.yjfutures.utils.DialogUtils;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.SocketUtils;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.utils.imageloader.ImageLoader;
import com.yjjr.yjfutures.widget.LoadingView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * "主页"
 */
public class HomePageFragment extends BaseFragment implements View.OnClickListener {


    private ConvenientBanner<Info> mBanner;
    private HomePageAdapter mAdapter;
    private LoadingView mLoadingView;
    private Gson mGson = new Gson();
    private EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
        }

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            EventBus.getDefault().post(new CSUnreadEvent(messages.size()));
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

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
        RecyclerView.ItemAnimator animator = rvList.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new HomePageAdapter(null);
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.header_home_page, rvList, false);
        mBanner = (ConvenientBanner<Info>) headerView.findViewById(R.id.banner);
        View demoView = headerView.findViewById(R.id.tv_header_title1);
        demoView.setOnClickListener(this);
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
        View csService = view.findViewById(R.id.tv_customer_service);
        csService.setOnClickListener(this);
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
                    .compose(this.<Boolean>bindUntilEvent(FragmentEvent.DESTROY))
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@NonNull Boolean symbols) throws Exception {
                            mLoadingView.setVisibility(View.GONE);
                            getHolding();
                            getHuodong();
                            initSocket();
                            loginHx();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            LogUtils.e(throwable);
                            mLoadingView.loadFail();
                        }
                    });
        } else {
            mLoadingView.setVisibility(View.GONE);
            getHolding();
            getHuodong();
            initSocket();
            loginHx();
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

    /**
     * 登陆环信
     */
    private void loginHx() {
        final UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
        if (TextUtils.isEmpty(userInfo.getEmchatAccount()) || TextUtils.isEmpty(userInfo.getEmchatPwd())) {
            return;
        }
        EMClient.getInstance().login(userInfo.getEmchatAccount(), userInfo.getEmchatPwd(), new EMCallBack() {
            @Override
            public void onSuccess() {
                //获取未读消息数量
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userInfo.getYjEmchat());
                if (conversation != null) {
                    LogUtils.d("未读消息%s条", conversation.getUnreadMsgCount());
                    EventBus.getDefault().post(new CSUnreadEvent(conversation.getUnreadMsgCount()));
                } else {
                    EventBus.getDefault().post(new CSUnreadEvent(0));
                }
                EMClient.getInstance().chatManager().addMessageListener(msgListener);
            }

            @Override
            public void onError(int code, String error) {
            }

            @Override
            public void onProgress(int progress, String status) {
            }
        });
    }

    private void initSocket() {
        SocketUtils.init();
        if (SocketUtils.getSocket() == null) {
            mLoadingView.loadFail();
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
                    mLoadingView.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setNewData(new ArrayList<>(StaticStore.getQuoteValues(false)));
                        }
                    });
                } catch (Exception e) {
                    LogUtils.e(e);
                }
            }
        });
        SocketUtils.getSocket().connect();
        SocketUtils.getSocket().emit("getSymbolList");
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

    private void getHuodong() {
        HttpManager.getBizService().getActivity()
                .compose(RxUtils.<BizResponse<Active>>applyBizSchedulers())
                .compose(this.<BizResponse<Active>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<BizResponse<Active>>() {
                    @Override
                    public void accept(@NonNull BizResponse<Active> response) throws Exception {
                        if (response.getResult() != null) {
                            DialogUtils.createImageDialog(mContext, response.getResult()).show();
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
                WebActivity.startActivity(mContext, HttpConfig.URL_CSCENTER, WebActivity.TYPE_CSCENTER);
//                ChatActivity.startActivity(mContext);
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
            Quote quote = StaticStore.getQuote(event.getSymbol(), false);
            int position = mAdapter.getData().indexOf(quote);
            mAdapter.notifyItemChanged(mAdapter.getHeaderLayoutCount() + position);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        EMClient.getInstance().logout(true);
        if (SocketUtils.getSocket() != null && SocketUtils.getSocket().connected()) {
            SocketUtils.getSocket().disconnect();
        }
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
