package com.yjjr.yjfutures.model.biz;

/**
 * Created by dell on 2017/7/25.
 */

public class Info {

    /**
     * name : 400-005-800
     * statue : Y
     * description : 400客服电话
     * id : 101
     * nameEn : 400-005-800
     * type : SERVICE_INFO
     */

    private String name;
    private String statue;
    private String description;
    private int id;
    private String nameEn;
    private String type;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Info{" +
                "name='" + name + '\'' +
                ", statue='" + statue + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", nameEn='" + nameEn + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
