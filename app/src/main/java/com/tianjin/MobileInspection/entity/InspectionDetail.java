package com.tianjin.MobileInspection.entity;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wuchang on 2016-12-12.
 */
public class InspectionDetail implements Serializable{

    String id;
    String title;//
    String inspectionId;//巡检ID
    String inspectionName;//巡检name
    String contractId;//合同id
    String contractName;//合同name
    String unitId;//单位id
    String unitName;//单位名称
    String traffic;//交通工具
    String content;//内容
    String contactId;//联系人id
    String contactName;//联系人名称
    String date;//日期
    ArrayList<Road> roads;//道路
    ArrayList<Yinhuan> yinhuans;//隐患
    ArrayList<LatLng> clocllist;//打点坐标
    String option;//操作
    String state;//状态
    String taskDefKey;//次字段表示在该界面要怎么做

    String startTime;//巡检起始时间
    String endTime;

    public ArrayList<LatLng> getClocllist() {
        return clocllist;
    }

    public void setClocllist(ArrayList<LatLng> clocllist) {
        this.clocllist = clocllist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
    }

    public String getInspectionName() {
        return inspectionName;
    }

    public void setInspectionName(String inspectionName) {
        this.inspectionName = inspectionName;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getTraffic() {
        return traffic;
    }

    public void setTraffic(String traffic) {
        this.traffic = traffic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<Road> getRoads() {
        return roads;
    }

    public void setRoads(ArrayList<Road> roads) {
        this.roads = roads;
    }

    public ArrayList<Yinhuan> getYinhuans() {
        return yinhuans;
    }

    public void setYinhuans(ArrayList<Yinhuan> yinhuans) {
        this.yinhuans = yinhuans;
    }
}
