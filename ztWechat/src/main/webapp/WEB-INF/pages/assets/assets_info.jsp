 <%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
 <%@ taglib prefix="e" uri="/emvc-tags" %>
 <%
  String path = request.getContextPath();
  String basePath = request.getScheme() + "://"+ request.getServerName(); 
  if(request.getServerPort()!=80){
    basePath = basePath + ":" + request.getServerPort(); 
  }      
  basePath = basePath + path + "/";
%>
<html>
<head lang="en">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <script type="text/javascript" src="static/js/assets/asset_info.js"></script>
    <title>我的懒猫</title>
</head>
<body>
<input id="totalWealth" type="hidden" value="<e:property value="@_formater.formatNumber(totalWealth)"/>">
<input id="yesterdayIncome" type="hidden" value="<e:property value="@_formater.formatNumber(yesterdayIncome)"/>">
<input id="totalIncome" type="hidden" value="<e:property value="@_formater.formatNumber(totalIncome)"/>">
<input id="accountBalanceResult" type="hidden" value="<e:property value="@_formater.formatNumber(accountBalanceResult)"/>">
<div id="box" class="scb">
    <div class="layout-wrap scbCard font-white tc">
        <div class="layou-up assetArea pr">
          <a id="look" onclick="changeType()" class="icon icon-look lookIcon font-25 pa"></a>
            <!--APP端隐藏个人中心按钮-->
             <%-- <e:if test="@platform=='WX'"> --%>
            <a href="account/toAccount" class="icon icon-ucenter pa"></a>
            <%-- </e:if> --%>
            <!--APP端隐藏个人中心按钮-->
            <a href="asset/myTotalWealth" class="font-white pr">
                <span id="totalWealthSpan" class="font-45"><e:property value="@_formater.formatNumber(totalWealth)"/></span>
                <p class="font-12">我的总财富（元）</p>
                <i class="icon-arrow triangle-right pa"></i>
            </a>
        </div>
        <div class="layou-down tc clearfix">
            <a class="layou-down-left" href="asset/toTodayIncome">
                <span id="yesterdayIncomeSpan" class="font-20"><e:property value="@_formater.formatNumber(yesterdayIncome)"/></span>
                <p class="font-10">&nbsp;&nbsp;&nbsp;昨日收益（元）</p>
                <i class="icon-arrow triangle-right pa"></i>
            </a>
            <a class="layou-down-right" href="asset/toTotalIncome">
                <span id="totalIncomeSpan" class="font-20"><e:property value="@_formater.formatNumber(totalIncome)"/></span>
                <p class="font-10">&nbsp;&nbsp;&nbsp;累计收益（元）</p>
                <i class="icon-arrow triangle-right pa"></i>
            </a>
        </div>
     </div>
     <div class="A-nav">
      <!-- app充值，提现 --> 
     <e:if test="@platform=='APP'">
      <e:if test="@isBankCard=='YES'">
       <a href="asset/toCashIn"><span class="btnUncard orange fr">提现</span></a>
        <a href="asset/toRecharge"> <span class="btn-small fr">充值</span></a>
      </e:if><e:else>
       <a href="account/card/toBindCard?returnFlag=toCashIn"><span class="btnUncard orange fr">提现</span></a>
        <a href="account/card/toBindCard?returnFlag=toRecharge"> <span class="btn-small fr">充值</span></a>
      </e:else>
     </e:if><e:else> <!-- wx充值，提现 -->
     <a href="asset/toCashIn"><span class="btnUncard orange fr">提现</span></a>
        <a href="asset/toRecharge"> <span class="btn-small fr">充值</span></a>
     </e:else>
         懒猫账户余额：<span class="orange" id="accountBalanceResultSpan"><e:property value="@_formater.formatNumber(accountBalanceResult)"/> 元</span>
     </div>
    <div class="noticeArea bg-white">
        <ul>
            <li>
                <a href="account/card/toCard"><i class="icon icon-bank-card"></i> 我的银行卡</a>
                <a href="fixed/toMyCoupons"><i class="icon icon-voucher"></i> 我的卡券</a>
            </li>
            <li>
                <a href="activity/toActivityList" class=""><!-- <i class="icon-round fr"></i> --><i class="icon icon-activity"></i> 活动专区</a>
                <a href="activity/toNewsList" class=""><e:if test="@messageFlag">
                <i class="icon-round fr"></i>
                </e:if><i class="icon icon-notice"></i> 消息公告</a>
            </li>   
            <!--APP端隐藏-->
            <e:if test="@platform=='WX'">
            <li>
                <a href="javascript:void(0)" class="referees"><i class="icon icon-my-recommended"></i> <e:property value="@consultantUserDesc"/></a>
                  <a id="drawPrizeContent" href=""><i class="icon icon-friends-recommended"></i> 朋友推荐</a>
            </li>
            </e:if>
            <!--APP端隐藏-->         
        </ul>
    </div>
</div>
<div class="h1-6"></div>
<!--弹出层-->
<div id="mask" style="display: none"></div>
<div id="alertLayer" class="unloginMask expectMask" style="display: none; width: 100%; height:45%;">
    <div class="pr">
        <img src="static/images/expectMask.png" class="repeatImg" alt=""/>
        <a class="btnClosed font-white pa" href="javascript:void(0)"><i class="icon icon-error2"></i></a>
    </div>
</div>
<div id="alertLayer-6" class="unloginMask expectMask" style="display: none; width: 100%; height:45%;">
    <div class="pr">
        <img src="static/images/expectMask.png" class="repeatImg" alt=""/>
        <a class="btnClosed font-white pa" href="javascript:void(0)"><i class="icon icon-error2"></i></a>
    </div>
</div>
<!--弹出层end-->
 <script>
    var drawPrizeContent='';
    //判断是否是微信内置浏览器
      var ua = navigator.userAgent.toLowerCase();  
      if(ua.match(/MicroMessenger/i)=="micromessenger") {  
          drawPrizeContent="https://open.weixin.qq.com/connect/oauth2/authorize?appid=<%=com.yeepay.g3.app.lmweChat.service.RequestParamBuilderService.APP_ID%>&redirect_uri=<%=java.net.URLEncoder.encode(basePath+"activity/toInviteFriend", "utf-8")%>&response_type=code&scope=snsapi_userinfo&state=common&connect_redirect=1#wechat_redirect";
      } else {  
        drawPrizeContent="activity/toInviteFriend";
      }  
      $("#drawPrizeContent").attr("href",drawPrizeContent);
    </script>
</body>
</html>