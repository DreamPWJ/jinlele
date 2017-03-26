<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/3/26 0026
  Time: 20:49
  To change this template use File | Settings | File Templates.J
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<input type="hidden" id="openid"  value="${opendId}"/>
</body>
<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>

<script>
    $(function(){
        var openid = $('#openid').val();
        localStorage.setItem("openId",openid);
        setTimeout(function(){
          window.location.href="http://www.6weiyi.com/jinlele/mall";
       },3000);

    });
</script>
</html>
