package com.tianjin.MobileInspection.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by wuchang on 2017-6-5.
 */
@Table(name = "THiddenSpinner")
public class HiddenSpinner extends entityBase{

    @Column(name="spid")
    String spid;//id
    @Column(name="name")
    String name;//名称
    @Column(name="unit")
    String unit;//单位
    @Column(name="parentId")
    String parentId;//父级id
    @Column(name="remarks")
    String remarks;
    @Column(name="parentIds")
    String parentIds;

    public String getSpid() {
        return spid;
    }

    public void setSpid(String spid) {
        this.spid = spid;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
