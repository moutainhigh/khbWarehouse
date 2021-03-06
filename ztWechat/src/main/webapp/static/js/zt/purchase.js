/**
 * 初始化参数
 */
var purInputFlag=false;
var pwdInputFlag=false;
var checkBoxFlag=true;
var purChaseList;

$(function(){
  //本月应投金额
  var purAmount=$("#purAmount").val();
  $("#purchase").val(parseFloat(purAmount).toFixed(2));
  if(purAmount!=0){
    purInputFlag=true;
  }
  $("#errorMsg").hide();
  $("#cleanPwd").hide();
  $("#errorInfo").hide();
  buttonToGray();
  //初始化purChaseList
  tabToString(0);
  var accountBalance=$("#accountBalance").val();
  if(parseFloat(accountBalance)<purAmount){
    $("#button").hide();
    $("#pwdArea").hide();
    $("#recharge").show();
    //按钮变
  }else{
    $("#button").show();
//    inintPwd();
    $("#pwdArea").show();
    $("#recharge").hide();
    }
  var minBuy=$("#minBuy").val();
  if(parseFloat(purAmount)==0){
    $("#purchase").val('');
    return;
  }
  if(parseFloat(purAmount)<parseFloat(minBuy)){
   $("#errorMsg").show();
   $("#errorMsg").html('<i class="icon icon-error2 font-12 "></i>购买金额必须大于起购金额');
   purInputFlag=false;
   buttonToGray();
 }
  //监听购买金额文本框
  $('#purchase').bind('input propertychange', function() {
    var shouldPurAmount=parseFloat($("#purAmount").val()).toFixed(2);
    var purAmount=parseFloat($("#purchase").val()).toFixed(2);
    var minBuy=$("#minBuy").val();
    //数据正则
    var reg = /^[0-9]+\.{0,1}[0-9]{0,2}$/;
    if(!reg.test(purAmount)){
      //按钮变灰
      purInputFlag=false;
      buttonToGray();
    }else if(parseFloat(purAmount)<parseFloat(minBuy)){
      $("#errorMsg").show();
      $("#errorMsg").html('<i class="icon icon-error2 font-12 "></i>购买金额必须大于起购金额');
      purInputFlag=false;
      buttonToGray();
    }else if(parseFloat(purAmount)<parseFloat(shouldPurAmount)){
      purInputFlag=true;
      $("#errorMsg").show();
      var ss=parseFloat(shouldPurAmount)-parseFloat(purAmount);
      $("#errorMsg").html('<i class="icon icon-error2 font-12 "></i>申购金额不足本月定投金额哦，您本月还需再申购'+parseFloat(ss).toFixed(2)+'元');
      if(pwdInputFlag&&checkBoxFlag){
        buttonToLight();
      }    
      }else{
      //按钮变亮
      purInputFlag=true;
      if(pwdInputFlag&&checkBoxFlag){
        buttonToLight();
      }
      $("#errorMsg").hide();
    }
    //账户余额不足问题
    var accountBalance=$("#accountBalance").val();
    if(parseFloat(accountBalance)<purAmount||parseFloat(accountBalance)==0){
      $("#button").hide();
      $("#pwdArea").hide();
      $("#recharge").show();
      //按钮变
    }else{
      $("#button").show();
//      inintPwd();
      $("#pwdArea").show();
      $("#recharge").hide();
    }
    
  });
  
  $(document).on('input propertychange','#pwd',function(){
    if($("#pwd").val().length>0){
      $("#cleanPwd").show();
    }else{
      $("#cleanPwd").hide();
    }
    var tradePwd=$("#pwd").val(); 
    if(tradePwd.length == 0){
      $("#errorInfo").hide();
      pwdInputFlag=false;
      buttonToGray();
    }
    else if (tradePwd == '' || (tradePwd.length < 6&&tradePwd.length > 0) || tradePwd.length > 20){
      $("#errorInfo").html(' 请输入6-20位的交易密码');
      $("#errorInfo").show();
      pwdInputFlag=false;
      buttonToGray();
    }else{
      $("#errorInfo").hide();
      pwdInputFlag=true;
      if(purInputFlag&&checkBoxFlag){
        buttonToLight();
      }
    }
    
  });
  
  
  
  //去充值按钮
  $("#recharge").click(function() {
    //账户余额
    var accountBalance=$("#accountBalance").val();
    //输入金额
    var buyAmount=$("#purchase").val();
    var rechargeAmount=parseFloat(buyAmount)-parseFloat(accountBalance);
    var policyId=$("#policyId").val();
    var sceneId=$("#sceneId").val();
    var purAmount=$("#purAmount").val();
    var policyOrderId=$("#policyOrderId").val();
            location.href = "asset/toRecharge?amount=" + rechargeAmount
            +"&ret=zt%2fgroupPurchase%2ftoPurchase%3fpolicyId%3d" + policyId + "%26sceneId%3d" + sceneId
                    + "%26purAmount%3d" + purAmount+"%26policyOrderId%3d"+policyOrderId;
  });
  
  $("#toFeeDetail").click(function() {
    //输入金额
    var buyAmount=$("#purchase").val();
    var policyOrderId=$("#policyOrderId").val();
    var policyId=$("#policyId").val();
        location.href = "zt/groupPurchase/toPurchaseFeeDetail?purAmount="
            + buyAmount + "&policyOrderId=" + policyOrderId + "&policyId="
            + policyId;
  });
  
  
  //清空密码按钮
  $("#cleanPwd").click(function() {
    $("#pwd").val("");
    $("#cleanPwd").hide();
    buttonToGray();
  });
  
  //明文密文按钮
  $("#seePwd").click(function() {
    //睁眼
    var classType=$("#seePwd").attr('class');
    if(classType!='icon icon-unlook fl'){
      document.getElementById('pwd').type="password";
      $("#seePwd").attr('class','icon icon-unlook fl');
    }else{
      document.getElementById('pwd').type="text";
      $("#seePwd").attr('class','icon icon-look orange fl');
    }  });
  
  //协议按钮
  $(".icon-checkbox").click(function() {
    //协议是否被选中
    if($("#agreeBox").is(":checked")) {
      checkBoxFlag=true;
      if(pwdInputFlag&&purInputFlag){
        buttonToLight();
      }
    } else {
      checkBoxFlag=false;
      buttonToGray();
    }
});
  //购买按钮
  $("#button").click(function() {
    var token=$("#token").val();
    var tradePwd=$("#pwd").val();
    buttonToGray();
    var policyOrderId=$("#policyOrderId").val();
    var sceneId=$("#sceneId").val();
    var policyId=$("#policyId").val();
    var fee=$("#fee").val();
    $("#button").val("处理中");
    $.ajax({
      url:'zt/groupPurchase/purchase',
      type:'post',
      dataType:'json',
      data:{fundList:purChaseList,tradePwd:tradePwd,policyOrderId:policyOrderId,sceneId:sceneId,policyId:policyId,token:token},
      error:function(){
        buttonToLight();
        $("#button").val("确认购买");
        $("#errorInfo").show();
        $("#errorInfo").html('<i class="icon icon-error2"> 系统或网络异常，请稍后再试</i>');
      },
      success:function(data){
        if(data.status == "SYSTEM_EXCEPTION"){
          $("#token").val(data.token);
          $("#errorInfo").show();
          $("#errorInfo").html('<i class="icon icon-error2"> 系统或网络异常，请稍后再试</i>');
          buttonToLight();
          $("#button").val("确认购买");
        }else if(data.status == "SUCCESS"){
          location.href="zt/groupPurchase/toPurchaseSuccess?orderDetailId="+data.orderDetailId+"&fee="+fee;
        }else if(data.status == "UNAVAILABLE_ACCOUNT"){
          $("#token").val(data.token);
          $("#errorInfo").show();
          $("#errorInfo").html('<i class="icon icon-error2"> 账户余额不足,请充值后再试 </i>');
          buttonToLight();
          $("#button").val("确认购买");
        }else if(data.status == "WRONG_PWD"){
          //交易密码错误弹层不改
          if(data.description.overPlusCount==0){
            $("#token").val(data.token);
            $("#errorInfo").show();
            $("#errorInfo").html('<i class="icon icon-error2"> 账号已冻结,解冻日期 +'+ data.description.lockedEndTime+'</i>');
            buttonToLight();
            $("#button").val("确认购买");
          }else{
            $("#token").val(data.token);
            $("#errorInfo").show();
            $("#errorInfo").html('<i class="icon icon-error2"> 交易密码输入错误，您还可再输入'+data.description.overPlusCount+'次</i>');
            buttonToLight();
            $("#button").val("确认购买");
          }
        }else{
          $("#token").val(data.token);
        }
      }
  });
});
  
});

