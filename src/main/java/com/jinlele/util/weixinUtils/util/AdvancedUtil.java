package com.jinlele.util.weixinUtils.util;


import com.jinlele.model.User;
import com.jinlele.util.ChangeCharsetUtil;
import com.jinlele.util.weixinUtils.message.resp.Article;
import com.jinlele.util.weixinUtils.message.resp.Music;
import com.jinlele.util.weixinUtils.vo.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 高级接口工具类
 */
public class AdvancedUtil {

    private static Logger log =  LoggerFactory.getLogger(WeiXinUtil.class);

    /**
     *获取用户信息
     */
    public static User getUserInfo(String accessToken , String openId){
        User userInfo = null;
        //拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID",openId);
        //获取用户信息
        JSONObject jsonObject = WeiXinUtil.httpsRequset(requestUrl ,"GET" ,null);
        if(null != jsonObject){
            try{
                userInfo = new User();
                // 用户的标识，对当前公众号唯一
                if(jsonObject.getString("openid")!=null || !"".equals(jsonObject.getString("openid"))) {
                    String openid = ChangeCharsetUtil.changeCharset(jsonObject.getString("openid"),"gbk","UTF-8");
                    userInfo.setOpenid(openid);
                }
                //用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
                userInfo.setSubscribe(jsonObject.getInt("subscribe"));
                //    nickname	用户的昵称
                if(jsonObject.getString("nickname")!=null || !"".equals(jsonObject.getString("nickname"))) {
                    String nickname = ChangeCharsetUtil.changeCharset(jsonObject.getString("nickname"),"gbk","UTF-8");
                    userInfo.setNickname(nickname);
                }

                //    sex	用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
                userInfo.setSex(jsonObject.getInt("sex"));
                //    city	用户所在城市
                if(jsonObject.getString("city")!=null || !"".equals(jsonObject.getString("city"))) {
                    String city = ChangeCharsetUtil.changeCharset(jsonObject.getString("city"),"gbk","UTF-8");
                    userInfo.setCity(city);
                }
                //    country	用户所在国家
                if(jsonObject.getString("country")!=null || !"".equals(jsonObject.getString("country"))) {
                    String country = ChangeCharsetUtil.changeCharset(jsonObject.getString("country"),"gbk","UTF-8");
                    userInfo.setCountry(country);
                }
                //    province	用户所在省份
                if(jsonObject.getString("province")!=null || !"".equals(jsonObject.getString("province"))) {
                    String province = ChangeCharsetUtil.changeCharset(jsonObject.getString("province"),"gbk","UTF-8");
                    userInfo.setProvince(province);
                }
                //  language	用户的语言，简体中文为zh_CN
                if(jsonObject.getString("language")!=null || !"".equals(jsonObject.getString("language"))) {
                    String language = ChangeCharsetUtil.changeCharset(jsonObject.getString("language"),"gbk","UTF-8");
                    userInfo.setLanguage(language);
                }
                //    headimgurl	用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
                userInfo.setHeadimgurl(jsonObject.getString("headimgurl"));
                if(jsonObject.getString("headimgurl")!=null || !"".equals(jsonObject.getString("headimgurl"))) {
                    String headimgurl = ChangeCharsetUtil.changeCharset(jsonObject.getString("headimgurl"),"gbk","UTF-8");
                    userInfo.setHeadimgurl(headimgurl);
                }
                //    subscribe_time	用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间userInfo.setSubscribetime(jsonObject.getString("subscribe_time"));
//
                if(jsonObject.getString("subscribe_time")!=null || !"".equals(jsonObject.getString("subscribe_time"))) {
                    String subscribe_time = ChangeCharsetUtil.changeCharset(jsonObject.getString("subscribe_time"),"gbk","UTF-8");
                    userInfo.setSubscribetime(subscribe_time);
                }
                //    unionid	只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。详见：获取用户个人信息（UnionID机制）
//                if(null!=jsonObject.getString("unionid")) {
//                    userInfo.setUnionid(jsonObject.getString("unionid"));
//                }
                //    remark	公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注
                if(jsonObject.getString("remark")!=null || !"".equals(jsonObject.getString("remark"))) {
                    String remark = ChangeCharsetUtil.changeCharset(jsonObject.getString("remark"),"gbk","UTF-8");
                    userInfo.setRemark(remark);
                }
                //    groupid	用户所在的分组ID
                if(jsonObject.getString("groupid")!=null || !"".equals(jsonObject.getString("groupid"))) {
                    String groupid = ChangeCharsetUtil.changeCharset(jsonObject.getString("groupid"),"gbk","UTF-8");
                    userInfo.setGroupid(groupid);
                }
            }catch (Exception e){
                if(0 == userInfo.getSubscribe()){
                    //log.error("用户{{}已取消关注" ,userInfo.getOpenid()) ;
                    System.out.println("用户{{}已取消关注" +userInfo.getOpenid());
                }else{
                    int errorCode = jsonObject.getInt("errcode");
                    String errMsg = jsonObject.getString("errmsg");
                    //log.error("获取用户信息失败errorcode:{} errmsg：{}",errorCode,errMsg);
                    System.out.println("获取用户信息失败errorcode:{} errmsg：{}"+errorCode+"===="+errMsg);
                }
            }
        }
        return userInfo;
    }


