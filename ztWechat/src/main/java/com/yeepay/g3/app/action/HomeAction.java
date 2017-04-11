package com.yeepay.g3.app.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lanmao.consultant.facade.dto.TblLmUser;
import com.lanmao.consultant.facade.service.UserFacade;
import com.lanmao.fund.facade.fundbiz.dto.FundbizRateInfo;
import com.lanmao.fund.facade.fundsales.dto.UserTradeQueryRequestParam;
import com.lanmao.fund.facade.fundsales.dto.UserTradeQueryResultDto;
import com.lanmao.fund.facade.fundsales.enumtype.FundTradeConfirmEnum;
import com.lanmao.fund.facade.fundsales.enumtype.FundTradeStatusEnum;
import com.lanmao.fund.facade.fundsales.enumtype.FundTradeTypeEnum;
import com.lanmao.fund.facade.fundsales.service.LMFundInfoQueryServiceFacade;
import com.lanmao.fund.facade.fundsales.service.LMFundTradeQueryFacade;
import com.lanmao.fund.facade.queryservice.dto.FundInfoQueryResultPageDTO;
import com.lanmao.fund.facade.queryservice.dto.FundQueryParamDTO;
import com.lanmao.fund.facade.queryservice.dto.LMFundBasicInfoDTO;
import com.lanmao.fund.facade.queryservice.enumtype.FundTypeEnum;
import com.lanmao.fund.facade.queryservice.enumtype.ListFundEnum;
import com.yeepay.g3.app.dto.LoginResultDTO;
import com.yeepay.g3.app.enums.LoginResultEnum;
import com.yeepay.g3.app.lmweChat.action.BaseAction;
import com.yeepay.g3.app.lmweChat.biz.UserBiz;
import com.yeepay.g3.app.lmweChat.dto.LmFundBasicInfoDTO;
import com.yeepay.g3.app.lmweChat.utils.EntityDtoUtils;
import com.yeepay.g3.app.lmweChat.utils.GetParamUtils;
import com.yeepay.g3.app.lmweChat.utils.SecretUtils;
import com.yeepay.g3.facade.activity.dto.ActivityUserInfoDTO;
import com.yeepay.g3.facade.activity.facade.ActivityUserInfoFacade;
import com.yeepay.g3.facade.activity.facade.ActivityUserMessageFacade;
import com.yeepay.g3.facade.fundbiz.dto.BalanceInfoResultDTO;
import com.yeepay.g3.facade.fundbiz.dto.IncomeInfoResultDTO;
import com.yeepay.g3.facade.fundbiz.service.FundBizTranscationFacade;
import com.yeepay.g3.facade.fundbiz.service.FundQueryFacade;
import com.yeepay.g3.facade.lmlc.account.dto.result.FixedTimeAccountResultDto;
import com.yeepay.g3.facade.lmlc.account.service.FixedTimeAccountFacade;
import com.yeepay.g3.facade.lmlc.trust.dto.api.ProductsForWXResultDto;
import com.yeepay.g3.facade.lmlc.trust.dto.api.TrustOrderParamDto;
import com.yeepay.g3.facade.lmlc.trust.dto.api.TrustOrderResultDto;
import com.yeepay.g3.facade.lmlc.trust.dto.api.TrustOrderResultPageDto;
import com.yeepay.g3.facade.lmlc.trust.dto.product.ProductDetailResultDto;
import com.yeepay.g3.facade.lmlc.trust.enumtype.ProductStatusEnum;
import com.yeepay.g3.facade.lmlc.trust.enumtype.TradeOrderStatusEnum;
import com.yeepay.g3.facade.lmlc.trust.service.FiQueryFacade;
import com.yeepay.g3.facade.lmlc.trust.service.api.TrustQueryFacade;
import com.yeepay.g3.facade.lmportal.dto.AccountInfoQueryResultDto;
import com.yeepay.g3.facade.lmportal.dto.BankCardInfoDto;
import com.yeepay.g3.facade.lmportal.dto.MemberDto;
import com.yeepay.g3.facade.lmportal.service.ChannelFacade;
import com.yeepay.g3.facade.lmportal.service.LPQueryFacade;
import com.yeepay.g3.facade.lmportal.service.LanmaoDemandFacade;
import com.yeepay.g3.facade.lmportal.service.MemberManagementFacade;
import com.yeepay.g3.facade.zt.facade.ZtPolicyOrderDetailFacade;
import com.yeepay.g3.facade.zt.facade.ZtPolicyYieldRateFacade;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.g3.utils.config.ConfigParam;
import com.yeepay.g3.utils.config.ConfigurationUtils;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;
import com.yeepay.g3.utils.web.emvc.annotation.Param;

