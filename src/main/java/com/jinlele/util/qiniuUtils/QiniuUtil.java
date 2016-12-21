package com.jinlele.util.qiniuUtils;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

import java.io.IOException;

/**
 * 七牛上传工具类
 */
public class QiniuUtil {

    public  final  static    String ACCESS_KEY = QiniuParamter.ACCESS_KEY;        //设置好账号的ACCESS_KEY
    public  final   static   String  SECRET_KEY = QiniuParamter.SECRET_KEY;        //SECRET_KEY
    public  final   static   String bucketname = QiniuParamter.BUCKETNAME;         //要上传的空间
    public  final   static   String URL = QiniuParamter.URL;  //公有空间/外链网址


    //授权
    public static Auth getAuth(){
        return Auth.create(ACCESS_KEY ,  SECRET_KEY);
    }

    //得到上传密钥
    public static String getUpToken() {
        //密钥配置
        Auth auth = getAuth();
        return auth.uploadToken(bucketname);
    }

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public static void upload(String FilePath , String key) throws IOException {
        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);
        //创建上传对象
        UploadManager uploadManager = new UploadManager(c);
        try {
            //调用put方法上传
            Response res = uploadManager.put(FilePath, key, getUpToken());
            //打印返回的信息
            System.out.println(res.bodyString());
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                //响应的文本信息
                System.out.println(r.bodyString());
            } catch (Exception e1) {
                //ignore
            }
        }
    }


    //拼接下载路径
    public static String  download(String key){
        Auth auth = getAuth();
        //调用privateDownloadUrl方法生成下载链接,第二个参数可以设置Token的过期时间
        String downloadRUL = auth.privateDownloadUrl(URL+key, 3600);
        return downloadRUL;
    }


    public static void main(String args[]) throws IOException {
        //测试普通上传文件
        //QiniuUtil.upload("G:\\1.jpg" ,"jin/1217");
        //测试 download url
        String url = download("jin/1217");
        System.out.println(url);
    }





}
