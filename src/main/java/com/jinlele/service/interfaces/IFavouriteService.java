package com.jinlele.service.interfaces;
import com.jinlele.model.Favourite;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-1-3.
 */
public interface IFavouriteService {

    int deleteByPrimaryKey(Integer id);

    int insertSelective(Favourite record);

    List<Map<String , Object>> selectByuserIdAndGoodId(Favourite favourite);

}
