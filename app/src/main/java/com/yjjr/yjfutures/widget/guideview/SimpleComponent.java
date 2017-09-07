package com.yjjr.yjfutures.widget.guideview;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yjjr.yjfutures.R;

/**
 * Created by dell on 2017/9/6.
 */

public class SimpleComponent implements Component {
    @Override public View getView(LayoutInflater inflater) {
        LinearLayout ll = new LinearLayout(inflater.getContext());
        LinearLayout.LayoutParams param =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(param);
        TextView textView = new TextView(inflater.getContext());
        textView.setText("模拟交易");
        textView.setTextColor(inflater.getContext().getResources().getColor(R.color.white));
        textView.setTextSize(20);
        ll.removeAllViews();
        ll.addView(textView);
        return ll;
    }

    @Override public int getAnchor() {
        return Component.ANCHOR_BOTTOM;
    }

    @Override public int getFitPosition() {
        return Component.FIT_CENTER;
    }

    @Override public int getXOffset() {
        return 0;
    }

    @Override public int getYOffset() {
        return 20;
    }
}
