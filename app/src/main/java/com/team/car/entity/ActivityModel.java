package com.team.car.entity;

/**
 * Created by Lmy on 2017/2/19.
 * email 1434117404@qq.com
 */

public class ActivityModel {
    private String title;
    private int id;

    public ActivityModel(int id,String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
