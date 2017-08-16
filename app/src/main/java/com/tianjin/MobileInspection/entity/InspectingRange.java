package com.tianjin.MobileInspection.entity;

import java.io.Serializable;

/**
 * Created by 吴昶 on 2016/12/17.
 */
public class InspectingRange implements Serializable{
    Road road;

    public Road getRoad() {
        return road;
    }

    public void setRoad(Road road) {
        this.road = road;
    }
}
