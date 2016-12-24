<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
  <head>
    <title></title>
      <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">

  </head>
  <style>
        *{
           margin: 0;
           padding: 0;
        }
        table{border:1px dashed #B9B9DD;font-size: 12px;padding:100px 30px;}
        td{border:1px dashed #B9B9DD;word-break:break-all;word-wrap: break-word;}
  </style>
  <body>

     <c:if test="${null != snSuserInfo}">
      <table  width="100%" cellspacing="0" cellpadding="0">
          <tr>
              <td>属性</td>
              <td width="80%">值</td>
          </tr>
          <tr>
              <td>openid</td>
              <td>${snSuserInfo.openid}</td>
          </tr>
          <tr>
              <td>昵称</td>
              <td>${snSuserInfo.nickname}</td>
          </tr>
          <tr>
              <td>性别</td>
              <td>${snSuserInfo.sex}</td>
          </tr>
          <tr>
              <td>国家</td>
              <td>${snSuserInfo.country}</td>
          </tr>
          <tr>
              <td>省份</td>
              <td>${snSuserInfo.province}</td>
          </tr>
          <tr>
              <td>城市</td>
              <td>${snSuserInfo.city}</td>
          </tr>
          <tr>
              <td>头像</td>
              <td><img src="${snSuserInfo.headimgurl}" alt="" width="200" height="200"/></td>
          </tr>
      </table>
     </c:if>

     <c:if test="${null == snSuserInfo}">
         用户不统一授权，未获取到用户信息!
     </c:if>
  </body>
</html>
