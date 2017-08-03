package com.yjjr.yjfutures.model.biz;

/**
 * Created by dell on 2017/8/2.
 */

public class Notice {

    /**
     * date : 1501571209000
     * id : 1
     * title : 重要通知001
     */

    private long date;
    private int id;
    private String title;
    private String content;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
