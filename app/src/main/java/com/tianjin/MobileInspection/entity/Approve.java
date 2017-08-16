package com.tianjin.MobileInspection.entity;

/**
 * 审批
 * Created by wuchang on 2016-12-12.
 */
public class Approve {

    private String approveId;//审批id
    private String approveContent;//审批内容
    private String approveDate;//审批日期
    private String approveCode;
    private String approveMan;
    private String approveStartTime;
    private String approveFinishTime;
    private String approveDecision;

    public String getApproveCode() {
        return approveCode;
    }

    public void setApproveCode(String approveCode) {
        this.approveCode = approveCode;
    }

    public String getApproveMan() {
        return approveMan;
    }

    public void setApproveMan(String approveMan) {
        this.approveMan = approveMan;
    }

    public String getApproveStartTime() {
        return approveStartTime;
    }

    public void setApproveStartTime(String approveStartTime) {
        this.approveStartTime = approveStartTime;
    }

    public String getApproveFinishTime() {
        return approveFinishTime;
    }

    public void setApproveFinishTime(String approveFinishTime) {
        this.approveFinishTime = approveFinishTime;
    }

    public String getApproveDecision() {
        return approveDecision;
    }

    public void setApproveDecision(String approveDecision) {
        this.approveDecision = approveDecision;
    }

    public String getApproveId() {
        return approveId;
    }

    public void setApproveId(String approveId) {
        this.approveId = approveId;
    }

    public String getApproveContent() {
        return approveContent;
    }

    public void setApproveContent(String approveContent) {
        this.approveContent = approveContent;
    }

    public String getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(String approveDate) {
        this.approveDate = approveDate;
    }

    @Override
    public String toString() {
        return "Approve{" +
                "approveId='" + approveId + '\'' +
                ", approveContent='" + approveContent + '\'' +
                ", approveDate='" + approveDate + '\'' +
                '}';
    }
}
