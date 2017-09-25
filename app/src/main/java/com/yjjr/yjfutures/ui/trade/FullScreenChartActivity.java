package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.model.HisData;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.utils.DateUtils;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.http.HttpManager;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class FullScreenChartActivity extends BaseActivity {

    public static final int DEFAULT_INDEX = 0;

    private int mLineColor;
    private int transparentColor;
    private int candleGridColor;
    private int mTextColor;

    private LineChart lineChart;
    private BarChart barChart;
    private XAxis xAxisLine;
    private YAxis axisLeftLine;
    private YAxis axisRightLine;
    private XAxis xAxisBar;
    private YAxis axisLeftBar;
    private YAxis axisRightBar;
    private String mSymbol;
    private boolean mIsDemo;

    public static void startActivity(Context context, String symbol, boolean isDemo) {
        Intent intent = new Intent(context, FullScreenChartActivity.class);
        intent.putExtra(Constants.CONTENT_PARAMETER, symbol);
        intent.putExtra(Constants.CONTENT_PARAMETER_2, isDemo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_chart);
        mSymbol = getIntent().getStringExtra(Constants.CONTENT_PARAMETER);
        mIsDemo = getIntent().getBooleanExtra(Constants.CONTENT_PARAMETER_2, false);
        lineChart = (LineChart) findViewById(R.id.line_chart);
        barChart = (BarChart) findViewById(R.id.bar_chart);
        initChart();
        requestData();
    }

    private void requestData() {
        Quote quote = StaticStore.getQuote(mSymbol, mIsDemo);
        if (quote == null) return;
        DateTime dateTime;
        if (quote.isRest()) { //未开盘，数据加载前一天的
            dateTime = DateUtils.nowDateTime();
            if (dateTime.getDayOfWeek() == 1 || dateTime.getDayOfWeek() == 7) { //星期一、星期天前一天还是没数据，要加载星期五的
                dateTime = dateTime.minusDays(1).withDayOfWeek(5).withHourOfDay(6).withMinuteOfHour(0).withSecondOfMinute(0);
            } else {
                dateTime = dateTime.minusDays(1).withHourOfDay(6).withMinuteOfHour(0).withSecondOfMinute(0);
            }
        } else {
            dateTime = DateUtils.nowDateTime().withHourOfDay(6).withMinuteOfHour(0).withSecondOfMinute(0);
        }
        HttpManager.getHttpService().getFsData(quote.getSymbol(), quote.getExchange(), DateUtils.formatData(dateTime.getMillis()))
//        HttpManager.getHttpService().getHistoryData(HttpConfig.KLINE_URL, new HistoryDataRequest(mQuote.getSymbol(), mQuote.getExchange(), DateUtils.formatData(dateTime.getMillis()), null))
                .map(new Function<List<HisData>, List<HisData>>() {
                    @Override
                    public List<HisData> apply(@NonNull List<HisData> hisDatas) throws Exception {
                        if (hisDatas == null || hisDatas.isEmpty()) {
                            throw new RuntimeException("数据为空");
                        }
                        return hisDatas;
                    }
                })
                .compose(RxUtils.<List<HisData>>applySchedulers())
                .compose(this.<List<HisData>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<List<HisData>>() {
                    @Override
                    public void accept(@NonNull List<HisData> list) throws Exception {
                        addData(list);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
//                        mChart.setNoDataText(getString(R.string.data_load_fail));
                    }
                });
    }


    private void addData(List<HisData> list) {
        LineData lineData = new LineData();
        BarData barData = new BarData();
        lineChart.setData(lineData);
        barChart.setData(barData);
        ILineDataSet lineDataSet = lineData.getDataSetByIndex(DEFAULT_INDEX);
        if(lineDataSet == null) {
            lineDataSet = createSet(0);
            lineData.addDataSet(lineDataSet);
        }

        IBarDataSet barDataSet = barData.getDataSetByIndex(DEFAULT_INDEX);
        if(barDataSet == null) {
            barDataSet = createBarSet();
            barData.addDataSet(barDataSet);
        }

        for (int i = 0; i < list.size(); i++) {
            HisData hisData = list.get(i);
            lineData.addEntry(new Entry(i, (float) hisData.getClose()), DEFAULT_INDEX);
            barData.addEntry(new BarEntry(i, hisData.getVol()), DEFAULT_INDEX);
        }
        lineChart.notifyDataSetChanged();
        barChart.notifyDataSetChanged();
    }

    private IBarDataSet createBarSet() {
        BarDataSet set = new BarDataSet(new ArrayList<BarEntry>(), null);
        return set;
    }

    private ILineDataSet createSet(int type) {
        LineDataSet set = new LineDataSet(null, null);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        if (type == 0) {
            set.setHighLightColor(mLineColor);
            set.setDrawHighlightIndicators(true);
            set.setHighlightLineWidth(0.5f);
            set.enableDashedHighlightLine(10, 5, 0);
            set.setCircleColor(mLineColor);
            set.setCircleRadius(2);
            set.setDrawCircleHole(false);
            set.setDrawFilled(true);
            set.setColor(mLineColor);
//            set.setFillColor(mLineColor);
            set.setFillDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_chart_fade));
        } else {
            set.setHighlightEnabled(false);
            set.setVisible(false);
            set.setColor(mLineColor);
            set.enableDashedLine(10, 5, 0);
            set.setDrawCircleHole(false);
            set.setCircleColor(transparentColor);
        }
        set.setDrawCircles(false);
        set.setLineWidth(1f);
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);

        return set;
    }


    private void initChart() {
        mLineColor = getResources().getColor(R.color.third_text_color);
        transparentColor = getResources().getColor(R.color.transparent);
        candleGridColor = getResources().getColor(R.color.color_333333);
        mTextColor = getResources().getColor(R.color.second_text_color);
        lineChart.setScaleEnabled(false);
        lineChart.setDrawBorders(true);
        lineChart.setBorderWidth(1);
        lineChart.setBorderColor(getResources().getColor(R.color.divider_color));
        lineChart.setDescription(null);
        Legend lineChartLegend = lineChart.getLegend();
        lineChartLegend.setEnabled(false);

        barChart.setScaleEnabled(false);
        barChart.setDrawBorders(true);
        barChart.setBorderWidth(1);
        barChart.setBorderColor(getResources().getColor(R.color.divider_color));
        barChart.setDescription(null);


        Legend barChartLegend = barChart.getLegend();
        barChartLegend.setEnabled(false);
        //x轴
        xAxisLine = lineChart.getXAxis();
        xAxisLine.setDrawLabels(true);
        xAxisLine.setPosition(XAxis.XAxisPosition.BOTTOM);
        // xAxisLine.setLabelsToSkip(59);


        //左边y
        axisLeftLine = lineChart.getAxisLeft();
        /*折线图y轴左没有basevalue，调用系统的*/
        axisLeftLine.setLabelCount(5, true);
        axisLeftLine.setDrawLabels(true);
        axisLeftLine.setDrawGridLines(false);
        /*轴不显示 避免和border冲突*/
        axisLeftLine.setDrawAxisLine(false);


        //右边y
        axisRightLine = lineChart.getAxisRight();
        axisRightLine.setLabelCount(2, true);
        axisRightLine.setDrawLabels(true);
        axisLeftLine.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00%");
                return mFormat.format(value);
            }
        });

        axisRightLine.setStartAtZero(false);
        axisRightLine.setDrawGridLines(false);
        axisRightLine.setDrawAxisLine(false);
        //背景线
        xAxisLine.setGridColor(getResources().getColor(R.color.divider_color));
        xAxisLine.enableGridDashedLine(10f, 5f, 0f);
        xAxisLine.setAxisLineColor(getResources().getColor(R.color.divider_color));
        xAxisLine.setTextColor(getResources().getColor(R.color.main_text_color));
        axisLeftLine.setGridColor(getResources().getColor(R.color.divider_color));
        axisLeftLine.setTextColor(getResources().getColor(R.color.divider_color));
        axisRightLine.setAxisLineColor(getResources().getColor(R.color.divider_color));
        axisRightLine.setTextColor(getResources().getColor(R.color.main_text_color));

        //bar x y轴
        xAxisBar = barChart.getXAxis();
        xAxisBar.setDrawLabels(false);
        xAxisBar.setDrawGridLines(true);
        xAxisBar.setDrawAxisLine(false);
        // xAxisBar.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisBar.setGridColor(getResources().getColor(R.color.divider_color));
        axisLeftBar = barChart.getAxisLeft();
        axisLeftBar.setAxisMinValue(0);
        axisLeftBar.setDrawGridLines(false);
        axisLeftBar.setDrawAxisLine(false);
        axisLeftBar.setTextColor(getResources().getColor(R.color.main_text_color));


        axisRightBar = barChart.getAxisRight();
        axisRightBar.setDrawLabels(false);
        axisRightBar.setDrawGridLines(false);
        axisRightBar.setDrawAxisLine(false);
        //y轴样式
        this.axisLeftLine.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00");
                return mFormat.format(value);
            }
        });

    }
}
