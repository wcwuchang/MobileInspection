package com.tianjin.MobileInspection.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by 吴昶 on 2016/12/7.
 */
@Table(name = "THiddenSpinner")
public class Road extends entityBase implements Serializable{

    @Column(name = "roadId")
    String roadId;
    @Column(name = "roadName")
    String roadName;//路名
    @Column(name = "lightCount")
    int lightCount;
    @Column(name = "roadNum")
    String roadNum;//路名
    @Column(name = "remark")
    String remark;//路名

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRoadId() {
        return roadId;
    }

    public void setRoadId(String roadId) {
        this.roadId = roadId;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public int getLightCount() {
        return lightCount;
    }

    public void setLightCount(int lightCount) {
        this.lightCount = lightCount;
    }

    public String getRoadNum() {
        return roadNum;
    }

    public void setRoadNum(String roadNum) {
        this.roadNum = roadNum;
    }

    @Override
    public String toString() {
        return "Road{" +
                "roadId='" + roadId + '\'' +
                ", roadName='" + roadName + '\'' +
                ", lightCount=" + lightCount +
                ", roadNum='" + roadNum + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
