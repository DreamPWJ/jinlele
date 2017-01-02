package com.jinlele.service.services;

import com.jinlele.dao.*;
import com.jinlele.model.*;
import com.jinlele.service.interfaces.ICommentService;
import com.jinlele.service.interfaces.IPictureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Override
    public int createComment(List<Map<String,Object>> list) {
        try {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                Integer gcid = Integer.valueOf(map.get("gcid").toString());
                String orderno = map.get("orderno").toString();
                Integer userId = Integer.valueOf(map.get("userId").toString());
                String content = map.get("content").toString();
                List mediaIds = (List) map.get("mediaIds");
                String[] strings = new String[mediaIds.size()];
                for (int j = 0; j < mediaIds.size(); j++) {
                    strings[j] = mediaIds.get(i).toString();
                }
                List<String> urls = pictureService.saveURL(strings);
                //添加评论
                Comment comment = new Comment(userId, content);
                commentMapper.insertSelective(comment);
                for (int m = 0; m < urls.size(); m++) {
                    //添加图片
                    Picture pic = new Picture(urls.get(i), userId);
                    pictureMapper.insertSelective(pic);
                    //添加评论图片中间表
                    CommentPicture cp = new CommentPicture(comment.getId(), pic.getId());
                    commentPictureMapper.insertSelective(cp);
                }
                //修改订单，添加评论id
                ShopOrderGood shopOrderGood = new ShopOrderGood();
                shopOrderGood.setCommentId(comment.getId());
                shopOrderGood.setGoodchildId(gcid);
                shopOrderGood.setShoporderNo(orderno);
                shopOrderGoodMapper.updateByPrimaryKeySelective(shopOrderGood);
            }
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }
}
