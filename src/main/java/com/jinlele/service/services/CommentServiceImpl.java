package com.jinlele.service.services;

import com.jinlele.dao.*;
import com.jinlele.model.*;
import com.jinlele.service.interfaces.ICommentService;
import com.jinlele.service.interfaces.IPictureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/2.
 */
@Service
public class CommentServiceImpl implements ICommentService {

    @Resource
    IPictureService pictureService;

    @Resource
    PictureMapper pictureMapper;

    @Resource
    CommentMapper commentMapper;

    @Resource
    CommentPictureMapper commentPictureMapper;

    @Resource
    ShopOrderGoodMapper shopOrderGoodMapper;

    @Resource
    ShopOrderMapper shopOrderMapper;

    @Override
    public int createComment(List<Map<String,Object>> list) {
        try {
            //读取整体信息，一条总记录
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> allCommentInfo = list.get(i);
                String orderno = allCommentInfo.get("orderno").toString();
                Integer userId = Integer.valueOf(allCommentInfo.get("userId").toString());
                Integer descriplevel = Integer.valueOf(allCommentInfo.get("descriplevel").toString());
                //读取评论内容信息，多条
                List itemsinfo= (List) allCommentInfo.get("itemsinfo");
                for(int j=0;j<itemsinfo.size();j++){
                    Map<String, Object> itemsinfoMap =(HashMap) itemsinfo.get(i);
                    Integer gcid =Integer.valueOf(itemsinfoMap.get("gcid").toString());//商品子id
                    String content = itemsinfoMap.get("content").toString();//针对商品子id的评论内容
                    List mediaIds = (List) itemsinfoMap.get("mediaIds");//针对商品子id的媒体文件集合
                    if(content.length()==0){
                        return 0;
                    }
                    //添加评论
                    Comment comment = new Comment(userId, content);
                    commentMapper.insertSelective(comment);
                    //转换media数组
                    String[] strings = new String[mediaIds.size()];
                    for (int k = 0; k < mediaIds.size(); k++) {
                        strings[k] = mediaIds.get(k).toString();
                    }
                    if(strings.length!=0) {
                       // 上传媒体文件
                        List<String> urls = pictureService.saveURL(strings);
                        for (int m = 0; m < urls.size(); m++) {
                            //添加图片
                            Picture pic = new Picture(urls.get(i), userId);
                            pictureMapper.insertSelective(pic);
                            //添加评论图片中间表
                            CommentPicture cp = new CommentPicture(comment.getId(), pic.getId());
                            commentPictureMapper.insertSelective(cp);
                        }
                    }
                    //修改订单商品中间表，添加评论id
                    ShopOrderGood shopOrderGood = new ShopOrderGood();
                    shopOrderGood.setCommentId(comment.getId());
                    shopOrderGood.setGoodchildId(gcid);
                    shopOrderGood.setShoporderNo(orderno);
                    shopOrderGoodMapper.updateByPrimaryKeySelective(shopOrderGood);
                    //修改订单，增加描述等级
                    ShopOrder shopOrder = new ShopOrder();
                    shopOrder.setDescriplevel(descriplevel);
                    shopOrder.setShoporderstatuscode("007");
                    shopOrder.setOrderno(orderno);
                    shopOrderMapper.updateByPrimaryKeySelective(shopOrder);
                }
            }
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }
}
