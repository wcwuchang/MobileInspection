package com.tianjin.MobileInspection.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 待办
 * Created by 吴昶 on 2016/12/15.
 */
@Table(name = "TTodo")
public class Todo extends entityBase implements Serializable{

    @Column(name = "todoId")
    String todoId;//待办id
    @Column(name = "todoType")
    int todoType;//待办类型
    @Column(name = "taskId")
    String taskId;//任务id
    @Column(name = "taskName")
    String taskName;//任务名称
    @Column(name = "taskDefKey")
    String taskDefKey;
    @Column(name = "procInsId")
    String procInsId;
    @Column(name = "procDefId")
    String procDefId;
    @Column(name = "procDefKey")
    String procDefKey;
    @Column(name = "title")
    String title;//待办标题
    @Column(name = "apply")
    String apply;//申请人
    @Column(name = "status")
    int status;//状态
    @Column(name = "taskCreateDate")
    String taskCreateDate;//创建时间

    @Column(name = "type")
    String type;
    @Column(name = "typeName")
    String typeName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTodoType() {
        return todoType;
    }

    public void setTodoType(int todoType) {
        this.todoType = todoType;
    }

    public String getProcDefKey() {
        return procDefKey;
    }

    public void setProcDefKey(String procDefKey) {
        this.procDefKey = procDefKey;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public String getTodoId() {
        return todoId;
    }

    public void setTodoId(String todoId) {
        this.todoId = todoId;
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

    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getApply() {
        return apply;
    }

    public void setApply(String apply) {
        this.apply = apply;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTaskCreateDate() {
        return taskCreateDate;
    }

    public void setTaskCreateDate(String taskCreateDate) {
        this.taskCreateDate = taskCreateDate;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "todoId='" + todoId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", taskName='" + taskName + '\'' +
                ", taskDefKey='" + taskDefKey + '\'' +
                ", procInsId='" + procInsId + '\'' +
                ", procDefId='" + procDefId + '\'' +
                ", procDefKey='" + procDefKey + '\'' +
                ", title='" + title + '\'' +
                ", apply='" + apply + '\'' +
                ", status=" + status +
                ", taskCreateDate='" + taskCreateDate + '\'' +
                '}';
    }
}
