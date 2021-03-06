<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<html>
<head>
<title>抽奖券详情</title>
<%@ include file="/WEB-INF/views/common/metas.jsp"%>
<%-- <script src="${resourceUrl}/js/product.js"></script> --%>
<script type="text/javascript">

  $(function () {
  
  });
</script>
</head>
<body>
	<div class="Container">
		<div class="Content fontText">
			<div class="information">
			    <div class="clear"></div>
			    <div class="search_tit">
					<h2 class="fw fleft f14">抽奖券详情</h2>
				</div>
			      <div class="input_cont">
			        <ul>
			        <li>
			            <label class="text_tit">抽奖券编码：</label>
			            <c:out value="${raffleTicketDto.raffleTicketCode }"></c:out>
			          </li>
			          <li>
			            <label class="text_tit">抽奖券名称：</label>
			            <c:out value="${raffleTicketDto.raffleTicketName }"></c:out>
			          </li>
			          <%-- <li><label class="text_tit">抽奖券类型：</label>
			          	  <c:if test="${raffleTicketDto.raffleTicketType == 'FULL_ADD'}">
			          	  	   满加券
			          	  </c:if>
			          	  <c:if test="${raffleTicketDto.raffleTicketType == 'INTEREST_ADD'}">
			          	  	   利息券
			          	  </c:if>
					  </li> --%>
			          <%-- <li id="raffleTicketAmount_li">
			            <label class="text_tit">抽奖券金额：</label>
						  <c:out value="${raffleTicketDto.raffleTicketAmount }"></c:out>
			          </li>
			          <li id="increaseInterest_li">
			            <label class="text_tit">加息利息：</label>
						  <c:out value="${raffleTicketDto.increaseInterest }"></c:out>
			          </li>
			          <li >
			            <label class="text_tit">可使用金额：</label>
						  <c:out value="${raffleTicketDto.minInvestAmount }"></c:out>
			          </li>
			          <li id="discountType_li">
			            <label class="text_tit">优惠类型：</label>
			          	  <c:if test="${raffleTicketDto.discountType == 'INTEREST'}">
			          	  	   利息
			          	  </c:if>
			          	  <c:if test="${raffleTicketDto.discountType == 'PRINCIPAL'}">
			          	  	   本息
			          	  </c:if>
			          </li> --%>
			          <li>
			            <label class="text_tit">抽奖券数量：</label>
						  <c:out value="${raffleTicketDto.totalCount }"></c:out>
			          </li>
			          <li>
			            <label class="text_tit">抽奖券已发数量：</label>
						  <c:out value="${raffleTicketDto.grantCount }"></c:out>
			          </li>
			          <li>
			            <label class="text_tit">有效期类型：</label>
						  <c:out value="${raffleTicketDto.validityType }"></c:out>
			          </li>
			          <li id="validityDays_li" >
			            <label class="text_tit">有效期天数：</label>
						  <c:out value="${raffleTicketDto.validityDays }"></c:out>
			          </li>
			          <li id="validityDate_li">
			            <label class="text_tit">有效期截止日期：</label>
						  <c:out value="${_messageFormater.formatDate(raffleTicketDto.validityDate) }"></c:out>
			          </li>
			          <%-- <li>
			            <label class="text_tit">可使用范围：</label>
			            <table>
			            	<c:forEach items="${raffleTicketLevelList}" var="raffleTicketLevel" varStatus="num">
		            			<tr>
		            				<td>
					  					<c:out value="${raffleTicketLevel.channelCode }-${raffleTicketLevel.bigColumnCode }-${raffleTicketLevel.smallColumnCode }" />
			            			</td>
							 	</tr>
			            	</c:forEach>
			            </table>
			          </li> --%>
			          <%-- <li>
			            <label class="text_tit">规则说明：</label>
			            <p>
						  <c:out value="${raffleTicketDto.ruleDesc }"></c:out>
			            </p>
			          </li> --%>
			          <%-- <form name="updateraffleTicketForm" id="updateraffleTicketForm" action="raffleTicketCheck" method="post">
			          	  <li>
				            <label class="text_tit">规则说明：</label>
				            <p>
				              <input type="hidden" name="id" value="${raffleTicketDto.id }">
				              <input type="hidden" name="version" value="${raffleTicketDto.version }">
				              <input type="hidden" name="operateType" value="updateRuleDesc">
				              <input type="hidden" name="raffleTicketStatus" value="${raffleTicketDto.raffleTicketStatus }">
				              <textarea name="ruleDesc" class="textfield"></textarea>
				              <span style="color: gray; font-size:12px;">说明：最多允许输入200个字</span>
				            </p>
				          </li>
						  <input type="submit" class="btn_sure fw" value="保存" />
			          </form> --%>
			        </ul>
			      </div>
			      <div class="btn">
					<input type="button" onclick="window.history.go(-1)" class="btn_cancel fw" value="取消" />
				  </div>
				  <div class="clearer"></div>
			  </div>
			<br />
		</div>
	</div>
</body>
</html>
