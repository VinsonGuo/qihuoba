package com.yjjr.yjfutures.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;
import com.umeng.socialize.UMShareAPI;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.ui.mine.ChatActivity;
import com.yjjr.yjfutures.utils.ActivityTools;
import com.yjjr.yjfutures.utils.DialogUtils;
import com.yjjr.yjfutures.utils.DisplayUtils;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.SpannableUtil;
import com.yjjr.yjfutures.widget.HeaderView;

public class WebActivity extends BaseActivity {

    public static final int TYPE_CSCENTER = 1;
    public static final int TYPE_SHARE = 2;

    private AgentWeb mAgentWeb;

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(Constants.CONTENT_PARAMETER, url);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String url, int type) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(Constants.CONTENT_PARAMETER, url);
        intent.putExtra(Constants.CONTENT_PARAMETER_2, type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        final String url = getIntent().getStringExtra(Constants.CONTENT_PARAMETER);
        LogUtils.d("WebView url %s", url);
        final int type = getIntent().getIntExtra(Constants.CONTENT_PARAMETER_2, 0);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.root_view);
        final HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        headerView.bindActivity(mContext);
        headerView.setMainTitle(url);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lp.weight = 1;
        mAgentWeb = AgentWeb.with(mContext)//传入Activity
                .setAgentWebParent(linearLayout, lp)
                .useDefaultIndicator()// 使用默认进度条
                .defaultProgressBarColor() // 使用默认进度条颜色
                .setReceivedTitleCallback(new ChromeClientCallbackManager.ReceivedTitleCallback() {
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        headerView.setMainTitle(title);
                    }
                }) //设置 Web 页面的 title 回调
                .createAgentWeb()//
                .ready()
                .go(url);
        if (type == TYPE_CSCENTER) { //客服中心
            headerView.getSubTitle().setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_cscenter_phone), null, null, null);
            TextView tv = new TextView(mContext);
            tv.setGravity(Gravity.CENTER);
            tv.setText(TextUtils.concat("没有解决问题？", SpannableUtil.getStringByColor(mContext, "点击提问", R.color.main_color)));
            int padding = DisplayUtils.dip2px(mContext, 10);
            tv.setPadding(padding, padding, padding, padding);
            tv.setTextColor(ContextCompat.getColor(mContext, R.color.main_text_color));
            linearLayout.addView(tv);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatActivity.startActivity(mContext);
                }
            });
        } else if (type == TYPE_SHARE) { //推广赚钱
            headerView.getSubTitle().setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_share), null, null, null);
        }
        headerView.setOperateClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == TYPE_CSCENTER) {
                    DialogUtils.createCustomServiceDialog(mContext).show();
                } else if (type == TYPE_SHARE) {
                    ActivityTools.share(mContext, getString(R.string.app_name), url);
//                    ShareUtils.share(mContext,url,"外盘期货原油黄金白银","美国顶级券商盈透证券，国内外盘期货领先者，原油黄金恒指德指，绝对实盘保证，自主研发系统。");
                }
            }
        });

        headerView.setBackImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (!mAgentWeb.back()) {
            super.onBackPressed();
        }
    }
}
