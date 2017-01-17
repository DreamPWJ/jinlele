package com.jinlele.service.services;

import com.jinlele.dao.BaseMapper;
import com.jinlele.dao.PictureMapper;
import com.jinlele.model.Picture;
import com.jinlele.model.ServicePicture;
import com.jinlele.service.interfaces.IPictureService;
import com.jinlele.util.StringHelper;
import com.jinlele.util.qiniuUtils.QiniuParamter;
import com.jinlele.util.qiniuUtils.QiniuUtil;
import com.jinlele.util.weixinUtils.util.AdvancedUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-28.
 */
@Service
public class PictureServiceImpl implements IPictureService{

    public static String key_suff =  "comment/";//七牛上评论所使用目录
    public static String savePath =  "c:/download";  //默认保存到服务器的该目录
    @Resource
    BaseMapper baseMapper;
    @Resource
    PictureMapper pictureMapper;

    @Override
    public List<String> saveURL(String[] mediaIds) throws IOException {
        //循环下载媒体文件 上传到七牛 并返回 七牛的连接
        String filePath = null;
        String key = null;
        String imgurl = null;
        List<String> list=new ArrayList<>();
        for(int i=0,len=mediaIds.length;i<len;i++){
            filePath = AdvancedUtil.getMedia(mediaIds[i], savePath);
            key = key_suff + mediaIds[i];
            QiniuUtil.upload(filePath, key);
            //拼接七牛的路径
            imgurl = QiniuParamter.URL + key;
            list.add(imgurl);
            //删除服务器上的该文件
            StringHelper.deleteFile(filePath);
        }
        return list;
    }
}
