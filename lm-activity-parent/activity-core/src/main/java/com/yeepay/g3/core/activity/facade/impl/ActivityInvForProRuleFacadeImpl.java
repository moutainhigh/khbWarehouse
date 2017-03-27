package com.yeepay.g3.core.activity.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.activity.entity.ActivityInvForProRuleXT;
import com.yeepay.g3.core.activity.service.ActivityInvForProRuleService;
import com.yeepay.g3.core.activity.utils.EntityDTOConvert;
import com.yeepay.g3.facade.activity.dto.ActivityInvForProRuleXTDTO;
import com.yeepay.g3.facade.activity.facade.ActivityInvForProRuleFacade;
@Service
public class ActivityInvForProRuleFacadeImpl implements ActivityInvForProRuleFacade {
	
	@Autowired
	private ActivityInvForProRuleService activityInvForProRuleServiceImpl;

	@Override
	public ActivityInvForProRuleXTDTO selectInvForProRuleById(long id) {
		ActivityInvForProRuleXT activityInvForProRule = new ActivityInvForProRuleXT();
		activityInvForProRule = activityInvForProRuleServiceImpl.selectInvForProRuleById(id);
		ActivityInvForProRuleXTDTO invForProRuleDto = new ActivityInvForProRuleXTDTO();
		invForProRuleDto = EntityDTOConvert.toTarget(activityInvForProRule, ActivityInvForProRuleXTDTO.class);
		return invForProRuleDto;
	}


	/*@Override
	public List<ActivityInvForProRuleXTDTO> selectListByParams(ActivityInvForProRuleXTDTO InvForProRuleDto) {
		// TODO Auto-generated method stub
		return null;
	}
*/

	@Override
	public List<ActivityInvForProRuleXTDTO> selectEffInvForProRuleList() {
		List<ActivityInvForProRuleXT> ruleList = activityInvForProRuleServiceImpl.selectEffInvForProRuleList();
		List<ActivityInvForProRuleXTDTO> ruleListDto = new ArrayList<ActivityInvForProRuleXTDTO>();
		ruleListDto = EntityDTOConvert.toTragetList(ruleList, ActivityInvForProRuleXTDTO.class);
		return ruleListDto;
	}

	@Override
	public void updateActivityInvForProRuleById(
			ActivityInvForProRuleXTDTO InvForProDto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addActivityInvForProRule(
			ActivityInvForProRuleXTDTO invForProDto) {
		ActivityInvForProRuleXT activityInvForProRule = new ActivityInvForProRuleXT();
		activityInvForProRule = EntityDTOConvert.toTarget(invForProDto, ActivityInvForProRuleXT.class);
		activityInvForProRuleServiceImpl.addActivityInvForProRule(activityInvForProRule);

	}


	@Override
	public List<ActivityInvForProRuleXTDTO> selectRuleByProductId(Long id) {
		List<ActivityInvForProRuleXT> list = activityInvForProRuleServiceImpl.selectRuleByProductId(id);
		List<ActivityInvForProRuleXTDTO> dtoList = new ArrayList<ActivityInvForProRuleXTDTO>();
		dtoList = EntityDTOConvert.toTragetList(list, ActivityInvForProRuleXTDTO.class);
		return dtoList;
	}


	@Override
	public List<ActivityInvForProRuleXTDTO> selectForProRuleList() {
		// TODO Auto-generated method stub
		List<ActivityInvForProRuleXT> list = activityInvForProRuleServiceImpl.selectForProRuleList();
		List<ActivityInvForProRuleXTDTO> dtoList = new ArrayList<ActivityInvForProRuleXTDTO>();
		dtoList = EntityDTOConvert.toTragetList(list, ActivityInvForProRuleXTDTO.class);
		return dtoList;
	}

	

}