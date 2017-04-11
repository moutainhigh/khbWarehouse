/**
 * 
 */
package com.yeepay.g3.boss.activity.controller.invforpro;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.yeepay.g3.boss.activity.controller.ActivityBaseController;
import com.yeepay.g3.facade.activity.dto.ActivityInvForProInfoDTO;
import com.yeepay.g3.facade.activity.dto.ActivityInvForProRuleXTDTO;
import com.yeepay.g3.facade.activity.enums.InvForProInfoStatusEnum;
import com.yeepay.g3.facade.activity.facade.ActivityInvForProInfoFacade;
import com.yeepay.g3.facade.activity.facade.ActivityInvForProRuleFacade;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;

/**
 * @Description 投资换产品管理Controller
 * @author hongbin.kang
 * @CreateTime 2016年7月27日 下午1:04:26
 * @version 1.0
 */
@Controller
@RequestMapping("/invForPro")
public class InvForProController extends ActivityBaseController {

	private Logger logger = LoggerFactory.getLogger(InvForProController.class);
//	// 投资换产品信息远程服务接口
//	private ActivityInvForProInfoFacade activityInvForProFacadeImpl = RemoteServiceFactory
//			.getService("http://localhost:8080/activity-hessian/hessian",
//					RemotingProtocol.HESSIAN, ActivityInvForProInfoFacade.class);
//	// 投资换产品规则信息远程服务接口
//	private ActivityInvForProRuleFacade activityInvForProRuleFacadeImpl = RemoteServiceFactory
//			.getService("http://localhost:8080/activity-hessian/hessian",
//					RemotingProtocol.HESSIAN, ActivityInvForProRuleFacade.class);

	 private ActivityInvForProInfoFacade activityInvForProFacadeImpl =
	 RemoteServiceFactory
	 .getService(ActivityInvForProInfoFacade.class);
	 //投资换产品规则信息远程服务接口
	 private ActivityInvForProRuleFacade activityInvForProRuleFacadeImpl =
	 RemoteServiceFactory
	 .getService(ActivityInvForProRuleFacade.class);

	/**
	 * 跳转至投资换产品新增页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toAddInvForPro")
	public String toAddInvForPro(Model model) {
		List<ActivityInvForProRuleXTDTO> ruleDtoList = activityInvForProRuleFacadeImpl
				.selectEffInvForProRuleList();
		if (null != ruleDtoList) {
			model.addAttribute("ruleDtoList", ruleDtoList);
		}
		return "invForProInfo/addInvForProInfo";
	}

	/**
	 * 保存投资换产品
	 * 
	 * @return
	 */
	@RequestMapping(value = "/addInvForPro")
	public String addInvForPro(
			@ModelAttribute ActivityInvForProInfoDTO invForProInfoDto,
			HttpSession session,
			HttpServletRequest requset,
			HttpServletResponse response,
			@RequestParam(required = false, value = "ruleIdStr") String ruleIdStr) {
		invForProInfoDto.setCreatePerson(this.getCurrentUser(session));
		invForProInfoDto.setCreateTime(new Date());
		invForProInfoDto.setUsedNum(0);// 初始化使用数量为0

		// 根据前台name名称得到上传的文件
		MultipartFile imgWX = ((MultipartRequest) requset)
				.getFile("activityImgForWX");
		MultipartFile imgPC = ((MultipartRequest) requset)
				.getFile("activityImgForPC");
		// 定义一个数组，用于保存可上传的文件类型
		List fileTypes = new ArrayList();
		fileTypes.add("jpg");
		fileTypes.add("jpeg");
		fileTypes.add("bmp");
		fileTypes.add("png");
		String fileNameWX = imgWX.getOriginalFilename();
		if (!(fileNameWX == null || "".equals(fileNameWX))) {
			String extensionName = fileNameWX.substring(fileNameWX
					.lastIndexOf(".") + 1);
			if (fileTypes.contains(extensionName)) {
				// 扩展名合法
				try { // 验证图片大小
					if (imgWX.getSize() > 1048576) {
						logger.error("[addInvForPro] 图片大小超过1M imgWX大小={}",
								imgWX.getSize());
					} else {
						byte[] content = imgWX.getBytes();
						invForProInfoDto.setProductImg(content);
					}
				} catch (IOException e) {
					logger.error("[addInvForPro] activityImgForWX异常 e={}",e);
				}
			}
		}
		// 验证pc图片
		String fileNamePC = imgPC.getOriginalFilename();
		if (!(fileNamePC == null || "".equals(fileNamePC))) {
			String extensionName = fileNamePC.substring(fileNamePC
					.lastIndexOf(".") + 1);
			if (fileTypes.contains(extensionName)) {
				// 扩展名合法
				try {
					// 验证图片大小
					if (imgPC.getSize() > 1048576) {
						logger.error("[addInvForPro] 图片大小超过1M imgPC大小={}",
								imgPC.getSize());
					} else {
						byte[] content = imgPC.getBytes();
						invForProInfoDto.setProductImgPc(content);
					}

				} catch (IOException e) {
					logger.error("[addInvForPro] activityImgForPC异常 e={}",e);
				}
			}
		}

		activityInvForProFacadeImpl.addActivityInvForProInfo(invForProInfoDto,
				ruleIdStr);
		return "invForProInfo/queryInvForProInfoList";
	}

