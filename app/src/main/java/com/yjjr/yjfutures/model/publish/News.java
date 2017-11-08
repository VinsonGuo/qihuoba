package com.yjjr.yjfutures.model.publish;

/**
 * Created by dell on 2017/11/7.
 */

public class News {

    /**
     * date : 2017-11-07 07:28:46
     * id : 15764
     * thumb : http://res.junchengtz.com/jin10/pic/0e/f5253b175940153efd34310faff32423.jpg
     * title : 金银油市突生剧变！黄金怒破1280大关 布油涨势惊人续刷两年新高
     */

    private String date;
    private int id;
    private String thumb;
    private String title;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "News{" +
                "date='" + date + '\'' +
                ", id=" + id +
                ", thumb='" + thumb + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
