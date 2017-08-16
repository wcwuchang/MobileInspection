package com.tianjin.MobileInspection.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wuchang on 2016-12-26.
 */
public class Task implements Serializable {

    String taskId;
    String taskName;//任务名称
    String taskContent;//任务内容
    String contactId;
    String contactName;//联系人
    String contractId;
    String contractName;//合同
    String troubleId;
    String troubleName;//隐患名称
    String date;//日期
    ArrayList<String> imagePath;//隐患图片地址
    ArrayList<HiddenType> hidden;//维修项列表
    ArrayList<String> fujian;//附件列表
    String option;
    int status;//状态
    int state;//??状态 0:审核中 1：待完成 2：已完成
    String acttaskId;//任务id
    String acttaskName;//任务名称
    String acttaskDefKey;//
    String actprocInsId;//
    String actprocDefId;//

    HiddenTroubleDetail hiddenTroubleDetail;

    boolean isMaintenace;
    String reportContent;//上报方案的内容

    int maintenanceState;//任务状态 0上报方案 1 立即维修
    int yearMonth;//计量月份

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public boolean isMaintenace() {
        return isMaintenace;
    }

    public void setMaintenace(boolean maintenace) {
        isMaintenace = maintenace;
    }

    public int getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(int yearMonth) {
        this.yearMonth = yearMonth;
    }

    public int getMaintenanceState() {
        return maintenanceState;
    }

    public void setMaintenanceState(int maintenanceState) {
        this.maintenanceState = maintenanceState;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getActtaskId() {
        return acttaskId;
    }

    public void setActtaskId(String acttaskId) {
        this.acttaskId = acttaskId;
    }

    public String getActtaskName() {
        return acttaskName;
    }

    public void setActtaskName(String acttaskName) {
        this.acttaskName = acttaskName;
    }

    public String getActtaskDefKey() {
        return acttaskDefKey;
    }

    public void setActtaskDefKey(String acttaskDefKey) {
        this.acttaskDefKey = acttaskDefKey;
    }

    public String getActprocInsId() {
        return actprocInsId;
    }

    public void setActprocInsId(String actprocInsId) {
        this.actprocInsId = actprocInsId;
    }

    public String getActprocDefId() {
        return actprocDefId;
    }

    public void setActprocDefId(String actprocDefId) {
        this.actprocDefId = actprocDefId;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public HiddenTroubleDetail getHiddenTroubleDetail() {
        return hiddenTroubleDetail;
    }

    public void setHiddenTroubleDetail(HiddenTroubleDetail hiddenTroubleDetail) {
        this.hiddenTroubleDetail = hiddenTroubleDetail;
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

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
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

    public String getTroubleId() {
        return troubleId;
    }

    public void setTroubleId(String troubleId) {
        this.troubleId = troubleId;
    }

    public String getTroubleName() {
        return troubleName;
    }

    public void setTroubleName(String troubleName) {
        this.troubleName = troubleName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getImagePath() {
        return imagePath;
    }

    public void setImagePath(ArrayList<String> imagePath) {
        this.imagePath = imagePath;
    }

    public ArrayList<HiddenType> getHidden() {
        return hidden;
    }

    public void setHidden(ArrayList<HiddenType> hidden) {
        this.hidden = hidden;
    }

    public ArrayList<String> getFujian() {
        return fujian;
    }

    public void setFujian(ArrayList<String> fujian) {
        this.fujian = fujian;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId='" + taskId + '\'' +
                ", taskName='" + taskName + '\'' +
                ", taskContent='" + taskContent + '\'' +
                ", contactId='" + contactId + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contractId='" + contractId + '\'' +
                ", contractName='" + contractName + '\'' +
                ", troubleId='" + troubleId + '\'' +
                ", troubleName='" + troubleName + '\'' +
                ", date='" + date + '\'' +
                ", imagePath=" + imagePath +
                ", hidden=" + hidden +
                ", fujian=" + fujian +
                ", option='" + option + '\'' +
                ", status=" + status +
                ", state=" + state +
                ", acttaskId='" + acttaskId + '\'' +
                ", acttaskName='" + acttaskName + '\'' +
                ", acttaskDefKey='" + acttaskDefKey + '\'' +
                ", actprocInsId='" + actprocInsId + '\'' +
                ", actprocDefId='" + actprocDefId + '\'' +
                ", hiddenTroubleDetail=" + hiddenTroubleDetail +
                ", isMaintenace=" + isMaintenace +
                ", reportContent='" + reportContent + '\'' +
                ", maintenanceState=" + maintenanceState +
                ", yearMonth=" + yearMonth +
                '}';
    }
}
