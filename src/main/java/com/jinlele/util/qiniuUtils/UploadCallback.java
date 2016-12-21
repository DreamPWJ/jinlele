package com.jinlele.util.qiniuUtils;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import java.io.IOException;

/**
 * 上传回调
 */
public class UploadCallback {


        //设置好账号的ACCESS_KEY和SECRET_KEY
        String ACCESS_KEY = QiniuParamter.ACCESS_KEY;
        String SECRET_KEY = QiniuParamter.SECRET_KEY;
        //要上传的空间
        String bucketname = QiniuParamter.BUCKETNAME;
        //上传到七牛后保存的文件名
        //String key = "my-java.png";
        String key = "shop/1.png";
        //上传文件的路径
        String FilePath = "G:\\1.jpg";

        //密钥配置
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

        ///////////////////////指定上传的Zone的信息//////////////////
        //第一种方式: 指定具体的要上传的zone
        //注：该具体指定的方式和以下自动识别的方式选择其一即可
        //要上传的空间(bucket)的存储区域为华东时
        // Zone z = Zone.zone0();
        //要上传的空间(bucket)的存储区域为华北时
        // Zone z = Zone.zone1();
        //要上传的空间(bucket)的存储区域为华南时
        // Zone z = Zone.zone2();

        //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);

        //创建上传对象
        UploadManager uploadManager = new UploadManager(c);

        //设置callbackUrl以及callbackBody,七牛将文件名和文件大小回调给业务服务器
        public String getUpToken(){
            return auth.uploadToken(bucketname,null,3600,new StringMap()
                    .put("callbackUrl","http://139.224.54.94/callback")
                    .put("callbackBody", "filename=$(fname)&filesize=$(fsize)"));
        }

        public void upload() throws IOException {
            try {
                //调用put方法上传
                Response res = uploadManager.put(FilePath, null, getUpToken());
                //打印返回的信息
                System.out.println(res.bodyString());
            } catch (QiniuException e) {
                Response r = e.response;
                // 请求失败时打印的异常的信息
                System.out.println(r.toString());
                try {
                    //响应的文本信息
                    System.out.println(r.bodyString());
                } catch (QiniuException e1) {
                    //ignore
                }
            }
        }

        public static void main(String args[]) throws IOException{
            new UploadCallback().upload();
        }


}
