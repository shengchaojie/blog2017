package com.scj.common.enums;

/**
 * Created by shengchaojie on 2017/6/21.
 */
public enum JobTypeEnum {
    SINGER,ALBUM,SONG;

    public static JobTypeEnum getEnumByCode(int i){
        for (JobTypeEnum jobTypeEnum:JobTypeEnum.values()){
            if (jobTypeEnum.ordinal()==i){
                return jobTypeEnum;
            }
        }
        return null;
    }
}
