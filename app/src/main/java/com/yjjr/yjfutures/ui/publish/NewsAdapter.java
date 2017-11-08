package com.yjjr.yjfutures.ui.publish;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.publish.News;
import com.yjjr.yjfutures.utils.imageloader.ImageLoader;

import java.util.List;

/**
 * Created by dell on 2017/11/6.
 */

public class NewsAdapter extends BaseQuickAdapter<News, BaseViewHolder> {
    public NewsAdapter(@Nullable List<News> data) {
        super(R.layout.item_news, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, News item) {
        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_time, item.getDate());
        ImageLoader.load(mContext, item.getThumb(), (ImageView) helper.getView(R.id.iv_image));
    }
}
