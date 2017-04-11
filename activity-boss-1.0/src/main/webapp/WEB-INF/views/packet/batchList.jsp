<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<html>
<head>
<title>红包批次导入列表</title>
<%@ include file="/WEB-INF/views/common/metas.jsp"%>
<%-- <script src="${resourceUrl}/js/product.js"></script> --%>
<script type="text/javascript">

  function toBatchImport() {
	  location.href = '${ctxPath}packet/toBatchRedPachet';
  }
  
  /**
   * 审核通过或退回
   */
  function changeStatus(batchId, status) {
	  var action = "batchCheck";
	  var warnMsg = "";
	  if(status == 'EFFECTIVE') {
		  warnMsg = "确认是否审核通过？";
	  } else {
		  warnMsg = "确认是否退回？";
	  }
	  if(confirm(warnMsg)) {
		  MessageBoxExt.ajax({
			  url : GV.ctxPath + "packet/" + action,
			  data : {
			    "batchId" : batchId,
			    "status" : status
			  },
			  style : "REDIRECT",
			  success : function (result) {
			     //alert(result);
			  }
		  });
	  }
  }
</script>
</head>
<body>
	<div class="Container">
		<div class="Content fontText">
			<!--search star-->
			<div class="information">
				<form id="batchListForm" method="get"
					action="${ctxPath}packet/batchList">
					<div class="search">
						<div class="search_tit">
							<h2 class="fw fleft f14">红包批次列表查询</h2>
						</div>
						<div class="search_con">
							<ul class="fix">
								<li><label class="text_tit">批次号：</label> <input
									type="text" id="batchId" name="batchId" class="input_text"
									value="${batchId}" /></li>
							</ul>
							<ul class="fix">
							<div class="btn">
								<input type="submit" class="btn_sure fw" value="查询" />
								<input type="button" onclick="clearAll();" class="btn_cancel fw" value="清空" />
								<input type="button" class="btn_sure fw" onclick="toBatchImport();" value="批量导入" />
							</div>
							<div class="clearer"></div>
						</div>
					</div>
				</form>
			</div>
			<div class="clearer"></div>
			<div class="result">
				<h2 class="fw">查询结果</h2>
				<q:table queryService="activityQueryService" queryKey="queryPacketBatchList"
					template="querytableForUnion" formId="queryPacketBatchListForm"
					class="table_list" pageSize="50">
					<q:nodata>无符合条件的记录</q:nodata>
					<q:column title="批次号" value="${batch_id}" />
                    <q:column title="状态">
                      <c:forEach var="item" items="${_textResource.getTextMap('activity_coupon_status') }">
                        <c:if test="${item.key == status }">
                        <c:out value="${item.value }"></c:out>
                        </c:if>
                      </c:forEach>
                    </q:column>
					<q:column title="总数量" value="${packetnum}" />
					<q:column title="操作" escapeHtml="false" >
						<c:if test="${status == 'CHECKING'}">
							<a title="审核"
								href="javascript:changeStatus('${batch_id}','EFFECTIVE')">审核通过</a>
							&nbsp;
							<a title="审核"
								href="javascript:changeStatus('${batch_id}','CANCEL')">作废</a>
							&nbsp;
			            </c:if>
						<a title="详情查看" href="batchRedPachetList?batchId=${batch_id }">查看详情</a>
					</q:column>
				</q:table>
			</div>
			<div class="clearer"></div>
			<br />
		</div>
	</div>
</body>
</html>
