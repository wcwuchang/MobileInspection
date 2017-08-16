package com.tianjin.MobileInspection.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhuangaoran on 2016-12-30.
 */
@Table(name="TPlanManage")
public class PlanManage extends entityBase implements Serializable {

    @Column(name="planId")
    String planId;
    @Column(name="planName")
    String planName;//计划名称
    @Column(name="planTypeId")
    String planTypeId;
    @Column(name="planType")
    String planType;//计划类型
    @Column(name="createManId")
    String createManId;
    @Column(name="createMan")
    String createMan;//创建人
    @Column(name="planContent")
    String planContent;//计划内容
    @Column(name="createDate")
    String createDate;//创建时间
    @Column(name="planStartDate")
    String planStartDate;//计划开始时间
    @Column(name="planEndDate")
    String planEndDate;//计划结束时间
    @Column(name="isFinished")
    boolean isFinished;//是否完成
    @Column(name="state")
    String state;//状态
    @Column(name="contractId")
    String contractId;//状态
    @Column(name="contract")
    String contract;//状态



    ArrayList<HiddenType> planDetails;



    public class PlanDetail{
        String id;
        String title;
        String content;
        String startTime;
        String endTime;
        String typeId;
        String typeName;
        int typeNum;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public int getTypeNum() {
            return typeNum;
        }

        public void setTypeNum(int typeNum) {
            this.typeNum = typeNum;
        }

        @Override
        public String toString() {
            return "PlanDetailActivity{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    ", startTime='" + startTime + '\'' +
                    ", endTime='" + endTime + '\'' +
                    ", typeId='" + typeId + '\'' +
                    ", typeName='" + typeName + '\'' +
                    ", typeNum=" + typeNum +
                    '}';
        }
    }

    public ArrayList<HiddenType> getPlanDetails() {
        return planDetails;
    }

    public void setPlanDetails(ArrayList<HiddenType> planDetails) {
        this.planDetails = planDetails;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getPlanTypeId() {
        return planTypeId;
    }

    public void setPlanTypeId(String planTypeId) {
        this.planTypeId = planTypeId;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public String getCreateManId() {
        return createManId;
    }

    public void setCreateManId(String createManId) {
        this.createManId = createManId;
    }

    public String getPlanContent() {
        return planContent;
    }

    public void setPlanContent(String planContent) {
        this.planContent = planContent;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(String planStartDate) {
        this.planStartDate = planStartDate;
    }

    public String getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(String planEndDate) {
        this.planEndDate = planEndDate;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    @Override
    public String toString() {
        return "PlanManage{" +
                "planId='" + planId + '\'' +
                ", planName='" + planName + '\'' +
                ", planTypeId='" + planTypeId + '\'' +
                ", planType='" + planType + '\'' +
                ", createManId='" + createManId + '\'' +
                ", createMan='" + createMan + '\'' +
                ", planContent='" + planContent + '\'' +
                ", createDate='" + createDate + '\'' +
                ", planStartDate='" + planStartDate + '\'' +
                ", planEndDate='" + planEndDate + '\'' +
                ", isFinished=" + isFinished +
                ", state='" + state + '\'' +
                '}';
    }
}
