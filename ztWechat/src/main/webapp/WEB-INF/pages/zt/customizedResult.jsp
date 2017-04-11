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
 <title>测评结果</title>
    <link rel="stylesheet" href="static/css/LM-invest.css?v=<%=sysVersion %>">
    <link rel="stylesheet" href="static/css/LM-funds.css?v=<%=sysVersion %>">
    <script type="text/javascript">
$(document).ready(function() {
  if($("#wanringFlag").val() == "0") {
    $("#mask").show();
    $("#alertLayer").show();
  }
});

</script>
</head>

<body>
<div class="pr">
    <div class="financial-ad">
        <img class="repeatImg" src="static/images/invest/financial-ad-1.jpg">
        <a href="zt/introduce/zTproduct" class="a-ad pa"></a>
        <a href="javascript:void(0)" class="financialClosed pa"></a>
    </div>
</div>
<div class="evalContent">
   <e:if test="${!empty all }">
   <p class="evalTitle">
        全部投资策略如下：<br/>
        波动性高的策略可能会超过您的风险承受能力
    </p>
    </e:if>
    <e:if test="${empty all }">
    <p class="evalTitle">根据您的风险承受能力，推荐投资策略如下：</p>
    </e:if>
    <input style="display: none" id="wanringFlag" value="${wanringFlag }" />
    <e:iterator list="@dtoList" var="item">
	    <div class="evalMain evalMain-1 bg-white">
	    <a href="zt/policy/toPolicyInfo?sceneId=${sceneId}&calculateRecordId=<e:property value="@item.id"/>">
	        <h3><a href="javascript:void(0)"><e:property value="@item.policyName"/>·策略<span class="fr label-title"><e:property value="@item.sugWish"/></span></a></h3>
	        </a>
	        <a href="zt/policy/toPolicyInfo?sceneId=${sceneId}&calculateRecordId=<e:property value="@item.id"/>">
	        
	        <img src="static/images/invest/evaLine.jpg" alt=""/>
	        </a>
	        <a href="zt/policy/toPolicyInfo?sceneId=${sceneId}&calculateRecordId=<e:property value="@item.id"/>">
	        
	        <div class="evalDetail">
	            <p class="sum"><e:property value="@item.futureAmountMinStr"/><span class="amount-line"> ~ </span><e:property value="@item.futureAmountMaxStr"/></p>
	            <div class="evalCard pr">
	                <p class="expect">预期未来总市值(元)</p>
	            </div>
	            <div class="invest">若投入<span><e:property value="@item.perInvestAmount"/></span>元/月，建议连续投资<span><e:property value="@item.totalInvestTerm"/></span>年</div>
	            <div class="rate pr">
	                <ul>
	                    <li class="fl">
	                        <p class="orange num"><e:property value="@item.totalYieldRate"/>%</p>
	                        <div class="evalCard">
	                            <p class="expect expect-new">过去<e:property value="@item.lastTerm"/>年回报率</p>
	                        </div>
	                    </li>
	                    <li class="fl">
	                        <p class="orange num"><e:property value="@item.policyType"/></p>
	                        <p class="expect-new">波动性<e:property value="@item.fluctuate"/></p>
	                    </li>
	                </ul>
	                <div class="clearfix"></div>
	            </div>
	            <div class="clearfix"></div>
	        </div>
	        </a>
	    </div>
    </e:iterator>
    <e:if test="${empty all }">
    	<e:if test="${empty showAll }">
		    <div class="btnArea">
		        <a class="singleBtn fl" href="zt/introduce/sceneList">重选心愿</a>
		        <a class="singleBtn fr" href="zt/risk/questionsPage?sceneId=${sceneId}">重新答题</a>
		    </div>
	    </e:if>
	    <e:if test="${!empty showAll }">
		    <div class="btnArea">
		        <a class="singleBtn singleBtn-1 fl" href="zt/introduce/sceneList">重选心愿</a>
		        <a class="singleBtn singleBtn-1 fl" href="zt/risk/questionsPage?sceneId=${sceneId}">重新答题</a>
		        <a class="singleBtn singleBtn-1 fl" href="zt/risk/doQuestions?sceneId=${sceneId}&money=${money}&years=${years}&type=${type}&all=all">查看全部</a>
		    </div>
	    </e:if>
    </e:if>
    
</div>
<!--弹出层-->
<div id="mask"></div>
<div id="alertLayer" class="bg-white" style="display: none; width: 90%; min-height: 20%;">
    <div class="withdrawIntro detailIntro">
        <div class="text pb15">每月投资额小于500元的投资计划，不满足起投金额，可以增加心愿单总额或缩短投资年限哦！</div>
        <div class="btnArea btnArea-new tc">
            <a href="zt/risk/questionsPage?sceneId=${sceneId}" class="singleBtn fl singleBtn-new">重新答题</a>
            <a href="javascript:void(0)"  class="btnClosed singleBtn singleBtn-new fr">先看看投资计划</a>
        </div>
    </div>
</div>
</body>
</html>