package com.tianjin.MobileInspection.entity;

import java.util.ArrayList;

/**
 * 合同管理
 * Created by wuchang on 2016/10/14.
 */
public class ContractManager extends entityBase {

    private String contractId;//合同id

    private String name;//合同名称

    private String unitName;//单位名称

    private String unitId;

    private String beginDate;//开始日期

    private String endDate;//结束日期

    private String content;//内容

    private ArrayList<Road> road;//道路列表

    private ArrayList<HiddenType> hiddenTypes;//维修项列表，属性相同共用 HiddenType

    private ArrayList<ContractData> datalist;

    private int type;//合同类型 1巡检合同，2维修合同

    private int flowtype;//用于流程的阶段判断

    public ArrayList<ContractData> getDatalist() {
        return datalist;
    }

    public void setDatalist(ArrayList<ContractData> datalist) {
        this.datalist = datalist;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<Road> getRoad() {
        return road;
    }

    public void setRoad(ArrayList<Road> road) {
        this.road = road;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getFlowtype() {
        return flowtype;
    }

    public void setFlowtype(int flowtype) {
        this.flowtype = flowtype;
    }

    public ArrayList<HiddenType> getHiddenTypes() {
        return hiddenTypes;
    }

    public void setHiddenTypes(ArrayList<HiddenType> hiddenTypes) {
        this.hiddenTypes = hiddenTypes;
    }

    @Override
    public String toString() {
        return "ContractManager{" +
                "contractId='" + contractId + '\'' +
                ", name='" + name + '\'' +
                ", unitName='" + unitName + '\'' +
                ", unitId='" + unitId + '\'' +
                ", beginDate='" + beginDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", content='" + content + '\'' +
                ", road=" + road +
                ", type=" + type +
                ", flowtype=" + flowtype +
                '}';
    }
}
