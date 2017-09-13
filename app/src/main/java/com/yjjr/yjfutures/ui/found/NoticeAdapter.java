package com.yjjr.yjfutures.ui.found;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.biz.Notice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by dell on 2017/8/2.
 */

public class NoticeAdapter extends BaseQuickAdapter<Notice, BaseViewHolder> {


    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-mm");

    public NoticeAdapter(@Nullable List<Notice> data) {
        super(R.layout.item_notice_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Notice item) {
        helper.setText(R.id.tv_name, "·通知：" + item.getTitle())
                .setText(R.id.tv_content, item.getSummary())
                .setText(R.id.tv_date, dateFormat.format(item.getDate()))
                .setText(R.id.tv_symbol, item.getRightTitle());
    }
}