    /**
     * 获取关注者列表
     */
    public static UserList getUserList(String accessToken , String nextOpenId){
        UserList userList = null;
        if(null == nextOpenId){
            nextOpenId = "";
        }
        //拼接请求url
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";
        requestUrl = requestUrl.replace("ACCESS_TOKEN" ,accessToken).replace("NEXT_OPENID",nextOpenId);
        //获取用户列表
        JSONObject jsonObject = WeiXinUtil.httpsRequset(requestUrl ,"GET" ,null);
//        total	关注该公众账号的总用户数
//        	拉取的OPENID个数，最大值为10000
//        data	列表数据，OPENID的列表
//        next_openid	拉取列表的最后一个用户的OPENID
        if(null != jsonObject){
            try{
                userList = new UserList();
                //  	关注该公众账号的总用户数
                userList.setTotal(jsonObject.getInt("total"));
                //	拉取的OPENID个数，最大值为10000
                userList.setCount(jsonObject.getInt("count"));
                //    nickname	用户的昵称
                userList.setNextOpenId(jsonObject.getString("next_openid"));
                JSONObject dataObject = (JSONObject) jsonObject.get("data");
                //列表数据，OPENID的列表
                userList.setOpenIdList(JSONArray.toList(dataObject.getJSONArray("openid")));
            }catch (Exception e){
                userList = null;
                int errorCode = jsonObject.getInt("errcode");
                String errMsg = jsonObject.getString("errmsg");
                //log.error("获取用户列表失败errorcode:{} errmsg：{}",errorCode,errMsg);
                System.out.println("获取用户列表失败errorcode:{} errmsg：{}"+errorCode+"===="+errMsg);

            }
        }

        return userList;
    }

    /**
     * 查询分组
     * @param accessToken
     * @return
     */
    public static List<WeiXinGroup> getGroups(String accessToken){
        List<WeiXinGroup> weiXinGroupList = null;
        //拼接请求url
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
        //获取用户列表
        JSONObject jsonObject = WeiXinUtil.httpsRequset(requestUrl ,"GET" ,null);
        if(null != jsonObject){
            try{
                weiXinGroupList =  JSONArray.toList(jsonObject.getJSONArray("groups"), WeiXinGroup.class);
                System.out.println(weiXinGroupList);
            }catch (Exception e){
                weiXinGroupList = null;
                int errorCode = jsonObject.getInt("errcode");
                String errMsg = jsonObject.getString("errmsg");
                //log.error("获取分组失败errorcode:{} errmsg：{}",errorCode,errMsg);
                System.out.println("获取分组失败errorcode:{} errmsg：{}"+errorCode+"===="+errMsg);

            }
        }

        return weiXinGroupList;

    }

    /**
     * 创建分组
     * @param
     */
    public static WeiXinGroup createGroups(String accessToken ,String name){
        WeiXinGroup weixingroup =  null;
        //拼接请求url
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
        Map map = new HashMap();
        Map mapGroup = new HashMap();
        map.put("name" ,name);
        mapGroup.put("group" ,map);
        JSONObject jsonGroup = JSONObject.fromObject(mapGroup);
        System.out.println(jsonGroup);
        //获取用户列表
        JSONObject jsonObject = WeiXinUtil.httpsRequset(requestUrl ,"POST" ,jsonGroup.toString());
        if(null != jsonObject){
            try{
                weixingroup = new WeiXinGroup();
                JSONObject dataObject =  jsonObject.getJSONObject("group");
                //分组id
                weixingroup.setId(dataObject.getInt("id"));
                //分组名称
                weixingroup.setName(dataObject.getString("name"));

            }catch (Exception e){
                weixingroup = null;
                int errorCode = jsonObject.getInt("errcode");
                String errMsg = jsonObject.getString("errmsg");
                //log.error("创建分组失败errorcode:{} errmsg：{}",errorCode,errMsg);
                System.out.println("创建分组失败errorcode:{} errmsg：{}"+errorCode+"===="+errMsg);
            }
        }
        return weixingroup;
    }

    /**
     *修改分组名
     */
    public static   boolean  updateGroup(String accessToken , int id , String name){
        boolean result = false;
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
        Map map = new HashMap();
        Map mapGroup = new HashMap();
        map.put("name" ,name);
        map.put("id" ,id);
        mapGroup.put("group" ,map);
        JSONObject jsonGroup = JSONObject.fromObject(mapGroup);
        System.out.println(jsonGroup);
        //获取用户列表
        JSONObject jsonObject = WeiXinUtil.httpsRequset(requestUrl ,"POST" ,jsonGroup.toString());
        if (null != jsonObject){
            int errorCode = jsonObject.getInt("errcode");
            String errorMsg = jsonObject.getString("errmsg");
            if(0 == errorCode){
                result = true;
                System.out.println("分组名修改成功");
            }else {
                result = false;
                //log.error("删除菜单失败errCode：{} errmsg:{}" ,errorCode ,errorMsg);
                System.out.println("修改分组名失败errCode：{} errmsg:{}" +errorCode +"===="+errorMsg);
            }
        }
        return  result;
    }

    /**
     * 移动用户分组
     */
    public static   boolean  updateMemberGroup(String accessToken , String openid , int to_groupid){
        boolean result = false;
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
        Map map = new HashMap();
        map.put("openid" ,openid);
        map.put("to_groupid" ,to_groupid);
        JSONObject jsonGroup = JSONObject.fromObject(map);
        System.out.println(jsonGroup);
        //获取用户列表
        JSONObject jsonObject = WeiXinUtil.httpsRequset(requestUrl ,"POST" ,jsonGroup.toString());
        if (null != jsonObject){
            int errorCode = jsonObject.getInt("errcode");
            String errorMsg = jsonObject.getString("errmsg");
            if(0 == errorCode){
                result = true;
                System.out.println("移动用户分组成功");
            }else {
                result = false;
                //log.error("删除菜单失败errCode：{} errmsg:{}" ,errorCode ,errorMsg);
                System.out.println("移动用户分组失败errCode：{} errmsg:{}" +errorCode +"===="+errorMsg);
            }
        }
        return  result;
    }

