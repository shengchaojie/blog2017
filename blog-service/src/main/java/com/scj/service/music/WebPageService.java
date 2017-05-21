package com.scj.service.music;

import com.scj.common.enums.WebPageEnum;
import com.scj.dal.ro.music.WebPageRO;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
public interface WebPageService {
    WebPageRO findByIdAndType(Long id,Integer index, WebPageEnum webPageType);

    int add(WebPageRO webPageRO);
}
