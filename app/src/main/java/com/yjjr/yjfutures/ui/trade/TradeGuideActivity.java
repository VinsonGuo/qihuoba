package com.yjjr.yjfutures.ui.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yjjr.yjfutures.BuildConfig;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.store.ConfigSharePrefernce;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.utils.SystemBarHelper;

public class TradeGuideActivity extends BaseActivity {

    private int index = 0;

    private int[] mRes = {
            R.drawable.trade_guide1,
            R.drawable.trade_guide2,
            R.drawable.trade_guide3,
            R.drawable.trade_guide4,
            R.drawable.trade_guide5,
            R.drawable.trade_guide6,
            R.drawable.trade_guide7,
            R.drawable.trade_guide8,
            R.drawable.trade_guide9,
            R.drawable.trade_guide10,
            R.drawable.trade_guide11
    };


    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, TradeGuideActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigSharePrefernce.setVersionCode(mContext, BuildConfig.VERSION_CODE);
        final ImageView iv = new ImageView(mContext);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        iv.setImageResource(mRes[0]);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < mRes.length - 1) {
                    iv.setImageResource(mRes[++index]);
                } else {
                    finish();
                }
            }
        });
        resetStatusBar();
        setContentView(iv);
        SystemBarHelper.immersiveStatusBar(mContext, 0);
    }

}
