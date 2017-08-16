package com.tianjin.MobileInspection.entity;

import java.io.Serializable;

/**
 * 评价
 * Created by wuchang on 2016-12-15.
 */
public class EvaluateManager implements Serializable{

    String evaluateId;//id
    String evaluateName;//名称
    String evaluateDate;//日期
    String evaluateUnit;//单位名称
    String itemId;
    String itemName;
    String itemGrade;
    String type;//xunjian:巡检单位，weixiu:维修单位,jijian:基建公司
    String type_role;//1:审批人员，2:ppp

    public String getType_role() {
        return type_role;
    }

    public void setType_role(String type_role) {
        this.type_role = type_role;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemGrade() {
        return itemGrade;
    }

    public void setItemGrade(String itemGrade) {
        this.itemGrade = itemGrade;
    }

    public String getEvaluateId() {
        return evaluateId;
    }

    public void setEvaluateId(String evaluateId) {
        this.evaluateId = evaluateId;
    }

    public String getEvaluateName() {
        return evaluateName;
    }

    public void setEvaluateName(String evaluateName) {
        this.evaluateName = evaluateName;
    }

    public String getEvaluateDate() {
        return evaluateDate;
    }

    public void setEvaluateDate(String evaluateDate) {
        this.evaluateDate = evaluateDate;
    }

    public String getEvaluateUnit() {
        return evaluateUnit;
    }

    public void setEvaluateUnit(String evaluateUnit) {
        this.evaluateUnit = evaluateUnit;
    }

    @Override
    public String toString() {
        return "EvaluateManager{" +
                "evaluateId='" + evaluateId + '\'' +
                ", evaluateName='" + evaluateName + '\'' +
                ", evaluateDate='" + evaluateDate + '\'' +
                ", evaluateUnit='" + evaluateUnit + '\'' +
                '}';
    }
}
