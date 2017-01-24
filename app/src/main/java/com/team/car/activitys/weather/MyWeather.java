package com.team.car.activitys.weather;

/**
 * Created by Lmy on 2017/1/24.
 * email 1434117404@qq.com
 */

public class MyWeather {
    private String date;
    private String weather;
    private String temperature;
    private int weatherIcon;

    public MyWeather() {
    }

    public MyWeather(String date, String weather, String temperature) {
        this.date = date;
        this.weather = weather;
        this.temperature = temperature;
    }

    public String getDate() {
        return date;
    }

    public String getWeather() {
        return weather;
    }

    public String getTemperature() {
        return temperature;
    }

}
