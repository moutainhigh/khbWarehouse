package com.yeepay.g3.app.lmweChat.biz.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yeepay.g3.app.dto.UserOpeDTO;
import com.yeepay.g3.app.enums.ErrorCodeTypeEnum;
import com.yeepay.g3.app.enums.LoginResultEnum;
import com.yeepay.g3.app.enums.ResultCodeEnum;
import com.yeepay.g3.app.lmweChat.biz.ChannelOpeBiz;
import com.yeepay.g3.app.lmweChat.biz.SendMsgCodeBiz;
import com.yeepay.g3.app.lmweChat.biz.UserBiz;
import com.yeepay.g3.app.lmweChat.utils.GetParamUtils;
import com.yeepay.g3.app.lmweChat.utils.LmConstants;
import com.yeepay.g3.app.lmweChat.utils.RegexMatchesUtils;
import com.yeepay.g3.facade.lmaccs.enumtype.CardVerifyStatusEnum;
import com.yeepay.g3.facade.lmportal.dto.CardVerifyAndPasswordParamDto;
import com.yeepay.g3.facade.lmportal.dto.CardVerifyAndPasswordResultDto;
import com.yeepay.g3.facade.lmportal.dto.ChannelDto;
import com.yeepay.g3.facade.lmportal.dto.MemberDto;
import com.yeepay.g3.facade.lmportal.dto.MemberPasswordValidationResultDto;
import com.yeepay.g3.facade.lmportal.enumtype.DynamicPwdTypeEnum;
import com.yeepay.g3.facade.lmportal.enumtype.PasswordStatusEnum;
import com.yeepay.g3.facade.lmportal.enumtype.PasswordValidationTypeEnum;
import com.yeepay.g3.facade.lmportal.service.LMUtilFacde;
import com.yeepay.g3.facade.lmportal.service.LanmaoDemandFacade;
import com.yeepay.g3.facade.lmportal.service.LanmaoTransactionFacade;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.encrypt.Digest;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.g3.utils.config.ConfigParam;
import com.yeepay.g3.utils.config.ConfigurationUtils;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.smartcache.utils.SmartCacheUtils;

/**
 * @Title: 发送短信验证码业务逻辑处理实现类
 * @Description: 发送短信验证码业务
 * @Copyright: 懒猫金服
 * @author ying.liu
 * @createTime 2016-7-29 下午5:37:40
 * @version 2016-7-29
 */
@Component
public class SendMsgCodeBizImpl implements SendMsgCodeBiz {

	protected LMUtilFacde lMUtilFacde = RemoteServiceFactory.getService(LMUtilFacde.class);
	
	protected LanmaoTransactionFacade lanmaoTransactionFacade = RemoteServiceFactory.getService(LanmaoTransactionFacade.class);
	
	protected LanmaoDemandFacade lanmaoDemandfFacade = RemoteServiceFactory.getService(LanmaoDemandFacade.class);
	
	private static final Logger logger = LoggerFactory.getLogger(UserBizImpl.class);
	
	@Autowired
	private ChannelOpeBiz channelOpeBizImpl;
	
	@Autowired
	private UserBiz userBizImpl;
	
