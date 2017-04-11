package com.yeepay.g3.app.lmweChat.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import com.yeepay.g3.app.lmweChat.action.asset.AssetAction;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.g3.utils.config.ConfigParam;
import com.yeepay.g3.utils.config.ConfigurationUtils;

public class LmConstants {
	
	/**
	 * 用户来源类型
	 */
	public static final String FROM_TYPE = "fromType";
	/**
	 * 用户来源编号
	 */
	public static final String FROM_NO = "fromNo";
	/**
	 * 用户个人ID
	 */
	public static final String OPEN_ID = "openId";
	/**
	 * 会话时长
	 */
	public static final String TIST = "tist";
	/**
	 * 签名
	 */
	public static final String SIGN = "sign";
	/**
	 * redis存储有效期
	 */
	public static final int REDIS_EXPIRE_TIME = 15724800;//半年
	/**
	 * css及js样式版本号控制
	 */
	public static final String sysVersion = "2.3";
	/**
	 * 投资换产品第二次投资半价活动code
	 */
	public static final String INVFORPRO_SECOND_DISCOUNT_TRIP_ACTIVITY="INVFORPRO_SECOND_DISCOUNT_TRIP_ACTIVITY";
	/**
	 * 投资换产品第二次投资半价事件code
	 */
	public static final String INVFORPRO_SECOND_DISCOUNT_TRIP_ACTION="INVFORPRO_SECOND_DISCOUNT_TRIP_ACTION";
	/**
	 * app推广送10元投资券事件code
	 */
	public static final String APP_MARKETING_PROMOTION_ACTION="APP_MARKETING_PROMOTION";
	/**
	 * 绑卡请求的流水号
	 */
	public static final String SESSION_BANCARD_REQUESTNO = "SESSION_BANKCARD_REQUESTNO";
	/**
	 * 智能投资商户编号
	 */
	public static String getZTMerchantNo(){
		return  (String) ConfigurationUtils.getConfigParam("config_type_text_resources", "ZT_MERCHANT_NO").getValue();
	}
	
	/**
	 * 懒猫APP错误码列表
	 */
	/*protected static Map<String, String> errorCodeMap = new HashMap<String, String>();
	
	static {
		errorCodeMap.put("00000", "请求成功");
		errorCodeMap.put("10001", "渠道请求不合法");
		errorCodeMap.put("10002", "用户名或密码为空");
		errorCodeMap.put("10003", "该会员不存在");
		errorCodeMap.put("10004", "登录密码不正确");
		errorCodeMap.put("11111", "未知错误，请稍后重试");
	}
	
	public static String getErrorCodeMsg(String errorCode) {
		return errorCodeMap.get(errorCode);
	}*/
	
	public static String getEsurfing800CallBackSwitch(){
		return  (String) ConfigurationUtils.getConfigParam("config_type_sys", "lmportal_esurfing800_switch").getValue();
	}
	
	/**
	 * 迁移开关  (true启用迁移后程序false使用旧程序)
	 * @author chao.han
	 */
	public static boolean getMigrateSwitch() {
		ConfigParam<?> config = ConfigurationUtils.getConfigParam("config_type_text_resources", "lanmao_migrate_switch"); 
		return config.getValue() == null ? false : (Boolean)config.getValue();
	}

	
	/**懒猫平台自有渠道号
	 * @return
	 */
	public static String readPlatformNo(){
		return (String)ConfigurationUtils.getConfigParam("config_type_sys", "lmportal_platform_channel_no").getValue();
	}
	
	/**
	 * 抽奖微信推送消息
	 * @author 陈大涛
	 * 2016-6-27下午4:55:15
	 */
	public static Map<String,String> getActivityDrawPrizeWxMessageModel(){
		ConfigParam<Map<String,String>> config = ConfigurationUtils.getConfigParam("config_type_text_resources", "wx_send_message_activity_draw_prize"); 
		return config.getValue() == null ? new HashMap<String,String>() : config.getValue();
	}
	/**
	 * 购买成功微信推送消息
	 * @author 陈大涛
	 * 2016-6-27下午4:55:30
	 */
	public static Map<String,String> getBuySuccessWxMessageModel(){
		ConfigParam<Map<String,String>> config = ConfigurationUtils.getConfigParam("config_type_text_resources", "wx_send_message_activity_buy_success"); 
		return config.getValue() == null ? new HashMap<String,String>() : config.getValue();
	}
	/**
	 * 注册成功微信推送消息
	 * @author 陈大涛
	 * 2016-6-27下午4:55:38
	 */
	public static Map<String,String> getRegisterSuccessWxMessageModel(){
		ConfigParam<Map<String,String>> config = ConfigurationUtils.getConfigParam("config_type_text_resources", "wx_send_message_activity_register_success"); 
		return config.getValue() == null ? new HashMap<String,String>() : config.getValue();
	}
	/**
	 * 提现成功微信推送消息
	 * @author 陈大涛
	 * 2016-6-28上午9:50:03
	 */
	public static Map<String,String> getCashInSuccessWxMessageModel(){
		ConfigParam<Map<String,String>> config = ConfigurationUtils.getConfigParam("config_type_text_resources", "wx_send_message_activity_cashin_success"); 
		return config.getValue() == null ? new HashMap<String,String>() : config.getValue();
	}
	/**
	 * 充值成功微信推送消息
	 * @author 陈大涛
	 * 2016-6-28上午9:50:52
	 */
	public static Map<String,String> getRechargeSuccessWxMessageModel(){
		ConfigParam<Map<String,String>> config = ConfigurationUtils.getConfigParam("config_type_text_resources", "wx_send_message_activity_recharge_success"); 
		return config.getValue() == null ? new HashMap<String,String>() : config.getValue();
	}
	/**
	 * 绑卡成功微信推送消息
	 * @author 陈大涛
	 * 2016-6-28上午10:42:10
	 */
	public static Map<String,String> getBindCardSuccessWxMessageModel(){
		ConfigParam<Map<String,String>> config = ConfigurationUtils.getConfigParam("config_type_text_resources", "wx_send_message_activity_bind_card_success"); 
		return config.getValue() == null ? new HashMap<String,String>() : config.getValue();
	}
	/**
	 * 生成时间戳，用于防止重复提交表单
	 */
	public static String makeToken(){
		String token = (System.currentTimeMillis() + new Random().nextInt(99999)) +"";
		String aesToken = SecretUtils.secretData(token);
		return aesToken;
	}
}
