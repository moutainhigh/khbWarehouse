<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<html>
<head>
<title>新增事件</title>
<%@ include file="/WEB-INF/views/common/metas.jsp"%>
<script type="text/javascript">
  $(function () {
    //初始化
    $(":checkbox").attr('checked',false);
    //$(".oddsJs").val(0);
    //监听checkbox
    $(":checkbox").click(function(){
      if(this.checked==false){
        //隐藏和修改name
         $(this).parent().siblings().eq(0).find('input').val('');
        $(this).parent().siblings().eq(0).find('input').attr('readonly','readonly');
        $(this).parent().siblings().eq(0).hide();
        $(this).parent().siblings().eq(1).attr('name','');
        $(this).parent().siblings().eq(0).find('input').attr('name','')
        $(this).parent().siblings().eq(0).find('select').attr('name','')
      }else{
        $(this).parent().siblings().eq(0).find('input').attr('readonly','');
        $(this).parent().siblings().eq(0).show();
        $(this).parent().siblings().eq(1).attr('name','versions');
        $(this).parent().siblings().eq(0).find('input').attr('name','odds')
        $(this).parent().siblings().eq(0).find('select').attr('name','levels')
      }
    });
	//校验
    $("#addActionForm").submit(function(){
      if(!validate()){
        return false;
      }
          return $(this).validateSubmit({
            actionCode: {
                  req: true,
                  label: "事件编码"
              },
              actionName: {
                  req: true,
                  label: "事件名称"
              },
              prizes: {
                  req: {
                      min: 1
                  },
                  label: "奖品配置"
              }
          });
      });
    function validate(){
      //不允许为空
      var checkBoxs=$(":checkbox");
      if(checkBoxs.length==0){
        alert("奖品数量为0，请先配置奖品");
        return false;
      }
     /* 
       for(var i=0;i<checkBoxs.length;i++){
        if(checkBoxs.eq(i).attr('checked')==true){
         var text= checkBoxs.eq(i).parent().siblings().eq(0).find('input').val();
         if(text==null||text==''){
           alert("已勾选的奖品概率不能为空");
           return false;
         }
        }else{
          continue;
        }
      }  */
       return true;
    /*   var odds=$("input[name='odds']");
      var num=0;
       for(var i=0;i<odds.length;i++){
        if(odds.eq(i).val()==''){
          continue;
        }
        //验证概率格式
       
        var result=/^[0-9]{1,2}|100\.{0,1}[0-9]{0,4}$/.test(odds.eq(i).val());
        if(!result){
          alert("奖品概率不能超过100%并且小数位不能超过4位");
          return false;
        }
        num=num+parseFloat(odds.eq(i).val());
      }
      if(num.toFixed(4)!=100){
        alert("奖品总概率必须等于100%");
        return false;
      }else{
        return true;
      } 
      //奖品等级唯一性
      var prizeLevelOnlyFlag=false;
      var prizeLevels=$("select");
      for(var i=0;i<prizeLevels.length;i++){
        if(prizeLevels.eq(i).val()==''){
          continue;
        }
        for(var j=i+1;j<prizeLevels.length;j++){
          if(prizeLevels.eq(j).val()==''){
            continue;
          }
          if(prizeLevels.eq(i).val()==prizeLevels.eq(j).val()){
            prizeLevelOnlyFlag=true;
            break;
          }
          continue;
        }
        if(prizeLevelOnlyFlag==true){
          break;
        }
        continue;
      }
      if(prizeLevelOnlyFlag==true){
        alert("奖品等级不能相同");
        return false;
      }
      return true;*/
    } 
  });
  
</script>
</head>
<body>
  <div class="Container">
    <div class="Content fontText">
      <div class="information">
          <div class="clear"></div>
          <div class="search_tit">
          <h2 class="fw fleft f14">事件新增</h2>
        </div>
          <form name="addActionForm" id="addActionForm" action="addEvent" method="post">
            <div class="input_cont">
              <ul>
                <li>
                  <label class="text_tit">事件编码：</label>
                  <input class="input_text" type="text" name="actionCode" id="actionCode">&nbsp;<font color="red">*请保证唯一性</font>
                </li>
                <li>
                  <label class="text_tit">事件名称：</label>
                  <input class="input_text" type="text" name="actionName" id="actionName">&nbsp;<font color="red">*</font>
                </li>
                <li>
                  <label class="text_tit">奖品与概率配置：</label>
                  <table>
                    <c:forEach items="${result}" var="itmes" varStatus="num">
                        <tr>
                          <td>
                           <span>  <input name="prizes" type="checkbox" value="${itmes.id }"/>
                           奖品序号：${itmes.id }；
                           奖品名称： ${itmes.prizeName } ；</span>
                      <span style="display:none"> 中奖概率： <input class="oddsJs" type="text" maxlength='7' style="width:45px;" readonly="readonly"  />% ； 
                           奖品等级： <select id="levels" >
                    <c:forEach var="item"
                      items="${_textResource.getTextMap('activity_prize_level') }">
                      <option
                        value="${item.key}">${item.value}</option>
                    </c:forEach>
                           </select></span> 
                         <input  type="hidden" value="${itmes.version}"  />
                         <span style="width:30px"></span></td>
                  </tr>
                    </c:forEach>
                  </table>
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
