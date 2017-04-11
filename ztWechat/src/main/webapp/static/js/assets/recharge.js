 
$(function () {
  var ret = $("#ret").val();
 var money=$("#amount").val();
 if(money!=''&&money!=0){
   $("#amount").val( format_money(money));
 }
  //动态显示title和其他提示
  if(ret=="fixed"||ret=="scb" || ret == "seckill"){
    $("#BUY_AMOUNT").val(formatNumberFloor($("#BUY_AMOUNT").val(),2));
    $("#errorAmount").hide();
    $("#title").html("<h2 class=\"titleWithdraw orange br-bottom\">充值到懒猫账户（充值后余额将直接购买）</h2>");
    if(ret=="fixed" || ret == "seckill"){
      $("#fixedInfo").html("购买"+$("#PRO_NAME").val()+" "+$("#BUY_AMOUNT").val()+" 元");
      //格式化购买金额
    }else{
      $("#fixedInfo").html("购买生财宝  "+$("#BUY_AMOUNT").val()+" 元");
    }
    $("#amount").attr('readOnly',true);
  }
  /**
   * 判断银行卡是否支持无卡
   */
  
  isNoCard();
$("#btn-login").click(function(){
      $('#btn-login').attr("disabled", true);
      $('#btn-login').val("处理中...");
      var amount = $("#amount").val();
      var tradePwd = $("#tradePwd").val();
      var uuid = $("#uuid").val();
      var token = $("#token").val();
//      alert(token);
      var isError = false;
        if (amount == '' || amount == 0) {
          $("#amountLi").addClass("input-wrap-error");
          $("#amountMessage").addClass("icon icon-error2").html("请输入正确的充值金额");
          isError = true;
        } else if (tradePwd == '' || tradePwd.length < 6 || tradePwd.length > 20) {
          $("#pwdLi").addClass("input-wrap-error");
          $("#pwdMessage").addClass("icon icon-error2").html("请输入6-20位的交易密码");
          isError = true;
        }
        if(isError){
          $('#btn-login').attr("disabled", false);
          $('#btn-login').val("确认充值");
          return false;
        }
        //无错误清除错误提示
        $("#amountLi").removeClass("input-wrap-error");
        $("#amountMessage").removeClass("icon icon-error2").html("");
        $("#pwdLi").removeClass("input-wrap-error");
        $("#pwdMessage").removeClass("icon icon-error2").html("");

      $.ajax({
          url:'asset/recharge', 
          type:'post',
          dataType:'json',
          data:{amount:amount, tradePwd:tradePwd, uuid:uuid ,token:token},
          error: function(){
            $("#failTitle").remove();//去掉“绑卡失败”字样
            $("#errorMsgMask").remove();//去掉错误信息
            $("#btnLayer").addClass("pb50");
            $("#layerBody").append("<p id='failTitle' class='failTitle'>充值失败</p>");
            $("#layerBody").append("<p id='errorMsgMask' class='orange'>系统异常，请稍后重试哦</p>");
            $("#btnLayer").append("<div class='btnMaskArea tc pa'><a href='javascript:void(0)' class='btnBig' onclick='cashInAgain()'>OK</a></div>");
            $("#mask").show();
            $("#failMask").show();
          }, 
          success: function(data){
             if(data == 'noLogin'){
                        location.href=window.location.href;  
                      }
             else if(data.result == 'processing'){
               $("#mask").css('display','block');
               $(".alertLayer-10").show();
               setTimeout(function() {
                 $.ajax({
                   url:"asset/selectRechargeResult",
                   data:{paymentFlowNo:data.paymentFlowNo},
                   dataType:"json",
                   type:"post",
                   success:function(data){
                     $(".alertLayer-10").hide();
                     if(data.result == 'failed'){
                       $("#token").val(data.token);
                       $("#mask").show();
                       $("#failTitle").remove();//去掉“绑卡失败”字样
                       $("#errorMsgMask").remove();//去掉错误信息
                       $("#btnLayer").addClass("pb50");
                       $("#layerBody").append("<p id='failTitle' class='failTitle'>充值失败</p>");
                       if(ret == "scb"||ret=="fixed" || ret == "seckill" ||ret.indexOf('zt')>-1){
                         $("#layerBody").append("<p id='errorMsgMask' class='orange'>"+data.description+"</p>");
                         $("#btnLayer").append("<div class='btnMaskArea tc pa'><a href='javascript:void(0)' class='btnBig' onclick='buyAgain()'>重新购买</a></div>");
                         
                       }else{
                         $("#layerBody").append("<p id='errorMsgMask' class='orange'>"+data.description+"</p>");
                         //添加按钮
                         $("#btnLayer").append("<div class='btnMaskArea tc pa'><a href='javascript:void(0)' class='btnBig' onclick='cashInAgain()'>重新充值</a></div>");
                       }
                       $("#failMask").show();
                     } else if (data.result=='system_exception'){
                       $("#token").val(data.token);
                       $("#mask").show();
                       $("#failTitle").remove();//去掉“绑卡失败”字样
                       $("#errorMsgMask").remove();//去掉错误信息
                       $("#btnLayer").addClass("pb50");
                       $("#layerBody").append("<p id='failTitle' class='failTitle'>充值失败</p>");
                       $("#layerBody").append("<p id='errorMsgMask' class='orange'>系统异常，请稍后重试哦</p>");
                       $("#btnLayer").append("<div class='btnMaskArea tc pa'><a href='javascript:void(0)' class='btnBig' onclick='cashInAgain()'>OK</a></div>");
//                       $("#mask").show();
                       $("#failMask").show();

                     } else if(data.result=='processing') {
                       $(".alertLayer-11").show();
                     } else if(data.result=='success'){
                       if(ret == "scb"||ret=="fixed" || ret=="seckill" || ret == "seckillPer" ||ret.indexOf('zt')>-1){
                         var u = navigator.userAgent;
                         var isApp = /lanmao/i.test(u);
                         if(isApp){
                           if(ret=="scb"){
                             location.href="scb/toConfirmTransferIn?totalBalance="+$("#BUY_AMOUNT").val();
                             return;
                           }else if(ret == "seckillPer"){
                             location.href="seckillActivity/toSeckillPro";
                           }else if(ret.indexOf('zt')>-1){
                               location.href=ret;
                           }else{
                             location.href="fixed/toBuyProductInfo?productId="+$("#PRO_NO").val()+"&totalPays="+$("#BUY_AMOUNT").val()+"&expectIncomeNoVoucher="+$("#EXPECT_INCOME").val()+"&promoNo="+$("#PROMO_NO").val()+"&promoType="+$("#promoType").val()+"&addRate="+$("#addRate").val()+"&expectType="+$("#expectType").val()+"&orderNum="+$("#orderNum").val();
                             return;
                           }
                         }
                         $("#buyType").css('display','block');
                       }else{
                         $("#common").css('display','block');
                       }
                       $("#alertLayer-3").css('display','block');
                       progress();//进度条
                     }
                   }
                 });
                 
               } , 5000);
                
            } else if(data.result == 'failed'){
              $("#token").val(data.token);
              $("#failTitle").remove();//去掉“绑卡失败”字样
              $("#errorMsgMask").remove();//去掉错误信息
              $("#btnLayer").addClass("pb50");
              $("#layerBody").append("<p id='failTitle' class='failTitle'>充值失败</p>");
              $("#layerBody").append("<p id='errorMsgMask' class='orange'>"+data.description+"</p>");
              if(ret == "scb"||ret=="fixed" || ret == "seckill"){
                $("#btnLayer").append("<div class='btnMaskArea tc pa'><a href='javascript:void(0)' class='btnBig' onclick='buyAgain()'>重新购买</a></div>");
              }else{
                //添加按钮
                $("#btnLayer").append("<div class='btnMaskArea tc pa'><a href='javascript:void(0)' class='btnBig' onclick='cashInAgain()'>重新充值</a></div>");
              }
              $("#failMask").show();
//              $("#alertLayer-4").css('display','block');
            } else if(data.result=='processing') {
              $(".alertLayer-11").show();
            } else if (data.result=='system_exception'){
              $("#token").val(data.token);
              $("#failTitle").remove();//去掉“绑卡失败”字样
              $("#errorMsgMask").remove();//去掉错误信息
              $("#btnLayer").addClass("pb50");
              $("#layerBody").append("<p id='failTitle' class='failTitle'>充值失败</p>");
              $("#layerBody").append("<p id='errorMsgMask' class='orange'>系统异常，请稍后重试哦</p>");
              $("#btnLayer").append("<div class='btnMaskArea tc pa'><a href='javascript:void(0)' class='btnBig' onclick='cashInAgain()'>OK</a></div>");
              $("#failMask").show();

            } else if (data.result=='payment_limit'){
              $("#token").val(data.token);
              $("#failTitle").remove();//去掉“绑卡失败”字样
              $("#errorMsgMask").remove();//去掉错误信息
              $("#btnLayer").addClass("pb50");
              $("#layerBody").append("<p id='failTitle' class='failTitle'>充值失败</p>");
              $("#layerBody").append("<p id='errorMsgMask' class='orange'>系统异常，请稍后重试哦</p>");
              $("#btnLayer").append("<div class='btnMaskArea tc pa'><a href='javascript:void(0)' onclick='buyAgain()'>重新购买</a><a href='#'>享大额充值特权</a></div>");
//              $("#mask").show();
              $("#failMask").show();
            } else {//交易密码错误
              $("#token").val(data.token);
              $("#failTitle").remove();//去掉“绑卡失败”字样
              $("#errorMsgMask").remove();//去掉错误信息
              $("#btnLayer").addClass("pb50");
              $("#layerBody").append("<p id='failTitle' class='failTitle'>充值失败</p>");
              if(data.description.overPlusCount==0){
                // 显示“重新充值”按钮
                $("#layerBody").append("<p id='errorMsgMask' class='orange'>账号已冻结,解冻日期 "+ data.description.lockedEndTime+"</p>");
                $("#btnLayer").append("<div class='btnMaskArea tc pa'><a href='javascript:void(0)' class='btnBig' onclick='cashInAgain()'>重新充值</a></div>");
              }else{
              // 显示“重新充值”按钮
                $("#layerBody").append("<p id='errorMsgMask' class='orange'>交易密码错误,还剩余 "+ data.description.overPlusCount+"次</p>");
                $("#btnLayer").append("<div class='btnMaskArea tc pa'><a href='javascript:void(0)' class='btnBig' onclick='cashInAgain()'>重新充值</a></div>");
              }
              $("#failMask").show();
            }
        $("#mask").css('display','block');
          } 
        });
    });
//恢复清除图标
$('.input-text').keyup(function(){
  $(this).parent().siblings('.icon.icon-error.fr').show();
});
}

);
/**
 * 判断银行卡是否支持无卡
 */
