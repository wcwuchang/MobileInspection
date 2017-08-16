package com.tianjin.MobileInspection.entity;

import android.graphics.Bitmap;

/**
 * 台账
 * Created by wuchang on 2016-12-15.
 */
public class StandingBook {

    String type;//类型
    String typeId;//类型id
    String bookId;//台账id
    String bookName;//台账名称
    Bitmap bookBmp;//

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Bitmap getBookBmp() {
        return bookBmp;
    }

    public void setBookBmp(Bitmap bookBmp) {
        this.bookBmp = bookBmp;
    }

    @Override
    public String toString() {
        return "StandingBook{" +
                "type='" + type + '\'' +
                ", typeId='" + typeId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", bookName='" + bookName + '\'' +
                '}';
    }
}
