package com.yeepay.g3.app.lmweChat.action.fixed;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yeepay.g3.facade.lmlc.trust.dto.api.ProductsForWXResultDto;
import com.yeepay.g3.app.dto.LoginResultDTO;
import com.yeepay.g3.app.enums.LoginResultEnum;
import com.yeepay.g3.app.lmweChat.action.BaseAction;
import com.yeepay.g3.app.lmweChat.biz.UserBiz;
import com.yeepay.g3.app.lmweChat.biz.impl.UserBizImpl;
import com.yeepay.g3.app.lmweChat.dto.UserUsedcouponDTO;
import com.yeepay.g3.app.lmweChat.enumType.FirstColumnEnum;
import com.yeepay.g3.app.lmweChat.utils.EntityDtoUtils;
import com.yeepay.g3.app.lmweChat.utils.HttpRequestUtils;
import com.yeepay.g3.app.lmweChat.utils.JSONObjectUtils;
import com.yeepay.g3.app.lmweChat.utils.LmConstants;
import com.yeepay.g3.app.lmweChat.utils.StringProcessorUtils;
import com.yeepay.g3.facade.activity.dto.ActivityInvForProInfoDTO;
import com.yeepay.g3.facade.activity.dto.ActivityInvForProOrderAndProInfoDTO;
import com.yeepay.g3.facade.activity.dto.ActivityInvForProOrderDTO;
import com.yeepay.g3.facade.activity.dto.ActivityUsercouponDTO;
import com.yeepay.g3.facade.activity.dto.ActivityUsercouponRecordDTO;
import com.yeepay.g3.facade.activity.dto.ActivityWXSendMessageDTO;
import com.yeepay.g3.facade.activity.enums.ActivityInvForProOrderStatusEnum;
import com.yeepay.g3.facade.activity.enums.ActivityWXSendMessageEnum;
import com.yeepay.g3.facade.activity.enums.BizTypeEnum;
import com.yeepay.g3.facade.activity.enums.CouponTypeEnum;
import com.yeepay.g3.facade.activity.enums.DiscountTypeEnum;
import com.yeepay.g3.facade.activity.enums.TripSecondDiscountSurplusNumResultCode;
import com.yeepay.g3.facade.activity.facade.ActivityCouponTradeFacade;
import com.yeepay.g3.facade.activity.facade.ActivityInvForProInfoFacade;
import com.yeepay.g3.facade.activity.facade.ActivityInvForProOrderFacade;
import com.yeepay.g3.facade.activity.facade.ActivityInvForProTripSecondDiscountFacade;
import com.yeepay.g3.facade.activity.facade.ActivityUsercouponFacade;
import com.yeepay.g3.facade.activity.facade.ActivityWXSendMessageFacade;
import com.yeepay.g3.facade.lmlc.account.dto.result.FixedTimeAccountResultDto;
import com.yeepay.g3.facade.lmlc.account.service.FixedTimeAccountFacade;
import com.yeepay.g3.facade.lmlc.trust.dto.api.AssetsInfoResultDto;
import com.yeepay.g3.facade.lmlc.trust.dto.api.InvRecordDto;
import com.yeepay.g3.facade.lmlc.trust.dto.api.ProductByTrustOrderIdResultDto;
import com.yeepay.g3.facade.lmlc.trust.dto.api.ProductContractForWXParamDto;
import com.yeepay.g3.facade.lmlc.trust.dto.api.ProductContractForWXResultDto;
import com.yeepay.g3.facade.lmlc.trust.dto.api.ProductContractParamDto;
import com.yeepay.g3.facade.lmlc.trust.dto.api.ProductContractResultDto;
import com.yeepay.g3.facade.lmlc.trust.dto.api.ProductDetailForWXResultDto;
import com.yeepay.g3.facade.lmlc.trust.dto.api.ProductInvRecordParamDto;
import com.yeepay.g3.facade.lmlc.trust.dto.api.ProductInvRecordResultDto;
import com.yeepay.g3.facade.lmlc.trust.dto.api.ProductsByColumnResultDto;
import com.yeepay.g3.facade.lmlc.trust.dto.api.TrustOrderParamDto;
import com.yeepay.g3.facade.lmlc.trust.dto.api.TrustOrderResultDto;
import com.yeepay.g3.facade.lmlc.trust.dto.api.TrustOrderResultPageDto;
import com.yeepay.g3.facade.lmlc.trust.dto.api.TrustPromotionInformationDto;
import com.yeepay.g3.facade.lmlc.trust.dto.product.ProductDetailResultDto;
import com.yeepay.g3.facade.lmlc.trust.dto.trading.PromotionCalculateParamDto;
import com.yeepay.g3.facade.lmlc.trust.dto.trading.PromotionCalculateResultDto;
import com.yeepay.g3.facade.lmlc.trust.dto.trading.TrustTradingParamDto;
import com.yeepay.g3.facade.lmlc.trust.dto.trading.TrustTradingResultDto;
import com.yeepay.g3.facade.lmlc.trust.enumtype.DeviceEnum;
import com.yeepay.g3.facade.lmlc.trust.enumtype.IncomeReleaseTypeEnum;
import com.yeepay.g3.facade.lmlc.trust.enumtype.ProductDeviceTypeEnum;
import com.yeepay.g3.facade.lmlc.trust.enumtype.ProductStatusEnum;
import com.yeepay.g3.facade.lmlc.trust.enumtype.ProductTypeEnum;
import com.yeepay.g3.facade.lmlc.trust.enumtype.TradeOrderShowTypeEnum;
import com.yeepay.g3.facade.lmlc.trust.enumtype.TradeOrderStatusEnum;
import com.yeepay.g3.facade.lmlc.trust.service.FiQueryFacade;
import com.yeepay.g3.facade.lmlc.trust.service.TradeFacade;
import com.yeepay.g3.facade.lmlc.trust.service.api.TrustQueryFacade;
import com.yeepay.g3.facade.lmportal.dto.AccountInfoQueryResultDto;
import com.yeepay.g3.facade.lmportal.dto.BankCardInfoDto;
import com.yeepay.g3.facade.lmportal.dto.MemberDto;
import com.yeepay.g3.facade.lmportal.dto.MemberPwdConstraintDto;
import com.yeepay.g3.facade.lmportal.service.LPQueryFacade;
import com.yeepay.g3.facade.lmportal.service.LanmaoDemandFacade;
import com.yeepay.g3.facade.lmportal.service.MemberPasswordFacade;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.g3.utils.config.ConfigParam;
import com.yeepay.g3.utils.config.ConfigurationUtils;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.web.IpUtils;
import com.yeepay.g3.utils.web.emvc.annotation.Param;

public class FixedAction extends BaseAction {
	
	/*protected ActivityCouponTradeFacade activityCouponTradeFacade = RemoteServiceFactory
	.getService("http://localhost:8004/activity-hessian/hessian", RemotingProtocol.HESSIAN, ActivityCouponTradeFacade.class);*/
//	protected ActivityInvForProTripSecondDiscountFacade invForProTripSecondDiscountFacade = RemoteServiceFactory
//	.getService("http://localhost:8080/activity-hessian/hessian", RemotingProtocol.HESSIAN, ActivityInvForProTripSecondDiscountFacade.class);
	private static final Logger logger = LoggerFactory
			.getLogger(FixedAction.class);

	protected TrustQueryFacade trustQueryFacade = RemoteServiceFactory
			.getService(TrustQueryFacade.class);
	protected LanmaoDemandFacade lanmaoDemandFacade = RemoteServiceFactory
			.getService(LanmaoDemandFacade.class);
	protected TradeFacade tradeFacade = RemoteServiceFactory
			.getService(TradeFacade.class);
	protected FixedTimeAccountFacade fixedTimeAccountFacade = RemoteServiceFactory
			.getService(FixedTimeAccountFacade.class);
	protected ActivityCouponTradeFacade activityCouponTradeFacade = RemoteServiceFactory
			.getService(ActivityCouponTradeFacade.class);
	/*protected ActivityCouponTradeFacade activityCouponTradeFacade = RemoteServiceFactory
			.getService("http://localhost:8004/activity-hessian/hessian", RemotingProtocol.HESSIAN, ActivityCouponTradeFacade.class);*/
	protected ActivityUsercouponFacade activityUsercouponFacade = RemoteServiceFactory
			.getService(ActivityUsercouponFacade.class);
	protected MemberPasswordFacade memberPasswordFacade = RemoteServiceFactory
			.getService(MemberPasswordFacade.class);
	protected LPQueryFacade lPQueryFacade = RemoteServiceFactory
			.getService(LPQueryFacade.class);
	protected ActivityWXSendMessageFacade activityWXSendMessageFacade = RemoteServiceFactory
			.getService(ActivityWXSendMessageFacade.class);
	
	protected FiQueryFacade fiQueryFacade = RemoteServiceFactory.getService(FiQueryFacade.class);
	protected ActivityInvForProOrderFacade invForProOrderFacade = RemoteServiceFactory
			.getService(ActivityInvForProOrderFacade.class);
	protected ActivityInvForProInfoFacade invForProInfoFacade = RemoteServiceFactory
			.getService(ActivityInvForProInfoFacade.class);
	protected ActivityInvForProTripSecondDiscountFacade invForProTripSecondDiscountFacade = RemoteServiceFactory
			.getService(ActivityInvForProTripSecondDiscountFacade.class);
	private ActivityInvForProOrderFacade activityInvForProOrderFacadeImpl = RemoteServiceFactory
			.getService(ActivityInvForProOrderFacade.class);
	private UserBiz userBizImpl;

