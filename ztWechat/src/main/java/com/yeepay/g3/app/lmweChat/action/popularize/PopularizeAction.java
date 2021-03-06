package com.yeepay.g3.app.lmweChat.action.popularize;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lanmao.fund.facade.fundbiz.dto.FundbizRateInfo;
import com.lanmao.fund.facade.fundsales.dto.UserTradeQueryRequestParam;
import com.lanmao.fund.facade.fundsales.dto.UserTradeQueryResultDto;
import com.lanmao.fund.facade.fundsales.enumtype.FundTradeStatusEnum;
import com.lanmao.fund.facade.fundsales.enumtype.FundTradeTypeEnum;
import com.lanmao.fund.facade.fundsales.service.LMFundInfoQueryServiceFacade;
import com.lanmao.fund.facade.fundsales.service.LMFundTradeQueryFacade;
import com.lanmao.fund.facade.queryservice.dto.FundInfoQueryResultPageDTO;
import com.lanmao.fund.facade.queryservice.dto.FundQueryParamDTO;
import com.lanmao.fund.facade.queryservice.dto.LMFundBasicInfoDTO;
import com.lanmao.fund.facade.queryservice.enumtype.FundTypeEnum;
import com.lanmao.fund.facade.queryservice.enumtype.ListFundEnum;
import com.yeepay.g3.app.lmweChat.action.BaseAction;
import com.yeepay.g3.app.lmweChat.dto.LmFundBasicInfoDTO;
import com.yeepay.g3.app.lmweChat.service.RequestParamBuilderService;
import com.yeepay.g3.app.lmweChat.utils.EntityDtoUtils;
import com.yeepay.g3.app.lmweChat.utils.GetParamUtils;
import com.yeepay.g3.app.lmweChat.utils.HttpRequestUtils;
import com.yeepay.g3.app.lmweChat.utils.LmConstants;
import com.yeepay.g3.app.lmweChat.utils.SecretUtils;
import com.yeepay.g3.facade.activity.dto.ActivityUserInfoDTO;
import com.yeepay.g3.facade.activity.enums.ShareBizTypeEnum;
import com.yeepay.g3.facade.activity.facade.ActivityUserInfoFacade;
import com.yeepay.g3.facade.fundbiz.service.FundQueryFacade;
import com.yeepay.g3.facade.lmlc.trust.dto.api.ProductsForWXResultDto;
import com.yeepay.g3.facade.lmlc.trust.dto.api.TrustOrderParamDto;
import com.yeepay.g3.facade.lmlc.trust.dto.api.TrustOrderResultDto;
import com.yeepay.g3.facade.lmlc.trust.dto.api.TrustOrderResultPageDto;
import com.yeepay.g3.facade.lmlc.trust.dto.product.ProductDetailResultDto;
import com.yeepay.g3.facade.lmlc.trust.enumtype.ProductStatusEnum;
import com.yeepay.g3.facade.lmlc.trust.enumtype.TradeOrderStatusEnum;
import com.yeepay.g3.facade.lmlc.trust.service.FiQueryFacade;
import com.yeepay.g3.facade.lmlc.trust.service.api.TrustQueryFacade;
import com.yeepay.g3.facade.lmportal.dto.BankCardInfoDto;
import com.yeepay.g3.facade.lmportal.dto.ChannelDto;
import com.yeepay.g3.facade.lmportal.dto.MemberDto;
import com.yeepay.g3.facade.lmportal.dto.MemberRelevanceDto;
import com.yeepay.g3.facade.lmportal.enumtype.ChannelTypeEnum;
import com.yeepay.g3.facade.lmportal.enumtype.MemberRelevanceStatusEnum;
import com.yeepay.g3.facade.lmportal.enumtype.MemberStatusEnum;
import com.yeepay.g3.facade.lmportal.service.ChannelFacade;
import com.yeepay.g3.facade.lmportal.service.LPQueryFacade;
import com.yeepay.g3.facade.lmportal.service.MemberManagementFacade;
import com.yeepay.g3.facade.zt.facade.ZtPolicyOrderDetailFacade;
import com.yeepay.g3.facade.zt.facade.ZtPolicyYieldRateFacade;
import com.yeepay.g3.utils.common.encrypt.AES;
import com.yeepay.g3.utils.common.encrypt.Digest;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.g3.utils.config.ConfigParam;
import com.yeepay.g3.utils.config.ConfigurationUtils;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;
import com.yeepay.g3.utils.web.emvc.annotation.Param;

/**
 * 理财列表首页功能
 * 
 * @Copyright: Copyright (c)2011
 * @company 懒猫金服
 * @author ping.zhu
 * @since 2016-3-14
 * @version 1.0
 */
@Controller
public class PopularizeAction extends BaseAction {

	private FiQueryFacade fiQueryFacade = RemoteServiceFactory
			.getService(FiQueryFacade.class);
	protected TrustQueryFacade trustQueryFacade = RemoteServiceFactory
			.getService(TrustQueryFacade.class);
	protected MemberManagementFacade memberManagementFacade = RemoteServiceFactory
			.getService(MemberManagementFacade.class);
	protected FundQueryFacade fundQueryFacade = RemoteServiceFactory
			.getService(FundQueryFacade.class);
	protected LPQueryFacade lpQueryFacade = RemoteServiceFactory
			.getService(LPQueryFacade.class);
	protected ChannelFacade channelFacade = RemoteServiceFactory.getService(ChannelFacade.class);
	@Autowired
	protected RequestParamBuilderService requestParamBuilderService;
	private static final Logger logger = LoggerFactory
			.getLogger(PopularizeAction.class);
	protected ActivityUserInfoFacade activityUserInfoFacade = RemoteServiceFactory
			.getService(ActivityUserInfoFacade.class);

	protected LMFundInfoQueryServiceFacade lMFundInfoQueryServiceFacade = RemoteServiceFactory
			.getService(LMFundInfoQueryServiceFacade.class);
	protected LMFundTradeQueryFacade lMFundTradeQueryFacade = RemoteServiceFactory.getService(LMFundTradeQueryFacade.class);
	
	protected ZtPolicyYieldRateFacade ztPolicyYieldRateFacade = RemoteServiceFactory.getService(ZtPolicyYieldRateFacade.class);
	
	protected ZtPolicyOrderDetailFacade ztPolicyOrderDetailFacade = RemoteServiceFactory.getService(ZtPolicyOrderDetailFacade.class);
	
//	protected ZtPolicyYieldRateFacade ztPolicyYieldRateFacade = RemoteServiceFactory.getService("http://localhost:8080/activity-hessian/hessian", RemotingProtocol.HESSIAN,ZtPolicyYieldRateFacade.class);
	
