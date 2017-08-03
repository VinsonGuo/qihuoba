package com.yjjr.yjfutures.ui.found;

import android.support.annotation.Nullable;
import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.biz.Notice;
import com.yjjr.yjfutures.utils.StringUtils;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

/**
 * Created by dell on 2017/8/2.
 */

public class NoticeAdapter extends BaseQuickAdapter<Notice, BaseViewHolder> {
    public NoticeAdapter(@Nullable List<Notice> data) {
        super(R.layout.item_notice_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Notice item) {
        helper.setText(R.id.tv_name, "·通知：" + item.getTitle())
                .setText(R.id.tv_content, Html.fromHtml(item.getContent()));
    }
}