@Controller
public class HomeAction extends BaseAction{
	
	@Autowired
	private UserBiz userBizImpl;
	
	protected TrustQueryFacade trustQueryFacade = RemoteServiceFactory
			.getService(TrustQueryFacade.class);
	protected MemberManagementFacade memberManagementFacade = RemoteServiceFactory
			.getService(MemberManagementFacade.class);
	protected FundQueryFacade fundQueryFacade = RemoteServiceFactory
			.getService(FundQueryFacade.class);
	protected LPQueryFacade lpQueryFacade = RemoteServiceFactory
			.getService(LPQueryFacade.class);
	protected ChannelFacade channelFacade = RemoteServiceFactory.getService(ChannelFacade.class);
	private static final Logger logger = LoggerFactory
			.getLogger(HomeAction.class);
	protected ActivityUserInfoFacade activityUserInfoFacade = RemoteServiceFactory
			.getService(ActivityUserInfoFacade.class);
	protected ActivityUserMessageFacade activityUserMessageFacade = RemoteServiceFactory
			.getService(ActivityUserMessageFacade.class);
	protected UserFacade userFacade = RemoteServiceFactory
			.getService(UserFacade.class);
	protected LanmaoDemandFacade lanmaoDemandFacade = RemoteServiceFactory
			.getService(LanmaoDemandFacade.class);
	protected FundBizTranscationFacade fundBizTranscationFacade = RemoteServiceFactory
			.getService(FundBizTranscationFacade.class);
	protected FixedTimeAccountFacade fixedTimeAccountFacade = RemoteServiceFactory
			.getService(FixedTimeAccountFacade.class);
	protected  FiQueryFacade fiQueryFacade=RemoteServiceFactory.getService(FiQueryFacade.class);
	protected LMFundInfoQueryServiceFacade lMFundInfoQueryServiceFacade = RemoteServiceFactory
			.getService(LMFundInfoQueryServiceFacade.class);
	
	protected LMFundTradeQueryFacade lMFundTradeQueryFacade = RemoteServiceFactory.getService(LMFundTradeQueryFacade.class);
//	protected ZtPolicyYieldRateFacade ztPolicyYieldRateFacade = RemoteServiceFactory.getService(ZtPolicyYieldRateFacade.class);
	
//	protected ZtPolicyYieldRateFacade ztPolicyYieldRateFacade = RemoteServiceFactory.getService("http://localhost:8080/activity-hessian/hessian", RemotingProtocol.HESSIAN,ZtPolicyYieldRateFacade.class);
	protected ZtPolicyYieldRateFacade ztPolicyYieldRateFacade = RemoteServiceFactory.getService(ZtPolicyYieldRateFacade.class);
	
	protected ZtPolicyOrderDetailFacade ztPolicyOrderDetailFacade = RemoteServiceFactory.getService(ZtPolicyOrderDetailFacade.class);
	
	/**
	 * APP跳转购买理财首页入口
	 * @param loginName
	 * @param pwd
	 * @param srcNo
	 * @return
	 */
	public String toHom(@Param("loginName") String loginName,
			@Param("srcNo") String srcNo,@Param("userSessionKey")String userSessionKey){
		        //获取session和request 不能设置为全局
				HttpSession session = getMvcFacade().getHttpSession();
				HttpServletResponse response = getMvcFacade().getResponse();
				MemberDto memberDto=null;
				String lastestRateForSevenDay = null;
				LoginResultDTO loginResultDto=null;
				List<ProductsForWXResultDto> resultList = new ArrayList<ProductsForWXResultDto>();
				List<ProductsForWXResultDto> list=null;
				List<ProductsForWXResultDto> shortProductList=new ArrayList<ProductsForWXResultDto>();
				
				//清空app用户存储在服务器的session
				memberDto=(MemberDto) session.getAttribute("member");
				if(null!=memberDto){
					session.removeAttribute("member");
				}
				//1.查询生财宝产品七日年化率
				try {
					List<FundbizRateInfo> fundbizRateInfoList = fundQueryFacade
							.queryRateInfos(1);
					if (fundbizRateInfoList != null && fundbizRateInfoList.size() != 0) {
						FundbizRateInfo fundbizRateInfo = fundbizRateInfoList.get(0);
						lastestRateForSevenDay = fundbizRateInfo.getRate() == null ? "0"
								: fundbizRateInfo.getRate().substring(0,5);
					}
				} catch (Exception e) {
					logger.error("[toAppPopularize] info={},ERROR={}", "购买理财首页查询生财宝时异常",
							e.getMessage());
					lastestRateForSevenDay="0";
				}
				addModelObject("lastestRateForSevenDay",lastestRateForSevenDay);
				
				//2.查询信托理财产品和新手标产品
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
					logger.error("[toAppPopularize] info={},ERROR={}", "查询固收理财产品时异常",
							e.getMessage());
				}
				logger.info("[toAppPopularize] shortProductList={}",shortProductList.toString());
				addModelObject("shortProductList",shortProductList);
				addModelObject("productList", resultList);
				
