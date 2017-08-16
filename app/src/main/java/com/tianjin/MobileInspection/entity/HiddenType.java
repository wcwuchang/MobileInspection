package com.tianjin.MobileInspection.entity;

import java.io.Serializable;

/**
 * Created by wuchang on 2016-12-14.
 */
public class HiddenType implements Serializable {

    String typeId;//隐患类型的Id
    String typeUnit;//隐患类型的单位（米，平米，块，个等）
    String typeUnitId;//单位的Id
    String typeName;//隐患名称
    String troubleContent;//内容
    double num;//数量
    boolean chosed=false;//是否被选择
    boolean show=false;

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }

    public String getTroubleContent() {
        return troubleContent;
    }

    public void setTroubleContent(String troubleContent) {
        this.troubleContent = troubleContent;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeUnit() {
        return typeUnit;
    }

    public void setTypeUnit(String typeUnit) {
        this.typeUnit = typeUnit;
    }

    public String getTypeUnitId() {
        return typeUnitId;
    }

    public void setTypeUnitId(String typeUnitId) {
        this.typeUnitId = typeUnitId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean isChosed() {
        return chosed;
    }

    public void setChosed(boolean chosed) {
        this.chosed = chosed;
    }

    @Override
    public String toString() {
        return "HiddenType{" +
                "typeId='" + typeId + '\'' +
                ", typeUnit='" + typeUnit + '\'' +
                ", typeUnitId='" + typeUnitId + '\'' +
                ", typeName='" + typeName + '\'' +
                ", chosed=" + chosed +
                '}';
    }
}
