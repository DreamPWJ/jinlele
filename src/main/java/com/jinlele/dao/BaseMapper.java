package com.jinlele.dao;

import java.util.List;
import java.util.Map;

/**
 * @author pwj on 2016/8/20
 *         公共的Dao接口类  定义常用方法实现代码复用
 */
public interface BaseMapper<Entity> {

    /**
     * 公用分页存储过程方法
     * map存储过程传入的参数
     */
     List getPaging(Map map);


}
