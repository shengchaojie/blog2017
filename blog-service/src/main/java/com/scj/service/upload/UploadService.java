package com.scj.service.upload;

import com.scj.dal.ro.upload.UploadInfoRo;

import java.util.List;

/**
 * Created by shengchaojie on 2017/8/15.
 */
public interface UploadService {

    void addUploadInfo(UploadInfoRo uploadInfoRo);

    List<UploadInfoRo> getUploadInfo(int page,int pageSize,String description);

    long count(String description);
}