	public void setUserBizImpl(UserBizImpl userBizImpl) {
		this.userBizImpl = userBizImpl;
	}
	//	protected ActivityWXSendMessageFacade activityWXSendMessageFacade = RemoteServiceFactory
//			.getService("http://localhost:8080/activity-hessian/hessian", RemotingProtocol.HESSIAN, ActivityWXSendMessageFacade.class);
//	private ActivityInvForProOrderFacade activityInvForProOrderFacadeImpl = RemoteServiceFactory
//			.getService("http://localhost:8080/activity-hessian/hessian", RemotingProtocol.HESSIAN, ActivityInvForProOrderFacade.class);
	/**
	 * 去信托理财账单
	 */
	public String toBill() {
		BigDecimal xtTotalAmount;// 已购买金额
		BigDecimal SumIncome;// xt已到账总收益
		BigDecimal expectIncome;// 预期收益
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		try {
			// 查询用户收益及资金总额
			FixedTimeAccountResultDto ftardto = fixedTimeAccountFacade
					.queryFixedTimeAccount(memberDto.getMemberNo());
			xtTotalAmount = ftardto == null||ftardto.getTotalAmount()==null ? new BigDecimal(0) : ftardto.getTotalAmount();
			xtTotalAmount=xtTotalAmount.add( ftardto == null||ftardto.getExpectIncome()==null ? new BigDecimal(0) :ftardto.getExpectIncome());
			// 已到账收益
			SumIncome = ftardto == null ? new BigDecimal(0) : ftardto.getSumIncome()
					;
			// 当前时间累计预期收益
			expectIncome = ftardto == null ? new BigDecimal(0) : ftardto.getExpectIncome()
					;
		} catch (Exception e) {
			logger.error("[toBill] info={},ERROR={}", "跳转信托理财菜单异常",
					e.getMessage());
			return SYSTEM_EXCEPTION;
		}
		addModelObject("purchasedAmountResult", xtTotalAmount);
		addModelObject("totalIncomeFixed", expectIncome);
		addModelObject("fixedIncomeResult",SumIncome);//已到账收益
		return "success";
	}

	/**
	 * 查询信托理财账单
	 */
	public String bill(@Param("status") String status,
			@Param("pageIndex") Integer pageIndex) {
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		try {
			TrustOrderParamDto params = new TrustOrderParamDto();
			params.setMemberNo(memberDto.getMemberNo());
			params.setPageIndex(pageIndex == null ? 1 : pageIndex);
			params.setPageSize(PAGE_SIZE);
			params.setStatus(TradeOrderStatusEnum.SUCCESS);
			if ("arrival".equals(status)) {
				params.setShowType(TradeOrderShowTypeEnum.ARRIVAL);
			} else {
				params.setShowType(TradeOrderShowTypeEnum.NOT_ARRIVAL);
			}
			TrustOrderResultPageDto resultDto = trustQueryFacade
					.queryTrustOrder(params);
			// test
			/*TrustOrderResultPageDto resultDto =new TrustOrderResultPageDto();
			resultDto.setPageTotal(2);
			resultDto.setRowTotal(4);
			TrustOrderResultDto testDto = new TrustOrderResultDto();
			testDto.setYearRate(new BigDecimal(12));
			testDto.setBusinessStatus(TradeOrderBizStatusEnum.SETTLEMENT);
			List<TrustOrderPromResultDto> promotionResult=new ArrayList<TrustOrderPromResultDto>();
			TrustOrderPromResultDto param=new TrustOrderPromResultDto();
			param.setPromoType(PromotionTypeEnum.SURPASSED_ADD_PRINCIPAL);
			param.setPromoPaymentType(PromotionPaymentTypeEnum.PRINCIPAL_ENABLE);
			param.setPromoIncomeAmount(new BigDecimal(8));
//			param.setAddIncome(new BigDecimal(1.6));
			promotionResult.add(param);
			testDto.setPromotionResult(promotionResult);
			List list = new ArrayList<TrustOrderResultDto>();
			for (int i = 0; i < 2; i++) {
				list.add(testDto);
			}

			resultDto.setTrustOrderResult(list);*/
			// test end
//			System.out.println(resultDto);
			setJsonModel(resultDto);
		} catch (Exception e) {
			logger.error("[bill] info={},ERROR={}", "查询信托理财账单异常",
					e.getMessage());
			setJsonModel(SYSTEM_EXCEPTION_JSON) ;
		}
		return "json";
	}

	/**
	 * 去产品信息页 月月盈一定有产品(调用条件)
	 */
	public String toPurchaseProduct(@Param("productId") Long productId,@Param("loginName") String loginName,
			@Param("srcNo") String srcNo,@Param("userSessionKey")String userSessionKey) {
		HttpSession session = getMvcFacade().getHttpSession();
		HttpServletResponse response=getMvcFacade().getResponse();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		String platform=(String) session.getAttribute("platform");
		//如果session里没有就从redis里取
		if(null==memberDto&&StringUtils.isNotEmpty(loginName)&&StringUtils.isNotEmpty(srcNo)&&StringUtils.isNotEmpty(userSessionKey)){
			LoginResultDTO loginResultDto=userBizImpl.obtainLogin(loginName, srcNo, userSessionKey);
			if(null!=loginResultDto&&LoginResultEnum.SUCCESS.equals(loginResultDto.getResultMsg())){
				memberDto=loginResultDto.getMemberDto();
				session.setAttribute("member", memberDto);
			}
		}
		//APP端已登录用户判断是否绑卡
		//1-未绑卡跳去原生绑卡页面，然后去h5充值页面 
		//2-已绑卡的和微信端一样跳转去充值页面
		logger.info("[toPurchaseProduct] platform={}",platform);
		if(null!=memberDto&&StringUtils.isNotEmpty(platform)&&"APP".equals(platform)){
			try {
				BankCardInfoDto bankCardInfoDto = lPQueryFacade.obtainDefaultBankCardInfo(memberDto.getMemberNo());
				// 说明未绑卡
				if (bankCardInfoDto == null) {
					addModelObject("isBankCard", "NO");
				}
				logger.info("[toPurchaseProduct] bankCardInfoDto={}",bankCardInfoDto);
			} catch (Exception e) {
				logger.error("[toPurchaseProduct] info={},ERROR={}", "去产品信息页查询绑卡信息时异常",
						e.getMessage());
			}
		}
		try {
			// 默认月月盈第一个产品
			if (null == productId) {
				List<ProductsByColumnResultDto> defaultList = trustQueryFacade
						.queryProductsByColumn(FirstColumnEnum.MONY.toString(),ProductDeviceTypeEnum.MOBILE);
				productId = defaultList.get(0).getProductId();
			}
			// 查询产品信息
			ProductDetailForWXResultDto pdfwxrDto = trustQueryFacade
					.queryProductDetailForWX(productId);
			//testStart
			//testEnd
			addModelObject("productId", productId);
			addModelObject("pdfwxrDto", pdfwxrDto);
			logger.info("[toPurchaseProduct] pdfwxrDto={}",pdfwxrDto);
			String firstColumn=pdfwxrDto.getFirstColumn();
			addModelObject("firstColumn", firstColumn);
			if (null != memberDto) {
				// 查询账户余额
				AccountInfoQueryResultDto aiqrDto = lanmaoDemandFacade
						.queryAccount(memberDto.getMemberNo(), new Date());
				addModelObject("accountAmount", aiqrDto.getAmount());
			}
			if(FirstColumnEnum.FRESHMAN.toString().equals(firstColumn)){//新手标
				
				if (null == memberDto) {
					return "freshManNoLogin";
				} else {
					if("APP".equals(platform)){
						//查询是否是新手 
						try{
							TrustOrderParamDto trustOrderParamDto = new TrustOrderParamDto();
							trustOrderParamDto.setMemberNo(memberDto.getMemberNo());
							trustOrderParamDto.setStatus(TradeOrderStatusEnum.SUCCESS);
							TrustOrderResultPageDto trustOrderResultPageDTO = trustQueryFacade.queryTrustOrder(trustOrderParamDto);
							logger.info("[toPurchaseProduct] trustOrderResultPageDTO={}",trustOrderResultPageDTO);
							List<TrustOrderResultDto> trustList = trustOrderResultPageDTO.getTrustOrderResult();
							logger.info("[toPurchaseProduct] trustList={}",trustList);
							if(trustList == null || trustList.size() == 0){
								session.setAttribute("newcomerFlag", "YES");
							}else{
								session.setAttribute("newcomerFlag", "NO");
							}
							logger.info("[toPurchaseProduct] newcomerFlag={}",session.getAttribute("newcomerFlag"));
						}catch(Exception e){
							logger.info("[toPurchaseProduct] info={},ERROR={}","查询信托购买记录失败，新手标志设置失败",e);
						}
					}
					logger.info("[toPurchaseProduct] newcomerFlag={}",session.getAttribute("newcomerFlag"));
					addModelObject("unBuy",session.getAttribute("newcomerFlag"));//YES:已购买过产品 NO:未购买过产品
					return "freshManLogined";	
				}
			}else if(FirstColumnEnum.SHORT.toString().equals(firstColumn)){//超短期
				if (null == memberDto) {
					return "shortNoLogin";
				} else {
					return "shortLogined";	
				}
			}else{//其他，月月盈，季季盈，年年盈
				
			}
			
			//查询定期理财的列表
			List<ProductsForWXResultDto> resultList = new ArrayList<ProductsForWXResultDto>();
			List<ProductsForWXResultDto> list=null;
			List<ProductsForWXResultDto> shortProductList=new ArrayList<ProductsForWXResultDto>();
			try {
				list = trustQueryFacade
						.queryProductsForWX();
				if (list != null && list.size() != 0) {
					for (ProductsForWXResultDto productsForWXResultDto : list) {
						if("FRESHMAN".equals(productsForWXResultDto.getChannelColumn())){
							long freshmanId=productsForWXResultDto.getProductId();
							addModelObject("FRESHMANID",freshmanId);
						}
						if (productsForWXResultDto.getStatus() == ProductStatusEnum.SALING
								&& "FIXED".equals(productsForWXResultDto
										.getChannelColumn())) {
							if ("SHORT".equals(productsForWXResultDto
									.getFirstColumn())) {
								shortProductList.add(productsForWXResultDto);
							} else {
								resultList.add(productsForWXResultDto);
							}
						}
					}
				}
			} catch (Exception e) {
				list=null;
				shortProductList=null;
				logger.error("[toPurchaseProduct] info={},ERROR={}", "查询固收理财产品时异常",
						e.getMessage());
			}
			addModelObject("productList", resultList);
			if (null == memberDto) {
				return "fixedNoLogin";
			} else {
				Cookie cookie=getMvcFacade().getCookie(MLANMAO_IS_FIRST_FIXED);
				if(cookie==null){
					addModelObject("MLANMAO_IS_FIRST_FIXED","no");
					Cookie c=new Cookie("MLANMAO_IS_FIRST_FIXED","NO");
					c.setPath("/");
					c.setMaxAge(-1);
					response.addCookie(c);
				}
				return "fixedLogined";	
			}
			
		} catch (Exception e) {
			logger.error("[toPurchaseProduct] info={},ERROR={}", "去产品信息页异常",
					e.getMessage());
			return SYSTEM_EXCEPTION;
		}
	}

