package com.tianjin.MobileInspection.entity;

/**
 * Created by wuchang on 2016-12-12.
 */
public class Yinhuan {

    String yhId;//隐患id
    String yhName;//隐患名称
    String yhContent;//隐患内容
    String yhLocation;//位置
    String personName;//上报人
    String inspectionName;//巡检名称
    String inspectionId;//巡检id
    String yhdate;//隐患日期

    public String getYhId() {
        return yhId;
    }

    public void setYhId(String yhId) {
        this.yhId = yhId;
    }

    public String getYhName() {
        return yhName;
    }

    public void setYhName(String yhName) {
        this.yhName = yhName;
    }

    public String getYhContent() {
        return yhContent;
    }

    public void setYhContent(String yhContent) {
        this.yhContent = yhContent;
    }

    public String getYhLocation() {
        return yhLocation;
    }

    public void setYhLocation(String yhLocation) {
        this.yhLocation = yhLocation;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getInspectionName() {
        return inspectionName;
    }

    public void setInspectionName(String inspectionName) {
        this.inspectionName = inspectionName;
    }

    public String getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
    }

    public String getYhdate() {
        return yhdate;
    }

    public void setYhdate(String yhdate) {
        this.yhdate = yhdate;
    }

    @Override
    public String toString() {
        return "Yinhuan{" +
                "yhId='" + yhId + '\'' +
                ", yhName='" + yhName + '\'' +
                ", yhContent='" + yhContent + '\'' +
                ", yhLocation='" + yhLocation + '\'' +
                ", personName='" + personName + '\'' +
                ", inspectionName='" + inspectionName + '\'' +
                ", inspectionId='" + inspectionId + '\'' +
                ", yhdate='" + yhdate + '\'' +
                '}';
    }
}
