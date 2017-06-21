package com.yjjr.yjfutures.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ProgressBar;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.utils.SystemBarHelper;
import com.yjjr.yjfutures.utils.imageloader.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class BigPhotoActivity extends BaseActivity {

    private ProgressBar mProgressBar;

    public static void startActivity(Context context, List<String> list) {
        Intent intent = new Intent(context, BigPhotoActivity.class);
        intent.putStringArrayListExtra(Constants.CONTENT_PARAMETER, new ArrayList<>(list));
        context.startActivity(intent);
    }

    public static void startActivity(Context context, ArrayList<String> list) {
        Intent intent = new Intent(context, BigPhotoActivity.class);
        intent.putStringArrayListExtra(Constants.CONTENT_PARAMETER, list);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String... url) {
        Intent intent = new Intent(context, BigPhotoActivity.class);
        intent.putStringArrayListExtra(Constants.CONTENT_PARAMETER, new ArrayList<>(Arrays.asList(url)));
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetStatusBar();
        setContentView(R.layout.activity_big_photo);
        SystemBarHelper.immersiveStatusBar(this, 0);
        final ArrayList<String> urls = getIntent().getStringArrayListExtra(Constants.CONTENT_PARAMETER);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new PhotoViewAdapter(this, urls));

    }

    private class PhotoViewAdapter extends PagerAdapter implements PhotoViewAttacher.OnPhotoTapListener {

        private Context context;
        private final ArrayList<String> urls;

        PhotoViewAdapter(Context context, ArrayList<String> urls) {
            this.context = context;
            this.urls = urls;
        }

        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String url = urls.get(position);
            PhotoView photoView = new PhotoView(context);
            if (URLUtil.isHttpsUrl(url) || URLUtil.isHttpUrl(url)) {
                ImageLoader.load(context, url, photoView, new RequestListener<Uri, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                });
            } else if (URLUtil.isFileUrl(url)) {
                ImageLoader.loadFile(context, url, photoView);
                mProgressBar.setVisibility(View.GONE);
            }
            final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
            attacher.setOnPhotoTapListener(this);
            container.addView(photoView,position);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            container.removeViewAt(position);
        }

        @Override
        public void onPhotoTap(View view, float x, float y) {
            if(context instanceof Activity){
                ((Activity) context).finish();
            }
        }

        @Override
        public void onOutsidePhotoTap() {
            if(context instanceof Activity){
                ((Activity) context).finish();
            }
        }
    }


}
