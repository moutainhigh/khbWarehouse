package com.yeepay.g3.app.lmweChat.action.asset;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.yeepay.g3.utils.common.StringUtils;
import com.lanmao.consultant.facade.dto.TblLmUser;
import com.lanmao.consultant.facade.service.UserFacade;
import com.lanmao.fund.facade.fundbiz.dto.FundbizRateInfo;
import com.lanmao.fund.facade.fundsales.dto.UserLastIncomeDetail;
import com.lanmao.fund.facade.fundsales.dto.UserLastIncomeQueryParam;
import com.lanmao.fund.facade.fundsales.dto.UserLastIncomesResult;
import com.lanmao.fund.facade.fundsales.dto.UserTotalCountResult;
import com.lanmao.fund.facade.fundsales.service.FundUserShareQueryFacade;
import com.lanmao.fund.facade.fundsales.service.LMFundInfoQueryServiceFacade;
import com.lanmao.fund.facade.queryservice.dto.FundInfoQueryResultPageDTO;
import com.lanmao.fund.facade.queryservice.dto.FundQueryParamDTO;
import com.lanmao.fund.facade.queryservice.enumtype.FundTypeEnum;
import com.lanmao.fund.facade.queryservice.enumtype.ListFundEnum;
import com.yeepay.g3.app.lmweChat.action.BaseAction;
import com.yeepay.g3.app.lmweChat.entity.ReturnUrlParamEntity;
import com.yeepay.g3.app.lmweChat.service.RequestParamBuilderService;
import com.yeepay.g3.app.lmweChat.utils.GetParamUtils;
import com.yeepay.g3.app.lmweChat.utils.HttpRequestUtils;
import com.yeepay.g3.app.lmweChat.utils.JSONObjectUtils;
import com.yeepay.g3.app.lmweChat.utils.LmConstants;
import com.yeepay.g3.app.lmweChat.utils.SecretUtils;
import com.yeepay.g3.app.lmweChat.utils.StringProcessorUtils;
import com.yeepay.g3.facade.activity.dto.ActivityWXSendMessageDTO;
import com.yeepay.g3.facade.activity.dto.ZtMemberWealthInfoDTO;
import com.yeepay.g3.facade.activity.enums.ActivityWXSendMessageEnum;
import com.yeepay.g3.facade.activity.enums.ShareBizTypeEnum;
import com.yeepay.g3.facade.activity.facade.ActivityUserMessageFacade;
import com.yeepay.g3.facade.activity.facade.ActivityWXSendMessageFacade;
import com.yeepay.g3.facade.fundbiz.dto.BalanceInfoResultDTO;
import com.yeepay.g3.facade.fundbiz.dto.FundIncomeDTO;
import com.yeepay.g3.facade.fundbiz.dto.FundIncomePageDTO;
import com.yeepay.g3.facade.fundbiz.dto.IncomeInfoResultDTO;
import com.yeepay.g3.facade.fundbiz.service.FundBizTranscationFacade;
import com.yeepay.g3.facade.fundbiz.service.FundQueryFacade;
import com.yeepay.g3.facade.lmlc.account.dto.param.FixedTimeIncomeHistoryListParamDto;
import com.yeepay.g3.facade.lmlc.account.dto.result.FixedTimeAccountResultDto;
import com.yeepay.g3.facade.lmlc.account.dto.result.FixedTimeHistoryIncomeListResultDto;
import com.yeepay.g3.facade.lmlc.account.dto.result.FixedTimeIncomeHistoryResultDto;
import com.yeepay.g3.facade.lmlc.account.enumtype.QueryStatusEnum;
import com.yeepay.g3.facade.lmlc.account.service.FixedTimeAccountFacade;
import com.yeepay.g3.facade.lmlc.trust.dto.api.ProductDetailForWXResultDto;
import com.yeepay.g3.facade.lmlc.trust.service.api.TrustQueryFacade;
import com.yeepay.g3.facade.lmportal.dto.AccountInfoQueryResultDto;
import com.yeepay.g3.facade.lmportal.dto.BankCardInfoDto;
import com.yeepay.g3.facade.lmportal.dto.CardInfoResultDto;
import com.yeepay.g3.facade.lmportal.dto.MemberDto;
import com.yeepay.g3.facade.lmportal.dto.MemberPwdConstraintDto;
import com.yeepay.g3.facade.lmportal.dto.QueryOrderParamDto;
import com.yeepay.g3.facade.lmportal.dto.QueryOrderResultDto;
import com.yeepay.g3.facade.lmportal.dto.QuickPayParamDto;
import com.yeepay.g3.facade.lmportal.dto.QuickPayResultDto;
import com.yeepay.g3.facade.lmportal.dto.WithdrawParamDto;
import com.yeepay.g3.facade.lmportal.dto.WithdrawResultDto;
import com.yeepay.g3.facade.lmportal.enumtype.DeviceEnum;
import com.yeepay.g3.facade.lmportal.enumtype.OrderStatusEnum;
import com.yeepay.g3.facade.lmportal.enumtype.QuickPayStatusEnum;
import com.yeepay.g3.facade.lmportal.enumtype.WithdrawStatusEnum;
import com.yeepay.g3.facade.lmportal.service.LMAccountTradeManagementFacde;
import com.yeepay.g3.facade.lmportal.service.LPQueryFacade;
import com.yeepay.g3.facade.lmportal.service.LanmaoDemandFacade;
import com.yeepay.g3.facade.lmportal.service.MemberManagementFacade;
import com.yeepay.g3.facade.lmportal.service.MemberPasswordFacade;
import com.yeepay.g3.facade.lmrouter.dto.result.PlatChannelResultDto;
import com.yeepay.g3.facade.lmrouter.enumtype.PlatChannelStatusEnum;
import com.yeepay.g3.facade.lmrouter.enumtype.RouteTypeEnum;
import com.yeepay.g3.facade.lmrouter.service.PlatChannelFacade;
import com.yeepay.g3.facade.zt.facade.ZtPolicyOrderFacade;
import com.yeepay.g3.utils.common.UIDGenerator;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.g3.utils.config.ConfigurationUtils;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;
import com.yeepay.g3.utils.web.IpUtils;
import com.yeepay.g3.utils.web.emvc.annotation.Param;

@Controller
public class AssetAction extends BaseAction {

	protected LPQueryFacade lPQueryFacade = RemoteServiceFactory
			.getService(LPQueryFacade.class);
	protected FundBizTranscationFacade fundBizTranscationFacade = RemoteServiceFactory
			.getService(FundBizTranscationFacade.class);
	protected FundQueryFacade fundQueryFacade = RemoteServiceFactory
			.getService(FundQueryFacade.class);
	protected MemberPasswordFacade memberPasswordFacade = RemoteServiceFactory
			.getService(MemberPasswordFacade.class);
	protected MemberManagementFacade memberManagementFacade = RemoteServiceFactory
			.getService(MemberManagementFacade.class);
	protected LMAccountTradeManagementFacde lMAccountTradeManagementFacde = RemoteServiceFactory
			.getService(LMAccountTradeManagementFacde.class);
	protected LanmaoDemandFacade lanmaoDemandFacade = RemoteServiceFactory
			.getService(LanmaoDemandFacade.class);
	protected TrustQueryFacade trustQueryFacade = RemoteServiceFactory
			.getService(TrustQueryFacade.class);
	//线下理财顾问facade
	protected UserFacade userFacade = RemoteServiceFactory
			.getService(UserFacade.class);

	protected FixedTimeAccountFacade fixedTimeAccountFacade = RemoteServiceFactory
			.getService(FixedTimeAccountFacade.class);
	protected PlatChannelFacade platChannelFacade = RemoteServiceFactory
			.getService(PlatChannelFacade.class);
	protected ActivityUserMessageFacade activityUserMessageFacade = RemoteServiceFactory
			.getService(ActivityUserMessageFacade.class);
	
	protected ActivityWXSendMessageFacade activityWXSendMessageFacade = RemoteServiceFactory
			.getService(ActivityWXSendMessageFacade.class);
	protected FundUserShareQueryFacade fundUserShareQueryFacade = RemoteServiceFactory
			.getService(FundUserShareQueryFacade.class);
	protected LMFundInfoQueryServiceFacade lMFundInfoQueryServiceFacade = RemoteServiceFactory.getService(LMFundInfoQueryServiceFacade.class);
	
	private ZtPolicyOrderFacade ztPolicyOrderFacade = RemoteServiceFactory.getService(ZtPolicyOrderFacade.class);
	
//	private ZtPolicyOrderFacade ztPolicyOrderFacade = RemoteServiceFactory.getService("http://localhost:8080/activity-hessian/hessian", RemotingProtocol.HESSIAN,ZtPolicyOrderFacade.class);
	
//	protected ActivityWXSendMessageFacade activityWXSendMessageFacade = RemoteServiceFactory
//			.getService("http://localhost:8080/activity-hessian/hessian", RemotingProtocol.HESSIAN, ActivityWXSendMessageFacade.class);

	@Autowired
	private RequestParamBuilderService requestParamBuilderService;
	
	protected Logger logger = LoggerFactory.getLogger(AssetAction.class);

	/**
	 * 我的懒猫中间页
	 */
	public String toMyLM(@Param("recommendMemberNo")String recommendMemberNo,@Param("bizType")ShareBizTypeEnum bizType,@Param("srcNo")String srcNo,@Param("returnFlag")String returnFlag) {
		HttpSession session = getMvcFacade().getHttpSession();
		HttpServletRequest request = getMvcFacade().getRequest();
		logger.info("[toMyLM] recommendMemberNo={},bizType={},srcNo={}",recommendMemberNo,bizType,srcNo);
		//如果没有推荐人会员编号，则不需要记录推荐人信息
		if(!StringUtils.isEmpty(recommendMemberNo) && !"null".equals(recommendMemberNo)){
			//存储推荐人的会员编号，推荐的业务类型，来源编号
			session.setAttribute("_recommendMemberNo", recommendMemberNo);
			session.setAttribute("_bizType", bizType);
			session.setAttribute("_srcNo", srcNo);
		}
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		HttpServletResponse response = getMvcFacade().getResponse();
		
		if(null == returnFlag) {
			returnFlag = "LM";
		}
		addModelObject("returnFlag", returnFlag);
		try {
			if (null != memberDto) {
//				response.sendRedirect("toAsset");
				/*try {
					RequestDispatcher dispatcher = request.getRequestDispatcher("toAsset");
					dispatcher.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}*/
				return "failed";
			} else { 
				return SUCCESS;
			}
		} catch (Exception e) {
			logger.error("[toMyLM] info={},ERROR={}", "我的懒猫中间页异常",
					e.getMessage());
			return SYSTEM_EXCEPTION;
		}

	}
	
	/**
	 * 获取线下理财顾问的访问链接
	 * @return
	 */
	public String getConsultantUserUrl() {
		String actUrl = "consultantEntrance";
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		String returnUrl = requestParamBuilderService.getConsultantUserUrlParam(actUrl, memberDto.getMemberNo());
		Map<String,Object> resultMap=new HashMap<String,Object>();
		resultMap.put("result", "success");
		resultMap.put("returnUrl", returnUrl);//是否是新手
		setJsonModel(resultMap);
		return "json";
	}

