package com.yeepay.g3.app.lmweChat.action.zt;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.lf5.util.DateFormatManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.lanmao.fund.facade.fundsales.dto.CalcFeeTradeRequestDto;
import com.lanmao.fund.facade.fundsales.dto.CalcFeeTradeResponseDto;
import com.lanmao.fund.facade.fundsales.service.FundCalendarFacade;
import com.lanmao.fund.facade.fundsales.service.FundInfoQueryServiceFacade;
import com.lanmao.fund.facade.fundsales.service.FundTradeQueryFacade;
import com.lanmao.fund.facade.fundsales.service.LMFundInfoQueryServiceFacade;
import com.lanmao.fund.facade.queryservice.dto.BasicFundInfoDTO;
import com.lanmao.fund.facade.queryservice.dto.BasicFundInfoRespDTO;
import com.lanmao.fund.facade.queryservice.dto.LMFundDetailInfoDTO;
import com.lanmao.fund.facade.queryservice.enumtype.FundTypeEnum;
import com.yeepay.g3.app.lmweChat.action.BaseAction;
import com.yeepay.g3.app.lmweChat.utils.ConstantUtils;
import com.yeepay.g3.app.lmweChat.utils.DateUtils;
import com.yeepay.g3.app.lmweChat.utils.LmConstants;
import com.yeepay.g3.app.lmweChat.utils.NumberFormatUtils;
import com.yeepay.g3.facade.lmportal.dto.AccountInfoQueryResultDto;
import com.yeepay.g3.facade.lmportal.dto.MemberDto;
import com.yeepay.g3.facade.lmportal.dto.MemberPwdConstraintDto;
import com.yeepay.g3.facade.lmportal.service.LanmaoDemandFacade;
import com.yeepay.g3.facade.lmportal.service.MemberPasswordFacade;
import com.yeepay.g3.facade.zt.dto.ZtPolicyDTO;
import com.yeepay.g3.facade.zt.dto.ZtPolicyFundOrderDetailDTO;
import com.yeepay.g3.facade.zt.dto.ZtPolicyInvestNewPlanDTO;
import com.yeepay.g3.facade.zt.dto.ZtPolicyOrderAndPlanDTO;
import com.yeepay.g3.facade.zt.dto.ZtPolicyOrderDetailDTO;
import com.yeepay.g3.facade.zt.dto.ZtPolicyProductDTO;
import com.yeepay.g3.facade.zt.dto.ZtPurFeeDetailDTO;
import com.yeepay.g3.facade.zt.dto.ZtPurchaseParamDTO;
import com.yeepay.g3.facade.zt.dto.ZtSinglePurchaseParamDTO;
import com.yeepay.g3.facade.zt.dto.ZtUserPolicyFundPropertionDTO;
import com.yeepay.g3.facade.zt.enums.PolicyFundOrderDetailStatusEnum;
import com.yeepay.g3.facade.zt.enums.ZtFundTypeEnum;
import com.yeepay.g3.facade.zt.facade.ZtPolicyCalculateFacade;
import com.yeepay.g3.facade.zt.facade.ZtPolicyFacade;
import com.yeepay.g3.facade.zt.facade.ZtPolicyFundOrderDetailFacade;
import com.yeepay.g3.facade.zt.facade.ZtPolicyOrderDetailFacade;
import com.yeepay.g3.facade.zt.facade.ZtPolicyProductFacade;
import com.yeepay.g3.facade.zt.facade.ZtPurchaseFacade;
import com.yeepay.g3.utils.common.CheckUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.json.JSONException;
import com.yeepay.g3.utils.common.json.JSONObject;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;
import com.yeepay.g3.utils.web.IpUtils;
import com.yeepay.g3.utils.web.emvc.annotation.Param;


/**
 * 智能投资购买Action
 * 
 * @Copyright: Copyright (c)2011
 * @company 懒猫金服
 * @author ping.zhu
 * @since 2016-10-26
 * @version 1.0
 */
@Controller
public class PurchaseAction extends BaseAction{
	
	protected ZtPolicyProductFacade ztPolicyProductFacade = RemoteServiceFactory.getService(ZtPolicyProductFacade.class);
	protected ZtPolicyFacade ztPolicyFacade=RemoteServiceFactory.getService(ZtPolicyFacade.class);
	protected ZtPurchaseFacade ztPurchaseFacade=RemoteServiceFactory.getService(ZtPurchaseFacade.class);
	protected ZtPolicyOrderDetailFacade ztPolicyOrderDetailFacade=RemoteServiceFactory.getService(ZtPolicyOrderDetailFacade.class);
	protected ZtPolicyFundOrderDetailFacade ztPolicyFundOrderDetailFacade = RemoteServiceFactory.getService(ZtPolicyFundOrderDetailFacade.class);
	protected ZtPolicyCalculateFacade ztPolicyCalculateFacade = RemoteServiceFactory.getService(ZtPolicyCalculateFacade.class);
	
