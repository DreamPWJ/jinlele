
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
        localStorage.setItem("openId",'${openId}');
        localStorage.setItem("jinlele_userId",'${userId}');
        setTimeout(function(){
          window.location.href="http://www.6weiyi.com/jinlele/mall";
       },10);

    });
</script>
</html>
