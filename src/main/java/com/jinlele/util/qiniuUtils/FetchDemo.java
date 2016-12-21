package com.jinlele.util.qiniuUtils;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;


/**
 * 抓取文件
 * 意思是找到空间的一个图片 然后保存到指定的文件夹(key)中
 *
 */
public class FetchDemo {
    public static void main(String args[]){
        //设置需要操作的账号的AK和SK
        String ACCESS_KEY = QiniuParamter.ACCESS_KEY;
        String SECRET_KEY = QiniuParamter.SECRET_KEY;
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

        Zone z = Zone.zone2();
        Configuration c = new Configuration(z);

        //实例化一个BucketManager对象
        BucketManager bucketManager = new BucketManager(auth,c);

        //文件保存的空间名和文件名
        String bucket = QiniuParamter.BUCKETNAME;
        String key = "05.png";

        //要fetch的url
        String url = "http://oi2k72ijx.bkt.clouddn.com/ceshi/1.png?e=1481685893&token=cHEZPoh-k4iwKXcVXxT0n2Tub1CXUKcJnF-qA9_6:RfGvTwxMR7G3qfvWeFchE9Yq4Ag";

        try {
            //调用fetch方法抓取文件
            bucketManager.fetch(url, bucket,key);
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            System.out.println(r.toString());
        }
    }
}
