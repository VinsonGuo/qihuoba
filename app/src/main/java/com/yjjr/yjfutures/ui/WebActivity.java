package com.yjjr.yjfutures.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;
import com.umeng.socialize.UMShareAPI;
import com.yjjr.yjfutures.BuildConfig;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.contants.Constants;
import com.yjjr.yjfutures.utils.ActivityTools;
import com.yjjr.yjfutures.utils.DialogUtils;
import com.yjjr.yjfutures.utils.ShareUtils;
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
        final int type = getIntent().getIntExtra(Constants.CONTENT_PARAMETER_2, 0);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.root_view);
        final HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        headerView.bindActivity(mContext);
        headerView.setMainTitle(url);
        if (type == TYPE_CSCENTER) { //客服中心
            headerView.getSubTitle().setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_cscenter_phone), null, null, null);
        }else if(type == TYPE_SHARE) { //推广赚钱
            headerView.getSubTitle().setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_share), null, null, null);
        }
        headerView.setOperateClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == TYPE_CSCENTER) {
                    DialogUtils.createCustomServiceDialog(mContext).show();
                } else if (type == TYPE_SHARE) {
//                    ActivityTools.share(mContext, getString(R.string.app_name), url);
                    ShareUtils.share(mContext,url,"title","desc");
                }
            }
        });

        headerView.setBackImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mAgentWeb = AgentWeb.with(mContext)//传入Activity
                .setAgentWebParent(linearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
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
