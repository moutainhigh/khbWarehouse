/**
 * @author 陈大涛
 * 2016-10-27上午11:31:03
 */
package com.yeepay.g3.app.lmweChat.action.zt;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;

import com.lanmao.fund.facade.fundsales.service.LMFundInfoQueryServiceFacade;
import com.lanmao.fund.facade.queryservice.dto.LMFundDetailInfoDTO;
import com.yeepay.g3.app.lmweChat.action.BaseAction;
import com.yeepay.g3.app.lmweChat.utils.StringProcessorUtils;
import com.yeepay.g3.facade.lmportal.dto.MemberDto;
import com.yeepay.g3.facade.zt.dto.ZtMemberRetreatRecordDTO;
import com.yeepay.g3.facade.zt.dto.ZtPolicyCalculateRecordDTO;
import com.yeepay.g3.facade.zt.dto.ZtPolicyDTO;
import com.yeepay.g3.facade.zt.dto.ZtPolicyFundCostDetailDTO;
import com.yeepay.g3.facade.zt.dto.ZtPolicyOrderDTO;
import com.yeepay.g3.facade.zt.dto.ZtPolicyProductDTO;
import com.yeepay.g3.facade.zt.dto.ZtSceneDTO;
import com.yeepay.g3.facade.zt.dto.ZtYieldRateDTO;
import com.yeepay.g3.facade.zt.enums.CalculateType;
import com.yeepay.g3.facade.zt.enums.ZtFundTypeEnum;
import com.yeepay.g3.facade.zt.enums.ZtSceneTypeEnum;
import com.yeepay.g3.facade.zt.facade.ZtPolicyCalculateFacade;
import com.yeepay.g3.facade.zt.facade.ZtPolicyFacade;
import com.yeepay.g3.facade.zt.facade.ZtPolicyFundTradeQueryFacade;
import com.yeepay.g3.facade.zt.facade.ZtPolicyOrderFacade;
import com.yeepay.g3.facade.zt.facade.ZtPolicyProductFacade;
import com.yeepay.g3.facade.zt.facade.ZtPolicyYieldRateFacade;
import com.yeepay.g3.facade.zt.facade.ZtRetreatRecordFacade;
import com.yeepay.g3.facade.zt.facade.ZtSceneFacade;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.web.emvc.annotation.Param;

/**
 * 策略组合
 * @Description
 * @author 陈大涛
 * 2016-10-27上午11:31:03
 */
@Controller
public class PolicyAction  extends BaseAction {
	private Logger logger = LoggerFactory.getLogger(PolicyAction.class);
	