	/**
	 * 跳转理财顾问红包产品详情页
	 */
	public String toRedPacketsDetail(){
		HttpSession session = getMvcFacade().getHttpSession();
		HttpServletResponse response=getMvcFacade().getResponse();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		logger.info("[toRedPacketsDetail] memberDto={}",memberDto);
		//根据栏目去查询产品
		try{
			if (null != memberDto) {
				// 查询账户余额
				AccountInfoQueryResultDto aiqrDto = lanmaoDemandFacade
						.queryAccount(memberDto.getMemberNo(), new Date());
				addModelObject("accountAmount", aiqrDto.getAmount());
			}else{
				logger.error("[toRedPacketsDetail] ERROR={}", "登录失效");
				return FAILED;
			}
			ProductDetailResultDto productDetailResultDto =fiQueryFacade.obtainProductDetail("ADVISOR", "ADVISOR", "ADVISOR");
//			Date date = new Date(productDetailResultDto.getTradeDay().getTime()-24*60*60);
//			productDetailResultDto.setTradeDay(date);
			logger.info("[toRedPacketsDetail] productDetailResultDto={}",productDetailResultDto);
			addModelObject("productDetailResultDto",productDetailResultDto);
		}catch(Exception e){
			logger.error("[toRedPacketsDetail] info={},ERROR={}", "查询红包产品详情失败",e.getMessage());
			return SYSTEM_EXCEPTION;
		}
		
		return SUCCESS;
	}
	/**
	 * 获取产品相关信息
	 */
	public String getProductInfo(@Param("productId") Long productId) {
		try {
			// 查询产品信息
			ProductDetailForWXResultDto pdfwxrDto = trustQueryFacade
					.queryProductDetailForWX(productId);
			List<ProductsByColumnResultDto> pbcrDtoList = trustQueryFacade
					.queryProductsByColumn(pdfwxrDto.getFirstColumn(),ProductDeviceTypeEnum.MOBILE);
			//testStart
			/*List<ProductsByColumnResultDto> pbcrDtoList=new ArrayList<ProductsByColumnResultDto>();
			if("MONY".equals(pdfwxrDto.getFirstColumn())){
					ProductsByColumnResultDto result=new ProductsByColumnResultDto();
					result.setTermDay(30);
					result.setProductId(new Long(459));
					result.setYearRate(new BigDecimal(9));
					ProductsByColumnResultDto result1=new ProductsByColumnResultDto();
					result1.setTermDay(10);
					result1.setProductId(new Long(458));
					result1.setYearRate(new BigDecimal(9));
					pbcrDtoList.add(result);
					pbcrDtoList.add(result1);
				
			}else if("QUAY".equals(pdfwxrDto.getFirstColumn())){
					ProductsByColumnResultDto result=new ProductsByColumnResultDto();
					result.setTermDay(30);
					result.setProductId(new Long(477));
					result.setYearRate(new BigDecimal(9));
					ProductsByColumnResultDto result1=new ProductsByColumnResultDto();
					result1.setTermDay(60);
					result1.setProductId(new Long(478));
					result1.setYearRate(new BigDecimal(9));
					ProductsByColumnResultDto result2=new ProductsByColumnResultDto();
					result2.setTermDay(90);
					result2.setProductId(new Long(479));
					result2.setYearRate(new BigDecimal(9));
					pbcrDtoList.add(result);
					pbcrDtoList.add(result1);
					pbcrDtoList.add(result2);
			}else{
				ProductsByColumnResultDto result=new ProductsByColumnResultDto();
				result.setProductId(new Long(455));
				result.setTermDay(360);
				result.setYearRate(new BigDecimal(9));
				pbcrDtoList.add(result);
			}*/
			//testEnd
			setJsonModel(pbcrDtoList);
		} catch (Exception e) {
			logger.error("[getProductInfo] info={},ERROR={}", "获取产品相关信息异常",
					e.getMessage());
			setJsonModel(SYSTEM_EXCEPTION_JSON);
		}
		return "json";
	}

	/**
	 * 跳转确认购买页面
	 */
	public String toBuyProductInfo(
			@Param("productId") Long productId,
			@Param("totalPays") BigDecimal totalPays,
			@Param("expectIncomeNoVoucher") BigDecimal expectIncomeNoVoucher,
		 @Param("promoNo") String promoNo,@Param("promoType") String promoType,@Param("addRate") String addRate,
		 @Param("channel") String channel,
		 @Param("expectType") String expectType,
		 @Param("orderNum") String orderNum) {
		logger.info("[toBuyProductInfo] channel={}",channel);
		HttpSession session = getMvcFacade().getHttpSession();
		//生成token串 TODO
		String token = LmConstants.makeToken();
		//存储session和页面
		session.setAttribute("buyToken", token);
		addModelObject("token", token);
		try {
			ProductDetailForWXResultDto pdfwxrDto = trustQueryFacade
					.queryProductDetailForWX(productId);
			BigDecimal ArrivalAmount=new BigDecimal(0);
			if(promoNo==null||"".equals(promoNo)){
				addModelObject("PROMO_FLAG", false);//允许为空
				addModelObject("expectIncomeNoVoucher", expectIncomeNoVoucher);
				ArrivalAmount=expectIncomeNoVoucher.add(totalPays);
			}else{
				addModelObject("PROMO_FLAG", true);
				PromotionCalculateParamDto params = new PromotionCalculateParamDto();
				params.setAmount(totalPays);
				params.setProductId(productId);
				params.setPromoNo(promoNo);
				PromotionCalculateResultDto result = tradeFacade
						.calculatePromotion(params);
				if(result.getCode()!=null&&!"".equals(result.getCode())){
					logger.error("[toBuyProductInfo] info={},ERROR={}", " 跳转确认购买页面异常",
							"tradeFacade.calculatePromotion 接口调用异常,子系统异常描述："+result.getDescription());
					return SYSTEM_EXCEPTION;
				}
				addModelObject("promoNo", promoNo);
				addModelObject("expectIncomeNoVoucher", result.getActualIncome());
				addModelObject("addtionalIncome", result.getAddtionalIncome());
				addModelObject("promoType", promoType);
				addModelObject("addRate", null==addRate||addRate==""?0:Float.parseFloat(addRate));
				addModelObject("promoPrincipal", result.getPromoPrincipal());
				BigDecimal PromoPrincipalAdd=new BigDecimal(0);
				if("mjq".equals(promoType)){
					PromoPrincipalAdd=result.getPromoPrincipal()==null?new BigDecimal(0):result.getPromoPrincipal();
				}
				ArrivalAmount=(result.getActualIncome()==null?new BigDecimal(0):result.getActualIncome()).add(result.getAddtionalIncome()==null?
						new BigDecimal(0):result.getAddtionalIncome()).add(totalPays==null?new BigDecimal(0):totalPays)
						.add(PromoPrincipalAdd);
			}
			addModelObject("ArrivalAmount", ArrivalAmount);
			addModelObject("contractUrl", pdfwxrDto.getContractUrl());
			addModelObject("PRO_NAME", pdfwxrDto.getProductName());
			addModelObject("PERIOD_NO", pdfwxrDto.getPeriodNo());
			addModelObject("TERM_DAY", pdfwxrDto.getTermDay());
			addModelObject("PRO_NO", productId);
			addModelObject("TOTAL_PAYS", totalPays);
			addModelObject("channel",channel);
			addModelObject("expectType",expectType);
			addModelObject("orderNum",orderNum);
		} catch (Exception e) {
			logger.error("[toBuyProductInfo] info={},ERROR={}", " 跳转确认购买页面异常",
					e.getMessage());
			return SYSTEM_EXCEPTION;
		}
		return "success";
	}