	protected LanmaoDemandFacade lanmaoDemandFacade=RemoteServiceFactory.getService(LanmaoDemandFacade.class);
	protected MemberPasswordFacade memberPasswordFacade = RemoteServiceFactory.getService(MemberPasswordFacade.class);
	protected FundCalendarFacade fundCalendarFacade= RemoteServiceFactory.getService(FundCalendarFacade.class);
	protected FundInfoQueryServiceFacade fundInfoQueryServiceFacade = RemoteServiceFactory
			.getService(FundInfoQueryServiceFacade.class);
	protected LMFundInfoQueryServiceFacade lMFundInfoQueryServiceFacade = RemoteServiceFactory
			.getService(LMFundInfoQueryServiceFacade.class);
	/**
	 * 交易类查询入口
	 */
	protected FundTradeQueryFacade fundTradeQueryFacade = RemoteServiceFactory
			.getService(FundTradeQueryFacade.class);
//	protected ZtPolicyProductFacade ztPolicyProductFacade = RemoteServiceFactory.getService("http://localhost:8080/activity-hessian/", RemotingProtocol.HESSIAN, ZtPolicyProductFacade.class);
//	protected ZtPolicyFacade ztPolicyFacade=RemoteServiceFactory.getService("http://localhost:8080/activity-hessian/", RemotingProtocol.HESSIAN, ZtPolicyFacade.class);
//	private ZtPolicyFundOrderDetailFacade ztPolicyFundOrderDetailFacade = RemoteServiceFactory.getService("http://localhost:8080/activity-hessian/", RemotingProtocol.HESSIAN, ZtPolicyFundOrderDetailFacade.class);
//	private ZtPolicyCalculateFacade ztPolicyCalculateFacade = RemoteServiceFactory.getService("http://localhost:8080/activity-hessian/", RemotingProtocol.HESSIAN, ZtPolicyCalculateFacade.class);
//	private ZtPurchaseFacade ztPurchaseFacade = RemoteServiceFactory.getService("http://localhost:8080/activity-hessian/", RemotingProtocol.HESSIAN, ZtPurchaseFacade.class);
//	private ZtPolicyOrderDetailFacade ztPolicyOrderDetailFacade = RemoteServiceFactory.getService("http://localhost:8080/activity-hessian/", RemotingProtocol.HESSIAN, ZtPolicyOrderDetailFacade.class);


	/**
	 * 去购买组合页面
	 * @param policyId
	 * @param isFirstBuyThisPolicy 是否第一次购买这个策略id
     * @author ping.zhu
	 */
	public String toPurchase(@Param("sceneId") Long sceneId,
			@Param("policyId") Long policyId,
			@Param("policyOrderId") Long policyOrderId,
			@Param("purAmount") BigDecimal purAmount) {
		/**
		 * 懒猫账户余额
		 */
		DecimalFormat df=new DecimalFormat("0.00");
		BigDecimal accountBalance=null;
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		//获取token序列,存储于session中
		String token = LmConstants.makeToken();
		session.setAttribute("rechargeToken", token);
		//存于页面
		addModelObject("token", token);
		//1.查询懒猫账户余额
		try {
			AccountInfoQueryResultDto accountInfoQueryResultDto = lanmaoDemandFacade
					.queryAccount(memberDto.getMemberNo(), new Date());
			accountBalance = accountInfoQueryResultDto == null
					|| accountInfoQueryResultDto.getAmount() == null ? BigDecimal.ZERO
					: accountInfoQueryResultDto.getAmount();
			addModelObject("accountBalance", df.format(accountBalance));
		} catch (Exception e) {
			logger.error("[toPurchase] info={},ERROR={}", "查询用户"+memberDto.getMemberNo()+"懒猫账户余额时异常",
					e.getMessage());
		}
		List<ZtUserPolicyFundPropertionDTO> resultlist=null;
		BigDecimal fee=new BigDecimal(0);
		ZtUserPolicyFundPropertionDTO fundProDto=null;
		//2.判断用户是否是首次购买这个策略id，如果是：不用重新计算基金占比
		if(null!=policyId&&null!=sceneId){
			//(1).根据策略id查询当前策略的各个产品占比和
			List<ZtPolicyProductDTO> productList=ztPolicyProductFacade.selectPolicyProductList(policyId);
			//(2).根据策略id查询当前策略名称，起投金额
			ZtPolicyDTO policy=ztPolicyFacade.selectPolicyByPrimaryKey(policyId);
			resultlist=new ArrayList<ZtUserPolicyFundPropertionDTO>();
			for(ZtPolicyProductDTO item:productList){
				fundProDto=new ZtUserPolicyFundPropertionDTO();
				fundProDto.setAdditionalAmount(purAmount.multiply(item.getProductProportion()));
				fundProDto.setAdditionalProportion(item.getProductProportion());
				fundProDto.setFundCode(item.getProductCode());
				fundProDto.setFundName(item.getProductName());
				fundProDto.setFundType(item.getProductType());
				fundProDto.setFundMinAmount(item.getProductMinAmount());
				resultlist.add(fundProDto);
			}
			addModelObject("resultList", resultlist);
			addModelObject("policyName", policy.getPolicyName());
			addModelObject("purAmount", purAmount);
			addModelObject("sceneId", sceneId);
			addModelObject("policyId", policyId);
			addModelObject("minBuy", policy.getMinPurchaseAmount().setScale(0,BigDecimal.ROUND_HALF_UP));
			}
		//3.已经购买过这个策略的用户
		else if(null!=policyOrderId){
			BigDecimal amount=null;
			//根据会员策略交易汇总id查询
			//查询用户策略投资计划表每月需要投多少钱，本月已经投资了多少钱  参数：会员编号 策略id
			ZtPolicyOrderAndPlanDTO ztPolicyOrderAndPlanDTO=null;
			try {
				logger.info("[toPurchase] info={}",policyOrderId);		
				ztPolicyOrderAndPlanDTO = ztPurchaseFacade.queryUserPolicyOrderAndPlan(policyOrderId, memberDto.getMemberNo());
				//TODO
				String proportionResult=calculatePolicyProportion(ztPolicyOrderAndPlanDTO.getZtPolicyOrderDTO().getPolicyId());
				addModelObject("proportionResult", proportionResult);
				BigDecimal perInvestAmount=ztPolicyOrderAndPlanDTO.getZtPolicyInvestPlanDTO().getPerInvestAmount();
				BigDecimal curTermInvestAmount=ztPolicyOrderAndPlanDTO.getZtPolicyOrderDTO().getCurTermInvestAmount();
				ZtPolicyDTO policy=ztPolicyFacade.selectPolicyByPrimaryKey(ztPolicyOrderAndPlanDTO.getZtPolicyInvestPlanDTO().getPolicyId());
				addModelObject("policyName", policy.getPolicyName());
				//(1).如果本月投资金额为0  investAmount 是指本月剩余应投金额  并带入购买页面
				if(curTermInvestAmount.compareTo(BigDecimal.ZERO)==0){
					amount=perInvestAmount;
				//(2).当月累计投资金额<每月定投金额
				}else if(perInvestAmount.compareTo(curTermInvestAmount)>0){
					//投资金额=计划投资金额-当月累计投资金额 
					amount=perInvestAmount.subtract(curTermInvestAmount);
				}else{
					amount=new BigDecimal(0);
				}
				addModelObject("minBuy", policy.getMinPurchaseAmount().setScale(0,BigDecimal.ROUND_HALF_UP));
			} catch (Exception e) {
				logger.error("[toPurchase] info={},ERROR={}", "查询投资计划和已投金额时异常",
						e.getMessage());
			}
			addModelObject("policyOrderId", policyOrderId);
			addModelObject("purAmount",amount);
//			resultlist=ztPurchaseFacade.calculatePolicyFundProportionBalance(amount, policyOrderId);
			addModelObject("resultList", resultlist);
		}else{
			//系统异常
			return FAILED;
		}
		//查询预估手续费
		try {
			List<ZtPurFeeDetailDTO> feeList=ztPurchaseFacade.queryPurchaseFeeDetail(resultlist);
			if(!CheckUtils.isEmpty(feeList)){
				for(ZtPurFeeDetailDTO item:feeList){
					//TODO 折扣前手续费没有添加
					fee=fee.add(item.getRealFareSx());
				}
			}
		} catch (Exception e) {
			logger.error("[toPurchase] info={},ERROR={}", "查询手续费异常",
					e.getMessage());		
			}
		addModelObject("fee", df.format(fee));
		
		return SUCCESS;
	}
	
