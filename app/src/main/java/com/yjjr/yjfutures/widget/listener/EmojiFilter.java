package com.yjjr.yjfutures.widget.listener;

import android.text.Spannable;
import android.text.SpannableString;
import android.widget.EditText;
import android.widget.TextView;

import com.sj.emoji.EmojiDisplay;
import com.sj.emoji.EmojiSpan;

import java.util.regex.Matcher;

import sj.keyboard.interfaces.EmoticonFilter;
import sj.keyboard.utils.EmoticonsKeyboardUtils;

/**
 * Created by dell on 2017/9/8.
 */

public class EmojiFilter extends EmoticonFilter {

    private static int emojiSize = -1;

    @Override
    public void filter(EditText editText, CharSequence text, int start, int lengthBefore, int lengthAfter) {
        emojiSize = emojiSize == -1 ? EmoticonsKeyboardUtils.getFontHeight(editText) : emojiSize;
        clearSpan(editText.getText(), start, text.toString().length());
        Matcher m = EmojiDisplay.getMatcher(text.toString().substring(start, text.toString().length()));
        if (m != null) {
            while (m.find()) {
                String emojiHex = Integer.toHexString(Character.codePointAt(m.group(), 0));
                EmojiDisplay.emojiDisplay(editText.getContext(), editText.getText(), emojiHex, emojiSize, start + m.start(), start + m.end());
            }
        }
    }

    public static void filter(TextView editText, CharSequence text, int start) {
        emojiSize = emojiSize == -1 ? EmoticonsKeyboardUtils.getFontHeight(editText) : emojiSize;
        SpannableString spannable = new SpannableString(editText.getText());
        clearSpan(spannable, start, text.toString().length());
        Matcher m = EmojiDisplay.getMatcher(text.toString().substring(start, text.toString().length()));
        if (m != null) {
            while (m.find()) {
                String emojiHex = Integer.toHexString(Character.codePointAt(m.group(), 0));
                EmojiDisplay.emojiDisplay(editText.getContext(), spannable, emojiHex, emojiSize, start + m.start(), start + m.end());
            }
        }
        editText.setText(spannable);
    }

    private static void clearSpan(Spannable spannable, int start, int end) {
        if (spannable == null || start == end) {
            return;
        }
        EmojiSpan[] oldSpans = spannable.getSpans(start, end, EmojiSpan.class);
        if(oldSpans == null) {
            return;
        }
        for (int i = 0; i < oldSpans.length; i++) {
            spannable.removeSpan(oldSpans[i]);
        }
    }
}