    /**
     * 批量移动用户分组 （测试号无权限）
     */
    public static   boolean  updateMoreMemberGroup(String accessToken ,String[]  openid_list, int to_groupid){
        boolean result = false;
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
        Map map = new HashMap();
        map.put("openid_list" ,openid_list);
        map.put("to_groupid" ,to_groupid);
        JSONObject jsonGroup = JSONObject.fromObject(map);
        System.out.println(jsonGroup);
        //获取用户列表
        JSONObject jsonObject = WeiXinUtil.httpsRequset(requestUrl ,"POST" ,jsonGroup.toString());
        if (null != jsonObject){
            int errorCode = jsonObject.getInt("errcode");
            String errorMsg = jsonObject.getString("errmsg");
            if(0 == errorCode){
                result = true;
                System.out.println("批量移动用户分组成功");
            }else {
                result = false;
                //log.error("删除菜单失败errCode：{} errmsg:{}" ,errorCode ,errorMsg);
                System.out.println("批量移动用户分组失败errCode：{} errmsg:{}" +errorCode +"===="+errorMsg);
            }
        }
        return  result;
    }

    /**
     * 上传多媒体文件
     */
    public static WeiXinMedia uploadMedia(String accessToken , String type , String mediaFileUrl) {
        WeiXinMedia weixinMedia = null;

        String uploadMediaUrl =  "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
        uploadMediaUrl = uploadMediaUrl.replace("ACCESS_TOKEN", accessToken).replace("TYPE",type);
        //定义数据分隔符
        String boundary = "---------------7da2e536604c8";

        try {
            URL uploadUrl = new URL(uploadMediaUrl);
            HttpURLConnection uploadConn = (HttpURLConnection) uploadUrl.openConnection();

            uploadConn.setDoOutput(true);
            uploadConn.setDoInput(true);
            uploadConn.setRequestMethod("POST");

            //设置请求头 Content-type
            uploadConn.setRequestProperty("Content-Type" , " multipart/form-data; boundary=" + boundary);

            //获取媒体文件上传的输出流(往微信服务器写数据)
            OutputStream outputStream = uploadConn.getOutputStream();

            URL mediaUrl = new URL(mediaFileUrl);
            HttpURLConnection mediaConn = (HttpURLConnection) mediaUrl.openConnection();
            mediaConn.setDoOutput(true);
            mediaConn.setRequestMethod("GET");

            //从请求头中获取内容类型
            String contentType = mediaConn.getHeaderField("Content-Type");
            //根据内容类型判断文件扩展名
            String fileExt = WeiXinUtil.getFileExt(contentType);
            //请求体开始
            outputStream.write(("--" + boundary + "\r\n").getBytes());
            outputStream.write(String.format("Content-Disposition: form-data;name = \"media\" ;filename=\"file1%s\"\r\n",fileExt).getBytes());
            outputStream.write(String.format("Content-Type: %s\r\n\r\n" ,contentType).getBytes());

            //获取媒体文件的输入流(读取文件)
            BufferedInputStream bis = new BufferedInputStream(mediaConn.getInputStream());
            byte[] buf = new byte[8096];
            int size = 0;
            while((size = bis.read(buf)) != -1){
                //将媒体文件写到输出流(往微信服务器写数据)
                outputStream.write(buf , 0 ,size);
            }
            //请求体结束
            outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
            outputStream.close();
            bis.close();
            mediaConn.disconnect();

            //获取媒体文件上传的输入流(从微信服务器读数据)
            InputStream inputStream = uploadConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream ,"utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer buffer = new StringBuffer();
            String str = null;
            while((str = bufferedReader.readLine()) != null){
                buffer.append(str);
            }
            //释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            uploadConn.disconnect();

            //使用JSON-lib解析返回结果
            JSONObject jsonObject = JSONObject.fromObject(buffer.toString());
            weixinMedia = new WeiXinMedia();
            weixinMedia.setType(jsonObject.getString("type"));
            //type等于thumb时的返回结果和其他类型不一样
            if("thumb".equals(type)){
                weixinMedia.setMediaId(jsonObject.getString("thumb_media_id"));
            }else{
                weixinMedia.setMediaId(jsonObject.getString("media_id"));
            }
            weixinMedia.setCreatedAt(jsonObject.getInt("created_at"));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            weixinMedia = null;
            //log.error("上传媒体文件失败:{}" ,e);
            System.out.println("上传媒体文件失败:{}"+ e);
        }
        return  weixinMedia;
    }


    /**
     * 下载媒体文件
     */
    public static String getMedia(String accessToken , String mediaId ,String savePath ){
        String filePath = null;
        String requestUrl =  "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID",mediaId);

        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            File file = new File(savePath);
            if(!file.isDirectory()){
                file.mkdirs();
            }
            if(!savePath.endsWith("/")){
                savePath += "/";
            }
            //从请求头中获取内容类型
            String contentType = conn.getHeaderField("Content-Type");
            //根据内容类型判断文件扩展名
            String fileExt = WeiXinUtil.getFileExt(contentType);
            //将mediaId作为文件名
            filePath = savePath + mediaId + fileExt;
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            byte[] buf = new byte[8096];
            int size = 0;
            while((size = bis.read(buf)) != -1){
                fos.write(buf ,  0, size);
            }
            fos.close();
            bis.close();

