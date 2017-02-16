package com.team.car.entity.found;

/**
 * Created by Lmy on 2017/2/16.
 * email 1434117404@qq.com
 */

public class ShareBean {
    private int iconId;
    private String name;

    public ShareBean() {
    }

    public ShareBean(int iconId, String name) {
        this.iconId = iconId;
        this.name = name;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "shareBean{" +
                "iconId=" + iconId +
                ", name='" + name + '\'' +
                '}';
    }
}
