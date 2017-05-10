
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<input type="hidden" id="openid"  value="${openId}"/>
<input type="hidden" id="userId"  value="${userId}"/>
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
