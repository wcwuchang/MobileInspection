package com.tianjin.MobileInspection.entity;

/**
 * Created by wuchang on 2017-7-21.
 */
public class Weather {

    int state;
    String code;
    String city;
    String alams;
    String temp;
    String fleepTemp;
    String hum;
    String wind;

    String pm25;
    String dayPictureUrl;
    String nightPictureUrl;
    String weather;
    String shishi;

    public String getNightPictureUrl() {
        return nightPictureUrl;
    }

    public void setNightPictureUrl(String nightPictureUrl) {
        this.nightPictureUrl = nightPictureUrl;
    }

    public String getShishi() {
        return shishi;
    }

    public void setShishi(String shishi) {
        this.shishi = shishi;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getDayPictureUrl() {
        return dayPictureUrl;
    }

    public void setDayPictureUrl(String dayPictureUrl) {
        this.dayPictureUrl = dayPictureUrl;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAlams() {
        return alams;
    }

    public void setAlams(String alams) {
        this.alams = alams;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getFleepTemp() {
        return fleepTemp;
    }

    public void setFleepTemp(String fleepTemp) {
        this.fleepTemp = fleepTemp;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "state=" + state +
                ", city='" + city + '\'' +
                ", alams='" + alams + '\'' +
                ", temp='" + temp + '\'' +
                ", fleepTemp='" + fleepTemp + '\'' +
                ", hum='" + hum + '\'' +
                ", wind='" + wind + '\'' +
                '}';
    }
}
