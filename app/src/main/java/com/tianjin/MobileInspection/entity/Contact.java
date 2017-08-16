package com.tianjin.MobileInspection.entity;

import android.graphics.Bitmap;

/**
 * 联系人
 * Created by 吴昶 on 2016/10/17.
 */
public class Contact extends entityBase{

    private String name;//姓名
    private String personId;//id
    private String sortLetters;//首字母
    private String officeName;//单位名称
    private String officeId;
    private String phoneNumber;//电话
    private String email;//邮箱
    private Bitmap head;
    private String headPath;//头像地址
    private boolean isChoice=false;

    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public String getOfficeName() {
        return officeName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public boolean isChoice() {
        return isChoice;
    }

    public void setChoice(boolean choice) {
        isChoice = choice;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public Bitmap getHead() {
        return head;
    }

    public void setHead(Bitmap head) {
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return officeName;
    }

    public void setType(String type) {
        this.officeName = type;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String photoNumber) {
        this.phoneNumber = photoNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", sortLetters='" + sortLetters + '\'' +
                ", type='" + officeName + '\'' +
                ", photoNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
