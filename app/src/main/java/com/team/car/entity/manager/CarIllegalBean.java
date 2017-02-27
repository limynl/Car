package com.team.car.entity.manager;

/**
 * Created by Lmy on 2017/2/27.
 * email 1434117404@qq.com
 */

public class CarIllegalBean {
    private String time;
    private String result;
    private String location;
    private String reason;

    public CarIllegalBean() {
    }

    public CarIllegalBean(String time, String result, String location, String reason) {
        this.time = time;
        this.result = result;
        this.location = location;
        this.reason = reason;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "CarIllegalBean{" +
                "time='" + time + '\'' +
                ", result='" + result + '\'' +
                ", location='" + location + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
