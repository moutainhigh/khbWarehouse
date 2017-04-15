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
<!DOCTYPE html>
<html>
<head lang="en">
<link rel="stylesheet" href="static/css/LM-funds.css?v=<%=sysVersion %>">
<script type="text/javascript" src="static/js/Lm-purchase.js?v=<%=sysVersion %>"></script>
<title>基金购买</title>
<script type="text/javascript">
    $(document).ready(function(){
    	
    	amountFlag = false;
		$("#confirmPurchase").click(function(){
			
			var leastPurchaseA = document.getElementById("leastPurchase").value;
			var buyAmountA = document.getElementById("buyAmount").value;

			if(!checkApplyMoney('buyAmount',leastPurchaseA,buyAmountA)){//重新校验输入参数
				return;
			}
			
			if(!amountFlag){//金额框校验失败
				$("#alertLayer-7Message").html("<a><i class=\"icon icon-tips\"></i> 申购金额有误，请检查 </a>");
	             $("#mask").show();
	             $("#alertLayer-7").show();
	             return false;
			}else{ //金额校验无误，交易密码校验
				var tradePwd = $("#tradepwd").val();
				if(tradePwd == '' || tradePwd.length < 6 || tradePwd.length > 20) {//交易密码长度有误
				       $("#error").html(" <i class=\"icon icon-error2\"></i> 请输入6-20位的交易密码");
				       $("#error").css("display","block");
				        return false;
				      }
				 //无错误清除错误提示
			     $("#error").html(" <i class=\"icon icon-error2\"></i> ");
			     $("#error").css("display","none");
			     var fundName = document.getElementById("hiddenFundName").value;
	  	  		$.ajax({
		  		  	url:"zt/groupPurchase/doFundPurchase",
		  		  	async:false,
		  		  	type: "POST",
		  		  	dataType:"json",
		  		  	data:{
		  			  	pwd:$("#tradepwd").val(),
		  			  	fundOrderDetailId:$("#fundOrderDetailId").val(),
		  			  	amount:$("#buyAmount").val(),
		  		 	 },
		  		  	error: function(request) {
		  		  		location.href=window.location.href;
					},
		  		  	success: function(data) {
			  		  	if(data.result=='WRONG_PWD'){//交易密码错误
			                if(data.description.overPlusCount==0){
			             		$("#alertLayer-6Message").html("<i class=\"icon icon-tips\"></i> 账号已冻结,解冻日期 "+ data.description.lockedEndTime);
			            		$("#mask").show();
			             		$("#alertLayer-6").show();
			           		}else{
			             		$("#alertLayer-5Message").html("<i class=\"icon icon-tips\"></i> 交易密码输入错误，您还可再输入"+data.description.overPlusCount+"次");
			             		$("#mask").show();
			             		$("#alertLayer").show();
			           		}
			            	return false;
			         	}else if(data.result == 'SUCCESS'){//交易成功
			         	    $("#tradepwd").val("");
			         		location.href="zt/groupPurchase/toSinglePurchaseSuccess?orderDetailId="+data.data;
			         			
			         	}else if(data.result == 'SYS_EXCEPTION'){//系统异常
			         		  $("#mask").show();
			                  $("#alertLayer-8").show();
			                  return false;
			         	}else if(data.result == 'PWD_ERROR'){
			         		$("#alertLayer-7Message").html("<i class=\"icon icon-tips\"></i> 交易密码格式有误,请输入6-20位的交易密码 ");
				             $("#mask").show();
				             $("#alertLayer-7").show();
				             return false;
			         	}else if(data.result == 'BALANCE_NOT_ENOUGH'){
			         		$("#alertLayer-7Message").html("<a><i class=\"icon icon-tips\"></i> 账户余额不足，请检查 </a>");
				             $("#mask").show();
				             $("#alertLayer-7").show();
				             return false;
			         	}
		  			}
	  		  });
			}
  	  	});
	 });
    
   
	//关闭弹出框
    function clean(){
    	  $("#mask").hide();
    	  $("#alertLayer-6").hide();
    	  $("#alertLayer").hide();
    	  $("#alertLayer-8").hide();
    	  $("#alertLayer-7").hide();
    	}
	
	function toResetPwd(){
		
		
		location.href="jump/toResetPwd";
	}
	
	function toForgetPwd(){
		
		location.href="jump/toForgetPwd";
	}
	
	function goRecharge(){
		
		location.href="asset/toRecharge";
	}