//按钮变灰，不可点
function buttonToGray(){
  $("#button").attr('disabled',true);
  $("#button").addClass('btnBuy-gray');
}
//按钮变亮，可点
function buttonToLight(){
  $("#button").attr('disabled',false);
  $("#button").removeClass('btnBuy-gray');
}

function calculateProportion(){
  //有sceneId说明用户还没有购买过此策略，不需要重新计算
  var purAmount=$("#purchase").val();
  var reg = /^[0-9]+\.{0,1}[0-9]{0,2}$/;
  var sceneId=$("#sceneId").val();
  var minBuy=$("#minBuy").val();
  if((!reg.test(purAmount))||parseFloat(purAmount)<parseFloat(minBuy)){
    return;
  }
  if(sceneId>0){
    tabToString(1);
    return;
  }
  var policyOrderId=$("#policyOrderId").val();
//弹窗
  $('#mask').show();
  $('#alertLayer').show();
  if($('#alertLayer').css('display')=='block'){
    setTimeout(function(){
        $('#alertLayer').hide();
    },3000);
    setTimeout(function(){
        $('#mask').hide();
    },3000);
    $('body').css('position','initial');
}
  $.ajax({
    url:'zt/groupPurchase/calculateProportion',
    type:'post',
    dataType:'json',
    data:{policyOrderId:policyOrderId,purAmount:purAmount},
    error:function(){
    },
    success:function(data){
      purChaseList="";
      var tb = document.getElementById("proportionTable");  //根据id找到这个表格
      var rows = tb.rows;               //取得这个table下的所有行
      for(var i=0;i<rows.length;i++)//循环遍历所有的tr行
      {
        if(i>0){
          for(var j=0;j<rows[i].cells.length;j++)//取得第几行下面的td个数，再次循环遍历该行下面的td元素
          {
             var cell = rows[i].cells[j];//获取某行下面的某个td元素
               var list=data.proportion;
             if(j==0){
               cell.innerHTML=list[i-1].fundName; 
               purChaseList+=list[i-1].fundName+";";
             }else if(j==1){
               cell.innerHTML=parseFloat(list[i-1].additionalProportion*100).toFixed(2); 
               purChaseList+=list[i-1].additionalProportion+";";
             }else if(j==2){
               cell.innerHTML=parseFloat(list[i-1].additionalAmount).toFixed(2); 
               purChaseList+=list[i-1].additionalAmount+";";
               purChaseList+=list[i-1].fundCode+";";
               purChaseList+=list[i-1].fundType+"#";
             }
            }
        }
      }
    }
});
}


