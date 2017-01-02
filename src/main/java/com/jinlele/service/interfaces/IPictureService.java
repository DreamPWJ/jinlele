package com.jinlele.service.interfaces;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016-12-28.
 */
public interface IPictureService {

    //保存图片集合
    List<String> saveURL(String[] mediaIds) throws IOException;
}
