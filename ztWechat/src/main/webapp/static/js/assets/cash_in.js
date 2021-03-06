$(function () {
  $("#explain").click(function(){
    $("#mask").css('display','block');
    $("#alertLayer").css('display','block');
  });
$("#btn-login").click(function(){
    $('#btn-login').attr("disabled", true);
    $('#btn-login').val("处理中...");
    var amount = $("#amount").val();
    var tradePwd = $("#pwd").val();
    var token = $("#token").val();
    var isError = false;
      if (amount == '' || amount == 0) {
        $("#amountLi").addClass("input-wrap-error");
        $("#amountMessage").addClass("icon icon-error2").html("请输入正确的提现金额"); 
        isError = true;
      } else if (tradePwd == '' || tradePwd.length < 6||tradePwd.length > 20) {
        $("#pwdLi").addClass("input-wrap-error");
        $("#pwdMessage").html("<a href='account/toResetTradePwd'"+
              "class='forgetCode orange fr'>忘记密码</a> <i class='icon icon-error2'></i>请输入6-20位的交易密码"); 
        isError = true;
      }
      if(isError){
        $('#btn-login').attr("disabled", false);
        $('#btn-login').val("免费提现");
        return false;
      }
      //无错误清除错误提示
      $("#amountLi").removeClass("input-wrap-error");
      $("#amountMessage").removeClass("icon icon-error2").html("");
      $("#pwdLi").removeClass("input-wrap-error");
      $("#pwdMessage").html("<a href='account/toResetTradePwd'"+
      "class='forgetCode orange fr'>忘记密码</a>");
      $.ajax({
        url:'asset/cashIn', 
        type:'post',
        dataType:'json',
        data:{amount:amount, tradePwd:tradePwd, token:token},
        error: function(){
          $("#mask").show();
          //TODO 系统异常
//          $("#alertLayer-8").show();
          $("#failTitle").remove();//去掉“绑卡失败”字样
          $("#errorMsgMask").remove();//去掉错误信息
          $("#btnLayer").addClass("pb50");
          $("#layerBody").append("<p id='failTitle' class='failTitle'>提现失败</p>");
          $("#layerBody").append("<p id='errorMsgMask' class='orange'>系统异常，请稍后重试哦</p>");
          $("#btnLayer").append("<div class='btnMaskArea tc pa'><a href='javascript:void(0)' class='btnBig' onclick='cashInAgain()'>OK</a></div>");
          $("#failMask").show();
        }, 
        success: function(data){
          if(data.result == 'SUCCESS'){
            $("#mask").css('display','block');
            $("#alertLayer-3").css('display','block');
          } else if(data == 'noLogin'){
            location.href=window.location.href;  
          } else if(data.result == 'SYSTEM_EXCEPTION'){
            $("#token").val(data.token);
            $("#mask").css('display','block');
            $("#failTitle").remove();//去掉“绑卡失败”字样
            $("#errorMsgMask").remove();//去掉错误信息
            $("#btnLayer").addClass("pb50");
            $("#layerBody").append("<p id='failTitle' class='failTitle'>提现失败</p>");
            $("#layerBody").append("<p id='errorMsgMask' class='orange'>系统异常，请稍后重试哦</p>");
            $("#btnLayer").append("<div class='btnMaskArea tc pa'><a href='javascript:void(0)' class='btnBig' onclick='cashInAgain()'>OK</a></div>");
            $("#failMask").show();
          } else if(data.result == 'failed'){
            $("#token").val(data.token);
            $("#mask").show();
            $("#failTitle").remove();//去掉“绑卡失败”字样
            $("#errorMsgMask").remove();//去掉错误信息
            $("#btnLayer").addClass("pb50");
            $("#layerBody").append("<p id='failTitle' class='failTitle'>提现失败</p>");
            $("#layerBody").append("<p id='errorMsgMask' class='orange'>"+data.description+"</p>");
            $("#btnLayer").append("<div class='btnMaskArea tc pa'><a href='javascript:void(0)' class='btnBig' onclick='cashInAgain()'>重新提现</a></div>");
            $("#failMask").show();
            
          } else{//交易密码错误
            $("#token").val(data.token);
            $("#mask").show();
            $("#failTitle").remove();//去掉“绑卡失败”字样
            $("#errorMsgMask").remove();//去掉错误信息
            $("#btnLayer").addClass("pb50");
            $("#layerBody").append("<p id='failTitle' class='failTitle'>提现失败</p>");
            $("#btnLayer").append("<div class='btnMaskArea tc pa'><a href='javascript:void(0)' class='btnBig' onclick='cashInAgain()'>重新提现</a></div>");
            if(data.description.overPlusCount==0){
              /*$(".errorCon.red.pa").html("账号已冻结,解冻日期 "+ data.description.lockedEndTime);
              $("#mask").css('display','block');
              $(".unloginMask.regMask.withdrawMask").css('display','block');*/
              $("#layerBody").append("<p id='errorMsgMask' class='orange'>账号已冻结,解冻日期 "+ data.description.lockedEndTime+"</p>");
            }else{
              /*$(".errorCon.red.pa").html("交易密码错误,还剩余"+data.description.overPlusCount+"次");
              $("#mask").css('display','block');
              $(".unloginMask.regMask.withdrawMask").css('display','block');*/
              $("#layerBody").append("<p id='errorMsgMask' class='orange'>交易密码错误,还剩余 "+ data.description.overPlusCount+"次</p>");
            }
            $("#failMask").show();
          
          }
        } 
      });
  });
//恢复清除图标
$('.input-text').keyup(function(){
  $(this).parent().siblings('.icon.icon-error.fr').show();
});
});
/**
 * 验证输入值
 * @returns {Boolean}
 */
function verifyAmount(){
  var amount=$("#amount").val();
  var availableCashIn=$("#availableCashIn").val();
  var filter=/^[0-9]+\.{0,1}[0-9]{0,2}$/;
  var flag=filter.test(amount);
  if(false==flag){ //输入金额是否满足数字并小数点不超过后俩位
    $("#amountLi").addClass("input-wrap-error");
    $("#amountMessage").addClass("icon icon-error2").html("提现金额必须为数字，小数点不能超过2位");
    $("#btn-login").attr("disabled",true);
    $("#btn-login").attr("class","btn-login-gray");
    return false;
  }
  if(parseFloat(amount) > parseFloat(availableCashIn)){
    $("#amountLi").addClass("input-wrap-error");
    $("#amountMessage").addClass("icon icon-error2").html("提现金额超过最多可提现金额");
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
  $("#pwdMessage").html("<a href='account/toResetTradePwd'"+
  "class='forgetCode orange fr'>忘记密码</a>");
  $("#pwdLi").removeClass("input-wrap-error");
   //清除内容
  $("#pwd").val('');
  $("#pwd").focus();
  $("#errorPwd").hide();
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
 * 重新提现
 */
function cashInAgain(){
  $("#pwd").val('');
  $('#btn-login').attr("disabled", false);
  $('#btn-login').val("免费提现");
  $("#mask").css('display','none');
  $("#alertLayer-3").css('display','none');
 /* $("#alertLayer-8").css('display','none');
  $("#alertLayer-4").css('display','none');*/
  $("#failMask").css('display','none');
}