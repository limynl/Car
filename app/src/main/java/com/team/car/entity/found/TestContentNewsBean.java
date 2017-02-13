package com.team.car.entity.found;

/**
 * Created by Lmy on 2017/2/10.
 * email 1434117404@qq.com
 */

public class TestContentNewsBean {
    private String imageUrl;
    private String src;
    private String title;
    private String time;
    private String content;
    private String contentUrl;
    private int browseNumber;

    public TestContentNewsBean() {
    }

    public TestContentNewsBean(String imageUrl, String src, String title, String time, String content, String contentUrl, int browseNumber) {
        this.imageUrl = imageUrl;
        this.src = src;
        this.title = title;
        this.time = time;
        this.content = content;
        this.contentUrl = contentUrl;
        this.browseNumber = browseNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public int getBrowseNumber() {
        return browseNumber;
    }

    public void setBrowseNumber(int browseNumber) {
        this.browseNumber = browseNumber;
    }

    @Override
    public String toString() {
        return "TestContentNewsBean{" +
                "imageUrl='" + imageUrl + '\'' +
                ", src='" + src + '\'' +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                ", browseNumber=" + browseNumber +
                '}';
    }
}
