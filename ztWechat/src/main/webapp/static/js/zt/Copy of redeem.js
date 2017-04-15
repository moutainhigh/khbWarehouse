/*$(function () {
  var totalEnableMoney=$("#totalEnableMoney").val();
  var leastRedeem=$("#leastRedeem").val();
  //判断可赎回总市值是否小于最低赎回金额,则下一步置灰，不可点
  if(parseFloat(totalEnableMoney)<parseFloat(leastRedeem)){
  //错误提现信息
    $("#errorInfo").html('<i class="icon icon-error2"></i>可赎回总市值小于最低赎回金额');
    toGray();
  }
  
});

//输入金额失去焦点
function blurRedeemMoney(){
  
  var leastRedeem=$("#leastRedeem").val();
  var totalEnableMoney=$("#totalEnableMoney").val();
  //判断是否低于最低赎回金额
  var redeemMoney=$("#redeemMoney").val();
  //数据正则
  var reg = /^[0-9]+\.{0,1}[0-9]{0,2}$/;
  if(redeemMoney==null||redeemMoney==''||!reg.test(redeemMoney)){
    //错误提现信息
    $("#errorInfo").html('<i class="icon icon-error2"></i>请输入正确金额');
    toGray();
    return;
  }
  if(parseFloat(redeemMoney)>parseFloat(totalEnableMoney)){
    //错误提现信息
    $("#errorInfo").html('<i class="icon icon-error2"></i>赎回金额大于可赎回总市值');
    toGray();
    return;
  }
  if(parseFloat(redeemMoney)<parseFloat(leastRedeem)){
    //错误提现信息
    $("#errorInfo").html('<i class="icon icon-error2"></i>赎回金额小于最低赎回金额');
    toGray();
  }else{
    toLight();
    //动态改变每只基金的赎回份额
    var trs = $('.redemptionTable.redemptionTable-page').find('tr');
    for(var i=1;i<trs.length;i++){
      //每个基金总份额
      var totalRedeem =$('.redemptionTable.redemptionTable-page').find('tr').eq(i).find('td').eq(1).html();
      //赎回份额=每只基金的持有份额*赎回金额/总市值
      var redeem=parseFloat(totalRedeem)*parseFloat(redeemMoney)/parseFloat(totalEnableMoney);//每个基金赎回份额
      $('.redemptionTable.redemptionTable-page').find('tr').eq(i).find('td').eq(2).html(redeem.toFixed(2));
    }
  }
 
}




//按钮变灰，不可点
function toGray(){
  $("#button").attr('disabled',true);
  $("#button").addClass('btnBuy-gray');
  $("#errorInfo").show();
}
//按钮变亮，可点
function toLight(){
  $("#button").attr('disabled',false);
  $("#button").removeClass('btnBuy-gray');
  $("#errorInfo").hide();
}
//全部赎回方法点击方法
function allRedeem(){
  var totalEnableMoney=$("#totalEnableMoney").val();
  $("#redeemMoney").val(totalEnableMoney);
  blurRedeemMoney();
}
//跳转下一页
function toNext(){
  var policyOrderId =$("#policyOrderId").val();
  var redeemMoney=$("#redeemMoney").val();
  window.location.href="zt/redeem/toConfirmRedeem?policyOrderId="+policyOrderId+"&redeemMoney="+redeemMoney;
}
*/