	/**
	 * 我的懒猫
	 * 
	 * @return
	 */
	public String toAsset(@Param("loginName") String loginName,
			@Param("srcNo") String srcNo,@Param("userSessionKey")String userSessionKey) {
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		logger.info("---------- session : " + session);
		logger.info("---------- memberDto : " + memberDto);
		BigDecimal accountBalanceResult;// 懒猫账户余额
		BigDecimal balanceInfoResult;// 生财宝总金额
		BigDecimal xtTotalAmount;// 信托总资产
		BigDecimal totalWealth;// 总资产
		BigDecimal incomeInfoResult;// 生财宝昨日收益
		BigDecimal fixedIncomeResult;// xt昨日收益
		BigDecimal yesterdayIncome;// 昨日收益总和
		BigDecimal sumAmountScb;// scb 累计收益
		BigDecimal SumIncome;// xt总收益
		BigDecimal expectIncome;// 预期收益
		BigDecimal totalIncome;// 总收益
		
		BigDecimal fundTotalAmount = new BigDecimal(0);// 基金的总资产
		BigDecimal fundYestodayResult = new BigDecimal(0);// 基金昨日收益
		BigDecimal fundSumIncome = new BigDecimal(0);// 基金总收益总收益
		
		BigDecimal policyAmount = BigDecimal.ZERO;//灵机一投金额
		try{
			ZtMemberWealthInfoDTO ztMemberWealthInfoDTO = ztPolicyOrderFacade.queryZtWealthInfoByMemberNo(memberDto.getMemberNo());
			if(ztMemberWealthInfoDTO != null){
				policyAmount = ztMemberWealthInfoDTO.getWealth();
			}
		}catch(Exception e){
			logger.error("[myTotalWealth] info={},ERROR={}","查询灵机一投金额失异常",e);
		}
		
		
		try {
			//查询基金的收益资产信息
			UserTotalCountResult userFundResult = fundUserShareQueryFacade
					.queryUserTotalCount(GetParamUtils.getFundPlatNo(),
							memberDto.getMemberNo());
			if (null != userFundResult.getLastIncome()) {
				fundYestodayResult = userFundResult.getLastIncome();
			}
			if (null != userFundResult.getTotalIncome()) {
				fundSumIncome = userFundResult.getTotalIncome();
			}
			if (null != userFundResult.getTotalWorth()) {
				fundTotalAmount = userFundResult.getTotalWorth();
			}
			if(null != userFundResult && null != userFundResult.getApplyingAmount()) {
				fundTotalAmount = fundTotalAmount.add(userFundResult.getApplyingAmount());
			}
		} catch (Exception e) {
			logger.error("查询基金的资产信息有误",e.getMessage());
		}
		try {
			// 查询懒猫账户余额
			AccountInfoQueryResultDto aiqrDto = lanmaoDemandFacade
					.queryAccount(memberDto.getMemberNo(), new Date());
			accountBalanceResult = aiqrDto == null
					|| aiqrDto.getAmount() == null ? new BigDecimal(0)
					: aiqrDto.getAmount();
			if (!"0".equals(aiqrDto.getCode())) {// 异常
				logger.error("[toAsset] info={},ERROR={}", "查询懒猫账户余额接口异常",
						aiqrDto.getDescription());
			}
			// 查询生财宝总金额
			BalanceInfoResultDTO balanceInfoResultDTO = fundBizTranscationFacade
					.queryBalanceInfo(GetParamUtils.getScbPlatNo(),
							memberDto.getMemberNo());
			balanceInfoResult = balanceInfoResultDTO == null
					|| balanceInfoResultDTO.getBalance() == null ? new BigDecimal(
					0) : balanceInfoResultDTO.getBalance();
			// 查询scb累计收益
			BigDecimal sumAmount = fundQueryFacade.sumAmount(
					GetParamUtils.getScbPlatNo(), memberDto.getMemberNo());
			sumAmountScb = sumAmount == null ? new BigDecimal(0) : sumAmount;
			// 查询用户收益及资金总额
			FixedTimeAccountResultDto ftardto = fixedTimeAccountFacade
					.queryFixedTimeAccount(memberDto.getMemberNo());
			if (!"0".equals(ftardto.getCode())) {// 异常
				logger.error("[toAsset] info={},ERROR={}", "查询用户收益及资金总额接口异常",
						ftardto.getDescription());
			}
			// 默认为0
			xtTotalAmount = ftardto == null || ftardto.getTotalAmount() == null ? new BigDecimal(
					0) : ftardto.getTotalAmount();
			xtTotalAmount = xtTotalAmount.add(ftardto == null
					|| ftardto.getExpectIncome() == null ? new BigDecimal(0)
					: ftardto.getExpectIncome());
			;// 信托理财资金总额
				// 查询总收益
			SumIncome = ftardto == null ? new BigDecimal(0) : ftardto
					.getSumIncome();
			// 预期收益
			expectIncome = ftardto == null ? new BigDecimal(0) : ftardto
					.getExpectIncome();
			totalIncome = sumAmountScb.add(SumIncome).add(expectIncome).add(fundSumIncome);
			totalWealth = xtTotalAmount.add(balanceInfoResult).add(
					accountBalanceResult).add(fundTotalAmount).add(policyAmount);

			// 获取生财宝昨日收益
			IncomeInfoResultDTO incomeInfoResultDTO = fundBizTranscationFacade
					.queryIncome(GetParamUtils.getScbPlatNo(),
							memberDto.getMemberNo(), SOURCE_SYSTEM);
			incomeInfoResult = incomeInfoResultDTO == null
					|| incomeInfoResultDTO.getLatestIncome() == null ? new BigDecimal(
					0) : incomeInfoResultDTO.getLatestIncome();
			// // 获取信托昨日收益
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DATE, 1);
			BigDecimal income = fixedTimeAccountFacade
					.queryFixedTimeAccountHistorySumIncome(
							memberDto.getMemberNo(), calendar.getTime());
			fixedIncomeResult = income == null ? new BigDecimal(0) : income;
			yesterdayIncome = incomeInfoResult.add(fixedIncomeResult).add(fundYestodayResult);
			
		} catch (Exception e) {
			logger.error("[toAsset] info={},ERROR={}", "跳转我的懒猫异常",
					e.getMessage());
			return SYSTEM_EXCEPTION;
		}
		if(StringUtils.isEmpty(loginName)&&StringUtils.isEmpty(srcNo)&&StringUtils.isEmpty(userSessionKey)){
			addModelObject("platform", "WX");
			try {
				// 查询用户身份（线下理财顾问使用）
				TblLmUser userBasic = userFacade.getUserBasic(memberDto
						.getMemberNo());
				// -1 userBasic null 成为推荐人
				// 0 - 未知 - 成为推荐人
				// 1 - 普通用户 - 成为推荐人
				// 2 - 推荐人 - 我的推荐
				// 3 - 顾问 - 我的客户
				// 4 - 销售 - 我的顾问
				if (userBasic != null) {
					Short userType = userBasic.getUserType();
					addModelObject("consultantUserDesc",
							userTypeDesc.get(userType));
				} else {
					addModelObject("consultantUserDesc", userTypeDesc.get(null));
				}
			} catch (Exception e) {
				logger.error("[toAsset] info={},ERROR={}", "查询线下理财顾问接口异常",
						e.getMessage());
			}
			
		}else{
			//判断是否绑卡
			try {
				//app我的懒猫去充值，提现走绑卡
				BankCardInfoDto bankCardInfoDto = lPQueryFacade.obtainDefaultBankCardInfo(memberDto.getMemberNo());
				if(bankCardInfoDto!=null){
					addModelObject("isBankCard", "YES");
				}else{
					addModelObject("isBankCard", "NO");
				}
			} catch (Exception e) {
				logger.error("[toAsset] info={},ERROR={}", "查询用户是否绑卡时异常",
						e.getMessage());			
				}
			addModelObject("platform", "APP");
		}
		//消息公告
		try {
			//查询未读消息数量
			Integer messageCount = activityUserMessageFacade.queryMessageCountByMemberNo(memberDto.getMemberNo());
			if(messageCount==null||messageCount==0){
				addModelObject("messageFlag", false);
			}else{
				addModelObject("messageFlag", true);
			}
		} catch (Exception e) {
			logger.error("[toAsset] info={},ERROR={}", "查询未读消息数量接口异常",
					e.getMessage());
		}
		//app我的懒猫去充值，提现走绑卡
//		String platform=(String)session.getAttribute("platform");
//		if("APP".equals(platform)){
//			//判断是否绑卡
//			BankCardInfoDto bankCardInfoDto = lPQueryFacade.obtainDefaultBankCardInfo(memberDto.getMemberNo());
//			if(bankCardInfoDto!=null){
//				addModelObject("isBankCard", "YES");
//			}else{
//				addModelObject("isBankCard", "NO");
//			}
//			//APP用户，不显示我的推荐，朋友推荐，各种中心div
//			addModelObject("platform", "APP");
//		}else{
//			//APP用户，不显示我的推荐，朋友推荐，各种中心div
//			addModelObject("platform", "WX");
//		}
		addModelObject("accountBalanceResult", accountBalanceResult);// 懒猫账户余额
		addModelObject("totalWealth", totalWealth);// 总资产
		addModelObject("yesterdayIncome", yesterdayIncome);// 昨日收益
		addModelObject("totalIncome", totalIncome);// 总收益
		return SUCCESS;
	}

	/**
	 * 累计收益
	 */
	public String toTotalIncome() {
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		BigDecimal sumAmountScb;// scb 累计收益
		BigDecimal SumIncome;// xt已到账总收益
		BigDecimal expectIncome;// 预期收益
		BigDecimal xtTotalIncome;// 信托理财累计收益
		BigDecimal totalIncome;// 总收益
		
		BigDecimal fundSumIncome = new BigDecimal(0);// 基金累计收益
		//查询基金的收益资产信息
		try {
			UserTotalCountResult userFundResult = fundUserShareQueryFacade
					.queryUserTotalCount(GetParamUtils.getFundPlatNo(),
							memberDto.getMemberNo());
		    if(null != userFundResult) {
		    	fundSumIncome = userFundResult.getTotalIncome();
		    }
		} catch (Exception e) {
			logger.error("查询基金的资产信息有误",e.getMessage());
		}
		
		try {
			// 查询scb累计收益
			BigDecimal sumAmount = fundQueryFacade.sumAmount(
					GetParamUtils.getScbPlatNo(), memberDto.getMemberNo());
			sumAmountScb = sumAmount == null ? new BigDecimal(0) : sumAmount;
			// 查询用户收益及资金总额
			FixedTimeAccountResultDto ftardto = fixedTimeAccountFacade
					.queryFixedTimeAccount(memberDto.getMemberNo());
			if (!"0".equals(ftardto.getCode())) {// 异常
				logger.error("[toTotalIncome] info={},ERROR={}",
						"查询用户收益及资金总额接口异常", ftardto.getDescription());
			}
			// 查询总收益
			SumIncome = ftardto == null ? new BigDecimal(0) : ftardto
					.getSumIncome();
			// 预期收益
			expectIncome = ftardto == null ? new BigDecimal(0) : ftardto
					.getExpectIncome();
			xtTotalIncome = SumIncome.add(expectIncome);
			totalIncome = sumAmountScb.add(SumIncome).add(expectIncome).add(fundSumIncome);
			addModelObject("totalIncome", totalIncome);
			addModelObject("sumAmountScb", sumAmountScb);
			addModelObject("xtTotalIncome", xtTotalIncome);
			addModelObject("fundSumIncome", fundSumIncome);
			return "totalIncome";
		} catch (Exception e) {
			logger.error("[toTotalIncome] info={},ERROR={}", "累计收益",
					e.getMessage());
			return SYSTEM_EXCEPTION;
		}

	}

	/**
	 * 昨日最新收益
	 */
	public String toTodayIncome() {
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		BigDecimal fundYestodayResult = new BigDecimal(0);// 基金昨日收益
		//查询基金的收益资产信息
		try {
			UserTotalCountResult userFundResult = fundUserShareQueryFacade
					.queryUserTotalCount(GetParamUtils.getFundPlatNo(),
							memberDto.getMemberNo());
		    if(null != userFundResult) {
		    	fundYestodayResult = userFundResult.getLastIncome();
		    }
		} catch (Exception e) {
			logger.error("查询基金的资产信息有误",e.getMessage());
		}
		try {
			BigDecimal incomeScb;
			BigDecimal incomeFixed;
			BigDecimal incomeTotal;
			// 获取生财宝昨日收益
			IncomeInfoResultDTO incomeInfoResultDTO = fundBizTranscationFacade
					.queryIncome(GetParamUtils.getScbPlatNo(),
							memberDto.getMemberNo(), SOURCE_SYSTEM);
			incomeScb = incomeInfoResultDTO == null
					|| incomeInfoResultDTO.getLatestIncome() == null ? new BigDecimal(
					0) : incomeInfoResultDTO.getLatestIncome();

			// 获取信托昨日收益
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DATE, 1);
			BigDecimal income = fixedTimeAccountFacade
					.queryFixedTimeAccountHistorySumIncome(
							memberDto.getMemberNo(), calendar.getTime());
			incomeFixed = income == null ? new BigDecimal(0) : income;
			incomeTotal = incomeScb.add(incomeFixed).add(fundYestodayResult);
			addModelObject("incomeFixed", incomeFixed);
			addModelObject("incomeScb", incomeScb);
			addModelObject("fundYestodayResult", fundYestodayResult);
			addModelObject("incomeTotal", incomeTotal);
		} catch (Exception e) {
			logger.error("[toTodayIncome] info={},ERROR={}", "昨日最新收益",
					e.getMessage());
			return SYSTEM_EXCEPTION;
		}
		return "goIncome";
	}

	/**
	 * 我的懒猫账号余额
	 */
	public String toMyAcount() {
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		try {
			AccountInfoQueryResultDto aiqrDto = lanmaoDemandFacade
					.queryAccount(memberDto.getMemberNo(), new Date());
			if (!"0".equals(aiqrDto.getCode())) {// 异常，跳转到异常页面
				logger.error("[toMyAcount] info={},ERROR={}", "我的懒猫账号余额接口异常",
						aiqrDto.getDescription());
			}
			addModelObject("amount", aiqrDto == null
					|| aiqrDto.getAmount() == null ? new BigDecimal(0)
					: aiqrDto.getAmount());
		} catch (Exception e) {
			logger.error("[toMyAcount] info={},ERROR={}", "跳转我的懒猫账户余额异常",
					e.getMessage());
			return SYSTEM_EXCEPTION;
		}
		//1.查询生财宝产品七日年化率
		String lastestRateForSevenDay = null;
		try {
			List<FundbizRateInfo> fundbizRateInfoList = fundQueryFacade
					.queryRateInfos(1);
			if (fundbizRateInfoList != null && fundbizRateInfoList.size() != 0) {
				FundbizRateInfo fundbizRateInfo = fundbizRateInfoList.get(0);
				lastestRateForSevenDay = fundbizRateInfo.getRate() == null ? "0"
						: fundbizRateInfo.getRate().substring(0,5);
			}
		} catch (Exception e) {
			logger.error("[toMyAcount] info={},ERROR={}", "懒猫账户余额查询生财宝年化收益率时异常",
					e.getMessage());
			lastestRateForSevenDay="0";
		}
		addModelObject("lastestRateForSevenDay",lastestRateForSevenDay);
		//3.获取基金链接以及最高年涨幅
		try {
			String ascMemberNo = null ;
			if(null != memberDto) {
				ascMemberNo =  SecretUtils.secretData(memberDto.getMemberNo());
			}
			//获取基金的url
			String fundSalesUrl = GetParamUtils.getfundSalesUrl();
			// 格式化
			addModelObject("fundSalesUrl", fundSalesUrl);// 基金的域名地址
			addModelObject("ascMemberNo", ascMemberNo);// 加密的用户编码
			
			FundQueryParamDTO fundQueryParamDTO = new FundQueryParamDTO();
			fundQueryParamDTO.setMerNo(GetParamUtils.getFundPlatNo());
			if (StringUtils.isEmpty(fundQueryParamDTO.getHeat())) {
				fundQueryParamDTO.setHeat(null);
			}
			fundQueryParamDTO.setFundType(FundTypeEnum.ALL);
			// 默认设为年涨幅
			if (null == fundQueryParamDTO.getListFundEnum()) {
				fundQueryParamDTO.setListFundEnum(ListFundEnum.YEAR_INCREASE);
			}
			FundInfoQueryResultPageDTO page = lMFundInfoQueryServiceFacade
					.queryFundList(fundQueryParamDTO);
			if(null != page&& page.getFundBasicInfoDto()!=null&&page.getFundBasicInfoDto().size()>0){
				addModelObject("indexShowRate", page.getFundBasicInfoDto().get(0).getYearIncrease().setScale(2, BigDecimal.ROUND_HALF_UP));
			}
		} catch (Exception e) {
			logger.info("基金的最高年涨幅查询有误：",e);
			//5.基金收益的统一配置
			Map<String, Object> map = (Map<String, Object>) ConfigurationUtils
					.getConfigParam("config_type_text_resources",
							"LM_fundRate_indexShow").getValue();
			if(null != map && map.get("indexShowRate") != null) {
				addModelObject("indexShowRate", map.get("indexShowRate").toString());
			}
		}
		
		//app我的懒猫去充值，提现走绑卡
		String platform=(String)session.getAttribute("platform");
		if("APP".equals(platform)){
			//判断是否绑卡
			BankCardInfoDto bankCardInfoDto = lPQueryFacade.obtainDefaultBankCardInfo(memberDto.getMemberNo());
			if(bankCardInfoDto!=null){
				addModelObject("isBankCard", "YES");
			}else{
				addModelObject("isBankCard", "NO");
			}
			//APP用户，不显示我的推荐，朋友推荐，各种中心div
			addModelObject("platform", "APP");
		}else{
			//APP用户，不显示我的推荐，朋友推荐，各种中心div
			addModelObject("platform", "WX");
		}
		return "myAcount";
	}

	/**
	 * 去充值
	 * 
	 * @author cdt
	 * @throws UnsupportedEncodingException
	 * @date 2016-3-21
	 * @time 下午2:57:33
	 */
	public String toRecharge(@Param("amount") BigDecimal amount,
			@Param("ret") String ret, @Param("productId") Long productId,
			@Param("expectIncome") BigDecimal expectIncome,
			@Param("promoNo") String promoNo,
			@Param("promoType") String promoType,
			@Param("addRate") String addRate,
			@Param("expectType") String expectType,
			@Param("orderNum") String orderNum)
			throws UnsupportedEncodingException {
		HttpSession session = getMvcFacade().getHttpSession();
		HttpServletResponse response = getMvcFacade().getResponse();
		HttpServletRequest request = getMvcFacade().getRequest();
		//获取token序列,存储于session中
		String token = LmConstants.makeToken();
		session.setAttribute("rechargeToken", token);
		//存于页面
		addModelObject("token", token);
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		BankCardInfoDto bankCardInfoDto = null;
		try {
			bankCardInfoDto = lPQueryFacade.obtainDefaultBankCardInfo(memberDto
					.getMemberNo());
			if (bankCardInfoDto != null) {
				if ("fixed".equals(ret)) {// 信托理财多的属性
					ProductDetailForWXResultDto pdfwxrDto = trustQueryFacade
							.queryProductDetailForWX(productId);
					addModelObject("EXPECT_INCOME", expectIncome);
					addModelObject("PRO_NAME", pdfwxrDto.getProductName());
					addModelObject("PRO_NO", productId);
					addModelObject("PROMO_NO", promoNo);
					addModelObject("promoType", promoType);
					addModelObject("addRate", addRate);
					//投资换产品的类型和订单号
					addModelObject("expectType", expectType);
					addModelObject("orderNum", orderNum);
				}
				addModelObject("RET", ret);
				addModelObject("AMOUNT", amount);
				AccountInfoQueryResultDto aiqrDto = lanmaoDemandFacade
						.queryAccount(memberDto.getMemberNo(), new Date());
				if (!"0".equals(aiqrDto.getCode())) {// 异常
					logger.error("[toRecharge] info={},ERROR={}", "查询懒猫账号接口异常",
							aiqrDto.getDescription());
					return SYSTEM_EXCEPTION;
				}
				BigDecimal accountBalanceResult = aiqrDto.getAmount();
				BigDecimal money = null == amount ? new BigDecimal(0) : amount;
				addModelObject("BUY_AMOUNT", accountBalanceResult.add(money));
				addModelObject("CARD_NO",
						StringProcessorUtils.getFourCardNo(bankCardInfoDto
								.getCardNo()));
				addModelObject("BANK_ID", bankCardInfoDto.getBankId());
				CardInfoResultDto cirDto = lanmaoDemandFacade
						.cardBin(bankCardInfoDto.getCardNo());// 根据银行卡号查询银行类型
				if (!"0".equals(cirDto.getCode())) {
					logger.error("[toRecharge] info={},ERROR={}",
							"根据银行卡号查询银行类型接口异常", cirDto.getDescription());
					return SYSTEM_EXCEPTION;
				}
				addModelObject("BANK_LOGO_NAME", cirDto.getBankName());
				PlatChannelResultDto result=platChannelFacade.queryPlatChannelByBankNo(bankCardInfoDto
						.getBankId(), RouteTypeEnum.NO_CARD);
				if(!"0".equals(result.getCode())){
					logger.error("[toRecharge] info={},ERROR={}",
							"根据银行编号查询限额接口异常或不支持无卡", result.getDescription());
					//TODO 由于接口问题，先暂时认为cod非0就是不支持无卡
					result.setStatus(PlatChannelStatusEnum.OFF);
//					return SYSTEM_EXCEPTION;
				}
				addModelObject("RESULT",result);
				//去充值页面生成uuid，传到充值action
				String uuid = UIDGenerator.getUUID();
				addModelObject("uuid", uuid);
				logger.info("[toRecharge] uuid={}",uuid);
				return "recharge";
			} else {
				// addModelObject("showTip", "1");// 显示温馨提示
				// 封装url
				ReturnUrlParamEntity returnUrlParamEntity = new ReturnUrlParamEntity();
				returnUrlParamEntity.setAmount(amount);
				returnUrlParamEntity.setExpectIncome(expectIncome);
				returnUrlParamEntity.setProductId(productId);
				returnUrlParamEntity.setPromoNo(promoNo);
				returnUrlParamEntity.setRet(ret);
				returnUrlParamEntity.setReturnFlag("toRecharge");
				returnUrlParamEntity.setExpectType(expectType);
				returnUrlParamEntity.setOrderNum(orderNum);
				urlMap.put(memberDto.getMemberNo(), returnUrlParamEntity);
				String platform =(String) session.getAttribute("platform");
				if("APP".equals(platform)){//app不需要中间页
//					response.sendRedirect("../account/card/toBindCard");
//					RequestDispatcher dispatcher = request.getRequestDispatcher("http://172.19.60.87:8081/lmweChat/account/card/toBindCard");
//					dispatcher.forward(request, response);
					return "addCardApp";
				}else{
//					response.sendRedirect("../account/card/toGOBindCard");
					try {
						RequestDispatcher dispatcher = request.getRequestDispatcher("../account/card/toGOBindCard");
						dispatcher.forward(request, response);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return "addCard";
				}
				
			}
		} catch (Exception e) {
			logger.error("[toRecharge] info={},ERROR={}", "去充值异常",
					e.getMessage());
			return SYSTEM_EXCEPTION;
		}

	}

	/**
	 * 去提现
	 */
	public String toCashIn() {
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		//获取token序列,存储于session中
		String token = LmConstants.makeToken();
		session.setAttribute("cashInToken", token);
		//存于页面
		addModelObject("token", token);
		BankCardInfoDto bankCardInfoDto = null;
		try {
			bankCardInfoDto = lPQueryFacade.obtainDefaultBankCardInfo(memberDto
					.getMemberNo());
			BigDecimal availableCashIn = null;
			if (bankCardInfoDto != null) {
				AccountInfoQueryResultDto dto = lPQueryFacade.queryAccount(
						memberDto.getMemberNo(), new Date());
				availableCashIn = dto.getWithdrawableAmount();
				BigDecimal amount=dto.getAmount();
				addModelObject("CARD_NO",
						StringProcessorUtils.getFourCardNo(bankCardInfoDto
								.getCardNo()));
				CardInfoResultDto cirDto = lanmaoDemandFacade
						.cardBin(bankCardInfoDto.getCardNo());// 根据银行卡号查询银行类型
				if (!"0".equals(cirDto.getCode())) {
					logger.error("[toCashIn] info={},ERROR={}",
							"根据银行卡号查询银行类型接口异常", cirDto.getDescription());
					return SYSTEM_EXCEPTION;
				}
				addModelObject("BANK_LOGO_NAME", cirDto.getBankName());
				addModelObject("BANK_ID", cirDto.getBankId());
				addModelObject("availableCashIn", amount);
				
				return "cashIn";
			} else {
				// addModelObject("showTip", "1");
				ReturnUrlParamEntity returnUrlParamEntity = new ReturnUrlParamEntity();
				returnUrlParamEntity.setReturnFlag("toCashIn");
				urlMap.put(memberDto.getMemberNo(), returnUrlParamEntity);
				logger.info("[register] returnUrlParamEntity={}",
						returnUrlParamEntity);
				String platform =(String) session.getAttribute("platform");
				if("APP".equals(platform)){//app不需要中间页
					return "addCardApp";
				}else{
					return "addCard";	
				}
				
			}
		} catch (Exception e) {
			logger.error("[toCashIn] info={}, ERROR={}", "去提现异常",
					e.getMessage());
			return SYSTEM_EXCEPTION;
		}

	}

	/**
	 * 去账号明细
	 */
	public String toBillInfo() {
		return "billInfo";
	}

	/**
	 * 去昨日收益列表页面
	 * 
	 * @throws ParseException
	 */
	public String toMyIncomeForYesterday(@Param("type") String type) {
		BigDecimal incomeInfoResult;// 生财宝最新收益
		BigDecimal fixedIncomeResult;// xt昨日收益
		BigDecimal yesterdayIncome;
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		BigDecimal fundYestodayResult = new BigDecimal(0);// 基金昨日收益
		
		try {
			//查询基金的收益资产信息
			UserTotalCountResult userFundResult = fundUserShareQueryFacade
					.queryUserTotalCount(GetParamUtils.getFundPlatNo(),
							memberDto.getMemberNo());
			if (null != userFundResult.getLastIncome()) {
				fundYestodayResult = userFundResult.getLastIncome();
			}
		} catch (Exception e) {
			logger.error("查询基金的资产信息有误",e.getMessage());
		}
		// 获取生财宝昨日收益
		IncomeInfoResultDTO incomeInfoResultDTO = fundBizTranscationFacade
				.queryIncome(GetParamUtils.getScbPlatNo(),
						memberDto.getMemberNo(), SOURCE_SYSTEM);
		incomeInfoResult = incomeInfoResultDTO == null
				|| incomeInfoResultDTO.getLatestIncome() == null ? new BigDecimal(
				0) : incomeInfoResultDTO.getLatestIncome();
		// 获取信托昨日收益

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 1);
		BigDecimal income = fixedTimeAccountFacade
				.queryFixedTimeAccountHistorySumIncome(memberDto.getMemberNo(),
						calendar.getTime());
		fixedIncomeResult = income == null ? new BigDecimal(0) : income;
		yesterdayIncome = incomeInfoResult.add(fixedIncomeResult).add(fundYestodayResult);
		addModelObject("yesterdayIncome", yesterdayIncome);
		addModelObject("incomeInfoResult", incomeInfoResult);
		addModelObject("fixedIncomeResult", fixedIncomeResult);
		addModelObject("fundYestodayResult", fundYestodayResult);
		addModelObject("type", type);
		return SUCCESS;
	}

	/**
	 * 去累计收益列表页面
	 */
	public String toMyIncome(@Param("type") String type) {
		BigDecimal sumAmountScb;// 生财宝累计收益
		BigDecimal sumIncomeTrust;// xt累计收益
		BigDecimal sumIncome;
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		logger.info("[toMyIncome] info={}",memberDto.getLoginName()+"去累计收益页面。。。。。type="+type);
		BigDecimal fundSumIncome = new BigDecimal(0);// 基金总收益总收益
		
		try {
			//查询基金的收益资产信息
			UserTotalCountResult userFundResult = fundUserShareQueryFacade
					.queryUserTotalCount(GetParamUtils.getFundPlatNo(),
							memberDto.getMemberNo());
			if (null != userFundResult && null != userFundResult.getTotalIncome()) {
				fundSumIncome = userFundResult.getTotalIncome();
			}
		} catch (Exception e) {
			logger.error("查询基金的资产信息有误",e.getMessage());
		}
		// 获取生财宝昨日收益
		BigDecimal sumAmount = fundQueryFacade.sumAmount(
				GetParamUtils.getScbPlatNo(), memberDto.getMemberNo());
		sumAmountScb = sumAmount == null ? new BigDecimal(0) : sumAmount;
		// 查询xt总收益
		FixedTimeAccountResultDto ftardto = fixedTimeAccountFacade
				.queryFixedTimeAccount(memberDto.getMemberNo());
		if (!"0".equals(ftardto.getCode())) {// 异常//
			logger.error("[toMyIncome] info={},ERROR={}", "查询用户收益及资金总额接口异常",
					ftardto.getDescription());
		}
		sumIncomeTrust = ftardto == null ? new BigDecimal(0) : ftardto
				.getSumIncome().add(ftardto.getExpectIncome());
		sumIncome = sumAmountScb.add(sumIncomeTrust).add(fundSumIncome);
		addModelObject("sumIncome", sumIncome);
		addModelObject("sumIncomeTrust", sumIncomeTrust);
		addModelObject("sumAmountScb", sumAmountScb);
		addModelObject("fundSumIncome", fundSumIncome);
		addModelObject("type", type);
		return SUCCESS;
	}

	/**
	 * 查询账户明细
	 */
	public String billInfo(@Param("startDate") Date startDate,
			@Param("endDate") Date endDate, @Param("index") Integer index) {
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		try {
			QueryOrderParamDto paramsDto = new QueryOrderParamDto();
			paramsDto.setMemberNo(memberDto.getMemberNo());
			if (endDate != null) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(endDate);
				calendar.add(Calendar.DATE, 1);// 加一天
				endDate = calendar.getTime();
			}
			paramsDto.setStartDate(startDate);
			paramsDto.setEndDate(endDate);
			paramsDto.setPageIndex(index);
			paramsDto.setPageSize(PAGE_SIZE);
			paramsDto.setStatus(OrderStatusEnum.SUCCESS);
			QueryOrderResultDto qorDto = lanmaoDemandFacade
					.queryOrder(paramsDto);
			setJsonModel(qorDto);
		} catch (Exception e) {
			logger.error("[billInfo] info={},ERROR={}", "查询账户明细异常",
					e.getMessage());
			setJsonModel(SYSTEM_EXCEPTION_JSON);
		}
		return "json";
	}

	/**
	 * 充值失败后，重新获取uuid
	 * @param in -
	 * @return 
	 */
	public String toGetUUID(){
		//获取uuid
		String uuid = UIDGenerator.getUUID();
		logger.info("[toGetUUID] uuid={}",uuid);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("uuid", uuid);
		setJsonModel(resultMap);
		return "json";
	}
	
	/**
	 * 充值
	 */
	public String recharge(@Param("amount") String amount,
			@Param("tradePwd") String tradePwd,
			@Param("uuid") String uuid,
			@Param("token") String token) {
		logger.info("[recharge] amount={},uuid={}",amount,uuid);
		HttpServletRequest request=getMvcFacade().getRequest();
		HttpSession session = getMvcFacade().getHttpSession();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//验证提交表单 
		String sessionToken = (String) session.getAttribute("rechargeToken");
		if(!StringUtils.isEmpty(token) && token.equals(sessionToken)){
			session.removeAttribute("rechargeToken");
			try {
				MemberDto memberDto = (MemberDto) session.getAttribute("member");
				if (StringUtils.isEmpty(amount) || StringUtils.isEmpty(tradePwd)) {
					resultMap.put("result", "failed");
					resultMap.put("description", "交易密码不允许为空");
					//更新token
					String newToken = LmConstants.makeToken();
					session.setAttribute("rechargeToken", newToken);
					//传到页面
					resultMap.put("token", newToken);
					setJsonModel(resultMap);
					return "json";
				}
				MemberPwdConstraintDto memberPwdConstraintDto = memberPasswordFacade
						.checkTradePassword(memberDto.getMemberNo(), tradePwd);
				if (!memberPwdConstraintDto.getResultFlag()) {
					resultMap.put("result", WRONG_PWD);
					resultMap.put("description", memberPwdConstraintDto);// 返回错误剩余次数
					//更新token
					String newToken = LmConstants.makeToken();
					session.setAttribute("rechargeToken", newToken);
					//传到页面
					resultMap.put("token", newToken);
					setJsonModel(resultMap);
					return "json";
				}
				BankCardInfoDto bankCardInfoDto = lPQueryFacade
						.obtainDefaultBankCardInfo(memberDto.getMemberNo());
				BigDecimal amountBD = new BigDecimal(amount);
				amountBD = amountBD.setScale(2, BigDecimal.ROUND_HALF_UP);
				QuickPayParamDto params = new QuickPayParamDto();
				params.setPlatformNo(LmConstants.readPlatformNo());
//				params.setRequestNo(UIDGenerator.getUUID());
				params.setRequestNo(uuid);
				logger.info("recharge requestNo={},memberNo={}", params.getRequestNo(), memberDto.getMemberNo());
				params.setMemberNo(memberDto.getMemberNo());
				params.setCardSeq(bankCardInfoDto.getCardSeq());
				params.setAmount(amountBD);
				params.setRequestTime(new Date());
				params.setDevice(DeviceEnum.WX);
//				params.setIp(IpUtils.getIpAddr(request));
				String macAddress = null;
				String userAgent = null;
				try{
					macAddress = HttpRequestUtils.getMacAddress(IpUtils.getIpAddr(request));
					userAgent = request.getHeader("User-Agent");
				}catch(Exception e){
					logger.error("[recharge] info={},error={}","获取mac地址时异常",e);
				}
				Object obj = JSONObjectUtils.userAddrToJSONStr(IpUtils.getIpAddr(request),macAddress, userAgent, null);
				if(obj != null){
					params.setIp(obj.toString());
				}
				logger.info("[recharge] params={}",params);
				QuickPayResultDto payResult = lMAccountTradeManagementFacde
						.quickPayment(params);
				logger.info("[recharge] payResult={}",payResult);
				if (QuickPayStatusEnum.FAIL.equals(payResult.getStatus())) {
					String des[] = GetParamUtils.readCommonMessage(
							GetParamUtils.PEBI900008).split(
							GetParamUtils.COMMON_MESSAGE_SPLIT);
					if (des[0].equals(payResult.getCode())) {
						resultMap.put("result", "payment_limit");// 支付超限
						//更新token
						String newToken = LmConstants.makeToken();
						session.setAttribute("rechargeToken", newToken);
						//传到页面
						resultMap.put("token", newToken);
						setJsonModel(resultMap);
						return "json";
					}
					resultMap.put("result", "failed");
					resultMap.put("description", payResult.getDescription());
					//更新token
					String newToken = LmConstants.makeToken();
					session.setAttribute("rechargeToken", newToken);
					//传到页面
					resultMap.put("token", newToken);
					setJsonModel(resultMap);
					return "json";
				}
				resultMap.put("result", "processing");
				resultMap.put("paymentFlowNo", payResult.getPaymentFlowNo());
				setJsonModel(resultMap);
				return "json";
			} catch (Exception e) {
				logger.error("[recharge] info={},ERROR={}", "充值异常", e.getMessage());
				resultMap.put("result", "system_exception");
				resultMap.put("description", "系统异常");
				//更新token
				String newToken = LmConstants.makeToken();
				session.setAttribute("rechargeToken", newToken);
				//传到页面
				resultMap.put("token", newToken);
				setJsonModel(resultMap);
				return "json";
			}
		}
		
		return "json";
	}

	/**
	 * 提现
	 */
	public String cashIn(@Param("amount") String amount,
			@Param("tradePwd") String tradePwd,
			@Param("token") String token) {
		HttpServletRequest request=getMvcFacade().getRequest();
		HttpSession session = getMvcFacade().getHttpSession();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String sessionToken = (String) session.getAttribute("cashInToken");
		if(!StringUtils.isEmpty(token) && token.equals(sessionToken)){
			session.removeAttribute("cashInToken");//清空session中的标志
			try {
				MemberDto memberDto = (MemberDto) session.getAttribute("member");
				MemberPwdConstraintDto memberPwdConstraintDto = memberPasswordFacade
						.checkTradePassword(memberDto.getMemberNo(), tradePwd);
				
				if (!memberPwdConstraintDto.getResultFlag()) {
					resultMap.put("result", "WRONG_PWD");
					resultMap.put("description", memberPwdConstraintDto);// 返回错误剩余次数
					//更新token
					String newToken = LmConstants.makeToken();
					session.setAttribute("cashInToken", newToken);
					//传到页面
					resultMap.put("token", newToken);
					setJsonModel(resultMap);
					return "json";
				}
				if (StringUtils.isEmpty(amount)) {
					resultMap.put("result", "failed");
					resultMap.put("description", "提现金额不允许为空");
					//更新token
					String newToken = LmConstants.makeToken();
					session.setAttribute("cashInToken", newToken);
					//传到页面
					resultMap.put("token", newToken);
					setJsonModel(resultMap);
					return "json";
				}
				BankCardInfoDto bankCardInfoDto = lPQueryFacade
						.obtainDefaultBankCardInfo(memberDto.getMemberNo());
				BigDecimal amountBD = new BigDecimal(amount);
				amountBD = amountBD.setScale(2, BigDecimal.ROUND_HALF_UP);
				WithdrawParamDto param = new WithdrawParamDto();
				param.setAmount(amountBD);
				param.setCardSeq(bankCardInfoDto.getCardSeq());
				param.setMemberNo(memberDto.getMemberNo());
				param.setPlatformNo(LmConstants.readPlatformNo());
				param.setRequestNo(UIDGenerator.getUUID());
				param.setRequestTime(new Date());
				param.setDevice(DeviceEnum.WX);
				String macAddress = null;
				String userAgent = null;
				try{
					macAddress = HttpRequestUtils.getMacAddress(IpUtils.getIpAddr(request));
					userAgent = request.getHeader("User-Agent");
				}catch(Exception e){
					logger.error("[cashIn] info={},error={}","获取mac地址时异常",e);
				}
				Object obj = JSONObjectUtils.userAddrToJSONStr(IpUtils.getIpAddr(request),macAddress, userAgent, null);
				if(obj != null){
					param.setIp(obj.toString());
				}
				
				WithdrawResultDto wiResult = lMAccountTradeManagementFacde
						.withdraw(param);
				if (WithdrawStatusEnum.FAIL.equals(wiResult.getStatus())) {
					logger.info("[cashIn] wiResult={}", wiResult);
					resultMap.put("result", "failed");
					resultMap.put("description", wiResult.getDescription());
					//更新token
					String newToken = LmConstants.makeToken();
					session.setAttribute("cashInToken", newToken);
					//传到页面
					resultMap.put("token", newToken);
					setJsonModel(resultMap);
					return "json";
				}
				// 提现成功推送微信消息
				try {
					String openId = (String) session.getAttribute("openId");
					logger.info("[cashIn] openId={}",openId);
					if(!StringUtils.isEmpty(openId)){
						Map<String,String> modelWx = LmConstants.getCashInSuccessWxMessageModel();
						ActivityWXSendMessageDTO dataDto = new ActivityWXSendMessageDTO();
						dataDto.setFirst(modelWx.get("first"));
						dataDto.setRemark(modelWx.get("remark"));
						dataDto.setOpenId(openId);
						dataDto.setUrl(modelWx.get("url"));
						dataDto.setKeyword1(StringProcessorUtils.desensitizedCardNo(bankCardInfoDto.getCardNo()) );
						dataDto.setKeyword2(amount+" 元");
						dataDto.setKeyword3(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						activityWXSendMessageFacade.sendWxMessage(ActivityWXSendMessageEnum.CASHIN_SUCCESS, dataDto);
					}
				} catch (Exception e) {
					logger.error("[cashIn] info={},ERROR={}", "提现成功推送微信消息异常",
							e.getMessage());
				}
			} catch (Exception e) {
				logger.error("[cashIn] info={},ERROR={}", "提现异常", e.getMessage());
				resultMap.put("result", "SYSTEM_EXCEPTION");
				//更新token
				String newToken = LmConstants.makeToken();
				session.setAttribute("cashInToken", newToken);
				//传到页面
				resultMap.put("token", newToken);
				setJsonModel(resultMap);
				return "json";
			}
			resultMap.put("result", "SUCCESS");
			setJsonModel(resultMap);
			return "json";
		}
		return "json";
	}

	/**
	 * 查询账号金额
	 */
	public String accountBalanceResult() {
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		AccountInfoQueryResultDto aiqrDto = lanmaoDemandFacade.queryAccount(
				memberDto.getMemberNo(), new Date());
		setJsonModel(aiqrDto.getAmount());
		return "json";
	}

	/**
	 * 我的总资产
	 */
	public String myTotalWealth() {
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		BigDecimal totalWealth;// 总资产
		BigDecimal accountBalanceResult;// 懒猫账户余额
		BigDecimal balanceInfoResult;// 生财宝总金额
		BigDecimal xtTotalAmount;// 信托理财总额
		BigDecimal freezeAmount;//提现在途金额
		BigDecimal policyAmount = BigDecimal.ZERO;//灵机一投金额
		BigDecimal fundTotalAmount = new BigDecimal(0);// 基金的总资产
		try{
			ZtMemberWealthInfoDTO ztMemberWealthInfoDTO = ztPolicyOrderFacade.queryZtWealthInfoByMemberNo(memberDto.getMemberNo());
			if(ztMemberWealthInfoDTO != null){
				policyAmount = ztMemberWealthInfoDTO.getWealth();
			}
		}catch(Exception e){
			logger.error("[myTotalWealth] info={},ERROR={}","查询灵机一投金额失异常",e);
			policyAmount = BigDecimal.ZERO;
		}
		if(policyAmount == null){
			policyAmount = BigDecimal.ZERO;
		}
		try {
			//查询基金的收益资产信息
			UserTotalCountResult userFundResult = fundUserShareQueryFacade
					.queryUserTotalCount(GetParamUtils.getFundPlatNo(),
							memberDto.getMemberNo());
			if(null != userFundResult && null != userFundResult.getTotalWorth()) {
				fundTotalAmount = userFundResult.getTotalWorth();
			}
			if(null != userFundResult && null != userFundResult.getApplyingAmount()) {
				fundTotalAmount = fundTotalAmount.add(userFundResult.getApplyingAmount());
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("查询基金的资产信息有误",e.getMessage());
		}

		try {
			// 查询懒猫账户余额
			AccountInfoQueryResultDto aiqrDto = lanmaoDemandFacade
					.queryAccount(memberDto.getMemberNo(), new Date());
			accountBalanceResult = aiqrDto == null
					|| aiqrDto.getAmount() == null ? new BigDecimal(0)
					: aiqrDto.getAmount();
			//提现在途金额	
		    freezeAmount = aiqrDto == null
					|| aiqrDto.getFreezeAmount() == null ? new BigDecimal(0)
					: aiqrDto.getFreezeAmount();
			// 查询生财宝总金额
			BalanceInfoResultDTO balanceInfoResultDTO = fundBizTranscationFacade
					.queryBalanceInfo(GetParamUtils.getScbPlatNo(),
							memberDto.getMemberNo());
			balanceInfoResult = balanceInfoResultDTO == null
					|| balanceInfoResultDTO.getBalance() == null ? new BigDecimal(
					0) : balanceInfoResultDTO.getBalance();
			// 查询信托理财收益和资金总额
			FixedTimeAccountResultDto ftardto = fixedTimeAccountFacade
					.queryFixedTimeAccount(memberDto.getMemberNo());
			xtTotalAmount = ftardto == null || ftardto.getTotalAmount() == null ? new BigDecimal(
					0) : ftardto.getTotalAmount();
			xtTotalAmount = xtTotalAmount.add(ftardto == null
					|| ftardto.getExpectIncome() == null ? new BigDecimal(0)
					: ftardto.getExpectIncome());
			// 总资产
			totalWealth = xtTotalAmount.add(balanceInfoResult).add(
					accountBalanceResult).add(fundTotalAmount).add(policyAmount);
		} catch (Exception e) {
			logger.error("[myTotalWealth] info={},ERROR={}", "我的总资产异常",
					e.getMessage());
			return SYSTEM_EXCEPTION;
		}
		String ascMemberNo =  SecretUtils.secretData(memberDto.getMemberNo());
		String fundSalesUrl = GetParamUtils.getfundSalesUrl();
		
		if(null != session.getAttribute("platform")){
			addModelObject("platform","APP");
		}
		// 格式化
		addModelObject("totalWealth", totalWealth);// 总资产
		addModelObject("accountBalanceResult", accountBalanceResult);// 懒猫账户余额
		addModelObject("freezeAmount", freezeAmount);// 提现在途金额
		addModelObject("balanceInfoResult", balanceInfoResult);// 生财宝总金额
		addModelObject("xtTotalAmount", xtTotalAmount);// 信托理财总额
		addModelObject("fundTotalAmount", fundTotalAmount);// 基金的总资产
		addModelObject("policyAmount", policyAmount);//灵机一投总资产
		addModelObject("fundSalesUrl", fundSalesUrl);// 基金的域名地址
		addModelObject("ascMemberNo", ascMemberNo);// 加密的用户编码
		return "success";
	}

	/**
	 * 查询用户scb收益记录
	 */
	public String queryAccumulativeIncome(@Param("pageIndex") Integer pageIndex) {
		logger.info("[toMyIncome] info={}","查询用户scb收益记录。。。。。。");
		Object result = queryAccumulativeIncomeCommon(pageIndex);
		setJsonModel(result);
		return "json";
	}

	// scb查询收益记录
	public Object queryAccumulativeIncomeCommon(Integer pageIndex) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		logger.info("[queryAccumulativeIncome] info={}","查询用户"+memberDto.getLoginName()+"scb收益记录。。。。。。");
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<FundIncomeDTO> resultFilter = new ArrayList<FundIncomeDTO>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if (null == pageIndex) {
				pageIndex = 1;
			}
			// 查询所有记录
			FundIncomePageDTO fundIncomePageDTO = fundQueryFacade
					.queryLstIncomes(GetParamUtils.getScbPlatNo(),
							memberDto.getMemberNo(), 1, 9999, null, new Date());
			if (fundIncomePageDTO != null
					&& fundIncomePageDTO.getFundIncomeDTOs() != null
					&& fundIncomePageDTO.getFundIncomeDTOs().size() != 0) {
				BigDecimal maxValue = new BigDecimal(0);
				for (FundIncomeDTO fundIncomeDTO : fundIncomePageDTO
						.getFundIncomeDTOs()) {
					// 过滤空值 过滤最大值
					if (fundIncomeDTO == null
							|| fundIncomeDTO.getLatestIncome() == null
							|| new BigDecimal(0).compareTo(fundIncomeDTO
									.getLatestIncome()) == 0) {
						continue;
					} else {
						if (fundIncomeDTO.getLatestIncome() != null
								&& !new BigDecimal(0).equals(fundIncomeDTO
										.getLatestIncome())) {
							if (maxValue.compareTo(fundIncomeDTO
									.getLatestIncome()) == -1) {
								maxValue = fundIncomeDTO.getLatestIncome();
							}
//							resultFilter.add(fundIncomeDTO);
							Map<String, Object> item = new HashMap<String, Object>();
							item.put("date", dateFormat.format(fundIncomeDTO
									.getLatestIncomeTime()));
							item.put("money", fundIncomeDTO.getLatestIncome()
									.toString());
							result.add(item);
						}
					}
				}

				// 分页
			/*	for (int i = (pageIndex - 1) * PAGE_SIZE; i < pageIndex
						* PAGE_SIZE
						&& i < resultFilter.size(); i++) {
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("date", dateFormat.format(resultFilter.get(i)
							.getLatestIncomeTime()));
					item.put("money", resultFilter.get(i).getLatestIncome()
							.toString());
					result.add(item);
				}*/
				resultMap.put("list", result);
				resultMap.put("maxValue", maxValue);
				resultMap.put("pageTotal", (int) Math.ceil(result.size()
						/ (float) PAGE_SIZE));
				resultMap.put("rowTotal", result.size());
				resultMap.put("pageSize", PAGE_SIZE);
				return resultMap;
			} else {
				return NULL_RESULT_JSON;
			}
		} catch (Exception e) {
			logger.error("[queryAccumulativeIncomeCommon] info={},ERROR={}",
					"查询累计收益记录异常", e.getMessage());
			return NULL_RESULT_JSON;
		}
	}
	
	/**
	 * 查询基金的每日收益记录
	 * @author hongbin.kang
	 * @date 2016年8月18日 下午7:48:21
	 * @param pageIndex
	 * @return
	 */
	public String queryFundIncome(@Param("pageIndex") Integer pageIndex) {
		logger.info("[queryFundIncome] info={}","查询基金的每日收益记录。。。。。。");
		Object result = queryFundIncomeCommon(pageIndex);
		setJsonModel(result);
		return "json";
	}

	/**
	 * 查询基金的每日收益记录
	 * @author hongbin.kang
	 * @date 2016年8月18日 下午7:49:02
	 * @param pageIndex
	 * @return
	 */
	private Object queryFundIncomeCommon(Integer pageIndex) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		logger.info("[queryFundIncome] info={}","查询"+memberDto.getLoginName()+"基金的每日收益记录。。。。。。");
		//查询参数
		UserLastIncomeQueryParam fundParam = new UserLastIncomeQueryParam();
		fundParam.setMerchantNo(GetParamUtils.getFundPlatNo());
