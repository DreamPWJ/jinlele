package com.jinlele.service.services;

import com.jinlele.dao.BaseMapper;
import com.jinlele.dao.PictureMapper;
import com.jinlele.dao.ServiceMapper;
import com.jinlele.dao.ServicePictureMapper;
import com.jinlele.model.Picture;
import com.jinlele.model.Service;
import com.jinlele.model.ServicePicture;
import com.jinlele.service.interfaces.IServiceService;
import com.jinlele.util.qiniuUtils.QiniuParamter;
import com.jinlele.util.qiniuUtils.QiniuUtil;
import com.jinlele.util.weixinUtils.util.AdvancedUtil;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 5大服务业务类Service
 */
@org.springframework.stereotype.Service
public class ServiceServiceImpl implements IServiceService {

    @Resource
    BaseMapper baseMapper;

    @Resource
    ServiceMapper serviceMapper;

    @Resource
    PictureMapper pictureMapper;

    @Resource
    ServicePictureMapper servicePictureMapper;


    public static String key_suff =  "service/";
    public static String savePath =  "c:/download";  //默认保存到服务器的该目录



    @Override
    public void saveService(Integer userId, Double price, String descrip,String type ,Integer storeId, String[] mediaIds) throws IOException {
        //循环下载媒体文件 上传到七牛 并返回 七牛的连接
        String filePath = null;
        String key = null;
        String imgurl = null;
        Picture picture = null;
        Service service = null;
        ServicePicture servicePicture = null;
        for(int i=0,len=mediaIds.length;i<len;i++){
            filePath = AdvancedUtil.getMedia(mediaIds[i] , savePath);
            key = key_suff + mediaIds[i];
            QiniuUtil.upload(filePath,key);
            //拼接七牛的路径
            imgurl = QiniuParamter.URL + key;
            //保存图片表
            picture = new Picture(imgurl , userId);
            pictureMapper.insertSelective(picture);
            //保存服务表
            service = new Service(price , userId , descrip , storeId); //暂时设定门店为1，以后会动态获取
            serviceMapper.insertSelective(service);
            //插入中间表
            servicePicture = new ServicePicture(service.getId(), picture.getId() , type);
            servicePictureMapper.insertSelective(servicePicture);
        }
    }


}