function isNoCard(){
 var status = $("#status").val();
 if(status=='OFF'){//不支持无卡
   $("#btn-login").attr("disabled",true);
   $("#btn-login").attr("class","btn-login-gray");
   return false;
 }
 return true;
}
  
//充值金额失去焦点的
function verifyAmount() {
  var amount = $("#amount").val();
  var restrict=$("#restrict").val()*10000; //转换单位万到元
  var filter=/^[0-9]+\.{0,1}[0-9]{0,2}$/;
  var flag=filter.test(amount);
  if(!isNoCard()){
    return false;
  }
  if(false==flag){ //输入金额是否满足数字并小数点不超过后俩位
    $("#amountLi").addClass("input-wrap-error");
    $("#amountMessage").addClass("icon icon-error2").html("充值金额必须为数字，小数点不能超过2位"); 
    $("#btn-login").attr("disabled",true);
    $("#btn-login").attr("class","btn-login-gray");
    return false;
  }
 if(parseFloat(amount) > parseFloat(restrict)){
   $("#amountLi").addClass("input-wrap-error");
    $("#amountMessage").addClass("icon icon-error2").html("充值金额不能大于单笔限额");
    $("#btn-login").attr("disabled",true);
    $("#btn-login").attr("class","btn-login-gray");
    return false;
  }
 $("#amountLi").removeClass("input-wrap-error");
    $("#amountMessage").removeClass("icon icon-error2").html("");
    $("#btn-login").attr("disabled",false);
    $("#btn-login").attr("class","btn-login");
}


