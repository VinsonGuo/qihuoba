package com.yjjr.yjfutures.utils.imageloader;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;

/**
 * 图片加载的包装类
 * Created by guoziwei on 2016/11/22.
 */

public class ImageLoader {
    public static void load(Context context, String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) return;
        Glide.with(context)
                .load(Uri.parse(url))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void load(Context context, String url, ImageView imageView, RequestListener<? super Uri, GlideDrawable> listener) {
        if (TextUtils.isEmpty(url)) return;
        Glide.with(context)
                .load(Uri.parse(url))
                .listener(listener)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void loadFile(Context context, String imagePath, ImageView imageView) {
        Glide.with(context)
                .load(Uri.parse(imagePath.startsWith("file://") ? imagePath : "file://" + imagePath))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }
}
