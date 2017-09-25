package com.yjjr.yjfutures.ui.trade;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.event.FastTakeOrderEvent;
import com.yjjr.yjfutures.event.OneMinuteEvent;
import com.yjjr.yjfutures.event.PollRefreshEvent;
import com.yjjr.yjfutures.event.PriceRefreshEvent;
import com.yjjr.yjfutures.event.SendOrderEvent;
import com.yjjr.yjfutures.model.CommonResponse;
import com.yjjr.yjfutures.model.FastTakeOrderConfig;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.model.biz.BizResponse;
import com.yjjr.yjfutures.model.biz.Funds;
import com.yjjr.yjfutures.model.biz.Holds;
import com.yjjr.yjfutures.store.FastOrderSharePrefernce;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.ui.SimpleFragmentPagerAdapter;
import com.yjjr.yjfutures.ui.WebActivity;
import com.yjjr.yjfutures.utils.ActivityTools;
import com.yjjr.yjfutures.utils.DialogUtils;
import com.yjjr.yjfutures.utils.DisplayUtils;
import com.yjjr.yjfutures.utils.DoubleUtil;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.SpannableUtil;
import com.yjjr.yjfutures.utils.StringUtils;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.CustomPromptDialog;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.NestRadioGroup;
import com.yjjr.yjfutures.widget.NoTouchScrollViewpager;
import com.yjjr.yjfutures.widget.dropdownmenu.MenuItem;
import com.yjjr.yjfutures.widget.dropdownmenu.TopRightMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class TradeFragment extends BaseFragment implements View.OnClickListener {

    private boolean mIsDemo;
    private ProgressBar pbLeft;
    private ProgressBar pbRight;
    private TextView tvLeft;
    private TextView tvRight;
    private String leftText = "看涨";
    private String rightText = "看跌";
    private TextView tvCenter;
    private ProgressDialog mProgressDialog;
    private NoTouchScrollViewpager mViewpager;
    private NestRadioGroup rgNav;
    private TextView mTvKchart;
    private TopRightMenu mTopRightMenu;
    private String mSymbol;
    private CandleStickChartFragment mCandleStickChartFragment;
    private TextView tvLeftPb;
    private TextView tvRightPb;
    private View vgOrder;
    private View vgSettlement;
    private TextView tvDirection;
    private TextView tvYueValue;
    private TextView tvMarginValue;
    private TextView tvTotal;
    private View colorView;
    private List<Holds> mHoldsList;
    private TextView tvPrice;
    private TextView tvChange;
    private TextView tvChangeRate;
    private CustomPromptDialog mCloseSuccessDialog;
    private CustomPromptDialog mCloseAllDialog;
    private CustomPromptDialog mCloseDialog;
    private HeaderView mHeaderView;
    /**
     * 买、卖的View，休市时隐藏
     */
    private View tradeView1;
    private View tradeView2;
    private View tradeView3;
    /**
     * 休市信息，休市时显示出来
     */
    private TextView tvRest;
    private TextView mTvDeposit;


    public TradeFragment() {
        // Required empty public constructor
    }

    public static TradeFragment newInstance(boolean isDemo, String symbol) {
        TradeFragment fragment = new TradeFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.CONTENT_PARAMETER, isDemo);
        args.putString(Constants.CONTENT_PARAMETER_2, symbol);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mIsDemo = getArguments().getBoolean(Constants.CONTENT_PARAMETER);
            mSymbol = getArguments().getString(Constants.CONTENT_PARAMETER_2);
        }
        mCloseSuccessDialog = new CustomPromptDialog.Builder(mContext)
                .setMessage("卖出委托成交完毕")
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        mCloseAllDialog = new CustomPromptDialog.Builder(mContext)
                .setMessage("您确定要卖出全部持仓么？")
                .isShowClose(true)
                .setMessageDrawableId(R.drawable.ic_info)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        closeAllOrder();
                    }
                })
                .isShowClose(true)
                .create();
        mCloseDialog = new CustomPromptDialog.Builder(mContext)
                .setMessage("您确定要平仓么？")
                .isShowClose(true)
                .setMessageDrawableId(R.drawable.ic_info)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        closeOrder();
                    }
                })
                .isShowClose(true)
                .create();
    }

    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trade, container, false);
        final Quote quote = StaticStore.getQuote(mSymbol, mIsDemo);
        findViews(v);
        setRestView(quote);
        mHeaderView.setOperateClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.startActivity(mContext, String.format(HttpConfig.URL_RULE, StringUtils.getRuleName(quote)));
            }
        });
        mCandleStickChartFragment = CandleStickChartFragment.newInstance(mSymbol, mIsDemo);
        Fragment[] fragments = {/*TickChartFragment.newInstance(mSymbol)*/new Fragment(), TimeSharingplanFragment.newInstance(mSymbol, mIsDemo),
                mCandleStickChartFragment, HandicapFragment.newInstance(mSymbol, mIsDemo)};
        mViewpager.setAdapter(new SimpleFragmentPagerAdapter(getChildFragmentManager(), fragments));
        mViewpager.setOffscreenPageLimit(fragments.length);
        rgNav.setOnCheckedChangeListener(new NestRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(NestRadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_chart1:
                        mViewpager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_chart2:
                        mViewpager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_chart4:
                        mViewpager.setCurrentItem(3, false);
                        break;
                }
                mTvKchart.setBackgroundColor(ContextCompat.getColor(mContext, R.color.background_dark));
                mTvKchart.setTextColor(ContextCompat.getColor(mContext, R.color.main_text_color));
                mTvKchart.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_white), null);
            }
        });
        ((RadioButton) rgNav.getChildAt(1)).setChecked(true);
        pbLeft.setRotation(180);
        mTopRightMenu = new TopRightMenu(getActivity());

