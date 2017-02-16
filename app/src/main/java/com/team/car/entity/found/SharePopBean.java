package com.team.car.entity.found;

/**
 * 分享的PopupWindow的bean
 * Created by Lmy on 2017/2/16.
 * email 1434117404@qq.com
 */

public class SharePopBean {
    private int iconId;//图片id
    private String name;//对应的名称

    public SharePopBean() {
    }

    public SharePopBean(int iconId, String name) {
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
