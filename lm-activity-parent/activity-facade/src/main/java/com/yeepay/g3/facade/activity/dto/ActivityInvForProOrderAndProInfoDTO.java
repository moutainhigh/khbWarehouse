/**
 * @author 陈大涛
 * 2016-7-29上午11:12:21
 */
package com.yeepay.g3.facade.activity.dto;

import java.io.Serializable;

/**
 * @Description 我的订单信息DTO
 * @author 陈大涛
 * 2016-7-29上午11:12:21
 */
public class ActivityInvForProOrderAndProInfoDTO  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2579760913050750157L;

	/**产品明细信息 **/
	private ActivityInvForProInfoDTO activityInvForProInfoDTO;
	/**订单信息 **/
	private ActivityInvForProOrderDTO activityInvForProOrderDTO;
	public ActivityInvForProInfoDTO getActivityInvForProInfoDTO() {
		return activityInvForProInfoDTO;
	}
	public void setActivityInvForProInfoDTO(
			ActivityInvForProInfoDTO activityInvForProInfoDTO) {
		this.activityInvForProInfoDTO = activityInvForProInfoDTO;
	}
	public ActivityInvForProOrderDTO getActivityInvForProOrderDTO() {
		return activityInvForProOrderDTO;
	}
	public void setActivityInvForProOrderDTO(
			ActivityInvForProOrderDTO activityInvForProOrderDTO) {
		this.activityInvForProOrderDTO = activityInvForProOrderDTO;
	}
	@Override
	public String toString() {
		return "ActivityInvForProOrderAndProInfoDTO [activityInvForProInfoDTO="
				+ activityInvForProInfoDTO + ", activityInvForProOrderDTO="
				+ activityInvForProOrderDTO + "]";
	}
	
	
	
}