//添加菜单项
        final List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(R.drawable.transport, "1分钟"));
        menuItems.add(new MenuItem(R.drawable.transport, "5分钟"));
        menuItems.add(new MenuItem(R.drawable.transport, "15分钟"));
        menuItems.add(new MenuItem(R.drawable.transport, "1小时"));
        menuItems.add(new MenuItem(R.drawable.transport, "日线"));
        mTopRightMenu
                .setWidth(DisplayUtils.dip2px(mContext, 100))      //默认宽度wrap_content
                .setHeight(DisplayUtils.dip2px(mContext, 30 * menuItems.size()))
                .showIcon(false)     //显示菜单图标，默认为true
                .dimBackground(true)        //背景变暗，默认为true
                .needAnimationStyle(true)   //显示动画，默认为true
                .addMenuList(menuItems)
                .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        rgNav.clearCheck();
                        mTvKchart.setText(menuItems.get(position).getText());
                        mTvKchart.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
                        mTvKchart.setBackgroundColor(ContextCompat.getColor(mContext, R.color.third_text_color));
                        mTvKchart.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow), null);
                        mViewpager.setCurrentItem(2, false);
                        String type = CandleStickChartFragment.MIN;
                        switch (position) {
                            case 0:
                                type = CandleStickChartFragment.MIN;
                                break;
                            case 1:
                                type = CandleStickChartFragment.MIN5;
                                break;
                            case 2:
                                type = CandleStickChartFragment.MIN15;
                                break;
                            case 3:
                                type = CandleStickChartFragment.HOUR;
                                break;
                            case 4:
                                type = CandleStickChartFragment.DAY;
                                break;
                        }
                        mCandleStickChartFragment.loadDataByType(type);
                    }
                });
        tvCenter.setText(FastOrderSharePrefernce.getFastTakeOrder(mContext, mSymbol) != null ? R.string.opened : R.string.closed);
        fillViews(quote);
        mHeaderView.setMainTitle(quote.getSymbolname());
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        v.findViewById(R.id.tv_center).setOnClickListener(this);
        v.findViewById(R.id.tv_close_order).setOnClickListener(this);
        mTvDeposit = (TextView) v.findViewById(R.id.tv_deposit);
        mTvDeposit.setSelected(true);
        mTvDeposit.setOnClickListener(this);
        v.findViewById(R.id.tv_kchart).setOnClickListener(this);
        v.findViewById(R.id.tv_fullscreen).setOnClickListener(this);
        DialogUtils.showGuideView(getActivity(), mTvDeposit);
        return v;
    }

    /**
     * 设置休市状态
     *
     * @param quote
     */
    private void setRestView(Quote quote) {
        if (quote.isRest()) { // 休市的状态
            tradeView1.setVisibility(View.GONE);
            tradeView2.setVisibility(View.GONE);
            tradeView3.setVisibility(View.GONE);
            tvRest.setVisibility(View.VISIBLE);
            tvRest.setText(TextUtils.concat(SpannableUtil.getStringBySize("休市中", 1.4f), String.format("\n下一个交易时间段：%s", quote.getTradingTime())));
        } else {
            tradeView1.setVisibility(View.VISIBLE);
            tradeView2.setVisibility(View.VISIBLE);
            tradeView3.setVisibility(View.VISIBLE);
            tvRest.setVisibility(View.GONE);
        }
    }

    private void fillViews(Quote quote) {
        if (quote == null) return;
        tvLeft.setText(leftText + StringUtils.getStringByTick(quote.getAskPrice(), quote.getTick()));
        tvRight.setText(StringUtils.getStringByTick(quote.getBidPrice(), quote.getTick()) + rightText);

        int allSize = quote.getBidSize() + quote.getAskSize();
        if (allSize != 0) {
            pbRight.setProgress(quote.getBidSize() * 100 / allSize);
            pbLeft.setProgress(quote.getAskSize() * 100 / allSize);
        }
        tvRightPb.setText(String.valueOf(quote.getBidSize()));
        tvLeftPb.setText(String.valueOf(quote.getAskSize()));
        tvPrice.setText(StringUtils.getStringByTick(quote.getLastPrice(), quote.getTick()));
        tvChange.setText(DoubleUtil.format2Decimal(quote.getChange()));
        tvChangeRate.setText(DoubleUtil.format2Decimal(quote.getChangeRate()) + "%");
    }


    private void findViews(View v) {
        mHeaderView = (HeaderView) v.findViewById(R.id.header_view);
        mHeaderView.bindActivity(getActivity());
        rgNav = (NestRadioGroup) v.findViewById(R.id.rg_nav);
        pbLeft = (ProgressBar) v.findViewById(R.id.pb_left);
        pbRight = (ProgressBar) v.findViewById(R.id.pb_right);
        tvLeft = (TextView) v.findViewById(R.id.tv_left);
        tvRight = (TextView) v.findViewById(R.id.tv_right);
        tvLeftPb = (TextView) v.findViewById(R.id.tv_left_pb);
        tvRightPb = (TextView) v.findViewById(R.id.tv_right_pb);
        tvCenter = (TextView) v.findViewById(R.id.tv_center);
        mViewpager = (NoTouchScrollViewpager) v.findViewById(R.id.viewpager);
        mTvKchart = (TextView) v.findViewById(R.id.tv_kchart);
        vgOrder = v.findViewById(R.id.vg_order);
        vgSettlement = v.findViewById(R.id.vg_settlement);
        tvDirection = (TextView) v.findViewById(R.id.tv_direction);
        tvYueValue = (TextView) v.findViewById(R.id.tv_yue_value);
        tvMarginValue = (TextView) v.findViewById(R.id.tv_margin_value);
        if (mIsDemo) {
            TextView tvYue = (TextView) v.findViewById(R.id.tv_yue);
            TextView tvMargin = (TextView) v.findViewById(R.id.tv_margin);
            tvYue.setText("可用金币");
            tvMargin.setText("保证金币");
        }
        tvTotal = (TextView) v.findViewById(R.id.tv_total);
        colorView = v.findViewById(R.id.view_color);
        tvPrice = (TextView) v.findViewById(R.id.tv_price);
        tvChange = (TextView) v.findViewById(R.id.tv_change);
        tvChangeRate = (TextView) v.findViewById(R.id.tv_change_rate);
        tradeView1 = v.findViewById(R.id.trade_view1);
        tradeView2 = v.findViewById(R.id.trade_view2);
        tradeView3 = v.findViewById(R.id.trade_view3);
        tvRest = (TextView) v.findViewById(R.id.tv_rest);
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(getString(R.string.operaing));
        mProgressDialog.setCancelable(false);
    }

    @Override
    protected void initData() {
        super.initData();
        getHolding();
    }

    private void getHolding() {
        HttpManager.getBizService(mIsDemo).getHolding()
                .flatMap(new Function<BizResponse<List<Holds>>, ObservableSource<Holds>>() {
                    @Override
                    public ObservableSource<Holds> apply(@NonNull BizResponse<List<Holds>> listBizResponse) throws Exception {
                        return Observable.fromIterable(listBizResponse.getResult());
                    }
                })
                .filter(new Predicate<Holds>() {
                    @Override
                    public boolean test(@NonNull Holds holding) throws Exception {
                        return TextUtils.equals(holding.getSymbol(), mSymbol);
                    }
                })
                .compose(RxUtils.<Holds>applySchedulers())
                .compose(this.<Holds>bindUntilEvent(FragmentEvent.DESTROY))
                .toList()
                .subscribe(new Consumer<List<Holds>>() {
                    @Override
                    public void accept(@NonNull List<Holds> holdses) throws Exception {
                        mHoldsList = holdses;
                        if (!holdses.isEmpty()) {
                            Holds holding = holdses.get(0);
                            // 总手数
                            int sumQty = 0;
                            double sumUnrealizedPL = 0; // 总浮动盈亏
                            // 遍历将这些参数累加
                            for (Holds h : holdses) {
                                sumQty += Math.abs(h.getQty());
                                sumUnrealizedPL += h.getUnrealizedPL();
                            }
                            vgSettlement.setVisibility(View.GONE);
                            vgOrder.setVisibility(View.VISIBLE);
                            tvDirection.setText((TextUtils.equals(holding.getBuySell(), "买入") ? "看涨" : "看跌") + Math.abs(sumQty) + "手");
                            tvTotal.setText(TextUtils.concat("持仓盈亏\n", StringUtils.formatUnrealizePL(mContext, sumUnrealizedPL)));
                            if (TextUtils.equals(holding.getBuySell(), "买入")) {
                                leftText = "追加";
                                rightText = "平仓";
                                tvDirection.setTextColor(ContextCompat.getColor(mContext, R.color.main_color_red));
                                colorView.setBackgroundResource(R.drawable.shape_online_tx_red);
                            } else {
                                leftText = "平仓";
                                rightText = "追加";
                                tvDirection.setTextColor(ContextCompat.getColor(mContext, R.color.main_color_green));
                                colorView.setBackgroundResource(R.drawable.shape_online_tx_green);
                            }
                        } else {
                            vgOrder.setVisibility(View.GONE);
                            vgSettlement.setVisibility(View.VISIBLE);
                            leftText = "看涨";
                            rightText = "看跌";
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        vgOrder.setVisibility(View.GONE);
                        vgSettlement.setVisibility(View.VISIBLE);
                        leftText = "看涨";
                        rightText = "看跌";
                    }
                });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PollRefreshEvent event) {
        if (getActivity() instanceof TradeActivity && ((TradeActivity) getActivity()).mIndex == 0) {
            getHolding();
            // 下面是轮询方式获取报价
//            Quote quote = StaticStore.getQuote(mSymbol, mIsDemo);
//            fillViews(quote);
            // end
            Funds result = StaticStore.getFunds(mIsDemo);
            tvYueValue.setText(getString(R.string.rmb_symbol) + DoubleUtil.format2Decimal(result.getAvailableFunds()));
            tvMarginValue.setText(getString(R.string.rmb_symbol) + DoubleUtil.format2Decimal(result.getFrozenMargin()));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OneMinuteEvent event) {
        // 每分钟检查一下是否开盘
        Quote quote = StaticStore.getQuote(mSymbol, mIsDemo);
        setRestView(quote);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PriceRefreshEvent event) {
        if (TextUtils.equals(event.getSymbol(), mSymbol)) {
            Quote quote = StaticStore.getQuote(mSymbol, mIsDemo);
            fillViews(quote);
            Funds result = StaticStore.getFunds(mIsDemo);
            tvYueValue.setText(getString(R.string.rmb_symbol) + DoubleUtil.format2Decimal(result.getAvailableFunds()));
            tvMarginValue.setText(getString(R.string.rmb_symbol) + DoubleUtil.format2Decimal(result.getFrozenMargin()));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FastTakeOrderEvent event) {
        tvCenter.setText(event.isOpened() ? R.string.opened : R.string.closed);
    }

    @Override
    public void onClick(View v) {
        FastTakeOrderConfig fastTakeOrder = FastOrderSharePrefernce.getFastTakeOrder(mContext, mSymbol);
        switch (v.getId()) {
            case R.id.tv_close_order:
                mCloseAllDialog.show();
                break;
            case R.id.tv_left:
                if (TextUtils.equals("平仓", leftText)) {
                    if (fastTakeOrder != null) {
                        closeOrder();
                    } else {
                        mCloseDialog.show();
                    }
                } else {
                    if (fastTakeOrder != null) {
                        //快速下单
                        takeOrder(fastTakeOrder, "买入");
                    } else {
                        TakeOrderActivity.startActivity(mContext, mSymbol, TakeOrderActivity.TYPE_BUY, mIsDemo);
                    }
                }
                break;
            case R.id.tv_right:
                if (TextUtils.equals("平仓", rightText)) {
                    if (fastTakeOrder != null) {
                        closeOrder();
                    } else {
                        mCloseDialog.show();
                    }
                } else {
                    if (fastTakeOrder != null) {
                        //快速下单
                        takeOrder(fastTakeOrder, "卖出");
                    } else {
                        TakeOrderActivity.startActivity(mContext, mSymbol, TakeOrderActivity.TYPE_SELL, mIsDemo);
                    }
                }
                break;
            case R.id.tv_center:
                FastTakeOrderActivity.startActivity(mContext, mSymbol, mIsDemo);
                break;
            case R.id.tv_deposit:
                if (mIsDemo) {
                    HttpManager.getBizService(true).resetCapitalAccount(BaseApplication.getInstance().getTradeToken(mIsDemo))
                            .compose(RxUtils.applyBizSchedulers())
                            .compose(this.<BizResponse>bindUntilEvent(FragmentEvent.DESTROY))
                            .subscribe(new Consumer<BizResponse>() {
                                @Override
                                public void accept(@NonNull BizResponse response) throws Exception {
                                    ToastUtils.show(mContext, R.string.opera_success);
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    LogUtils.e(throwable);
                                    ToastUtils.show(mContext, throwable.getMessage());
                                }
                            });
                } else {
                    ActivityTools.toDeposit(mContext);
                }
                break;
            case R.id.tv_kchart:
                mTopRightMenu.showAsDropDown(mTvKchart, 0, DisplayUtils.dip2px(mContext, 10));
                break;
            case R.id.tv_fullscreen:
                FullScreenChartActivity.startActivity(mContext, mSymbol, mIsDemo);
                break;
        }
    }

    private void closeOrder() {
        if (mHoldsList == null || mHoldsList.isEmpty()) return;
        mProgressDialog.show();
        RxUtils.createCloseObservable(mIsDemo, mHoldsList.get(0))
                .delay(1, TimeUnit.SECONDS)
                .compose(RxUtils.<CommonResponse>applySchedulers())
                .compose(this.<CommonResponse>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<CommonResponse>() {
                    @Override
                    public void accept(@NonNull CommonResponse response) throws Exception {
                        mCloseSuccessDialog.show();
                        mProgressDialog.dismiss();
                        EventBus.getDefault().post(new SendOrderEvent());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                        mProgressDialog.dismiss();
                        ToastUtils.show(mContext, throwable.getMessage());
                    }
                });
    }

    private void closeAllOrder() {
        if (mHoldsList == null || mHoldsList.isEmpty()) return;
        mProgressDialog.show();
        HttpManager.getBizService(mIsDemo).closeAllOrder(BaseApplication.getInstance().getTradeToken(mIsDemo), mSymbol)
                .delay(1, TimeUnit.SECONDS)
                .compose(RxUtils.<BizResponse>applyBizSchedulers())
                .compose(this.<BizResponse>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<BizResponse>() {
                    @Override
                    public void accept(@NonNull BizResponse response) throws Exception {
                        mCloseSuccessDialog.show();
                        mProgressDialog.dismiss();
                        EventBus.getDefault().post(new SendOrderEvent());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                        mProgressDialog.dismiss();
                        ToastUtils.show(mContext, throwable.getMessage());
                    }
                });
    }

    private void takeOrder(FastTakeOrderConfig order, final String type) {
        mProgressDialog.show();
        RxUtils.createSendOrderObservable(mIsDemo, mSymbol, type, Math.abs(order.getQty()),
                order.getStopLose(), order.getStopWin(), order.getFee(), order.getMarginYJ())
                .delay(1, TimeUnit.SECONDS)
                .compose(RxUtils.<BizResponse<CommonResponse>>applyBizSchedulers())
                .compose(this.<BizResponse<CommonResponse>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<BizResponse<CommonResponse>>() {
                    @Override
                    public void accept(@NonNull BizResponse<CommonResponse> commonResponse) throws Exception {
                        DialogUtils.createTakeOrderSuccessDialog(mContext, type).show();
                        mProgressDialog.dismiss();
                        EventBus.getDefault().post(new SendOrderEvent());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                        mProgressDialog.dismiss();
                        ToastUtils.show(mContext, throwable.getMessage());
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
