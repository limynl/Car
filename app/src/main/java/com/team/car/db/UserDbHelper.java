package com.team.car.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.team.car.entity.user.UserBean;

/**
 * 用户的数据库
 * Created by Lmy on 2017/2/17.
 * email 1434117404@qq.com
 */

public class UserDbHelper {
    private static UserDbHelper instance;
    private SharedPreferences sharedPreferences;

    private UserDbHelper(Context context){
        sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    /**
     * 得到当前类对象
     * @return
     */
    public static UserDbHelper getInstance(){
        return instance;
    }

    /**
     * 实例化当前类对象
     * @param context
     */
    public static void setInstance(Context context){
        instance = new UserDbHelper(context);
    }

    /**
     * 保存用户的信息到当前数据库
     * @param userBean 要保存的用户
     */
    public void saveUserLoginInfo(UserBean userBean){
        saveIntegerConfig("userId", userBean.getUserId());
        saveStringConfig("userName", userBean.getUserName());
        saveStringConfig("nickName", userBean.getNickName());
        saveStringConfig("passWord", userBean.getPassWord());
        saveStringConfig("birthDay", userBean.getBirthDay());
        saveStringConfig("sex", userBean.getSex());
        saveStringConfig("phoneNumber", userBean.getPhoneNumber());
        saveStringConfig("qqNumber", userBean.getQqNumber());
        saveStringConfig("sinaNumber", userBean.getSinaNumber());
        saveStringConfig("userAddress", userBean.getUserAddress());
        saveBooleanConfig("loginFlag", userBean.getLoginFlag());
    }

    /**
     * 从数据库中得到用户的信息
     * @return  返回当前需要的用户
     */
    public UserBean getUserInfo(){
        UserBean userBean = new UserBean();
        userBean.setUserId(getIntegerConfig("userId"));
        userBean.setUserName(getStringConfig("userName"));
        userBean.setNickName(getStringConfig("nickName"));
        userBean.setPassWord(getStringConfig("passWord"));
        userBean.setBirthDay(getStringConfig("birthDay"));
        userBean.setSex(getStringConfig("sex"));
        userBean.setPhoneNumber(getStringConfig("phoneNumber"));
        userBean.setQqNumber(getStringConfig("qqNumber"));
        userBean.setSinaNumber(getStringConfig("sinaNumber"));
        userBean.setUserAddress(getStringConfig("userAddress"));
        userBean.setLoginFlag(getBooleanConfig("loginFlag"));
        return userBean;
    }

    /**
     * 保存String类型数据
     * @param key
     * @param value
     */
    public void saveStringConfig(String key, String value){
        sharedPreferences.edit().putString(key, value).commit();
    }

    /**
     * 保存boolean类型数据
     * @param key
     * @param value
     */
    public void saveBooleanConfig(String key, boolean value){
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    /**
     * 保存Integer类型数据
     * @param key
     * @param value
     */
    public void saveIntegerConfig(String key, int value){
        sharedPreferences.edit().putInt(key, value).commit();
    }

    /**
     * 得到String类型的数据
     * @param key
     * @return
     */
    public String getStringConfig(String key){
        return sharedPreferences.getString(key, "");
    }

    /**
     * 得到boolean类型数据
     * @param key
     * @return
     */
    public boolean getBooleanConfig(String key){
        return sharedPreferences.getBoolean(key, false);
    }

    /**
     * 得到Integer类型数据
     * @param key
     * @return
     */
    public int getIntegerConfig(String key){
        return sharedPreferences.getInt(key, -1);
    }
}
