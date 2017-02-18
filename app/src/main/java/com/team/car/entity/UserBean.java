package com.team.car.entity;

/**
 * 用户的实体类
 * Created by Lmy on 2017/2/16.
 * email 1434117404@qq.com
 */

public class UserBean {
    private int userId;//用户的ID
    private String userName;//用户名
    private String nickName;//用户昵称
    private String passWord;//用户密码
    private String birthDay;//用户生日
    private String sex;//用户性别
    private String phoneNumber;//电话号码
    private String qqNumber;//QQ账号
    private String sinaNumber;//新浪账号
    private String userAddress;//用户地址
    private String headImageUrl;//用户头像连接
    private Boolean loginFlag;//用户是否登录

    public UserBean() {
    }

    public UserBean(int userId, String userName, String nickName, String passWord, String birthDay, String sex, String phoneNumber, String qqNumber, String sinaNumber, String userAddress, String headImageUrl, Boolean loginFlag) {
        this.userId = userId;
        this.userName = userName;
        this.nickName = nickName;
        this.passWord = passWord;
        this.birthDay = birthDay;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.qqNumber = qqNumber;
        this.sinaNumber = sinaNumber;
        this.userAddress = userAddress;
        this.headImageUrl = headImageUrl;
        this.loginFlag = loginFlag;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getQqNumber() {
        return qqNumber;
    }

    public void setQqNumber(String qqNumber) {
        this.qqNumber = qqNumber;
    }

    public String getSinaNumber() {
        return sinaNumber;
    }

    public void setSinaNumber(String sinaNumber) {
        this.sinaNumber = sinaNumber;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public Boolean getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(Boolean loginFlag) {
        this.loginFlag = loginFlag;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", birthDay='" + birthDay + '\'' +
                ", sex='" + sex + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", qqNumber='" + qqNumber + '\'' +
                ", sinaNumber='" + sinaNumber + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", headImageUrl='" + headImageUrl + '\'' +
                ", loginFlag=" + loginFlag +
                '}';
    }
}