//初始化purChaseList
function tabToString(q) {
  purChaseList="";
  var tb = document.getElementById("proportionTable");  //根据id找到这个表格
  var rows = tb.rows;               //取得这个table下的所有行
  for(var i=0;i<rows.length;i++)//循环遍历所有的tr行
  {
    if(i>0){
      var fundName;
      var fundProportion;
      var  buyAmount=$("#purchase").val();
      for(var j=0;j<rows[i].cells.length;j++)//取得第几行下面的td个数，再次循环遍历该行下面的td元素
      {
         var cell = rows[i].cells[j];//获取某行下面的某个td元素
         if(j==0){
           fundName=cell.innerHTML;
           purChaseList+=cell.innerHTML+";";
         }else if(j==1){
           purChaseList+=cell.innerHTML/100+";";
           fundProportion=cell.innerHTML/100;
           cell.innerHTML=parseFloat(cell.innerHTML).toFixed(2);
         }else if(j==2){
           //0代表初始化
           if(q==0){
             purChaseList+=cell.innerHTML+";";
             cell.innerHTML=parseFloat(cell.innerHTML).toFixed(2);
           }else{
             var amount=buyAmount*fundProportion;
             purChaseList+=amount+";";
             cell.innerHTML=parseFloat(amount).toFixed(2);
           }
         }else{
           purChaseList+=cell.innerHTML+"#";
         }
        }
    }
  }
  }

//监听密码文本框
function  handle(){
//  $("#pwd").bind('input propertychange', function() {
    
//  });
}
  
