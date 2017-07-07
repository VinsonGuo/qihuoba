package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseActivity;
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


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        findViews();
        headerView.bindActivity(mContext);
    }


    private void findViews() {
        headerView = (HeaderView)findViewById( R.id.header_view );
        tvDirection = (TextView)findViewById( R.id.tv_direction );
        tvProfitYuan = (TextView)findViewById( R.id.tv_profit_yuan );
        tvProfitDollar = (TextView)findViewById( R.id.tv_profit_dollar );
        tvTradeSymbol = (TextView)findViewById( R.id.tv_trade_symbol );
        tvTradeNum = (TextView)findViewById( R.id.tv_trade_num );
        tvMargin = (TextView)findViewById( R.id.tv_margin );
        tvCharge = (TextView)findViewById( R.id.tv_charge );
        tvContractTime = (TextView)findViewById( R.id.tv_contract_time );
        tvBuyPrice = (TextView)findViewById( R.id.tv_buy_price );
        tvSellPrice = (TextView)findViewById( R.id.tv_sell_price );
        tvBuyType = (TextView)findViewById( R.id.tv_buy_type );
        tvSellType = (TextView)findViewById( R.id.tv_sell_type );
        tvBuyTime = (TextView)findViewById( R.id.tv_buy_time );
        tvSellTime = (TextView)findViewById( R.id.tv_sell_time );
        tvTicket = (TextView)findViewById( R.id.tv_ticket );
    }
}
