package com.team.car.entity.shop;

/**
 * Created by Lmy on 2017/2/22.
 * email 1434117404@qq.com
 */

public class ShopMessageBean {
    private String shopIcon;//商店的图标
    private String shopName;//商店名称
    private String shopAddress;//商店地址
    private String shopDistance;//商店的距离
    private String shopLocationLat;//商店的经度
    private String shopLocationLng;//商店的维度
    private String shopType;//商店的类型
    private String shopPrice;//商店的价格
    private String shopTelephone;///商店的电话号码
    private String shopCityName;//商店所在的城市名
    private String District;//商店所在的地区

    public ShopMessageBean() {
    }

    public ShopMessageBean(String shopIcon, String shopName, String shopAddress, String shopDistance, String shopLocationLat, String shopLocationLng, String shopType, String shopPrice, String shopTelephone, String shopCityName, String district) {
        this.shopIcon = shopIcon;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopDistance = shopDistance;
        this.shopLocationLat = shopLocationLat;
        this.shopLocationLng = shopLocationLng;
        this.shopType = shopType;
        this.shopPrice = shopPrice;
        this.shopTelephone = shopTelephone;
        this.shopCityName = shopCityName;
        District = district;
    }

    public String getShopIcon() {
        return shopIcon;
    }

    public void setShopIcon(String shopIcon) {
        this.shopIcon = shopIcon;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopDistance() {
        return shopDistance;
    }

    public void setShopDistance(String shopDistance) {
        this.shopDistance = shopDistance;
    }

    public String getShopLocationLat() {
        return shopLocationLat;
    }

    public void setShopLocationLat(String shopLocationLat) {
        this.shopLocationLat = shopLocationLat;
    }

    public String getShopLocationLng() {
        return shopLocationLng;
    }

    public void setShopLocationLng(String shopLocationLng) {
        this.shopLocationLng = shopLocationLng;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(String shopPrice) {
        this.shopPrice = shopPrice;
    }

    public String getShopTelephone() {
        return shopTelephone;
    }

    public void setShopTelephone(String shopTelephone) {
        this.shopTelephone = shopTelephone;
    }

    public String getShopCityName() {
        return shopCityName;
    }

    public void setShopCityName(String shopCityName) {
        this.shopCityName = shopCityName;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    @Override
    public String toString() {
        return "ShopMessageBean{" +
                "shopIcon='" + shopIcon + '\'' +
                ", shopName='" + shopName + '\'' +
                ", shopAddress='" + shopAddress + '\'' +
                ", shopDistance='" + shopDistance + '\'' +
                ", shopLocationLat='" + shopLocationLat + '\'' +
                ", shopLocationLng='" + shopLocationLng + '\'' +
                ", shopType='" + shopType + '\'' +
                ", shopPrice='" + shopPrice + '\'' +
                ", shopTelephone='" + shopTelephone + '\'' +
                ", shopCityName='" + shopCityName + '\'' +
                ", District='" + District + '\'' +
                '}';
    }
}
