/*
 * Copyright 2011 Azwan Adli Abdullah
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yjjr.yjfutures.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.widget.TextView;

import com.yjjr.yjfutures.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class StringUtils.
 */
public class StringUtils {


    private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\._%\\-\\+]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+");

    public static String parseSpan(String s) {
        String replaceSpan = "<span class=\"highlight\">";
        s = s.replaceAll(replaceSpan, "<font color='#0083f2'>").
                replaceAll("</span>", "</font>");
        return s;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static String floatToPercent(float value) {
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(2);
        percentFormat.setMinimumFractionDigits(2);
        String result = percentFormat.format(value);
        return result;
    }

    public static String doubleToPercent(double value) {
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(2);
        percentFormat.setMinimumFractionDigits(2);
        String result = percentFormat.format(value);
        return result;
    }

    public static String doubleFormat2Decimal(double value) {
        DecimalFormat df = new DecimalFormat("######0.00");
        String result = df.format(value);
        return result;
    }

    public static String commaFormatDoubleValue(double value) {
        DecimalFormat commaFormat = new DecimalFormat("#,##0.00");
        String result = commaFormat.format(value);
        return result;
    }

    public static String formatDoubleValue(double value) {
        if (value == 0) {
            return "0";
        } else {
            try {
                return "$" + String.valueOf(DoubleUtil.format2Decimal(value));
            } catch (Exception e) {
                return "0";
            }
        }
    }

    /**
     * Checks if is blank.
     *
     * @param val the val
     * @return true, if is blank
     */
    public static boolean isBlank(String val) {
        return val == null || val.trim().isEmpty();
    }

    /**
     * Do teaser.
     *
     * @param text the text
     * @return the string
     */
    public static String doTeaser(String text) {
        if (isBlank(text)) {
            return "";
        }

        int indexNewLine = text.indexOf("\n");
        int indexDot = text.indexOf(". ");

        if (indexDot != -1 && indexNewLine != -1) {
            if (indexDot > indexNewLine) {
                text = text.substring(0, indexNewLine);
            } else {
                text = text.substring(0, indexDot + 1);
            }
        } else if (indexDot != -1) {
            text = text.substring(0, indexDot + 1);
        } else if (indexNewLine != -1) {
            text = text.substring(0, indexNewLine);
        }

        return text;
    }

    public static String addMask(String s, int leftSaveLength, int rightSaveLength) {
        try {
            if (isBlank(s)) return "";
            int saveLength = leftSaveLength + rightSaveLength;
            int originLength = s.length();
            if (originLength <= saveLength) return s;
            String leftString = s.substring(0, leftSaveLength);
            String rightString = s.substring(originLength - rightSaveLength, originLength);
            int maskLength = originLength - saveLength;
            String midString = "";
            for (int i = 0; i < maskLength; i++) {
                midString += "*";
            }
            String resultString = leftString + midString + rightString;
            return resultString;
        } catch (Exception e) {
            return "";
        }

    }

    public static boolean validatText(String s, int minLength, int maxLength) {
        boolean result = false;
        if (s.length() >= minLength && s.length() <= maxLength) {
            result = true;
        }
        return result;
    }

    //判断字符串是否是整形数据
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }


    /**
     * 得到在线交易数值的String
     * 小数点后两位字体变大
     */
    @NonNull
    public static SpannableString getOnlineTxString(String data) {
        AbsoluteSizeSpan asp = new AbsoluteSizeSpan(20, true);
        SpannableString ssb = new SpannableString(data);
        //如果数据没有.就捕获这个异常直接将字符串返回
        try {
            int index = data.lastIndexOf(".");
            if (index > 0 && ssb.length() >= 4) {
                String[] split = data.split("\\.");
                //如果小数点后大于等于两位，将小数点后点位变大
                if (split.length == 2 && split[split.length - 1].length() > 2) {
                    ssb.setSpan(asp, ssb.length() - 3, ssb.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    //如果小数点后正好两位，把这两位放大
                } else if (split.length == 2 && split[split.length - 1].length() == 2) {
//                    ssb = new SpannableString(data + "0");
                    ssb.setSpan(asp, ssb.length() - 4, ssb.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    //如果小数点后只有一位，将这一个数字变成两位
                } else if (split.length == 2 && split[split.length - 1].length() >= 1) {
                    ssb.setSpan(asp, ssb.length() - 4, ssb.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else {
                ssb = new SpannableString(data);
                int end = data.length() - 1;
                if (end >= 3) {
                    int start = end - 2;
                    ssb.setSpan(asp, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            return ssb;
        } catch (Exception e) {
            LogUtils.e(e.toString());
            return new SpannableString(data);
        }
    }

    /**
     * 给在线交易的TextView设置指定的样式
     */
    public static void setOnlineTxTextStyleRight(TextView tv, String content, double change) {
        if (TextUtils.isEmpty(content)) return;
        String text = tv.getContext().getString(R.string.bearish);
        if (change > 0) {
            tv.setText(TextUtils.concat(text, content));
            tv.setBackgroundResource(R.drawable.shape_online_tx_green);
        } else if (change < 0) {
            tv.setText(TextUtils.concat(text, content));
            tv.setBackgroundResource(R.drawable.shape_online_tx_red);
        } else {
            tv.setText(TextUtils.concat(text, content));
//            tv.setBackgroundResource(R.drawable.shape_online_tx_gray);
            tv.setBackgroundResource(R.drawable.shape_online_tx_green);
        }
    }

    public static void setOnlineTxTextStyleLeft(TextView tv, String content, double change) {
        if (TextUtils.isEmpty(content)) return;
        String text = tv.getContext().getString(R.string.bullish);
        if (change > 0) {
            tv.setText(TextUtils.concat(text, content));
            tv.setBackgroundResource(R.drawable.shape_online_tx_green);
        } else if (change < 0) {
            tv.setText(TextUtils.concat(text, content));
            tv.setBackgroundResource(R.drawable.shape_online_tx_red);
        } else {
            tv.setText(TextUtils.concat(text, content));
//            tv.setBackgroundResource(R.drawable.shape_online_tx_gray);
            tv.setBackgroundResource(R.drawable.shape_online_tx_green);
        }
    }


    public static void setOnlineTxArrow(TextView tv, int change) {
        if (change > 0) {
            tv.setText("↑");
        } else if (change < 0) {
            tv.setText("↓");
        } else {
            tv.setText("\u00A0\u00A0\u00A0\u00A0");
        }
    }

    /**
     * 给TextView设置点差
     */
    /*public static void setDiancha(TextView tv, MT4Symbol symbol) {
        String type = symbol.getSymbol();
        double buy = Double.valueOf(symbol.getAsk());
        double sell = Double.valueOf(symbol.getBid());
        double value = 0;
        if (type.equals("XAU/USD")) {
            value = (buy - sell) * 100;
        } else if ("UK100,USDOLLAR,GER30,FRA40,US30,".contains(type + ",")) {
            value = (buy - sell) * 1;
        } else {
            value = (buy - sell) * Math.pow(10, symbol.getDigits() - 1);
        }
        tv.setText(String.format(Locale.getDefault(), "%.1f", value));
    }*/

    /**
     * 根据精度得到一个小数的字符串
     *
     * @param num    double小数
     * @param digits 精度
     */
    public static String getStringByDigits(double num, int digits) {
        if (digits == 0) {
            return (int) num + "";
        } else {
            NumberFormat instance = DecimalFormat.getInstance();
            instance.setMinimumFractionDigits(digits);
            instance.setMaximumFractionDigits(digits);
            return instance.format(num).replace(",", "");
        }
    }

    public static String getStringByDigits(String num, int digits) {
        return getStringByDigits(Double.parseDouble(num), digits);
    }

    /**
     * 设置RadioButton的样式，样式要根据数字显示颜色和箭头
     */
  /*  public static void setRadioButtonStyle(Context ctx, Symbol symbol, TextView mTvLeft, TextView mTvRight, TextView mTvCenter, RadioButton mLeftBtn, RadioButton mRightBtn) {
        StringUtils.setOnlineTxTextStyle(mTvLeft, symbol.getBid(), symbol.getBidChange());
        StringUtils.setOnlineTxTextStyle(mTvRight, symbol.getAsk(), symbol.getAskChange());
        StringUtils.setDiancha(mTvCenter, symbol);
        mTvRight.setText(TextUtils.concat(mTvRight.getText(), "\n", ctx.getString(R.string.buy_in)));
        mTvLeft.setText(TextUtils.concat(mTvLeft.getText(), "\n", ctx.getString(R.string.sold_out)));

        setRadioButtonStyle(ctx, symbol, mLeftBtn, mRightBtn);
    }*/

    /**
     * 设置RadioButton的样式，样式要根据数字显示颜色和箭头
     */
    /*public static void setRadioButtonStyle(Context ctx, Symbol symbol, RadioButton mLeftBtn, RadioButton mRightBtn) {
        if (symbol.getAskChange() > 0) {
            String data = ctx.getString(R.string.buy_in) + "\n" + symbol.getAsk() + ctx.getString(R.string.online_transaction_arrow_top);
            SpannableString ssb = new SpannableString(data);
            ssb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.color_00b876)), data.indexOf("\n"), data.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mRightBtn.setText(ssb);
        } else if (symbol.getAskChange() < 0) {
            String data = ctx.getString(R.string.buy_in) + "\n" + symbol.getAsk() + ctx.getString(R.string.online_transaction_arrow_down);
            SpannableString ssb = new SpannableString(data);
            ssb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.color_f56262)), data.indexOf("\n"), data.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mRightBtn.setText(ssb);
        }
        if (symbol.getBidChange() > 0) {
            String data = ctx.getString(R.string.sold_out) + "\n" + symbol.getBid() + ctx.getString(R.string.online_transaction_arrow_top);
            SpannableString ssb = new SpannableString(data);
            ssb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.color_00b876)), data.indexOf("\n"), data.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mLeftBtn.setText(ssb);
        } else if (symbol.getBidChange() < 0) {
            String data = ctx.getString(R.string.sold_out) + "\n" + symbol.getBid() + ctx.getString(R.string.online_transaction_arrow_down);
            SpannableString ssb = new SpannableString(data);
            ssb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.color_f56262)), data.indexOf("\n"), data.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mLeftBtn.setText(ssb);
        }
    }*/

    /**
     * 设置RadioButton的样式，样式要根据数字显示颜色和箭头
     */
   /* public static void setRadioButtonStyle(Context ctx, MT4Symbol symbol, TextView mTvLeft, TextView mTvRight, TextView mTvCenter, RadioButton mLeftBtn, RadioButton mRightBtn) {
        StringUtils.setOnlineTxTextStyle(mTvLeft, StringUtils.getStringByDigits(symbol.getBid(), symbol.getDigits()), symbol.getBidChange());
        StringUtils.setOnlineTxTextStyle(mTvRight, StringUtils.getStringByDigits(symbol.getAsk(), symbol.getDigits()), symbol.getAskChange());
        StringUtils.setDiancha(mTvCenter, symbol);
        mTvRight.setText(TextUtils.concat(mTvRight.getText(), "\n", ctx.getString(R.string.buy_in)));
        mTvLeft.setText(TextUtils.concat(mTvLeft.getText(), "\n", ctx.getString(R.string.sold_out)));

        setRadioButtonStyle(ctx, symbol, mLeftBtn, mRightBtn);
    }

    */

    /**
     * 设置RadioButton的样式，样式要根据数字显示颜色和箭头
     *//*
    public static void setRadioButtonStyle(Context ctx, MT4Symbol symbol, RadioButton mLeftBtn, RadioButton mRightBtn) {
        if (symbol.getAskChange() > 0) {
            String data = ctx.getString(R.string.buy_in) + "\n" + StringUtils.getStringByDigits(symbol.getAsk(), symbol.getDigits()) + ctx.getString(R.string.online_transaction_arrow_top);
            SpannableString ssb = new SpannableString(data);
            ssb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.main_color)), data.indexOf("\n"), data.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mRightBtn.setText(ssb);
        } else if (symbol.getAskChange() < 0) {
            String data = ctx.getString(R.string.buy_in) + "\n" + StringUtils.getStringByDigits(symbol.getAsk(), symbol.getDigits()) + ctx.getString(R.string.online_transaction_arrow_down);
            SpannableString ssb = new SpannableString(data);
            ssb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.color_f56262)), data.indexOf("\n"), data.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mRightBtn.setText(ssb);
        }
        if (symbol.getBidChange() > 0) {
            String data = ctx.getString(R.string.sold_out) + "\n" + StringUtils.getStringByDigits(symbol.getBid(), symbol.getDigits()) + ctx.getString(R.string.online_transaction_arrow_top);
            SpannableString ssb = new SpannableString(data);
            ssb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.main_color)), data.indexOf("\n"), data.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mLeftBtn.setText(ssb);
        } else if (symbol.getBidChange() < 0) {
            String data = ctx.getString(R.string.sold_out) + "\n" + StringUtils.getStringByDigits(symbol.getBid(), symbol.getDigits()) + ctx.getString(R.string.online_transaction_arrow_down);
            SpannableString ssb = new SpannableString(data);
            ssb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.color_f56262)), data.indexOf("\n"), data.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mLeftBtn.setText(ssb);
        }
    }*/
    public static CharSequence getMineSpannableStringWithColor(Context mContext, @StringRes int res, double d) {
        return TextUtils.concat(
                mContext.getString(res),
                "\n",
                d >= 0 ?
                        SpannableUtil.getStringBySize(SpannableUtil.getStringByColor(mContext, "$" + DoubleUtil.format2Decimal(d), R.color.main_color), 1.6f) :
                        SpannableUtil.getStringBySize(SpannableUtil.getStringByColor(mContext, "$" + DoubleUtil.format2Decimal(d), R.color.main_color_red), 1.6f)
        );
    }

    public static CharSequence getMineSpannableString(Context mContext, @StringRes int res, int d) {
        return TextUtils.concat(
                mContext.getString(res),
                "\n",
                SpannableUtil.getStringBySize(SpannableUtil.getStringByColor(mContext, d + mContext.getString(R.string.pen), R.color.black), 1.6f)
        );
    }

    public static String encryptBankCode(String bankCode) {
        if (bankCode == null || bankCode.length() < 4) {
            return bankCode;
        }
        StringBuilder sb = new StringBuilder(20);
        String lastCode = bankCode.substring(bankCode.length() - 4, bankCode.length());
        for (int i = 0; i < bankCode.length() - 4; i++) {
            sb.append("*");
            if ((i + 1) % 4 == 0) {
                sb.append(" ");
            }
        }
        sb.append(lastCode);
        return sb.toString();
    }

    public static String encodePassword(String password) {
        return sha256(sha256(password) + "wzhdxtx");
    }

    private static String sha256(final String strText) {
        // 返回值
        String strResult = null;

        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {
            try {
                // SHA 加密开始
                // 创建加密对象 并傳入加密類型
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                // 得到 byte 類型结果
                byte byteBuffer[] = messageDigest.digest(strText.getBytes("UTF-8"));

                // 將 byte 轉換爲 string
                StringBuffer strHexString = new StringBuffer();
                // 遍歷 byte buffer
                for (int i = 0; i < byteBuffer.length; i++) {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]).toUpperCase();
//                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return strResult;
    }

}