	/**
	 * 获取二级栏目
	 */
	public String getSecondColumn(@Param("firstColumn") String firstColumn) {
		try {
			List<ProductsByColumnResultDto> pbcrDtoList = trustQueryFacade
					.queryProductsByColumn(firstColumn,ProductDeviceTypeEnum.MOBILE);
			//testStart
			/*List<ProductsByColumnResultDto> pbcrDtoList=new ArrayList<ProductsByColumnResultDto>();
			if("MONY".equals(firstColumn)){
					ProductsByColumnResultDto result=new ProductsByColumnResultDto();
					result.setTermDay(30);
					result.setProductId(new Long(459));
					result.setYearRate(new BigDecimal(9));
					ProductsByColumnResultDto result1=new ProductsByColumnResultDto();
					result1.setTermDay(10);
					result1.setProductId(new Long(458));
					result1.setYearRate(new BigDecimal(9));
					pbcrDtoList.add(result);
					pbcrDtoList.add(result1);
				
			}else if("QUAY".equals(firstColumn)){
					ProductsByColumnResultDto result=new ProductsByColumnResultDto();
					result.setTermDay(30);
					result.setProductId(new Long(477));
					result.setYearRate(new BigDecimal(9));
					ProductsByColumnResultDto result1=new ProductsByColumnResultDto();
					result1.setTermDay(60);
					result1.setProductId(new Long(478));
					result1.setYearRate(new BigDecimal(9));
					ProductsByColumnResultDto result2=new ProductsByColumnResultDto();
					result2.setTermDay(90);
					result2.setProductId(new Long(479));
					result2.setYearRate(new BigDecimal(9));
					pbcrDtoList.add(result);
					pbcrDtoList.add(result1);
					pbcrDtoList.add(result2);
			}else{
				ProductsByColumnResultDto result=new ProductsByColumnResultDto();
				result.setProductId(new Long(455));
				result.setTermDay(360);
				result.setYearRate(new BigDecimal(9));
				pbcrDtoList.add(result);
			}*/
			//testEnd
			setJsonModel(pbcrDtoList);
			return "json";
		} catch (Exception e) {
			logger.error("[getSecondColumn] info={},ERROR={}", "根据产品ID获取产品信息异常",
					e.getMessage());
			setJsonModel(SYSTEM_EXCEPTION_JSON);
			return "json";
		}

	}

	/**
	 * 根据产品ID获取产品信息
	 */
	public String getProductById(@Param("productId") Long productId) {
		try {
			ProductDetailForWXResultDto pdfwxrDto = trustQueryFacade
					.queryProductDetailForWX(productId);
			setJsonModel(pdfwxrDto);
		} catch (Exception e) {
			logger.error("[getProductById] info={},ERROR={}", "根据产品ID获取产品信息异常",
					e.getMessage());
			setJsonModel(SYSTEM_EXCEPTION_JSON);
		}

		return "json";

	}

