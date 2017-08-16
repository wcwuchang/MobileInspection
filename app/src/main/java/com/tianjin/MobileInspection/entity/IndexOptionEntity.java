package com.tianjin.MobileInspection.entity;

import android.graphics.Bitmap;

/**
 * 首页操作
 * Created by wuchang on 2016-11-16.
 */
public class IndexOptionEntity {

    private String optionName;
    private int optionId;
    private Bitmap optionBmp;

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public Bitmap getOptionBmp() {
        return optionBmp;
    }

    public void setOptionBmp(Bitmap optionBmp) {
        this.optionBmp = optionBmp;
    }

    @Override
    public String toString() {
        return "IndexOptionEntity{" +
                "optionName='" + optionName + '\'' +
                ", optionId=" + optionId +
                ", optionBmp=" + optionBmp +
                '}';
    }
}