</script>
</head>
<body>
	<div id="box">
		    <input type="hidden" name="fundCode" id="fundCode" value="${fundCode}" /> 
			<input type="hidden" name="hiddenFundName" id="hiddenFundName" value="${fundName}" /> 
			<input type="hidden" name="lmweChat" id="lmweChat" value="${lmweChat}" /> 
			<input type="hidden" name="leastPurchase" id="leastPurchase" value="${leastPurchase}" />
			<input type="hidden" name="fundOrderDetailId" id="fundOrderDetailId" value="${fundOrderDetailId}" />
			<div class="buyCard bg-white pr">
				<ul class="buyInfo buyInfo2">

					<li>
						<table>
							<tr>
								<td width="100px" valign="top"><i
									class="dot orange font-10">●</i> 基金名称：</td>
								<td valign="top">
									<div style="text-align: left;">${fundName}</div>
								</td>
							</tr>
						</table>

					</li>

					<li><i class="dot orange font-10">●</i> 基金代码：${fundCode}</li>
					<li><i class="dot orange font-10">●</i> 近1年涨幅：
					<e:if test="${!empty yearIncrease }">
					<e:if test="${yearIncrease>0 }">
					<span class="red">${yearIncrease}</span>
					</e:if>
					<e:if test="${yearIncrease<0 }">
					<span class="green">${yearIncrease}</span>
					</e:if>
					</e:if>
					</li>
					<e:if test="${empty yearIncrease }">--</e:if>
					<li><i class="dot orange font-10">●</i>单位净值[${tradingDay}]：${unitNv}</li>
				</ul>
				<img src="static/images/bg-buy.png">
			</div>
			<div class="fundBox radius1 bg-white">
				<p>账户余额：${balance }
					元</p>
				<div class="redeemDiv2 orange">
					购买金额：<input type="text" class="amount-text" id="buyAmount"
						name="amount" value="${amount }" readonly
						 /> 元
				</div>
				<p class="modTips moderror" id="buyAmountMsg" style="display: none">
					<i class="icon icon-error2 font-12"></i> 余额不足
				</p>
				<p class="gray font-12" id="buyAmountMsg">
					手续费估算： <span class="orange" id="fee">${calcFee }</span> 元
				</p>
			</div>
			<div class="input-group withdraw-input-group">
				<ul>
					<li style="border: 1px #ddd;">
						<div class="input-wrap bg-white">
							<a href="javascript:void(0)" id="look"
								onclick="changePasswordType('tradepwd')"
								class="icon icon-unlook orange fr"></a> <a
								href="javascript:void(0)" id="errorPwd" onclick="pwdClick()"
								class="icon icon-error fr"></a> <label>交易密码</label> <span><input
								type="password" class="input-text" id="tradepwd" name="tradepwd"
								placeholder="请输入交易密码" /></span>
						</div>
						<div class="error-tips red">
							<a href="account/toResetTradePwd" id="toForgetPwd" class="forgetCode orange fr">忘记密码</a>
							<div id="error" style="display: none"></div>
						</div>

					</li>
					<li class="pt30" id="goPurchaseSpan"><input type="button"
						class="btn-login" id="confirmPurchase" name="confirmPurchase"
						value="确认购买" style="disabled: false" /></li>
					<li class="pt30" style="display: none" id="expectIncomeSpan">
					   <a id="toRecharge" href="asset/toRecharge">
						<input type="button" class="btn-login" 
						name="toRecharge" value="去充值"
						style="disabled: true" /></a>
					</li>
					
				</ul>
			</div>
	</div>
	<div id="mask" style="display: none"></div>
	<div id="alertLayer" class="bg-white radius1"
		style="display: none; width: 90%; min-height: 28%;">
		<div class="pt30 pb30 br-bottom orange tc font-14"
			id="alertLayer-5Message"></div>
		<div class="btnMaskArea tc mt30">
			<a href="javascript:void(0)" onclick="clean()">再次输入</a> <a href="account/toResetTradePwd"
				 class="toResetPwd">忘记密码</a>
		</div>
	</div>

	<div id="alertLayer-6" class="bg-white radius1"
		style="display: none; width: 90%; min-height: 35%;">
		<div class="pt30 pb30 br-bottom orange tc font-14"
			id="alertLayer-6Message">
			<i class="icon icon-tips" href="javascript:void(0)" onclick="clean()"></i>
		</div>
		<div class="btnMaskArea tc mt30">
			<a href="account/toResetTradePwd" class="toResetPwd">忘记密码</a> <a
				href="javascript:void(0)" onclick="clean()">取消</a>
		</div>
	</div>

	<div id="alertLayer" class="bg-white radius1"
		style="display: none; width: 90%; min-height: 28%;">
		<div class="pt30 pb30 br-bottom orange tc font-14"
			id="alertLayer-5Message"></div>
		<div class="btnMaskArea tc mt30">
			<a href="javascript:void(0)" onclick="clean()">再次输入</a> <a href="account/toResetTradePwd"
				class="toResetPwd">忘记密码</a>
		</div>
	</div>

	<!-- 金额输入框校验失败  -->
	<div id="alertLayer-7" class="bg-white radius1"
		style="display: none; width: 90%; min-height: 28%;">
		<div class="pt30 pb30 br-bottom orange tc font-14"
			id="alertLayer-7Message"></div>
		<div class="btnMaskArea tc mt30">
			<a href="javascript:void(0)" onclick="clean()">再次输入</a>
		</div>
	</div>

	<!--系统异常-弹出层-->
	<div id="alertLayer-8" class="unloginMask regMask rechargeMask"
		style="display: none; width: 100%; height: 40%;">
		<div class="pr">
			<img src="static/images/errorMask.png" class="repeatImg"
				alt="" /> <a class="btnClosed font-white pa"
				href="javascript:void(0)"><i class="icon icon-error2"
				onclick="clean()"></i></a>
			<p class="errorCon red pa">系统异常，请稍后重试哦</p>
			<div class="btnMaskArea tc pa">
				<a href="javascript:void(0)" onclick="clean()">OK</a>
			</div>
		</div>
	</div>
</body>
</html>