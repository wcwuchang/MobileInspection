package com.tianjin.MobileInspection.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 吴昶 on 2016/12/22.
 */
public class HiddenTroubleDetail implements Serializable {

    String troubleId;//隐患id
    String inspectionName;//巡检
    String title;//隐患标题
    String content;//内容
    double longitude;//坐标经度
    double latitude;//坐标纬度
    String status;//状态
    ArrayList<String> image;//隐患图片地址
    List<HiddenType> hiddenTypes;//隐患的详情item
    String taskId;//任务id
    String taskName;//任务名称
    String taskDefKey;//
    String procInsId;//
    String procDefId;//
    String date;//时间
    String state;//状态
    String option;//审批操作
    String createManId;//创建人

    String emergencyType;//紧急情况
    String type;//隐患类型
    String typeName;//隐患名称
    String roadId;//道路id
    String roadName;//道路id
    String lightId;//路灯号id
    String quantity;//数量
    String unit;//单位
    String nameId;//设施名称Id
    String name;//设施名称Id
    String stockId;//病害库Id
    String stockName;//病害库Id
    int maintenanceState;//任务状态 0上报方案 1 立即维修
    ContractManager contractData;

    public ContractManager getContractData() {
        return contractData;
    }

    public void setContractData(ContractManager contractData) {
        this.contractData = contractData;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public int getMaintenanceState() {
        return maintenanceState;
    }

    public void setMaintenanceState(int maintenanceState) {
        this.maintenanceState = maintenanceState;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    public String getEmergencyType() {
        return emergencyType;
    }

    public void setEmergencyType(String emergencyType) {
        this.emergencyType = emergencyType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoadId() {
        return roadId;
    }

    public void setRoadId(String roadId) {
        this.roadId = roadId;
    }

    public String getLightId() {
        return lightId;
    }

    public void setLightId(String lightId) {
        this.lightId = lightId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCreateManId() {
        return createManId;
    }

    public void setCreateManId(String createManId) {
        this.createManId = createManId;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTroubleId() {
        return troubleId;
    }

    public void setTroubleId(String troubleId) {
        this.troubleId = troubleId;
    }

    public String getInspectionName() {
        return inspectionName;
    }

    public void setInspectionName(String inspectionName) {
        this.inspectionName = inspectionName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public List<HiddenType> getHiddenTypes() {
        return hiddenTypes;
    }

    public void setHiddenTypes(List<HiddenType> hiddenTypes) {
        this.hiddenTypes = hiddenTypes;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return "HiddenTroubleDetail{" +
                "troubleId='" + troubleId + '\'' +
                ", inspectionName='" + inspectionName + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", status='" + status + '\'' +
                ", image=" + image +
                ", hiddenTypes=" + hiddenTypes +
                ", taskId='" + taskId + '\'' +
                ", taskName='" + taskName + '\'' +
                ", taskDefKey='" + taskDefKey + '\'' +
                ", procInsId='" + procInsId + '\'' +
                ", procDefId='" + procDefId + '\'' +
                ", date='" + date + '\'' +
                ", state='" + state + '\'' +
                ", option='" + option + '\'' +
                ", createManId='" + createManId + '\'' +
                ", emergencyType='" + emergencyType + '\'' +
                ", type='" + type + '\'' +
                ", typeName='" + typeName + '\'' +
                ", roadId='" + roadId + '\'' +
                ", roadName='" + roadName + '\'' +
                ", lightId='" + lightId + '\'' +
                ", quantity='" + quantity + '\'' +
                ", unit='" + unit + '\'' +
                ", nameId='" + nameId + '\'' +
                ", name='" + name + '\'' +
                ", stockId='" + stockId + '\'' +
                ", stockName='" + stockName + '\'' +
                ", maintenanceState=" + maintenanceState +
                ", contractData=" + contractData +
                '}';
    }
}
