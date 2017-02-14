package com.jinlele.service.interfaces;

import java.util.List;

/**
 * Created by Administrator on 2016-12-29.
 */
public interface IStoreService {

    List findAllStores(String latitude,String longitude);
}
