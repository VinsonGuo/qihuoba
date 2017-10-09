package com.yjjr.yjfutures.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;

import com.yjjr.yjfutures.R;


/**
 * Created by houhualiang on 2015/7/2.
 */
public class SpannableUtil {

    public static SpannableString imageResToSpannableString(Context context, int imageResId, String name) {
        try {
            // 根据随机产生的1至9的整数从R.drawable类中获得相应资源ID（静态变量）的Field对象
//            Field field = R.drawable.class.getDeclaredField("face" + randomId);
            // 获得资源ID的值，也就是静态变量的值
//            int resourceId = Integer.parseInt(field.get(null).toString());
            // 根据资源ID获得资源图像的Bitmap对象
//            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imageResId);
            Drawable drawable = context.getResources().getDrawable(imageResId);
            drawable.setBounds(0, 0, 60, 60);//这里设置图片的大小
            ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);

            // 根据Bitmap对象创建ImageSpan对象
//            ImageSpan imageSpan = new ImageSpan(context, bitmap);
            // 创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
            SpannableString spannableString = new SpannableString(name);
            // 用ImageSpan对象替换face
            spannableString.setSpan(imageSpan, 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // 将随机获得的图像追加到EditText控件的最后
//            edittext.append(spannableString);
            return spannableString;
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return null;
    }


    /*public static void deleteEmoji(KeyboardControlEditText mEditText) {
        int selectionStart = mEditText.getSelectionStart();// 获取光标的位置
        if (selectionStart > 0) {
            String body = mEditText.getText().toString();
            if (!StringUtils.isBlank(body)) {
                String tempStr = body.substring(0, selectionStart);
                if (tempStr.endsWith("]")) {
                    int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                    if (i != -1) {
                        CharSequence cs = tempStr
                                .subSequence(i, selectionStart);
                        if (cs.toString().startsWith("[")) {// 判断是否是一个表情
                            mEditText.getEditableText().delete(i, selectionStart);
                            return;
                        }
                    }
                    mEditText.getEditableText().delete(tempStr.length() - 1,
                            selectionStart);
                } else {
                    mEditText.getEditableText().delete(selectionStart - 1, selectionStart);
                }
            }
        }
    }*/

    public static SpannableString getOnlinePriceString(Context ctx, String content, double change) {
        if (content == null) {
            content = "";
        }
        SpannableString ssb = new SpannableString(content);
        if (change > 0) {
            ssb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.main_color_red)), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } /*else if (change < 0) {
            ssb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.main_color_green)), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }*/ else {
            ssb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.main_color_green)), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ssb;
    }

    public static SpannableString getStringByColor(Context ctx, CharSequence content, @ColorRes int color) {
        if (content == null) {
            content = "";
        }
        SpannableString ssb = new SpannableString(content);
        ssb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ctx, color)), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    public static SpannableString getStringByDrawable(Context ctx, @DrawableRes int id) {
        Drawable drawable = ctx.getResources().getDrawable(id);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //需要处理的文本，[smile]是需要被替代的文本
        SpannableString spannable = new SpannableString("[smile]");
        //要让图片替代指定的文字就要用ImageSpan
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        //开始替换，注意第2和第3个参数表示从哪里开始替换到哪里替换结束（start和end）
//最后一个参数类似数学中的集合,[5,12)表示从5到12，包括5但不包括12
        spannable.setSpan(span, 0, "[smile]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static SpannableString getStringBySize(CharSequence content, float size) {
        if (content == null) {
            content = "";
        }
        SpannableString ssb = new SpannableString(content);
        ssb.setSpan(new RelativeSizeSpan(size), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }
}