	@Override
	public UserOpeDTO sendMsgCode(String srcNo,String loginName,String mobileNo, String opeType) {
		logger.info("[sendMsgCode] mobileNo={},opeType={}",mobileNo,opeType);
		UserOpeDTO userOpeDto = new UserOpeDTO();
		if(StringUtils.isEmpty(srcNo)){
			String[] error = GetParamUtils.readErrorCodeConfig(ErrorCodeTypeEnum.LMAPP_SRC_REQUEST_ERROR);	

			userOpeDto.setResultCode(ResultCodeEnum.FAILED);
//			String errorCode = "10001";
			userOpeDto.setErrorCode(error[0]);
			userOpeDto.setErrorMsg(error[1]);
			return userOpeDto;
		}
		//查询渠道信息
		ChannelDto channelDto = channelOpeBizImpl.getChannelInfo(srcNo);
		if(channelDto == null){
			logger.info("[sendMsgCode] ERROR={}","渠道号不存在或已过期");
			String[] error = GetParamUtils.readErrorCodeConfig(ErrorCodeTypeEnum.LMAPP_SRC_REQUEST_ERROR);	

			userOpeDto.setResultCode(ResultCodeEnum.FAILED);
//			String errorCode = "10001";
			userOpeDto.setErrorCode(error[0]);
			userOpeDto.setErrorMsg(error[1]);
			return userOpeDto;
		}
		if(StringUtils.isEmpty(mobileNo) || StringUtils.isEmpty(opeType)){
			logger.info("[sendMsgCode] info={}","手机号或操作类型为空");
			String[] error = GetParamUtils.readErrorCodeConfig(ErrorCodeTypeEnum.LMAPP_TELOROPE_NULL_ERROR);	

			userOpeDto.setResultCode(ResultCodeEnum.FAILED);
//			String errorCode = "10005";
			userOpeDto.setErrorCode(error[0]);
			userOpeDto.setErrorMsg(error[1]);
			return userOpeDto;
		}
		//旧版app绑卡时，提示系统升级
		if("BINDBANKCARD".equals(opeType)){
			String[] error = GetParamUtils.readErrorCodeConfig(ErrorCodeTypeEnum.LMAPP_SYSTEM_UPGRADE);	
			userOpeDto.setResultCode(ResultCodeEnum.FAILED);
//			String errorCode = "10001";
			userOpeDto.setErrorCode(error[0]);
			userOpeDto.setErrorMsg(error[1]);
			return userOpeDto;
		}
		//计算用户的登录标识
		String userSessionKey = Digest.md5Digest(srcNo + loginName + channelDto.getSign());
		//根据标识查询redis中是否有用户信息
		MemberDto memberDto = (MemberDto) SmartCacheUtils.get(userSessionKey);
		try{
			//没有用户真实姓名时，传参为手机号
			String realName = mobileNo;
			if(memberDto != null && !memberDto.getRealName().equals("")){
				realName = memberDto.getRealName();
			}
			lMUtilFacde.sendDynamicPwd(DynamicPwdTypeEnum.valueOf(opeType), mobileNo, realName);
		}catch(Exception e){
			logger.error("[] info={},mobileNo={},ERROR={}","发送短信验证码时异常",mobileNo,e);
			String[] error = GetParamUtils.readErrorCodeConfig(ErrorCodeTypeEnum.LMAPP_SEND_MSG_EXCEPTION);	
			userOpeDto.setResultCode(ResultCodeEnum.FAILED);
//			String errorCode = "10001";
			userOpeDto.setErrorCode(error[0]);
			userOpeDto.setErrorMsg(error[1]);
			return userOpeDto;
		}
		String[] error = GetParamUtils.readErrorCodeConfig(ErrorCodeTypeEnum.LMAPP_REQUEST_SUCCESS);	
		userOpeDto.setResultCode(ResultCodeEnum.SUCCESS);
		userOpeDto.setErrorCode(error[0]);
		userOpeDto.setErrorMsg(error[1]);
		if(memberDto != null){
			userOpeDto.setMemberNo(memberDto.getMemberNo());
			userOpeDto.setMemberTel(memberDto.getBindMobileNo());
			userOpeDto.setUserSessionKey(userSessionKey);
		}
		return userOpeDto;
	}

