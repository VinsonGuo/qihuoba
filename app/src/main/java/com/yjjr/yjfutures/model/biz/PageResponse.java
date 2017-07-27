package com.yjjr.yjfutures.model.biz;

import java.util.List;

/**
 * Created by dell on 2017/7/27.
 */

public class PageResponse<T> {
    private int total;
    private List<T> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageResponse{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }
}
