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
<title>风险测评</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache, must-revalidate">
<meta http-equiv="expires" content="0">  
<link rel="stylesheet" href="static/css/LM-funds.css?v=<%=sysVersion %>">
<script type="text/javascript" src="static/js/fundRisk.js?v=<%=sysVersion %>"></script>
<script type="text/javascript">
function jumpRiskTest(){
  var riskLevel = $("#riskLevel").val();
  if(riskLevel == '') {
    riskLevel="保守型";
  }
  $("#alertLayer-6Message").html("跳过风险测评平台将默认您为"+riskLevel+"投资者，是否确认跳过测评？");
  $("#mask").show();
  $("#alert6").show();
}
//跳过风险测评
var jumpTest = true;
function toJumpRiskTest(){
	var sceneId = document.getElementById("sceneId").value;
    if(jumpTest) {
	 location.href="zt/risk/goJumpRisk?sceneId="+sceneId;
	 jumpTest= false;
    }
}

//关闭弹出框
function clean(){
	  $("#mask").hide();  
	  $("#alert").hide();
	  $("#alert7").hide();
	  $("#alert6").hide();
	}

</script>
</head>
<!-- 头部结束 -->

<body>
<div class="subjectArea pr">
   <div class="coverContent">
    <!--进度条开始-->
    <div class="progressBar">
        <div class="evolve evolve-1" id='evo'></div>
    </div>
<input type="text" id="riskLevel" name="riskLevel" value="${riskLevel}" style="display:none" />
    <!--进度条结束-->
   <form action="zt/risk/doRiskTest" id="form" method="get">
    <input type="text" id="answerArr" name="answerArr" style="display:none" />
    <input type="text" id="sceneId" name="sceneId" value="${sceneId}" style="display:none" />
    <input type="text" id="retreatAnswer" name="retreatAnswer"  style="display:none" />
    <!-- 此处埋一个标签 -->
    <div class="subjectHeader">
        <p class="question" id='qus'>1/10. 您属于那个年龄阶层？</p>
        <p class="subjectBar">
            <a class="next fl" href="javascript:lastPage()" id='lastPage'>上一题</a>
            <a class=" hop fr" href="javascript:jumpRiskTest()">跳过</a>
        </p>
    </div>
    <div class="answer">
        <ul>
            <li id='l1' value="1">
                <div>
                    <i class="icon">●</i>
                    <span id='a1'>60岁以上/20岁以下</span>
                </div>

            </li>
            <li id='l2' value="2">
                <div>
                    <i class="icon">●</i>
                    <span id='a2'>50至60岁</span>
                </div>
            </li>
            <li id='l3' value="3">
                <div>
                    <i class="icon">●</i>
                    <span id='a3'>30至40岁</span>
                </div>
            </li>
            <li id='l4' value="4">
                <div>
                    <i class="icon">●</i>
                    <span id='a4'>20至30岁</span>
                </div>
            </li>
            <li id='l5' value="5">
                <div>
                    <i class="icon">●</i>
                    <span id='a5'>40至50岁</span>
                </div>
            </li>
        </ul>
    </div>
    <div class="submitBtn" style="display:none" id="submitBtn">
            <a href="javascript:void(0)" >提交</a>
    </div>
    </form>
    </div>
</div>
<div class="prompt">
    <img src="static/images/fund/prompt.png" alt=""/>
</div>
<!--弹层-->
<div id="mask" style="display: none"></div>

<div id="alertLayer-1" class="alertLayer  bg-white" style="width: 100%; display: none">
    <div class="pr">
        <div class="hopArea">
            <p>是否确认跳过投资者风险承受能力评测？</p>
            <div class="hopBtn">
                <a href="zt/riskTest/goJumpRisk?sceneId=${sceneId }" onClick="jumpRiskTest()">跳过</a>
                <a class="btnClosed" href="#">取消</a>
            </div>
        </div>
    </div>
</div>

<div id="alert6" class="alertLayer" 
		style="display: none; width: 100%; top:20%; ">
		<div class="bg-white radius1" style=" width: 90%; margin:0 auto">
			<div class="pt30 pb30 br-bottom orange tc font-16"
				id="alertLayer-6Message" style="margin:10px 20px 10px 20px;text-align:left;">
			</div>
			<div class="btnMaskArea tc mt30">
				<a href="javascript:toJumpRiskTest();">跳过</a> <a
					href="javascript:void(0)" onclick="clean()">取消</a>
			</div>
		</div>
</div>
	
<div id="alert7" class="alertLayer"
	style="display: none; width: 100%; top:20%;">
	<div class="bg-white radius1" style=" width: 90%; margin:0 auto">
	<div class="pt30 pb30 br-bottom orange tc font-16"
		id="alertLayer-7Message" style="margin:10px 20px 10px 20px;text-align:left;"></div>
	<div class="btnMaskArea tc mt30">
		<a href="javascript:toJumpRiskTest()">跳过</a>
		<a href="javascript:void(0)" onclick="clean()">取消</a>
	</div>
	</div>
</div>
</body>
</html>