	/**
	 * 跳转购买理财登录首页或未登录首页
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	/*public String toPopularize() {
		String lastestRateForSevenDay = null;
		HttpServletResponse response=getMvcFacade().getResponse();
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		List<ProductsForWXResultDto> resultList = new ArrayList<ProductsForWXResultDto>();
		List<ProductsForWXResultDto> list=null;
		List<ProductsForWXResultDto> shortProductList=new ArrayList<ProductsForWXResultDto>();
		try {
			List<FundbizRateInfo> fundbizRateInfoList = fundQueryFacade
					.queryRateInfos(1);
			if (fundbizRateInfoList != null && fundbizRateInfoList.size() != 0) {
				FundbizRateInfo fundbizRateInfo = fundbizRateInfoList.get(0);
				lastestRateForSevenDay = fundbizRateInfo.getRate() == null ? "0"
						: fundbizRateInfo.getRate().substring(0,5);
			}
		} catch (Exception e) {
			logger.error("[toPopularize] info={},ERROR={}", "购买理财首页查询生财宝时异常",
					e.getMessage());
			lastestRateForSevenDay="0";
		}
		addModelObject("lastestRateForSevenDay",lastestRateForSevenDay);
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
			logger.error("[queryFixedProcduct] info={},ERROR={}", "查询固收理财产品时异常",
					e.getMessage());
		}
		System.out.println(shortProductList.toString());
		addModelObject("shortProductList",shortProductList);
		addModelObject("productList", resultList);
		//未登录
		if (memberDto == null) {
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
		} else {
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

				
			
		}
		return "success";
	}*/
	public String toPopularize(@Param("recommendMemberNo")String recommendMemberNo,@Param("bizType")ShareBizTypeEnum bizType,@Param("srcNo")String srcNo){
		logger.info("[toPopularize] recommendMemberNo={},bizType={},srcNo={}",recommendMemberNo,bizType,srcNo);
		HttpServletResponse response=getMvcFacade().getResponse();
		HttpSession session = getMvcFacade().getHttpSession();
		//公告开关统一配置
		Map<String,String> scbSwitchMap = new LinkedHashMap<String,String>();
		try{
			scbSwitchMap = GetParamUtils.readScbSwitchConfig();
		}catch(Exception e){
			logger.info("[toPopularize] info={},ERROR={}","公告开关统一配置获取失败",e);
		}
		addModelObject("scbSwitchMap", scbSwitchMap);
		
		String lastestRateForSevenDay = null;//生财宝七日年华收益率
		String perRateForSevenDay = "0";
		String lateRateForSevenday = "0";
		List<ProductsForWXResultDto> list=null;//信托理财产品列表查询
		List<ProductsForWXResultDto> shortProductList = new ArrayList<ProductsForWXResultDto>();//超短期产品列表
		List<ProductsForWXResultDto> fixedPoductList = new ArrayList<ProductsForWXResultDto>();//定期理财产品列表
		List<LMFundBasicInfoDTO> fundProductList = new ArrayList<LMFundBasicInfoDTO>();//基金产品列表
		Map<String,Object> resultMap = new LinkedHashMap<String,Object>();//用来存储智能投资的最大和最小回报率
		//存储推荐人的会员编号，推荐的业务类型，来源编号
		if(!StringUtils.isEmpty(recommendMemberNo) && !"null".equals(recommendMemberNo)){
			session.setAttribute("_recommendMemberNo", recommendMemberNo);
			session.setAttribute("_bizType", bizType);
			session.setAttribute("_srcNo", srcNo);
		}
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		//查询秒杀产品 
//		String seckillFlag = null;
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
		if(memberDto != null && memberDto.getMemberNo() != null){
			try{
				//新手标志。yes表示是新手显示新手专区，no表示不是新手，不显示新手专区
				logger.info("[toPopularize] info={}","查询信托交易记录，判断是否显示新手标");
				//判断是否投资过
				//信托购买记录存在，则不显示新手标
				TrustOrderParamDto trustOrderParamDto = new TrustOrderParamDto();
				trustOrderParamDto.setMemberNo(memberDto.getMemberNo());
				trustOrderParamDto.setStatus(TradeOrderStatusEnum.SUCCESS);
				TrustOrderResultPageDto trustOrderResultPageDTO = trustQueryFacade.queryTrustOrder(trustOrderParamDto);
				List<TrustOrderResultDto> truestOrderlist = trustOrderResultPageDTO.getTrustOrderResult();
				//没有信托理财记录，则查询是否有基金交易记录
				if(truestOrderlist == null || truestOrderlist.size() == 0){
					logger.info("[toPopularize] memberNo={},info1={},info2={}",memberDto.getMemberNo(),"该用户没有信托交易记录","查询基金交易记录");
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
						logger.info("[toPopularize] userTradeQueryResultDto.getCode()={},userTradeQueryResultDto.getCount()={}",userTradeQueryResultDto.getCode(),userTradeQueryResultDto.getCount());
						//无基金交易记录，显示
						if(userTradeQueryResultDto.getCount() == 0){
							addModelObject("isNew", "YES");
						}else{
							addModelObject("isNew", "NO");
						}
					}else{
						//接口调用失败，显示新手标,且不存入session，下次进入时重新查询
						logger.info("[toPopularize] info={}","查询用户基金交易记录表接口调用失败");
						addModelObject("isNew", "YES");
					}
				}else{
					//有信托记录，不查询基金记录，不显示新手标
					addModelObject("isNew", "NO");
				}
			}catch(Exception e){
				logger.error("[toPopularize] info={},ERROR={}","查询信托和基金订单判断新手异常",e);
				addModelObject("isNew", "YES");
			}
			
			//查询用户是否投资过，表5中所有该会员的累计投资额相加
			try{
				int count = ztPolicyOrderDetailFacade.queryCountPurOrderByMemberNo(memberDto.getMemberNo());
				if(count == 0){
					addModelObject("ztUrl", "zt/introduce/brand");
				}else{
					addModelObject("ztUrl", "zt/introduce/sceneList");
				}
			}catch(Exception e){
				logger.error("[toPopularize] info={},ERROR={}","查询智能投资申购订单总数异常",e);
				addModelObject("ztUrl","zt/introduce/brand");
			}
			
		}else{
			addModelObject("isNew", "YES");
			addModelObject("ztUrl","zt/introduce/brand");
		}
		//1.查询生财宝七日年化收益率
		try{
			List<FundbizRateInfo> fundbizRateInfoList = fundQueryFacade.queryRateInfos(1);
			if(fundbizRateInfoList != null && fundbizRateInfoList.size() != 0){
				FundbizRateInfo fundbizRateInfo = fundbizRateInfoList.get(0);
				lastestRateForSevenDay = fundbizRateInfo.getRate() == null ? "0" : fundbizRateInfo.getRate().substring(0,5);
				String[] rateForSevenDay = lastestRateForSevenDay.split("\\.");
				//带小数点的年华收益率
				if(rateForSevenDay.length == 2){
					perRateForSevenDay = rateForSevenDay[0];
					lateRateForSevenday	= rateForSevenDay[1];
				}else{
					perRateForSevenDay = rateForSevenDay[0];
				}
			}
		}catch(Exception e){
			logger.error("[toPopularize] info={},error={}","查询生财宝年化收益率时异常",e);
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
			logger.error("[toPopularize] info={},error={}","查询信托理财产品列表时异常",e);
		}
		logger.info("[toPopularize] shortProductList={},fixedSze={},fixedPoductList={}",shortProductList,fixedPoductList.size(),fixedPoductList);
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
			logger.info("[toPopularize] fundCodeList={}",fundCodeList);
			
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
				logger.info("[toPopularize] fundInfoQueryResultPageDto={}",fundInfoQueryResultPageDto);
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
						/*//取整数部分
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
					logger.info("[toPopularize] lmFundBasicInfoDTO={}",lmFundBasicInfoDTO);
					fundProductList.add(lmFundBasicInfoDTO);
				}
			}
		}catch(Exception e){
			logger.error("[toPopularize] info={},error={}","查询基金信息时异常",e);
		}
		logger.info("[toPopularize] fundProductList={}",fundProductList);
		addModelObject("fundProductList", fundProductList);
		//4.查询智能投资回报率区间 TODO
		try{
			//条件：近1年回报率，策略每月定投
			Map<String,Object> map = ztPolicyYieldRateFacade.queryRateInterval(1, "POLICY_FIXED_INVEST");
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
		logger.info("[toPopularize] resultMap={}",resultMap);
		addModelObject("ztRateMap", resultMap);
		return SUCCESS;
	}
	
	/*public static void main(String[] args){
		BigDecimal str = new BigDecimal(13.56);
		int d = (int) Math.floor(str.doubleValue());
		BigDecimal s = new BigDecimal(d);
		int sa = str.subtract(s).multiply(new BigDecimal(100)).intValue();
		System.out.println(d);
		System.out.println(sa);
	}*/
	public String newToPopularize(@Param("recommendMemberNo")String recommendMemberNo,@Param("bizType")ShareBizTypeEnum bizType,@Param("srcNo")String srcNo) {
		logger.info("[toPopularize] recommendMemberNo={},bizType={},srcNo={}",recommendMemberNo,bizType,srcNo);
		String lastestRateForSevenDay = null;
		List<ProductsForWXResultDto> list=null;
		HttpServletResponse response=getMvcFacade().getResponse();
		HttpSession session = getMvcFacade().getHttpSession();
		//存储推荐人的会员编号，推荐的业务类型，来源编号
		if(!StringUtils.isEmpty(recommendMemberNo) && !"null".equals(recommendMemberNo)){
			session.setAttribute("_recommendMemberNo", recommendMemberNo);
			session.setAttribute("_bizType", bizType);
			session.setAttribute("_srcNo", srcNo);
		}
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		
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
			logger.error("[toPopularize] info={},ERROR={}", "购买理财首页查询生财宝时异常",
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
			logger.error("[toPopularize] info={},ERROR={}", "查询固收理财产品时异常",
					e.getMessage());
		}
		
		//3.加密用户编码
		String ascMemberNo = null ;
		if(null != memberDto) {
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
		
		//未登录
		if (memberDto == null) {
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
		} else {
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
				}
				//用户的幸运值
				ActivityUserInfoDTO activityUserInfo = activityUserInfoFacade
						.selectUserInfoByMemberNo(memberDto.getMemberNo());
				addModelObject("activityUserInfo", activityUserInfo);
		}
		return "success";
	}