/**
*显示 隐藏密码
*/
function changePasswordType(id){
  var classType=$("#look").attr('class');
  if(classType!='icon icon-unlook fr'){
    document.getElementById(id).type="password";
    $("#look").attr('class','icon icon-unlook fr');
  }else{
    document.getElementById(id).type="text";
    $("#look").attr('class','icon icon-look orange fr');
  }
}
/**
 * 错误信息清空
*/
function amountClick(){
  //清除错误提示
  $("#amountMessage").html('');
  $("#amountMessage").removeClass("icon icon-error2");
  $("#amountLi").removeClass("input-wrap-error");
   //清除内容
  $("#amount").val('');
  $("#amount").focus();
  $("#errorAmount").hide();
}
function pwdClick(){
  //清除错误提示
  $("#pwdMessage").html('');
  $("#pwdMessage").removeClass("icon icon-error2");
  $("#pwdLi").removeClass("input-wrap-error");
   //清除内容
  $("#tradePwd").val('');
  $("#tradePwd").focus();
  $("#errorPwd").hide();
}
/**
 * 清除蒙层
 */
function cashInAgain(){
  $("#tradePwd").val('');
  $('#btn-login').attr("disabled", false);
  $('#btn-login').val("确认充值");
  $("#mask").css('display','none');
  /*$("#rechargeLimit").css('display','none');
  $("#buyTypeFailed").css('display','none');
  $("#commonFailed").css('display','none');*/
  $("#buyType").css('display','none');
  $("#common").css('display','none');
  /*$("#alertLayer-3").css('display','none');
  $("#alertLayer-4").css('display','none');
  $("#alertLayer-8").css('display','none');*/
  $("#failMask").css('display','none');
//  location.reload();不刷新页面
//  buyAgain();
  $.ajax({
    url:'asset/toGetUUID', 
    type:'post',
    dataType:'json',
    data:{},
    error: function(){
      
    }, 
    success: function(data){
      //更新页面的uuid
//      $("#uuid").attr("value",data.uuid);
      $("#uuid").val(data.uuid);
    }
  });
}
/**
 * 重新购买
 */
