package com.scj.dal.ro.upload;

import com.scj.dal.ro.BaseRO;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by shengchaojie on 2017/8/15.
 */
@Table(name = "upload_info")
public class UploadInfoRo extends BaseRO{

    private String originName;

    private String url;

    private Date uploadTime;

    private String uploadPerson;

    private String description;

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUploadPerson() {
        return uploadPerson;
    }

    public void setUploadPerson(String uploadPerson) {
        this.uploadPerson = uploadPerson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
