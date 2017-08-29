package com.yjjr.yjfutures.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.yjjr.yjfutures.R;

/**
 * Created by dell on 2017/8/29.
 */

public class ShareUtils {
    public static void share(final Activity activity) {
        UMWeb  web = new UMWeb("http://www.baidu.com");
        web.setTitle("This is music title");//标题
        web.setDescription("my description");//描述
        web.setThumb(new UMImage(activity, R.mipmap.ic_launcher_icon));
        final CustomShareListener listener = new CustomShareListener(activity);
        ShareAction mShareAction = new ShareAction(activity).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
                SHARE_MEDIA.SMS)
                .withMedia(web)
                .setCallback(listener);
        mShareAction.open();
    }

    private static class CustomShareListener implements UMShareListener {

        private final Context context;

        public CustomShareListener(Context context) {
            this.context = context;
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST

                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                LogUtils.d(platform + " 分享成功啦");
                ToastUtils.show(context, "分享成功啦");
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST

                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                LogUtils.e(platform + " 分享失败啦");
                ToastUtils.show(context, "分享失败啦");
                if (t != null) {
                    LogUtils.e(t);
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            LogUtils.d(platform + " 分享取消了");
            ToastUtils.show(context, "分享取消了");
        }
    }
}