	/**
	* @Description: 计算策略股债配比
	* @author ping.zhu   
	* @date 2016年12月1日 下午3:22:33 
	 */
	private String calculatePolicyProportion(Long policyId){
		//根据策略id查询当前策略的各个产品占比和
		List<ZtPolicyProductDTO> productList=ztPolicyProductFacade.selectPolicyProductList(policyId);
		if(null==productList&&productList.size()==0)
			return null;
		
		BigDecimal bondProportion=new BigDecimal(0);
		BigDecimal stockProportion=new BigDecimal(0);
		StringBuffer result=new StringBuffer();
		for(ZtPolicyProductDTO item:productList){
			switch (item.getProductType()) {
			   //债基
			case BOND:
				bondProportion=bondProportion.add(item.getProductProportion());
				break;
				//股基
			case STOCK:
				stockProportion=stockProportion.add(item.getProductProportion());
				break;
			default:
				break;
			}
		}
		logger.info("[calculatePolicyProportion] bondProportion={},stockProportion={}",bondProportion,stockProportion);	
		//判断是否有小数
		result.append(new BigDecimal(stockProportion.multiply(new BigDecimal(10)).intValue()).compareTo(stockProportion.multiply(new BigDecimal(10)))==0?
				stockProportion.multiply(new BigDecimal(10)).setScale(0).toString():stockProportion.multiply(new BigDecimal(10)).setScale(1).toString());
		result.append("：");
		result.append(new BigDecimal(bondProportion.multiply(new BigDecimal(10)).intValue()).compareTo(bondProportion.multiply(new BigDecimal(10)))==0?
				bondProportion.multiply(new BigDecimal(10)).setScale(0).toString():bondProportion.multiply(new BigDecimal(10)).setScale(1).toString());
		return result.toString();
	}
	
	
	/**
	 * 
	* @Description: 计算基金占比
	* @author ping.zhu   
	* @date 2016年11月8日 上午9:39:23 
	* @param policyOrderId
	* @param purAmount
	* @return
	 */
	public String calculateProportion(@Param("policyOrderId") Long policyOrderId,
			@Param("purAmount") BigDecimal purAmount){
		if(CheckUtils.isEmpty(purAmount)&&CheckUtils.isEmpty(policyOrderId)){
			logger.error("[calculateProportion] info={},ERROR={}", "计算基金占比时参数为空");
			return JSON;
		}
		Map<String,List<ZtUserPolicyFundPropertionDTO>> map=new HashMap<String, List<ZtUserPolicyFundPropertionDTO>>();
		try{
//			List<ZtUserPolicyFundPropertionDTO> list=ztPurchaseFacade.calculatePolicyFundProportionBalance(purAmount, policyOrderId);
//			map.put("proportion", list);
			setJsonModel(map);
		}catch(Exception e){
			logger.error("[calculateProportion] info={},ERROR={}", "计算基金占比时异常",e.getMessage());
		}
		return JSON;
	}
	
	
	/**
	* @Description: 组合购买
	* @author ping.zhu   
	* @date 2016年11月8日 上午9:40:30 
	 */
	public String purchase(@Param("fundList") String fundList,
			@Param("tradePwd") String tradePwd,
			@Param("sceneId") Long sceneId,
			@Param("policyId") Long policyId,
			@Param("policyOrderId") Long policyOrderId,@Param("token") String token) {
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		HttpServletRequest request=getMvcFacade().getRequest();
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		//验证提交表单 
		String sessionToken = (String) session.getAttribute("rechargeToken");
		if (!StringUtils.isEmpty(token) && token.equals(sessionToken)) {
			session.removeAttribute("rechargeToken");
		if(StringUtils.isEmpty(fundList)){
			resultMap.put("status", SYSTEM_EXCEPTION);
			//更新token
			String newToken = LmConstants.makeToken();
			session.setAttribute("rechargeToken", newToken);
			//传到页面
			resultMap.put("token", newToken);
			setJsonModel(resultMap);
			return JSON;
		}
		
		List<ZtUserPolicyFundPropertionDTO> list=null;
		BigDecimal purMoney=null;
		try {
			list = new ArrayList<ZtUserPolicyFundPropertionDTO>();
			ZtUserPolicyFundPropertionDTO item=null;
			purMoney = new BigDecimal(0);
			for(String fund:fundList.split("#")){
				item=new ZtUserPolicyFundPropertionDTO();
				String[] fundDetail=fund.split(";");
				for(int i=0;i<5;i++){
					switch(i){
					case 0:
						item.setFundName(fundDetail[i]);
						break;
					case 1:
						item.setAdditionalProportion(new BigDecimal(fundDetail[i]));
						break;
					case 2:
						item.setAdditionalAmount(new BigDecimal(fundDetail[i]));
						purMoney=purMoney.add(new BigDecimal(fundDetail[i]));
						break;
					case 3:
						item.setFundCode(fundDetail[i]);
						break;
					case 4:
						if(ZtFundTypeEnum.BOND.toString().equals(fundDetail[i])){
						item.setFundType(ZtFundTypeEnum.BOND);
						}else{
							item.setFundType(ZtFundTypeEnum.STOCK);
						}
						break;
					}
				}
				list.add(item);
			}
		} catch (Exception e) {
			//更新token
			String newToken = LmConstants.makeToken();
			session.setAttribute("rechargeToken", newToken);
			//传到页面
			resultMap.put("token", newToken);
			return JSON;
		}
		try {
			AccountInfoQueryResultDto accountInfoQueryResultDto = lanmaoDemandFacade
					.queryAccount(memberDto.getMemberNo(), new Date());
			BigDecimal accountBalance = accountInfoQueryResultDto == null
					|| accountInfoQueryResultDto.getAmount() == null ? BigDecimal.ZERO
					: accountInfoQueryResultDto.getAmount();
			if(accountBalance.compareTo(purMoney)==-1){
				resultMap.put("status", "UNAVAILABLE_ACCOUNT");
				//更新token
				String newToken = LmConstants.makeToken();
				session.setAttribute("rechargeToken", newToken);
				//传到页面
				resultMap.put("token", newToken);
				setJsonModel(resultMap);
				return JSON;
			}
		} catch (Exception e) {
			logger.error("[purchase] info={},ERROR={}",
					"查询用户" + memberDto.getMemberNo() + "懒猫账户余额时异常",
					e.getMessage());
			resultMap.put("status", "SYSTEM_EXCEPTION");
			//更新token
			String newToken = LmConstants.makeToken();
			session.setAttribute("rechargeToken", newToken);
			//传到页面
			resultMap.put("token", newToken);
			setJsonModel(resultMap);
			return JSON;
		}
		
		
		try {
			
			// 验证交易密码
			MemberPwdConstraintDto memberPwdConstraintDto = memberPasswordFacade.checkTradePassword(memberDto.getMemberNo(), tradePwd);
			if (!memberPwdConstraintDto.getResultFlag()) {
				resultMap.put("status", "WRONG_PWD");
				resultMap.put("description", memberPwdConstraintDto);
				//更新token
				String newToken = LmConstants.makeToken();
				session.setAttribute("rechargeToken", newToken);
				//传到页面
				resultMap.put("token", newToken);
				setJsonModel(resultMap);
				return JSON;
			}
			
			ZtPurchaseParamDTO paramDto = new ZtPurchaseParamDTO();
			if(null!=sceneId&&null!=policyId){
				paramDto.setSceneId(sceneId);
				paramDto.setPolicyId(policyId);
			}else if(null!=policyOrderId){
				paramDto.setPolicyOrderId(policyOrderId);
			}
			paramDto.setPurchaseMoney(purMoney);
			paramDto.setMemberNo(memberDto.getMemberNo());
			paramDto.setMerchantNo(LmConstants.getZTMerchantNo());
			paramDto.setFundList(list);
			paramDto.setClientIp(IpUtils.getIpAddr(request));
			paramDto.setUserAgent(request.getHeader("User-Agent"));
			Long orderDetailId=ztPurchaseFacade.purchasePolicy(paramDto);
			if(null==orderDetailId){
				resultMap.put("status", SYSTEM_EXCEPTION);
				resultMap.put("orderDetailId", orderDetailId);
				//更新token
				String newToken = LmConstants.makeToken();
				session.setAttribute("rechargeToken", newToken);
				//传到页面
				resultMap.put("token", newToken);
				setJsonModel(resultMap);
				return JSON;
			}
			resultMap.put("status", "SUCCESS");
			resultMap.put("orderDetailId", orderDetailId);
			setJsonModel(resultMap);
		} catch (Exception e) {
			logger.error("[groupPurchase] info={},ERROR={}", "购买组合基金时异常",e.getMessage());
			resultMap.put("status", SYSTEM_EXCEPTION);
			//更新token
			String newToken = LmConstants.makeToken();
			session.setAttribute("rechargeToken", newToken);
			//传到页面
			resultMap.put("token", newToken);
			setJsonModel(resultMap);
			return JSON;
		}
				}
		return JSON;
	}
	
