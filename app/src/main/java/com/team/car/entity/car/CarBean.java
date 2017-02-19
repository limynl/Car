package com.team.car.entity.car;

import java.io.Serializable;

/**
 * Created by Lmy on 2017/2/16.
 * email 1434117404@qq.com
 */

public class CarBean implements Serializable{
    private int carId;//车信息ID
    private String carNumber;//车牌号
    private String carIconUrl;//车图标
    private String carFrameNum;//车架号
    private String carEngineNum;//发动机号
    private String carRegistrationDate;//注册日期
    private String carBrand;//车品牌
    private String carSpecificModel;//具体车型

    public CarBean() {
    }

    public CarBean(int carId, String carNumber, String carIconUrl, String carFrameNum, String carEngineNum, String carRegistrationDate, String carBrand, String carSpecificModel) {
        this.carId = carId;
        this.carNumber = carNumber;
        this.carIconUrl = carIconUrl;
        this.carFrameNum = carFrameNum;
        this.carEngineNum = carEngineNum;
        this.carRegistrationDate = carRegistrationDate;
        this.carBrand = carBrand;
        this.carSpecificModel = carSpecificModel;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarIconUrl() {
        return carIconUrl;
    }

    public void setCarIconUrl(String carIconUrl) {
        this.carIconUrl = carIconUrl;
    }

    public String getCarFrameNum() {
        return carFrameNum;
    }

    public void setCarFrameNum(String carFrameNum) {
        this.carFrameNum = carFrameNum;
    }

    public String getCarEngineNum() {
        return carEngineNum;
    }

    public void setCarEngineNum(String carEngineNum) {
        this.carEngineNum = carEngineNum;
    }

    public String getCarRegistrationDate() {
        return carRegistrationDate;
    }

    public void setCarRegistrationDate(String carRegistrationDate) {
        this.carRegistrationDate = carRegistrationDate;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarSpecificModel() {
        return carSpecificModel;
    }

    public void setCarSpecificModel(String carSpecificModel) {
        this.carSpecificModel = carSpecificModel;
    }

    @Override
    public String toString() {
        return "CarBean{" +
                "carId=" + carId +
                ", carNumber='" + carNumber + '\'' +
                ", carIconUrl='" + carIconUrl + '\'' +
                ", carFrameNum='" + carFrameNum + '\'' +
                ", carEngineNum='" + carEngineNum + '\'' +
                ", carRegistrationDate='" + carRegistrationDate + '\'' +
                ", carBrand='" + carBrand + '\'' +
                ", carSpecificModel='" + carSpecificModel + '\'' +
                '}';
    }
}
