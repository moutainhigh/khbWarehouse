<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<html>
<head>
<title>新增抽奖券</title>
<%@ include file="/WEB-INF/views/common/metas.jsp"%>
<%-- <script src="${resourceUrl}/js/product.js"></script> --%>
<script type="text/javascript">

  $(function () {
	// From数据校验
// 	  var valAllList = {
// 		  raffleTicketName: {
// 	      req: true,
// 	      label: "抽奖券名称"
// 	    }
// 	  };
// 	  ValidateExt.listen("addRaffleTicketForm",valAllList);
	  
 	  $("#modifyRaffleTicket").submit(function(){
           return $(this).validateSubmit();
       });

	  DatePickerExt.date("validityDate");

// 	抽奖券类型
	  /* $("#raffleTicketType").change(function() {
		
		  var raffleTicketType = $(this).children('option:selected').val();
		  if("INTEREST_ADD" == raffleTicketType) {
			  $("#increaseInterest_li").show();
			  $("#increaseInterest").attr("req","true");
			  
			  $("#raffleTicketAmount").removeAttr("req");
		      $("#raffleTicketAmount").val("");
			  $("#raffleTicketAmount_li").hide();
			  
			  //优惠类型
			  $("#discountType_li").hide();
			  
			  
		  } else {
			  $("#increaseInterest").removeAttr("req");
		      $("#increaseInterest").val("");
			  $("#increaseInterest_li").hide();
			  
			  $("#raffleTicketAmount_li").show();
			  $("#raffleTicketAmount").attr("req","true");

			  $("#discountType_li").show();
		  }
	  }); */
	
// 	有效期类型
	$("#validityType").change(function() {
		
		var validityType = $(this).children('option:selected').val();
		if("FLOAT" == validityType) {
			$("#validityDays_li").show();
			$("#validityDays").attr("req","true");
			
			$("#validityDate_li").hide();
			$("#validityDate").val("");
			$("#validityDate").removeAttr("req");

		} else if("FIXED" == validityType) {
			$("#validityDate_li").show();
			$("#validityDate").attr("req","true");
			
			$("#validityDays").val("");
			$("#validityDays_li").hide();
			$("#validityDays").removeAttr("req");
		} else {
			$("#validityDate").val("");
			$("#validityDays").val("");
			$("#validityDate_li").hide();
			$("#validityDays_li").hide();
			$("#validityDate").removeAttr("req");
			$("#validityDays").removeAttr("req");
		}
	});
	
	$("#selectAll").change(function() {
		selectAll();
	});
	
	$(document).ready(function() {
	    var $dayOrdate = $("#dayOrdate");
		if("FLOAT" == $dayOrdate.val()) {
		  $("#validityType").val("FLOAT");
			$("#validityDays_li").show();
			$("#validityDays").attr("req","true");
			
			$("#validityDate_li").hide();
			$("#validityDate").val("");
			$("#validityDate").removeAttr("req");

		} else if("FIXED" == $dayOrdate.val()) {
		   $("#validityType").val("FIXED");
			$("#validityDate_li").show();
			$("#validityDate").attr("req","true");
			
			$("#validityDays").val("");
			$("#validityDays_li").hide();
			$("#validityDays").removeAttr("req");
		}
	});
	
  });
  
  //复选框事件  
  //全选、取消全选的事件  
  /* function selectAll(){  
      if ($("#selectAll").attr("checked")) {  
          $(":checkbox").attr("checked", true);  
      } else {  
          $(":checkbox").attr("checked", false);  
      }  
  }  */ 
  
