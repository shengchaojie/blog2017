package com.scj.web.controller;

import com.alibaba.fastjson.util.IOUtils;
import com.google.common.collect.Sets;
import com.scj.common.Page;
import com.scj.common.ResponseResult;
import com.scj.common.exception.StatusCode;
import com.scj.dal.ro.upload.UploadInfoRo;
import com.scj.service.upload.UploadService;
import com.scj.web.query.UploadImageQuery;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.jws.WebResult;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * Created by shengchaojie on 2017/8/15.
 * 博客图层工具
 */
@RestController
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private static final Set<String> imgSuffix = Sets.newHashSet("jpg","gif","jpeg","bmp","png","ico");

    @Value("${upload.folder}")
    private String uploadFolder;

    @Value("${upload.url}")
    private String preUrl;

    @Resource
    private UploadService uploadService;

    @RequestMapping(value = "/img/upload",method = RequestMethod.POST)
    public ResponseResult<String> imgUpload(@RequestParam("file")MultipartFile file,@RequestParam("description")String description) {
        if(file.isEmpty()) {
            return new ResponseResult<>(StatusCode.UPLOAD_ERROR);
        }

        String fileName =file.getOriginalFilename();
        //得到文件后缀
        if(StringUtils.isEmpty(fileName)||!fileName.contains(".")){
            return new ResponseResult<>(StatusCode.UPLOAD_ERROR);
        }

        String suffix =fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        //判断是否是图片
        if(!imgSuffix.contains(suffix.toLowerCase())){
            return new ResponseResult<>(StatusCode.NOT_IMAGE_FORMAT);
        }

        String dateFormat = DateTime.now().toString("/yyyy/MM/dd");
        File desFolder =new File(uploadFolder+dateFormat);
        if(!desFolder.exists()){
            desFolder.mkdirs();
        }
        String relativePath = dateFormat+"/"+ UUID.randomUUID()+"."+suffix;
        String outputFileName =uploadFolder+relativePath;
        FileOutputStream fos = null;
        try {
            fos =new FileOutputStream(outputFileName);
            fos.write(file.getBytes());
        }catch (IOException ex){
            logger.error("上传图片出现io异常,{}",ex);
            return new ResponseResult<>(StatusCode.UPLOAD_ERROR);
        }finally {
            IOUtils.close(fos);
        }

        UploadInfoRo uploadInfoRo =new UploadInfoRo();
        uploadInfoRo.setUrl(preUrl+relativePath);
        uploadInfoRo.setDescription(description);
        uploadInfoRo.setOriginName(fileName);
        uploadInfoRo.setUploadTime(new Date());
        uploadInfoRo.setUploadPerson("scj");
        uploadService.addUploadInfo(uploadInfoRo);

        return new ResponseResult<>(StatusCode.OK,preUrl+relativePath);
    }

    @RequestMapping(value = "/img/uploadUrl",method = RequestMethod.POST)
    public ResponseResult<String> uploadImgUrl(@RequestParam("url")String urlString,@RequestParam("description") String description)  {

        String suffix =urlString.substring(urlString.lastIndexOf(".")+1,urlString.length());
        //判断是否是图片
        if(!imgSuffix.contains(suffix.toLowerCase())){
            return new ResponseResult<>(StatusCode.NOT_IMAGE_FORMAT);
        }
        String dateFormat = DateTime.now().toString("/yyyy/MM/dd");
        File desFolder =new File(uploadFolder+dateFormat);
        if(!desFolder.exists()){
            desFolder.mkdirs();
        }
        String relativePath = dateFormat+"/"+ UUID.randomUUID()+"."+suffix;
        String outputFileName =uploadFolder+relativePath;
        FileOutputStream fos = null;
        InputStream is =null;
        byte[] buffer = new byte[1024];
        try {
            URL url = new URL(urlString);
            is = url.openStream();
            fos =new FileOutputStream(outputFileName);
            int size =0;
            while ((size =is.read(buffer))>0){
                fos.write(buffer,0,size);
            }
        } catch (MalformedURLException e) {
            logger.error("url格式有问题",e);
            return new ResponseResult<>(StatusCode.NOT_IMAGE_FORMAT);
        } catch (IOException e) {
            logger.error("IO出现异常",e);
            return new ResponseResult<>(StatusCode.FAILED);
        }finally {
            IOUtils.close(fos);
            IOUtils.close(is);
        }

        UploadInfoRo uploadInfoRo =new UploadInfoRo();
        uploadInfoRo.setUrl(relativePath);
        uploadInfoRo.setDescription(description);
        uploadInfoRo.setOriginName(urlString);
        uploadInfoRo.setUploadTime(new Date());
        uploadInfoRo.setUploadPerson("scj");
        uploadService.addUploadInfo(uploadInfoRo);

        return new ResponseResult<>(StatusCode.OK,relativePath);
    }

    @RequestMapping(value = "/img/upload/list",method = RequestMethod.POST)
    public ResponseResult<Page<UploadInfoRo>> listImages(@RequestBody UploadImageQuery uploadImageQuery){
        return new ResponseResult<>(StatusCode.OK,new Page<>(uploadService.getUploadInfo(uploadImageQuery.getPage(),uploadImageQuery.getLimit(),uploadImageQuery.getDescription()),uploadService.count(uploadImageQuery.getDescription())));
    }
}