	//本地
	/*private ZtPolicyFacade ztPolicyFacadeImpl = RemoteServiceFactory.getService("http://localhost:8080/activity-hessian/", RemotingProtocol.HESSIAN, ZtPolicyFacade.class);
	private ZtPolicyProductFacade ztPolicyProductFacadeImpl = RemoteServiceFactory.getService("http://localhost:8080/activity-hessian/", RemotingProtocol.HESSIAN, ZtPolicyProductFacade.class);
	private ZtPolicyCalculateFacade ztPolicyCalculateFacadeImpl = RemoteServiceFactory.getService("http://localhost:8080/activity-hessian/", RemotingProtocol.HESSIAN, ZtPolicyCalculateFacade.class);
	private ZtSceneFacade ztSceneFacadeImpl = RemoteServiceFactory.getService("http://localhost:8080/activity-hessian/", RemotingProtocol.HESSIAN, ZtSceneFacade.class);
	private ZtRetreatRecordFacade ztRetreatRecordFacadeImpl = RemoteServiceFactory.getService("http://localhost:8080/activity-hessian/", RemotingProtocol.HESSIAN, ZtRetreatRecordFacade.class);
	private ZtPolicyYieldRateFacade ztPolicyYieldRateFacadeImpl = RemoteServiceFactory.getService("http://localhost:8080/activity-hessian/", RemotingProtocol.HESSIAN, ZtPolicyYieldRateFacade.class);
	private ZtPolicyOrderFacade ztPolicyOrderFacadeImpl = RemoteServiceFactory.getService("http://localhost:8080/activity-hessian/", RemotingProtocol.HESSIAN, ZtPolicyOrderFacade.class);
	private ZtSceneFacade ztSceneFacadeImpl = RemoteServiceFactory.getService("http://localhost:8080/activity-hessian/", RemotingProtocol.HESSIAN, ZtSceneFacade.class);
	private ZtPolicyFundTradeQueryFacade ztPolicyFundTradeQueryFacadeImpl = RemoteServiceFactory.getService("http://localhost:8080/activity-hessian/", RemotingProtocol.HESSIAN, ZtPolicyFundTradeQueryFacade.class);
	*/
	//线上 
	private ZtPolicyFacade ztPolicyFacadeImpl = RemoteServiceFactory.getService(ZtPolicyFacade.class);
	private ZtPolicyProductFacade ztPolicyProductFacadeImpl = RemoteServiceFactory.getService(ZtPolicyProductFacade.class);
	private ZtPolicyCalculateFacade ztPolicyCalculateFacadeImpl = RemoteServiceFactory.getService(ZtPolicyCalculateFacade.class);
	private ZtRetreatRecordFacade ztRetreatRecordFacadeImpl = RemoteServiceFactory.getService(ZtRetreatRecordFacade.class);
	private ZtPolicyYieldRateFacade ztPolicyYieldRateFacadeImpl = RemoteServiceFactory.getService(ZtPolicyYieldRateFacade.class);
	private ZtPolicyOrderFacade ztPolicyOrderFacadeImpl = RemoteServiceFactory.getService(ZtPolicyOrderFacade.class);
	private ZtSceneFacade ztSceneFacadeImpl = RemoteServiceFactory.getService(ZtSceneFacade.class);
	protected ZtPolicyCalculateFacade ztPolicyCalculateFacade = RemoteServiceFactory.getService(ZtPolicyCalculateFacade.class);
	private ZtPolicyFundTradeQueryFacade ztPolicyFundTradeQueryFacadeImpl = RemoteServiceFactory.getService(ZtPolicyFundTradeQueryFacade.class);
	
