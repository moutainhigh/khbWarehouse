package com.yeepay.g3.boss.activity.controller.activity;
/**
 * @author cdt
 * @date 2016-5-18
 * @time 上午9:10:34
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yeepay.g3.boss.activity.controller.ActivityBaseController;
import com.yeepay.g3.boss.activity.utils.UploadFile;
import com.yeepay.g3.facade.activity.dto.ActivityActionDTO;
import com.yeepay.g3.facade.activity.dto.ActivityInfoDTO;
import com.yeepay.g3.facade.activity.dto.ActivityWXSendMessageDTO;
import com.yeepay.g3.facade.activity.enums.ActivityWXSendMessageEnum;
import com.yeepay.g3.facade.activity.facade.ActivityActionFacade;
import com.yeepay.g3.facade.activity.facade.ActivityActionRelaFacade;
import com.yeepay.g3.facade.activity.facade.ActivityFacade;
import com.yeepay.g3.facade.activity.facade.ActivityWXSendMessageFacade;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;

/**
 * @author cdt
 * @date 2016-5-18
 * @time 上午9:10:34
 */
@Controller
@RequestMapping("/activity")
public class ActivityController extends ActivityBaseController {
	
	/*private ActivityActionFacade activityActionFacadeImpl = RemoteServiceFactory
			.getService("http://localhost:8080/activity-hessian/hessian", RemotingProtocol.HESSIAN, ActivityActionFacade.class);

	private ActivityFacade activityFacadeImpl = RemoteServiceFactory
			.getService("http://localhost:8080/activity-hessian/hessian", RemotingProtocol.HESSIAN, ActivityFacade.class);
	
	private ActivityActionRelaFacade activityActionRelaFacadeImpl = RemoteServiceFactory
			.getService("http://localhost:8080/activity-hessian/hessian", RemotingProtocol.HESSIAN, ActivityActionRelaFacade.class);*/
	
	private ActivityActionFacade activityActionFacadeImpl = RemoteServiceFactory
	.getService(ActivityActionFacade.class);
	
	private ActivityFacade activityFacadeImpl = RemoteServiceFactory
			.getService(ActivityFacade.class);
	
	private ActivityActionRelaFacade activityActionRelaFacadeImpl = RemoteServiceFactory
			.getService(ActivityActionRelaFacade.class);
	private Logger logger = LoggerFactory.getLogger(ActivityController.class);
	/**
	 * 查询活动集合列表
	 * @author cdt
	 * @date 2016-5-18
	 * @time 上午9:39:57
	 */
	@RequestMapping(value="queryActivityList")
	public String queryActivityList(){
		return "activity/queryActivityList";
	}
	
	/**
	 * 查询审核集合列表
	 * @author cdt
	 * @date 2016-5-18
	 * @time 上午9:58:36
	 */
	@RequestMapping(value="queryActivityCheckList")
	public String queryActivityCheckList(){
		return "activity/queryActivityCheckList";
	}
	
	/**
	 *去新增活动页面 
	 * @author cdt
	 * @date 2016-5-18
	 * @time 上午9:42:54
	 */
	@RequestMapping(value="toAddActivity")
	public String toAddActivity(Model model) throws Exception{
		List<ActivityActionDTO> activityActionListDto=activityActionFacadeImpl.queryActionAll();
		model.addAttribute("activityActionListDto", activityActionListDto);
		return "activity/addActivity";
	}
	
	/**
	 * 保存新增活动
	 * @author cdt
	 * @throws Exception 
	 * @date 2016-5-18
	 * @time 上午9:45:16
	 */
	@RequestMapping(value="addActivity")
	public String addActivity(Model model,@ModelAttribute ActivityInfoDTO activityInfoDto,@RequestParam(value = "activityImg", required = false) MultipartFile activityImg,
			@RequestParam(required = true, value = "actions") String actions,
			HttpSession session,HttpServletRequest request) throws Exception{
		logger.info("[addActivity] 参数 ActivityInfoDTO={},actions={}",activityInfoDto,actions);
		String creator=getCurrentUser(session);
		if(!activityImg.isEmpty()){
			String reg = ".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$";
			 Pattern pattern = Pattern.compile(reg);
			  Matcher matcher = pattern.matcher(activityImg.getOriginalFilename());
			  boolean flag= matcher.matches();
			if(!flag){
				model.addAttribute("picture",false);
				toAddActivity(model);
				return "activity/addActivity";	
			}
			activityInfoDto.setImg(activityImg.getBytes());
		}
		activityFacadeImpl.insertActivity(activityInfoDto, actions, creator);
		return "activity/queryActivityList";
	}
	
