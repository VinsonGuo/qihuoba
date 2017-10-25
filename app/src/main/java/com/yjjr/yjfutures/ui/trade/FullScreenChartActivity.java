package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.Gson;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.model.HisData;
import com.yjjr.yjfutures.model.HistoryDataRequest;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.store.StaticStore;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.utils.DateUtils;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.SocketUtils;
import com.yjjr.yjfutures.utils.StringUtils;
import com.yjjr.yjfutures.widget.chart.AppCombinedChart;
import com.yjjr.yjfutures.widget.chart.CoupleChartGestureListener;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;

public class FullScreenChartActivity extends BaseActivity {

    public static final int DEFAULT_INDEX = 0;

    protected AppCombinedChart mChartPrice;
    protected AppCombinedChart mChartVolume;

    //开，收，高，低，量，换，额，查，比
    protected TextView mTvOpen, mTvClose, mTvMax, mTvMin, mTvNum, mTvExchange, mTvAmount, mTvSub, mTvPercent;

    protected XAxis xAxisPrice;
    protected YAxis axisRightPrice;
    protected YAxis axisLeftPrice;

    protected XAxis xAxisVolume;
    protected YAxis axisRightVolume;
    protected YAxis axisLeftVolume;
    private String mSymbol;
    private boolean mIsDemo;
    private List<HisData> mData = new ArrayList<>(300);
    private Gson mGson = new Gson();

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
        mChartPrice = (AppCombinedChart) findViewById(R.id.line_chart);
        mChartVolume = (AppCombinedChart) findViewById(R.id.bar_chart);

        initChartPrice();
        initChartVolume();
        initChartListener();

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

