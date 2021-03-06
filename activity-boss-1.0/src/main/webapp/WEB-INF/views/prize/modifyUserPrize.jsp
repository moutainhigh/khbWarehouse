<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<html>
<head>
<title>修改奖品</title>
<%@ include file="/WEB-INF/views/common/metas.jsp"%>
<%-- <script src="${resourceUrl}/js/product.js"></script> --%>
<script type="text/javascript">

  $(function () {
	  
 	  $("#modifyPrize").submit(function(){
           return $(this).validateSubmit({});
       });

	  DatePickerExt.date("validityDate");
	
	$("#selectAll").change(function() {
		selectAll();
	});
	
	//根据奖品的类型和发放方式初始化信息
	$(document).ready(function() {
	  //奖品类型
	  var prizeOfType = $("#prizeOfType").val();
	  $("#prizeType").val(prizeOfType);
	  //发放方式
	  var wayOfType = $("#wayOfType").val();
	  $(".grantWay").val(wayOfType);
	  
	  init(prizeOfType);
	});

  //奖品类型改变展示不同的奖品
  $("#prizeType").change(function() {
      //移除被选中的项
	  $(".radioChecked").removeAttr("checked");
	  var prizeType = $(this).children('option:selected').val();
	  init(prizeType);
  });
  
  function init(prizeType) {
  //优惠券
	  if("COUPON" == prizeType) {
		  $("#coupon").show();
		  $("#raffleTicket").hide();
		  $("#goods").hide();
		  //奖品发放方式，自动
		  $(".grantWay").val("AUTOMATIC")
	  } else if("RAFFLETICKET" == prizeType){
	      //抽奖券
	      $("#raffleTicket").show();
		  $("#coupon").hide();
		  $("#goods").hide();
		  //奖品发放方式，自动
		  $(".grantWay").val("AUTOMATIC")
	  } else {
	      //商品
	      $("#goods").show();
		  $("#raffleTicket").hide();
		  $("#coupon").hide();
		  //奖品发放方式，物流
		  $(".grantWay").val("LOGISTICS")
	  }
  }
	
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
					<h2 class="fw fleft f14">修改奖品</h2>
				</div>
			    <form name="modifyUserPrize" id="modifyUserPrize" action="modifyUserPrize" method="post">
			      <div class="input_cont">
			        <ul>
			          <li style="display:none">
			            <input  value="${userPrizeDto.id }" name="id" id="id"  label="主键">
			            <input  value="${userPrizeDto.version }" name="version" id="version"  label="版本号">
			          </li>
			          <li>
			            <label class="text_tit">奖品名称：</label>
			            <c:out value="${userPrizeDto.prizeName}"></c:out>
			          </li>
			          <li>
			            <label class="text_tit">用户手机号：</label>
			            <c:out value="${userPrizeDto.memberTel}"></c:out>
			          </li>
					  <li><label class="text_tit">奖品发放方式：</label>
					  <input name="grantWay"  value="${userPrizeDto.grantType }" class="grantWay" style="display:none"/>
						  <select class="grantWay"  disabled class="input_text" req="true" label="奖品发放方式" >
								<c:forEach var="item"
									items="${_textResource.getTextMap('activity_grant_way') }">
									<option ${item.key==param.grantWay?'selected':''}
										value="${item.key}">${item.value}</option>
								</c:forEach>
	                	  </select>&nbsp;<font color="red">*</font>
					  </li>
					  <li>
			            <label class="text_tit">奖品物流单号：</label>
			            <input class="input_text" type="text" value="" name="trackingNumber" id="trackingNumber" req="true" label="奖品物流单号">&nbsp;<font color="red">*</font>
			          </li>
			          <li>
			            <label class="text_tit">备注说明：</label>
			            <p>
			              <textarea name="memo" class="textfield"></textarea>
			              <span style="color: gray; font-size:12px;">说明：最多允许输入200个字</span>
			            </p>
			          </li>
			         
			        </ul>
			      </div>
			      <div class="btn">
					<input type="submit" class="btn_sure fw" value="保存" />
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
