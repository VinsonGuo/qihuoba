package com.yjjr.yjfutures.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.TextView;

import com.yjjr.yjfutures.R;


/**
 * Created by hou on 2015/9/9.
 */
public class TextViewUtil {

    /**
     * 以某值为界，大于此值显示为绿色，小于显示为红色
     *
     * @param context
     * @param textView
     * @param value
     * @param segmentation
     * @param lastString
     */
   /* public static void setColorWithSegmentation(Context context, TradeMsgItemView textView, double value, double segmentation, String lastString) {
        if (value < segmentation) {
            textView.setValueColor(context.getResources().getColor(R.color.main_color_orange));
            textView.setValue(StringUtils.doubleFormat2Decimal(value) + lastString);
        } else {
            textView.setValueColor(context.getResources().getColor(R.color.color_00b876));
            textView.setValue(StringUtils.doubleFormat2Decimal(value) + lastString);
        }
    }

    public static void setColorWithSegmentation(Context context, TradeMsgItemView textView, double value, double segmentation) {
        setColorWithSegmentation(context, textView, value, segmentation, "");
    }

    public static void setColor(Context context, TradeMsgItemView textView, double value, String lastString) {
        setColorWithSegmentation(context, textView, value, 0, lastString);
    }

    public static void setColor(Context context, TradeMsgItemView textView, double value) {
        setColorWithSegmentation(context, textView, value, 0, "");
    }*/

    public static void setColorWithDollar(Context context, TextView textView, double value) {
        setColorWithDollar(context, textView, value, true);
    }

    public static void setColorWithDollar(Context context, TextView textView, double value, boolean changeColor) {
        setColorWithDollar(context, textView, value, changeColor, true);
    }

    /**
     * 格式话TextView填充美元，大于1000结尾为K
     *
     * @param context
     * @param textView
     * @param value
     * @param changeColor
     */
    public static void setColorWithDollar(Context context, TextView textView, double value, boolean changeColor, boolean showWithK) {
        if (value < 0) {
            if (value < -1000 && showWithK) {
                String valueFormat = "$" + DoubleUtil.format2Decimal(value / 1000) + "K";
                textView.setText(valueFormat);
            } else {
                textView.setText("$" + StringUtils.commaFormatDoubleValue(value));
            }
            if (changeColor)
                textView.setTextColor(context.getResources().getColor(R.color.main_color_red));
        } else {
            if (value > 1000 && showWithK) {
                String valueFormat = "$" + DoubleUtil.format2Decimal(value / 1000) + "K";
//                LogUtils.e("valueFormat " + valueFormat);
                textView.setText(valueFormat);
            } else {
                textView.setText("$" + StringUtils.commaFormatDoubleValue(value));
            }
            if (changeColor)
                textView.setTextColor(context.getResources().getColor(R.color.main_color));
        }
    }

    public static void setColorWithNoFormat(Context context, TextView textView, String value) {
        textView.setText(value);
        if (Double.valueOf(value) < 0) {
            textView.setTextColor(context.getResources().getColor(R.color.main_color));
        } else {
            textView.setTextColor(context.getResources().getColor(R.color.color_00b876));
        }
    }

    public static void setColorWithNoFormat(Context context, TextView textView, double value,String suffix) {
        textView.setText(value + suffix);
        if (value < 0) {
            textView.setTextColor(context.getResources().getColor(R.color.main_color));
        } else {
            textView.setTextColor(context.getResources().getColor(R.color.color_00b876));
        }
    }

    public static void setColorWithRound(Context context, TextView textView, double value) {
        textView.setText(value + "");
        value = Math.round(value);
        if (value < 0) {
            textView.setTextColor(context.getResources().getColor(R.color.main_color));
        } else {
            textView.setTextColor(context.getResources().getColor(R.color.color_00b876));
        }
    }

    /**
     * 设置数值，大于1000，结尾为k
     *
     * @param context
     * @param textView
     * @param value
     * @param changeColor
     */
    public static void setColorTextViewWithNum(Context context, TextView textView, double value, boolean changeColor) {
        if (value < 0) {
            if (value < -1000) {
                String valueFormat = DoubleUtil.format2Decimal(value / 1000) + "K";
                textView.setText(valueFormat);
            } else {
                textView.setText(StringUtils.commaFormatDoubleValue(value));
            }
            if (changeColor)
                textView.setTextColor(context.getResources().getColor(R.color.main_color));
        } else {
            if (value > 1000) {
                String valueFormat = DoubleUtil.format2Decimal(value / 1000) + "K";
                textView.setText(valueFormat);
            } else {
                textView.setText(StringUtils.commaFormatDoubleValue(value));
            }
            if (changeColor)
                textView.setTextColor(context.getResources().getColor(R.color.color_00b876));
        }
    }

    public static void setColorWithDollar(Context context, TextView textView, String values) {
        double value = 0;
        try {
            value = Double.valueOf(value);
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        if (value < 0) {
            textView.setTextColor(context.getResources().getColor(R.color.main_color));
            textView.setText("$" + StringUtils.commaFormatDoubleValue(value));
        } else {
            textView.setTextColor(context.getResources().getColor(R.color.color_00b876));
            textView.setText("$" + StringUtils.commaFormatDoubleValue(value));
        }
    }

  /*  public static void setColorWithDollar(Context context, TradeMsgItemView textView, double value) {
//        if(value < 0) {
//            textView.setValueColor(context.getResources().getColor(R.color.main_color_orange));
//            textView.setValue("$" + StringUtils.commaFormatDoubleValue(value));
//        } else {
//            textView.setValueColor(context.getResources().getColor(R.color.color_00b876));
//            textView.setValue("$" + StringUtils.commaFormatDoubleValue(value));
//        }
        setColorWithDollar(context, textView.getValueTextView(), value);
    }*/

    public static void setColorWithPercent(Context context, TextView textView, double value) {
        if (value < 0) {
            textView.setTextColor(context.getResources().getColor(R.color.main_color));
            textView.setText(StringUtils.doubleToPercent(value));
        } else {
            textView.setTextColor(context.getResources().getColor(R.color.color_00b876));
            textView.setText(StringUtils.doubleToPercent(value));
        }
    }

    public static void setColorWithPercent(Context context, TextView textView, double value, boolean changeColor) {
        if (!changeColor) {
            textView.setText(StringUtils.doubleToPercent(value));
            return;
        }
        if (value < 0) {
            textView.setTextColor(context.getResources().getColor(R.color.main_color));
            textView.setText(StringUtils.doubleToPercent(value));
        } else {
            textView.setTextColor(context.getResources().getColor(R.color.color_00b876));
            textView.setText(StringUtils.doubleToPercent(value));
        }
    }

    public static void setValueWithDollar(Context context, TextView textView, double value) {
        setColorWithDollar(context, textView, value, false);
    }

    /*public static void setColorWithPercent(Context context, TradeMsgItemView textView, double value) {
        setColorWithPercent(context, textView, value, true);
    }

    public static void setColorWithPercent(Context context, TradeMsgItemView textView, double value, boolean changeColor) {
        if (value < 0) {
            if (changeColor)
                textView.setValueColor(context.getResources().getColor(R.color.main_color_orange));
            textView.setValue(StringUtils.doubleToPercent(value));
        } else {
            if (changeColor)
                textView.setValueColor(context.getResources().getColor(R.color.color_00b876));
            textView.setValue(StringUtils.doubleToPercent(value));
        }
    }
*/
    public static void copy(Context context, CharSequence content) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("kvb", content);
        clipboard.setPrimaryClip(clip);
        ToastUtils.show(context, R.string.copy_success);
    }
}
