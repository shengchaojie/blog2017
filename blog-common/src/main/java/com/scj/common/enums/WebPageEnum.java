package com.scj.common.enums;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
public enum WebPageEnum {
    SINGER,ALBUM,SONG;

    public static WebPageEnum getEnumByCode(int i){
        for (WebPageEnum webPageEnum:WebPageEnum.values()){
            if (webPageEnum.ordinal()==i){
                return webPageEnum;
            }
        }
        return null;
    }
}
