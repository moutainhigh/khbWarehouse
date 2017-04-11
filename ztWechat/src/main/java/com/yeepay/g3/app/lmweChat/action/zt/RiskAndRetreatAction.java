package com.yeepay.g3.app.lmweChat.action.zt;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;

import com.lanmao.fund.facade.fundsales.dto.FundPaperInfoAddDto;
import com.lanmao.fund.facade.fundsales.dto.QuestionResultDto;
import com.lanmao.fund.facade.fundsales.service.FundTradeFacade;
import com.lanmao.fund.facade.fundsales.service.FundTradeManagementFacade;
import com.lanmao.fund.facade.fundsales.service.FundTradeQueryFacade;
import com.yeepay.g3.app.lmweChat.action.BaseAction;
import com.yeepay.g3.app.lmweChat.utils.ConstantUtils;
import com.yeepay.g3.app.lmweChat.utils.GetParamUtils;
import com.yeepay.g3.app.lmweChat.utils.HttpRequestUtils;
import com.yeepay.g3.app.lmweChat.utils.PageShowUtils;
import com.yeepay.g3.facade.lmportal.dto.BankCardInfoDto;
import com.yeepay.g3.facade.lmportal.dto.MemberDto;
import com.yeepay.g3.facade.lmportal.service.LPQueryFacade;
import com.yeepay.g3.facade.zt.dto.ZtMemberRetreatRecordDTO;
import com.yeepay.g3.facade.zt.dto.ZtPolicyCalculateRecordDTO;
import com.yeepay.g3.facade.zt.dto.ZtPolicyDTO;
import com.yeepay.g3.facade.zt.dto.ZtSceneDTO;
import com.yeepay.g3.facade.zt.enums.ZtSceneTypeEnum;
import com.yeepay.g3.facade.zt.facade.ZtPolicyCalculateFacade;
import com.yeepay.g3.facade.zt.facade.ZtPolicyFacade;
import com.yeepay.g3.facade.zt.facade.ZtRetreatRecordFacade;
import com.yeepay.g3.facade.zt.facade.ZtSceneFacade;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;
import com.yeepay.g3.utils.web.emvc.annotation.Param;

@Controller
public class RiskAndRetreatAction extends BaseAction {
	
	private static final Logger logger = LoggerFactory
			.getLogger(RiskAndRetreatAction.class);
	/**
	 * 查询用户交易记录
	 */
	protected FundTradeManagementFacade fundTradeManagementFacade = RemoteServiceFactory
			.getService(FundTradeManagementFacade.class);
	
	protected ZtSceneFacade ztSceneFacade = RemoteServiceFactory
			.getService(ZtSceneFacade.class);
	protected ZtRetreatRecordFacade ztRetreatRecordFacade = RemoteServiceFactory
			.getService(ZtRetreatRecordFacade.class);
	protected ZtPolicyCalculateFacade ztPolicyCalculateFacade = RemoteServiceFactory
			.getService(ZtPolicyCalculateFacade.class);
	protected ZtPolicyFacade ztPolicyFacade = RemoteServiceFactory
			.getService(ZtPolicyFacade.class);
	