	/**
	 * 查询所有投资换产品列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryInvForProList")
	public String queryInvForProList() {
		return "invForProInfo/queryInvForProInfoList";
	}

	/**
	 * 查看投资换产品的详情
	 */
	@RequestMapping(value = "/invForProDetail")
	public String invForProInfoDetail(
			@RequestParam(required = true, value = "id") String id, Model model) {
		ActivityInvForProInfoDTO invForProInfoDto = new ActivityInvForProInfoDTO();
		invForProInfoDto = activityInvForProFacadeImpl
				.selectInvForProInfoById(Long.valueOf(id));
		List<ActivityInvForProRuleXTDTO> ruleDtoList = activityInvForProRuleFacadeImpl
				.selectRuleByProductId(Long.valueOf(id));
		model.addAttribute("invForProInfoDto", invForProInfoDto);
		model.addAttribute("ruleDtoList", ruleDtoList);
		return "invForProInfo/invForProInfoDetail";
	}

	/**
	 * 查询待审核列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryCheckingInvForProList")
	public String queryCheckingInvForProList() {
		return "invForProInfo/checkingInvForProInfoList";
	}

	/**
	 * 投资换产品审核
	 * 
	 * @param id
	 * @param InvForProInfoStatus
	 * @param version
	 * @param session
	 */
	@RequestMapping(value = "/invForProCheck")
	@ResponseBody
	public String invForProCheck(
			@RequestParam(required = true, value = "id") String id,
			@RequestParam(required = true, value = "version") String version,
			@RequestParam(required = true, value = "status") String status,
			HttpSession session) {
		ActivityInvForProInfoDTO invForProInfoDto = new ActivityInvForProInfoDTO();
		// id、version为必传项
		invForProInfoDto.setId(Long.valueOf(id));
		invForProInfoDto.setVersion(Long.valueOf(version));
		// 根据传递的参数将投资换产品状态置为有效或者退回
		if (status.equals(String.valueOf(InvForProInfoStatusEnum.EFFECTIVE))) {
			invForProInfoDto.setStatus(InvForProInfoStatusEnum.EFFECTIVE);
		} else if (status.equals(String
				.valueOf(InvForProInfoStatusEnum.RETURN_BACK))) {
			invForProInfoDto.setStatus(InvForProInfoStatusEnum.RETURN_BACK);
		}
		// 审核人、审核时间
		invForProInfoDto.setOperatorer(this.getCurrentUser(session));
		invForProInfoDto.setOperatorTime(new Date());
		activityInvForProFacadeImpl
				.updateActivityInvForProStatusById(invForProInfoDto);
		return "SUCCESS";
	}

	/**
	 * 去修改投资换产品页面
	 */
	@RequestMapping(value = "/toModifyInvForProInfo")
	public String toModifyInvForProInfo(
			@RequestParam(required = true, value = "id") String id, Model model) {
		ActivityInvForProInfoDTO invForProInfoDto = new ActivityInvForProInfoDTO();
		if (null != id) {
			invForProInfoDto = activityInvForProFacadeImpl
					.selectInvForProInfoById(Long.valueOf(id));
		}
		// 规则列表
		List<ActivityInvForProRuleXTDTO> ruleDtoList = activityInvForProRuleFacadeImpl
				.selectEffInvForProRuleList();
		// 该产品的选中的规则
		List<ActivityInvForProRuleXTDTO> checkedRuleDtoList = activityInvForProRuleFacadeImpl
				.selectRuleByProductId(Long.valueOf(id));
		StringBuffer str = new StringBuffer();
		for (ActivityInvForProRuleXTDTO ruleDto : checkedRuleDtoList) {
			str.append(ruleDto.getId().toString()).append("#");
		}
		String checkedIds = null;
		if (str.length() > 0) {
			checkedIds = str.toString().substring(0, str.length() - 1);

		}
		ruleDtoList.addAll(checkedRuleDtoList);
		model.addAttribute("checkedIds", checkedIds);
		model.addAttribute("ruleDtoList", ruleDtoList);
		model.addAttribute("invForProInfoDto", invForProInfoDto);
		return "invForProInfo/modifyInvForProInfo";
	}