            conn.disconnect();
            //log.info("下载媒体文件成功，filePath=" + filePath);
            System.out.println("下载媒体文件成功，filePath=" + filePath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    /**
     * 创建临时带参二维码
     */
    public static WeixinQRCode createTemporaryQRCode(String accessToken , int expireSeconds , int sceneId){
        WeixinQRCode weixinQRCode = null;
        //拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
        requestUrl = requestUrl.replace("TOKEN",accessToken);
        //需要提交的json数据
        String jsonMsg = "{\"expire_seconds\": %d, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": %d}}}";
        //创建临时带参二维码
        JSONObject jsonObject = WeiXinUtil.httpsRequset(requestUrl , "POST" , String.format(jsonMsg,expireSeconds , sceneId));
        if (null != jsonObject){
            try{
                weixinQRCode = new WeixinQRCode();
                weixinQRCode.setTicket(jsonObject.getString("ticket"));
                weixinQRCode.setExpireSeconds(jsonObject.getInt("expire_seconds"));
                //log.info("创建带参二维码成功 ticket:{} expire_seconds：{}" ,weixinQRCode.getTicket() , weixinQRCode.getExpireSeconds());
                System.out.println("创建临时带参二维码成功 ticket:{} expire_seconds：{}" +weixinQRCode.getTicket()+"===" + weixinQRCode.getExpireSeconds());
            }catch (Exception e){
                weixinQRCode = null;
                int errorCode = jsonObject.getInt("errcode");
                String errMsg = jsonObject.getString("errmsg");
                //log.error("创建临时带参二维码失败 errorcode:{} errmsg：{}",errorCode,errMsg);
                System.out.println("创建临时带参二维码失败 errorcode:{} errmsg：{}"+errorCode+"===="+errMsg);
            }


        }
        return  weixinQRCode;
    }

    /**
     * 创建永久带参二维码
     * @param accessToken   接口访问凭证
     * @param sceneId
     * @return
     */
    public static String createPermannentQRCode(String accessToken , int sceneId){
        String ticket = null;
        //拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
        requestUrl = requestUrl.replace("TOKEN",accessToken);
        //需要提交的json数据
        String jsonMsg = "{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": %d}}}";
        //创建临时带参二维码
        JSONObject jsonObject = WeiXinUtil.httpsRequset(requestUrl , "POST" , String.format(jsonMsg , sceneId));
        if (null != jsonObject){
            try{
                ticket = jsonObject.getString("ticket");
                //log.info("创建永久带参二维码成功 ticket:{} " ,ticket);
                System.out.println("创建永久带参二维码成功 ticket:{} expire_seconds：{}" +ticket);
            }catch (Exception e){
                int errorCode = jsonObject.getInt("errcode");
                String errMsg = jsonObject.getString("errmsg");
                //log.error("创建临时带参二维码失败 errorcode:{} errmsg：{}",errorCode,errMsg);
                System.out.println("创建永久带参二维码失败 errorcode:{} errmsg：{}"+errorCode+"===="+errMsg);
            }
        }
        return  ticket;
    }

    /**
     * 通过ticket换取二维码
     */
    public static String getQRCode(String ticket , String savePath){
        String filePath = null;
        String requestUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
        requestUrl = requestUrl.replace("TICKET", WeiXinUtil.urlEncodeUTF8(ticket));
        try{
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");

            if(!savePath.endsWith("/")){
                savePath += "/";
            }

            //将ticket作为文件名
            filePath = savePath + ticket + ".jpg";

            //将微信服务器返回的输入流写入文件
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            byte[] buf = new byte[8096];
            int size = 0;
            while((size = bis.read(buf))!= -1){
                fos.write(buf , 0 , size);
            }
            fos.close();
            bis.close();
            conn.disconnect();
            //log.info("根据ticket换取二维码成功,filePath="+filePath);
            System.out.println("根据ticket换取二维码成功,filePath="+filePath);
        }catch (Exception e){
            filePath = null;
            //log.error("根据ticket换取二维码失败,filePath="+filePath);
            System.out.println("根据ticket换取二维码失败,filePath="+filePath);
        }
        return  filePath;
    }


    /**
     * 长链接转短链接
     * @param accessToken   接口访问凭证
     * @param longUrl    长链接
     * @return
     */
    public static String longUrl2shortUrl(String accessToken , String longUrl){
        String shortUrl = null;
        //拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN",accessToken);
        //需要提交的json数据
        String jsonMsg = "{\"action\":\"long2short\",\"long_url\":\"%s\"}";
        //创建临时带参二维码
        JSONObject jsonObject = WeiXinUtil.httpsRequset(requestUrl , "POST" , String.format(jsonMsg , longUrl));
        if (null != jsonObject){
            try{
                shortUrl = jsonObject.getString("short_url");
                //log.info("创建永久带参二维码成功 ticket:{} " ,ticket);
                System.out.println("长链接转短链接成功 短连接为:{}" +shortUrl);
            }catch (Exception e){
                int errorCode = jsonObject.getInt("errcode");
                String errMsg = jsonObject.getString("errmsg");
                //log.error("创建临时带参二维码失败 errorcode:{} errmsg：{}",errorCode,errMsg);
                System.out.println("长链接转短链接失败 errorcode:{} errmsg：{}"+errorCode+"===="+errMsg);
            }
        }
        return  shortUrl;
    }

    /**
     * 添加客服账号
     */
    public static  boolean  createCustomer(String accessToken ,String account ,String weiWinHao , String nickname ,String password) throws NoSuchAlgorithmException {
        boolean flag = false;
        //拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/customservice/kfaccount/add?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN",accessToken);
        //需要提交的json数据
        String jsonMsg = "{\"kf_account\" :\"%s@%s\",\"nickname\" :\"%s\",\"password\" :\"%s\"}";
        //创建临时带参二维码
        String json =  String.format(jsonMsg, account, weiWinHao, nickname, WeiXinUtil.md5(password));
        JSONObject jsonObject = WeiXinUtil.httpsRequset(requestUrl , "POST" ,json);
        if (null != jsonObject){
            try{
                int errorCode = jsonObject.getInt("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                if(0 == errorCode){
                    flag = true;
                    System.out.println("添加客服账号成功");
                }else {
                    flag = false;
                    //log.error("删除菜单失败errCode：{} errmsg:{}" ,errorCode ,errorMsg);
                    System.out.println("添加客服账号失败errCode：{} errmsg:{}" +errorCode +"===="+errorMsg);
                }
            }catch (Exception e){
                int errorCode = jsonObject.getInt("errcode");
                String errMsg = jsonObject.getString("errmsg");
                //log.error("创建临时带参二维码失败 errorcode:{} errmsg：{}",errorCode,errMsg);
                System.out.println("添加客服账号失败 errorcode:{} errmsg：{}"+errorCode+"===="+errMsg);
            }
        }
        return  flag;
    }



    /**
     * 组装文本客服消息
     */
    public static String makeTextCustomMessage(String opendId , String content){
        //对消息内容中的双引号进行转义
        content = content.replaceAll("\"","\"");
        String jsonMsg = "{\"touser\":\"%s\", \"msgtype\":\"text\", \"text\": {\"content\":\"%s\"}}";
        return String.format(jsonMsg , opendId , content);
    }

    /**
     * 组装图片客服信息
     */
    public static String makeImageCustomMessage(String opendId , String mediaId){
        //对消息内容中的双引号进行转义
        String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"image\",\"image\": {\"media_id\":\"%s\"}}";
        return String.format(jsonMsg , opendId , mediaId);
    }

    /**
     * 组装语音客服信息
     */
    public static String makeVoiceCustomMessage(String opendId , String mediaId){
        //对消息内容中的双引号进行转义
        String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"voice\",\"image\": {\"media_id\":\"%s\"}}";
        return String.format(jsonMsg , opendId , mediaId);
    }

    /**
     * 组装视频客服信息
     */
    public static String makeVideoCustomMessage(String opendId , String mediaId , String thumbMediaId){
        //对消息内容中的双引号进行转义
        String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"video\",\"video\": { \"media_id\":\"%s\",\"thumb_media_id\":\"%s\",\"title\":\"TITLE\",\"description\":\"DESCRIPTION\"}}";
        return String.format(jsonMsg , opendId , mediaId ,thumbMediaId);
    }

    /**
     * 组装音乐客服信息
     */
    public static String makeMusicCustomMessage(String opendId , Music music){
        //对消息内容中的双引号进行转义
        String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"music\",\"music\":\"%s\"}}";
        jsonMsg =  String.format(jsonMsg , opendId , JSONObject.fromObject(music));
        jsonMsg.replace("ThumbMediaId","thumb_media_id");
        return jsonMsg;
    }

    /**
     * 组装图文信息
     */
    public static String makeNewsCustomMessage(String opendId , List<Article> arryNews){
        //对消息内容中的双引号进行转义
        String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"news\",\"news\":{\"articles\":%s}}";
        jsonMsg =  String.format(jsonMsg , opendId , JSONObject.fromObject(arryNews));
        jsonMsg.replace("PicUrl","picurl");
        return jsonMsg;
    }

    /**
     * 发送客服消息
     */
    public static  boolean  sendCustomerMessage(String accessToken ,String jsonMsg) throws NoSuchAlgorithmException {
        boolean flag = false;
        //拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN",accessToken);
        //需要提交的json数据
        //创建临时带参二维码
        JSONObject jsonObject = WeiXinUtil.httpsRequset(requestUrl , "POST" ,jsonMsg);
        if (null != jsonObject){
            try{
                int errorCode = jsonObject.getInt("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                if(0 == errorCode){
                    flag = true;
                    System.out.println("发送客服消息成功");
                }else {
                    flag = false;
                    //log.error("删除菜单失败errCode：{} errmsg:{}" ,errorCode ,errorMsg);
                    System.out.println("发送客服消息失败errCode：{} errmsg:{}" +errorCode +"===="+errorMsg);
                }
            }catch (Exception e){
                int errorCode = jsonObject.getInt("errcode");
                String errMsg = jsonObject.getString("errmsg");
                //log.error("创建临时带参二维码失败 errorcode:{} errmsg：{}",errorCode,errMsg);
                System.out.println("发送客服消息失败errCode:{} errmsg：{}"+errorCode+"===="+errMsg);
            }
        }
        return  flag;
    }

    /**
     * 获得网页授权
     * appid 公众号的唯一标示
     * appSecret 公众账号的密钥
     * code
     */
    public static  WeiXinOauth2Token getOauth2AccessToken(String appid ,String appsecret ,String code){
        WeiXinOauth2Token wat = null;
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        requestUrl = requestUrl.replace("APPID",appid).replace("SECRET",appsecret).replace("CODE",code);
        //获取网页授权凭证
        JSONObject jsonObject = WeiXinUtil.httpsRequset(requestUrl ,"GET" ,null);
        if(null!= jsonObject){
            try{
                wat = new WeiXinOauth2Token();
                wat.setAccessToken(jsonObject.getString("access_token"));
                wat.setExpiresIn(jsonObject.getInt("expires_in"));
                wat.setOpenId(jsonObject.getString("openid"));
                wat.setRefreshToken(jsonObject.getString("refresh_token"));
                wat.setScope(jsonObject.getString("scope"));
            }catch (Exception e){
                wat = null;
                int errorCode = jsonObject.getInt("errcode");
                String errMsg = jsonObject.getString("errmsg");
                //log.error("获得网页授权失败 errorcode:{} errmsg：{}",errorCode,errMsg);
                System.out.println("获得网页授权失败errCode:{} errmsg：{}"+errorCode+"===="+errMsg);
            }
        }
        return wat;

    }

    /**
     * 刷新网页凭证
     * @param appid
     * @param refreshToken
     * @return   WeiXinOauth2Token
     */
    public static  WeiXinOauth2Token refreshOauth2AccessToken(String appid ,String refreshToken){
        WeiXinOauth2Token wat = null;
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
        requestUrl = requestUrl.replace("APPID", appid).replace("REFRESH_TOKEN",refreshToken);
        //获取网页授权凭证
        JSONObject jsonObject = WeiXinUtil.httpsRequset(requestUrl ,"GET" ,null);
        if(null!= jsonObject){
            try{
                wat = new WeiXinOauth2Token();
                wat.setAccessToken(jsonObject.getString("access_token"));
                wat.setExpiresIn(jsonObject.getInt("expires_in"));
                wat.setOpenId(jsonObject.getString("openid"));
                wat.setRefreshToken(jsonObject.getString("refresh_token"));
                wat.setScope(jsonObject.getString("scope"));
            }catch (Exception e){
                wat = null;
                int errorCode = jsonObject.getInt("errcode");
                String errMsg = jsonObject.getString("errmsg");
                //log.error("刷新网页凭证失败 errorcode:{} errmsg：{}",errorCode,errMsg);
                System.out.println("发刷新网页凭证失败errCode:{} errmsg：{}"+errorCode+"===="+errMsg);
            }
        }
        return wat;
    }

    /**
     * 拉取用户信息(需scope为 snsapi_userinfo)
     */
    public static SNSuserInfo getsnsUserInfo(String refreshToken , String openid){
        SNSuserInfo snsUserInfo = null;
        String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", refreshToken).replace("OPENID",openid);
        //获取网页授权凭证
        JSONObject jsonObject = WeiXinUtil.httpsRequset(requestUrl ,"GET" ,null);
        System.out.println("jsonObject=="+jsonObject);
        if(null!= jsonObject){
            try{
                snsUserInfo = new  SNSuserInfo();
                snsUserInfo.setOpenid(jsonObject.getString("openid"));
                snsUserInfo.setNickname(jsonObject.getString("nickname"));
                snsUserInfo.setSex(jsonObject.getInt("sex"));
                snsUserInfo.setProvince(jsonObject.getString("province"));
                snsUserInfo.setCity(jsonObject.getString("city"));
                snsUserInfo.setCountry(URLDecoder.decode(jsonObject.getString("country"),"gbk"));
                snsUserInfo.setHeadimgurl(jsonObject.getString("headimgurl"));
//                JSONObject dataObject = (JSONObject) jsonObject.get("privilege");
//                //列表数据，OPENID的列表
//                userList.setOpenIdList(JSONArray.toList(dataObject.getJSONArray("openid")));
                // snsUserInfo.setPrivilegedList(JSONArray.toList(JSONArray.fromObject(jsonObject.getString("privilege"))));
            }catch (Exception e){
                int errorCode = jsonObject.getInt("errcode");
                String errMsg = jsonObject.getString("errmsg");
                //log.error("刷新网页凭证失败 errorcode:{} errmsg：{}",errorCode,errMsg);
                System.out.println("拉取用户信息失败errCode:{} errmsg：{}"+errorCode+"===="+errMsg);
            }
        }
        return snsUserInfo;
    }



    //测试 上传多媒体文件
    public static void uploadMedia(String type , String mediaurl) {
        String accessToken  = WeiXinUtil.getToken(Parameter.corId , Parameter.appsecret).getAccessToken();
        System.out.println(accessToken);
        //WeiXinMedia weiXinMedia = uploadMedia(accessToken , "image","http://localhost:8080/hflBlog/image/jin.jpg");
        //WeiXinMedia weiXinMedia = uploadMedia(accessToken , "image","http://localhost:8080/hflBlog/image/timg.jpg");

        WeiXinMedia weiXinMedia = uploadMedia(accessToken , type,mediaurl);
        System.out.println(String.format("type: %s === media: %s   createTime: ===== %d",weiXinMedia.getType(), weiXinMedia.getMediaId() , weiXinMedia.getCreatedAt()));
    }

    //重载 下载多媒体文件
    public static String getMedia(String mediaId ,String savePath) {
        String accessToken  = WeiXinUtil.getToken(Parameter.corId , Parameter.appsecret).getAccessToken();
        String filePath = getMedia(accessToken, mediaId,savePath);
        //String filePath = getMedia(accessToken, mediaId,"c:/download");
        return  filePath;
    }



    /*********************
     * 以下为测试代码
     * *********************************************/

