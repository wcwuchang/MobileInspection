package com.tianjin.MobileInspection.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by wuchang on 2017-8-8.
 */
@Table(name = "TNotificationMsg")
public class NotificationMsg extends entityBase{

    @Column(name = "msgId")
    String msgId;
    @Column(name = "title")
    String title;
    @Column(name = "tricker")
    String tricker;
    @Column(name = "text")
    String text;
    @Column(name = "type")
    String type;
    @Column(name = "sendId")
    String sendId;
    @Column(name = "custom")
    String custom;
    @Column(name = "hasRead")
    boolean hasRead;
    @Column(name = "msgTime")
    String msgTime;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTricker() {
        return tricker;
    }

    public void setTricker(String tricker) {
        this.tricker = tricker;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    @Override
    public String toString() {
        return "NotificationMsg{" +
                "msgId='" + msgId + '\'' +
                ", title='" + title + '\'' +
                ", tricker='" + tricker + '\'' +
                ", text='" + text + '\'' +
                ", type='" + type + '\'' +
                ", sendId='" + sendId + '\'' +
                ", custom='" + custom + '\'' +
                ", hasRead=" + hasRead +
                ", msgTime='" + msgTime + '\'' +
                '}';
    }
}
