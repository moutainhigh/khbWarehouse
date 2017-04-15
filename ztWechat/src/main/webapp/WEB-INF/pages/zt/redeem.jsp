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
<html>
<head lang="en">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <!--优先使用 IE 最新版本和 Chrome-->
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="viewport" content="width=device-width, maximum-scale=1.0, initial-scale=1.0, user-scalable=0" />
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <!--设置苹果工具栏颜色-->
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
  <%--   <link rel="stylesheet" href="static/css/LM-common.css?v=<%=sysVersion %>">
    <link rel="stylesheet" href="static/css/LM-app.css?v=<%=sysVersion %>">
    <link rel="stylesheet" href="static/css/icon-style.css?v=<%=sysVersion %>">
    <link rel="stylesheet" href="static/css/LM-invest.css?v=<%=sysVersion %>">
    <script type="text/javascript" src="static/js/jquery-1.8.3.min.js?v=<%=sysVersion %>"></script>
    <script type="text/javascript" src="static/js/LM-app.js?v=<%=sysVersion %>"></script> --%>
    <script type="text/javascript" src="static/js/zt/commonUtil.js"?v=<%=sysVersion %>></script>
    <script type="text/javascript" src="static/js/zt/redeem.js"?v=<%=sysVersion %>></script>
    <title>赎回</title>
</head>
<body>
<input type="hidden" id="totalEnableMoney" value="<e:property value="@_formater.formatNumber(totalEnableMoney)"/>">
<input type="hidden" id="policyOrderId" value="<e:property value="@policyOrderId"/>">
<div class="redemption-Area">
    <div class="main-page">
        <div class="redemptionTitle redemptionTitle-page pr">
            <p>赎回策略：<e:property value="@ztPolicyDto.policyName"/> </p>
            <p >可赎回总市值：<e:property value="@_formater.formatNumber(totalEnableMoney)"/>元</p>
        </div>
        <div class="investBox bg-white">
            <ul class="input-area">
                <li class="input-wrap-error">
                    <label class="inputLabel orange">赎回金额</label>
                    <span class="amount-text inp-text fl">
                        <input type="number" onblur="blurRedeemMoney()" class="" id="redeemMoney" name="" placeholder="最多可赎回<e:property value="@_formater.formatNumber(totalEnableMoney)"/>元"/>
                        <a href="javascript:void()" onclick="allRedeem()" class="font-text-note light-gray fr">全部赎回</a>
                    </span>
                    <span class="orange">元</span>
                    <div class="clearfix"></div>
                    <div class="clearfix"></div>
                    <div id="errorInfo" hidden="true" class="modTips font-10 red"><i class="icon icon-error2 font-12"></i> 错误信息 </div>
                </li>
            </ul>
        </div>
        <div class="redemptionMain redemptionMain-page bg-white">
            <div class="page-title">
              <!--   <p class="expect">预计<span class="orange">9-29</span>日确认申请</p> -->
                <p class="detail">赎回明细</p>
            </div>
            <table width="100%" class="redemptionTable redemptionTable-page">
             <tr>
                    <th class="tc" width="40%">基金名称</th>
                    <th class="tc" width="30%">总份额(份)</th>
                    <th class="tc">赎回份额(份)</th>
                </tr>
            <e:iterator list="@fundAndPolicyDtoList" var="items">
            <tr>
                    <td class="tc t-1"><e:property value="@items.fundName"/></td>
                    <td class="tc"><e:property value="@_formater.formatNumber(items.enableShare)"/></td>
                    <td class="tc" >--</td>
                </tr>
            </e:iterator>
            <div style="display:none" id="leastRedeemList">
            <e:iterator list="@leastRedeemList" var="items">
             <input   value="<e:property value="@items"/>" type="hidden" />
             </e:iterator>
             </div>
             <div style="display:none" id="netValueList">
            <e:iterator list="@fundAndPolicyDtoList" var="items">
             <input   value="<e:property value="@items.netValue"/>" type="hidden" />
             </e:iterator>
             </div>
             <%-- <div style="display:none" id="totalFundDetailList">
            <e:iterator list="@totalFundDetailListDto" var="items">
             <input   value="<e:property value="@items.enableShare"/>" type="hidden" />
             </e:iterator>
             </div> --%>
            </table>
        </div>
    </div>
</div>

<div class="identifier-page" style="display:none">
    <p class="tc"></p>
</div>

<div class="btnBot">
<input type="button" id="button" onclick="toNext()" value="下一步" class="btnBuy btnBuy-gray" disabled="disabled" /></div>

<!--弹出层-->
<div id="mask" style="display: none"></div>
<div id="alertLayer" class="bg-white" style="display: none; width: 90%; min-height: 20%;">
    <div class="withdrawIntro detailIntro">
        <h3 class="tc font-tit-normal orange">重要提示</h3>
        <div class="text pb15 font-text-sm" id="chanceInfo">努力计算后，由于华安宝利,华安中小盘成长混合,国寿安保尊享债券A,博时信用债券A/B基金不满足赎回限制，需增加其赎回份额，并增加总赎回金额至 <span class="orange">506.25</span>元</div>
        <div class="btnArea btnArea-new tc">
            <a href="javascript:void(0)" class="btnClosed singleBtn">我知道了</a>
        </div>
    </div>
</div>
</body>
</html>