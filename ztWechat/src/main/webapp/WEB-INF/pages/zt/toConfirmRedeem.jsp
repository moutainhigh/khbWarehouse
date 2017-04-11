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
<!DOCTYPE html>
<html>
<head lang="en">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <!--优先使用 IE 最新版本和 Chrome-->
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="viewport" content="width=device-width, maximum-scale=1.0, initial-scale=1.0, user-scalable=0" />
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <!--设置苹果工具栏颜色-->
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
   <%--  <link rel="stylesheet" href="static/css/LM-common.css?v=<%=sysVersion %>">
    <link rel="stylesheet" href="static/css/LM-app.css?v=<%=sysVersion %>">
    <link rel="stylesheet" href="static/css/icon-style.css?v=<%=sysVersion %>">
    <link rel="stylesheet" href="static/css/LM-invest.css?v=<%=sysVersion %>">
    <script type="text/javascript" src="static/js/jquery-1.8.3.min.js?v=<%=sysVersion %>"></script>
    <script type="text/javascript" src="static/js/LM-app.js?v=<%=sysVersion %>"></script> --%>
    <script type="text/javascript" src="static/js/zt/commonUtil.js"?v=<%=sysVersion %>></script>
    <script type="text/javascript" src="static/js/zt/confirmRedeem.js?v=<%=sysVersion %>"></script>
    <title>确认赎回</title>
</head>
<body>
<input id="policyOrderId" type="hidden" value="<e:property value="@policyOrderId"/>">
<input id="buyMoney" type="hidden" value="<e:property value="@redeemMoney"/>">
<input type="hidden" id="totalEnableMoney" value="<e:property value="@_formater.formatNumber(totalEnableMoney)"/>">
<div class="redemption-Area">
    <div class="main">
        <div class="redemptionTitle">
            <p><i class="icon icon-tips icon-white"></i>以下各基金的预计到账金额按照昨日净值计算，最终到账金额以基金公司计算为准。</p>
        </div>
        <div class="redemptionMain bg-white">
            <h3>赎回明细</h3>
            <table width="100%" class="redemptionTable redemptionTable-page">
                <tr>
                    <td class="tc" width="40%">基金名称</td>
                    <td class="tc" width="30%">赎回份额(份)</td>
                    <td class="tc">预计到账金额(元)</td>
                </tr>
              <%--   <e:iterator list="@fundAndPolicyDtoList" var="items">
                <tr>
                    <td class="tc" width="40%"><e:property value="@items.ztPolicyProductDTO.productName"/></td>
                    <td class="tc" width="30%"><e:property value="@_formater.formatNumber(items.ztPolicyFundShareDto.enableShare*redeemMoney/totalEnableMoney)"/></td>
                    <td class="tc"><e:property value="@_formater.formatNumber((items.ztPolicyFundShareDto.enableShare*redeemMoney/totalEnableMoney)*items.ztPolicyFundShareDto.netValue)"/></td>
                </tr>
                </e:iterator> --%>
                <e:iterator list="@fundAndPolicyDtoList" var="items">
                 <tr>
                    <td class="tc t-1"><e:property value="@items.fundName"/></td>
                    <td class="tc"><e:property value="@_formater.formatNumber(items.enableShare)"/></td>
                    <td class="tc" ></td>
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
        <div class="investBox bg-white">
            <ul class="input-area">
                <li class=" forgetPassword pb">
                    <label class="inputLabel">交易密码</label>
                    <span class="amount-text inp-text fl">
                        <input type="password" class="input-text" id="tradePwd" onblur="onTradePwdBlur()" name="" placeholder="请输入交易密码"/>
                        <a href="javascript:void(0)" class="icon icon-error fr" style="display:none"></a>
                    </span>
                    <a href="javascript:changePasswordType()" class="icon icon-unlook fl" id="look"></a>
                    <div class="error-tips red">
                        <div class="forget-field fl">
                            <a href="account/toResetTradePwd" class="forgetCode fr">忘记密码</a>
                           <span id="errorInfo" style="display:none"> <i class="icon icon-error2"></i> 交易密码错误</span>
                        </div>
                    </div>
                </li>
                <li>
                    <label class="inputLabel">赎回方式</label><span class="light-gray font-text-sm">赎回至您的银行卡(尾号<e:property value="@bankCardInfoDto.cardNo.substring(bankCardInfoDto.cardNo.length()-4)"/>)</span>
                </li>
            </ul>
        </div>

    </div>
</div>
<div class="identifier">
    <p class="tc">持牌销售机构 基金代销牌照编号 000000383</p>
    <p class="tc">由中国证监会和民生银行全程监督</p>
</div>
<div class="btnBot"><input type="button" value="确认赎回" class="btnBuy btnBuy-gray" disabled="disabled" id="sumbit"/></div>

<div id="mask" style="display: none"></div>
<!--交易密码输入错误 弹出层-->
<div id="alertLayer" class="bg-white radius1" style="display: none; width: 90%; min-height: 25%;">
    <div class="errorTexts br-bottom orange tc" id="alertLayer-5Message"></div>
    <div class="btnMaskArea btnMaskArea1 tc">
        <a href="javascript:void(0)" onclick="clean()">再次输入</a>
        <a href="account/toResetTradePwd">重置密码</a>
    </div>
</div>
<!--交易密码输入错误 弹出层-->
<div id="alertLayer-6" class="bg-white radius1" style="display: none; width: 90%; min-height: 25%;">
    <div class="errorTexts br-bottom orange tc" id="alertLayer-6Message"><i class="icon icon-tips" href="javascript:void(0)" onclick="clean()"></i> </div>
    <div class="btnMaskArea btnMaskArea1 tc">
        <a href="account/toResetTradePwd">重置密码</a>
        <a href="javascript:void(0)" onclick="clean()">取消</a>
    </div>
</div>
<!--系统异常-弹出层-->
<div id="alertLayer-8" class="unloginMask regMask rechargeMask" style="display: none; width: 100%; height:40%;">
    <div class="pr">
        <img src="static/images/errorMask.png" class="repeatImg" alt=""/>
        <a class="btnClosed font-white pa" href="javascript:void(0)"><i class="icon icon-error2" onclick="clean()"></i></a>
        <p class="errorCon red pa">系统异常，请稍后重试哦</p>
        <div class="btnMaskArea tc pa">
            <a href="javascript:void(0)" onclick="clean()">OK</a>
        </div>
    </div>
</div>

</body>
</html>