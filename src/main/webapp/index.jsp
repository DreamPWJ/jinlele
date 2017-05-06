
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
        <input id="openid" type="hidden" value="${openId}">
        <input id="userId" type="hidden" value="${userId}">
</body>
<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
<script>
    $(function(){
        var openid = $('#openid').val();
        var userId = $('#userId').val();
        localStorage.setItem("openId",openid);
        localStorage.setItem("jinlele_userId",userId);
        setTimeout(function(){
          window.location.href="http://www.6weiyi.com/jinlele/mall";
       },10);

    });
</script>
</html>
