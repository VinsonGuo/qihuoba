package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.model.CloseOrder;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.utils.DateUtils;
import com.yjjr.yjfutures.utils.DoubleUtil;
import com.yjjr.yjfutures.utils.StringUtils;
import com.yjjr.yjfutures.widget.HeaderView;

/**
 * 订单详情页面
 */
public class OrderDetailActivity extends BaseActivity {
    private HeaderView headerView;
    private TextView tvDirection;
    private TextView tvProfitYuan;
    private TextView tvProfitDollar;
    private TextView tvTradeSymbol;
    private TextView tvTradeNum;
    private TextView tvMargin;
    private TextView tvCharge;
    private TextView tvContractTime;
    private TextView tvBuyPrice;
    private TextView tvSellPrice;
    private TextView tvBuyType;
    private TextView tvSellType;
    private TextView tvBuyTime;
    private TextView tvSellTime;
    private TextView tvTicket;


    public static void startActivity(Context context, CloseOrder order) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(Constants.CONTENT_PARAMETER, order);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        CloseOrder order = getIntent().getParcelableExtra(Constants.CONTENT_PARAMETER);
        findViews();
        fillViews(order);

    }

    private void fillViews(CloseOrder order) {
        Quote quote = StaticStore.sQuoteMap.get(order.getSymbol());
        headerView.bindActivity(mContext);
        if (TextUtils.equals(order.getOpenBuySell(), "买入")) {
            tvDirection.setText("看涨");
            tvDirection.setBackgroundResource(R.drawable.shape_online_tx_red);
        } else {
            tvDirection.setText("看跌");
            tvDirection.setBackgroundResource(R.drawable.shape_online_tx_green);
        }
        tvProfitYuan.setText(DoubleUtil.format2Decimal(order.getRealizedPL_CNY()));
        int color = StringUtils.getProfitColor(mContext, order.getRealizedPL());
        tvProfitYuan.setTextColor(color);
        tvProfitDollar.setTextColor(color);
        tvProfitDollar.setText(DoubleUtil.format2Decimal(order.getRealizedPL()) + String.format("汇率(%s)", order.getExchange()));
        tvTradeSymbol.setText(order.getSymbol());
        tvTradeNum.setText(Math.abs(order.getQty()) + "手");
        tvBuyPrice.setText(StringUtils.getStringByTick(order.getOpenPrice(),quote.getTick()));
        tvSellPrice.setText(StringUtils.getStringByTick(order.getClosePrice(),quote.getTick()));
        tvBuyTime.setText(DateUtils.formatData(order.getOpenDate()));
        tvSellTime.setText(DateUtils.formatData(order.getCloseDate()));
//        tvTicket.setText(order.getFilledID());
        tvBuyType.setText("市价买入");
        tvSellType.setText("市价卖出");
    }


    private void findViews() {
        headerView = (HeaderView) findViewById(R.id.header_view);
        tvDirection = (TextView) findViewById(R.id.tv_direction);
        tvProfitYuan = (TextView) findViewById(R.id.tv_profit_yuan);
        tvProfitDollar = (TextView) findViewById(R.id.tv_profit_dollar);
        tvTradeSymbol = (TextView) findViewById(R.id.tv_trade_symbol);
        tvTradeNum = (TextView) findViewById(R.id.tv_trade_num);
        tvMargin = (TextView) findViewById(R.id.tv_margin);
        tvCharge = (TextView) findViewById(R.id.tv_charge);
        tvContractTime = (TextView) findViewById(R.id.tv_contract_time);
        tvBuyPrice = (TextView) findViewById(R.id.tv_buy_price);
        tvSellPrice = (TextView) findViewById(R.id.tv_sell_price);
        tvBuyType = (TextView) findViewById(R.id.tv_buy_type);
        tvSellType = (TextView) findViewById(R.id.tv_sell_type);
        tvBuyTime = (TextView) findViewById(R.id.tv_buy_time);
        tvSellTime = (TextView) findViewById(R.id.tv_sell_time);
        tvTicket = (TextView) findViewById(R.id.tv_ticket);
    }
}
