package com.team.car.entity.car;

import java.util.List;

/**
 * Created by Lmy on 2017/2/2.
 * email 1434117404@qq.com
 */

public class CarBrandSelectBean {
    private String carLogo;//汽车的logo
    private String carBrand;//汽车的品牌
    private String carInitial;//汽车品牌的首字母
    private String name;//名字
    private List<String> list;//具体车型

    public CarBrandSelectBean() {
    }

    public CarBrandSelectBean(String carLogo, String carBrand, String carInitial, String name, List<String> list) {
        this.carLogo = carLogo;
        this.carBrand = carBrand;
        this.carInitial = carInitial;
        this.name = name;
        this.list = list;
    }

    public String getCarLogo() {
        return carLogo;
    }

    public void setCarLogo(String carLogo) {
        this.carLogo = carLogo;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarInitial() {
        return carInitial;
    }

    public void setCarInitial(String carInitial) {
        this.carInitial = carInitial;
    }

    public List<String> getList() {
        return list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "CarBrandSelectBean{" +
                "carLogo='" + carLogo + '\'' +
                ", carBrand='" + carBrand + '\'' +
                ", carInitial='" + carInitial + '\'' +
                ", name='" + name + '\'' +
                ", list=" + list +
                '}';
    }
}