		try{
			loginResultDto=userBizImpl.obtainLogin(loginName,srcNo,userSessionKey);
		}catch(Exception e){
			logger.error("[toAppPopularize] info={},ERROR={}", "购买理财首页验证用户是否登录时异常",
					e.getMessage());
		}
		if(null!=loginResultDto&&loginResultDto.getResultMsg()==LoginResultEnum.SUCCESS) {
			//将memberDto存入session 方便h5页面中获取
			memberDto=loginResultDto.getMemberDto();
			session.setAttribute("member", memberDto);
			// 说明与公众号首次交互 弹出蒙层
				Cookie isFirstPopularize=getMvcFacade().getCookie(MLANMAO_IS_FIRST_Popularize);
				//如果isFirstPopularize不存在 说明没有和购买理财首页交互过
				if (isFirstPopularize==null) {
					Cookie firstPopularize=new Cookie(MLANMAO_IS_FIRST_Popularize,"NO");
					firstPopularize.setMaxAge(-1);
					firstPopularize.setPath("/");
					response.addCookie(firstPopularize);
					addModelObject("first", "firstAction");
				}else {
					addModelObject("first", "secondAction");
				}
				BankCardInfoDto bankCardInfoDto = null;
				try {
					bankCardInfoDto = lpQueryFacade
							.obtainDefaultBankCardInfo(memberDto.getMemberNo());
				} catch (Exception e) {
					logger.error("[toPopularize] info={},ERROR={}", "购买理财首页查询用户绑卡信息时异常",
							e.getMessage());
					bankCardInfoDto=null;
				}
				// 说明未绑卡
				if (bankCardInfoDto == null) {
					addModelObject("flag", "unBankCard");
				} else {
					// 是否是新手 判断是否投资过
					String newcomerFlag = (String) session
							.getAttribute("newcomerFlag");
					if (!StringUtils.isEmpty(newcomerFlag)&&"YES".equals(newcomerFlag)) {
						addModelObject("flag", "unBuy");
					}else{
						addModelObject("flag", "old");
					}
				}
				//用户的幸运值
				ActivityUserInfoDTO activityUserInfo = activityUserInfoFacade
						.selectUserInfoByMemberNo(memberDto.getMemberNo());
				addModelObject("activityUserInfo", activityUserInfo);
				session.setAttribute("member", memberDto);
		}else{
			//未登录
			addModelObject("flag","unLogin");
			Cookie isFirstIn=getMvcFacade().getCookie(MLANMAO_IS_FIRST_IN);
			//如果isFirstPopularize不存在 说明没有和购买理财首页交互过
			if (isFirstIn==null) {
				Cookie firstIn=new Cookie(MLANMAO_IS_FIRST_IN,"NO");
				firstIn.setMaxAge(30*60);
				firstIn.setPath("/");
				response.addCookie(firstIn);
				addModelObject("firstIn", "firstIn");
			}else {
				addModelObject("firstIn", "secondIn");
			}
		} 
		return SUCCESS;
	}

	
	public String newToHome(@Param("loginName") String loginName,
			@Param("srcNo") String srcNo,@Param("userSessionKey")String userSessionKey){
		logger.info("[toHome] loginName={},srcNo={},userSessionKey={}",loginName,srcNo,userSessionKey);
		/**定期理财七日年化率**/
		String lastestRateForSevenDay=null;
		List<ProductsForWXResultDto> list=null;
		LoginResultDTO loginResultDto=null;
		HttpSession session = getMvcFacade().getHttpSession();
		//给活期理财页面标识这个用户目前是从APP客户端访问的，为了不去跳绑卡中间页
		session.setAttribute("platform", "APP");
		addModelObject("platform","APP");
		logger.info("[toHome] platform={}",session.getAttribute("platform"));
		//清空app用户存储在服务器的session
		MemberDto memberDto=(MemberDto) session.getAttribute("member");
		if(null!=memberDto){
			//清除登录信息
			session.removeAttribute("member");
		}
		try{
			loginResultDto=userBizImpl.obtainLogin(loginName,srcNo,userSessionKey);
		}catch(Exception e){
			logger.error("[toHome] info={},ERROR={}", "购买理财首页验证用户是否登录时异常",
					e.getMessage());
		}
		if(null!=loginResultDto&&loginResultDto.getResultMsg()==LoginResultEnum.SUCCESS) {
			//将memberDto存入session 方便h5页面中获取
			memberDto=loginResultDto.getMemberDto();
			session.setAttribute("member", memberDto);
		}
		/*//查询是否是新手 
		try{
			TrustOrderParamDto trustOrderParamDto = new TrustOrderParamDto();
			trustOrderParamDto.setMemberNo(memberDto.getMemberNo());
			trustOrderParamDto.setStatus(TradeOrderStatusEnum.SUCCESS);
			TrustOrderResultPageDto trustOrderResultPageDTO = trustQueryFacade.queryTrustOrder(trustOrderParamDto);
			logger.info("[login] trustOrderResultPageDTO={}",trustOrderResultPageDTO);
			List<TrustOrderResultDto> trustList = trustOrderResultPageDTO.getTrustOrderResult();
			logger.info("[login] trustList={}",trustList);
			if(trustList.size() == 0){
				session.setAttribute("newcomerFlag", "YES");
			}else{
				session.setAttribute("newcomerFlag", "NO");
			}
			logger.info("[toHome] newcomerFlag={}",session.getAttribute("newcomerFlag"));
		}catch(Exception e){
			logger.info("[toHome] info={},ERROR={}","查询信托购买记录失败，新手标志设置失败",e);
		}*/
		
		//1.查询生财宝产品七日年化率
		try {
			List<FundbizRateInfo> fundbizRateInfoList = fundQueryFacade
					.queryRateInfos(1);
			if (fundbizRateInfoList != null && fundbizRateInfoList.size() != 0) {
				FundbizRateInfo fundbizRateInfo = fundbizRateInfoList.get(0);
				lastestRateForSevenDay = fundbizRateInfo.getRate() == null ? "0"
						: fundbizRateInfo.getRate().substring(0,5);
			}
		} catch (Exception e) {
			logger.error("[toHome] info={},ERROR={}", "购买理财首页查询生财宝时异常",
					e.getMessage());
			lastestRateForSevenDay="0";
		}
		addModelObject("lastestRateForSevenDay",lastestRateForSevenDay);
		//2.查询信托理财产品和新手标产品
		try {
			list = trustQueryFacade.queryProductsForWX();
			if (list != null && list.size() != 0) {
				for (ProductsForWXResultDto productsForWXResultDto : list) {
					if("FRESHMAN".equals(productsForWXResultDto.getChannelColumn())){
						long freshmanId=productsForWXResultDto.getProductId();
						addModelObject("FRESHMANID",freshmanId);
					}
					if (productsForWXResultDto.getStatus() == ProductStatusEnum.SALING
							&& "FIXED".equals(productsForWXResultDto
									.getChannelColumn())
							&& "MONY".equals(productsForWXResultDto
									.getFirstColumn())
							&& "30D".equals(productsForWXResultDto
									.getSecondColumn())) {
						if(null!=productsForWXResultDto){
						addModelObject("id", productsForWXResultDto.getProductId());
						addModelObject("termDay", productsForWXResultDto.getTermDay());
						addModelObject("yearRate", productsForWXResultDto.getYearRate());
						addModelObject("surplusAmount", productsForWXResultDto.getSurplusAmount());
						}
						logger.info("productsForWXResultDto:"+productsForWXResultDto);
					}
				}
			}
		} catch (Exception e) {
			logger.error("[toHome] info={},ERROR={}", "查询固收理财产品时异常",
					e.getMessage());
		}
		
		//3.加密用户编码
		String ascMemberNo = "";
		if(null != (MemberDto) session.getAttribute("member")) {
			ascMemberNo =  SecretUtils.secretData(memberDto.getMemberNo());
		}
		//获取基金的url
		String fundSalesUrl = GetParamUtils.getfundSalesUrl();
		// 格式化
		addModelObject("fundSalesUrl", fundSalesUrl);// 基金的域名地址
		addModelObject("ascMemberNo", ascMemberNo);// 加密的用户编码
		
		//4.基金的最高年涨幅
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
		try {
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
		logger.info("[toHome] platfrom={}",session.getAttribute("platform"));
		return SUCCESS;
	}
	
	public String toHome(@Param("loginName") String loginName,
			@Param("srcNo") String srcNo,@Param("userSessionKey")String userSessionKey){
		logger.info("[toHome] loginName={},srcNo={},userSessionKey={}",loginName,srcNo,userSessionKey);
		//获取公告开关统一配置
		Map<String,String> scbSwitchMap = new LinkedHashMap<String,String>();
		try{
			scbSwitchMap = GetParamUtils.readScbSwitchConfig();
		}catch(Exception e){
			logger.info("[toPopularize] info={},ERROR={}","公告开关统一配置获取失败",e);
		}
		addModelObject("scbSwitchMap", scbSwitchMap);
		/**定期理财七日年化率**/
		String lastestRateForSevenDay=null;//生财宝七日年华收益率
		String perRateForSevenDay = "0";
		String lateRateForSevenday = "0";
		List<ProductsForWXResultDto> list=null;//信托理财产品列表查询
		LoginResultDTO loginResultDto=null;
		List<ProductsForWXResultDto> shortProductList = new ArrayList<ProductsForWXResultDto>();//超短期产品列表
		List<ProductsForWXResultDto> fixedPoductList = new ArrayList<ProductsForWXResultDto>();//定期理财产品列表
		List<LMFundBasicInfoDTO> fundProductList = new ArrayList<LMFundBasicInfoDTO>();//基金产品列表
		Map<String,Object> resultMap = new LinkedHashMap<String,Object>();//用来存储智能投资的最大和最小回报率
		
		HttpSession session = getMvcFacade().getHttpSession();
		//给活期理财页面标识这个用户目前是从APP客户端访问的，为了不去跳绑卡中间页
		session.setAttribute("platform", "APP");
		addModelObject("platform","APP");
		logger.info("[toHome] platform={}",session.getAttribute("platform"));
		//清空app用户存储在服务器的session
		MemberDto memberDto=(MemberDto) session.getAttribute("member");
		if(null!=memberDto){
			//清除登录信息
			session.removeAttribute("member");
		}
		try{
			loginResultDto=userBizImpl.obtainLogin(loginName,srcNo,userSessionKey);
		}catch(Exception e){
			logger.error("[toHome] info={},ERROR={}", "购买理财首页验证用户是否登录时异常",
					e.getMessage());
		}
		//查询秒杀产品 TODO
		ProductDetailResultDto productDetailResultDto = null;
		try{
			productDetailResultDto =  fiQueryFacade.obtainSeckillProductDetail();
			logger.info("[toPopularize] productDetailResultDto={}",productDetailResultDto);
			
		}catch(Exception e){
			logger.error("[toPopularize] info={},ERROR={}","查询秒杀产品失败",e);
		}
		addModelObject("productDetailResultDto", productDetailResultDto);
		//判断用户是否在平台交易过
		//登录状态
		if(null != loginResultDto && LoginResultEnum.SUCCESS.equals(loginResultDto.getResultMsg())) {
			//将memberDto存入session 方便h5页面中获取
			memberDto=loginResultDto.getMemberDto();
			if(memberDto != null && memberDto.getMemberNo() != null){
				session.setAttribute("member", memberDto);
				try{
					//新手标志。yes表示是新手显示新手专区，no表示不是新手，不显示新手专区
					logger.info("[toHome] info={}","查询信托交易记录，判断是否显示新手标");
					//判断是否投资过
					//信托购买记录存在，则不显示新手标
					TrustOrderParamDto trustOrderParamDto = new TrustOrderParamDto();
					trustOrderParamDto.setMemberNo(memberDto.getMemberNo());
					trustOrderParamDto.setStatus(TradeOrderStatusEnum.SUCCESS);
					TrustOrderResultPageDto trustOrderResultPageDTO = trustQueryFacade.queryTrustOrder(trustOrderParamDto);
					List<TrustOrderResultDto> truestOrderlist = trustOrderResultPageDTO.getTrustOrderResult();
					//没有信托理财记录，则查询是否有基金交易记录
					if(truestOrderlist == null || truestOrderlist.size() == 0){
						logger.info("[toHome] memberNo={},info1={},info2={}",memberDto.getMemberNo(),"该用户没有信托交易记录","查询基金交易记录");
						//基金购买记录  
						//如果没有基金购买记录，显示新手专区，有记录，不显示新手专区
						UserTradeQueryRequestParam userTradeQueryRequestParam = new UserTradeQueryRequestParam();
						//交易类型为申购
						List<FundTradeTypeEnum> tradeTypes = new ArrayList<FundTradeTypeEnum>();
						tradeTypes.add(FundTradeTypeEnum.PURCHASE);
						//状态为申购确认成功
						List<FundTradeStatusEnum> tradeStatus = new ArrayList<FundTradeStatusEnum>();
						tradeStatus.add(FundTradeStatusEnum.CONFRIMSUCCESS);
						userTradeQueryRequestParam.setMemberNo(memberDto.getMemberNo());
						userTradeQueryRequestParam.setTradeTypes(tradeTypes);
						userTradeQueryRequestParam.setTradeStatus(tradeStatus);
						UserTradeQueryResultDto  userTradeQueryResultDto= lMFundTradeQueryFacade.querUserTradeInfo(userTradeQueryRequestParam);
						//接口调用成功
						if(userTradeQueryResultDto != null && "0".equals(userTradeQueryResultDto.getCode())){
							//没有基金交易记录
							if(userTradeQueryResultDto.getCount() == 0){
								addModelObject("isNew", "YES");
							}else{
								addModelObject("isNew", "NO");
							}
						}else{
							//接口调用失败，显示新手标,且不存入session，下次进入时重新查询
							logger.info("[toHome] info={}","查询用户基金交易记录表接口调用失败");
							addModelObject("isNew", "NO");
						}
					}else{
						//有信托记录，不查询基金记录，不显示新手标
						addModelObject("isNew", "NO");
					}
				}catch(Exception e){
					logger.error("[toHome] info={}，ERROR={}","查询用户新手标志失败");
				}
				//判断用户是否买过只能投资
				try{
					int count = ztPolicyOrderDetailFacade.queryCountPurOrderByMemberNo(memberDto.getMemberNo());
					if(count == 0){
						addModelObject("ztUrl", "zt/introduce/brand");
					}else{
						addModelObject("ztUrl", "zt/introduce/sceneList");
					}
				}catch(Exception e){
					logger.error("[toHome] info={},ERROR={}","查询用户是否申购智能投资异常",e);
					addModelObject("ztUrl","zt/introduce/brand");
				}
			}else{
				logger.info("[toHome] info={}","用户未登录");
				//判断登录状态接口中返回的用户信息不正确，显示新手专区，并且不放入session，下次访问时重新判断
				addModelObject("isNew", "YES");
				addModelObject("ztUrl","zt/introduce/brand");
			}
		}else{
			logger.info("[toHome] info={},loginResultDto={}","判断用户登录接口失败",loginResultDto);
			//未登录状态，显示新手专区，并且不放入session，下次访问时重新判断
			addModelObject("isNew", "YES");
			addModelObject("ztUrl","zt/introduce/brand");
		}
		//1.查询生财宝产品七日年化率
		try {
			List<FundbizRateInfo> fundbizRateInfoList = fundQueryFacade
					.queryRateInfos(1);
			if (fundbizRateInfoList != null && fundbizRateInfoList.size() != 0) {
				FundbizRateInfo fundbizRateInfo = fundbizRateInfoList.get(0);
				lastestRateForSevenDay = fundbizRateInfo.getRate() == null ? "0"
						: fundbizRateInfo.getRate().substring(0,5);
				String[] rateForSevenDay = lastestRateForSevenDay.split("\\.");
				//带小数点的年华收益率
				if(rateForSevenDay.length == 2){
					perRateForSevenDay = rateForSevenDay[0];
					lateRateForSevenday	= rateForSevenDay[1];
				}else{
					perRateForSevenDay = rateForSevenDay[0];
				}
			}
		} catch (Exception e) {
			logger.error("[toHome] info={},ERROR={}", "购买理财首页查询生财宝时异常",
					e.getMessage());
		}
		addModelObject("perRateForSevenDay", perRateForSevenDay);
		addModelObject("lateRateForSevenday", lateRateForSevenday);
		//2.查询定期理财产品列表
		try{
			list = trustQueryFacade.queryProductsForWX();
			if(list != null  && list.size() != 0){
				for(ProductsForWXResultDto productsForWXResultDto : list){
					//新手标
					if("FRESHMAN".equals(productsForWXResultDto.getChannelColumn())){
						addModelObject("FRESHMANDTO",productsForWXResultDto);
					}
					//在售的信托产品
					if(productsForWXResultDto.getStatus() == ProductStatusEnum.SALING && "FIXED".equals(productsForWXResultDto.getChannelColumn())){
						//超短期
						if("SHORT".equals(productsForWXResultDto.getFirstColumn())){
							shortProductList.add(productsForWXResultDto);
						}else{
							//定期只显示30天、45天、90天
							if("30D".equals(productsForWXResultDto.getSecondColumn())){
								fixedPoductList.add(productsForWXResultDto);
							}
							/*if("45D".equals(productsForWXResultDto.getSecondColumn())){
								fixedPoductList.add(productsForWXResultDto);
							}*/
							if("90D".equals(productsForWXResultDto.getSecondColumn())){
								fixedPoductList.add(productsForWXResultDto);
							}
						}
					}
				}
				
				
			}
		}catch(Exception e){
			logger.error("[toHome] info={},error={}","查询信托理财产品列表时异常",e);
		}
		logger.info("[toHome] shortProductList={},fixedSze={},fixedPoductList={}",shortProductList,fixedPoductList.size(),fixedPoductList);
		//当产品查询异常时，list中没有数据
		addModelObject("shortProductList", shortProductList);
		addModelObject("fixedPoductList", fixedPoductList);
		
		//3.查询基金产品列表
		try{
			//（1）加密用户编码
			String ascMemberNo = null ;
			if(null != memberDto) {
				ascMemberNo =  SecretUtils.secretData(memberDto.getMemberNo());
			}
			//（2）获取基金的url
			String fundSalesUrl = GetParamUtils.getfundSalesUrl();
			addModelObject("fundSalesUrl", fundSalesUrl);// 基金的域名地址
			addModelObject("ascMemberNo", ascMemberNo);// 加密的用户编码
			//(3)查询统一配置，基金code列表
			ConfigParam configParam = ConfigurationUtils.getConfigParam("config_type_text_resources", "lmapp_fund_show_code");
			List<String> fundCodeList = (List<String>) configParam.getValue();
			logger.info("[toHome] fundCodeList={}",fundCodeList);
			
			//（4）根据code查询基金详细信息，添加到基金列表中
			FundQueryParamDTO fundQueryParamDTO = new FundQueryParamDTO();
			fundQueryParamDTO.setMerNo(GetParamUtils.getFundPlatNo());
			if (StringUtils.isEmpty(fundQueryParamDTO.getHeat())) {
				fundQueryParamDTO.setHeat(null);
			}
			fundQueryParamDTO.setFundType(FundTypeEnum.ALL);
			//排序默认设为年涨幅
			if (null == fundQueryParamDTO.getListFundEnum()) {
				fundQueryParamDTO.setListFundEnum(ListFundEnum.YEAR_INCREASE);
			}
			for(int i = 0 ; i < fundCodeList.size() ; i ++){
				fundQueryParamDTO.setSearchCondi(fundCodeList.get(i));
				FundInfoQueryResultPageDTO fundInfoQueryResultPageDto = lMFundInfoQueryServiceFacade.queryFundList(fundQueryParamDTO);
				logger.info("[toHome] fundInfoQueryResultPageDto={}",fundInfoQueryResultPageDto);
				if(fundInfoQueryResultPageDto != null && fundInfoQueryResultPageDto.getFundBasicInfoDto() != null && fundInfoQueryResultPageDto.getFundBasicInfoDto().size() != 0){
					//只会有一条数据
					LMFundBasicInfoDTO lMFundBasicInfoDto = fundInfoQueryResultPageDto.getFundBasicInfoDto().get(0);
					LmFundBasicInfoDTO lmFundBasicInfoDTO = new LmFundBasicInfoDTO();
					lmFundBasicInfoDTO = EntityDtoUtils.toTarget(lMFundBasicInfoDto, LmFundBasicInfoDTO.class);
					lmFundBasicInfoDTO.setFundType(lMFundBasicInfoDto.getFundType());
					BigDecimal yearIncrease = BigDecimal.ZERO;
					if(null != lmFundBasicInfoDTO.getYearIncrease() && !"".equals(lmFundBasicInfoDTO.getYearIncrease())){
						//年涨幅大于等于0,符号
						if(BigDecimal.ZERO.compareTo(lmFundBasicInfoDTO.getYearIncrease()) != 1){
							lmFundBasicInfoDTO.setSign("+");
							yearIncrease = lmFundBasicInfoDTO.getYearIncrease().setScale(2, BigDecimal.ROUND_HALF_UP);
						}else{
							lmFundBasicInfoDTO.setSign("-");
							yearIncrease = lmFundBasicInfoDTO.getYearIncrease().multiply(new BigDecimal(-1)).setScale(2, BigDecimal.ROUND_HALF_UP);
						}
						//取整数部分
						/*
						int perYear = (int) Math.floor(yearIncrease.doubleValue());
						BigDecimal s = new BigDecimal(perYear);
						//取小数部分
						int lateYaer = yearIncrease.subtract(s).multiply(new BigDecimal(100)).intValue();*/
						String[] yearIncreaseStr = yearIncrease.toString().split("\\.");
						if(yearIncreaseStr.length == 2){
							lmFundBasicInfoDTO.setPerYearIncrease(yearIncreaseStr[0]);
							lmFundBasicInfoDTO.setLateYearIncrease(yearIncreaseStr[1]);
						}else{
							lmFundBasicInfoDTO.setPerYearIncrease(yearIncreaseStr[0]);
							lmFundBasicInfoDTO.setLateYearIncrease("0");
						}
					}else{
						lmFundBasicInfoDTO.setYearIncrease(BigDecimal.ZERO);
						lmFundBasicInfoDTO.setSign("+");
						lmFundBasicInfoDTO.setPerYearIncrease("0");
						lmFundBasicInfoDTO.setLateYearIncrease("0");
					}
					logger.info("[toHome] lmFundBasicInfoDTO={}",lmFundBasicInfoDTO);
					fundProductList.add(lmFundBasicInfoDTO);
				}
			}
		}catch(Exception e){
			logger.error("[toHome] info={},error={}","查询基金信息时异常",e);
		}
		logger.info("[toHome] fundProductList={}",fundProductList);
		addModelObject("fundProductList", fundProductList);
		//4.查询智能投资回报率区间 TODO
		try{
			//条件：近1年回报率，策略每月定投
			Map<String,Object> map = ztPolicyYieldRateFacade.queryRateInterval(1, "POLICY_FIXED_INVEST");
			logger.info("[toHome] 回报率map={}",map);
			BigDecimal minRate = (BigDecimal) map.get("MIN_RATE");
			BigDecimal maxRate = (BigDecimal) map.get("MAX_RATE");
			if(minRate == null){
				minRate = BigDecimal.ZERO;
			}
			if(maxRate == null){
				maxRate = BigDecimal.ZERO;
			}
			//大于等于0
			if(BigDecimal.ZERO.compareTo(minRate) != 1){
				resultMap.put("minRateSign", "+");
			}else{
				minRate = minRate.multiply(new BigDecimal(-1));
				resultMap.put("minRateSign", "-");
			}
			BigDecimal minYiledRate = minRate.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);//乘以100，保留两位小数
			String[] minRateStrs = minYiledRate.toString().split("\\.");
			resultMap.put("perMinRate", minRateStrs[0]);
			resultMap.put("lateMinRate", minRateStrs[1]);
			if(BigDecimal.ZERO.compareTo(maxRate) != 1){
				resultMap.put("maxRateSign", "+");
			}else{
				maxRate = maxRate.multiply(new BigDecimal(-1));
				resultMap.put("maxRateSign", "-");
			}
			BigDecimal maxYiledRate = maxRate.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);//乘以100，保留两位小数
			String[] maxRateStrs = maxYiledRate.toString().split("\\.");
			resultMap.put("perMaxRate", maxRateStrs[0]);
			resultMap.put("lateMaxRate", maxRateStrs[1]);
		}catch(Exception e){
			logger.error("[toPopularize] info={},ERROR={}","查询智能投资回报率区间失败",e);
		}
		addModelObject("ztRateMap", resultMap);
		logger.info("[toHome] platfrom={}",session.getAttribute("platform"));
		return SUCCESS;
	}
	
	/**
	 * @Title: toRobotAdvisor 
	 * @Description: 跳转灵机一投
	 * @param
	 * @throws
	 */
	public String toRobotAdvisor(@Param("loginName") String loginName,
			@Param("srcNo") String srcNo,
			@Param("userSessionKey")String userSessionKey){
		return SUCCESS;
	}
}