//    /**
//     *获取用户信息
//     */
//    public static UserInfo getUserInfo(String accessToken , String openId){
//        UserInfo userInfo = null;
//        //拼接请求地址
//        String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
//        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID",openId);
//        //获取用户信息
//        JSONObject jsonObject = WeiXinUtil.httpsRequset(requestUrl ,"GET" ,null);
//        if(null != jsonObject){
//            try{
//                userInfo = new UserInfo();
//                // 用户的标识，对当前公众号唯一
//                if(jsonObject.getString("openid")!=null || !"".equals(jsonObject.getString("openid"))) {
//                    String openid = ChangeCharsetUtil.changeCharset(jsonObject.getString("openid"),"gbk","UTF-8");
//                    userInfo.setOpenid(openid);
//                }
//                //用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
//                userInfo.setSubscribe(jsonObject.getInt("subscribe"));
//                //    nickname	用户的昵称
//                if(jsonObject.getString("nickname")!=null || !"".equals(jsonObject.getString("nickname"))) {
//                    String nickname = ChangeCharsetUtil.changeCharset(jsonObject.getString("nickname"),"gbk","UTF-8");
//                    userInfo.setNickname(nickname);
//                }
//
//                //    sex	用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
//                userInfo.setSex(jsonObject.getInt("sex"));
//                //    city	用户所在城市
//                if(jsonObject.getString("city")!=null || !"".equals(jsonObject.getString("city"))) {
//                    String city = ChangeCharsetUtil.changeCharset(jsonObject.getString("city"),"gbk","UTF-8");
//                    userInfo.setCity(city);
//                }
//                //    country	用户所在国家
//                if(jsonObject.getString("country")!=null || !"".equals(jsonObject.getString("country"))) {
//                    String country = ChangeCharsetUtil.changeCharset(jsonObject.getString("country"),"gbk","UTF-8");
//                    userInfo.setCountry(country);
//                }
//                //    province	用户所在省份
//                if(jsonObject.getString("province")!=null || !"".equals(jsonObject.getString("province"))) {
//                    String province = ChangeCharsetUtil.changeCharset(jsonObject.getString("province"),"gbk","UTF-8");
//                    userInfo.setProvince(province);
//                }
//                //  language	用户的语言，简体中文为zh_CN
//                if(jsonObject.getString("language")!=null || !"".equals(jsonObject.getString("language"))) {
//                    String language = ChangeCharsetUtil.changeCharset(jsonObject.getString("language"),"gbk","UTF-8");
//                    userInfo.setLanguage(language);
//                }
//                //    headimgurl	用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
//                userInfo.setHeadimgurl(jsonObject.getString("headimgurl"));
//                if(jsonObject.getString("headimgurl")!=null || !"".equals(jsonObject.getString("headimgurl"))) {
//                    String headimgurl = ChangeCharsetUtil.changeCharset(jsonObject.getString("headimgurl"),"gbk","UTF-8");
//                    userInfo.setHeadimgurl(headimgurl);
//                }
//                //    subscribe_time	用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
//                userInfo.setSubscribeTime(jsonObject.getString("subscribe_time"));
//                if(jsonObject.getString("subscribe_time")!=null || !"".equals(jsonObject.getString("subscribe_time"))) {
//                    String subscribe_time = ChangeCharsetUtil.changeCharset(jsonObject.getString("subscribe_time"),"gbk","UTF-8");
//                    userInfo.setSubscribeTime(subscribe_time);
//                }
//                //    unionid	只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。详见：获取用户个人信息（UnionID机制）
////                if(null!=jsonObject.getString("unionid")) {
////                    userInfo.setUnionid(jsonObject.getString("unionid"));
////                }
//                //    remark	公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注
//                if(jsonObject.getString("remark")!=null || !"".equals(jsonObject.getString("remark"))) {
//                    String remark = ChangeCharsetUtil.changeCharset(jsonObject.getString("remark"),"gbk","UTF-8");
//                    userInfo.setRemark(remark);
//                }
//                //    groupid	用户所在的分组ID
//                if(jsonObject.getString("groupid")!=null || !"".equals(jsonObject.getString("groupid"))) {
//                    String groupid = ChangeCharsetUtil.changeCharset(jsonObject.getString("groupid"),"gbk","UTF-8");
//                    userInfo.setGroupid(groupid);
//                }
//            }catch (Exception e){
//                if(0 == userInfo.getSubscribe()){
//                    //log.error("用户{{}已取消关注" ,userInfo.getOpenid()) ;
//                    System.out.println("用户{{}已取消关注" +userInfo.getOpenid());
//                }else{
//                   int errorCode = jsonObject.getInt("errcode");
//                   String errMsg = jsonObject.getString("errmsg");
//                   //log.error("获取用户信息失败errorcode:{} errmsg：{}",errorCode,errMsg);
//                    System.out.println("获取用户信息失败errorcode:{} errmsg：{}"+errorCode+"===="+errMsg);
//                }
//            }
//        }
//        return userInfo;
//    }