	@Override
	public UserOpeDTO sendBankMsgCode(String srcNo, String loginName,
			String mobileNo, String realName, String idCard, String cardNo,
			String tradePwd) {
		UserOpeDTO userOpeDto = new UserOpeDTO();
		if(StringUtils.isEmpty(srcNo)){
			String[] error = GetParamUtils.readErrorCodeConfig(ErrorCodeTypeEnum.LMAPP_SRC_REQUEST_ERROR);	
			userOpeDto.setResultCode(ResultCodeEnum.FAILED);
			userOpeDto.setErrorCode(error[0]);
			userOpeDto.setErrorMsg(error[1]);
			return userOpeDto;
		}
		//1.查询渠道信息
		ChannelDto channelDto = channelOpeBizImpl.getChannelInfo(srcNo);
		if(channelDto == null){
			logger.info("[sendBankMsgCode] ERROR={}","渠道号不存在或已过期");
			String[] error = GetParamUtils.readErrorCodeConfig(ErrorCodeTypeEnum.LMAPP_SRC_REQUEST_ERROR);	
			userOpeDto.setResultCode(ResultCodeEnum.FAILED);
			userOpeDto.setErrorCode(error[0]);
			userOpeDto.setErrorMsg(error[1]);
			return userOpeDto;
		}
		//2.计算用户的登录标识，取member信息
		String userSessionKey = Digest.md5Digest(srcNo + loginName + channelDto.getSign());
		//根据标识查询redis中是否有用户信息
		MemberDto memberDto = (MemberDto) SmartCacheUtils.get(userSessionKey);
		if(null == memberDto || null == memberDto.getMemberNo()){
			//用户登录失效
			String[] error = GetParamUtils.readErrorCodeConfig(ErrorCodeTypeEnum.LMAPP_LOGIN_INVALID_ERROR);	
			userOpeDto.setResultCode(ResultCodeEnum.FAILED);
			userOpeDto.setErrorCode(error[0]);
			userOpeDto.setErrorMsg(error[1]);
			return userOpeDto;
		}
		//3.验证传入的参数规则
		//交易密码
		if(StringUtils.isEmpty(tradePwd)){
			String[] error = GetParamUtils.readErrorCodeConfig(ErrorCodeTypeEnum.LMAPP_SENDMSG_TRADEPWD_NULL);	
			userOpeDto.setResultCode(ResultCodeEnum.FAILED);
			userOpeDto.setErrorCode(error[0]);
			userOpeDto.setErrorMsg(error[1]);
			return userOpeDto;
		}
		if(RegexMatchesUtils.regexTradePWD(tradePwd) == false){
			String[] error = GetParamUtils.readErrorCodeConfig(ErrorCodeTypeEnum.LMAPP_SENDMSG_TRADEPWD_ERROR);	
			userOpeDto.setResultCode(ResultCodeEnum.FAILED);
			userOpeDto.setErrorCode(error[0]);
			userOpeDto.setErrorMsg(error[1]);
			return userOpeDto;
		}
		//真实姓名
		if(StringUtils.isEmpty(realName) || RegexMatchesUtils.regexIsChinese(realName) == false){
			String[] error = GetParamUtils.readErrorCodeConfig(ErrorCodeTypeEnum.LMAPP_SENDMSG_REALNAME_ERROR);	
			userOpeDto.setResultCode(ResultCodeEnum.FAILED);
			userOpeDto.setErrorCode(error[0]);
			userOpeDto.setErrorMsg(error[1]);
			return userOpeDto;
		}
		//身份证号
		if(StringUtils.isEmpty(idCard) || RegexMatchesUtils.regexIdCard(idCard) == false){
			String[] error = GetParamUtils.readErrorCodeConfig(ErrorCodeTypeEnum.LMAPP_SENDMSG_IDCARD_ERROR);	
			userOpeDto.setResultCode(ResultCodeEnum.FAILED);
			userOpeDto.setErrorCode(error[0]);
			userOpeDto.setErrorMsg(error[1]);
			return userOpeDto;
		}
		//银行卡号
		if(StringUtils.isEmpty(cardNo)){
			String[] error = GetParamUtils.readErrorCodeConfig(ErrorCodeTypeEnum.LMAPP_SENDMSG_CARDNO_ERROR);	
			userOpeDto.setResultCode(ResultCodeEnum.FAILED);
			userOpeDto.setErrorCode(error[0]);
			userOpeDto.setErrorMsg(error[1]);
			return userOpeDto;
		}
		//请输入正确手机号
		if(StringUtils.isEmpty(mobileNo) || mobileNo.length() != 11){
			String[] error = GetParamUtils.readErrorCodeConfig(ErrorCodeTypeEnum.LMAPP_SENDMSG_MOBILENO_ERROR);	
			userOpeDto.setResultCode(ResultCodeEnum.FAILED);
			userOpeDto.setErrorCode(error[0]);
			userOpeDto.setErrorMsg(error[1]);
			return userOpeDto;
		}
		//4.验证交易密码和登录密码是否相同
		//验证交易密码和登录密码
		try {
			MemberPasswordValidationResultDto memberPasswordValidationResultDto = lanmaoDemandfFacade
					.queryMemberPwdValidation(memberDto.getMemberNo(),tradePwd,PasswordValidationTypeEnum.TRAD_COMPARE_LOGIN);
			if(null!=memberPasswordValidationResultDto&&!memberPasswordValidationResultDto.getStatus().equals(PasswordStatusEnum.SUCCESS)){
				logger.info("[sendBankMsgCode] info={},memberPasswordValidationResultDto={}","交易密码和登录密码相同",memberPasswordValidationResultDto);
				userOpeDto = new UserOpeDTO();
//				String[] error = GetParamUtils.readErrorCodeConfig(ErrorCodeTypeEnum.LMAPP_TRADEPWD_EQUALS_LOGINPWD_ERROR);	
				userOpeDto.setResultCode(ResultCodeEnum.FAILED);
				userOpeDto.setErrorCode(memberPasswordValidationResultDto.getCode());
				userOpeDto.setErrorMsg(memberPasswordValidationResultDto.getDescription());
				return userOpeDto;
			}
		} catch (Exception e) {
			logger.error("[sendBankMsgCode] info={},ERROR={}","验证交易密码和登录密码相同时异常",e);
			userOpeDto = new UserOpeDTO();
			String[] error = GetParamUtils.readErrorCodeConfig(ErrorCodeTypeEnum.LMAPP_VERIFY_TRADEPWD_LOGINPWD_ERROR);	
			userOpeDto.setResultCode(ResultCodeEnum.FAILED);
			userOpeDto.setErrorCode(error[0]);
			userOpeDto.setErrorMsg(error[1]);
			return userOpeDto;
		}
		//4.发送短信验证码
		try{
			CardVerifyAndPasswordParamDto cardVerifyAndPasswordParamDto = new CardVerifyAndPasswordParamDto();
			cardVerifyAndPasswordParamDto.setMemberNo(memberDto.getMemberNo());
			cardVerifyAndPasswordParamDto.setBankAccountName(realName);
			cardVerifyAndPasswordParamDto.setBankAccountNo(cardNo);
			cardVerifyAndPasswordParamDto.setBindMobile(mobileNo);
			cardVerifyAndPasswordParamDto.setIdNumber(idCard);
			cardVerifyAndPasswordParamDto.setTradingPassword(tradePwd);
			//绑卡发送短验
			logger.info(
					"[sendBankMsgCode]cardVerifyAndPasswordParamDto={}",
					cardVerifyAndPasswordParamDto);
			CardVerifyAndPasswordResultDto cardVerifyAndPasswordResultDto = lanmaoTransactionFacade.cardVerifyRequest(cardVerifyAndPasswordParamDto);
			logger.info("[sendBankMsgCode] cardVerifyAndPasswordResultDto={}",cardVerifyAndPasswordResultDto);
			//状态码不是0，说明失败
			/*if(!cardVerifyAndPasswordResultDto.getCode().equals("0")){
				setJsonModel(cardVerifyAndPasswordResultDto.getDescription());
				logger.error("[sendBindBankCardCode] code={},error={}",cardVerifyAndPasswordResultDto.getCode(),cardVerifyAndPasswordResultDto.getDescription());
				return "json";
			}*/
			if(!CardVerifyStatusEnum.SUCCESS.equals(cardVerifyAndPasswordResultDto.getStatus())){
				logger.info("[sendBankMsgCode] info={},cardVerifyAndPasswordParamDto={},cardVerifyAndPasswordResultDto={}","发送短信状态失败",cardVerifyAndPasswordParamDto,cardVerifyAndPasswordResultDto);
				userOpeDto.setResultCode(ResultCodeEnum.FAILED);
				userOpeDto.setErrorCode(cardVerifyAndPasswordResultDto.getCode());
				userOpeDto.setErrorMsg(cardVerifyAndPasswordResultDto.getDescription());
				return userOpeDto;
			}
			/*if(!"0".equals(cardVerifyAndPasswordResultDto.getCode())){
				logger.info("[sendBankMsgCode] info={},cardVerifyAndPasswordParamDto={},cardVerifyAndPasswordResultDto={}","发送短信状态失败",cardVerifyAndPasswordParamDto,cardVerifyAndPasswordResultDto);
				userOpeDto.setResultCode(ResultCodeEnum.FAILED);
				userOpeDto.setErrorCode(cardVerifyAndPasswordResultDto.getCode());
				userOpeDto.setErrorMsg(cardVerifyAndPasswordResultDto.getDescription());
				return userOpeDto;
			}*/
			String requestNo = "";
			if(null != cardVerifyAndPasswordResultDto.getRequestNo()){
				requestNo = cardVerifyAndPasswordResultDto.getRequestNo();
			}
			SmartCacheUtils.set(memberDto.getMemberNo(), requestNo, LmConstants.REDIS_EXPIRE_TIME); //设置有效期
		}catch(Exception e){
//			e.printStackTrace();
			logger.error("[sendBankMsgCode] info={},ERROR={}","发送短信验证码异常",e);
			String[] error = GetParamUtils.readErrorCodeConfig(ErrorCodeTypeEnum.LMAPP_SEND_MSG_EXCEPTION);	
			userOpeDto.setResultCode(ResultCodeEnum.FAILED);
			userOpeDto.setErrorCode(error[0]);
			userOpeDto.setErrorMsg(error[1]);
			return userOpeDto;
		}
		
		String[] error = GetParamUtils.readErrorCodeConfig(ErrorCodeTypeEnum.LMAPP_REQUEST_SUCCESS);	
		userOpeDto.setResultCode(ResultCodeEnum.SUCCESS);
		userOpeDto.setErrorCode(error[0]);
		userOpeDto.setErrorMsg(error[1]);
		if(memberDto != null){
			userOpeDto.setMemberNo(memberDto.getMemberNo());
			userOpeDto.setMemberTel(memberDto.getBindMobileNo());
			userOpeDto.setUserSessionKey(userSessionKey);
		}
		logger.info("[sendBankMsgCode] userOpeDto={}",userOpeDto);
		return userOpeDto;
	}

}
