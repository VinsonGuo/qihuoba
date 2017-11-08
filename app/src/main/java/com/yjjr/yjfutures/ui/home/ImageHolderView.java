package com.yjjr.yjfutures.ui.home;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.yjjr.yjfutures.model.biz.Info;
import com.yjjr.yjfutures.ui.WebActivity;
import com.yjjr.yjfutures.utils.http.HttpConfig;
import com.yjjr.yjfutures.utils.imageloader.ImageLoader;

/**
 * Created by dell on 2017/11/6.
 */

public class ImageHolderView implements Holder<Info> {
    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public void UpdateUI(final Context context, final int position, final Info data) {
        ImageLoader.load(context, HttpConfig.BIZ_HOST + data.getName(), imageView);
        if (!TextUtils.isEmpty(data.getValue())) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebActivity.startActivity(context, data.getValue());
                }
            });
        }
    }
}
