package com.jinlele.service.services;
import com.jinlele.dao.StoreMapper;
import com.jinlele.service.interfaces.IStoreService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2016-12-29.
 */
@Service
public class StoreServiceImpl implements IStoreService {

    @Resource
    private StoreMapper storeMapper;

    @Override
    public List findAllStores() {
        return storeMapper.findAll();
    }
}