//		fundParam.setMemberNo(testMemberNo);
		fundParam.setMemberNo(memberDto.getMemberNo());		
				
		
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<FundIncomeDTO> resultFilter = new ArrayList<FundIncomeDTO>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if (null == pageIndex) {
				pageIndex = 1;
			}
			// 查询所有记录
			UserLastIncomesResult fundIncomesResult = fundUserShareQueryFacade.queryUserLastIncomes(fundParam);
			if (fundIncomesResult != null
					&& fundIncomesResult.getUserShareList() != null
					&& fundIncomesResult.getUserShareList().size() != 0) {
				BigDecimal maxValue = new BigDecimal(0);
				for (UserLastIncomeDetail fundIncomeDTO : fundIncomesResult.getUserShareList()) {
					// 过滤空值 过滤最大值
					if (fundIncomeDTO == null
							|| fundIncomeDTO.getTotalIncome() == null
							|| new BigDecimal(0).compareTo(fundIncomeDTO
									.getTotalIncome()) == 0 
							|| (new BigDecimal(0).compareTo(fundIncomeDTO
											.getTotalIncome())<0 && new BigDecimal(0.005).compareTo(fundIncomeDTO
													.getTotalIncome())>0)) {
						continue;
					} else {
						if (fundIncomeDTO.getTotalIncome() != null
								&& !new BigDecimal(0).equals(fundIncomeDTO
										.getTotalIncome())) {
							if (maxValue.compareTo(fundIncomeDTO
									.getTotalIncome()) == -1) {
								maxValue = fundIncomeDTO.getTotalIncome();
							}
//							resultFilter.add(fundIncomeDTO);
							Map<String, Object> item = new HashMap<String, Object>();
							item.put("date", dateFormat.format(fundIncomeDTO
									.getIncomeDate()));
							item.put("money", fundIncomeDTO.getTotalIncome()
									.toString());
							result.add(item);
						}
					}
				}
				resultMap.put("list", result);
				resultMap.put("maxValue", maxValue);
				resultMap.put("pageTotal", (int) Math.ceil(result.size()
						/ (float) PAGE_SIZE));
				resultMap.put("rowTotal", result.size());
				resultMap.put("pageSize", PAGE_SIZE);
				return resultMap;
			} else {
				return NULL_RESULT_JSON;
			}
		} catch (Exception e) {
			logger.error("[queryFundIncomeCommon] info={},ERROR={}",
					"查询基金累计收益记录异常", e.getMessage());
			return NULL_RESULT_JSON;
		}
	}
	

	/**
	 * 查询用户信托理财收益记录
	 */
	public String queryTrustIncome(@Param("pageIndex") Integer pageIndex) {
		logger.info("[queryTrustIncome] info={}","查询用户信托理财收益记录。。。。。。");
		Object result = queryTrustIncomeCommon(pageIndex);
		setJsonModel(result);
		return "json";
	}

	// 查询定期理财收益记录
	public Object queryTrustIncomeCommon(Integer pageIndex) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		logger.info("[queryTrustIncome] info={}","查询用户"+memberDto.getLoginName()+"信托理财收益记录。。。。。。");
		List<FixedTimeIncomeHistoryResultDto> resultFilter = new ArrayList<FixedTimeIncomeHistoryResultDto>();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if (null == pageIndex) {
				pageIndex = 1;
			}
			FixedTimeIncomeHistoryResultDto resultDto = fixedTimeAccountFacade
					.queryFixedTimeIncomeFirst(memberDto.getMemberNo());
			if (null == resultDto
					|| QueryStatusEnum.FAIL == resultDto.getStatus()) {
				return NULL_RESULT_JSON;
			}
			FixedTimeIncomeHistoryListParamDto params = new FixedTimeIncomeHistoryListParamDto();
			params.setMemberNo(memberDto.getMemberNo());
			params.setStartDate(resultDto.getCreateTime());
			params.setEndDate(new Date());
			FixedTimeHistoryIncomeListResultDto fixedTimeHistoryIncomeListResultDto = fixedTimeAccountFacade
					.queryFixedTimeIncomeList(params);
			List<FixedTimeIncomeHistoryResultDto> resultList = fixedTimeHistoryIncomeListResultDto
					.getIncomeList();
			BigDecimal maxValue = new BigDecimal(0);
			// 过滤空值 过滤最大值
			for (FixedTimeIncomeHistoryResultDto itmes : resultList) {
				if (itmes.getAmount() != null
						&& new BigDecimal(0).compareTo(itmes.getAmount()) != 0) {
					if (maxValue.compareTo(itmes.getAmount()) == -1) {
						maxValue = itmes.getAmount();
					}
//					resultFilter.add(itmes);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(itmes.getCreateTime());
					calendar.add(Calendar.DATE, -1);
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("date", dateFormat.format(calendar.getTime()));
					item.put("money",itmes.getAmount().toString());
					result.add(item);
				}
			}
			/*// 分页
			for (int i = (pageIndex - 1) * PAGE_SIZE; i < pageIndex * PAGE_SIZE
					&& i < resultFilter.size(); i++) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(resultFilter.get(i).getCreateTime());
				calendar.add(Calendar.DATE, -1);
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("date", dateFormat.format(calendar.getTime()));
				item.put("money", resultFilter.get(i).getAmount().toString());
				result.add(item);
			}*/
			resultMap.put("list", result);
			resultMap.put("maxValue", maxValue);
			resultMap.put("pageTotal",
					(int) Math.ceil(result.size() / (float) PAGE_SIZE));
			resultMap.put("rowTotal", result.size());
			resultMap.put("pageSize", PAGE_SIZE);
			// test start
			return resultMap;
		} catch (Exception e) {
			logger.error("[queryTrustIncomeCommon] info={},ERROR={}",
					"查询用户信托理财收益记录异常", e.getMessage());
			return NULL_RESULT_JSON;
		}
	}

	/**
	 * 查询用户总收益记录
	 */
	public String queryTotalIncome(@Param("pageIndex") Integer pageIndex) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		logger.info("[queryTotalIncome] info={}","查询用户"+memberDto.getLoginName()+"总收益记录。。。。。。");
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> resultTotal = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> resultFilter = new ArrayList<Map<String, Object>>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if (null == pageIndex) {
				pageIndex = 1;
			}
			FixedTimeIncomeHistoryResultDto resultDto = fixedTimeAccountFacade
					.queryFixedTimeIncomeFirst(memberDto.getMemberNo());
			/*if (null == resultDto
					|| QueryStatusEnum.FAIL == resultDto.getStatus()) {
				// scb
				Object value = queryAccumulativeIncomeCommon(pageIndex);
				setJsonModel(value);
				return "json";
			}*/
			List<FixedTimeIncomeHistoryResultDto> resultList = new ArrayList<FixedTimeIncomeHistoryResultDto>();
			if(null != resultDto && QueryStatusEnum.FAIL != resultDto.getStatus()) {
				FixedTimeIncomeHistoryListParamDto params = new FixedTimeIncomeHistoryListParamDto();
				params.setMemberNo(memberDto.getMemberNo());
				params.setStartDate(resultDto.getCreateTime());
				params.setEndDate(new Date());
				FixedTimeHistoryIncomeListResultDto fixedTimeHistoryIncomeListResultDto = fixedTimeAccountFacade
						.queryFixedTimeIncomeList(params);
				resultList = fixedTimeHistoryIncomeListResultDto
						.getIncomeList();
				
			}
			FundIncomePageDTO fundIncomePageDTO = fundQueryFacade
					.queryLstIncomes(GetParamUtils.getScbPlatNo(),
							memberDto.getMemberNo(), pageIndex, PAGE_SIZE,
							null, new Date());
			/*if (fundIncomePageDTO == null
					|| fundIncomePageDTO.getFundIncomeDTOs() == null
					|| fundIncomePageDTO.getFundIncomeDTOs().size() == 0) {
				// fixed
				Object value = queryTrustIncomeCommon(pageIndex);
				setJsonModel(value);
				return "json";
			}*/
			// 如果定期理财第一次收益时间在生财宝第一次收益之前，即 生财宝收益记录少
			Date fristScb = null;
			List<FundIncomeDTO> scbResult = new ArrayList<FundIncomeDTO>();
			if(fundIncomePageDTO != null
					&& fundIncomePageDTO.getFundIncomeDTOs() != null
					&& fundIncomePageDTO.getFundIncomeDTOs().size() != 0) {
				
				fristScb = fundIncomePageDTO.getFundIncomeDTOs().get(fundIncomePageDTO.getFundIncomeDTOs().size() - 1)
						.getLatestIncomeTime();// 生财宝第一条收益时间
				scbResult = fundIncomePageDTO.getFundIncomeDTOs();
			}
			boolean flag = true;
			for (int i = 0; i < resultList.size(); i++) {
				Map<String, Object> item = new HashMap<String, Object>();
				if (flag) {
					item.put("date", dateFormat.format(resultList.get(i)
							.getCreateTime()));
					Calendar calendar = Calendar.getInstance();
					if (scbResult.size() != 0) {
						Date date = scbResult.get(0).getLatestIncomeTime();
						calendar.setTime(date);
						calendar.add(Calendar.DATE, 1);
						if (resultList.get(i).getCreateTime()
								.compareTo(calendar.getTime()) == 0) {
							item.put(
									"money",
									fundIncomePageDTO.getFundIncomeDTOs()
											.get(0).getLatestIncome()
											.add(resultList.get(i).getAmount())
											.toString());
							scbResult.remove(0);
						} else {
							item.put("money", resultList.get(i).getAmount()
									.toString());
						}
					} else {
						item.put("money", resultList.get(i).getAmount()
								.toString());
					}

				} else {
					item.put("date", dateFormat.format(resultList.get(i)
							.getCreateTime()));
					item.put("money", resultList.get(i).getAmount().toString());
				}
				if (fristScb != null && flag == true
						&& resultDto.getCreateTime().compareTo(fristScb) == 0) {
					flag = false;
				}
				result.add(item);
			}
			if(fristScb != null) {
				
				if (null == resultDto.getCreateTime()||resultDto.getCreateTime().after(fristScb)) {
					for (FundIncomeDTO scbItmes : scbResult) {
						Map<String, Object> item = new HashMap<String, Object>();
						Calendar calendar = Calendar.getInstance();
						Date date = scbItmes.getLatestIncomeTime();
						calendar.setTime(date);
						calendar.add(Calendar.DATE, 1);
						item.put("date", dateFormat.format(calendar.getTime()));
						item.put("money", scbItmes.getLatestIncome().toString());
						result.add(item);
					}
					
				}
			}
			BigDecimal maxValue = new BigDecimal(0);
			// 过滤空值 过滤最大值
			for (Map<String, Object> itmes : result) {
				if (itmes.get("money") != null
						&& 0!=Float.parseFloat(itmes.get("money").toString())) {
					if (maxValue.compareTo(new BigDecimal((String) itmes
							.get("money"))) == -1) {
						maxValue = new BigDecimal((String) itmes.get("money"));
					}
//					resultFilter.add(itmes);
					Calendar calendar = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date date = sdf.parse((itmes.get("date")
							.toString()));
					calendar.setTime(date);
					calendar.add(Calendar.DATE, -1);
					Map<String, Object> items = new HashMap<String, Object>();
					items.put("date", dateFormat.format(calendar.getTime()));
					items.put("money", itmes.get("money"));
					resultTotal.add(items);
				}
			}
			
			/**
			 * 此处有天坑，望后来者优化，若是再加一个理财类型，这就必须优化了，
			 * 建议将收益记录的表放到一个库里，每天计算出昨日收益存储起来，望后来者，洪福齐天，不在入坑
			 */
			List<Map<String, Object>> newResultTotal = new ArrayList<Map<String, Object>>();
			
			//查询参数
			UserLastIncomeQueryParam fundParam = new UserLastIncomeQueryParam();
			fundParam.setMerchantNo(GetParamUtils.getFundPlatNo());
