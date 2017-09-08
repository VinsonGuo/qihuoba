package com.yjjr.yjfutures.widget.listener;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.Spanned;
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

public class EmojiInputFilter implements InputFilter {

    private int emojiSize = -1;
    private TextView tv;

    public EmojiInputFilter(TextView tv) {
        this.tv = tv;
    }

    private void clearSpan(Spannable spannable, int start, int end) {
        if (start == end) {
            return;
        }
        EmojiSpan[] oldSpans = spannable.getSpans(start, end, EmojiSpan.class);
        for (int i = 0; i < oldSpans.length; i++) {
            spannable.removeSpan(oldSpans[i]);
        }
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        emojiSize = emojiSize == -1 ? EmoticonsKeyboardUtils.getFontHeight(tv) : emojiSize;
        Editable editableText = tv.getEditableText();
        clearSpan(editableText, start, source.toString().length());
        Matcher m = EmojiDisplay.getMatcher(source.toString().substring(start, source.toString().length()));
        if (m != null) {
            while (m.find()) {
                String emojiHex = Integer.toHexString(Character.codePointAt(m.group(), 0));
                EmojiDisplay.emojiDisplay(tv.getContext(), editableText, emojiHex, emojiSize, start + m.start(), start + m.end());
            }
        }
        return editableText;
    }
}