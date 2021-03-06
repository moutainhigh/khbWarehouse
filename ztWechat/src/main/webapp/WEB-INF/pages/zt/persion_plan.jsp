<%@page import="com.yeepay.g3.app.lmweChat.service.RequestParamBuilderService"%>
<%@page import="com.yeepay.g3.facade.activity.enums.ShareBizTypeEnum" %> 
<%@page import="com.yeepay.g3.app.lmweChat.utils.WXAPIUtils,java.util.Map" %>
<%@page import="com.yeepay.g3.app.lmweChat.utils.LmConstants" %>
<%@page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
 <%@ taglib prefix="e" uri="/emvc-tags" %>
 <%
 String path = request.getContextPath();
 String basePath = request.getScheme() + "://"+ request.getServerName(); 
 if(request.getServerPort()!=80){
   basePath = basePath + ":" + request.getServerPort(); 
 }      
 basePath = basePath + path + "/";
 String sysVersion = LmConstants.sysVersion;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>养老计划</title>
<link rel="stylesheet" href="static/css/LM-common.css?v=<%=sysVersion %>">
<link rel="stylesheet" href="static/css/LM-app.css?v=<%=sysVersion %>">
<link rel="stylesheet" href="static/css/LM-funds.css?v=<%=sysVersion %>">
<link rel="stylesheet" href="static/css/LM-invest.css?v=<%=sysVersion %>">
<script type="text/javascript" src="static/js/common/format_common.js?v=<%=sysVersion %>"></script>
<script type="text/javascript" src="static/js/zt/customizedQuestions.js?v=<%=sysVersion %>"></script>
</head>
<!-- 头部结束 -->
<body>
<div class="evaluationArea">
    <form action="zt/risk/doQuestions" id="form" method="get">
	    <input type="text" id="sceneId" name="sceneId" value="${sceneId}" style="display:none" />
	    <input type="text" name="type" value="customized" style="display:none" />
	    <input type="text"  id="flag" value="" style="display:none" />
	    <div class="evaluation-list">
	        <p>请告诉我您的年龄</p>
	        <div class="input-wrap">
	            <span class="evaluation-input pr">
	                <input type="number"  class="input-text" iqd=""  id="years" name="years" placeholder=""/>
	                <span class="evaluation-text pa">岁</span>
	            </span>
	        </div>
	        <div class="error-tips red" id="yearsError"></div>
	    </div>
	    <div class="evaluation-list">
	        <p>每月可投入多少钱</p>
	        <div class="input-wrap">
	            <span class="evaluation-input pr">
	                <input type="number" class="input-text" iqd="" id="monthMoney" name="money" placeholder=""/>
	                <span class="evaluation-text pa">元</span>
	            </span>
	        </div>
	        <div class="error-tips red" id="moneyError" ></div>
	    </div>
    </form>
    <div class="submitBtn submitBtn-gray" id="submitBtn">
        <a href="javascript:viod(0)">提交</a>
    </div>
</div>

</body>
</html>