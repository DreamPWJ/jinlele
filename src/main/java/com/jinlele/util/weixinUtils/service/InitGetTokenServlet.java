package com.jinlele.util.weixinUtils.service;
import com.jinlele.util.weixinUtils.vo.TokenThread;
import com.jinlele.util.weixinUtils.vo.WeiXinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 *
 */
@WebServlet(name="InitGetTokenServlet",urlPatterns="/InitGetTokenServlet" ,loadOnStartup = 1)
public class InitGetTokenServlet extends HttpServlet {
    private static  final  long serialVersionUID  = 1L;
    private static  final  String appid  = "wx7a6a63e9ee94e24d";
    private static  final  String appsecret  = "wx7a6a63e9ee94e24d";
    private static Logger log =  LoggerFactory.getLogger(WeiXinUtil.class);


    public void init() throws ServletException {
        //获取web.xml中配置的参数
        TokenThread.appid = getInitParameter("appid");
        TokenThread.appsecret = getInitParameter("appsecret");

        log.info("微api appid:{}" , TokenThread.appid);
        log.info("微api appsecret:{}" , TokenThread.appsecret);

        //未配置appid ，appsecret时给出提示
        if("".equals(TokenThread.appid) || "".equals(TokenThread.appsecret)){
           // log.error("appid and appsecret配置错误,请仔细检查!");
        }else{
            //启动定时获取access_token的线程
            new Thread(new TokenThread()).start();
        }

    }
}
