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
    <title>投资计划详情</title>
</head>
<body>
<div id="box" class="scb">
    <div class="layout-wrap scbCard investCard font-white tc">
        <div class="layou-up assetArea pr">
            <a href="zt/asset/toPolicyOrderDetail?policyOrderId=<e:property value="@ztAssetOrderInfoDTO.policyOrderId"/>" class="icon icon-record icon-record1 pa"></a>
            <!-- 跳转该策略的组合交易记录页面 -->
            <a href="zt/policy/toPolicyDetailForAgainQuery?policyOrderId=<e:property value='@ztAssetOrderInfoDTO.policyOrderId'/> " class="cl-detail pa"><e:property value="@ztAssetOrderInfoDTO.policyName"/>·策略详情 <!-- <i class="icon icon-arrow-right"></i> --> </a>
            <a href="javascript:void(0);" class="font-white pr">
                <span class="font-45"><e:property value="@_formater.formatNumber(ztAssetOrderInfoDTO.wealthValue)"/></span>
                <p class="font-14"><e:property value="@ztAssetOrderInfoDTO.policyName"/>总市值（元）</p>
            </a>
        </div>
        <div class="layou-down tc clearfix br-bottom">
            <a class="layou-down-left" href="javascript:void(0);">
                <span class="font-20"><e:property value="@_formater.formatNumber(ztAssetOrderInfoDTO.yesterdayIncome)"/></span>
                <p class="font-10">&nbsp;&nbsp;&nbsp;昨日收益（元）</p>
            </a>
            <a class="layou-down-right" href="javascript:void(0);">
                <span class="font-20"><e:property value="@_formater.formatNumber(ztAssetOrderInfoDTO.totalIncome)"/></span>
                <p class="font-10">&nbsp;&nbsp;&nbsp;累计收益（元）</p>
            </a>
        </div>
        <div class="layou-down tc clearfix">
            <a class="layou-down-left" href="javascript:void(0);">
                <span class="font-20"><e:property value="@_formater.formatNumber(ztAssetOrderInfoDTO.totalInvestAmount)"/></span>
                <p class="font-10">&nbsp;&nbsp;&nbsp;累计购买 (元)</p>
            </a>
            <a class="layou-down-right" href="javascript:void(0);">
                <span class="font-20"><e:property value="@_formater.formatNumber(ztAssetOrderInfoDTO.totalRedemed)"/></span>
                <p class="font-10">&nbsp;&nbsp;&nbsp;累计赎回 (元)</p>
            </a>
        </div>
    </div>
    <!-- 既有申购中又有赎回中 -->
    <e:if test="@ztAssetOrderInfoDTO.totalPurchasing != 0 && ztAssetOrderInfoDTO.totalRedeeming != 0 ">
        <div class="sgBar bg-white">申购中金额：<e:property value="@_formater.formatNumber(ztAssetOrderInfoDTO.totalPurchasing)"/>元，赎回中金额：<e:property value="@_formater.formatNumber(ztAssetOrderInfoDTO.totalRedeeming)"/>元</div>
    </e:if>
    <!-- 只有赎回中，没有申购中 -->
    <e:elseif test="@ztAssetOrderInfoDTO.totalPurchasing == 0 && ztAssetOrderInfoDTO.totalRedeeming != 0">
        <div class="sgBar bg-white">赎回中金额：<e:property value="@_formater.formatNumber(ztAssetOrderInfoDTO.totalRedeeming)"/>元</div>
    </e:elseif>
    <!-- 只有申购中，没有赎回中 -->
    <e:elseif test="@ztAssetOrderInfoDTO.totalPurchasing != 0 && ztAssetOrderInfoDTO.totalRedeeming == 0">
        <div class="sgBar bg-white">申购中金额：<e:property value="@_formater.formatNumber(ztAssetOrderInfoDTO.totalPurchasing)"/>元</div>
    </e:elseif>
    
    <div class="currentC tc">— 当前持仓 —</div>
    <e:if test="@fundSharelist != null && fundSharelist.size() != 0">
    <e:iterator list="@fundSharelist" var="item">
        <div class="myInvest groupList bg-white radius1" onclick="window.location.href='<e:property value="@fundSalesUrl"/>show/archivesIndex/<e:property value="@item.fundCode"/>?memberNo=<e:property value="@ascMemberNo"/>&src=roboAdvisor'">
        <a href="javascript:void(0)">
            <h3 class="investTitle br-bottom"><e:property value="@item.fundName"/> <i class="icon icon-arrow-right fr"></i></h3>
        </a>
            <div class="groupCon tc">
                <ul>
                    <li>
                        <p><span class="font-tit-normal dark-gray"><e:property value="@_formater.formatNumber(item.worthValue)"/></span></p>
                        <p>最新市值 (元)</p>
                    </li>
                    <li>
                        <e:if test="@ztAssetOrderInfoDTO.wealthValue == 0">
                            <p><span class="font-tit-normal dark-gray">0.00%</span></p>
                        </e:if>
                        <e:else>
                            <p><span class="font-tit-normal dark-gray"><e:property value="@_formater.formatNumber(item.worthValue / ztAssetOrderInfoDTO.wealthValue * 100)"/>%</span></p>
                        </e:else>
                        <p>市值占比</p>
                    </li>
                    <li>
                        <p><span class="font-tit-normal dark-gray"><e:property value="@item.netValue"/></span></p>
                        <p>最新净值 (元)</p>
                    </li>
                </ul>
            </div>
        </div>
    </e:iterator>
    </e:if>
    <!-- 
    <div class="myInvest groupList bg-white radius1">
        <h3 class="investTitle br-bottom"><a href="#">诺德成长优势</a> <i class="icon icon-arrow-right fr"></i></h3>
        <div class="groupCon tc">
            <ul>
                <li>
                    <p><span class="font-tit-normal dark-gray">122.48</span></p>
                    <p>最新市值 (元)</p>
                </li>
                <li>
                    <p><span class="font-tit-normal dark-gray">12%</span></p>
                    <p>市场占比</p>
                </li>
                <li>
                    <p><span class="font-tit-normal dark-gray">122.48</span></p>
                    <p>最新净值 (元)</p>
                </li>
            </ul>
        </div>
    </div> -->
    <div class="h1-6"></div>
    <div class="btn-group">
        <input type="button" value="赎回" class="btn-inout" onclick="window.location.href='zt/redeem/toRedeem?policyOrderId=<e:property value="@ztAssetOrderInfoDTO.policyOrderId"/>'"/>
        <input type="button" value="追加购买" class="btn-inout btn-in" onclick="window.location.href='zt/groupPurchase/toPurchase?policyOrderId=<e:property value="@ztAssetOrderInfoDTO.policyOrderId"/>'"/>
    </div>
</div>
</body>
</html>