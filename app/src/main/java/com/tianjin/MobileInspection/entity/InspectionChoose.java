package com.tianjin.MobileInspection.entity;

import java.util.List;

/**
 * 开始巡检列表
 * Created by wuchang on 2016/10/13.
 */
public class InspectionChoose extends entityBase{

    private String inspectionId;//巡检id
    private String inspectionName;//巡检name
    private String unitId;//单位id
    private String unitName;//单位id
    private String contractname;//合同名称
    private String traffic;//交通工具
    private int trafficId;//交通工具
    private int state;//1待巡检 2巡检中 3巡检结束 4验收结束
    private String flowState;
    private String range;//巡检范围
    private String content;//巡检内容
    private String person;//巡检人
    private String personId;//巡检人
    private String nodeId;//节点编号
    private String date;//
    private String procInsId;//流程实例id
    private String taskId;
    private String taskDefKey;
    private String procDefKey;

    private List<String> nodes;//节点

    private List<Road> roads;//道路

    public List<Road> getRoads() {
        return roads;
    }

    public void setRoads(List<Road> roads) {
        this.roads = roads;
    }

    private boolean more=false;

    public String getFlowState() {
        return flowState;
    }

    public void setFlowState(String flowState) {
        this.flowState = flowState;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getTrafficId() {
        return trafficId;
    }

    public void setTrafficId(int trafficId) {
        this.trafficId = trafficId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getProcDefKey() {
        return procDefKey;
    }

    public void setProcDefKey(String procDefKey) {
        this.procDefKey = procDefKey;
    }

    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getInspectionName() {
        return inspectionName;
    }

    public void setInspectionName(String inspectionName) {
        this.inspectionName = inspectionName;
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

    public String getContractname() {
        return contractname;
    }

    public void setContractname(String contractname) {
        this.contractname = contractname;
    }

    public String getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
    }

    public boolean isMore() {
        return more;
    }
    public void setMore(boolean more) {
        this.more = more;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTraffic() {
        return traffic;
    }

    public void setTraffic(String traffic) {
        this.traffic = traffic;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getName() {
        return contractname;
    }

    public void setName(String name) {
        this.contractname = name;
    }

    @Override
    public String toString() {
        return "InspectionChoose{" +
                "inspectionId='" + inspectionId + '\'' +
                ", inspectionName='" + inspectionName + '\'' +
                ", unitId='" + unitId + '\'' +
                ", unitName='" + unitName + '\'' +
                ", contractname='" + contractname + '\'' +
                ", traffic='" + traffic + '\'' +
                ", trafficId=" + trafficId +
                ", range='" + range + '\'' +
                ", content='" + content + '\'' +
                ", person='" + person + '\'' +
                ", personId='" + personId + '\'' +
                ", nodeId='" + nodeId + '\'' +
                ", date='" + date + '\'' +
                ", procInsId='" + procInsId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", taskDefKey='" + taskDefKey + '\'' +
                ", procDefKey='" + procDefKey + '\'' +
                ", nodes=" + nodes +
                ", more=" + more +
                '}';
    }
}
