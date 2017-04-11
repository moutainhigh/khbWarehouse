<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<html>
<head>
<title>新增策略</title>
<%@ include file="/WEB-INF/views/common/metas.jsp"%>
<%-- <script src="${resourceUrl}/js/product.js"></script> --%>
<script type="text/javascript">

  $(function () {
	// From数据校验
// 	  var valAllList = {
// 		  couponName: {
// 	      req: true,
// 	      label: "策略名称"
// 	    }
// 	  };
// 	  ValidateExt.listen("addGoodsForm",valAllList);
	  
	  $("#addPolicyForm").submit(function(){
          return $(this).validateSubmit({});
      });

	  DatePickerExt.date("validityDate");

  });
  
  //复选框事件  
  //全选、取消全选的事件  
  function selectAll(){  
      if ($("#selectAll").attr("checked")) {  
          $(":checkbox").attr("checked", true);  
      } else {  
          $(":checkbox").attr("checked", false);  
      }  
  }  
  
</script>
<script type="text/javascript">
$(document).ready(function() {
  $("#policyProductString").val('')
  $("#addFund").click(function() {
    var str = '<tbody><tr><th>占比(小数形式)</th><th>名称</th><th>code</th><th>类型</th><th>起购金额</th></tr>';
    var fundNum = $("#fundNumber").val();
    for(var i = 0;i<fundNum;i++) {
      str = str + '<tr id=tr_'+i+'>';
      str = str + '<td><input class="input_text" type="text"  req="true"></td>'+
      '<td><input class="input_text" type="text"  req="true"></td>'+
      '<td><input class="input_text" type="text"  req="true"></td>'+
      '<td><input class="input_text" type="text"  req="true"></td>'+
      '<td><input class="input_text" type="text"  req="true"></td></tr>'
    }
    str = str +'</tbody>';
    $("#table").html(str);
  });
  
  $("#createFund").click(function() {
    var fundNum = $("#fundNumber").val();
    var str='';
    for(var i = 0;i<fundNum;i++) {
      str = str + $("#tr_"+i).find('td').eq(0).find('input').val() + '?';
      str = str + $("#tr_"+i).find('td').eq(1).find('input').val() + '?';
      str = str + $("#tr_"+i).find('td').eq(2).find('input').val() + '?';
      str = str + $("#tr_"+i).find('td').eq(3).find('input').val() + '?';
      str = str + $("#tr_"+i).find('td').eq(4).find('input').val() + '#';
    }
    str=str.substring(0,str.length-1)
    $("#policyProductString").val(str)
  });
});
</script>
</head>
<body>
	<div class="Container">
		<div class="Content fontText">
			<div class="information">
			    <div class="clear"></div>
			    <div class="search_tit">
					<h2 class="fw fleft f14">策略新增</h2>
				</div>
			    <form name="addPolicyForm" id="addPolicyForm" action="addZtPolicy" method="post">
			      <div class="input_cont">
			        <ul>
			          <li>
			            <label class="text_tit">策略名称：</label>
			            <input class="input_text" type="text" name="policyName" id="policyName" req="true" label="策略名称">&nbsp;<font color="red">*</font>
			          </li>
					  <li>
			            <label class="text_tit">波动性类型 ：</label>
			            <input class="input_text" type="text" name="fluctuate" id="fluctuate" req="true" label="波动性类型 ">&nbsp;<font color="red">(如：小，很小，非常小)*</font>
			          </li>
			          <li>
			            <label class="text_tit">策略类型：</label>
			            <input class="input_text" type="text" name="policyType" id="policyType" req="true" label="策略类型">&nbsp;<font color="red">（如：保守型）*</font>
			          </li>
			          <li>
			            <label class="text_tit">计划愿望：</label>
			            <input class="input_text" type="text" name="sugWish" id="sugWish" req="true" label="计划愿望">&nbsp;<font color="red">*</font>
			          </li>
			          <li>
			            <label class="text_tit">策略介绍：</label>
			            <p>
			              <textarea name="policyDesc" class="textfield" id="policyDesc" req="true" label="策略介绍"></textarea>&nbsp;<font color="red">*</font>
			            </p>
			          </li>
			          <li>
			            <label class="text_tit">相关费用：</label>
			         	<p>
			              <textarea name="costDesc" id="costDesc" req="true" label="相关费用" class="textfield"></textarea>&nbsp;<font color="red">*</font>
			            </p>
			          </li>
			          <li>
			            <label class="text_tit">注意事项：</label>
			          	<p>
			              <textarea name="attentionDesc" id="attentionDesc" req="true" label="注意事项" class="textfield"></textarea>&nbsp;<font color="red">*</font>
			            </p>
			          </li>
			          <li>
			            <label class="text_tit">起购金额：</label>
			            <input class="input_text" type="text" name="minPurchaseAmount" id="minPurchaseAmount" req="true" label="起购金额">&nbsp;<font color="red">*</font>
			          </li>
			          <li>
			            <label class="text_tit">建议投资年限：</label>
			            <input class="input_text" type="text" name="sugInvYear" id="sugInvYear" req="true" label="建议投资年限">&nbsp;<font color="red">*</font>
			          </li>
			          <li>
			            <label class="text_tit">最近成立的基金年月：</label>
			            <input class="input_text" type="text" name="lastEstablishFundYear" id="lastEstablishFundYear" label="最近成立的基金年月">
			          </li>
			          <li>
			            <label class="text_tit">是否重新计算策略收益率：</label>
			            <input class="input_text" type="text" name="whetherReCalculate" id="whetherReCalculate" label="是否重新计算策略收益率">
			          </li>
			          <li>
			            <label class="text_tit">收益率计算状态：</label>
			            <input class="input_text" type="text" name="calculateStatus" id="calculateStatus" label="收益率计算状态">
			          </li>
			          <li>
			            <label class="text_tit">基金串：</label>
			            <input class="input_text" type="text" name="policyProductString" id="policyProductString" req="true" label="基金串">&nbsp;<font color="red">(下方添加完基金规则，点击生成基金串，自动填写)*</font>
			          </li>
			        </ul>
			      </div>
			      <div class="input_cont" style="padding-top:20px;">
			            <input type="button" id="addFund" class="btn_sure fw" value="添加" />(类型： 债劵型 BOND,股票型	STOCK,固定型 FIXED)
			      		<input type="button" id="createFund" class="btn_sure fw" value="生成基金串" />(添加完基金信息后点击此按钮)
			      		<label class="text_tit" style="float:left; width: 160px;text-align: right;margin-right: 10px;">基金个数：</label>
			            <input class="input_text" type="text" id="fundNumber" req="true" label="基金个数">&nbsp;<font color="red">*</font>
			            <div class="rateTable light-gray tc radius1">
			                <table id="table" cellpadding="0" cellspacing="0" width="50%" style="text-align:center">
			                    
			                </table>
            			</div>
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
