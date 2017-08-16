package com.tianjin.MobileInspection.entity;

import java.util.Map;

/**
 * Created by wuchang on 2016-12-9.
 */
public class NewInspectionChose {

    String id;
    String name;
    boolean isChose=false;

    Map<String,Object> map;

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsChose() {
        return isChose;
    }

    public void setIsChose(boolean isChose) {
        this.isChose = isChose;
    }
}