//			fundParam.setMemberNo(testMemberNo);
			fundParam.setMemberNo(memberDto.getMemberNo());	
			
			// 查询基金所有记录
			UserLastIncomesResult fundIncomesResult = new UserLastIncomesResult();
			try {
				logger.info("查询基金的收益记录");
				fundIncomesResult = fundUserShareQueryFacade.queryUserLastIncomes(fundParam);
				
			} catch (Exception e) {
				logger.error("查询基金的收益记录出错",e.getMessage());
			}
			// 如果定期和生财宝的第一次收益时间在基金第一次收益之前，即 基金收益记录少
			if(null != fundIncomesResult && null != fundIncomesResult.getUserShareList() && fundIncomesResult.getUserShareList().size()>0) {
				if(resultTotal.size() > 0) {
					Date minDate = null;
					Date maxDate = null;
					List<UserLastIncomeDetail> userFundIncomeList = fundIncomesResult.getUserShareList();
					// 基金最小收益时间
					Date minFundDate = dateFormat.parse(dateFormat.format(userFundIncomeList.get(userFundIncomeList.size() - 1)
							.getIncomeDate()));
					// 基金最大收益时间
					Date maxFundDate = dateFormat.parse(dateFormat.format(userFundIncomeList
							.get(0).getIncomeDate()));
					//最小收益时间
					Date minReaultDate = dateFormat.parse(resultTotal.get(resultTotal.size() - 1).get("date").toString());
					// 最大收益时间
					Date maxResultDate = dateFormat.parse(resultTotal.get(0).get("date").toString());
					
					if(minFundDate.before(minReaultDate)) {
						minDate = minFundDate;
					} else {
						minDate = minReaultDate;
					}
					
					if(maxFundDate.after(maxResultDate)) {
						maxDate = maxFundDate;
					} else {
						maxDate = maxResultDate;
					}
					//计算日期差值
					int daysBetween = daysBetween(minDate,maxDate) + 1;
					//列出最大日期和最小日期每一天的收益
					for(int i = 0 ; i <daysBetween; i++) {
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(maxDate);
						calendar.add(Calendar.DATE, -i);
						
						BigDecimal bathMoney = new BigDecimal(0);
						//第一次删选的结过有对应日期的收益，相加
						if(resultTotal.size() > 0) {
							if(calendar.getTime().compareTo(dateFormat.parse(resultTotal.get(0).get("date").toString())) == 0) {
								bathMoney = bathMoney.add(new BigDecimal(resultTotal.get(0).get("money").toString()));
								resultTotal.remove(0);
							}
						}
						//对应的日期基金有收益，相加
						if(userFundIncomeList.size() > 0) {
							if(calendar.getTime().compareTo(dateFormat.parse(dateFormat.format(userFundIncomeList.get(0).getIncomeDate()))) == 0) {
								bathMoney = bathMoney.add(userFundIncomeList.get(0).getTotalIncome());
								userFundIncomeList.remove(0);
							}
						}
						
						if ((new BigDecimal(0).compareTo(bathMoney)<0 
								&& new BigDecimal(0.005).compareTo(bathMoney)>0)) {
							continue;
						}
						
						//不等于0，把数据存储起来
						if(bathMoney.compareTo(new BigDecimal(0)) != 0) {
							Map<String, Object> items = new HashMap<String, Object>();
							items.put("date", dateFormat.format(calendar.getTime()));
							items.put("money", bathMoney.toString());
							if (maxValue.compareTo(bathMoney) == -1) {
								maxValue = bathMoney;
							}
							newResultTotal.add(items);
						}
					}
					
				} else {
					for (UserLastIncomeDetail fundItmes : fundIncomesResult.getUserShareList()) {
						Map<String, Object> item = new HashMap<String, Object>();
						Date date = fundItmes.getIncomeDate();
						if(fundItmes.getTotalIncome().compareTo(new BigDecimal(0)) != 0) {
							if (maxValue.compareTo(new BigDecimal(fundItmes.getTotalIncome().toString())) == -1) {
								maxValue = new BigDecimal(fundItmes.getTotalIncome().toString());
							}
							item.put("date", dateFormat.format(date));
							item.put("money", fundItmes.getTotalIncome().toString());
							newResultTotal.add(item);
						}
					}
				}
			} else {
				newResultTotal = resultTotal;
			}
			
			/*// 分页
			for (int i = (pageIndex - 1) * PAGE_SIZE; i < pageIndex * PAGE_SIZE
					&& i < resultFilter.size(); i++) {
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = sdf.parse((resultFilter.get(i).get("date")
						.toString()));
				calendar.setTime(date);
				calendar.add(Calendar.DATE, -1);
				Map<String, Object> items = new HashMap<String, Object>();
				items.put("date", dateFormat.format(calendar.getTime()));
				items.put("money", resultFilter.get(i).get("money"));
				resultTotal.add(items);
			}*/
			resultMap.put("list", newResultTotal);
			resultMap.put("maxValue", maxValue);
			resultMap.put("pageTotal",
					(int) Math.ceil(newResultTotal.size() / (float) PAGE_SIZE));
			resultMap.put("rowTotal", newResultTotal.size());
			resultMap.put("pageSize", PAGE_SIZE);

			setJsonModel(resultMap);
		} catch (Exception e) {
			logger.error("[queryTotalIncome] info={},ERROR={}", "查询用户总收益记录异常",
					e.getMessage());
			setJsonModel(NULL_RESULT_JSON);
		}
		return "json";
	}
	
	
	/**  
     * 计算两个日期之间相差的天数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
     */    
	public static int daysBetween(Date smdate,Date bdate) throws ParseException {    
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		smdate=sdf.parse(sdf.format(smdate));  
		bdate=sdf.parse(sdf.format(bdate));  
		Calendar cal = Calendar.getInstance();    
		cal.setTime(smdate);    
		long time1 = cal.getTimeInMillis();                 
		cal.setTime(bdate);    
		long time2 = cal.getTimeInMillis();         
		long between_days=(time2-time1)/(1000*3600*24);  
		
		return Integer.parseInt(String.valueOf(between_days));           
	} 
	
	/**
	 * 基金过来的快速登录并跳转
	 * @author hongbin.kang
	 * @date 2016年8月22日 下午1:05:41
	 * @param memberNo
	 * @param type
	 * @param incomeType
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	public String fastLoginByFundSales(@Param("memberNo")String memberNo,@Param("targetUrl")String targetUrl) throws IOException {
		HttpServletResponse response = getMvcFacade().getResponse();
		HttpServletRequest request = getMvcFacade().getRequest();
		logger.info("[fastLoginByFundSales] 快速登录跳转 ");
		setSessionByFundSales(memberNo, request);
		RequestDispatcher dispatcher = request.getRequestDispatcher(targetUrl);
		try {
			dispatcher.forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * app登陆成功后，跳转基金的中间action
	 * @author hongbin.kang
	 * @date 2016年8月26日 上午11:37:34
	 * @param fundCode
	 * @return
	 * @throws IOException
	 */
	public String appFastLoginJumpFund() throws IOException {
		HttpServletResponse response = getMvcFacade().getResponse();
		HttpServletRequest request = getMvcFacade().getRequest();
		String queryString = HttpRequestUtils.getRequsetParams(request);
		logger.info("[appFastLoginJumpFund] app登陆成功后，跳转基金的中间action queryString={}",queryString);
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		String platform = (String) session.getAttribute("platform");
		String memberNo = "";
		if(null !=memberDto ) {
			memberNo = memberDto.getMemberNo();
		}
		String aesMemberNo = SecretUtils.secretData(memberNo);
		String fundSalesUrl = GetParamUtils.getfundSalesUrl();
		String params = "?memberNo="+aesMemberNo+"&platform="+platform;
		if(StringUtils.isNotEmpty(queryString)) {
			params = params+"&"+queryString;
		}
		//跳转的uri
		String interceptUrl = request.getParameter("fundUri");
		if(StringUtils.isEmpty(interceptUrl)) {
			interceptUrl = "../show/fundList";
		} else {
			interceptUrl = ".." + interceptUrl.substring(interceptUrl.indexOf("/",1));
		}
		interceptUrl = URLEncoder.encode(interceptUrl, "UTF-8");
		params = params + "&interceptUrl="+interceptUrl;
		logger.info("app登陆成功后的参数params={}",params);
		String targetUrl = fundSalesUrl + "fastLogin/loginSuccessJump" + params;
		logger.info("[appFastLoginJumpFund] app登陆成功后，跳转基金的中间action targetUrl={}",targetUrl);
		response.sendRedirect(targetUrl);
		
		return null;
	}
	
	
	
	
	
	/*public String appFastLoginFundDetail(@Param("fundCode")String fundCode) throws IOException {
		HttpServletResponse response = getMvcFacade().getResponse();
		HttpServletRequest request = getMvcFacade().getRequest();
		logger.info("[appFastLoginFundSales] app登陆成功后，跳转基金的中间action fundCode{}",fundCode);
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		String platform = (String) session.getAttribute("platform");
		String memberNo = "";
		if(null !=memberDto ) {
			memberNo = memberDto.getMemberNo();
		}
		String aesMemberNo = SecretUtils.secretData(memberNo);
		String fundSalesUrl = GetParamUtils.getfundSalesUrl();
		String targetUrl = fundSalesUrl + "show/archivesIndex/" + fundCode + "?memberNo="+aesMemberNo+"&platform="+platform;
		logger.info("[appFastLoginFundSales] app登陆成功后，跳转基金的中间action targetUrl{}",targetUrl);
		response.sendRedirect(targetUrl);
		
		return null;
	}
	
	*//**
	 * 绑卡成功后跳转基金的评测页面
	 * @author hongbin.kang
	 * @date 2016年8月26日 下午3:46:18
	 * @return
	 * @throws IOException
	 *//*
	public String appFastLoginBindCardSuccess() throws IOException {
		HttpServletResponse response = getMvcFacade().getResponse();
		HttpServletRequest request = getMvcFacade().getRequest();
		String queryString = HttpRequestUtils.getRequsetParams(request);
		logger.info("[appFastLoginBindCardSuccess] app登陆成功后，跳转基金的中间action queryString{}",queryString);
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		String platform = (String) session.getAttribute("platform");
		String memberNo = "";
		if(null !=memberDto ) {
			memberNo = memberDto.getMemberNo();
		}
		String aesMemberNo = SecretUtils.secretData(memberNo);
		String fundSalesUrl = GetParamUtils.getfundSalesUrl();
		String params = "?memberNo="+aesMemberNo+"&platform="+platform;
		if(StringUtils.isNotEmpty(queryString)) {
			params = params+"&"+queryString;
		}
		String targetUrl = fundSalesUrl + "riskTest/purchaseRisk" + params;
		logger.info("[appFastLoginFundSales] app登陆成功后，跳转基金的中间action targetUrl{}",targetUrl);
		response.sendRedirect(targetUrl);
		
		return null;
	}*/
	
	
	/**
	 * 查询充值结果
	 */
	public String selectRechargeResult(@Param("paymentFlowNo") String paymentFlowNo) {
		logger.info("[selectRechargeResult] paymentFlowNo={}",paymentFlowNo);
		HttpSession session = getMvcFacade().getHttpSession();
		HttpServletRequest request=getMvcFacade().getRequest();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			QuickPayResultDto payResult = lMAccountTradeManagementFacde.queryNoCardPay(paymentFlowNo);
			logger.info("[selectRechargeResult] payResult={}",payResult);
			if (QuickPayStatusEnum.FAIL.equals(payResult.getStatus())) {
				logger.info("[selectRechargeResult] FAILED, payResult={}", payResult);
				String des[] = GetParamUtils.readCommonMessage(
						GetParamUtils.PEBI900008).split(
						GetParamUtils.COMMON_MESSAGE_SPLIT);
				if (des[0].equals(payResult.getCode())) {
					resultMap.put("result", "payment_limit");// 支付超限
					//更新token
					String newToken = LmConstants.makeToken();
					session.setAttribute("rechargeToken", newToken);
					//传到页面
					resultMap.put("token", newToken);
					setJsonModel(resultMap);
					return "json";
				}
				resultMap.put("result", "failed");
				resultMap.put("description", payResult.getDescription());
				//更新token
				String newToken = LmConstants.makeToken();
				session.setAttribute("rechargeToken", newToken);
				//传到页面
				resultMap.put("token", newToken);
				setJsonModel(resultMap);
				return "json";
			}
			if (QuickPayStatusEnum.PROCESSING.equals(payResult.getStatus())) {
				logger.info("[selectRechargeResult] PROCESSING, payResult={}", payResult);
				resultMap.put("result", "processing");
				//更新token
				String newToken = LmConstants.makeToken();
				session.setAttribute("rechargeToken", newToken);
				//传到页面
				resultMap.put("token", newToken);
				setJsonModel(resultMap);
				return "json";
			}
			// 充值成功推送微信消息
			try {
				String openId = (String) session.getAttribute("openId");
				if(!StringUtils.isEmpty(openId)){
					Map<String,String> modelWx = LmConstants.getRechargeSuccessWxMessageModel();
					ActivityWXSendMessageDTO dataDto = new ActivityWXSendMessageDTO();
					dataDto.setFirst(modelWx.get("first"));
					dataDto.setRemark(modelWx.get("remark"));
					dataDto.setOpenId(openId);
					dataDto.setUrl(modelWx.get("url"));
					dataDto.setKeyword1(payResult.getAmount()+" 元");
					dataDto.setKeyword2(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					// 查询懒猫账户余额
					AccountInfoQueryResultDto aiqrDto = lanmaoDemandFacade
							.queryAccount(memberDto.getMemberNo(), new Date());
					BigDecimal accountBalanceResult = aiqrDto == null
							|| aiqrDto.getAmount() == null ? new BigDecimal(0)
							: aiqrDto.getAmount();
					dataDto.setKeyword3(accountBalanceResult.toString());
					activityWXSendMessageFacade.sendWxMessage(ActivityWXSendMessageEnum.RECHARGE_SUCCESS, dataDto);
				}
			} catch (Exception e) {
				logger.error("[recharge] info={},ERROR={}", "充值成功推送微信消息异常",
						e.getMessage());
			}
		} catch (Exception e) {
			logger.error("[recharge] info={},ERROR={}", "充值异常", e.getMessage());
			resultMap.put("result", "system_exception");
			resultMap.put("description", "系统异常");
			//更新token
			String newToken = LmConstants.makeToken();
			session.setAttribute("rechargeToken", newToken);
			//传到页面
			resultMap.put("token", newToken);
			setJsonModel(resultMap);
			return "json";
		}
		resultMap.put("result", "success");
		setJsonModel(resultMap);
		return "json";
	}
	
	/**
	 * 提现在途金额
	 * @author 陈大涛
	 * 2016-10-24下午4:09:06
	 */
	public String toCashOnTheWay(){
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		try {
			AccountInfoQueryResultDto aiqrDto = lanmaoDemandFacade
					.queryAccount(memberDto.getMemberNo(), new Date());
			if (!"0".equals(aiqrDto.getCode())) {// 异常，跳转到异常页面
				logger.error("[getFreezeAmount] info={},ERROR={}", "我的懒猫账号余额接口异常",
						aiqrDto.getDescription());
			}
			addModelObject("freezeAmount", aiqrDto == null
					|| aiqrDto.getFreezeAmount() == null ? new BigDecimal(0)
					: aiqrDto.getFreezeAmount());
		} catch (Exception e) {
			logger.error("[getFreezeAmount] info={},ERROR={}", "跳转提现在途金额异常",
					e.getMessage());
			return SYSTEM_EXCEPTION;
		}
		return SUCCESS;
	}
	

}
