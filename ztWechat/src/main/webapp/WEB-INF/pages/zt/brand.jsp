<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.yeepay.g3.app.lmweChat.utils.LmConstants" %>
<%@ taglib prefix="e" uri="/emvc-tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    String sysVersion = LmConstants.sysVersion;
%>
<html>
<head lang="en">
 <title>品牌宣传</title>
    <link rel="stylesheet" href="static/css/LM-invest.css?v=<%=sysVersion %>">
    <link rel="stylesheet" href="static/css/LM-funds.css?v=<%=sysVersion %>">
    <script type="text/javascript" src="static/js/jquery-1.8.3.min.js?v=<%=sysVersion %>"></script>
    <script type="text/javascript" src="static/js/jquery.flexslider-min.js?v=<%=sysVersion %>"></script>
    <script type="text/javascript" src="static/js/typed.js?v=<%=sysVersion %>"></script>
    <script>
        $(function(){
            $(".element").typed({
                strings: ["意得志满的现在", "多姿多彩的未来","安静祥和的晚年"],
                typeSpeed: 200,
                loop: true,
                showCursor: true,
                cursorChar: "|",
                startDelay: 300,
                backSpeed: 50,
                backDelay: 2000,
            });

            $(".flexslider").flexslider({
                slideshowSpeed: 4800, //展示时间间隔ms
                animationSpeed: 1000, //滚动时间ms
                touch: true, //是否支持触屏滑动
                controlNav: false,
                directionNav: false,
            });
        });

    </script>
</head>
<body>
<div style="background: #eeeeee;">
    <!--banner-->
    <div class="investBanner pr">
        <div class="flexslider">
            <ul class="slides">
                <li><img src="static/images/invest/brand-1-1.jpg" class="repeatImg"/></li>
                <li><img src="static/images/invest/brand-1-2.jpg" class="repeatImg"/></li>
                <li><img src="static/images/invest/brand-1-3.jpg" class="repeatImg"/></li>
            </ul>
        </div>
        <div class="font-tit pa tc">许ta一个</div>
        <div class="i-element pa tc"><span class="element"></span></div>
        <div class="clearfix"></div>
    </div>
    <!--banner--end-->
    <div class="pr">
        <img src="static/images/invest/brand-02.jpg" class="repeatImg"/>
        <img src="static/images/invest/brand-03.jpg" class="repeatImg"/>
        <img src="static/images/invest/brand-04.jpg" class="repeatImg"/>
    </div>
    <div class="input-group input-group-new">
        <p class="font-tit-sm tc">
            你羡慕吗？<br/>
            那就来试试我们的灵机一投吧！
        </p>
        <a class="btn-login" href="zt/introduce/zTproduct">去看看</a>
    </div>
</div>
</body>
</html>