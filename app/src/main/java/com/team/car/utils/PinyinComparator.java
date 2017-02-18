package com.team.car.utils;

import com.team.car.entity.car.CarBrandSelectBean;

import java.util.Comparator;

/**
 * 根据拼音排序
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<CarBrandSelectBean> {
	public int compare(CarBrandSelectBean o1, CarBrandSelectBean o2) {
		if (o1.getCarInitial().equals("@") || o2.getCarInitial().equals("#")) {
			return -1;
		} else if (o1.getCarInitial().equals("#") || o2.getCarInitial().equals("@")) {
			return 1;
		} else {
			return o1.getCarInitial().compareTo(o2.getCarInitial());
		}
	}
}