	/**
	 * 去产品详情页 
	 */
	public String productDetailInfo(@Param("productId") Long productId,@Param("loginName") String loginName,
			@Param("srcNo") String srcNo,@Param("userSessionKey")String userSessionKey) {
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto)session.getAttribute("member");
		String platform = (String) session.getAttribute("platform");
		try{
			//如果session里没有就从redis里取
			if(null == memberDto && StringUtils.isNotEmpty(loginName) && StringUtils.isNotEmpty(srcNo) && StringUtils.isNotEmpty(userSessionKey)){
				LoginResultDTO loginResultDto=userBizImpl.obtainLogin(loginName, srcNo, userSessionKey);
				if(null!=loginResultDto&&LoginResultEnum.SUCCESS.equals(loginResultDto.getResultMsg())){
					memberDto=loginResultDto.getMemberDto();
					session.setAttribute("member", memberDto);
				}
			}
		}catch(Exception e){
			logger.error("[productDetailInfo] info={},ERROR={}","redis取member信息异常",e);
		}
		try {
			//可以优化 只返回一个pdfwxrDto
			ProductDetailForWXResultDto pdfwxrDto = trustQueryFacade
					.queryProductDetailForWX(productId);
			addModelObject("yearRate", pdfwxrDto.getYearRate());
			addModelObject("termDay", pdfwxrDto.getTermDay());
			//投资记录人次和总金额
			ProductInvRecordParamDto params = new ProductInvRecordParamDto();
			params.setPageIndex(1);
			params.setPageSize(PAGE_SIZE_MAX);
			params.setProductId(productId);
			ProductInvRecordResultDto result = trustQueryFacade
					.queryProductInvRecord(params);
			BigDecimal totalPay=new BigDecimal(0);
			for(InvRecordDto itmes:result.getInvRecord()){
				totalPay=totalPay.add(itmes.getAmount());
			}
			addModelObject("productId", productId);
			addModelObject("totalPay", totalPay);
			addModelObject("pdfwxrDto", pdfwxrDto);
			AssetsInfoResultDto safeguards =new AssetsInfoResultDto();
			String info="";
			if(null!=pdfwxrDto.getAssetsInfoList()&&pdfwxrDto.getAssetsInfoList().size()!=0){
				Map<String, String> ConfigurationMap=new HashMap<String, String>();
				ConfigParam<Map<String, String>> configParam=ConfigurationUtils
						.getConfigParam(CONFIG_TYPE_TEXT_RESOURCES, LMWX_PRODUCT_INFO_PARAM);
				if(configParam.getValue() == null){
					logger.error("[productDetailInfo] error={}","获取消息模板id，统一配置调用失败");
				}
				ConfigurationMap= configParam.getValue() == null ? new HashMap<String, String>() : configParam.getValue();
				for(AssetsInfoResultDto itmes:pdfwxrDto.getAssetsInfoList()){
					if((ConfigurationMap.get("safeguards")==null?"":ConfigurationMap.get("safeguards")).equals(itmes.getTitle())){
						safeguards=itmes;
					}
					if((ConfigurationMap.get("info")==null?"":ConfigurationMap.get("info")).equals(itmes.getTitle())){
						info=itmes.getInfo().toString();
					}
					continue;
				}
			}
			String[] safeguardList=safeguards.getInfo()==null?null:safeguards.getInfo().split("。");
			addModelObject("listInfo",safeguardList );
			logger.info("[去产品详情页]pdfwxrDto="+pdfwxrDto);
			addModelObject("info", info);
			addModelObject("totalSize", result.getRowTotal());
			addModelObject("pageTotal",Math.ceil((float)result.getRowTotal()/PAGE_SIZE) );
			 if(ProductTypeEnum.FLOAT==pdfwxrDto.getProductType()){ //浮动持有期限型
				addModelObject("raiseStart", pdfwxrDto.getRaiseStart());
				addModelObject("expireDay", pdfwxrDto.getExpireDay());
				addModelObject("totalAmount", pdfwxrDto.getTotalAmount());
				addModelObject("type", "float");
			}else{
				 addModelObject("type", "repository");
			}
			//判断是否登录跳转不同页面
			 if(memberDto!=null){
				 return "repository"; 
			 }else{
				 addModelObject("platform", platform);
				 return "repositoryNoLogin"; 
			 }
//			 return "repository"; 
			
		} catch (Exception e) {
			logger.error("[productDetailInfo] info={},ERROR={}", "去产品详情页异常",
					e.getMessage());
			return SYSTEM_EXCEPTION;
		}
	}
	/**
	 * 投资记录
	 */
	public String ProductInvRecord(@Param("productId") Long productId,@Param("pageIndex") Integer pageIndex){
		try {
			ProductInvRecordParamDto params = new ProductInvRecordParamDto();
			params.setPageIndex(pageIndex);
			params.setPageSize(PAGE_SIZE);
			params.setProductId(productId);
			ProductInvRecordResultDto result = trustQueryFacade
					.queryProductInvRecord(params);
			setJsonModel(result);
		} catch (Exception e) {
			logger.error("[ProductInvRecord] info={},ERROR={}", "投资记录异常",
					e.getMessage());
			setJsonModel(SYSTEM_EXCEPTION_JSON);
		}
		return "json";
	}
	/**
	 * 购买信托理财产品
	 */
	public String purchaseProduct(@Param("tradePwd") String tradePwd,
			@Param("productId") Long productId, @Param("amount") BigDecimal amount,
			@Param("usePromotion") Boolean usePromotion,
			@Param("promotionFlow") Long promotionFlow,
			@Param("contractUrl") String contractUrl,@Param("periodNo") String periodNo,
			@Param("expectType") String expectType,
			@Param("orderNum") String orderNum,
			@Param("token") String token) {
		HttpServletRequest request=getMvcFacade().getRequest();
		HttpSession session = getMvcFacade().getHttpSession();
		//验证token
		String sessionToken = (String) session.getAttribute("buyToken");
		if(!StringUtils.isEmpty(token) && token.equals(sessionToken)){
			MemberDto memberDto = (MemberDto) session.getAttribute("member");
			String couponType="";
			Map<String,Object> resultMap=new HashMap<String,Object>();
			try {
				if (null == amount || new BigDecimal(0).equals(amount)) {
					resultMap.put("result", "failed");
					resultMap.put("description", "购买金额不允许为空");
					//更新token
					String newToken = LmConstants.makeToken();
					session.setAttribute("buyToken", newToken);
					//传入页面
					resultMap.put("token", newToken);
					setJsonModel(resultMap);
					return "json";
				}
				ProductDetailForWXResultDto pdfwxrDto = trustQueryFacade
						.queryProductDetailForWX(productId);
				
				//判断是否新手专区产品，且该会员是否已经购买过新手产品
				if(FirstColumnEnum.FRESHMAN.toString().equals(pdfwxrDto.getFirstColumn())) {
					//查询是否是新手
					TrustOrderParamDto trustOrderParamDto = new TrustOrderParamDto();
					trustOrderParamDto.setMemberNo(memberDto.getMemberNo());
					trustOrderParamDto.setStatus(TradeOrderStatusEnum.SUCCESS);
					TrustOrderResultPageDto trustOrderResultPageDTO = trustQueryFacade.queryTrustOrder(trustOrderParamDto);
					logger.info("[purchaseProduct] trustOrderResultPageDTO={}",trustOrderResultPageDTO);
					List<TrustOrderResultDto> list = trustOrderResultPageDTO.getTrustOrderResult();
					logger.info("[purchaseProduct] list={}",list);
					if(list != null && list.size() > 0){
						session.setAttribute("newcomerFlag", "NO");
						resultMap.put("result", "failed");
						resultMap.put("description", "不允许购买新手专区产品");
						//更新token
						String newToken = LmConstants.makeToken();
						session.setAttribute("buyToken", newToken);
						//传入页面
						resultMap.put("token", newToken);
						setJsonModel(resultMap);
						return "json";
					}
				}
				
				if(null != orderNum && !"".equals(orderNum) && !"null".equalsIgnoreCase(orderNum)) {
					try {
						ActivityInvForProOrderAndProInfoDTO invForProOrderAndProInfoDto = invForProOrderFacade.queryInvForProOrderDetailByOrderCode(orderNum);
						
						if(null != invForProOrderAndProInfoDto){
							//1.查询订单是否已经过期
							if(ActivityInvForProOrderStatusEnum.CANCE.equals(invForProOrderAndProInfoDto.getActivityInvForProOrderDTO().getStatus())) {
								logger.info("[purchaseProduct] error={}","订单已经过期");
								resultMap.put("result", "sale_finish");
								resultMap.put("description", "订单已经过期");
								//更新token
								String newToken = LmConstants.makeToken();
								session.setAttribute("buyToken", newToken);
								//传入页面
								resultMap.put("token", newToken);
								setJsonModel(resultMap);
								return "json";
							}
							
							//2.判断数量余额
							ActivityInvForProInfoDTO invForProInfoDto = invForProInfoFacade.selectInvForProInfoById(invForProOrderAndProInfoDto.getActivityInvForProOrderDTO().getProductId());
							if(Integer.valueOf(invForProOrderAndProInfoDto.getActivityInvForProOrderDTO().getNum()) > (invForProInfoDto.getStockNum()-invForProInfoDto.getUsedNum())) {
								logger.info("[purchaseProduct] 旅游线路余额不足");
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("result", "fail");
								map.put("description", "旅游线路余额不足");
								//更新token
								String newToken = LmConstants.makeToken();
								session.setAttribute("buyToken", newToken);
								//传入页面
								map.put("token", newToken);
								setJsonModel(map);
								return "json";
							}
							//3.根据人数，打折限额剩余数计算总金额
							//查询第二次投资打折限额
							Integer surplus=0;
							try {
								String surplusNum = invForProTripSecondDiscountFacade.
								queryTripSecondDiscountSurplusNum(LmConstants.INVFORPRO_SECOND_DISCOUNT_TRIP_ACTION, 
										LmConstants.INVFORPRO_SECOND_DISCOUNT_TRIP_ACTIVITY);
								if(TripSecondDiscountSurplusNumResultCode.NO_ACITITY.toString().equals(surplusNum)){
									logger.error("[purchaseProduct] 查询第二次投资打折限额 ,没有活动或活动失效");
								}else if(TripSecondDiscountSurplusNumResultCode.NO_PRIZE.toString().equals(surplusNum)){
									logger.error("[purchaseProduct] 查询第二次投资打折限额 ,没有奖品");
								}else if(TripSecondDiscountSurplusNumResultCode.ERROR_PRIZE.toString().equals(surplusNum)){
									logger.error("[purchaseProduct] 查询第二次投资打折限额,奖品配置错误");
								}else{
									surplus=Integer.valueOf(surplusNum);
								}
							} catch (Exception e) {
								logger.error("[purchaseProduct] 查询第二次投资打折限额异常 ",e);
							}
							//计算打折之后的总金额
							Integer buyNum=invForProOrderAndProInfoDto.getActivityInvForProOrderDTO().getNum();
							BigDecimal total = invForProOrderAndProInfoDto.getActivityInvForProOrderDTO().getPrice().multiply(new BigDecimal(invForProOrderAndProInfoDto.getActivityInvForProOrderDTO().getNum()));
							logger.info("[purchaseProduct] 计算打折之后的总金额buyNum={},price={},surplus={}",buyNum,invForProOrderAndProInfoDto.getActivityInvForProOrderDTO().getPrice(),surplus);
							 if(surplus!=0&&buyNum>1){
								 Float buyNumF=null;
							      if(buyNum/2>surplus){//超出限额
							    	  buyNumF=(float) (buyNum-surplus*2+surplus*1.5);
							      }else{
							    	  buyNumF=(float)(buyNum/2*1.5+buyNum%2);
							      }
							      total=invForProOrderAndProInfoDto.getActivityInvForProOrderDTO().getPrice().multiply(new BigDecimal(buyNumF));
							    }
							if(total.compareTo(invForProOrderAndProInfoDto.getActivityInvForProOrderDTO().getTotal())!=0){
								try {
									//自动取消折扣限额不足等原因的订单
									ActivityInvForProOrderDTO activityInvForProOrderDTO = new ActivityInvForProOrderDTO();
									activityInvForProOrderDTO.setId(invForProOrderAndProInfoDto.getActivityInvForProOrderDTO().getId());
									activityInvForProOrderDTO.setVersion(invForProOrderAndProInfoDto.getActivityInvForProOrderDTO().getVersion());
									activityInvForProOrderDTO.setStatus(ActivityInvForProOrderStatusEnum.CANCE);
									activityInvForProOrderDTO.setOperator("自动取消折扣限额不足等原因的订单");
									activityInvForProOrderDTO.setOperatorTime(new Date());
									activityInvForProOrderFacadeImpl.updateInvForProOrder(activityInvForProOrderDTO);
								} catch (Exception e) {
									logger.error("[purchaseProduct] error={},info={}","自动取消折扣限额不足等原因的订单异常",e);
								}
								
								logger.info("[purchaseProduct] info={}","投资换产品折扣订单因打折限额不足等原因已取消,总金额不正确");
								resultMap.put("result", "fail");
								resultMap.put("description", "折扣订单因打折限额不足等原因已取消");
								//更新token
								String newToken = LmConstants.makeToken();
								session.setAttribute("buyToken", newToken);
								//传入页面
								resultMap.put("token", newToken);
								setJsonModel(resultMap);
								return "json";
							}
						
						}
					} catch (Exception e) {
						logger.info("[purchaseProduct] error={}","投资换产品订单查询有误");
						resultMap.put("result", "sale_finish");
						resultMap.put("description", "投资换产品订单查询有误");
						//更新token
						String newToken = LmConstants.makeToken();
						session.setAttribute("buyToken", newToken);
						//传入页面
						resultMap.put("token", newToken);
						setJsonModel(resultMap);
						return "json";
					}
					
				}
				
				//期号是否一致
				if(!periodNo.equals(pdfwxrDto.getPeriodNo())){
					resultMap.put("result", "noMatch");
					resultMap.put("periodNoOld", periodNo);
					resultMap.put("periodNoNew", pdfwxrDto.getPeriodNo());
					resultMap.put("incomeDay", pdfwxrDto.getIncomeDay());
					//更新token
					String newToken = LmConstants.makeToken();
					session.setAttribute("buyToken", newToken);
					//传入页面
					resultMap.put("token", newToken);
					setJsonModel(resultMap);
					return "json";
				}
				//查询产品是否已售完
				if(ProductStatusEnum.SALE_FINISH.equals(pdfwxrDto.getStatus())){
					logger.info("[purchaseProduct] error={}","产品已售完");
					resultMap.put("result", "sale_finish");
					resultMap.put("description", "购买失败，产品被抢光啦");
					//更新token
					String newToken = LmConstants.makeToken();
					session.setAttribute("buyToken", newToken);
					//传入页面
					resultMap.put("token", newToken);
					setJsonModel(resultMap);
					return "json";
				}
				//查询产品是否已售完
				if(ProductStatusEnum.PAUSE.equals(pdfwxrDto.getStatus())){
					logger.info("[purchaseProduct] error={}","产品暂停销售");
					resultMap.put("result", "sale_finish");
					resultMap.put("description", "购买失败，产品暂停销售");
					//更新token
					String newToken = LmConstants.makeToken();
					session.setAttribute("buyToken", newToken);
					//传入页面
					resultMap.put("token", newToken);
					setJsonModel(resultMap);
					return "json";
				}
				//查询产品剩余金额是否小于购买金额
				if(amount.compareTo(pdfwxrDto.getSurplusAmount())>1){
					logger.info("[purchaseProduct] error={}","产品剩余金额不足");
					resultMap.put("result", "sale_no_enough");
					resultMap.put("description", "购买失败，产品剩余金额不足");
					//更新token
					String newToken = LmConstants.makeToken();
					session.setAttribute("buyToken", newToken);
					//传入页面
					resultMap.put("token", newToken);
					setJsonModel(resultMap);
					return "json";
				}
				//验证交易密码
				MemberPwdConstraintDto memberPwdConstraintDto = memberPasswordFacade.checkTradePassword(memberDto.getMemberNo(), tradePwd);
				if (!memberPwdConstraintDto.getResultFlag()) {
					resultMap.put("result", "WRONG_PWD");
					resultMap.put("description",memberPwdConstraintDto );
					//更新token
					String newToken = LmConstants.makeToken();
					session.setAttribute("buyToken", newToken);
					//传入页面
					resultMap.put("token", newToken);
					setJsonModel(resultMap);
					return "json";
				}
				
				TrustTradingParamDto params = new TrustTradingParamDto();
				params.setAmount(amount);
				params.setRequestTime(new Date());
				params.setContractUrl(contractUrl);
				params.setMemberNo(memberDto.getMemberNo());
				params.setPlatformNo(LmConstants.readPlatformNo());
				params.setProductId(productId);
				params.setPromotionFlow(promotionFlow==null?null:promotionFlow.toString());
				params.setSignatured(true);
				params.setUsePromotion(usePromotion == null ? false : usePromotion);
				params.setDevice(DeviceEnum.WX);
				params.setContractTime(new Date());
//				params.setIp(IpUtils.getIpAddr(request));
//				String macAddress = HttpRequestUtils.getMacAddress(IpUtils.getIpAddr(request));
//				String userAgent = request.getHeader("User-Agent");
				String macAddress = null;
				String userAgent = null;
				try{
					macAddress = HttpRequestUtils.getMacAddress(IpUtils.getIpAddr(request));
					userAgent = request.getHeader("User-Agent");
				}catch(Exception e){
					logger.error("[purchaseProduct] info={},error={}","获取mac地址时异常",e);
				}
				logger.info("[purchaseProduct] macAddress={},userAgent={}",macAddress,userAgent);
				Object obj = JSONObjectUtils.userAddrToJSONStr(IpUtils.getIpAddr(request),macAddress, userAgent, null);
				if(obj != null){
					params.setIp(obj.toString());
				}
				
				if(null != expectType && !"".equals(expectType) && !"null".equalsIgnoreCase(expectType)) {
					params.setReleaseType(IncomeReleaseTypeEnum.GOODS);
				}
				if(null != orderNum && !"".equals(orderNum) && !"null".equalsIgnoreCase(orderNum)) {
					params.setGoodsCode(orderNum);
				}
				TrustTradingResultDto result = tradeFacade.trustTrading(params);
				if ("SUCCESS".equals(null==result.getStatus()?"failed":result.getStatus().toString())) {
					
//					addModelObject("productId", productId);
//					addModelObject("amount", Float.parseFloat(amount));
					resultMap.put("productId", productId);
					resultMap.put("amount", amount);
					/*ProductDetailForWXResultDto pdfwxrDto = trustQueryFacade
							.queryProductDetailForWX(result.getProductId());*/
//					addModelObject("pdfwxrDto", pdfwxrDto);
					resultMap.put("pdfwxrDto", pdfwxrDto);
					//获取赠送收益
					PromotionCalculateParamDto param = new PromotionCalculateParamDto();
					param.setAmount(amount);
					param.setProductId(productId);
					param.setPromoNo(promotionFlow==null?null:promotionFlow.toString());
					PromotionCalculateResultDto pcdto = tradeFacade
							.calculatePromotion(param);
//					addModelObject("addIncome", pcdto.getAddtionalIncome());
					resultMap.put("addIncome", pcdto.getAddtionalIncome()==null?new BigDecimal(0):pcdto.getAddtionalIncome());
					resultMap.put("promoPrincipal", pcdto.getPromoPrincipal()==null?new BigDecimal(0):pcdto.getPromoPrincipal());
					//判断是否是新手标
					if("FRESHMAN".equals(pdfwxrDto.getChannelColumn())){
						couponType="freshMan";
					}
					if(promotionFlow!=null){
						ActivityUsercouponDTO activityUsercouponDTO=activityUsercouponFacade.selectUsercouponById(promotionFlow);
						if(CouponTypeEnum.INTEREST_ADD== activityUsercouponDTO.getCoupon().getCouponType()){//加息
							couponType="addRate";
//							addModelObject("addRate", activityUsercouponDTO.getCoupon().getIncreaseInterest());
							resultMap.put("addRate", activityUsercouponDTO.getCoupon().getIncreaseInterest());
						}else if(CouponTypeEnum.FULL_ADD== activityUsercouponDTO.getCoupon().getCouponType()){
							if(DiscountTypeEnum.INTEREST==activityUsercouponDTO.getCoupon().getDiscountType()){//理财金
								couponType="lcj";
							}else{//投资券
								couponType="tzq";
							}
							
						}	
					}
					//微信推送购买成功消息
					try {
						String openId = (String) session.getAttribute("openId");
						if(!StringUtils.isEmpty(openId)){
							Map<String,String> modelWx = LmConstants.getBuySuccessWxMessageModel();
							ActivityWXSendMessageDTO dataDto = new ActivityWXSendMessageDTO();
							dataDto.setFirst(modelWx.get("first"));
							dataDto.setRemark(modelWx.get("remark"));
							dataDto.setOpenId(openId);
							dataDto.setUrl(modelWx.get("url"));
							dataDto.setKeyword1(pdfwxrDto.getProductName());
							dataDto.setKeyword2(amount.toString()+" 元");
							dataDto.setKeyword3(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
							activityWXSendMessageFacade.sendWxMessage(ActivityWXSendMessageEnum.BUY_SUCCESS, dataDto);
						}
					} catch (Exception e) {
						logger.error("[purchaseProduct] info={},ERROR={}", "推送微信购买成功异常",
								e.getMessage());
					}
					session.removeAttribute("buyToken");
					resultMap.put("result", "success");
					resultMap.put("couponType", couponType);
					resultMap.put("expectType", expectType);
					resultMap.put("orderNum", orderNum);
					session.setAttribute("newcomerFlag", "NO");//是否是新手
					setJsonModel(resultMap);
					return "json";
//					return "success";
				} else {
					resultMap.put("result", "failed");
					resultMap.put("description", result.getDescription());
					//更新token
					String newToken = LmConstants.makeToken();
					session.setAttribute("buyToken", newToken);
					//传入页面
					resultMap.put("token", newToken);
					setJsonModel(resultMap);
					return "json";
				}

			} catch (Exception e) {
				logger.error("[purchaseProduct] info={},ERROR={}", "购买信托理财产品异常",
						e.getMessage());
				resultMap.put("result", "SYSTEM_EXCEPTION");
				//更新token
				String newToken = LmConstants.makeToken();
				session.setAttribute("buyToken", newToken);
				//传入页面
				resultMap.put("token", newToken);
				setJsonModel(resultMap);
				return "json";
			}
		}
		return "json";

	}
	
	/**
	 * 跳转不同的去购买成功页面
	 */
	public String toSwitchBuySuccess(@Param("productId") Long productId,@Param("amount") BigDecimal amount,
			@Param("addRate") BigDecimal addRate,@Param("promoPrincipal") BigDecimal promoPrincipal,
			@Param("addIncome") BigDecimal addIncome,@Param("couponType") String couponType,@Param("ArrivalAmount") BigDecimal ArrivalAmount,
			@Param("expectType") String expectType,
			@Param("orderNum") String orderNum){
		logger.info("[toSwitchBuySuccess] toSwitchBuySuccess={}", "购买成功跳转不同页面");
		HttpServletResponse response = getMvcFacade().getResponse();
		HttpSession session = getMvcFacade().getHttpSession();
		HttpServletRequest request = getMvcFacade().getRequest();
		MemberDto member = (MemberDto) session.getAttribute("member");
		logger.info("[toSwitchBuySuccess] member={}",member);
		String toAction = "toBuySuccess";
		// 查询产品信息
		ProductDetailForWXResultDto pdfwxrDto = trustQueryFacade
				.queryProductDetailForWX(productId);
		//获取跳转的购买成功中间页的action
		Map<String, Object> map = (Map<String, Object>) ConfigurationUtils
				.getConfigParam("config_type_text_resources",
						"buy_success_by_source").getValue();
		logger.info("[toSwitchBuySuccess] map={}",map);
		if (null != pdfwxrDto && FirstColumnEnum.FRESHMAN.toString().equals(pdfwxrDto.getFirstColumn())) {
			//购买的是新手标并且是多啦宝渠道的用户，跳转新的购买成功页面
			if(null != member && null != member.getRegisterSrcNo()) {
				String source = member.getRegisterSrcNo();
				if (null != map && null != source && null != map.get(source)) {
					toAction = map.get(source).toString();
				}
			}
		}
		
		//获取投资换产品跳转的购买成功中间页的action
		if(null != expectType && !"".equals(expectType) && !"null".equalsIgnoreCase(expectType)) {
			Map<String, Object> invForPromap = (Map<String, Object>) ConfigurationUtils
					.getConfigParam("config_type_text_resources",
							"LM_buy_success_by_goods").getValue();
			if(null != invForPromap && null != invForPromap.get(expectType)) {
				toAction = invForPromap.get(expectType).toString();
			}
			try {
//				response.sendRedirect(toAction + "?productId=" + productId
//						+ "&amount=" + amount + "&expectType="
//						+ expectType + "&orderNum=" + orderNum);
				RequestDispatcher dispatcher = request.getRequestDispatcher(toAction + "?productId=" + productId
						+ "&amount=" + amount + "&expectType="
						+ expectType + "&orderNum=" + orderNum);
				dispatcher.forward(request, response);
				return SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
				return SUCCESS;
			}
		}
		
		String str = "&addRate=";
		if(null != addRate) {
			str = str+ addRate;
		}
		try {
//			response.sendRedirect(toAction + "?productId=" + productId
//					+ "&amount=" + amount + "&promoPrincipal="
//					+ promoPrincipal + "&addIncome=" + addIncome + "&couponType=" + couponType
//					+ "&ArrivalAmount=" + ArrivalAmount+str);
			RequestDispatcher dispatcher = request.getRequestDispatcher(toAction + "?productId=" + productId
					+ "&amount=" + amount + "&promoPrincipal="
					+ promoPrincipal + "&addIncome=" + addIncome + "&couponType=" + couponType
					+ "&ArrivalAmount=" + ArrivalAmount+str);
			dispatcher.forward(request, response);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return SUCCESS;
		}
	}
	
	/**投资换产品购买成功
	 * 去购买旅游成功页面
	 */
	public String toTourBuySuccess(@Param("productId") Long productId,@Param("amount") BigDecimal amount,
			@Param("expectType") String expectType,@Param("orderNum") String orderNum){
		logger.info("[toBuySuccess] toBuySuccess={}", "购买成功跳转正常成功");
		HttpSession session = getMvcFacade().getHttpSession();
		String openId = (String)session.getAttribute("openId");
		addModelObject("openId",openId);
		addModelObject("productId", productId);
		addModelObject("amount", amount);
		ProductDetailForWXResultDto pdfwxrDto = trustQueryFacade
				.queryProductDetailForWX(productId);
		addModelObject("pdfwxrDto", pdfwxrDto);
		ActivityInvForProOrderAndProInfoDTO invForProDto = invForProOrderFacade.queryInvForProOrderDetailByOrderCode(orderNum);
		addModelObject("invForProDto", invForProDto);
		return SUCCESS;
	}
	
	/**
	 * 多啦宝去购买成功页面
	 */
	public String toDLBaoBuySuccess(@Param("productId") Long productId,@Param("amount") BigDecimal amount,
			@Param("addRate") BigDecimal addRate,@Param("promoPrincipal") BigDecimal promoPrincipal,
			@Param("addIncome") BigDecimal addIncome,@Param("couponType") String couponType,@Param("ArrivalAmount") BigDecimal ArrivalAmount){
		logger.info("[toDLBaoBuySuccess] toDLBaoBuySuccess={}", "购买成功跳转多啦宝成功页面");
		HttpSession session = getMvcFacade().getHttpSession();
		String openId = (String)session.getAttribute("openId");
		addModelObject("openId",openId);
		addModelObject("productId", productId);
		addModelObject("amount", amount);
		ProductDetailForWXResultDto pdfwxrDto = trustQueryFacade
				.queryProductDetailForWX(productId);
		addModelObject("pdfwxrDto", pdfwxrDto);
		addModelObject("addIncome",addIncome.floatValue());
		addModelObject("promoPrincipal",promoPrincipal.floatValue());
		addModelObject("addRate", addRate);
		addModelObject("couponType",couponType);
		addModelObject("ArrivalAmount",ArrivalAmount);
		return SUCCESS;
	}
	
	/**
	 * 天翼去购买成功页面
	 */
	public String toTYBuySuccess(@Param("productId") Long productId,@Param("amount") BigDecimal amount,
			@Param("addRate") BigDecimal addRate,@Param("promoPrincipal") BigDecimal promoPrincipal,
			@Param("addIncome") BigDecimal addIncome,@Param("couponType") String couponType,@Param("ArrivalAmount") BigDecimal ArrivalAmount){
		logger.info("[toTYBuySuccess] toTYBuySuccess={}", "购买成功跳转多啦宝成功页面");
		HttpSession session = getMvcFacade().getHttpSession();
		String openId = (String)session.getAttribute("openId");
		String app = (String)session.getAttribute("platform");
		addModelObject("app",app);
		addModelObject("openId",openId);
		addModelObject("productId", productId);
		addModelObject("amount", amount);
		ProductDetailForWXResultDto pdfwxrDto = trustQueryFacade
				.queryProductDetailForWX(productId);
		addModelObject("pdfwxrDto", pdfwxrDto);
		addModelObject("addIncome",addIncome.floatValue());
		addModelObject("promoPrincipal",promoPrincipal.floatValue());
		addModelObject("addRate", addRate);
		addModelObject("couponType",couponType);
		addModelObject("ArrivalAmount",ArrivalAmount);
		return SUCCESS;
	}
	
	/**
	 * 去购买成功页面
	 */
	public String toBuySuccess(@Param("productId") Long productId,@Param("amount") BigDecimal amount,
			@Param("addRate") BigDecimal addRate,@Param("promoPrincipal") BigDecimal promoPrincipal,
			@Param("addIncome") BigDecimal addIncome,@Param("couponType") String couponType,@Param("ArrivalAmount") BigDecimal ArrivalAmount){
		logger.info("[toBuySuccess] toBuySuccess={}", "购买成功跳转正常成功");
		HttpSession session = getMvcFacade().getHttpSession();
		String openId = (String)session.getAttribute("openId");
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		String alreadyConcerned = (String)session.getAttribute("alreadyConcerned");
		String source = memberDto.getRegisterSrcNo();
		String app = (String)session.getAttribute("platform");
//		String source = (String)session.getAttribute("source");
//		addModelObject("openId",openId);
		addModelObject("source", source);
		addModelObject("alreadyConcerned", alreadyConcerned);//已经关注公众号
		if("APP".equals(app)) {
			addModelObject("app", app);
		}
		addModelObject("productId", productId);
		addModelObject("amount", amount);
		ProductDetailForWXResultDto pdfwxrDto = trustQueryFacade
				.queryProductDetailForWX(productId);
		addModelObject("pdfwxrDto", pdfwxrDto);
		addModelObject("addIncome",addIncome.floatValue());
		addModelObject("promoPrincipal",promoPrincipal.floatValue());
		addModelObject("addRate", addRate);
		addModelObject("couponType",couponType);
		addModelObject("ArrivalAmount",ArrivalAmount);
		return SUCCESS;
	}
	/**
	 * 查询用户优惠券
	 * 
	 * @return
	 */
	public String toMyCoupons() {
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		//1.未使用的优惠券
		try {
			List<ActivityUsercouponDTO> unuseList = activityCouponTradeFacade
					.selectUserCouponUnuseList(memberDto.getMemberNo());
			logger.info("[toMyCoupons] unuseList={}",unuseList.get(0).getCoupon().getIsRedPacket());
			addModelObject("unuseList", unuseList);
		} catch (Exception e) {
		
		}
		//2.已使用的卡券
		try {
			List<UserUsedcouponDTO> usedResultList = new ArrayList<UserUsedcouponDTO>();
			List<ActivityUsercouponRecordDTO> usedList = activityCouponTradeFacade
					.selectUserCouponUsedList(memberDto.getMemberNo(),
							BizTypeEnum.LM_LICAI);
			for (ActivityUsercouponRecordDTO activityUsercouponRecordDTO : usedList) {
				UserUsedcouponDTO userUsedcouponDTO = EntityDtoUtils.toTarget(
						activityUsercouponRecordDTO, UserUsedcouponDTO.class);
				String productName=null;
				try {
					ProductByTrustOrderIdResultDto productByTrustOrderIdResultDto = trustQueryFacade
							.queryProductByTrustOrderId(userUsedcouponDTO
									.getTradeId());
					 productName = productByTrustOrderIdResultDto == null
							|| productByTrustOrderIdResultDto.getProductName() == null ? ""
							: productByTrustOrderIdResultDto.getProductName();
					userUsedcouponDTO.setProductName(productName);
				} catch (Exception e) {
					logger.error("[toUsedCoupon] info={},ERROR={}", "查询未使用优惠券的产品详情异常",
							e.getMessage());
					productName=null;
				}
				userUsedcouponDTO.setProductName(productName);
				usedResultList.add(userUsedcouponDTO);
			}
			addModelObject("usedResultList",usedResultList);
		} catch (Exception e) {

		}
		//3.已过期的卡券
		try {
			List<ActivityUsercouponDTO> expiredList = activityCouponTradeFacade
					.selectUserCouponExpiredList(memberDto.getMemberNo());
			addModelObject("expiredList",expiredList);
		} catch (Exception e) {
			logger.equals("查询用户:" + memberDto.getMemberNo() + "已过期优惠券时异常");
		}
		
		return "success";
	}

	/**
	 * 查询用户已使用优惠券
	 * 
	 * @return
	 */
	public String toUsedCoupon() {
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		try {
			List<UserUsedcouponDTO> usedResultList = new ArrayList<UserUsedcouponDTO>();
			List<ActivityUsercouponRecordDTO> usedList = activityCouponTradeFacade
					.selectUserCouponUsedList(memberDto.getMemberNo(),
							BizTypeEnum.LM_LICAI);
			for (ActivityUsercouponRecordDTO activityUsercouponRecordDTO : usedList) {
				UserUsedcouponDTO userUsedcouponDTO = EntityDtoUtils.toTarget(
						activityUsercouponRecordDTO, UserUsedcouponDTO.class);
				String productName=null;
				try {
					ProductByTrustOrderIdResultDto productByTrustOrderIdResultDto = trustQueryFacade
							.queryProductByTrustOrderId(userUsedcouponDTO
									.getTradeId());
					 productName = productByTrustOrderIdResultDto == null
							|| productByTrustOrderIdResultDto.getProductName() == null ? ""
							: productByTrustOrderIdResultDto.getProductName();
					userUsedcouponDTO.setProductName(productName);
				} catch (Exception e) {
					logger.error("[toUsedCoupon] info={},ERROR={}", "查询未使用优惠券的产品详情异常",
							e.getMessage());
					productName=null;
				}
				userUsedcouponDTO.setProductName(productName);
				usedResultList.add(userUsedcouponDTO);
			}
			addModelObject("usedResultList",usedResultList);
		} catch (Exception e) {

		}
		return "success";
	}

	/**
	 * 查询用户已过期优惠券
	 * 
	 * @return
	 */
	public String toExpiredCoupon() {
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		try {
			List<ActivityUsercouponDTO> expiredList = activityCouponTradeFacade
					.selectUserCouponExpiredList(memberDto.getMemberNo());
			addModelObject("expiredList",expiredList);
		} catch (Exception e) {
			logger.equals("查询用户:" + memberDto.getMemberNo() + "已过期优惠券时异常");
		}
		return "success";
	}
	/**
	 * 查询优惠券详情
	 * @return
	 */
	public String toCouponDetail(@Param("id") long id){
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		try {
			ActivityUsercouponDTO activityUsercouponDTO=activityUsercouponFacade.selectUsercouponById(id);
			logger.info("[toCouponDetail] activityUsercouponDTO={}",activityUsercouponDTO);
			if(activityUsercouponDTO.getCoupon().getRuleDesc() != null){
				activityUsercouponDTO.getCoupon().setRuleDesc(
						StringProcessorUtils.replaceRN(activityUsercouponDTO.getCoupon().getRuleDesc()));
			}
			addModelObject("items",activityUsercouponDTO);
		} catch (Exception e) {
			logger.error("[toCouponDetail] info={},ERROR={}", "查询优惠券详情时异常",
					e.getMessage());		
			return "SYSTEM_EXCEPTION";	
		}
		return "success";
		
	}

	/**
	 * 信托理财产品可用优惠券查询
	 */
	public String queryPromotionInfomations(@Param("productId") Long productId
			) {
		logger.info("[queryPromotionInfomations] productId={}",productId);
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		logger.info("[queryPromotionInfomations] memberDto={}",memberDto);
		try {
			ProductDetailForWXResultDto pdfwxrDto = trustQueryFacade
					.queryProductDetailForWX(productId);
			logger.info("[queryPromotionInfomations] pdfwxrDto={}",pdfwxrDto);
			List<TrustPromotionInformationDto> resultList = trustQueryFacade
					.queryPromotionInfomations(memberDto.getMemberNo(),
							productId, pdfwxrDto.getChannelColumn(), pdfwxrDto.getFirstColumn(), pdfwxrDto.getSecondColumn());
			logger.info("[queryPromotionInfomations] resultList={}",resultList);
			setJsonModel(resultList);
		} catch (Exception e) {
			logger.error("[queryPromotionInfomations] info={},ERROR={}", "信托理财产品可用优惠券查询异常",
					e.getMessage());
			// 暂时除去异常处理
//			setJsonModel(SYSTEM_EXCEPTION_JSON);
		}
		return "json";
	}

	/**
	 * 使用优惠券，预核算
	 */
	public String calculatePromotion(@Param("amount") BigDecimal amount,
			@Param("productId") Long productId, @Param("promoNo") String promoNo) {
		try {
			PromotionCalculateParamDto params = new PromotionCalculateParamDto();
			params.setAmount(amount);
			params.setProductId(productId);
			params.setPromoNo(promoNo);
			PromotionCalculateResultDto result = tradeFacade
					.calculatePromotion(params);
			setJsonModel(result);
		} catch (Exception e) {
			logger.error("[calculatePromotion] info={},ERROR={}", "使用优惠券，预核算异常",
					e.getMessage());
			setJsonModel(SYSTEM_EXCEPTION_JSON);
		}
		return "json";
	}
	
	/**
     * 查询产品合同
     */
    public String toProductContract(@Param("productId") Long productId,@Param("amount") BigDecimal amount) {
    	logger.info("[toProductContract] productId={}",productId);
    	HttpSession session = getMvcFacade().getHttpSession();
    	MemberDto memberDto = (MemberDto) session.getAttribute("member");
    	HttpServletResponse response = getMvcFacade().getResponse();
        ServletOutputStream out;
        InputStream in = null;
        response.setContentType("application/pdf");
        try {
        	ProductContractForWXParamDto productContractForWXParamDto = new ProductContractForWXParamDto();
            out = response.getOutputStream();
            //获取项目的详细信息
            ProductDetailForWXResultDto pdfwxrDto = trustQueryFacade
            		.queryProductDetailForWX(productId);
            if(null != productId) {
            	productContractForWXParamDto.setPeriodNo(pdfwxrDto.getPeriodNo());
            }
            
            //放入用户的详细信息
            productContractForWXParamDto.setRealName(memberDto.getRealName());
            productContractForWXParamDto.setCredentialsNo(memberDto.getCredentialsNo());
            productContractForWXParamDto.setMobileNo(memberDto.getBindMobileNo());
            //用户的购买金额
            productContractForWXParamDto.setAmount(String.valueOf(amount));
            //如果是库存型传入起息日和到期日，产品名称
            if(ProductTypeEnum.REPOSITORY == pdfwxrDto.getProductType()) {
            	productContractForWXParamDto.setProductName(pdfwxrDto.getProductName()+" "+pdfwxrDto.getPeriodNo());
            	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            	productContractForWXParamDto.setIncomeDate(sdf.format(pdfwxrDto.getIncomeDay()));
            	productContractForWXParamDto.setExpireDate(sdf.format(pdfwxrDto.getExpireDay()));
            }
            
            ProductContractForWXResultDto prdctContractDto = trustQueryFacade
                    .queryProductContractForWX(productContractForWXParamDto);
			if (prdctContractDto != null && prdctContractDto.getData() != null
					&& prdctContractDto.getData().length != 0) {
                in = new ByteArrayInputStream(prdctContractDto.getData());
                int i;
                byte[] buffer = new byte[4096];
                while ((i = in.read(buffer)) != -1) {
                    out.write(buffer, 0, i);
                }
            }
        } catch (Exception e) {
        	logger.error("查询产品合同时异常，产品id:{}", e, productId);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                	logger.error("关闭产品合同文件流时异常", e);
                }
            }
        }
        return "success";
    }
    
    /**
     * 查询已购买的产品合同
     */
    public String toFinishedContact(@Param("periodNo") String periodNo,@Param("orderNo") String orderNo,@Param("loginName") String loginName,@Param("srcNo") String srcNo,@Param("userSessionKey") String userSessionKey) {
    	HttpSession session = getMvcFacade().getHttpSession();
    	MemberDto memberDto = (MemberDto) session.getAttribute("member");
    	HttpServletResponse response = getMvcFacade().getResponse();
        ServletOutputStream out;
        InputStream in = null;
        response.setContentType("application/pdf");
        try {
        	ProductContractParamDto productContractParamDto = new ProductContractParamDto();
            out = response.getOutputStream();
            //期号和订单号
            productContractParamDto.setPeriodNo(String.valueOf(periodNo));
            productContractParamDto.setOrderNo(String.valueOf(orderNo));
            
            ProductContractResultDto prdctContractDto = trustQueryFacade
                    .queryProductContract(productContractParamDto);
            logger.info("[toFinishedContact] prdctContractDto.getPeriodNo={}",prdctContractDto.getPeriodNo());
			if (prdctContractDto != null && prdctContractDto.getData() != null
					&& prdctContractDto.getData().length != 0) {
                in = new ByteArrayInputStream(prdctContractDto.getData());
                int i;
                byte[] buffer = new byte[4096];
                while ((i = in.read(buffer)) != -1) {
                    out.write(buffer, 0, i);
                }
            }
        } catch (Exception e) {
        	logger.error("查询产品合同时异常，产品id:{}", e, periodNo);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                	logger.error("关闭产品合同文件流时异常", e);
                }
            }
        }
        return "success";
    }
    
    //懒猫服务协议
    public String toLmProtocol() {
    	HttpSession session = getMvcFacade().getHttpSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("member");
        addModelObject("realName", StringProcessorUtils.desensitizedRealName(memberDto.getRealName()));
        return "success";
    }

    //点击banner，跳转新用户活动页面
    public String toNewManActivity(@Param("productId") Long productId) {
    	HttpSession session = getMvcFacade().getHttpSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("member");
        String platform=(String) session.getAttribute("platform");
        //判断绑卡
        //判断用户是否登陆
        if(null == memberDto) {
        	addModelObject("flag","unland");
        } else {
        	BankCardInfoDto bankCardInfoDto = lPQueryFacade.obtainDefaultBankCardInfo(memberDto.getMemberNo());
        	if(null == bankCardInfoDto) {
        		addModelObject("flag","unbind");
        	}else {
        		addModelObject("flag","bind");
        	}
        }
        addModelObject("platform", platform);
        addModelObject("productId", productId);
        return "success";
    }
    /**
     * 区分pc端或微信端新手特权页
     * @return
     */
    public String toNewerBeginner() {
    	HttpSession session = getMvcFacade().getHttpSession();
    	HttpServletRequest request = getMvcFacade().getRequest();
    	String requestHeader = getMvcFacade().getRequest().getHeader("user-agent");
    	// 获取cookie中渠道号
    	String source = null;
    	try {
    		Cookie[] cookies = request.getCookies();
    		for (Cookie cookie : cookies) {
    			logger.info("[toNewerBeginner] cookie={}", cookie.getName());
    			if (cookie.getName().equals(LmConstants.FROM_NO)) {
    				source = cookie.getValue();
    	    		session.setAttribute("srcNo", source);
    				logger.info("[toNewerBeginner] srcNo={}", source);
    			}
    		}
    	} catch (Exception e) {
    		logger.error("[toNewerBeginner] info={},ERROR={}", "cookie中取渠道号时异常",
    				e.getMessage());
    	}
    	try {
    		if (isMobileDevice(requestHeader)) {
    			//微信端链接
    			getMvcFacade().getResponse().sendRedirect("toNewManActivity?flag=menu&productId=128");
    		} else {
    			getMvcFacade().getResponse().sendRedirect("https://www.lanmao.com/lanmao/act/promo?source=H08Q43P001");
    		}
    	} catch (Exception e) {
    		logger.error("[toNewerBeginner] info={},ERROR={}", "区分pc端微信端新手特权页时跳转异常",
    				e.getMessage());
    	}
    	return "success";
    }
    
    /**
     * 根据userAgent判断是PC还是手机
     * @param requestHeader
     * @return
     */
    private static boolean  isMobileDevice(String requestHeader){
        /**
         * android : 所有android设备
         * mac os : iphone ipad
         * windows phone:Nokia等windows系统的手机
         */
        String[] deviceArray = new String[]{"android","iphone","windows phone"};
        if(requestHeader == null)
            return false;
        requestHeader = requestHeader.toLowerCase();
        for(int i=0;i<deviceArray.length;i++){
            if(requestHeader.indexOf(deviceArray[i])>0){
                return true;
            }
        }
        return false;
}

}
