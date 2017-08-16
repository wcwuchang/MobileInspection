package com.tianjin.MobileInspection.entity;

import android.graphics.Bitmap;

/**
 * 巡检
 * Created by wuchang on 2016/10/14.
 */
public class Inspection extends entityBase {

    private String name;//巡检名称
    private String inspectionId;//巡检id
    private String content;//巡检内容
    private String person;//巡检人
    private String date;//日期
    private Bitmap personhead;//头像
    private boolean isFinished;//是否完成

    public String getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
    }

    public Bitmap getPersonhead() {
        return personhead;
    }

    public void setPersonhead(Bitmap personhead) {
        this.personhead = personhead;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Inspection{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", person='" + person + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