function buyAgain(){
  var ret = $("#ret").val();
  if(ret == "scb"){
    location.href="scb/toScb";
  }else if(ret=="fixed"){
    location.href="fixed/toPurchaseProduct?productNo="+$("#PRO_NO").val();
  }else if(ret.indexOf("zt")>-1){
    location.href=ret;
  }else if(ret == "seckill"){
    location.href = "seckillActivity/toSeckillPro";
  }
}
function progress(){
  //进度条
  var ret = $("#ret").val();
  var i =0;
  var ms = 2000; //变量MS: 从0%到100%需要的毫秒数
  var time = setInterval(function(){
    $("#progressBar-2 .finish-2").css("width",i+"%");i=i+(1000/ms);
    if(i>100){
      clearInterval(time);
      if(ret == "scb"){
        location.href="scb/toConfirmTransferIn?totalBalance="+$("#BUY_AMOUNT").val();
      }else if(ret=="fixed" || ret =="seckill"){
        location.href="fixed/toBuyProductInfo?productId="+$("#PRO_NO").val()+"&totalPays="+$("#BUY_AMOUNT").val()+"&expectIncomeNoVoucher="+$("#EXPECT_INCOME").val()+"&promoNo="+$("#PROMO_NO").val()+"&promoType="+$("#promoType").val()+"&addRate="+$("#addRate").val()+"&expectType="+$("#expectType").val()+"&orderNum="+$("#orderNum").val();
      }else if(ret.indexOf("zt")>-1){
        location.href=ret;
      }else if(ret == "seckillPer"){
        location.href="seckillActivity/toSeckillPro";
      }
    }},10);
}
