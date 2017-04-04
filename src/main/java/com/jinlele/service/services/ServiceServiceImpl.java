package com.jinlele.service.services;

import com.jinlele.dao.BaseMapper;
import com.jinlele.dao.PictureMapper;
import com.jinlele.dao.ServiceMapper;
import com.jinlele.dao.ServicePictureMapper;
import com.jinlele.model.Picture;
import com.jinlele.model.Service;
import com.jinlele.model.ServicePicture;
import com.jinlele.service.interfaces.IServiceService;
import com.jinlele.util.CommonUtil;
import com.jinlele.util.StringHelper;
import com.jinlele.util.SysConstants;
import com.jinlele.util.qiniuUtils.QiniuParamter;
import com.jinlele.util.qiniuUtils.QiniuUtil;
import com.jinlele.util.weixinUtils.util.AdvancedUtil;
import org.apache.commons.collections.map.HashedMap;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 5大服务业务类Service
 */
@org.springframework.stereotype.Service
public class ServiceServiceImpl implements IServiceService {

    public static String key_suff =  "service/";
    public static String savePath =  "c:/download";  //默认保存到服务器的该目录
    @Resource
    BaseMapper baseMapper;
    @Resource
    ServiceMapper serviceMapper;
    @Resource
    PictureMapper pictureMapper;
    @Resource
    ServicePictureMapper servicePictureMapper;

    @Override
    public Map<String , Object> saveService(Integer userId, Double totalprice, String type ,String[] mediaIds) throws IOException {
        //循环下载媒体文件 上传到七牛 并返回 七牛的连接
        String filePath = null;
        String key = null;
        String imgurl = null;
        Picture picture = null;
        //保存服务表
        Service service = new Service();
        service.setPrice(totalprice);//服务订单总金额
        serviceMapper.insertSelective(service);
        Map<String , Object> map = new HashedMap();
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
            //插入中间表
            servicePicture = new ServicePicture(service.getId(), picture.getId() , type);
            servicePictureMapper.insertSelective(servicePicture);
            //删除服务器上的该文件
            StringHelper.deleteFile(filePath);
        }
        map.put("serviceId" , service.getId());
        return map;
    }

    @Override
    public Map<String, Object> getrefurbishPrice() {
        return serviceMapper.getrefurbishPrice();
    }

    @Override
    public Map<String, Object> getdetectPrice() {
        return serviceMapper.getdetectPrice();
    }

    @Override
    public Map<String, Object> getServiceInfo(String orderno) {
        return serviceMapper.getServiceInfo(orderno);
    }

    @Override
    public Map<String, Object> getDictInfo(String typename) {
        Map<String , Object> map = new HashedMap();
        map.put("selectedItems",serviceMapper.getDictInfo(typename));
        return map;
    }

    @Override
    public Map<String, Object> getBarterListPaging(double amount, int pagenow, String type) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("tableName", "  (SELECT min(price) as price,id,good_id FROM goodchild group by good_id) as gc,good g  ");
        paramMap.put("fields", "  g.id,gc.price,gc.id childId,g.title,g.bannerurl ");
        paramMap.put("pageNow", pagenow);
        paramMap.put("pageSize", 4);
        switch (type){
            case "free":
                paramMap.put("wherecase", " g.id = gc.good_id and  g.canchange = 0 and gc.price <=" + amount);
                break;
            case "new":
                paramMap.put("wherecase", " g.id = gc.good_id and  g.canchange = 0 and gc.price >" + amount);
                break;
        }
        paramMap.put("orderField", "  g.create_time ");
        paramMap.put("orderFlag", 1);
        this.baseMapper.getPaging(paramMap);
        paramMap.put("pagingList", this.baseMapper.getPaging(paramMap));
        return CommonUtil.removePaingMap(paramMap);
    }
}