//    public static void main(String[] args) {
//        //下载
//        String filePath =  getMedia("qyejfNJg9Vzreh90SRMugGeC_xgHrKzqJdOuZJ_ARa40ZYNJtaGRjWtHsdaoCD3I" ,"c:/download");
//        System.out.println(filePath);
////        上传
////         uploadMedia("image" ,"http://127.0.0.1:8080/jinlele/images/com/001.jpg");
//    }
    //测试用户列表
    public static void main(String[] args) {
        //获取接口访问凭证
        String accessToken  = WeiXinUtil.getToken(Parameter.corId , Parameter.appsecret).getAccessToken();
        System.out.println(accessToken);
        UserList  userList = getUserList(accessToken, "");
        System.out.println(new JSONObject().fromObject(userList));
    }

    //测试用户信息
//    public static void main(String[] args) {
//        //获取接口访问凭证
//        String accessToken  = WeiXinUtil.getToken(Parameter.corId , Parameter.appsecret).getAccessToken();
//        System.out.println(accessToken);
//        User  userInfo = getUserInfo(accessToken, "okhnkvvnLaxUQxAeH6v8SUcu9jZQ");
//        System.out.println(new JSONObject().fromObject(userInfo));
//    }

    //测试用户列表+用户信息
//    public static void main(String[] args) {
//        String accessToken  = WeiXinUtil.getToken(Parameter.corId , Parameter.appsecret).getAccessToken();
//        System.out.println(accessToken);
//        UserList  userList = getUserList(accessToken, "");
//        System.out.println(new JSONObject().fromObject(userList));
//        System.out.println("总用户数=="+userList.getTotal());
//        System.out.println("拉取的OPENID个数=="+userList.getCount());
//        System.out.println("拉取列表的最后一个用户的OPENID=="+userList.getNextOpenId());
//        List<String> openidList = userList.getOpenIdList();
//        UserInfo  userInfo = null;
//        for(int i = 0;i<openidList.size();i++){
//            System.out.println("========="+(i+1)+"============");
//            userInfo = getUserInfo(accessToken,openidList.get(i));
//            System.out.println("关注情况:===="+userInfo.getSubscribe());
//            System.out.println("昵称:===="+userInfo.getNickname());
//            System.out.println("性别:===="+userInfo.getSex());
//            System.out.println("城市:===="+userInfo.getCity());
//            System.out.println("省:===="+userInfo.getProvince());
//            System.out.println("国家:===="+userInfo.getCountry());
//            System.out.println("语言:===="+userInfo.getLanguage());
//            System.out.println("头像:===="+userInfo.getHeadimgurl());
//            System.out.println("关注时间:====" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(Long.parseLong(userInfo.getSubscribeTime()))));
//            System.out.println("关注情况:===="+userInfo.getSubscribe());
//            System.out.println("备注:===="+userInfo.getRemark());
//            System.out.println("分组id:===="+userInfo.getGroupid());
//            System.out.println("unionid:===="+userInfo.getUnionid());
//        }
//    }

    //测试查询分组
