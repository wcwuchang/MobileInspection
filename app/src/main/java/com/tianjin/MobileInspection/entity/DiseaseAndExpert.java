package com.tianjin.MobileInspection.entity;

/**
 * Created by zhuangaoran on 2017-1-4.
 */
public class DiseaseAndExpert {

    String deId;
    String deName;
    String deContent;
    String deParentId;
    boolean isChose=false;

    public boolean isChose() {
        return isChose;
    }

    public void setChose(boolean chose) {
        isChose = chose;
    }

    public String getDeId() {
        return deId;
    }

    public void setDeId(String deId) {
        this.deId = deId;
    }

    public String getDeName() {
        return deName;
    }

    public void setDeName(String deName) {
        this.deName = deName;
    }

    public String getDeContent() {
        return deContent;
    }

    public void setDeContent(String deContent) {
        this.deContent = deContent;
    }

    public String getDeParentId() {
        return deParentId;
    }

    public void setDeParentId(String deParentId) {
        this.deParentId = deParentId;
    }
}
