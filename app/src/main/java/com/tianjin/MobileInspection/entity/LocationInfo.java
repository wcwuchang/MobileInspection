package com.tianjin.MobileInspection.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 位置数据实体类对象
 * Created by wuchang on 2016-11-24.
 * 此数据库仅保存上传失败的位置信息
 */
@Table(name="TLocationInfo")
public class LocationInfo extends entityBase{

    @Column(name="lon")
    private double lon;//经度
    @Column(name="lat")
    private double lat;//纬度
    @Column(name="time")
    private String time;//时间

    public double getLon() {
        return lon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "LocationInfo{" +
                "lon=" + lon +
                ", lat=" + lat +
                ", time='" + time + '\'' +
                '}';
    }
}
