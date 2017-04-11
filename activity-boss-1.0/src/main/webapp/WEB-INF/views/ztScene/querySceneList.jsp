<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
<html>
<head>
<title>智投场景查询</title>
<%@ include file="/WEB-INF/views/common/metas.jsp"%>
<script type="text/javascript">
/* $(function(){
  DatePickerExt.time("createTimeStart");
  DatePickerExt.time("createTimeEnd");
}); */
  /**
   *跳转至新增智投场景页面
   */
  function addSceneInfo() {
    var action = "ztScene/toAddSceneInfo";
    window.location.href = GV.ctxPath + action;
  }
</script>
</head>
<body>
  <div class="Container">
    <div class="Content fontText">
      <!--search star-->
      <div class="information">
        <form id="queryZtsceneForm" method="get"
          enctype="multipart/form-data"
          action="${ctxPath}ztScene/querySceneList">
          <div class="search">
            <div class="search_tit">
              <h2 class="fw fleft f14">智投场景查询</h2>
            </div>
            <div class="search_con">
              <ul class="fix">
                <li><label class="text_tit">场景名称：</label> <input
                  type="text" name="sceneName" class="input_text"
                  value="<c:out value="${sceneName}"/>" /></li>
                <%-- <li><label class="text_tit">开始时间：</label> <input
                  type="text" id="createTimeStart" name="createTimeStart"
                  readonly="readonly" class="input_text"
                  value="<c:out value="${createTimeStart}"/>" /></li>
                <li><label class="text_tit">截止时间：</label> <input
                  type="text" id="createTimeEnd" name="createTimeEnd"
                  readonly="readonly" class="input_text"
                  value="<c:out value="${createTimeEnd}"/>" /></li> --%>
                <li><label class="text_tit">审核状态： </label> <select
                  id="checkStatus" name="checkStatus"
                  class="input_text">
                    <option ${empty param.checkStatus?'selected':'' }
                      value="">全部</option>
                    <c:forEach var="item"
                      items="${_textResource.getTextMap('zt_scene_status') }">
                      <option
                        ${item.key==param.checkStatus?'selected':''}
                        value="${item.key}">${item.value}</option>
                    </c:forEach>
                </select></li>
              </ul>
              <div class="btn">
                <input type="submit" class="btn_sure fw" value="查询" />
                <input type="button" onclick="clearAll();"
                  class="btn_cancel fw" value="清空" /> <input
                  type="button" class="btn_sure fw"
                  onclick="addSceneInfo()" value="新增场景" />
              </div>
              <div class="clearer"></div>
            </div>
          </div>
        </form>
      </div>
      <div class="clearer"></div>
      <div class="result">
        <h2 class="fw">查询结果</h2>
        <q:table queryService="ztSceneQueryService"
          queryKey="queryZtScene" template="querytableForUnion"
          formId="queryZtsceneForm" class="table_list" pageSize="15">
          <q:nodata>无符合条件的记录</q:nodata>
          <q:column title="序号" value="${id}" />
          <q:column title="场景名称" value="${scene_name}" />
          <q:column title="场景描述" value="${scene_desc}" />
           <q:column title="类型">
            <c:forEach var="item"
              items="${_textResource.getTextMap('zt_scene_type') }">
              <c:if test="${item.key == scene_type }">
                <c:out value="${item.value }"></c:out>
              </c:if>
            </c:forEach>
          </q:column>
          <q:column title="审核状态">
            <c:forEach var="item"
              items="${_textResource.getTextMap('zt_scene_status') }">
              <c:if test="${item.key == check_status }">
                <c:out value="${item.value }"></c:out>
              </c:if>
            </c:forEach>
          </q:column>
          <q:column title="创建时间"
            value="${_messageFormater.formatDate(create_time)}" />
          <q:column title="创建人" value="${creator}" />
          <q:column title="审核时间"
            value="${_messageFormater.formatDate(check_time)}" />
          <q:column title="审核人" value="${checker}" />
          <q:column title="操作" escapeHtml="false">
            <a title="详情查看" href="querySceneDetailInfo?id=${id }">查看</a>
            <a title="修改" href="toUpdateSceneInfo?id=${id }">修改</a>
          </q:column>
        </q:table>
      </div>
      <div class="clearer"></div>
      <br />
    </div>
  </div>
</body>
</html>