	/**
	 * 查看活动详情
	 * @author cdt
	 * @throws IOException 
	 * @date 2016-5-18
	 * @time 上午9:46:46
	 */
	@RequestMapping(value="activityDetail")
	public String activityDetail(Model model,@RequestParam(required = true, value = "id") Long id)
			 {
		logger.info("[activityDetail]  参数： id={}",id);
		//查询活动详情
		ActivityInfoDTO activityInfoDto= activityFacadeImpl.queryActivityById(id);
		logger.info("[activityDetail] activityInfoDto={}",activityInfoDto);
		//查询此活动对应事件集合
		List<ActivityActionDTO> activityActionListDto=activityActionRelaFacadeImpl.queryActionByActivityId(id);
		logger.info("[activityDetail] activityActionListDto={}",activityActionListDto);
		model.addAttribute("activityInfoDto", activityInfoDto);
		model.addAttribute("activityActionListDto", activityActionListDto);
		return "activity/activityDetail";
		
	}
	
	/**
	 * 查看活动图片
	 * @author 陈大涛
	 * 2016-6-3下午7:17:43
	 */
	@RequestMapping(value="lookActivityImg")
	public void lookActivityImg(@RequestParam(required = true, value = "id") Long id,
			HttpServletRequest request,HttpServletResponse response){
		logger.info("[lookActivityImg]  参数： id={}",id);
		//查询活动详情
		ActivityInfoDTO activityInfoDto= activityFacadeImpl.queryActivityById(id);
		logger.info("[lookActivityImg] activityInfoDto={}",activityInfoDto);
		try {
			byte[] data = activityInfoDto.getImg();
			OutputStream out = response.getOutputStream();
			byte[] buff = new byte[(int)data.length ];
			int i=0;
			InputStream in=new ByteArrayInputStream(data);
			while((i= in.read(buff))!=-1){
				out.write(buff);
			}
			out.close();
			in.close();	
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * 去修改活动页面
	 * @author cdt
	 * @date 2016-5-18
	 * @time 上午9:49:16
	 */
	@RequestMapping(value="toUpdateActivity")
	public String toUpdateActivity(Model model,@RequestParam(required = true, value = "id") Long id){
		logger.info("[toUpdateActivity]  参数： id={}",id);
		//查询活动详情
		ActivityInfoDTO activityInfoDto= activityFacadeImpl.queryActivityById(id);
		logger.info("[toUpdateActivity] activityInfoDto={}",activityInfoDto);
		//查询此活动对应事件集合
		List<ActivityActionDTO> activityActionListDto=activityActionRelaFacadeImpl.queryActionByActivityId(id);
		StringBuilder actionList=new StringBuilder("");
		for(ActivityActionDTO itmes:activityActionListDto){
			actionList.append(itmes.getId()).append(";");
		}
		logger.info("[toUpdateActivity] ActionList={}",actionList);
		//查询所有事件
		List<ActivityActionDTO> activityActionListAllDto=activityActionFacadeImpl.queryActionAll();
		logger.info("[toUpdateActivity] activityActionListAllDto={}",activityActionListAllDto);
		model.addAttribute("activityInfoDto", activityInfoDto);
		model.addAttribute("actionList", actionList);
		model.addAttribute("activityActionListAllDto", activityActionListAllDto);
		return "activity/updateActivity";
	}
	
	/**
	 * 修改活动
	 * @author cdt
	 * @date 2016-5-18
	 * @time 上午9:50:24
	 */
	@RequestMapping(value="updateActivity")
	public String updateActivity(Model model,@ModelAttribute ActivityInfoDTO activityInfoDto,
			@RequestParam(value = "activityImg", required = false) MultipartFile activityImg,
			@RequestParam(required = true, value = "actions") String actions,
			HttpSession session) {
		logger.info("[updateActivity] 参数 ActivityInfoDTO={},actions={}",activityInfoDto,actions);
		try {
			if(!activityImg.isEmpty()){
				String reg = ".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$";
				 Pattern pattern = Pattern.compile(reg);
				  Matcher matcher = pattern.matcher(activityImg.getOriginalFilename());
				  boolean flag= matcher.matches();
				if(!flag){
					model.addAttribute("picture",false);
					toUpdateActivity(model, activityInfoDto.getId());
					return "activity/updateActivity";	
				}
				activityInfoDto.setImg(activityImg.getBytes());
			}
			activityFacadeImpl.updateActivity(activityInfoDto, actions, getCurrentUser(session));
		} catch (Exception e) {
			logger.info("[updateActivity] error={}",e);
		}
		
		return "activity/queryActivityList";
	}
	
	/**
	 * 审核活动
	 * @author cdt
	 * @date 2016-5-18
	 * @time 上午9:56:24
	 */
	@RequestMapping(value="checkActivity")
	@ResponseBody
	public String checkActivity(@ModelAttribute ActivityInfoDTO activityInfoDto,
			@RequestParam(required = true, value = "status") String status,
			HttpSession session){
		activityFacadeImpl.checkActivity(activityInfoDto, status,getCurrentUser(session));
		return "SUCCESS";
	}
	
	
}
