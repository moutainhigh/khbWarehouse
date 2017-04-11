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
    <link rel="stylesheet" href="static/css/LM-invest.css?v=<%=sysVersion %>">
     <script type="text/javascript" src="static/js/zt/purchaseSuccess.js?v=<%=sysVersion %>"></script>
    <title>购买成功</title>
</head>
<body>
<input type="hidden" id="fee" value="<e:property value="@fee"/>">
<input type="hidden" id="policyOrderId" value="<e:property value="@policyOrderId"/>">
<div class="bg-white">
    <div class="pr">
        <img src="static/images/invest/groupBuySuccess.jpg" class="repeatImg">
        <div class="investSuccess tc pa">
            <p><e:property value="@policyName"/></p>
            <span class="red sum"><e:property value="@purMoney"/>元</span>
        </div>
        <div class="tipArea tc">
            <p class="light-gray font-text-note">离您
            <e:iterator list="@_textResource.getTextMap('lmzt_order_relevant_enum')" var="item">
		    <e:if test="@item.key == sceneName">
			<e:property value="@item.value"/>
		   </e:if>
	       </e:iterator>心愿更近一步啦</p>
            <e:if test="${!empty thisMonthBuy}">
                  <p class="orange font-text-sm">购买金额不足本月定投金额<br/>
                您本月还需再购买<e:property value="@thisMonthBuy"/>元</p>
            </e:if>
            <e:if test="${!empty nextMonthBuy}">
             <p class="orange font-text-sm">为了达成您的愿望<br/>
                您下月还需再购买<e:property value="@nextMonthBuy"/>元</p>
            </e:if>
        </div>
        <div class="I-itemList dark-gray radius1">
				<ul>
					<e:if test="${!empty fundList}">
					<e:iterator list="@fundList" var="items">
					<li><span class="fr"><e:property value="@items.amount"/>元</span> <e:property value="@items.fundName"/></li>
					</e:iterator>
					</e:if>
				</ul>
			</div>
        <div class="mb15">
            <div class="flowList pr">
                <span class="bg-t pa"></span>
                <span class="bg-b pa"></span>
                <ul class="applyFlow">
                    <li class="on">
                        <i class="icon icon-circle"></i> 支付成功
                        <p><e:property value="@buyDay"/></p>
                        <p>实扣金额：<e:property value="@purMoney"/>元 手续费金额：<e:property value="@fee"/>元</p>
                    </li>
                    <li>
                        <i class="icon icon-circle"></i> 基金公司确认份额，开始计算收益 <a href="javascript:void(0)" class="btnClick"><i class="icon icon-tips"></i></a>
                        <p><e:property value="@comfirmDay"/></p>
                    </li>
                    <li><i class="icon icon-circle"></i> 查看收益 <a href="javascript:void(0)" class="btnClick-3"><i class="icon icon-tips"></i></a>
                        <p><e:property value="@incomeDay"/>  18:00前</p>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <div class="btnArea">
        <a class="singleBtn first fl" href="zt/asset/toMyPolicyInvest">查看我的灵机一投</a>
        <a class="singleBtn fl" href="zt/groupPurchase/toPurchase?policyOrderId=<e:property value="@policyOrderId"/>">继续购买</a>
    </div>
</div>


<!--弹出层-->
<div id="mask"></div>
<div id="alertLayer" class="bg-white" style="display: none; width: 90%; min-height: 33%;">
    <div class="withdrawIntro">
        <h3 class="orange tc">份额说明</h3>
        <div class="pb15 tc">
            发起申购后的第二个工作日，基金公司接收用户的交易申请并计算应得的份额。确认成功后，用户可以查询到自己所持有的份额和相应的资产数据。<br/>
            组合申购，即用户一次发起多只基金的申购，基金份额将分别确认。
        </div>
        <div class="tc mt15"><a href="javascript:void(0)" class="btnClosed btn-small">我知道了</a></div>
    </div>
</div>

<div id="alertLayer-3" class="bg-white" style="display: none; width: 90%; min-height: 33%;">
    <div class="withdrawIntro">
        <h3 class="orange tc">收益说明</h3>
        <div class="pb15">
            基金份额确认后，当日即会产生盈亏，手续费从申购金额中自动扣除，默认记入首日亏损。<br/>
            但下一个工作日凌晨之后才可查询。<br/>
            组合总盈亏，为组合包含基金的盈亏总和。
        </div>
        <div class="tc mt15"><a href="javascript:void(0)" class="btnClosed btn-small">我知道了</a></div>
    </div>
</div>
</body>
</html>