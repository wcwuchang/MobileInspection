package com.tianjin.MobileInspection.entity;

/**
 * Created by 吴昶 on 2016/12/17.
 */
public class Office {

    String officeId;
    String officeName;//单位名称
    String officeParentId;
    String officeParentName;//上一级单位名称
    String officeAreaId;
    String officeAreaName;//负责区域

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getOfficeParentId() {
        return officeParentId;
    }

    public void setOfficeParentId(String officeParentId) {
        this.officeParentId = officeParentId;
    }

    public String getOfficeParentName() {
        return officeParentName;
    }

    public void setOfficeParentName(String officeParentName) {
        this.officeParentName = officeParentName;
    }

    public String getOfficeAreaId() {
        return officeAreaId;
    }

    public void setOfficeAreaId(String officeAreaId) {
        this.officeAreaId = officeAreaId;
    }

    public String getOfficeAreaName() {
        return officeAreaName;
    }

    public void setOfficeAreaName(String officeAreaName) {
        this.officeAreaName = officeAreaName;
    }
}
