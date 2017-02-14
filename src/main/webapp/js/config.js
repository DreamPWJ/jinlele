/**
 * Created by pwj on 2016/12/7.
 * 系统接口常量配置
 */
var configMod = angular.module("starter.config", []);

configMod.constant("JinLeLe", {
    "name": "JinLeLe", //项目名称
    "debug": false, //调试标示 暂无使用
    //"api": "http://www.6weiyi.com/jinlele",//接口服务地址  使用
   "api": "http://127.0.0.1:8080/jinlele",//接口服务地址  使用
    'siteUrl': "http://a.jinlele.com",//仓库地址 暂无使用
    'imgUrl': "http://f.jinlele.com",//图片地址 暂无使用
    'moblileApi': "http://m.jinlele.com",//手机端服务
    'version': '0.1.0' //当前版本号
});