	private LMFundInfoQueryServiceFacade lMFundInfoQueryServiceFacadeImpl = RemoteServiceFactory.getService(LMFundInfoQueryServiceFacade.class);
	/**
	 * 跳转策略详情页
	 * @author 陈大涛
	 * 2016-10-27上午11:34:44
	 */
	public String toPolicyInfo(@Param("sceneId") Long sceneId,@Param("calculateRecordId") Long calculateRecordId){
		logger.info("[toPolicyInfo] sceneId={},calculateRecordId={}",sceneId,calculateRecordId);
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto)session.getAttribute("member");
		ZtPolicyDTO ztPolicyDto=null;
		List<ZtPolicyProductDTO> policyProductList =null;	
		ZtPolicyCalculateRecordDTO  ztPolicyCalculateRecordDto=null;
		BigDecimal netValue=new BigDecimal(0);//基金加权的组合净值信息
		Date tradingDay =null;//组合净值获取时间
		BigDecimal BOND=new BigDecimal(0);
		BigDecimal STOCK=new BigDecimal(0);
		ZtMemberRetreatRecordDTO ztMemberRetreatRecordDto=null;
		try {
			//1.查询用户临时测评
			 ztPolicyCalculateRecordDto = ztPolicyCalculateFacadeImpl.queryPolicyCalcRecordById(calculateRecordId);
			//2.查询策略信息
			 ztPolicyDto =  ztPolicyFacadeImpl.selectPolicyByPrimaryKey(ztPolicyCalculateRecordDto.getPolicyId());
			//3.查询策略对应基金列表
			 policyProductList = ztPolicyProductFacadeImpl.selectPolicyProductList(ztPolicyCalculateRecordDto.getPolicyId());
			 //4.查询基金加权的组合净值信息（每只基金单位净值根据比例加权）
			 for(ZtPolicyProductDTO items:policyProductList){
				 LMFundDetailInfoDTO fundDetailInfoDto = lMFundInfoQueryServiceFacadeImpl.queryFundDetail(items.getProductCode());
				 netValue=netValue.add(fundDetailInfoDto.getUnitNV().multiply(items.getProductProportion()));
				 tradingDay=fundDetailInfoDto.getTradingDay();
				 if(items.getProductType()==ZtFundTypeEnum.BOND){
					 BOND= BOND.add(items.getProductProportion()); 
				 }else  if(items.getProductType()==ZtFundTypeEnum.STOCK){
					 STOCK=STOCK.add(items.getProductProportion()); 
				 }else{
					 //none
				 }
			 }
//			//5.查询场景信息
//			 ztSceneDto = ztSceneFacadeImpl.querySceneInfoById(sceneId);
			//6.查询用户测评回撤率
			 ztMemberRetreatRecordDto = ztRetreatRecordFacadeImpl.selectRetreatRecordByMemberNo(memberDto.getMemberNo());
		} catch (Exception e) {
			logger.error("[toPolicyInfo] 远程调用接口异常 e={}",e);
		}
		addModelObject("ztPolicyDto", ztPolicyDto);//策略信息
		addModelObject("policyDesc", StringProcessorUtils.replaceRN(ztPolicyDto.getPolicyDesc()));//策略信息动态策略
		addModelObject("costDesc", StringProcessorUtils.replaceRN(ztPolicyDto.getCostDesc()));//策略信息相关费用 
		addModelObject("attentionDesc", StringProcessorUtils.replaceRN(ztPolicyDto.getAttentionDesc()));//策略信息注意事项
		addModelObject("policyProductList", policyProductList);//策略对应基金列表
		addModelObject("ztPolicyCalculateRecordDto", ztPolicyCalculateRecordDto);//用户临时测评
		addModelObject("sceneId", sceneId);//场景略信息
		addModelObject("netValue", netValue);//基金加权的组合净值信息
		addModelObject("date",new SimpleDateFormat("MM/dd").format(tradingDay));//组合净值获取时间月/日 格式化
		addModelObject("bond", BOND.multiply(new BigDecimal(100)).intValue());//基金加权的组合净值信息
		addModelObject("stock", STOCK.multiply(new BigDecimal(100)).intValue());//基金加权的组合净值信息
		addModelObject("ztMemberRetreatRecordDto", ztMemberRetreatRecordDto);//查询用户测评回撤率
		return SUCCESS;
	}
	
	public String toPolicyDetailForAgainQuery(@Param("policyOrderId") Long policyOrderId){
		logger.info("[toPolicyDetailForAgainQuery] policyOrderId={}",policyOrderId);
		HttpSession session = getMvcFacade().getHttpSession();
		MemberDto memberDto = (MemberDto)session.getAttribute("member");
		ZtPolicyDTO ztPolicyDto=null;
		List<ZtPolicyProductDTO> policyProductList =null;	
		ZtPolicyCalculateRecordDTO  ztPolicyCalculateRecordDto=null;
		BigDecimal BOND=new BigDecimal(0);
		BigDecimal STOCK=new BigDecimal(0);
		try {
			//0.查询策略订单
			ZtPolicyOrderDTO ztPolicyOrderDto = ztPolicyOrderFacadeImpl.queryZtPolicyOrderById(policyOrderId);
			//1.查询用户临时测评
			ZtSceneDTO ztSceneDto = ztSceneFacadeImpl.querySceneInfoById(ztPolicyOrderDto.getSceneId());
			if(ztSceneDto.getSceneType()==ZtSceneTypeEnum.CARANDHOUSE||ztSceneDto.getSceneType()==ZtSceneTypeEnum.CHILDREN_EDUCATION){
				//心愿单
//				ztPolicyCalculateFacade.calculatePolicyInvest(memberDto.getMemberNo(), new BigDecimal(0), 2,ztSceneDto.getSceneType());
			}else{
				//定制组合
//				ztPolicyCalculateFacade.calculateCustomizedPolicyInvest(memberDto.getMemberNo(),new BigDecimal(0) ,2,ztSceneDto.getSceneType());
			}
			 ztPolicyCalculateRecordDto = ztPolicyCalculateFacadeImpl.queryPolicyCalcRecordByMemberAndPolicyId(memberDto.getMemberNo(), ztPolicyOrderDto.getPolicyId());
			//2.查询策略信息
			 ztPolicyDto =  ztPolicyFacadeImpl.selectPolicyByPrimaryKey(ztPolicyOrderDto.getPolicyId());
			//3.查询策略对应基金列表
			 policyProductList = ztPolicyProductFacadeImpl.selectPolicyProductList(ztPolicyOrderDto.getPolicyId());
			 //4.查询基金加权的组合净值信息（每只基金单位净值根据比例加权）
			 for(ZtPolicyProductDTO items:policyProductList){
				 if(items.getProductType()==ZtFundTypeEnum.BOND){
					 BOND= BOND.add(items.getProductProportion()); 
				 }else  if(items.getProductType()==ZtFundTypeEnum.STOCK){
					 STOCK=STOCK.add(items.getProductProportion()); 
				 }else{
					 //none
				 }
			 }
		} catch (Exception e) {
			logger.error("[toPolicyInfo] 远程调用接口异常 e={}",e);
		}
		addModelObject("ztPolicyDto", ztPolicyDto);//策略信息
		addModelObject("policyDesc", StringProcessorUtils.replaceRN(ztPolicyDto.getPolicyDesc()));//策略信息动态策略
		addModelObject("costDesc", StringProcessorUtils.replaceRN(ztPolicyDto.getCostDesc()));//策略信息相关费用 
		addModelObject("attentionDesc", StringProcessorUtils.replaceRN(ztPolicyDto.getAttentionDesc()));//策略信息注意事项
		addModelObject("policyProductList", policyProductList);//策略对应基金列表
		addModelObject("ztPolicyCalculateRecordDto", ztPolicyCalculateRecordDto);//用户临时测评
		addModelObject("bond", BOND.multiply(new BigDecimal(100)).intValue());//基金加权的组合净值信息
		addModelObject("stock", STOCK.multiply(new BigDecimal(100)).intValue());//基金加权的组合净值信息
		return SUCCESS;
	}
	/**
	 * 查询策略详情页面收益曲线和策略基金集合
	 * @author 陈大涛
	 * 2016-11-4下午4:13:45
	 */
	public String queryPolicyFundIncomeList(@Param("policyId") Long policyId){
		logger.info("[toPolicyInfo] policyId={}",policyId);
		Map<String,Object> resultMap = new HashMap<String, Object>();
		List<ZtPolicyProductDTO> policyProductList =new ArrayList<ZtPolicyProductDTO>();	
		List<List<ZtYieldRateDTO>> yieldRateList = new ArrayList<List<ZtYieldRateDTO>>();
		List<List<ZtYieldRateDTO>> yieldRateFixedList = new ArrayList<List<ZtYieldRateDTO>>();
		List<List<ZtYieldRateDTO>> yieldRateOnceList = new ArrayList<List<ZtYieldRateDTO>>();
		Integer yield =0;
		try {
			//1.查询策略对应基金列表
			 policyProductList = ztPolicyProductFacadeImpl.selectPolicyProductList(policyId);
			 //2.循环查询5年的收益率信息queryEntityByParam
			 for(int i=1;i<6;i++){
				 //查询比较基准list
				 ZtYieldRateDTO ztYieldRateDto =new ZtYieldRateDTO();
				 ztYieldRateDto.setPolicyId(policyId);
				 ztYieldRateDto.setCalculateType(CalculateType.AINDEX_CBINDEX_EOD);//比较基准
				 ztYieldRateDto.setLastTerm(i);
				 yieldRateList.add(ztPolicyYieldRateFacadeImpl.queryEntityByParam(ztYieldRateDto));
				//查询一次性投入list
				 ZtYieldRateDTO ztYieldRateOnceDTO =new ZtYieldRateDTO();
				 ztYieldRateOnceDTO.setPolicyId(policyId);
				 ztYieldRateOnceDTO.setCalculateType(CalculateType.POLICY_ONCE_INVEST);
				 ztYieldRateOnceDTO.setLastTerm(i);
				 List<ZtYieldRateDTO> yieldRateDtoList = ztPolicyYieldRateFacadeImpl.queryEntityByParam(ztYieldRateOnceDTO);
				 yieldRateOnceList.add(yieldRateDtoList);
				//查询每月定投list
				 ZtYieldRateDTO ztYieldRateFixedDto =new ZtYieldRateDTO();
				 ztYieldRateFixedDto.setPolicyId(policyId);
				 ztYieldRateFixedDto.setCalculateType(CalculateType.POLICY_FIXED_INVEST);
				 ztYieldRateFixedDto.setLastTerm(i);
				 yieldRateFixedList.add(ztPolicyYieldRateFacadeImpl.queryEntityByParam(ztYieldRateFixedDto)); 
				 //3.有效数据年数
				 if(yieldRateDtoList!=null&&yieldRateDtoList.size()!=0){
					 yield++;
				 }
			 }
			
			
		} catch (Exception e) {
			logger.error("[queryPolicyFundIncomeList] 远程调用接口异常 e={}",e);
		}
		resultMap.put("policyProductList", policyProductList);
		resultMap.put("yieldRateList", commonUtil(yieldRateList));
		resultMap.put("yieldRateFixedList", commonUtil(yieldRateFixedList));
		resultMap.put("yieldRateOnceList", commonUtil(yieldRateOnceList));
		resultMap.put("yield", yield);
		setJsonModel(resultMap);
		return "json";
	}
	
	private List<List<List<Double>>> commonUtil(List<List<ZtYieldRateDTO>> params){
		List<List<List<Double>>> result = new ArrayList<List<List<Double>>>();
		
		for(List<ZtYieldRateDTO> param :params){
			List<List<Double>> resultParam = new ArrayList<List<Double>>();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMM");
			for(ZtYieldRateDTO items:param){
				//过滤  5年之后的数据不要
				if(items.getLastTerm()<=5){
					List<Double> paramList = new ArrayList<Double>();
					Calendar cal = Calendar.getInstance();
					try {
						cal.setTime(sdf.parse(items.getCurMonth()));
					} catch (ParseException e) {
						logger.error("[commonUtil] 转换时间格式错误");
					}
					long timestamp = cal.getTimeInMillis();
					paramList.add(new BigDecimal(timestamp).doubleValue());
					paramList.add((items.getYieldRate().multiply(new BigDecimal(100) )).doubleValue());//百分比
					resultParam.add(paramList);
				}
			}
			result.add(resultParam);
		}
		return result;
	}
	/**
	 * 跳转常见问题页面
	 * @author 陈大涛
	 * 2016-11-4下午2:26:25
	 */
	public String toQuestion(){
		return SUCCESS;
	}
	/**
	 * 跳转策略详情查询更多页面
	 * @author 陈大涛
	 * 2016-11-4下午2:26:25
	 */
	public String toPolicyInfoMoreDetail(@Param("policyId") Long policyId,@Param("type") String type){
		logger.info("[toPolicyInfoMoreDetail] policyId={},type={}",policyId,type);
		ZtPolicyDTO ztPolicyDto=null;
		try {
			//查询策略信息
			 ztPolicyDto =  ztPolicyFacadeImpl.selectPolicyByPrimaryKey(policyId);
		} catch (Exception e) {
			logger.error("[toPolicyInfoMoreDetail] 远程调用接口异常 e={}",e);
			return SYSTEM_EXCEPTION;
		}
		ztPolicyDto.setAttentionDesc(StringProcessorUtils.replaceRN(ztPolicyDto.getAttentionDesc()));
		ztPolicyDto.setCostDesc(StringProcessorUtils.replaceRN(ztPolicyDto.getCostDesc()));
		ztPolicyDto.setPolicyDesc(StringProcessorUtils.replaceRN(ztPolicyDto.getPolicyDesc()));
		addModelObject("ztPolicyDto", ztPolicyDto);//策略信息
		addModelObject("type", type);//类型
		return SUCCESS;
	}
	
	/**
	 * 跳转相关费用
	 * @author 陈大涛
	 * 2016-11-16下午5:03:40
	 */
	public String toCostMore(@Param("policyId") Long policyId){
		logger.info("[toCostMore] policyId={}",policyId);
		List<ZtPolicyFundCostDetailDTO> result = ztPolicyFundTradeQueryFacadeImpl.queryPolicyCostDetail(policyId);
		addModelObject("result", result);
		return SUCCESS;
	}
	
	
}