//    public static void main(String[] args) {
//        String accessToken  = WeiXinUtil.getToken(Parameter.corId , Parameter.appsecret).getAccessToken();
//        List<WeiXinGroup> weiXinGroupList = getGroups(accessToken);
//        for(WeiXinGroup weiXinGroup:weiXinGroupList){
//            System.out.println("分组id==="+weiXinGroup.getId());
//            System.out.println("分组名称===="+weiXinGroup.getName());
//            System.out.println("分组用户数===="+weiXinGroup.getCount());
//        }
//    }

    //测试新增分组
//    public static void main(String[] args) {
//        String accessToken  = WeiXinUtil.getToken(Parameter.corId , Parameter.appsecret).getAccessToken();
//        WeiXinGroup weiXinGroup = createGroups(accessToken ,"广东");
//        if(null != weiXinGroup){
//            System.out.println("分组id==="+weiXinGroup.getId());
//            System.out.println("分组名称===="+weiXinGroup.getName());
//            System.out.println("分组用户数===="+weiXinGroup.getCount());
//            System.out.println(String.format("成功创建分组: %s id: %d",weiXinGroup.getName() ,weiXinGroup.getId()));
//        }
//
//    }

    //测试修改分组名
//    public static void main(String[] args) {
//        String accessToken  = WeiXinUtil.getToken(Parameter.corId , Parameter.appsecret).getAccessToken();
//        updateGroup(accessToken , 107 ,"山东");
//    }

    //测试移动用户分组
