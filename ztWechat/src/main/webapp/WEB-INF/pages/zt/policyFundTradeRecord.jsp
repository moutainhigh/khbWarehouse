<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="e" uri="/emvc-tags" %>
<html>
    <head lang="en">
        <title>交易明细详情</title>
        <style type="text/css">
            #body{background: #fff;}
        </style>
    </head>
    <body>
    <div class="redemption-Area" id="body">
        <div class="tradingTitle">
            <table width="100%">
                <tr>
                    <!-- 未确认状态下，显示支付金额 -->
                    <e:if test="@ztPolicyOrderDetailDTO.orderStatus.toString() == 'NO_CONFIRMED'">
                        <td class="td-1 num" width="67%"><e:property value="@_formater.formatNumber(ztPolicyOrderDetailDTO.orderAmount)"/></td>
                    </e:if>
                    <!-- 已确认或部分确认状态，都显示确认金额 -->
                    <e:else>
                        <td class="td-1 num" width="67%"><e:property value="@_formater.formatNumber(ztPolicyOrderDetailDTO.confirmBalance)"/></td>
                    </e:else>
                    <!-- 手续费 -->
                    <%-- <e:if test="@ztPolicyOrderDetailDTO.fee == 0">
                        <td class="td-2 num-1 tc">--</td>
                    </e:if> --%>
                    <td class="td-2 num-1 tc"><e:property value="@_formater.formatNumber(fee)"/></td>
                </tr>
                <tr>
                    <!-- 未确认状态 -->
                    <e:if test="@ztPolicyOrderDetailDTO.orderStatus.toString() == 'NO_CONFIRMED'">
                        <e:if test="@ztPolicyOrderDetailDTO.orderType.toString() == 'REDEEM'">
                            <td class="td-1 text" width="67%">赎回中金额 (元)</td>
                        </e:if>
                        <e:else>
                            <td class="td-1 text" width="67%">实际支付金额 (元)</td>
                        </e:else>
                    </e:if>
                    <e:else>
                        <e:if test="@ztPolicyOrderDetailDTO.orderType.toString() == 'REDEEM'">
                            <td class="td-1 text" width="67%">赎回确认金额 (元)</td>
                        </e:if>
                        <e:else>
                            <td class="td-1 text" width="67%">申购确认金额 (元)</td>
                        </e:else>
                    </e:else>
                    <td class="td-2 text tc">手续费 (元)</td>
                </tr>
            </table>
        </div>
        <div class="tradingMain">
            <div class="trade-info">
                <table width="100%">
                    <tr>
                        <td class="tc" width="38%">策略名称</td>
                        <td class="t-2"><e:property value="@ztPolicyOrderDetailDTO.ztPolicyDTO.policyName"/></td>
                    </tr>
                    <tr>
                        <td class="tc" width="38%">交易类型</td>
                        <td class="t-2">
                            <e:iterator list="@_textResource.getTextMap('lmzt_order_relevant_enum')" var="item">
                                <e:if test="@item.key == ztPolicyOrderDetailDTO.orderType.toString()">
                                    <e:property value="@item.value"/>
                                </e:if>
                            </e:iterator>
                        </td>
                    </tr>
                    <tr>
                        <td class="tc" width="38%">交易时间</td>
                        <td class="t-2"><e:property value="@_formater.formatDate(ztPolicyOrderDetailDTO.orderTime)"/></td>
                    </tr>
                    <tr>
                        <e:if test="@ztPolicyOrderDetailDTO.orderType.toString() == 'REDEEM'">
                            <td class="tc" width="38%">赎回方式</td>
                        </e:if>
                        <e:else>
                            <td class="tc" width="38%">支付方式</td>
                        </e:else>
                        <td class="t-2">
                            <e:iterator list="@_textResource.getTextMap('lmzt_order_relevant_enum')" var="item">
                                <e:if test="@item.key == ztPolicyOrderDetailDTO.payMode.toString()">
                                    <e:property value="@item.value"/>
                                </e:if>
                            </e:iterator>
                        </td>
                    </tr>
                    <tr>
                        <td class="tc" width="38%">确认日期</td>
                        <e:if test="@confirmDate == null">
                            <td class="t-2">--</td>
                        </e:if>
                        <e:else>
                            <td class="t-2"><e:property value="@confirmDate"/> 开始计息</td>
                        </e:else>
                    </tr>
                </table>
            </div>
            <div class="redemptionMain">
                <h3>本次交易明细</h3>
                <table width="100%" class="redemptionTable">
                    <tr>
                        <th class="tc" width="40%">基金名称</th>
                        <e:if test="@ztPolicyOrderDetailDTO.orderType.toString() == 'PURCHASE' || ztPolicyOrderDetailDTO.orderType.toString() == 'SINGLE_PURCHASE'">
                            <th class="tc" width="30%">申购金额（元）</th>
                        </e:if>
                        <e:elseif test="@ztPolicyOrderDetailDTO.orderType.toString() == 'REDEEM'">
                            <th class="tc" width="30%">赎回份额（份）</th>
                        </e:elseif>
                        <th class="tc">已确认份额（份）</th>
                    </tr>
                    <e:iterator list="@list" var="item">
                        <tr>
                            <td class="tl t-1"><e:property value="@item.fundName"/></td>
                            <!-- 申购 ,显示金额-->
                            <e:if test="@ztPolicyOrderDetailDTO.orderType.toString() == 'PURCHASE' || ztPolicyOrderDetailDTO.orderType.toString() == 'SINGLE_PURCHASE'">
                                <!-- 份额不是0，表示确认成功，显示确认份额 ，确认金额-->
                                <e:if test="@item.tradeConfirmShare != 0">
                                    <td class="tc"><e:property value="@_formater.formatNumber(item.tradeConfirmBalance)"/>
                                    <td class="tc"><e:property value="@_formater.formatNumber(item.tradeConfirmShare)"/></td>
                                </e:if>
                                <!-- 没有确认份额，显示基金交易金额 -->
                                <e:else>
                                    <td class="tc"><e:property value="@_formater.formatNumber(item.balance)"/>
                                    <!--订单交易金额为0，说明此次投资组合，该支基金未发起申购  -->
                                    <e:if test = "@item.balance == '0'">
                                        <td class="tc">0</td>
                                    </e:if>
                                    <!-- 交易金额不是0,确认份额是0 可能确认中可能确认失败-->
                                    <e:else>
                                        <!-- 判断确认状态，如果是未确认，显示确认中 -->
                                        <e:if test="@item.isConfirmation.toString() == 'FALSE'">
                                            <td class="tc">确认中</td>
                                        </e:if>
                                        <!-- 如果状态是已确认，在判断成功标识字段，如果是“确认失败已重新申购”，显示“确认失败已重新申购” -->
                                        <e:elseif test="@item.status.toString() == 'SINGLE_PURCHASE_COMMIT'">
                                            <td class="tc">申购失败，已重新申购</td>
                                        </e:elseif>
                                        <e:else>
                                            <td class="tc">
                                                <p><e:property value="@item.status.displayName"/></p>
                                                <e:if test="@item.operationType.toString() == 'PURCHASE' || item.operationType.toString() == 'SINGLE_PURCHASE'">
                                                    <a href="zt/groupPurchase/toFundPurchase?amount=<e:property value="@item.balance"/>&fundOrderDetailId=<e:property value="@item.id"/>&fundCode=<e:property value="@item.fundCode"/>">重新申购</a>
                                                </e:if>
                                                <%-- <e:elseif test="@item.operationType.toString() == 'REDEEM' || @item.operationType.toString() == 'MERGED_REDEEM'">
                                                    <a href="zt/redeem/toRedeem">重新赎回</a>
                                                </e:elseif> --%>
                                            </td>
                                        </e:else>
                                    </e:else>
                                </e:else>
                            </e:if>
                            <!-- 赎回 -->
                            <e:elseif test="@ztPolicyOrderDetailDTO.orderType.toString() == 'REDEEM'">
                                <!-- 显示赎回份额 -->
                                <td class="tc"><e:property value="@_formater.formatNumber(item.shares)"/></td>
                                <!-- 份额不是0，表示确认成功，显示确认份额 -->
                                <e:if test="@item.tradeConfirmShare != 0">
                                    <td class="tc"><e:property value="@_formater.formatNumber(item.tradeConfirmShare)"/></td>
                                </e:if>
                                <!-- 确认份额为0，如果赎回份额为0，则显示0 -->
                                <e:elseif test="@item.shares == 0">
                                    <td class="tc">0</td>
                                </e:elseif>
                                <!-- 赎回份额不为0，可能确认中，可能确认失败 -->
                                <e:else>
                                    <!-- 确认中 -->
                                    <e:if test="@item.isConfirmation.toString() == 'FALSE'">
                                        <td class="tc">确认中</td>
                                    </e:if>
                                    <e:else>
                                        <td class="tc">赎回失败</td>
                                    </e:else>
                                </e:else>
                            </e:elseif>
                        </tr>
                    </e:iterator>
                    <%-- <tr>
                        <td class="tl t-1"><e:property value=""/></td>
                        <td class="tc">120009.00 (元)</td>
                        <td class="tc">40 (份)</td>
                    </tr>
                    <tr>
                        <td class="tl t-1">华泰百瑞价值成</td>
                        <td class="tc">2123.20 (份)</td>
                        <td class="tc">40 (元)</td>
                    </tr>
                    <tr>
                        <td class="tl t-1">诺德成长优势</td>
                        <td class="tc">2123.20 (份)</td>
                        <td class="tc">40 (元)</td>
                    </tr>
                    <tr>
                        <td class="tl t-1">华泰柏瑞稳健收超出字符换行显示</td>
                        <td class="tc">2123.20 (份)</td>
                        <td class="tc">
                            <p>申购失败!</p>
                            <a href="#">重新申购</a>
                        </td>
                    </tr> --%>
                </table>
            </div>
        </div>
    
    </div>
    </body>
</html>