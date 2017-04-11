<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<html>
<head>
<title>流量通道规则查询</title>
<%@ include file="/WEB-INF/views/common/metas.jsp"%>
<%-- <script src="${resourceUrl}/js/product.js"></script> --%>
<script type="text/javascript">
$(function(){
	  DatePickerExt.time("createTimeStart");
	  DatePickerExt.time("createTimeEnd");
	});
function deleteSrcFlowPlat(id){
    if(!confirm("确认删除？？？")){
      return;
    }
    var action = "fluxPlat/deleteSrcFlowPlat?id="+id;
    window.location.href = GV.ctxPath + action;
  }
        /**
         *跳转至新增活动页面
         */
        function toAddFluxPlat () {
         	var action = "fluxPlat/toAddFluxPlat";
          window.location.href = GV.ctxPath + action;
        }
</script>
</head>
<body>
	<div class="Container">
		<div class="Content fontText">
			<!--search star-->
			<div class="information">
				<form id="queryFluxListForm" method="get"
					action="${ctxPath}fluxPlat/toQuerySrcFlowPlatList">
					<div class="search">
						<div class="search_tit">
							<h2 class="fw fleft f14">送流量查询</h2>
						</div>
						<div class="search_con">
							<ul class="fix">
								<li><label class="text_tit">渠道号：</label> <input
									type="text" name="srcNo" class="input_text"
									value="<c:out value="${srcNo}"/>" /></li>
								<li><label class="text_tit">创建起始时间：</label>
									<input type="text" name="createTimeStart" class="input_text" id="createTimeStart" value="${createTimeStart}" readonly="true" />
								</li>
								<li><label class="text_tit">创建截止时间：</label>
                  					<input type="text" name="createTimeEnd" class="input_text" value="${createTimeEnd}" id="createTimeEnd" readonly="true" />
								</li>
							</ul>
							<div class="btn">
								<input type="submit" class="btn_sure fw" value="查询" />
								<input type="button" onclick="clearAll();" class="btn_cancel fw" value="清空" />
								<input type="button" class="btn_sure fw" onclick="toAddFluxPlat()" value="新增规则" />
								<!-- <input type="button" class="btn_sure fw" onclick="addCoupon()" value="新增送流量" /> -->
							</div>
							<div class="clearer"></div>
						</div>
					</div>
				</form>
			</div>
			<div class="clearer"></div>
			<div class="result">
				<h2 class="fw">查询结果</h2>
				<q:table queryService="srcFlowPlatQueryService" queryKey="querySrcFlowPlatList"
					template="querytableForUnion" formId="querySrcFlowPlatListForm"
					class="table_list" pageSize="15">
					<q:nodata>无符合条件的记录</q:nodata>
					<q:column title="序号" value="${id}" width="40px" />
					<q:column title="渠道号" value="${src_no}" 	/>
					<q:column title="手机号号段" value="${mobile_section}" />
					<q:column title="流量平台号" value="${flux_plat_code}" />
					<q:column title="创建时间" value="${_messageFormater.formatDate(create_time)}" />
					<q:column title="操作人" value="${operator}" />
					<q:column title="最后修改时间" value="${_messageFormater.formatDate(last_update_time)}" />
					<q:column title="操作" escapeHtml="false" >
						  <a title="修改" href="toModifySrcFlowPlat?id=${id }">修改</a>
                          <a title="删除" href="javascript:deleteSrcFlowPlat('${id}')">删除</a>
					</q:column>
				</q:table>
			</div>
			<div class="clearer"></div>
			<br />
		</div>
	</div>
</body>
</html>