//    public static void main(String[] args) {
//        String accessToken  = WeiXinUtil.getToken(Parameter.corId , Parameter.appsecret).getAccessToken();
//        updateMemberGroup(accessToken , "o8_32sy_Exe2_Shv9XHgiIzMomoI",107);
//    }

    //测试 批量移动用户分组 updateMoreMemberGroup   （测试号无权限）
//    public static void main(String[] args) {
//        String accessToken  = WeiXinUtil.getToken(Parameter.corId , Parameter.appsecret).getAccessToken();
//        System.out.println(accessToken);
//        String[] opidList = new String[]{"o8_32sy_Exe2_Shv9XHgiIzMomoI"};
//        updateMoreMemberGroup(accessToken , opidList,107);
//    }






    //测试 创建临时二维码
//    public static void main(String[] args) {
//        String accessToken  = WeiXinUtil.getToken(Parameter.corId , Parameter.appsecret).getAccessToken();
//        System.out.println(accessToken);
//        WeixinQRCode temporaryQRCode = createTemporaryQRCode(accessToken , 1800 , 1000);
//        System.out.println(String.format("ticket= %s , expire_time= %d", temporaryQRCode.getTicket() ,temporaryQRCode.getExpireSeconds()));
//    }

    //测试 创建永久二维码
//    public static void main(String[] args) {
//        String accessToken  = WeiXinUtil.getToken(Parameter.corId , Parameter.appsecret).getAccessToken();
//        System.out.println(accessToken);
//        String  ticket = createPermannentQRCode(accessToken, 100);
//        System.out.println(String.format("ticket= %s ", ticket));
//    }

    //测试 根据ticket 换取二维码
//    public static void main(String[] args) {
//        //临时
//        //String tiket =  "gQHL8DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL3praEc0RmJrVjNFdW1QNWhrR1kzAAIEG4UFWAMECAcAAA==";
//        //永久
//        String tiket = "gQHk8DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xLzVVaEpxUGZrU0hFeFdOYzVuMlEzAAIEPYgFWAMEAAAAAA==";
//        String savePath = "e:/download";
//        String  filePath = getQRCode(tiket , savePath);
//        System.out.println(filePath);
//    }
    //https://mp.weixin.qq.com/cgi-bin/announce?action=getannouncement&key=1461052555&version=1&lang=zh_CN&platform=2

    //测试长链接转短链接
//    public static void main(String[] args) {
//        String accessToken  = WeiXinUtil.getToken(Parameter.corId , Parameter.appsecret).getAccessToken();
//        String longUrl = "https://mp.weixin.qq.com/cgi-bin/announce?action=getannouncement&key=1461052555&version=1&lang=zh_CN&platform=2";
//        String  shortUrl = longUrl2shortUrl(accessToken, longUrl);
//        System.out.println("短链接为:==="+shortUrl);
//    }

    //测试创建客服账号
//    public static void main(String[] args) throws NoSuchAlgorithmException {
//        String accessToken  = WeiXinUtil.getToken(Parameter.corId , Parameter.appsecret).getAccessToken();
//        String account= "123456";
//        //String weixinHao = "gh_857250b7a95a";
//        String weixinHao = "i69032866";
//        String nickname = "小胡";
//        String password = "123456";
//        createCustomer(accessToken , account , weixinHao ,nickname , password);
//    }

//      public static void main(String[] args) throws NoSuchAlgorithmException {
//        String password = "123456";
//        System.out.println(WeiXinUtil.md5(password).length());
//      }



}
