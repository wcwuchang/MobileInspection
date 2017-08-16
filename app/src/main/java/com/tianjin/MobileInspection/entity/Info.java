package com.tianjin.MobileInspection.entity;

/**
 * Created by 吴昶 on 2017/6/30.
 */
public class Info {

    String version;
    String url;
    String des;
    String type;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Info{" +
                "version='" + version + '\'' +
                ", url='" + url + '\'' +
                ", des='" + des + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
