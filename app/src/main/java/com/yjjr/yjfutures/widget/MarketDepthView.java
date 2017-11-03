package com.yjjr.yjfutures.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.MarketDepth;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.StringUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by dell on 2017/11/3.
 */

public class MarketDepthView extends LinearLayout {

    private TextView buyPrice1;
    private TextView buyVol1;
    private TextView buyPrice2;
    private TextView buyVol2;
    private TextView buyPrice3;
    private TextView buyVol3;
    private TextView buyPrice4;
    private TextView buyVol4;
    private TextView buyPrice5;
    private TextView buyVol5;
    private TextView sellPrice1;
    private TextView sellVol1;
    private TextView sellPrice2;
    private TextView sellVol2;
    private TextView sellPrice3;
    private TextView sellVol3;
    private TextView sellPrice4;
    private TextView sellVol4;
    private TextView sellPrice5;
    private TextView sellVol5;
    private Comparator<MarketDepth> mComparator = new Comparator<MarketDepth>() {
        @Override
        public int compare(MarketDepth o1, MarketDepth o2) {
            if (o1.getPosition() == o2.getPosition()) {
                return o1.getSide() - o2.getSide();
            }
            return o1.getPosition() - o2.getPosition();
        }
    };


    public MarketDepthView(Context context) {
        super(context);
        init(context);
    }

    public MarketDepthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MarketDepthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_market_depth, this);
        buyPrice1 = (TextView) findViewById(R.id.buy_price1);
        buyVol1 = (TextView) findViewById(R.id.buy_vol1);
        buyPrice2 = (TextView) findViewById(R.id.buy_price2);
        buyVol2 = (TextView) findViewById(R.id.buy_vol2);
        buyPrice3 = (TextView) findViewById(R.id.buy_price3);
        buyVol3 = (TextView) findViewById(R.id.buy_vol3);
        buyPrice4 = (TextView) findViewById(R.id.buy_price4);
        buyVol4 = (TextView) findViewById(R.id.buy_vol4);
        buyPrice5 = (TextView) findViewById(R.id.buy_price5);
        buyVol5 = (TextView) findViewById(R.id.buy_vol5);
        sellPrice1 = (TextView) findViewById(R.id.sell_price1);
        sellVol1 = (TextView) findViewById(R.id.sell_vol1);
        sellPrice2 = (TextView) findViewById(R.id.sell_price2);
        sellVol2 = (TextView) findViewById(R.id.sell_vol2);
        sellPrice3 = (TextView) findViewById(R.id.sell_price3);
        sellVol3 = (TextView) findViewById(R.id.sell_vol3);
        sellPrice4 = (TextView) findViewById(R.id.sell_price4);
        sellVol4 = (TextView) findViewById(R.id.sell_vol4);
        sellPrice5 = (TextView) findViewById(R.id.sell_price5);
        sellVol5 = (TextView) findViewById(R.id.sell_vol5);
    }

    public void setData(List<MarketDepth> list, double tick) {
        try {
            Collections.sort(list, mComparator);
            MarketDepth sellDepth1 = list.get(0);
            sellPrice1.setText(StringUtils.getStringByTick(sellDepth1.getPrice(), tick));
            sellVol1.setText(String.valueOf(sellDepth1.getSize()));
            MarketDepth buyDepth1 = list.get(1);
            buyPrice1.setText(StringUtils.getStringByTick(buyDepth1.getPrice(), tick));
            buyVol1.setText(String.valueOf(buyDepth1.getSize()));

            MarketDepth sellDepth2 = list.get(2);
            sellPrice2.setText(StringUtils.getStringByTick(sellDepth2.getPrice(), tick));
            sellVol2.setText(String.valueOf(sellDepth2.getSize()));
            MarketDepth buyDepth2 = list.get(3);
            buyPrice2.setText(StringUtils.getStringByTick(buyDepth2.getPrice(), tick));
            buyVol2.setText(String.valueOf(buyDepth2.getSize()));

            MarketDepth sellDepth3 = list.get(4);
            sellPrice3.setText(StringUtils.getStringByTick(sellDepth3.getPrice(), tick));
            sellVol3.setText(String.valueOf(sellDepth3.getSize()));
            MarketDepth buyDepth3 = list.get(5);
            buyPrice3.setText(StringUtils.getStringByTick(buyDepth3.getPrice(), tick));
            buyVol3.setText(String.valueOf(buyDepth3.getSize()));

            MarketDepth sellDepth4 = list.get(6);
            sellPrice4.setText(StringUtils.getStringByTick(sellDepth4.getPrice(), tick));
            sellVol4.setText(String.valueOf(sellDepth4.getSize()));
            MarketDepth buyDepth4 = list.get(7);
            buyPrice4.setText(StringUtils.getStringByTick(buyDepth4.getPrice(), tick));
            buyVol4.setText(String.valueOf(buyDepth4.getSize()));

            MarketDepth sellDepth5 = list.get(8);
            sellPrice5.setText(StringUtils.getStringByTick(sellDepth5.getPrice(), tick));
            sellVol5.setText(String.valueOf(sellDepth5.getSize()));
            MarketDepth buyDepth5 = list.get(9);
            buyPrice5.setText(StringUtils.getStringByTick(buyDepth5.getPrice(), tick));
            buyVol5.setText(String.valueOf(buyDepth5.getSize()));
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }
}
