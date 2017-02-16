package com.team.car.entity.found;

import java.io.Serializable;

/**
 * @author nanchen
 * @fileName AiYaSchoolPush
 * @packageName com.example.nanchen.aiyaschoolpush.model.info
 * @date 2016/11/23  10:51
 */

public class PicModel implements Serializable,IJsonModel{
    /**
     * 图片id
     */
    public int picid;
    /**
     * 图片地址
     */
    public String imageUrl;


    public PicModel(int picid,String imageUrl){
        this.picid = picid;
        this.imageUrl = imageUrl;
    }


}