	/**
	 * 修改投资换产品
	 */
	@RequestMapping(value = "/modifyInvForProInfo")
	public String modifyInvForProInfo(
			@ModelAttribute ActivityInvForProInfoDTO invForProInfoDto,
			MultipartRequest request,
			@RequestParam(required = false, value = "ruleIdStr") String ruleIdStr) {
		// 定义一个数组，用于保存可上传的文件类型
		List fileTypes = new ArrayList();
		fileTypes.add("jpg");
		fileTypes.add("jpeg");
		fileTypes.add("bmp");
		fileTypes.add("png");
		if (!request.getFile("activityImgForWX").isEmpty()) {
			MultipartFile imgWX = request.getFile("activityImgForWX");
			String fileNameWX = imgWX.getOriginalFilename();
			if (!(fileNameWX == null || "".equals(fileNameWX))) {
				String extensionName = fileNameWX.substring(fileNameWX
						.lastIndexOf(".") + 1);
				if (fileTypes.contains(extensionName)) {
					// 扩展名合法
					try {
						// 验证图片大小
						if (imgWX.getSize() > 1048576) {
							logger.error(
									"[modifyInvForProInfo] 图片大小超过1M imgWX大小={}",
									imgWX.getSize());
						} else {
							byte[] content = imgWX.getBytes();
							if (null != content) {
								invForProInfoDto.setProductImg(content);
								//删除redis 图片缓存
//								SmartCacheUtils.remove("productImgForId"+invForProInfoDto.getId());
							}
						}
					} catch (IOException e) {
						logger.error("[modifyInvForProInfo] activityImgForWX 异常 e={}",e);
					}
				}
			}
		}
		if (!request.getFile("activityImgForPC").isEmpty()) {
			MultipartFile imgPC = request.getFile("activityImgForPC");
			String fileNamePC = imgPC.getOriginalFilename();
			if (!(fileNamePC == null || "".equals(fileNamePC))) {
				String extensionName = fileNamePC.substring(fileNamePC
						.lastIndexOf(".") + 1);
				if (fileTypes.contains(extensionName)) {
					// 扩展名合法
					try {
						// 验证图片大小
						if (imgPC.getSize() > 1048576) {
							logger.error(
									"[modifyInvForProInfo] 图片大小超过1M imgPC大小={}",
									imgPC.getSize());
						} else {
							byte[] content = imgPC.getBytes();
							if (null != content) {
								invForProInfoDto.setProductImgPc(content);
							}
						}
					} catch (IOException e) {
						logger.error("[modifyInvForProInfo] activityImgForPC 异常 e={}",e);
					}
				}
			}
		}
		
		if (null != invForProInfoDto.getId()
				&& null != invForProInfoDto.getVersion()) {
			activityInvForProFacadeImpl.updateActivityInvForProAndRule(
					invForProInfoDto, ruleIdStr);
		}
		return "invForProInfo/checkingInvForProInfoList";
	}

	/**
	 * 查看图片
	 * @throws IOException 
	 */
	@RequestMapping(value = "/lookInvForProImg")
	public void lookInvForProImg(
			@RequestParam(required = true, value = "id") String id,
			@RequestParam(required = true, value = "type") String type,
			Model model, HttpServletRequest requset,
			HttpServletResponse response) throws IOException {
		ActivityInvForProInfoDTO invForProInfoDto = new ActivityInvForProInfoDTO();
		byte[] imageData = null;
		OutputStream output=null;InputStream in=null;
		if ("PC".equals(type)) {
				invForProInfoDto = activityInvForProFacadeImpl
						.selectAllInvForProInfoById(Long.valueOf(id));
				imageData=invForProInfoDto.getProductImgPc();
		}else{
				invForProInfoDto = activityInvForProFacadeImpl
						.selectAllInvForProInfoById(Long.valueOf(id));
				imageData=invForProInfoDto.getProductImg();
		}
		try {
			 output = response.getOutputStream();
			 in = new ByteArrayInputStream(imageData);
			int len;
			byte[] buf = new byte[1024];
			while ((len = in.read(buf)) != -1) {
				output.write(buf, 0, len);
			}
			output.flush();
		} catch (IOException e) {
			logger.error("[lookInvForProImg] 异常e={}",e);
		}
		finally{
			if(output!=null){
				output.close();
			}
			if(in!=null){
				in.close();
			}
		}
	}

}
