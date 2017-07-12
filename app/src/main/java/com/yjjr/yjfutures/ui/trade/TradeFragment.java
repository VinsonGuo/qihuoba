package com.yjjr.yjfutures.ui.trade;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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
import com.yjjr.yjfutures.event.RefreshEvent;
import com.yjjr.yjfutures.event.SendOrderEvent;
import com.yjjr.yjfutures.model.AccountInfo;
import com.yjjr.yjfutures.model.Holding;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.model.SendOrderResponse;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.store.UserSharePrefernce;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.BaseFragment;
import com.yjjr.yjfutures.ui.SimpleFragmentPagerAdapter;
import com.yjjr.yjfutures.utils.DoubleUtil;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.StringUtils;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;
import com.yjjr.yjfutures.widget.CustomPromptDialog;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.NestRadioGroup;
import com.yjjr.yjfutures.widget.NoTouchScrollViewpager;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;

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

    private boolean mIsNeedBack;
    private ProgressBar pbLeft;
    private ProgressBar pbRight;
    private TextView tvLeft;
    private TextView tvRight;
    private TextView tvLeftArrow;
    private TextView tvRightArrow;
    private TextView tvCenter;
    private CustomPromptDialog mTakeOrderDialog;
    private CustomPromptDialog mCloseOrderDialog;
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
    /**
     * 持仓的对象
     */
    private Holding mHolding;


    public TradeFragment() {
        // Required empty public constructor
    }

    public static TradeFragment newInstance(boolean isNeedBack, String symbol) {
        TradeFragment fragment = new TradeFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.CONTENT_PARAMETER, isNeedBack);
        args.putString(Constants.CONTENT_PARAMETER_2, symbol);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mIsNeedBack = getArguments().getBoolean(Constants.CONTENT_PARAMETER);
            mSymbol = getArguments().getString(Constants.CONTENT_PARAMETER_2);
        }
    }

    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trade, container, false);
        Quote quote = StaticStore.sQuoteMap.get(mSymbol);
        findViews(v);
        mCandleStickChartFragment = CandleStickChartFragment.newInstance(mSymbol);
        Fragment[] fragments = {TickChartFragment.newInstance(mSymbol), TimeSharingplanFragment.newInstance(mSymbol),
                mCandleStickChartFragment, HandicapFragment.newInstance(mSymbol)};
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
                mTvKchart.setBackgroundResource(R.drawable.shape_trade_rb_bg_unchecked);
                mTvKchart.setTextColor(ContextCompat.getColor(mContext, R.color.second_text_color));
            }
        });
        ((RadioButton) rgNav.getChildAt(0)).setChecked(true);
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
                .setWidth(220)      //默认宽度wrap_content
                .showIcon(false)     //显示菜单图标，默认为true
                .dimBackground(true)        //背景变暗，默认为true
                .needAnimationStyle(true)   //显示动画，默认为true
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                .addMenuList(menuItems)
                .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        rgNav.clearCheck();
                        mTvKchart.setText(menuItems.get(position).getText());
                        mTvKchart.setTextColor(ContextCompat.getColor(mContext, R.color.main_text_color));
                        mTvKchart.setBackgroundResource(R.drawable.shape_trade_rb_bg_checked);
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
        tvCenter.setText(UserSharePrefernce.isFastTakeOrder(mContext) ? R.string.opened : R.string.closed);
        fillViews(quote);
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        v.findViewById(R.id.tv_center).setOnClickListener(this);
        v.findViewById(R.id.tv_close_order).setOnClickListener(this);
        v.findViewById(R.id.tv_deposit).setOnClickListener(this);
        v.findViewById(R.id.tv_kchart).setOnClickListener(this);
        return v;
    }

    private void fillViews(Quote quote) {
        if (quote == null) return;
        double change = quote.getChangeRate();
        StringUtils.setOnlineTxTextStyleLeft(tvLeft, quote.getBidPrice() + "", change);
        StringUtils.setOnlineTxArrow(tvLeftArrow, change);
        StringUtils.setOnlineTxTextStyleRight(tvRight, quote.getAskPrice() + "", change);
        StringUtils.setOnlineTxArrow(tvRightArrow, change);
        pbLeft.setProgress(quote.getBidSize());
        pbRight.setProgress(quote.getAskSize());
        tvLeftPb.setText(String.valueOf(quote.getBidSize()));
        tvRightPb.setText(String.valueOf(quote.getAskSize()));
    }


    private void findViews(View v) {
        HeaderView headerView = (HeaderView) v.findViewById(R.id.header_view);
        headerView.bindActivity(getActivity());
        headerView.setMainTitle(mSymbol);
        rgNav = (NestRadioGroup) v.findViewById(R.id.rg_nav);
        pbLeft = (ProgressBar) v.findViewById(R.id.pb_left);
        pbRight = (ProgressBar) v.findViewById(R.id.pb_right);
        tvLeft = (TextView) v.findViewById(R.id.tv_left);
        tvRight = (TextView) v.findViewById(R.id.tv_right);
        tvLeftPb = (TextView) v.findViewById(R.id.tv_left_pb);
        tvRightPb = (TextView) v.findViewById(R.id.tv_right_pb);
        tvLeftArrow = (TextView) v.findViewById(R.id.tv_left_arrow);
        tvRightArrow = (TextView) v.findViewById(R.id.tv_right_arrow);
        tvCenter = (TextView) v.findViewById(R.id.tv_center);
        mViewpager = (NoTouchScrollViewpager) v.findViewById(R.id.viewpager);
        mTvKchart = (TextView) v.findViewById(R.id.tv_kchart);
        vgOrder = v.findViewById(R.id.vg_order);
        vgSettlement = v.findViewById(R.id.vg_settlement);
        tvDirection = (TextView) v.findViewById(R.id.tv_direction);
        tvYueValue = (TextView) v.findViewById(R.id.tv_yue_value);
        tvMarginValue = (TextView) v.findViewById(R.id.tv_margin_value);
        mTakeOrderDialog = new CustomPromptDialog.Builder(mContext)
                .setMessage("确定要下单么")
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mProgressDialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mProgressDialog.dismiss();
                                mTakeOrderDialog.dismiss();
                                ToastUtils.show(mContext, R.string.online_transaction_open_success);
                            }
                        }, 3000);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        mCloseOrderDialog = new CustomPromptDialog.Builder(mContext)
                .setMessage("确定要卖出全部持仓么")
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(getString(R.string.online_transaction_in_order));
        mProgressDialog.setCancelable(false);
    }

    @Override
    protected void initData() {
        super.initData();
        vgOrder.setVisibility(View.GONE);
        vgSettlement.setVisibility(View.VISIBLE);
        HttpManager.getHttpService().getHolding(BaseApplication.getInstance().getTradeToken())
                .flatMap(new Function<List<Holding>, ObservableSource<Holding>>() {
                    @Override
                    public ObservableSource<Holding> apply(@NonNull List<Holding> holdings) throws Exception {
                        return Observable.fromIterable(holdings);
                    }
                })
                .filter(new Predicate<Holding>() {
                    @Override
                    public boolean test(@NonNull Holding holding) throws Exception {
                        return TextUtils.equals(holding.getSymbol(), mSymbol) && holding.getQty() != 0;
                    }
                })
                .retry()
                .compose(RxUtils.<Holding>applySchedulers())
                .compose(this.<Holding>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<Holding>() {
                    @Override
                    public void accept(@NonNull Holding holding) throws Exception {
                        mHolding = holding;
                        vgSettlement.setVisibility(View.GONE);
                        vgOrder.setVisibility(View.VISIBLE);
                        tvDirection.setText(holding.getBuySell() + Math.abs(holding.getQty()) + "手");
                    }
                }, RxUtils.commonErrorConsumer());
        HttpManager.getHttpService().getAccountInfo(BaseApplication.getInstance().getTradeToken())
                .retry()
                .compose(RxUtils.<AccountInfo>applySchedulers())
                .compose(this.<AccountInfo>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<AccountInfo>() {
                    @Override
                    public void accept(@NonNull AccountInfo accountInfo) throws Exception {
                        tvYueValue.setText(DoubleUtil.format2Decimal(accountInfo.getAvailableFund()));
                        tvMarginValue.setText(DoubleUtil.format2Decimal(accountInfo.getFrozenMargin()));
                    }
                }, RxUtils.commonErrorConsumer());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SendOrderEvent event) {
        initData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshEvent event) {
        Quote quote = StaticStore.sQuoteMap.get(mSymbol);
        fillViews(quote);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FastTakeOrderEvent event) {
        tvCenter.setText(event.isOpened() ? R.string.opened : R.string.closed);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close_order:
                if (mHolding == null) return;
                mProgressDialog.show();
                RxUtils.createCloseObservable(mHolding)
                        .delay(1, TimeUnit.SECONDS)
                        .compose(RxUtils.<SendOrderResponse>applySchedulers())
                        .compose(this.<SendOrderResponse>bindUntilEvent(FragmentEvent.DESTROY))
                        .subscribe(new Consumer<SendOrderResponse>() {
                            @Override
                            public void accept(@NonNull SendOrderResponse response) throws Exception {
                                ToastUtils.show(mContext, response.getMessage());
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
                break;
            case R.id.tv_left:
                if (UserSharePrefernce.isFastTakeOrder(mContext)) {
                    mTakeOrderDialog.show();
                } else {
                    TakeOrderActivity.startActivity(mContext, mSymbol, TakeOrderActivity.TYPE_BUY);
                }
                break;
            case R.id.tv_right:
                if (UserSharePrefernce.isFastTakeOrder(mContext)) {
                    mTakeOrderDialog.show();
                } else {
                    TakeOrderActivity.startActivity(mContext, mSymbol, TakeOrderActivity.TYPE_SELL);
                }
                break;
            case R.id.tv_center:
                FastTakeOrderActivity.startActivity(mContext);
                break;
            case R.id.tv_deposit:
                DepositActivity.startActivity(mContext);
                break;
            case R.id.tv_kchart:
                mTopRightMenu.showAsDropDown(mTvKchart);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
