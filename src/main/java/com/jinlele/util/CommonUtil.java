package com.jinlele.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/20 0020.
 * 公共工具类
 */
public class CommonUtil {

    /**
     * 去除分页map里面无效数据
     */
    public static Map<String,Object> removePaingMap(Map map){
        Map<String,Object> newMap=new HashMap();
        map.remove("tableName");
        map.remove("fields");
        map.remove("wherecase");
        map.remove("orderField");
        map.remove("orderFlag");
        newMap=map;
        return newMap;
    }
}
