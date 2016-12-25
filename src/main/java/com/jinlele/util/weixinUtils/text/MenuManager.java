package com.jinlele.util.weixinUtils.text;


import com.jinlele.util.weixinUtils.menu.*;
import com.jinlele.util.weixinUtils.util.MenuUtil;
import com.jinlele.util.weixinUtils.util.Parameter;
import com.jinlele.util.weixinUtils.vo.Token;
import com.jinlele.util.weixinUtils.vo.WeiXinUtil;

/**
 *
 * 菜单单操作测试
 */
public class MenuManager {
    //private static Logger log = LoggerFactory.getLogger(MenuManager.class);

    /**
     * 自定义菜单结构
     */
    private static Menu getMenu(){
        ClickButton btn11 = new ClickButton();
        btn11.setName("胡芳学院..");
        btn11.setType("click");
        btn11.setKey("HufangCollege");

        ClickButton btn12 = new ClickButton();
        btn12.setName("胡芳软件学院..");
        btn12.setType("click");
        btn12.setKey("HufangSoft");

        ViewButton btn13 = new ViewButton();
        btn13.setName("聚化网..");
        btn13.setType("view");
        btn13.setUrl("http://www.juhuawang.cn/");

        ViewButton btn21 = new ViewButton();
        btn21.setName("百度..");
        btn21.setType("view");
        btn21.setUrl("http://baidu.com");

        ViewButton btn22 = new ViewButton();
        btn22.setName("ITEYE..");
        btn22.setType("view");
        btn22.setUrl("http://www.iteye.com/");

        ViewButton btn23 = new ViewButton();
        btn23.setName("36氪互联网创业");
        btn23.setType("view");
        btn23.setUrl("http://36kr.com/");

        ViewButton btn24 = new ViewButton();
        btn24.setName("腾讯科技");
        btn24.setType("view");
        btn24.setUrl("http://tech.qq.com/");

        ViewButton btn25 = new ViewButton();
        btn25.setName("拍拍贷..");
        btn25.setType("view");
        btn25.setUrl("http://www.ppdai.com/");

        ViewButton btn31 = new ViewButton();
        btn31.setName("门店网络..");
        btn31.setType("view");
        btn31.setUrl("http://139.224.54.94/hflBlog2/aboutUs/aboutUs.jsp");

//        ViewButton btn32= new ViewButton();
//        btn32.setName("1588");
//        btn32.setType("view");
//        btn32.setUrl("http://www.yi588.com/");

        ComplexButton mainBtn1 = new ComplexButton();
        mainBtn1.setName("在线教育");
        mainBtn1.setSub_button(new Button[]{btn11 ,btn12 ,btn13});

        ComplexButton mainBtn2 = new ComplexButton();
        mainBtn2.setName("行业资讯");
        mainBtn2.setSub_button(new Button[]{btn21 ,btn22 ,btn23});

        ComplexButton mainBtn3 = new ComplexButton();
        mainBtn3.setName("关于我们");
        mainBtn3.setSub_button(new Button[]{btn31});

        Menu menu = new Menu();
        menu.setButton(new Button[]{mainBtn1 , mainBtn2 ,mainBtn3});

        return  menu;
    }

    private static Menu getMenu2(){
        ViewButton btn11 = new ViewButton();
        btn11.setName("金乐乐商城");
        btn11.setType("view");
        btn11.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx7a6a63e9ee94e24d&redirect_uri=http%3A%2F%2F6weiyi.com%2Fjinlele%2FoauthServlet&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect");

        ViewButton btn21 = new ViewButton();
        btn21.setName("会员登录");
        btn21.setType("view");
        btn21.setUrl("http://baidu.com");

        ViewButton btn22 = new ViewButton();
        btn22.setName("会员注册");
        btn22.setType("view");
        btn22.setUrl("http://www.iteye.com/");

        ClickButton btn23 = new ClickButton();
        btn23.setName("客服电话");
        btn23.setType("click");
        btn23.setKey("mobphone");


        ViewButton btn31 = new ViewButton();
        btn31.setName("贵重金属");
        btn31.setType("view");
        btn31.setUrl("http://baidu.com");

        ViewButton btn32 = new ViewButton();
        btn32.setName("砖石");
        btn32.setType("view");
        btn32.setUrl("http://baidu.com");

//        ViewButton btn32= new ViewButton();
//        btn32.setName("1588");
//        btn32.setType("view");
//        btn32.setUrl("http://www.yi588.com/");

//        ComplexButton mainBtn1 = new ComplexButton();
//        mainBtn1.setName("金乐乐商城");
//        mainBtn1.setSub_button(new Button[]{btn11});

        ComplexButton mainBtn2 = new ComplexButton();
        mainBtn2.setName("会员中心");
        mainBtn2.setSub_button(new Button[]{btn21 ,btn22 ,btn23});

        ComplexButton mainBtn3 = new ComplexButton();
        mainBtn3.setName("最新优惠");
        mainBtn3.setSub_button(new Button[]{btn31 ,btn32});

        Menu menu = new Menu();
        menu.setButton(new Button[]{btn11 , mainBtn2 ,mainBtn3});

        return  menu;
    }

    //测试创建菜单
    public static void main(String[] args) {
        //调用接口凭证
        Token token = WeiXinUtil.getToken(Parameter.corId, Parameter.appsecret);
        if(null != token){
            //创建菜单
            boolean result = MenuUtil.createMenu(getMenu2(), token.getAccessToken());

            //判断菜单创建结果
            if(result){
                //log.info("菜单创建成功");
                System.out.println("菜单创建成功!");
            }else {
               // log.info("菜单创建失败");
                System.out.println("菜单创建失败!");
            }
        }
    }

//    //测试查询菜单
//    public static void main(String[] args) {
//        //调用接口凭证
//        Token token = WeiXinUtil.getToken(Parameter.corId, Parameter.appsecret);
//        if(null != token){
//            //创建菜单
//            String result = MenuUtil.getMenu(token.getAccessToken());
//
//            //查询菜单结果
//            System.out.println("查看菜单查询结果:===" + result);
//        }
//    }

    //测试删除菜单  删除是删除全部菜单
//    public static void main(String[] args) {
//        //调用接口凭证
//        Token token = WeiXinUtil.getToken(Parameter.corId, Parameter.appsecret);
//        if(null != token){
//            //删除菜单
//            boolean result = MenuUtil.deleteMenu(token.getAccessToken());
//
//            //判断菜单删除结果
//            if(result){
//                //log.info("菜单删除成功");
//                System.out.println("菜单删除成功!");
//            }else {
//               // log.info("菜单删除失败");
//                System.out.println("菜单删除失败!");
//            }
//        }
//    }



}
