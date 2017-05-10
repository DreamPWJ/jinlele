
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>

</body>
<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
<script>
    $(function(){
//        var openid = $('#openid').val();
//        var userId = $('#userId').val();
        localStorage.setItem("openId",'${openId}');
        localStorage.setItem("jinlele_userId",'${userId}');
        // window.location.href="http://www.6weiyi.com/jinlele/mall";//只能在一个网站中打开本网站的网页
        window.open("http://www.6weiyi.com/jinlele/mall", "_blank");//可以在一个网站上打开另外的一个网站的地址
    });
</script>
</html>
