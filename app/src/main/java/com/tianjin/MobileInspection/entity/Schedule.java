package com.tianjin.MobileInspection.entity;

/**
 * 待办事项
 * Created by wuchang on 2016/10/14.
 */
public class Schedule extends entityBase {

    private String type;//类型

    private String content;//内容

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "type='" + type + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
