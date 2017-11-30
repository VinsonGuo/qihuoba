package com.yjjr.yjfutures.ui.found;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.widget.HeaderView;

public class NoticeActivity extends BaseActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, NoticeActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_common_with_fragment);
        HeaderView headerView = (HeaderView) findViewById(R.id.header_view);
        headerView.setMainTitle("重要通知");
        headerView.bindActivity(mContext);
        NoticeListFragment fragment = new NoticeListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();
        fragment.setUserVisibleHint(true);
    }
}
