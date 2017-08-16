package com.tianjin.MobileInspection.entity;

/**
 * Created by wuchang on 2017-4-5.
 */
public class ContractData {

    String contractItem;//名称
    String contractItemId;//名称
    double contractNum;//合同数量
    double hasdoneNum;//已完成数量
    String unit;//单位
    int month;
    int year;
    double price;//单价
    double budget;//预算
    double spend;//已花费
    String priceUnit;//单位
    Road road;

    public String getContractItemId() {
        return contractItemId;
    }

    public void setContractItemId(String contractItemId) {
        this.contractItemId = contractItemId;
    }

    public String getContractItem() {
        return contractItem;
    }

    public void setContractItem(String contractItem) {
        this.contractItem = contractItem;
    }

    public Road getRoad() {
        return road;
    }

    public void setRoad(Road road) {
        this.road = road;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getSpend() {
        return spend;
    }

    public void setSpend(double spend) {
        this.spend = spend;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public double getContractNum() {
        return contractNum;
    }

    public void setContractNum(double contractNum) {
        this.contractNum = contractNum;
    }

    public double getHasdoneNum() {
        return hasdoneNum;
    }

    public void setHasdoneNum(double hasdoneNum) {
        this.hasdoneNum = hasdoneNum;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "ContractData{" +
                "contractItem='" + contractItem + '\'' +
                ", contractNum=" + contractNum +
                ", hasdoneNum=" + hasdoneNum +
                ", unit='" + unit + '\'' +
                ", month=" + month +
                ", year=" + year +
                ", price=" + price +
                ", budget=" + budget +
                ", spend=" + spend +
                ", priceUnit='" + priceUnit + '\'' +
                ", road=" + road +
                '}';
    }
}
