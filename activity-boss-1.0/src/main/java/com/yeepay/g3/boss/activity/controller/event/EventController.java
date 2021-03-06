/**
 * 
 */
package com.yeepay.g3.boss.activity.controller.event;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yeepay.g3.boss.activity.controller.ActivityBaseController;
import com.yeepay.g3.boss.activity.controller.grant.BatchGrantController;
import com.yeepay.g3.facade.activity.dto.ActivityCouponDTO;
import com.yeepay.g3.facade.activity.dto.ActivityEventDTO;
import com.yeepay.g3.facade.activity.enums.CouponStatusEnum;
import com.yeepay.g3.facade.activity.facade.ActivityCouponFacade;
import com.yeepay.g3.facade.activity.facade.ActivityEventFacade;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;

/**
 * @Description 活动事件管理Controller
 * @author zhenping.zhou
 * @CreateTime 2016年3月31日 下午4:17:55
 * @version 1.0
 */
@Controller
@RequestMapping("/event")
public class EventController extends ActivityBaseController {
	
//	private ActivityEventFacade activityEventFacadeImpl = RemoteServiceFactory
//			.getService("http://localhost:8080/activity-hessian/hessian", RemotingProtocol.HESSIAN, ActivityEventFacade.class);
//	private ActivityCouponFacade activityCouponFacadeImpl = RemoteServiceFactory
//			.getService("http://localhost:8080/activity-hessian/hessian", RemotingProtocol.HESSIAN, ActivityCouponFacade.class);
	private ActivityCouponFacade activityCouponFacadeImpl = RemoteServiceFactory
			.getService(ActivityCouponFacade.class);
	
	private ActivityEventFacade activityEventFacadeImpl = RemoteServiceFactory
			.getService(ActivityEventFacade.class);
	private Logger logger = LoggerFactory.getLogger(EventController.class);
	
	/**
	 * 查询所有活动事件列表
	 * @return
	 */
	@RequestMapping(value = "/queryEventList")
	public String queryEventList() {
		return "event/queryEventList";
	}

	/**
	 * 跳转至活动事件新增页面
	 * @return
	 */
	@RequestMapping(value = "/toAddEvent")
	public String toAddEvent(Model model) {
		ActivityCouponDTO activityCouponDto = new ActivityCouponDTO();
		activityCouponDto.setCouponStatus(CouponStatusEnum.EFFECTIVE); //有效
		List<ActivityCouponDTO> couponList = activityCouponFacadeImpl.selectListByParams(activityCouponDto);
		model.addAttribute("couponList", couponList);
		
		return "event/addEvent";
	}
	
	/**
	 * 保存活动事件
	 * @return
	 */
	@RequestMapping(value = "/addEvent")
	public String addEvent(@ModelAttribute ActivityEventDTO eventDto,
			@RequestParam(required = true, value = "coupons") String coupons,
			HttpSession session) {
		eventDto.setCreator(this.getCurrentUser(session));
		activityEventFacadeImpl.addEvent(eventDto, coupons);
		return "event/queryEventList";
	}

	/**
	 * 事件查看
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/eventDetail")
	public String eventDetail(
			@RequestParam(required = true, value = "id") String id,
			Model model) {
		ActivityEventDTO eventDto = activityEventFacadeImpl.selectEventById(Long.valueOf(id));
		model.addAttribute("eventDto", eventDto);
		logger.info("[eventDetail] eventDto={}",eventDto);
		List<ActivityCouponDTO> couponDtoList = activityCouponFacadeImpl.selecEventCouponList(eventDto.getEventCode());
		model.addAttribute("couponDtoList", couponDtoList);
		logger.info("[eventDetail] couponDtoList={}",couponDtoList);
		return "event/eventDetail";
	}
	
	@RequestMapping(value="/toUpdateEvent")
	public String toUpdateEvent(@RequestParam(required = true, value = "id")String id, Model model){
		//根据id查询事件信息
		ActivityEventDTO eventDto = activityEventFacadeImpl.selectEventById(Long.valueOf(id));
		logger.info("[toUpdateEvent] eventDto={}",eventDto);
		model.addAttribute("eventDto", eventDto);
		//根据事件id查询关联优惠券列表 TODO
		List<ActivityCouponDTO> list = activityCouponFacadeImpl.selecEventCouponList(eventDto.getEventCode());
		logger.info("[toUpdateEvent] list={}",list);
		/*logger.info("[toUpdateEvent] list={}",list);
		model.addAttribute("list",list);*/
		StringBuilder conList = new StringBuilder();
		for(int i = 0; i<list.size(); i++){
			conList.append(list.get(i).getId()).append(";");
		}
		model.addAttribute("conList", conList);
		//查询所有优惠券列表
		ActivityCouponDTO activityCouponDto = new ActivityCouponDTO();
		activityCouponDto.setCouponStatus(CouponStatusEnum.EFFECTIVE); //有效
		activityCouponDto.setValidityDate(new Date());
		List<ActivityCouponDTO> couponList = activityCouponFacadeImpl.selectEffCouponList(activityCouponDto);
		logger.info("[toUpdateEvent] couponList={}",couponList);
		model.addAttribute("couponList", couponList);
		return "event/updateEvent";
	}
	@RequestMapping(value="/updateEvent")
	public String updateEvent(@ModelAttribute ActivityEventDTO eventDto,@RequestParam(required = true, value = "coupons") String coupons,HttpSession session){
		logger.info("[updateEvent] eventDto={},coupons={}",eventDto,coupons);
    	eventDto.setCreateTime(new Date());
    	eventDto.setCreator(this.getCurrentUser(session));
    	activityEventFacadeImpl.updateEvent(eventDto, coupons);
		return "event/queryEventList";
	}
}