	/**
	 * 根据openId快速免登录，跳转购买理财首页
	 * 
	 * @param code
	 * @param response
	 * @return
	 */
	public String fastToPopularize(@Param("code") String code) {
		logger.info("[fastToPopularize] code={}",code);
		HttpSession session = getMvcFacade().getHttpSession();
		HttpServletRequest request = getMvcFacade().getRequest();
		HttpServletResponse response = getMvcFacade().getResponse();
		//判断cookie是否存在 存在说明已经与公众号交互过，将其改为NO
		String openId = null;
		try {
			if (!StringUtils.isEmpty(code)) {
				String result = HttpRequestUtils.sendHttpRequest(WX_OAUTH_REUQEST_URL,WX_OAUTH_REUQEST_METHOD,requestParamBuilderService.buildGetWebAccessTokenParam(code));
				logger.info("[fastToPopularize] result={}",result);
				if (!StringUtils.isEmpty(result)) {
					JSONObject resultJson = JSONObject.fromObject(result);
					if(resultJson.get("openid") != null){ 
						openId = resultJson.get("openid").toString(); 
						logger.info("[fastToPopularize] openId={}",openId);
						session.setAttribute("openId", openId);
						Cookie cookie = new Cookie(MLANMAO_OPEN_ID, openId);
						cookie.setMaxAge(-1); 
						response.addCookie(cookie);
						MemberRelevanceDto memberRelevanceDto = memberManagementFacade
								.obtainMemberRelevance(openId);
						if(memberRelevanceDto != null && (MemberRelevanceStatusEnum.ON).equals(memberRelevanceDto.getStatus()) && memberRelevanceDto.getMemberNo() != null){
							MemberDto memberDto = memberManagementFacade.obtainMember(memberRelevanceDto.getMemberNo());
							if(memberDto != null && (memberDto.getStatus().equals(MemberStatusEnum.ACTIVATED) || memberDto.getStatus().equals(MemberStatusEnum.AVAILABLY))){
								session.setAttribute("member", memberDto);
								session.setAttribute("recommendMemberNo", memberDto.getMemberNo());
								session.setAttribute("alreadyConcerned", "alreadyConcerned");//已经关注公众号
								try{
									//更新用户最后登录时间
									memberManagementFacade.updateMemberRelevanceLogDate(openId);
								}catch(Exception e){
									logger.info("[fastToPopularize] ERROR={}",e);
								}
								//查询是否是新手
								TrustOrderParamDto trustOrderParamDto = new TrustOrderParamDto();
								trustOrderParamDto.setMemberNo(memberDto.getMemberNo());
								trustOrderParamDto.setStatus(TradeOrderStatusEnum.SUCCESS);
								TrustOrderResultPageDto trustOrderResultPageDTO = trustQueryFacade.queryTrustOrder(trustOrderParamDto);
								logger.info("[login] trustOrderResultPageDTO={}",trustOrderResultPageDTO);
								List<TrustOrderResultDto> list = trustOrderResultPageDTO.getTrustOrderResult();
								logger.info("[login] list={}",list);
								if(list == null || list.size() == 0){
									session.setAttribute("newcomerFlag", "YES");
								}else{
									session.setAttribute("newcomerFlag", "NO");
								}
							}
						}
					} 
					
				}
			}
		} catch (Exception e) {
			logger.info("[fastToPopularize] info={},ERROR={}","免登录时发生异常",e.getMessage());
		}
		try {
			response.sendRedirect("../popularize/toPopularize");
//				RequestDispatcher dispatcher = request.getRequestDispatcher("toPopularize");
//				dispatcher.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * 我的懒猫快速登录
	 * @param in -
	 * @return 读取到的固定长度数据
	 */
	public String fastToAsset(@Param("code") String code){
		logger.info("[fastToAsset] code={}",code);
		HttpSession session = getMvcFacade().getHttpSession();
		HttpServletRequest request = getMvcFacade().getRequest();
		HttpServletResponse response = getMvcFacade().getResponse();
		//判断cookie是否存在 存在说明已经与公众号交互过，将其改为NO
		String openId = null;
		try {
			if (!StringUtils.isEmpty(code)) {
				String result = HttpRequestUtils.sendHttpRequest(WX_OAUTH_REUQEST_URL,WX_OAUTH_REUQEST_METHOD,requestParamBuilderService.buildGetWebAccessTokenParam(code));
				logger.info("[fastToAsset] result={}",result);
				if (!StringUtils.isEmpty(result)) {
					JSONObject resultJson = JSONObject.fromObject(result);
					if(resultJson.get("openid") != null){ 
						openId = resultJson.get("openid").toString(); 
						logger.info("[fastToAsset] openId={}",openId);
						session.setAttribute("openId", openId);
						Cookie cookie = new Cookie(MLANMAO_OPEN_ID, openId);
						cookie.setMaxAge(-1); 
						response.addCookie(cookie);
						MemberRelevanceDto memberRelevanceDto = memberManagementFacade
								.obtainMemberRelevance(openId);
						logger.info("[fastToAsset] memberRelevanceDto={}",memberRelevanceDto);
						if(memberRelevanceDto != null && (MemberRelevanceStatusEnum.ON).equals(memberRelevanceDto.getStatus()) && memberRelevanceDto.getMemberNo() != null){
							MemberDto memberDto = memberManagementFacade.obtainMember(memberRelevanceDto.getMemberNo());
							if(memberDto != null && (memberDto.getStatus().equals(MemberStatusEnum.ACTIVATED) || memberDto.getStatus().equals(MemberStatusEnum.AVAILABLY))){
								session.setAttribute("member", memberDto);
								session.setAttribute("recommendMemberNo", memberDto.getMemberNo());
								session.setAttribute("alreadyConcerned", "alreadyConcerned");//已经关注公众号
								try{
									//更新用户最后登录时间
									memberManagementFacade.updateMemberRelevanceLogDate(openId);
								}catch(Exception e){
									logger.info("[fastToAsset] ERROR={}",e);
								}
								//查询是否是新手
								TrustOrderParamDto trustOrderParamDto = new TrustOrderParamDto();
								trustOrderParamDto.setMemberNo(memberDto.getMemberNo());
								trustOrderParamDto.setStatus(TradeOrderStatusEnum.SUCCESS);
								TrustOrderResultPageDto trustOrderResultPageDTO = trustQueryFacade.queryTrustOrder(trustOrderParamDto);
								logger.info("[login] trustOrderResultPageDTO={}",trustOrderResultPageDTO);
								List<TrustOrderResultDto> list = trustOrderResultPageDTO.getTrustOrderResult();
								logger.info("[login] list={}",list);
								if(list == null || list.size() == 0){
									session.setAttribute("newcomerFlag", "YES");
								}else{
									session.setAttribute("newcomerFlag", "NO");
								}
							}
						}
					} 
					
				}
			}
		} catch (Exception e) {
			logger.info("[fastToAsset] info={},ERROR={}","免登录时发生异常",e.getMessage());
		}
		try {
			response.sendRedirect("../asset/toMyLM");
			/*RequestDispatcher dispatcher = request.getRequestDispatcher("../asset/toMyLM");
			dispatcher.forward(request, response);*/
		} catch (Exception e) {
			logger.info("[fastToAsset] info={},ERROR={}","重定向时发生异常",e.getMessage());
		}
		return SUCCESS;
	}

	/**
	 * 外部渠道联合登录，跳转至懒猫理财列表页面
	 * @param code
	 * @param response
	 * @return
	 */
	public String quickLoginByChannel(@Param("source") String source,
			@Param("openid") String openid,
			@Param("tist") String tist,
			@Param("sign") String sign) {
		logger.info("[quickLoginByChannel] source={},openid={},tist={},sign={}",source,openid,tist,sign);
		String toAction = "../asset/toMyLM";
		HttpSession session = getMvcFacade().getHttpSession();
		HttpServletRequest request = getMvcFacade().getRequest();
		HttpServletResponse response = getMvcFacade().getResponse();
		if ((MemberDto) session.getAttribute("member") != null) {
			session.removeAttribute("member");
		}
		try {
			ChannelDto channelDto = null;
			if (StringUtils.isNotEmpty(source)) {
				channelDto = channelFacade.queryChannelByChannelNo(source,ChannelTypeEnum.APP);
				if (channelDto == null) {
					//当查询出为空，说明已经失效或关闭
					logger.info("channelDto is null");
					response.sendRedirect(toAction);
					/*try {
						RequestDispatcher dispatcher = request.getRequestDispatcher(toAction);
						dispatcher.forward(request, response);
					} catch (Exception e) {
						e.printStackTrace();
					}*/
					return SUCCESS;

				}
				//跳转至该渠道指定的URL
				if(StringUtils.isNotEmpty(channelDto.getSkipPageUrl())) {
					toAction = channelDto.getSkipPageUrl();
				}
				// 判断是否超时
				Date d = new Date();
				if(StringUtils.isNotEmpty(tist)) {
					
					if ((d.getTime() - Long.valueOf(tist)) > channelDto.getSessionHoldTime() * 60 * 1000 ? true : false) {
						// 大于n分钟-抛出
						logger.info("what's fucking timeout");
						response.sendRedirect(toAction);
						/*try {
							RequestDispatcher dispatcher = request.getRequestDispatcher(toAction);
							dispatcher.forward(request, response);
						} catch (Exception e) {
							e.printStackTrace();
						}*/
						return SUCCESS;

					}
				}
				// 是否需要签名
				if (channelDto.getIsSign()) {
					String str = Digest.md5Digest(source + openid + tist + channelDto.getSign());
					if (!str.equals(sign)) {
						logger.info("sign string is not equals : " + str);
						response.sendRedirect(toAction);
						/*try {
							RequestDispatcher dispatcher = request.getRequestDispatcher(toAction);
							dispatcher.forward(request, response);
						} catch (Exception e) {
							e.printStackTrace();
						}*/
						return SUCCESS;
					}
				}
				// code不存在，则openid一定存在。mpos，app登陆方式
				if (!StringUtils.isEmpty(openid)) {
					session.setAttribute("openId", openid);
					Cookie cookie = new Cookie(MLANMAO_OPEN_ID, openid);
					//加入渠道
					Cookie cookie2 = new Cookie(LmConstants.FROM_NO, source);
					Cookie cookie3 = new Cookie(LmConstants.FROM_TYPE,URLEncoder.encode(channelDto.getChannelName(),"utf-8"));
					cookie.setMaxAge(-1);
					cookie2.setMaxAge(-1);
					cookie2.setPath("/");
					cookie3.setMaxAge(-1);
					cookie3.setPath("/");
					response.addCookie(cookie);
					response.addCookie(cookie2);
					response.addCookie(cookie3);
					MemberRelevanceDto memberEntiry = memberManagementFacade.obtainMemberRelevance(openid);
					if (memberEntiry != null) {
						if (memberEntiry.getMemberNo() != null && memberEntiry.getStatus() != null && MemberRelevanceStatusEnum.ON == memberEntiry.getStatus()) {
							MemberDto memberDto = memberManagementFacade.obtainMember(memberEntiry.getMemberNo());
							if (memberDto != null && (memberDto.getStatus().equals(MemberStatusEnum.ACTIVATED) || memberDto.getStatus().equals(MemberStatusEnum.AVAILABLY))) {
								session.setAttribute("member", memberDto);
								session.setAttribute("recommendMemberNo", memberDto.getMemberNo());
								toAction = "toPopularize";
								response.sendRedirect(toAction);
								/*try {
									RequestDispatcher dispatcher = request.getRequestDispatcher(toAction);
									dispatcher.forward(request, response);
								} catch (Exception e) {
									e.printStackTrace();
								}*/
								return SUCCESS;
							}
						}
					}
				} else {
					session.setAttribute("openId", null);
					Cookie cookie = new Cookie(MLANMAO_OPEN_ID, null);
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				} 
			} 
//			response.sendRedirect(toAction);
			try {
				RequestDispatcher dispatcher = request.getRequestDispatcher(toAction);
				dispatcher.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			logger.error("[quickLoginByChannel] info={},ERROR={}","免登录时异常", e);
		}
		return SUCCESS;
	}

	/**
	 * 外部渠道联合登录，跳转至懒猫理财列表页面
	 * @param code
	 * @param response
	 * @param specify 该参数为true时，说明若快速登录验证成功，跳转至渠道所配的指定URL，若为false，则跳转至理财列表首页
	 * @return
	 */
	public String quickLoginToSpecifyPage(@Param("source") String source,
			@Param("openid") String openid,
			@Param("tist") String tist,
			@Param("sign") String sign,
			@Param("specify") boolean specify) {
		logger.info("[quickLoginToSpecifyPage] source={},openid={},tist={},sign={}",source,openid,tist,sign);
		String toAction = "../asset/toMyLM";
		HttpServletRequest request = getMvcFacade().getRequest();
		HttpSession session = getMvcFacade().getHttpSession();
		HttpServletResponse response = getMvcFacade().getResponse();
		if ((MemberDto) session.getAttribute("member") != null) {
			session.removeAttribute("member");
		}
		try {
			ChannelDto channelDto = null;
			if (StringUtils.isNotEmpty(source)) {
				channelDto = channelFacade.queryChannelByChannelNo(source,ChannelTypeEnum.APP);
				if (channelDto == null) {
					//当查询出为空，说明已经失效或关闭
					logger.info("channelDto is null");
					response.sendRedirect(toAction);
					/*try {
						RequestDispatcher dispatcher = request.getRequestDispatcher(toAction);
						dispatcher.forward(request, response);
					} catch (Exception e) {
						e.printStackTrace();
					}*/
					return SUCCESS;

				}
				//跳转至该渠道指定的URL
				if(StringUtils.isNotEmpty(channelDto.getSkipPageUrl())) {
					toAction = channelDto.getSkipPageUrl();
				}
				// 判断是否超时
				Date d = new Date();
				if(StringUtils.isNotEmpty(tist)) {
					
					if ((d.getTime() - Long.valueOf(tist)) > channelDto.getSessionHoldTime() * 60 * 1000 ? true : false) {
						// 大于n分钟-抛出
						logger.info("what's fucking timeout");
						response.sendRedirect(toAction);
						/*try {
							RequestDispatcher dispatcher = request.getRequestDispatcher(toAction);
							dispatcher.forward(request, response);
						} catch (Exception e) {
							e.printStackTrace();
						}*/
						return SUCCESS;

					}
				}
				// 是否需要签名
				if (channelDto.getIsSign()) {
					String str = Digest.md5Digest(source + openid + tist + channelDto.getSign());
					if (!str.equals(sign)) {
						logger.info("sign string is not equals : " + str);
						response.sendRedirect(toAction);
						/*try {
							RequestDispatcher dispatcher = request.getRequestDispatcher(toAction);
							dispatcher.forward(request, response);
						} catch (Exception e) {
							e.printStackTrace();
						}*/
						return SUCCESS;
					}
				}
				// code不存在，则openid一定存在。mpos，app登陆方式
				if (!StringUtils.isEmpty(openid)) {
					session.setAttribute("openId", openid);
					Cookie cookie = new Cookie(MLANMAO_OPEN_ID, openid);
					//加入渠道
					Cookie cookie2 = new Cookie(LmConstants.FROM_NO, source);
					Cookie cookie3 = new Cookie(LmConstants.FROM_TYPE,URLEncoder.encode(channelDto.getChannelName(),"utf-8"));
					cookie.setMaxAge(-1);
					cookie2.setMaxAge(-1);
					cookie2.setPath("/");
					cookie3.setMaxAge(-1);
					cookie3.setPath("/");
					response.addCookie(cookie);
					response.addCookie(cookie2);
					response.addCookie(cookie3);
					MemberRelevanceDto memberEntiry = memberManagementFacade.obtainMemberRelevance(openid);
					if (memberEntiry != null) {
						if (memberEntiry.getMemberNo() != null && memberEntiry.getStatus() != null && MemberRelevanceStatusEnum.ON == memberEntiry.getStatus()) {
							MemberDto memberDto = memberManagementFacade.obtainMember(memberEntiry.getMemberNo());
							if (memberDto != null && (memberDto.getStatus().equals(MemberStatusEnum.ACTIVATED) || memberDto.getStatus().equals(MemberStatusEnum.AVAILABLY))) {
								session.setAttribute("member", memberDto);
								session.setAttribute("recommendMemberNo", memberDto.getMemberNo());
								if(!specify) {
									toAction = "toPopularize";
								}
								response.sendRedirect(toAction);
								/*try {
									RequestDispatcher dispatcher = request.getRequestDispatcher(toAction);
									dispatcher.forward(request, response);
								} catch (Exception e) {
									e.printStackTrace();
								}*/
								return SUCCESS;
							}
						}
					}
				} else {
					session.setAttribute("openId", null);
					Cookie cookie = new Cookie(MLANMAO_OPEN_ID, null);
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				} 
			} 
			response.sendRedirect(toAction);
			/*try {
				RequestDispatcher dispatcher = request.getRequestDispatcher(toAction);
				dispatcher.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}*/
		} catch (Exception e) {
			logger.error("[quickLoginToSpecifyPage] info={},ERROR={}","免登录时异常", e);
		}
		return SUCCESS;
	}
	
	//懒猫服务协议
    public String toGuide() {
        return SUCCESS;
    }
	
   /**
    * 奥运活动快速登录
    * @param in -
    * @return 
    */
    public String fastToOlympicActivity(@Param("code") String code){
    	logger.info("[fastToOlympicActivity] code={}",code);
		HttpSession session = getMvcFacade().getHttpSession();
		HttpServletResponse response = getMvcFacade().getResponse();
		HttpServletRequest request = getMvcFacade().getRequest();
		//判断cookie是否存在 存在说明已经与公众号交互过，将其改为NO
		String openId = null;
		try {
			if (!StringUtils.isEmpty(code)) {
				String result = HttpRequestUtils.sendHttpRequest(WX_OAUTH_REUQEST_URL,WX_OAUTH_REUQEST_METHOD,requestParamBuilderService.buildGetWebAccessTokenParam(code));
				logger.info("[fastToOlympicActivity] result={}",result);
				if (!StringUtils.isEmpty(result)) {
					JSONObject resultJson = JSONObject.fromObject(result);
					if(resultJson.get("openid") != null){ 
						openId = resultJson.get("openid").toString(); 
						logger.info("[fastToOlympicActivity] openId={}",openId);
						session.setAttribute("openId", openId);
						Cookie cookie = new Cookie(MLANMAO_OPEN_ID, openId);
						cookie.setMaxAge(-1); 
						response.addCookie(cookie);
						MemberRelevanceDto memberRelevanceDto = memberManagementFacade
								.obtainMemberRelevance(openId);
						logger.info("[fastToOlympicActivity] memberRelevanceDto={}",memberRelevanceDto);
						if(memberRelevanceDto != null && (MemberRelevanceStatusEnum.ON).equals(memberRelevanceDto.getStatus()) && memberRelevanceDto.getMemberNo() != null){
							MemberDto memberDto = memberManagementFacade.obtainMember(memberRelevanceDto.getMemberNo());
							logger.info("[fastToOlympicActivity] memberDto={}",memberDto);
							if(memberDto != null && (memberDto.getStatus().equals(MemberStatusEnum.ACTIVATED) || memberDto.getStatus().equals(MemberStatusEnum.AVAILABLY))){
								session.setAttribute("member", memberDto);
								session.setAttribute("recommendMemberNo", memberDto.getMemberNo());
								session.setAttribute("alreadyConcerned", "alreadyConcerned");//已经关注公众号
								try{
									//更新用户最后登录时间
									memberManagementFacade.updateMemberRelevanceLogDate(openId);
								}catch(Exception e){
									logger.info("[fastToOlympicActivity] ERROR={}",e);
								}
							}
						}
					} 
					
				}
			}
		} catch (Exception e) {
			logger.info("[fastToOlympicActivity] info={},ERROR={}","免登录时发生异常",e.getMessage());
		}
		try {
			response.sendRedirect("../activity/toOlympicActivity");
			/*try {
				RequestDispatcher dispatcher = request.getRequestDispatcher("../activity/toOlympicActivity");
				dispatcher.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}*/
		} catch (Exception e) {
			logger.info("[fastToOlympicActivity] info={},ERROR={}","重定向时发生异常",e.getMessage());
		}
    	return SUCCESS;
    }
    /**
     * 助威拿奖活动快速登录
     * @param in -
     * @return 
     */
    public String fastToCheerOlympic(@Param("code") String code){
    	logger.info("[fastToCheerOlympic] code={}",code);
		HttpSession session = getMvcFacade().getHttpSession();
		HttpServletResponse response = getMvcFacade().getResponse();
		HttpServletRequest request = getMvcFacade().getRequest();
		//判断cookie是否存在 存在说明已经与公众号交互过，将其改为NO
		String openId = null;
		try {
			if (!StringUtils.isEmpty(code)) {
				String result = HttpRequestUtils.sendHttpRequest(WX_OAUTH_REUQEST_URL,WX_OAUTH_REUQEST_METHOD,requestParamBuilderService.buildGetWebAccessTokenParam(code));
				logger.info("[fastToCheerOlympic] result={}",result);
				if (!StringUtils.isEmpty(result)) {
					JSONObject resultJson = JSONObject.fromObject(result);
					if(resultJson.get("openid") != null){ 
						openId = resultJson.get("openid").toString(); 
						logger.info("[fastToCheerOlympic] openId={}",openId);
						session.setAttribute("openId", openId);
						Cookie cookie = new Cookie(MLANMAO_OPEN_ID, openId);
						cookie.setMaxAge(-1); 
						response.addCookie(cookie);
						//查询该openid与会员的关联关系
						MemberRelevanceDto memberRelevanceDto = memberManagementFacade
								.obtainMemberRelevance(openId);
						if(memberRelevanceDto != null && (MemberRelevanceStatusEnum.ON).equals(memberRelevanceDto.getStatus()) && memberRelevanceDto.getMemberNo() != null){
							MemberDto memberDto = memberManagementFacade.obtainMember(memberRelevanceDto.getMemberNo());
							if(memberDto != null && (memberDto.getStatus().equals(MemberStatusEnum.ACTIVATED) || memberDto.getStatus().equals(MemberStatusEnum.AVAILABLY))){
								session.setAttribute("member", memberDto);
								try{
									//更新用户最后登录时间
									memberManagementFacade.updateMemberRelevanceLogDate(openId);
								}catch(Exception e){
									logger.info("[fastToCheerOlympic] ERROR={}",e);
								}
								try{
									//查询是否是新手
									TrustOrderParamDto trustOrderParamDto = new TrustOrderParamDto();
									trustOrderParamDto.setMemberNo(memberDto.getMemberNo());
									trustOrderParamDto.setStatus(TradeOrderStatusEnum.SUCCESS);
									TrustOrderResultPageDto trustOrderResultPageDTO = trustQueryFacade.queryTrustOrder(trustOrderParamDto);
									logger.info("[fastToCheerOlympic] trustOrderResultPageDTO={}",trustOrderResultPageDTO);
									List<TrustOrderResultDto> list = trustOrderResultPageDTO.getTrustOrderResult();
									logger.info("[fastToCheerOlympic] list={}",list);
									if(list == null || list.size() == 0){
										session.setAttribute("newcomerFlag", "YES");
									}else{
										session.setAttribute("newcomerFlag", "NO");
									}
								}catch(Exception e){
									logger.info("[fastToCheerOlympic] info={},ERROR={}","查询信托购买记录失败，新手标志设置失败",e);
								}
								
							}
						}
					} 
					
				}
			}
		} catch (Exception e) {
			logger.info("[fastToCheerOlympic] info={},ERROR={}","免登录时发生异常",e.getMessage());
		}
		try {
			//渠道链接
//			response.sendRedirect("../activity/toCheerOlympic");
			response.sendRedirect("../activity/promo?source=00065W003");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SUCCESS;
    }
    
    /**
     * 投资换产品快速登录
     */
    public String fastToInvForProActivity(@Param("code") String code){
    	logger.info("[fastToInvForProActivity] code={}",code);
		HttpSession session = getMvcFacade().getHttpSession();
		HttpServletResponse response = getMvcFacade().getResponse();
		//判断cookie是否存在 存在说明已经与公众号交互过，将其改为NO
		String openId = null;
		try {
			if (!StringUtils.isEmpty(code)) {
				String result = HttpRequestUtils.sendHttpRequest(WX_OAUTH_REUQEST_URL,WX_OAUTH_REUQEST_METHOD,requestParamBuilderService.buildGetWebAccessTokenParam(code));
				logger.info("[fastToInvForProActivity] result={}",result);
				if (!StringUtils.isEmpty(result)) {
					JSONObject resultJson = JSONObject.fromObject(result);
					if(resultJson.get("openid") != null){ 
						openId = resultJson.get("openid").toString(); 
						logger.info("[fastToInvForProActivity] openId={}",openId);
						session.setAttribute("openId", openId);
						Cookie cookie = new Cookie(MLANMAO_OPEN_ID, openId);
						cookie.setMaxAge(-1); 
						response.addCookie(cookie);
						MemberRelevanceDto memberRelevanceDto = memberManagementFacade
								.obtainMemberRelevance(openId);
						logger.info("[fastToInvForProActivity] memberRelevanceDto={}",memberRelevanceDto);
						if(memberRelevanceDto != null && (MemberRelevanceStatusEnum.ON).equals(memberRelevanceDto.getStatus()) && memberRelevanceDto.getMemberNo() != null){
							MemberDto memberDto = memberManagementFacade.obtainMember(memberRelevanceDto.getMemberNo());
							logger.info("[fastToInvForProActivity] memberDto={}",memberDto);
							if(memberDto != null && (memberDto.getStatus().equals(MemberStatusEnum.ACTIVATED) || memberDto.getStatus().equals(MemberStatusEnum.AVAILABLY))){
								session.setAttribute("member", memberDto);
								session.setAttribute("recommendMemberNo", memberDto.getMemberNo());
								session.setAttribute("alreadyConcerned", "alreadyConcerned");//已经关注公众号
								try{
									//更新用户最后登录时间
									memberManagementFacade.updateMemberRelevanceLogDate(openId);
								}catch(Exception e){
									logger.info("[fastToInvForProActivity] ERROR={}",e);
								}
							}
						}
					} 
					
				}
			}
		} catch (Exception e) {
			logger.info("[fastToInvForProActivity] info={},ERROR={}","免登录时发生异常",e.getMessage());
		}
		try {
			response.sendRedirect("../invForPro/toInvestForTravelList");
		} catch (IOException e) {
			logger.info("[fastToInvForProActivity] info={},ERROR={}","重定向时发生异常",e.getMessage());
		}
    	return SUCCESS;
    }
    /**
     * 旅游第二份半价活动快速登录
     * @param in -
     * @return 
     */
    public String fastToTourHalfActivity(@Param("code") String code){
    	logger.info("[fastToTourHalfActivity] code={}",code);
		HttpSession session = getMvcFacade().getHttpSession();
		HttpServletResponse response = getMvcFacade().getResponse();
		HttpServletRequest request = getMvcFacade().getRequest();
		//判断cookie是否存在 存在说明已经与公众号交互过，将其改为NO
		String openId = null;
		try {
			if (!StringUtils.isEmpty(code)) {
				String result = HttpRequestUtils.sendHttpRequest(WX_OAUTH_REUQEST_URL,WX_OAUTH_REUQEST_METHOD,requestParamBuilderService.buildGetWebAccessTokenParam(code));
				logger.info("[fastToTourHalfActivity] result={}",result);
				if (!StringUtils.isEmpty(result)) {
					JSONObject resultJson = JSONObject.fromObject(result);
					if(resultJson.get("openid") != null){ 
						openId = resultJson.get("openid").toString(); 
						logger.info("[fastToTourHalfActivity] openId={}",openId);
						session.setAttribute("openId", openId);
						Cookie cookie = new Cookie(MLANMAO_OPEN_ID, openId);
						cookie.setMaxAge(-1); 
						response.addCookie(cookie);
						//查询该openid与会员的关联关系
						MemberRelevanceDto memberRelevanceDto = memberManagementFacade
								.obtainMemberRelevance(openId);
						if(memberRelevanceDto != null && (MemberRelevanceStatusEnum.ON).equals(memberRelevanceDto.getStatus()) && memberRelevanceDto.getMemberNo() != null){
							MemberDto memberDto = memberManagementFacade.obtainMember(memberRelevanceDto.getMemberNo());
							if(memberDto != null && (memberDto.getStatus().equals(MemberStatusEnum.ACTIVATED) || memberDto.getStatus().equals(MemberStatusEnum.AVAILABLY))){
								session.setAttribute("member", memberDto);
								session.setAttribute("alreadyConcerned", "alreadyConcerned");//已经关注公众号
								try{
									//更新用户最后登录时间
									memberManagementFacade.updateMemberRelevanceLogDate(openId);
								}catch(Exception e){
									logger.info("[fastToTourHalfActivity] ERROR={}",e);
								}
								try{
									//查询是否是新手
									TrustOrderParamDto trustOrderParamDto = new TrustOrderParamDto();
									trustOrderParamDto.setMemberNo(memberDto.getMemberNo());
									trustOrderParamDto.setStatus(TradeOrderStatusEnum.SUCCESS);
									TrustOrderResultPageDto trustOrderResultPageDTO = trustQueryFacade.queryTrustOrder(trustOrderParamDto);
									logger.info("[fastToTourHalfActivity] trustOrderResultPageDTO={}",trustOrderResultPageDTO);
									List<TrustOrderResultDto> list = trustOrderResultPageDTO.getTrustOrderResult();
									logger.info("[fastToTourHalfActivity] list={}",list);
									if(list == null || list.size() == 0){
										session.setAttribute("newcomerFlag", "YES");
									}else{
										session.setAttribute("newcomerFlag", "NO");
									}
								}catch(Exception e){
									logger.info("[fastToTourHalfActivity] info={},ERROR={}","查询信托购买记录失败，新手标志设置失败",e);
								}
							}
						}
					} 
					
				}
			}
		} catch (Exception e) {
			logger.info("[fastToTourHalfActivity] info={},ERROR={}","免登录时发生异常",e.getMessage());
		}
		try {
			//渠道链接
//			response.sendRedirect("../activity/toTourHalfActivity");
			response.sendRedirect("../activity/promo?source=00061W008");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SUCCESS;
    }
    
    
    /**
     * 中秋活动快速登录
     * @param in -
     * @return 
     */
    public String fastToMidAutumnActivity(@Param("code") String code){
    	logger.info("[fastToMidAutumnActivity] code={}",code);
		HttpSession session = getMvcFacade().getHttpSession();
		HttpServletResponse response = getMvcFacade().getResponse();
		HttpServletRequest request = getMvcFacade().getRequest();
		//判断cookie是否存在 存在说明已经与公众号交互过，将其改为NO
		String openId = null;
		try {
			if (!StringUtils.isEmpty(code)) {
				String result = HttpRequestUtils.sendHttpRequest(WX_OAUTH_REUQEST_URL,WX_OAUTH_REUQEST_METHOD,requestParamBuilderService.buildGetWebAccessTokenParam(code));
				logger.info("[fastToMidAutumnActivity] result={}",result);
				if (!StringUtils.isEmpty(result)) {
					JSONObject resultJson = JSONObject.fromObject(result);
					if(resultJson.get("openid") != null){ 
						openId = resultJson.get("openid").toString(); 
						logger.info("[fastToMidAutumnActivity] openId={}",openId);
						session.setAttribute("openId", openId);
						Cookie cookie = new Cookie(MLANMAO_OPEN_ID, openId);
						cookie.setMaxAge(-1); 
						response.addCookie(cookie);
						//查询该openid与会员的关联关系
						MemberRelevanceDto memberRelevanceDto = memberManagementFacade
								.obtainMemberRelevance(openId);
						if(memberRelevanceDto != null && (MemberRelevanceStatusEnum.ON).equals(memberRelevanceDto.getStatus()) && memberRelevanceDto.getMemberNo() != null){
							MemberDto memberDto = memberManagementFacade.obtainMember(memberRelevanceDto.getMemberNo());
							if(memberDto != null && (memberDto.getStatus().equals(MemberStatusEnum.ACTIVATED) || memberDto.getStatus().equals(MemberStatusEnum.AVAILABLY))){
								session.setAttribute("member", memberDto);
								try{
									//更新用户最后登录时间
									memberManagementFacade.updateMemberRelevanceLogDate(openId);
								}catch(Exception e){
									logger.info("[fastToMidAutumnActivity] ERROR={}",e);
								}
								try{
									//查询是否是新手
									TrustOrderParamDto trustOrderParamDto = new TrustOrderParamDto();
									trustOrderParamDto.setMemberNo(memberDto.getMemberNo());
									trustOrderParamDto.setStatus(TradeOrderStatusEnum.SUCCESS);
									TrustOrderResultPageDto trustOrderResultPageDTO = trustQueryFacade.queryTrustOrder(trustOrderParamDto);
									logger.info("[fastToMidAutumnActivity] trustOrderResultPageDTO={}",trustOrderResultPageDTO);
									List<TrustOrderResultDto> list = trustOrderResultPageDTO.getTrustOrderResult();
									logger.info("[fastToMidAutumnActivity] list={}",list);
									if(list == null || list.size() == 0){
										session.setAttribute("newcomerFlag", "YES");
									}else{
										session.setAttribute("newcomerFlag", "NO");
									}
								}catch(Exception e){
									logger.info("[fastToMidAutumnActivity] info={},ERROR={}","查询信托购买记录失败，新手标志设置失败",e);
								}
							}
						}
					} 
					
				}
			}
		} catch (Exception e) {
			logger.info("[fastToMidAutumnActivity] info={},ERROR={}","免登录时发生异常",e.getMessage());
		}
		try {
			//渠道链接
//			response.sendRedirect("../activity/toTourHalfActivity");
			response.sendRedirect("../activity/promo?source=00068W001");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SUCCESS;
    }
}
