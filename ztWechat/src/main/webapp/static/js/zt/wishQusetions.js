$(document).ready(function() {
  
  if($("#flag").val() == "0") {
    yesrs ()
    money()
  }
  
  $("#years,#money").focus(function() {
    $(".input-wrap-error").removeClass("input-wrap-error");
    $(this).parents(".input-wrap").addClass("input-wrap-error");
  });
  
  var errorMsg,moneyFlag,yearsFlag;
  $("#years").keyup(function() {
    yesrs ()
  });
  $("#years").blur(function() {
    yesrs ()
  });
  
  function yesrs () {
    var yearsFil = /^[0-9]*[1-9][0-9]*$/;
    var yearsFilter=yearsFil.test($("#years").val());
    if(yearsFilter == false) {
      errorMsg=" 年限应为整数"; 
      var yearsString =  $("#years").val();
      $("#years").val(yearsString.substring(0,yearsString.length-1));
      $("#yearsError").html('<i class="icon icon-error2"></i>'+errorMsg);
      $("#submitBtn").addClass("submitBtn-gray");
      yearsFlag = false;
    } else if($("#years").val() > 200){
      errorMsg=" 预计年数不能大于200年"; 
      $("#yearsError").html('<i class="icon icon-error2"></i>'+errorMsg);
      $("#submitBtn").addClass("submitBtn-gray");
      yearsFlag = false;
    }else if($("#years").val() < 1){
      errorMsg=" 预计年数不能小于1年"; 
      $("#yearsError").html('<i class="icon icon-error2"></i>'+errorMsg);
      $("#submitBtn").addClass("submitBtn-gray");
      yearsFlag = false;
    } else {
      $("#yearsError").html('');
      
      yearsFlag = true;
    }
    if(yearsFlag && moneyFlag) {
      $("#flag").val("0");
      $("#submitBtn").removeClass("submitBtn-gray");
    }
  }
  
  
  
  $("#money").keyup(function() {
    money()
  });
  $("#money").blur(function() {
    money()
  });
  
  function money () {
    var moneyString =  $("#money").val();
    if(moneyString.indexOf(".")>0 && moneyString.length > moneyString.indexOf(".")+3) {
      $("#money").val(moneyString.substring(0,moneyString.length-1));
    } 
    
    var filterMoney=/^[0-9]+\.{0,1}[0-9]{0,2}$/;
    var flagFilter=filterMoney.test($("#money").val());
    if(false==flagFilter){ //输入金额是否满足数字并小数点不超过后俩位
      errorMsg=" 购买金额必须为数字，小数点不能超过2位"; 
      $("#moneyError").html('<i class="icon icon-error2"></i>'+errorMsg);
      $("#submitBtn").addClass("submitBtn-gray");
      moneyFlag = false;
    }else if ($("#money").val() == null || $("#money").val() == '') {
      errorMsg = " 请输入购买金额";
      $("#moneyError").html('<i class="icon icon-error2"></i>'+errorMsg);
      $("#submitBtn").addClass("submitBtn-gray");
      moneyFlag = false;
    }else if($("#money").val() < 500){
      errorMsg = " 心愿单金额需大于500元";
      $("#moneyError").html('<i class="icon icon-error2"></i>'+errorMsg);
      $("#submitBtn").addClass("submitBtn-gray");
      moneyFlag = false;
    } else {
      $("#moneyError").html('');
      moneyFlag = true;
    }
    if(yearsFlag && moneyFlag) {
      $("#flag").val("0");
      $("#submitBtn").removeClass("submitBtn-gray");
    }
  }
  
 $("#submitBtn").click(function() {
   if(yearsFlag && moneyFlag) {
     $("#form").submit();
   }
  });
});