        if (SocketUtils.getSocket() == null) {
//            mChart.setNoDataText(getString(R.string.data_load_fail));
            return;
        }
        SocketUtils.getSocket().emit(SocketUtils.HIS_DATA, mGson.toJson(new HistoryDataRequest(quote.getSymbol(), quote.getExchange(), DateUtils.formatData(dateTime.getMillis()), "min")));
        SocketUtils.getSocket().once(SocketUtils.HIS_DATA, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogUtils.d("history data -> " + args[0].toString());
                mData.clear();
                final List<HisData> list = StringUtils.parseHisData(args[0].toString(), null);
                mData.addAll(list);

                mChartPrice.post(new Runnable() {
                    @Override
                    public void run() {
                        if (list.isEmpty()) {
//                            mChart.setNoDataText(getString(R.string.data_is_null));
                            return;
                        }

                        initChartPriceData(mChartPrice);
                        initChartVolumeData(mChartVolume);
                    }
                });
            }
        });
    }


    private void initChartPrice() {
        mChartPrice.setScaleEnabled(true);//启用图表缩放事件
        mChartPrice.setDrawBorders(true);//是否绘制边线
        mChartPrice.setBorderWidth(1);//边线宽度，单位dp
        mChartPrice.setDragEnabled(true);//启用图表拖拽事件
        mChartPrice.setScaleYEnabled(false);//启用Y轴上的缩放
//        mChartPrice.setBorderColor(getResources().getColor(R.color.border_color));//边线颜色
        mChartPrice.getDescription().setEnabled(false);//右下角对图表的描述信息
        mChartPrice.setHardwareAccelerationEnabled(true);//是否不开启硬件加速
        mChartPrice.setMinOffset(0f);//设置上下内边距
//        mChartPrice.setMinOffsetLR(0f);//设置左右内边距

        Legend lineChartLegend = mChartPrice.getLegend();//主要控制左下方的图例的
        lineChartLegend.setEnabled(false);//是否绘制 Legend 图例

        //x轴
        xAxisPrice = mChartPrice.getXAxis();//控制X轴的
        xAxisPrice.setDrawLabels(true);//是否显示X坐标轴上的刻度，默认是true
        xAxisPrice.setDrawAxisLine(false);//是否绘制坐标轴的线，即含有坐标的那条线，默认是true
        xAxisPrice.setDrawGridLines(false);//是否显示X坐标轴上的刻度竖线，默认是true
//        xAxisPrice.setPosition(XAxis.XAxisPosition.BOTTOM);//把坐标轴放在上下 参数有：TOP, BOTTOM, BOTH_SIDED, TOP_INSIDE or BOTTOM_INSIDE.
        xAxisPrice.enableGridDashedLine(10f, 10f, 0f);//绘制成虚线，只有在关闭硬件加速的情况下才能使用

        //左边y
        axisLeftPrice = mChartPrice.getAxisLeft();
        axisLeftPrice.setLabelCount(5, false); //第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
        axisLeftPrice.setDrawLabels(true);//是否显示Y坐标轴上的刻度，默认是true
        axisLeftPrice.setDrawGridLines(false);//是否显示Y坐标轴上的刻度竖线，默认是true
        /*轴不显示 避免和border冲突*/
        axisLeftPrice.setDrawAxisLine(true);//是否绘制坐标轴的线，即含有坐标的那条线，默认是true
//        axisLeftPrice.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART); //参数是INSIDE_CHART(Y轴坐标在内部) 或 OUTSIDE_CHART(在外部（默认是这个）)

        //右边y
        axisRightPrice = mChartPrice.getAxisRight();
        axisRightPrice.setLabelCount(5, false);//参考上面
        axisRightPrice.setDrawLabels(false);//参考上面
        axisRightPrice.setDrawGridLines(false);//参考上面
        axisRightPrice.setDrawAxisLine(true);//参考上面


    }


    private void initChartVolume() {
        mChartVolume.setScaleEnabled(true);//启用图表缩放事件
        mChartVolume.setDrawBorders(true);//是否绘制边线
        mChartVolume.setBorderWidth(1);//边线宽度，单位dp
        mChartVolume.setDragEnabled(true);//启用图表拖拽事件
        mChartVolume.setScaleYEnabled(false);//启用Y轴上的缩放
//        mChartVolume.setBorderColor(getResources().getColor(R.color.border_color));//边线颜色
        mChartVolume.getDescription().setEnabled(false);//右下角对图表的描述信息
        mChartVolume.setHardwareAccelerationEnabled(true);//是否开启硬件加速

        Legend lineChartLegend = mChartVolume.getLegend();
        lineChartLegend.setEnabled(false);//是否绘制 Legend 图例

        //x轴
        xAxisVolume = mChartVolume.getXAxis();
        xAxisVolume.setEnabled(false);//是否绘制X轴的数据
        xAxisVolume.setDrawLabels(false);
        xAxisVolume.setDrawAxisLine(false);
        xAxisVolume.setDrawGridLines(false);

        //左边y
        axisLeftVolume = mChartVolume.getAxisLeft();
        axisLeftVolume.setDrawLabels(false);//参考上面
        axisLeftVolume.setDrawGridLines(false);//参考上面
        /*轴不显示 避免和border冲突*/
        axisLeftVolume.setDrawAxisLine(false);//参考上面
//        axisLeftVolume.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);//参考上面


        //右边y
        axisRightVolume = mChartVolume.getAxisRight();
        axisRightVolume.setAxisMinValue(0);//参考上面
//        axisRightVolume.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);//参考上面
        axisRightVolume.setDrawLabels(true);//参考上面
        axisRightVolume.setDrawGridLines(true);//参考上面
        axisRightVolume.setDrawAxisLine(false);//参考上面


    }

    private void initChartListener() {
        // 将K线控的滑动事件传递给交易量控件
        mChartPrice.setOnChartGestureListener(new CoupleChartGestureListener(mChartPrice, mChartVolume));
//        // 将交易量控件的滑动事件传递给K线控件
        mChartVolume.setOnChartGestureListener(new CoupleChartGestureListener(mChartVolume, mChartPrice));
        mChartPrice.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                mChartVolume.highlightValues(new Highlight[]{h});
            }


            @Override
            public void onNothingSelected() {
                mChartVolume.highlightValue(null);
            }
        });

        mChartVolume.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                mChartPrice.highlightValues(new Highlight[]{h});
            }

            @Override
            public void onNothingSelected() {
                mChartPrice.highlightValue(null);
            }
        });

    }


    private void initChartPriceData(AppCombinedChart combinedChartX) {

        ArrayList<Entry> lineCJEntries = new ArrayList<>();
        ArrayList<Entry> lineJJEntries = new ArrayList<>();

        for (int i = 0, j = 0; i < mData.size(); i++, j++) {
            HisData t = mData.get(j);

            if (t == null) {
                lineCJEntries.add(new Entry(Float.NaN, i));
                lineJJEntries.add(new Entry(Float.NaN, i));
                continue;
            }
            lineCJEntries.add(new Entry(i, (float) mData.get(i).getClose()));
            lineJJEntries.add(new Entry(i, (float) mData.get(i).getAvePrice()));

        }

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(setLine(0, lineCJEntries));
        sets.add(setLine(1, lineJJEntries));
        /*注老版本LineData参数可以为空，最新版本会报错，修改进入ChartData加入if判断*/
        LineData lineData = new LineData(sets);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(lineData);
        combinedChartX.setData(combinedData);

    }

    @android.support.annotation.NonNull
    private LineDataSet setLine(int type, ArrayList<Entry> lineEntries) {
        LineDataSet lineDataSetMa = new LineDataSet(lineEntries, "ma" + type);
        lineDataSetMa.setDrawValues(false);
        if (type == 0) {
//            lineDataSetMa.setDrawFilled(true);
            lineDataSetMa.setAxisDependency(YAxis.AxisDependency.LEFT);
            lineDataSetMa.setColor(getResources().getColor(R.color.minute_blue));
        } else if (type == 1) {
            lineDataSetMa.setAxisDependency(YAxis.AxisDependency.RIGHT);
            lineDataSetMa.setColor(getResources().getColor(R.color.minute_yellow));
        } else {
            lineDataSetMa.setAxisDependency(YAxis.AxisDependency.RIGHT);
            lineDataSetMa.setColor(getResources().getColor(R.color.transparent));
        }
        lineDataSetMa.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSetMa.setLineWidth(1f);

        lineDataSetMa.setDrawCircles(false);
        lineDataSetMa.setAxisDependency(YAxis.AxisDependency.LEFT);

        return lineDataSetMa;
    }


    private void initChartVolumeData(CombinedChart combinedChartX) {

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0, j = 0; i < mData.size(); i++, j++) {
            HisData t = mData.get(j);

            if (t == null) {
                barEntries.add(new BarEntry(Float.NaN, i));
                continue;
            }
            barEntries.add(new BarEntry(i, t.getVol()));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "成交量");