</script>
</head>
<body>
	<div class="Container">
		<div class="Content fontText">
			<div class="information">
			    <div class="clear"></div>
			    <div class="search_tit">
					<h2 class="fw fleft f14">修改抽奖券</h2>
				</div>
			    <form name="modifyRaffleTicket" id="modifyRaffleTicket" action="modifyRaffleTicket" method="post">
			      <div class="input_cont">
			        <ul>
			          <li style="display:none">
			            <input  value="${raffleTicketDto.id }" name="id" id="id"  label="主键">
			            <input  value="${raffleTicketDto.version }" name="version" id="version"  label="版本号">
			          </li>
			          <li>
			            <label class="text_tit">抽奖券编码：</label>
			            <input class="input_text" type="text" value="${raffleTicketDto.raffleTicketCode }" name="raffleTicketCode" id="raffleTicketCode" req="true" label="抽奖券编码">&nbsp;<font color="red">*</font>
			          </li>
			          <li>
			            <label class="text_tit">抽奖券名称：</label>
			            <input class="input_text" type="text" value="${raffleTicketDto.raffleTicketName }" name="raffleTicketName" id="raffleTicketName" req="true" label="抽奖券名称">&nbsp;<font color="red">*</font>
			          </li>
			          <%-- <li><label class="text_tit">抽奖券类型：</label>
						  <select id="raffleTicketType" name="raffleTicketType" class="input_text" req="true" label="抽奖券类型">
								<c:forEach var="item"
									items="${_textResource.getTextMap('activity_raffleTicket_type') }">
									<option ${item.key==param.raffleTicketType?'selected':''}
										value="${item.key}">${item.value}</option>
								</c:forEach>
	                	  </select>&nbsp;<font color="red">*</font>
					  </li> --%>
			          <!-- <li id="raffleTicketAmount_li">
			            <label class="text_tit">抽奖券金额：</label>
			            <input class="input_text" type="text" name="raffleTicketAmount" id="raffleTicketAmount" req="true" label="抽奖券金额">&nbsp;<font color="red">*</font>
			          </li>
			          <li id="increaseInterest_li" style="display: none" >
			            <label class="text_tit">加息利息：</label>
			            <input class="input_text" type="text" id="increaseInterest" name="increaseInterest"  label="加息利息">&nbsp;<font color="red">*</font>
			          </li>
			          <li >
			            <label class="text_tit">可使用金额：</label>
			            <input class="input_text" type="text" name="minInvestAmount" id="minInvestAmount">
			          </li> -->
			          <!-- <li id="discountType_li">
			            <label class="text_tit">优惠类型：</label>
			            <select id="discountType" name="discountType" class="input_text" req="true" label="优惠类型">
						     <option value="INTEREST">利息</option>
						     <option value="PRINCIPAL">本息</option>
	                	</select>&nbsp;<font color="red">*</font>
			          </li> -->
			          <li>
			            <label class="text_tit">抽奖券数量：</label>
			            <input class="input_text" type="text" name="totalCount" value="${raffleTicketDto.totalCount }" id="totalCount" req="true" label="抽奖券数量">&nbsp;<font color="red">*</font>
			          </li>
			          <%-- <li>
			            <label class="text_tit">有效期类型：</label>
			            <input style="display:none" value="${raffleTicketDto.validityType }" id="dayOrdate">
			            <select id="validityType" name="validityType"  class="input_text" req="true" label="有效期类型">
						     <option value="FLOAT">领取后多少天</option>
						     <option value="FIXED">固定截止日期</option>
	                	</select>&nbsp;<font color="red">*</font>
			          </li>
			          <li id="validityDays_li" >
			            <label class="text_tit">有效期天数：</label>
			            <input class="input_text" type="text" value="${raffleTicketDto.validityDays }" name="validityDays" id="validityDays" req="true" label="有效期天数">&nbsp;<font color="red">*</font>
			          </li>
			          <li id="validityDate_li" style="display: none" >
			            <label class="text_tit">有效期截止日期：</label>
			            <input id="validityDate" class="input_text" value="${raffleTicketDto.validityDate }" type="text" name="validityDate" label="有效期截止日期">&nbsp;<font color="red">*</font>
			          </li> --%>
			          <!-- <li>
			            <label class="text_tit">可使用范围：</label>
			            <table border="1">
			            	<tr><td><input id="selectAll" type="checkbox"/></td><td>频道</td><td>大栏目</td><td>小栏目</td></tr>
			            	<tr><td><input name="RaffleTicketLevel" type="checkbox" value="FRESHMAN-FRESHMAN-FRESHMAN"/></td><td>新手</td><td>无</td><td>无</td></tr>
			            	<tr><td><input name="RaffleTicketLevel" type="checkbox" value="CURRENT-CURRENT-CURRENT"/></td><td>活期</td><td>无</td><td>无</td></tr>
			            	<tr><td><input name="RaffleTicketLevel" type="checkbox" value="FIXED-SHORT-SHORT"/></td><td>超短期</td><td>无</td><td>无</td></tr>
			            	<tr><td><input name="RaffleTicketLevel" type="checkbox" value="FIXED-MONY-30D"/></td><td>定期</td><td>月月盈</td><td>30日</td></tr>
			            	<tr><td><input name="RaffleTicketLevel" type="checkbox" value="FIXED-MONY-45D"/></td><td>定期</td><td>月月盈</td><td>45日</td></tr>
			            	<tr><td><input name="RaffleTicketLevel" type="checkbox" value="FIXED-QUAY-90D"/></td><td>定期</td><td>季季盈</td><td>三个月</td></tr>
			            	<tr><td><input name="RaffleTicketLevel" type="checkbox" value="FIXED-QUAY-180D"/></td><td>定期</td><td>季季盈</td><td>六个月</td></tr>
			            	<tr><td><input name="RaffleTicketLevel" type="checkbox" value="FIXED-QUAY-270D"/></td><td>定期</td><td>季季盈</td><td>九个月</td></tr>
			            	<tr><td><input name="RaffleTicketLevel" type="checkbox" value="FIXED-YEAY-365D"/></td><td>定期</td><td>年年盈</td><td>一年</td></tr>
			            	<tr><td><input name="RaffleTicketLevel" type="checkbox" value="FIXED-YEAY-540D"/></td><td>定期</td><td>年年盈</td><td>18个月</td></tr>
			            </table>
			          </li> -->
			          <!-- <li>
			            <label class="text_tit">规则说明：</label>
			            <p>
			              <textarea name="ruleDesc" class="textfield"></textarea>
			              <span style="color: gray; font-size:12px;">说明：最多允许输入200个字</span>
			            </p>
			          </li> -->
			        </ul>
			      </div>
			      <div class="btn">
					<input type="submit" class="btn_sure fw" value="修改" />
					<input type="button" onclick="window.history.go(-1)" class="btn_cancel fw" value="取消" />
				  </div>
				  <div class="clearer"></div>
			    </form>
			  </div>
			<br />
		</div>
	</div>
</body>
</html>