	/**
	* @Description: 购买成功页面
	* @author ping.zhu   
	* @date 2016年11月8日 下午1:52:59 
	 */
	public String toPurchaseSuccess(@Param("orderDetailId") Long orderDetailId,@Param("fee") BigDecimal fee) {
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		DecimalFormat df=new DecimalFormat("0.00");
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		//1.根据策略订单id查询基金订单详情
		List<Map<String,Object>> fundList=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=null;
		BigDecimal purMoney=new BigDecimal(0);
		List<ZtPolicyFundOrderDetailDTO> list = ztPolicyFundOrderDetailFacade
				.queryFundDetailByPolOrdDetialId(orderDetailId,memberDto.getMemberNo());
		if(!CheckUtils.isEmpty(list)){
			for(ZtPolicyFundOrderDetailDTO item:list){
				//失败不展示
				if(item.getStatus()!=PolicyFundOrderDetailStatusEnum.PURCHASECOMMIT){
					continue;
				}
				map=new HashMap<String, Object>();
				map.put("fundName", item.getFundName());
				map.put("amount", df.format(item.getBalance()));
				purMoney=purMoney.add(item.getBalance());
				fundList.add(map);
			}
		}
		addModelObject("purMoney", df.format(purMoney));
		addModelObject("fundList", fundList);
		addModelObject("fee", df.format(fee));
		
		//2.计算下月应投金额
		try {
			ZtPolicyOrderDetailDTO ztPolicyOrderDetailDTO=ztPolicyOrderDetailFacade.queryPolicyOrderDetailById(orderDetailId,memberDto.getMemberNo());
			addModelObject("policyOrderId", ztPolicyOrderDetailDTO.getPolicyOrderId());
			ZtPolicyOrderAndPlanDTO ztPolicyOrderAndPlanDTO = ztPurchaseFacade.queryUserPolicyOrderAndPlan(ztPolicyOrderDetailDTO.getPolicyOrderId(), memberDto.getMemberNo());
			ZtPolicyDTO policy=ztPolicyFacade.selectPolicyByPrimaryKey(ztPolicyOrderAndPlanDTO.getZtPolicyOrderDTO().getPolicyId());
			addModelObject("sceneName", ztPolicyOrderAndPlanDTO.getZtPolicyInvestPlanDTO().getPolicyCalculateType().toString());
			addModelObject("policyName", policy.getPolicyName());
			BigDecimal perInvestAmount=ztPolicyOrderAndPlanDTO.getZtPolicyInvestPlanDTO().getPerInvestAmount();
			BigDecimal curTermInvestAmount=ztPolicyOrderAndPlanDTO.getZtPolicyOrderDTO().getCurTermInvestAmount();
			
			//(1) |累计投资金额-定投金额|<=0.5
			if(purMoney.add(curTermInvestAmount).subtract(perInvestAmount).abs().compareTo(new BigDecimal(0.03))!=1){
				addModelObject("nextMonthBuy",df.format(perInvestAmount));
			//(2)累计投资金额-定投金额<0.5
			}else if(purMoney.add(curTermInvestAmount).subtract(perInvestAmount).compareTo(new BigDecimal(0.03))==-1){
				addModelObject("thisMonthBuy", df.format(perInvestAmount.subtract(curTermInvestAmount).subtract(purMoney)));
			}else{
				//重新计算临时策略
				Long id=ztPolicyOrderAndPlanDTO
						.getZtPolicyInvestPlanDTO().getId();
				ZtPolicyInvestNewPlanDTO ztPolicyInvestNewPlanDto = ztPolicyCalculateFacade
						.calculatePolicyInvestNewPlan(ztPolicyOrderAndPlanDTO
								.getZtPolicyInvestPlanDTO().getId(), orderDetailId);
				addModelObject("nextMonthBuy", df.format(ztPolicyInvestNewPlanDto.getNewPerInvestAmount().compareTo(new BigDecimal(0))==0?perInvestAmount:ztPolicyInvestNewPlanDto.getNewPerInvestAmount()));
			}
		} catch (Exception e) {
			logger.error("[toPurchaseSuccess] info={},ERROR={}", "查询收益时间时异常",e.getMessage());
		}
		
		//3.查询收益时间
		try {
			List<Date> dateList=fundCalendarFacade.queryNextNFundWorKDay(DateUtils.formatFundDate(new Date()), 0, 2);
			addModelObject("comfirmDay", format1.format(dateList.get(0)));
			addModelObject("incomeDay", format1.format(dateList.get(1)));
			addModelObject("buyDay", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		} catch (Exception e) {
			logger.error("[toPurchaseSuccess] info={},ERROR={}", "查询收益时间时异常",e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	* @Description: 预估手续费
	* @author ping.zhu   
	* @date 2016年11月8日 下午3:02:47 
	 */
	public String calculateFee(@Param("fundList")String fundList){
		List<ZtUserPolicyFundPropertionDTO> list=new ArrayList<ZtUserPolicyFundPropertionDTO>();
		ZtUserPolicyFundPropertionDTO item=null;
		Map<String,Object> resultMap=new HashMap<String, Object>();
		try {
			for(String fund:fundList.split("#")){
				item=new ZtUserPolicyFundPropertionDTO();
				String[] fundDetail=fund.split(";");
				for(int i=0;i<4;i++){
					switch(i){
					case 0:
						item.setFundName(fundDetail[i]);
						break;
					case 1:
						item.setAdditionalProportion(new BigDecimal(fundDetail[i]));
						break;
					case 2:
						item.setAdditionalAmount(new BigDecimal(fundDetail[i]));
						break;
					case 3:
						item.setFundCode(fundDetail[i]);
						break;
					}
				}
				list.add(item);
			}
			BigDecimal realFee=new BigDecimal(0);
			BigDecimal fee=new BigDecimal(0);
			List<ZtPurFeeDetailDTO> resultList=ztPurchaseFacade.queryPurchaseFeeDetail(list);
			for(ZtPurFeeDetailDTO feeDto:resultList){
				realFee=realFee.add(feeDto.getRealFareSx());
				fee=fee.add(feeDto.getFareSx());
			}
			resultMap.put("realFee", realFee);
			resultMap.put("fee", fee);
			setJsonModel(resultMap);
		} catch (Exception e) {
			resultMap.put("status", SYSTEM_EXCEPTION);
			setJsonModel(resultMap);
			logger.error("[calculateFee] info={},ERROR={}", "计算手续费时异常",e.getMessage());

		}
		return JSON;
	}
	
	/**
	* @Description: 去手续费详情页面
	* @author ping.zhu   
	* @date 2016年11月8日 下午3:03:22 
	 */
	public String toPurchaseFeeDetail(@Param("policyId") Long policyId,
			@Param("policyOrderId") Long policyOrderId,
			@Param("purAmount") BigDecimal purAmount){
		List<ZtUserPolicyFundPropertionDTO> fundList=null;
		ZtUserPolicyFundPropertionDTO fundProDto=null;
		DecimalFormat df =new DecimalFormat("0.00");
		if (null != policyId&&purAmount!=null) {
			try {
				// (1).根据策略id查询当前策略的各个产品占比和
				List<ZtPolicyProductDTO> productList = ztPolicyProductFacade
						.selectPolicyProductList(policyId);
				fundList = new ArrayList<ZtUserPolicyFundPropertionDTO>();
				for (ZtPolicyProductDTO item : productList) {
					fundProDto = new ZtUserPolicyFundPropertionDTO();
					fundProDto.setAdditionalAmount(purAmount.multiply(item.getProductProportion()));
					fundProDto.setAdditionalProportion(item.getProductProportion());
					fundProDto.setFundCode(item.getProductCode());
					fundProDto.setFundName(item.getProductName());
					fundProDto.setFundMinAmount(item.getProductMinAmount());
					fundList.add(fundProDto);
				}
			} catch (Exception e) {
				logger.error("[toPurchaseFeeDetail] info={},ERROR={}", "查询基金占比时异常",e.getMessage());
				return SYSTEM_EXCEPTION;
			}
		}else if(policyOrderId!=null&&purAmount!=null){
			try {
//				fundList=ztPurchaseFacade.calculatePolicyFundProportionBalance(purAmount, policyOrderId);
			} catch (Exception e) {
				logger.error("[toPurchaseFeeDetail] info={},ERROR={}", "查询基金占比时异常",e.getMessage());
				return SYSTEM_EXCEPTION;
			}
		}
		try{
			BigDecimal totalFee=new BigDecimal(0);
			List<ZtPurFeeDetailDTO> resultList=ztPurchaseFacade.queryPurchaseFeeDetail(fundList);
			if(!CheckUtils.isEmpty(resultList)){
				for(ZtPurFeeDetailDTO item:resultList){
					totalFee=totalFee.add(item.getRealFareSx());
				}
			}
			addModelObject("feeList", resultList);
			addModelObject("totalFee", df.format(totalFee));
		}catch(Exception e){
			logger.error("[toPurchaseFeeDetail] info={},ERROR={}", "计算基金费率时异常",e.getMessage());
			return SYSTEM_EXCEPTION;
		}
		return SUCCESS;
	}
	public static void main(String args[]){
			DecimalFormat df=new DecimalFormat("0.00");
			df.format(new BigDecimal(0.000));
	}
	
	/**
	* @Description: 单只基金申购成功页面
	* @author ping.zhu   
	* @date 2016年11月15日 下午6:16:22 
	 */
	public String toSinglePurchaseSuccess(@Param("orderDetailId") Long orderDetailId){
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		try {
			ZtPolicyOrderDetailDTO ztPolicyOrderDetailDto=ztPolicyOrderDetailFacade.queryPolicyOrderDetailById(orderDetailId, memberDto.getMemberNo());
			List<ZtPolicyFundOrderDetailDTO> list=ztPolicyFundOrderDetailFacade.queryFundDetailByPolOrdDetialId(orderDetailId, memberDto.getMemberNo());
			if(null!=list&&list.size()>0&&list.get(0)!=null){
				ZtPolicyFundOrderDetailDTO ztPolicyFundOrderDetailDto=list.get(0);
				List<ZtPolicyProductDTO> productList=ztPolicyProductFacade.selectPolicyProductList(ztPolicyOrderDetailDto.getPolicyId());
				if(productList!=null&&productList.size()>0){
					for(ZtPolicyProductDTO item:productList){
						if(ztPolicyFundOrderDetailDto.getFundCode().equals(item.getProductCode())){
							addModelObject("fundName", item.getProductName());
						}
					}
				}
				addModelObject("balance", ztPolicyFundOrderDetailDto.getBalance());
				addModelObject("orderDetailId", orderDetailId);
			}
		} catch (Exception e) {
			logger.error("[toSinglePurchaseSuccess] info={},ERROR={}", "跳转单只基金购买成功页面异常",e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 基金单只购买
	 * @author hongbin.kang
	 * @date 2016年11月15日 下午3:38:34
	 * @param fundCode
	 * @param money
	 * @param policyOrderId
	 * @return
	 * @throws Exception
	 */
	public String toFundPurchase(@Param("amount") BigDecimal amount,@Param("fundCode") String fundCode,@Param("fundOrderDetailId") Long fundOrderDetailId) throws Exception {
		logger.info("[toPurchase] 用户基金份额申请.fundCode="
				+ fundCode);
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		//1.数据信息
		BasicFundInfoDTO fund = getFundBasicByFundCode(fundCode);
		addModelObject("fundType", fund.getFundType());
		addModelObject("fundCode", fund.getFundCode());
		addModelObject("fundName", fund.getFundName());
		addModelObject("yearIncrease", fund.getrRSinceThisYear());
		addModelObject("tradingDay", DateUtils.getDateByFormat(fund.getAnnDate(),
				DateUtils.MMDD));
		addModelObject("unitNv", fund.getUnitNV());
		addModelObject("leastPurchase", getLeastPurchase(fund.getLeastPurchase()));
		addModelObject("fundStatus",  getFundDetailInfo(fundCode).getFundStatus());
		addModelObject("balance", NumberFormatUtils.DecimalToString(getUserAmount(memberDto.getMemberNo()), 2));
		addModelObject("amount", NumberFormatUtils.DecimalToString(amount, 2));
		addModelObject("fundOrderDetailId", fundOrderDetailId);
		//2.手续费
		String calcFee = NumberFormatUtils.DecimalToString(BigDecimal.ZERO, 2);
		CalcFeeTradeResponseDto calcFeeTradeResponseDto =
				fundTradeQueryFacade.calcFeeTradeQuery(new
						CalcFeeTradeRequestDto(LmConstants.getZTMerchantNo(),fundCode,String.valueOf(amount),null,ConstantUtils.FUNDBUSINCODE_BUY));
		if ("0".equals(calcFeeTradeResponseDto.getCode())) {
			logger.info("[PurchaseAction]-[computeFareSx]-[计算手续费]-info=",
					calcFeeTradeResponseDto.getDescription());
			calcFee = NumberFormatUtils.DecimalToString(
					calcFeeTradeResponseDto.getFareSx(), 2);
		}
		addModelObject("calcFee", calcFee);
		return SUCCESS;
	}
	
	protected BasicFundInfoDTO getFundBasicByFundCode(String fundCode) {
		List<String> funds = new ArrayList<String>();
		funds.add(fundCode);
		BasicFundInfoRespDTO resp = fundInfoQueryServiceFacade
				.queryBasicFundInfo(funds);
		List<BasicFundInfoDTO> basicInfo = resp.getProducts();
		if (basicInfo == null || basicInfo.size() == 0) {
			return new BasicFundInfoDTO();
		}
		BasicFundInfoDTO fund = basicInfo.get(0);
		return fund;
	}
	
	/**
	 * 获取最小申购金额
	 * 
	 * @param leastRedeem
	 * @return
	 */
	private BigDecimal getLeastPurchase(BigDecimal leastRedeem) {
		if (leastRedeem == null || leastRedeem.equals(new BigDecimal("0"))) {
			return new BigDecimal("0.01");
		}
		return leastRedeem;
	}
	
	/**
	 * 根据基金代码获取基金详情信息
	 * 
	 * @param fundCode
	 * @return
	 */
	protected LMFundDetailInfoDTO getFundDetailInfo(String fundCode) {
		return lMFundInfoQueryServiceFacade.queryFundDetailInfo(fundCode);
	}
	
	/**
	 * 获取用户账户余额
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected BigDecimal getUserAmount(String memberNo)
			throws Exception {
		AccountInfoQueryResultDto aiqrDto = lanmaoDemandFacade.queryAccount(
				memberNo, new Date());
		if(aiqrDto != null){
			return aiqrDto.getAmount();
		}else{
			return new BigDecimal("0.00");
		}
	}
	
	/**
	 * 单只申购基金
	 * @author hongbin.kang
	 * @date 2016年11月15日 下午3:53:53
	 * @param pwd
	 * @param amount
	 * @param fundCode
	 * @param model
	 * @param fundName
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	public String doFundPurchase(@Param("pwd") String pwd,@Param("amount") String amount,
			@Param("fundOrderDetailId") Long fundOrderDetailId) throws JSONException, IOException {

		logger.info("[doPurchase] 用户申购并校验交易密码.amount={},fundOrderDetailId={}",
				amount, fundOrderDetailId);
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			// 验证交易密码
			MemberPwdConstraintDto memberPwdConstraintDto = memberPasswordFacade.checkTradePassword(memberDto.getMemberNo(), pwd);
			if (!memberPwdConstraintDto.getResultFlag()) {
				resultMap.put("result", "WRONG_PWD");
				resultMap.put("description", memberPwdConstraintDto);
				setJsonModel(resultMap);
				return JSON;
			}
			
			// 4、用户账户余额校验
			BigDecimal balance = getUserAmount(memberDto.getMemberNo());
			if (new BigDecimal(amount).compareTo(balance) > 0) {
				resultMap.put("code", "fail");
				resultMap.put("result", "BALANCE_NOT_ENOUGH");
				setJsonModel(resultMap);
				return JSON;
			}
			// 5、用户申购
			ZtSinglePurchaseParamDTO paramDto = new ZtSinglePurchaseParamDTO();
			paramDto.setFunOrderDetailLId(fundOrderDetailId);
			paramDto.setMemberNo(memberDto.getMemberNo());
			paramDto.setMerchantNo(LmConstants.getZTMerchantNo());
			
			Long orderDetailId = ztPurchaseFacade.purchaseSingleFund(paramDto);
			Map<String, Object> map = new HashMap<String, Object>();
			logger.info("[purchaseSingleFund]-[申购基金成功]-fundDetailId={}",
					orderDetailId);
			if(null == orderDetailId) {
				resultMap.put("code", "fail");
				resultMap.put("result", "SYS_EXCEPTION");
				setJsonModel(resultMap);
				return JSON;
			}
			resultMap.put("result", "SUCCESS");
			resultMap.put("data", orderDetailId);
			setJsonModel(resultMap);
			return JSON;
		} catch (Exception e) {
			logger.error("[PurchaseAction][purchaseFund] 用户申请份额异常.", e);
			resultMap.put("code", "fail");
			resultMap.put("result", "SYS_EXCEPTION");
			setJsonModel(resultMap);
			return JSON;
		}
	}
	
	/**
	 * 计算手续费
	 * @author hongbin.kang
	 * @date 2016年11月15日 下午5:26:13
	 * @param fundCode
	 * @param balance
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public String computeFareSx(@Param("fundCode") String fundCode,@Param("amount") String amount) throws IOException{
		logger.info(
				"[computeFareSx]-[计算手续费]-fundCode={}, balance={}",
				fundCode, amount);
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			 CalcFeeTradeResponseDto calcFeeTradeResponseDto =
			 fundTradeQueryFacade.calcFeeTradeQuery(new
			 CalcFeeTradeRequestDto(LmConstants.getZTMerchantNo(),fundCode,amount,null,ConstantUtils.FUNDBUSINCODE_BUY));
			if (!"0".equals(calcFeeTradeResponseDto.getCode())) {
				logger.info("[PurchaseAction]-[computeFareSx]-[计算手续费报错]-info=",
						calcFeeTradeResponseDto.getDescription());
				resultMap.put("status", "error");
				resultMap.put("data", null);
				setJsonModel(resultMap);
				return JSON;
			}
			resultMap.put("status", "succ");
			resultMap.put("data", NumberFormatUtils.DecimalToString(
					calcFeeTradeResponseDto.getFareSx(), 2));
			setJsonModel(resultMap);
			return JSON;
		} catch (Exception e) {
			logger.error("[PurchaseAction]-[computeFareSx]-[计算手续费]-exception="
					+ e.getMessage(), e);
			resultMap.put("status", "fail");
			resultMap.put("data", null);
			setJsonModel(resultMap);
			return JSON;
		}
	}
}