//        barDataSet.setBarSpacePercent(20); //bar空隙，可以控制树状图的大小，空隙越大，树状图越窄
        barDataSet.setDrawValues(false);//是否在线上绘制数值
        barDataSet.setColor(getResources().getColor(R.color.increasing_color));//设置树状图颜色
        List<Integer> list = new ArrayList<>();
        list.add(getResources().getColor(R.color.increasing_color));
        list.add(getResources().getColor(R.color.decreasing_color));
        barDataSet.setColors(list);//可以给树状图设置多个颜色，判断条件在BarChartRenderer 类的140行以下修改了判断条件
        barDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);//设置这些值对应哪条轴
        BarData barData = new BarData(barDataSet);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(barData);
        combinedChartX.setData(combinedData);

        setOffset();
        mChartPrice.invalidate();
        combinedChartX.invalidate();
    }

    /*设置量表对齐*/
    private void setOffset() {
        float lineLeft = mChartPrice.getViewPortHandler().offsetLeft();
        float barLeft = mChartVolume.getViewPortHandler().offsetLeft();
        float lineRight = mChartPrice.getViewPortHandler().offsetRight();
        float barRight = mChartVolume.getViewPortHandler().offsetRight();
        float barBottom = mChartVolume.getViewPortHandler().offsetBottom();
        float offsetLeft, offsetRight;
        float transLeft = 0, transRight = 0;
        if (barLeft < lineLeft) {
            //offsetLeft = Utils.convertPixelsToDp(lineLeft - barLeft);
            // barChart.setExtraLeftOffset(offsetLeft);
            transLeft = lineLeft;

        } else {
            offsetLeft = Utils.convertPixelsToDp(barLeft - lineLeft);
            mChartPrice.setExtraLeftOffset(offsetLeft);
            transLeft = barLeft;
        }

        if (barRight < lineRight) {
            //offsetRight = Utils.convertPixelsToDp(lineRight);
            //barChart.setExtraRightOffset(offsetRight);
            transRight = lineRight;
        } else {
            offsetRight = Utils.convertPixelsToDp(barRight);
            mChartPrice.setExtraRightOffset(offsetRight);
            transRight = barRight;
        }
        mChartVolume.setViewPortOffsets(transLeft, 5, transRight, barBottom);
    }
}
