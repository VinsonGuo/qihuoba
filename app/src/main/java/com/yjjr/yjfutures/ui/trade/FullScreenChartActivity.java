package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseActivity;

import java.text.DecimalFormat;

public class FullScreenChartActivity extends BaseActivity {

    private LineChart lineChart;
    private BarChart barChart;
    private XAxis xAxisLine;
    private YAxis axisLeftLine;
    private YAxis axisRightLine;
    private XAxis xAxisBar;
    private YAxis axisLeftBar;
    private YAxis axisRightBar;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, FullScreenChartActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_chart);
        lineChart = (LineChart) findViewById(R.id.line_chart);
        barChart = (BarChart) findViewById(R.id.bar_chart);
    }

    private void initChart() {
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
