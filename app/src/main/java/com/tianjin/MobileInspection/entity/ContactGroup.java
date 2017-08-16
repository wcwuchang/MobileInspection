package com.tianjin.MobileInspection.entity;

/**
 * 未使用
 * Created by wuchang on 2016-12-1.
 */
public class ContactGroup extends entityBase {

    private String groupName;
    private int number;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "ContactGroup{" +
                "groupName='" + groupName + '\'' +
                ", number=" + number +
                '}';
    }
}