	protected FundTradeQueryFacade fundTradeQueryFacade = RemoteServiceFactory
			.getService(FundTradeQueryFacade.class);
	protected FundTradeFacade fundTradeFacade = RemoteServiceFactory
			.getService(FundTradeFacade.class);
	/**
	 * 获取用户绑定的银行卡信息
	 */
	protected LPQueryFacade lPqueryFacade = RemoteServiceFactory
			.getService(LPQueryFacade.class);
	
	
	/*protected ZtSceneFacade ztSceneFacade = RemoteServiceFactory
			.getService("http://localhost:8099/activity-hessian/hessian",
					RemotingProtocol.HESSIAN, ZtSceneFacade.class);
	protected ZtRetreatRecordFacade ztRetreatRecordFacade = RemoteServiceFactory
			.getService("http://localhost:8099/activity-hessian/hessian",
					RemotingProtocol.HESSIAN, ZtRetreatRecordFacade.class);
	protected ZtPolicyCalculateFacade ztPolicyCalculateFacade = RemoteServiceFactory
			.getService("http://localhost:8099/activity-hessian/hessian",
					RemotingProtocol.HESSIAN, ZtPolicyCalculateFacade.class);
	protected ZtPolicyFacade ztPolicyFacade = RemoteServiceFactory
			.getService("http://localhost:8099/activity-hessian/hessian",
					RemotingProtocol.HESSIAN, ZtPolicyFacade.class);*/
	/**
	 * 心愿单或定制题目页面
	 * @author hongbin.kang
	 * @date 2016年10月24日 下午6:38:44
	 * @param sceneId
	 * @return
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public String questionsPage(@Param("sceneId") String sceneId) throws ServletException, IOException {
		logger.info("心愿单或定制题目页面sceneId={}",sceneId);
		HttpServletRequest request = getMvcFacade().getRequest();
		HttpServletResponse response = getMvcFacade().getResponse();
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		
		//STEP.1 是否做过测评
		String userRisk =  fundTradeQueryFacade.checkIsOpenAcct(memberDto.getMemberNo()).getSuccessType();
		if(!"0".equals(userRisk)){
			//没做过跳转测评页面
			RequestDispatcher dispatcher = request.getRequestDispatcher("toRiskTestStart");
			dispatcher.forward(request, response);
		} 
		
		//STEP.2 是否做过最大回撤率
		ZtMemberRetreatRecordDTO retreatRecordDTO = ztRetreatRecordFacade.selectRetreatRecordByMemberNo(memberDto.getMemberNo());
		if(null == retreatRecordDTO) {
			//没做过最大回撤率
			RequestDispatcher dispatcher = request.getRequestDispatcher("toRetreatTest?sceneId="+sceneId);
			dispatcher.forward(request, response);
		}
		addModelObject("sceneId", sceneId);
		ZtSceneDTO ztSceneDto = ztSceneFacade.querySceneInfoById(Long.valueOf(sceneId));
		if(null == ztSceneDto) {
			logger.error("查询场景不存在");
		} else {
			return ztSceneDto.getSceneType().toString();
		}
	    return SUCCESS;
	}
	
	/**
	 * 处理问题答案
	 * @author hongbin.kang
	 * @date 2016年11月2日 下午5:15:46
	 * @param sceneId
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doQuestions(@Param("sceneId") String sceneId,@Param("type") String type,
			@Param("money") BigDecimal money,@Param("years") String years,@Param("all") String all) {/*
		logger.info("[doWishQuestions] sceneId={},totalMoney={},years={},all={}",sceneId,money,years,all);
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		ZtSceneDTO ztSceneDto = ztSceneFacade.querySceneInfoById(Long.valueOf(sceneId));
		//STEP.1 计算满足用户条件的策略
		if("wish".equals(type)) {
			//心愿单
			ztPolicyCalculateFacade.calculatePolicyInvest(memberDto.getMemberNo(),money , Integer.valueOf(years),ztSceneDto.getSceneType());	
		} else if("customized".equals(type)) {
			//定制组合
			ztPolicyCalculateFacade.calculateCustomizedPolicyInvest(memberDto.getMemberNo(),money ,Integer.valueOf(years),ztSceneDto.getSceneType());	
		}
		//STEP.2 查询用户的最大回撤率
		ZtMemberRetreatRecordDTO retreatRecordDTO = ztRetreatRecordFacade.selectRetreatRecordByMemberNo(memberDto.getMemberNo());
		List<ZtPolicyCalculateRecordDTO> dtoList = new ArrayList<ZtPolicyCalculateRecordDTO>();
		List<ZtPolicyCalculateRecordDTO> allList = ztPolicyCalculateFacade.queryListByMemberNoAndRetreat(memberDto.getMemberNo(), null);
		if(StringUtils.isEmpty(all)) {
			//查看部分
			dtoList = ztPolicyCalculateFacade.queryListByMemberNoAndRetreat(memberDto.getMemberNo(), retreatRecordDTO.getRecordRate().negate());
			if(dtoList.size() == 0) {
				dtoList.add(allList.get(0));
			}
			if(dtoList.size() != allList.size()) {
				//是否需要全部展示按钮
				addModelObject("showAll", "0");
			}
		} else {
			//查看全部
			dtoList = allList;
			addModelObject("all", all);
		}
		addModelObject("ztSceneDto", ztSceneDto);
		for(ZtPolicyCalculateRecordDTO dto : dtoList) {
			ZtPolicyDTO ztPolicyDto =  ztPolicyFacade.selectPolicyByPrimaryKey(dto.getPolicyId());
			if(dto.getPerInvestAmount().compareTo(new BigDecimal(500)) < 0) {
				addModelObject("wanringFlag", "0");
			}
			if(ztSceneDto.getSceneType().toString().equals(ZtSceneTypeEnum.PERSONALIZED_CUSTOM.toString())){
				dto.setSugWish(ztPolicyDto.getSugWish());
			} else {
				dto.setSugWish(ztSceneDto.getSceneName());
			}
			dto.setPolicyName(ztPolicyDto.getPolicyName());
			dto.setPolicyType(ztPolicyDto.getPolicyType());
			dto.setFluctuate(ztPolicyDto.getFluctuate());
			NumberFormat nf = new DecimalFormat("#,###.##");
			dto.setPerInvestAmount(dto.getPerInvestAmount().setScale(0,BigDecimal.ROUND_HALF_UP));
			dto.setFutureAmountMin(dto.getFutureAmountMin().setScale(0,BigDecimal.ROUND_HALF_UP));
			dto.setFutureAmountMinStr(nf.format(dto.getFutureAmountMin().setScale(0,BigDecimal.ROUND_HALF_UP)));
			dto.setFutureAmountMax(dto.getFutureAmountMax().setScale(0,BigDecimal.ROUND_HALF_UP));
			dto.setFutureAmountMaxStr(nf.format(dto.getFutureAmountMax().setScale(0,BigDecimal.ROUND_HALF_UP)));
			dto.setTotalYieldRate(dto.getTotalYieldRate().multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP));
		}
		addModelObject("sceneId", sceneId);
		addModelObject("dtoList", dtoList);
		addModelObject("money", money);
		addModelObject("years", years);
		addModelObject("type", type);
		if("customized".equals(type)){
			return "customizedResult";
		}
	    return SUCCESS;
	*/}
	
	
	/**
	 * 同意协议开始基金测评
	 * @author hongbin.kang
	 * @date 2016年10月24日 下午8:09:07
	 * @param sceneId
	 * @return
	 * @throws Exception 
	 */
	public String toRiskTestStart(@Param("sceneId") String sceneId) throws Exception {
		logger.info("同意协议开始基金测评sceneId={}",sceneId);
		HttpServletRequest request = getMvcFacade().getRequest();
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		String riskLevel = getUserRisk(memberDto.getMemberNo());
		if(!StringUtils.isEmpty(riskLevel)){
			addModelObject("riskLevel", riskLevel);
		}
		if (isUserBindCard(memberDto.getMemberNo())) {
			addModelObject("isBinkCard", "1");
		} else {
			addModelObject("isBinkCard", "0");
			String APP = (String) request.getSession().getAttribute("platform");
			String queryStr = HttpRequestUtils.buildReturnUrl(request);
			if (StringUtils.isNotEmpty(APP)) {
				addModelObject("lanmao", "app");
				addModelObject("addr","account/card/toBindCard?returnUrl="+queryStr);
			} else {
				addModelObject("addr","account/card/toBindCard?returnUrlString="+queryStr);
			}

		}
		addModelObject("sceneId", sceneId);
	    return SUCCESS;
	}
	
	/**
	 * 判断用户是否绑卡
	 * 
	 * @return true:已经绑卡，false:未绑卡
	 * @throws Exception
	 */
	protected boolean isUserBindCard(String memberNo)
			throws Exception {
		BankCardInfoDto bankCard = lPqueryFacade
				.obtainDefaultBankCardInfo(memberNo);
		if (bankCard != null && !StringUtils.isEmpty(bankCard.getCardNo())) {
			return true;
		}
		return false;
	}
	
	/**
	 * 去基金风险测评
	 * @author hongbin.kang
	 * @date 2016年10月24日 下午8:09:07
	 * @param sceneId
	 * @return
	 * @throws Exception 
	 */
	public String toRiskTest(@Param("sceneId") String sceneId) throws Exception {
		logger.info("去基金风险测评sceneId={}",sceneId);
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		addModelObject("sceneId", sceneId);
		String riskLevel = getUserRisk(memberDto.getMemberNo());
		if(!StringUtils.isEmpty(riskLevel)){
			addModelObject("riskLevel", riskLevel);
		}
	    return SUCCESS;
	}
	
	/**
	 * 跳过风险测评
	 * 
	 * @param request
	 * @param response
	 * @param fundCode
	 * @param riskFlag
	 * @param riskLevel
	 * @return
	 * @throws Exception
	 */
	public String goJumpRisk(@Param("sceneId") String sceneId) throws Exception {
		logger.info("[goJumpRisk] 跳过风险测评.sceneId={}",sceneId);
		HttpServletRequest request = getMvcFacade().getRequest();
		HttpServletResponse response = getMvcFacade().getResponse();
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		String riskLevel = getUserRisk(memberDto.getMemberNo());
		if(StringUtils.isEmpty(riskLevel)){
			String[] questionArr = { "1", "2", "3", "4", "5", "6", "7", "8",
			"9" };
			String[] answerArr = { "1", "1", "1", "1", "1", "1", "1", "1", "1" };
			//STEP.1 基金风险测评
			QuestionResultDto questionResultDto = riskTest(memberDto.getMemberNo(),
					questionArr, Arrays.asList(answerArr));
			if (!ConstantUtils.SUCCESS_TYPE_SUCC.equals(questionResultDto
					.getCode())) { // 创建账户失败抛异常
				logger.info("[LMRiskTestAct]-[riskTesting]-[风险评测失败]-info={}",
						questionResultDto.toString());
				addModelObject("reason", "fund");
				return "testFail";
			}
		}
		String uri = "toRetreatTest?sceneId="+sceneId;
		RequestDispatcher dispatcher = request.getRequestDispatcher(uri);
		dispatcher.forward(request, response);
		
		return null;
	}
	
	private QuestionResultDto riskTest(String memberNo,
			String[] questionArr, List answerArr) throws Exception {
		FundPaperInfoAddDto dto = new FundPaperInfoAddDto(GetParamUtils.getFundPlatNo(),
				memberNo, Arrays.asList(questionArr),answerArr);
		QuestionResultDto questionResultDto = new QuestionResultDto();
		//基金风险测评
		try {
			questionResultDto = fundTradeFacade.paperInfoAddAndUpdate(dto);
		} catch (Exception e) {
			logger.error(
					"[IntroduceAction][doRiskTest] message=" + e.getMessage(),e);
			questionResultDto.setCode("1");
		}
		logger.info("[LMRiskTestAct]-[riskTesting]questionResultDto={}{}",
				questionResultDto.getCode(), questionResultDto.getResult());
		// QuestionResultDto questionResultDto = new QuestionResultDto();
		// questionResultDto.setResult("5"); //TODO 测试用
		// questionResultDto.setCode("0");
		logger.info("基金风险测评结果：questionResultDto="
				+ questionResultDto);
		return questionResultDto;
	}
	
	/**
	 * 做风险测评
	 * @author hongbin.kang
	 * @date 2016年10月24日 下午8:22:27
	 * @param sceneId
	 * @return
	 * @throws Exception 
	 */
	public String doRiskTest(@Param("answerArr") String[] answerArr,@Param("sceneId") String sceneId,@Param("retreatAnswer") String retreatAnswer) throws Exception {
		HttpServletRequest request = getMvcFacade().getRequest();
		HttpServletResponse response = getMvcFacade().getResponse();
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		logger.info("做基金风险测评[doRiskTest],answer={},retreatAnswer={},memberDto={}",answerArr,retreatAnswer,memberDto.toString());
		String[] questionArr = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		String[] answer = answerArr[0].split(",");
		//STEP.1 基金风险测评
		QuestionResultDto questionResultDto = riskTest(memberDto.getMemberNo(),questionArr, Arrays.asList(answer));
		
		if (!ConstantUtils.SUCCESS_TYPE_SUCC
				.equals(questionResultDto.getCode())) { // 创建账户失败抛异常
			logger.info("[风险评测失败]-info={}",
					questionResultDto.toString());
			addModelObject("reason", "fund");
			return "testFail";
		} else {
			//STEP.2 最大回撤率测评
			String retreat = retreatTest(memberDto.getMemberNo(),retreatAnswer);
			if("fail".equals(retreat)) {
				addModelObject("reason", "retreat");
				return "testFail";
			}
			String uri = "../../zt/asset/toMyPolicyInvest";
			if(!StringUtils.isEmpty(sceneId)) {
				uri = "questionsPage?sceneId="+sceneId;
			}
			RequestDispatcher dispatcher = request.getRequestDispatcher(uri);
			dispatcher.forward(request, response);
		}
	    return null;
	}
	
	/**
	 * 去最大回撤率测评
	 * @author hongbin.kang
	 * @date 2016年10月24日 下午8:09:32
	 * @param sceneId
	 * @return
	 */
	public String toRetreatTest(@Param("sceneId") String sceneId) {
		logger.info("去最大回撤率测评[toRetreatTest],sceneId={}",sceneId);
		addModelObject("sceneId", sceneId);
	    return SUCCESS;
	}
	
	/**
	 * 做最大回撤率测评
	 * @author hongbin.kang
	 * @date 2016年10月24日 下午8:22:42
	 * @param sceneId
	 * @return
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public String doRetreatTest(@Param("sceneId") String sceneId,@Param("retreatAnswer") String retreatAnswer) throws ServletException, IOException {
		logger.info("做最大回撤率测评[doRetreatTest],sceneId={}",sceneId);
		HttpServletRequest request = getMvcFacade().getRequest();
		HttpServletResponse response = getMvcFacade().getResponse();
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("member");
		// 最大回撤率测评
		String retreat = retreatTest(memberDto.getMemberNo(),retreatAnswer);
		if("fail".equals(retreat)) {
			addModelObject("reason", "retreat");
			return "testFail";
		}
		String uri = "../../zt/asset/toMyPolicyInvest";
		if(!StringUtils.isEmpty(sceneId)) {
			uri = "questionsPage?sceneId="+sceneId;
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(uri);
		dispatcher.forward(request, response);
	    return null;
	}
	
	/**
	 * 保存最大回撤率
	 * @author hongbin.kang
	 * @date 2016年10月25日 上午10:21:30
	 * @param memberNo
	 * @param answer
	 * @return
	 */
	private String  retreatTest(String memberNo , String answer) {
		logger.info("保存最大回撤率[retreatTest] memberNo={},answer={}",memberNo,answer);
		ZtMemberRetreatRecordDTO retreatRecordDTO = ztRetreatRecordFacade.selectRetreatRecordByMemberNo(memberNo);
		ZtMemberRetreatRecordDTO dto = new ZtMemberRetreatRecordDTO();
		dto.setMemberNo(memberNo);
		dto.setRecordRate(BigDecimal.valueOf(Double.valueOf(answer)));
		if(null == retreatRecordDTO) {
			try {
				String result =  ztRetreatRecordFacade.addRetreatRecord(dto);
				logger.info("[retreatTest] 保存结果result={}",result);
			} catch (Exception e) {
				// TODO: handle exception
				logger.info("保存最大回撤率异常");
				return "fail";
			}
		} else {
			try {
				retreatRecordDTO.setRecordRate(BigDecimal.valueOf(Double.valueOf(answer)));
			    ztRetreatRecordFacade.updateRetreatRecord(retreatRecordDTO);
			} catch (Exception e) {
				// TODO: handle exception
				logger.info("更新最大回撤率异常");
				return "fail";
			}

		}
		return SUCCESS;
	}
	
	
	public String riskInstruction() {

		return SUCCESS;
	}
	
	public String riskProtocol() {

		return SUCCESS;
	}
	
	/**
	 * 获取用户风险等级
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected String getUserRisk(String memberNo) throws Exception {
		return PageShowUtils.getRiskString(fundTradeManagementFacade
				.queryUserRiskTolerance(memberNo));
	}
}
 