/**
 * @author cdt
 * @date 2016-5-16
 * @time 下午4:10:41
 */
package com.yeepay.g3.core.activity.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.db2.jcc.t4.ac;
import com.yeepay.g3.core.activity.entity.ActivityPrize;
import com.yeepay.g3.core.activity.entity.ActivityUserPrize;
import com.yeepay.g3.core.activity.service.ActivityPrizeService;
import com.yeepay.g3.core.activity.service.ActivityUserPrizeService;
import com.yeepay.g3.core.activity.utils.EntityDTOConvert;
import com.yeepay.g3.facade.activity.dto.ActivityPrizeDTO;
import com.yeepay.g3.facade.activity.dto.ActivityUserPrizeDTO;
import com.yeepay.g3.facade.activity.enums.PrizeStatusEnum;
import com.yeepay.g3.facade.activity.facade.ActivityPrizeFacade;
import com.yeepay.g3.facade.activity.facade.ActivityUserPrizeFacade;

/**
 * @author hongbin.kang
 *
 */
@Service
public class ActivityUserPrizeFacadeImpl implements ActivityUserPrizeFacade {
	@Autowired
	private ActivityUserPrizeService activityUserPrizeServiceImpl;
	

	@Override
	public ActivityUserPrizeDTO selectUserPrizeById(Long id) {
		ActivityUserPrize activityUserPrize = new ActivityUserPrize();
		activityUserPrize = activityUserPrizeServiceImpl.selectUserPrizeById(id);
		ActivityUserPrizeDTO userPrizeDto = EntityDTOConvert.toTarget(activityUserPrize, ActivityUserPrizeDTO.class);
		return userPrizeDto;
	}

	@Override
	public void updateActivityUserPrizeById(ActivityUserPrizeDTO userPrizeDto) {
		ActivityUserPrize activityUserPrize = new ActivityUserPrize();
		activityUserPrize = EntityDTOConvert.toTarget(userPrizeDto, ActivityUserPrize.class);
		activityUserPrizeServiceImpl.updateActivityUserPrizeById(activityUserPrize);
	}

	@Override
	public List<ActivityUserPrizeDTO> selectUserPrizeByMemberNo(String memberNo) {
		List<ActivityUserPrize> userPrizeList = new ArrayList<ActivityUserPrize>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("memberNo", memberNo);
		userPrizeList = activityUserPrizeServiceImpl.selectUserPrizeByMemberNo(params);
		List<ActivityUserPrizeDTO> userPrizeDtoList = EntityDTOConvert.toTragetList(userPrizeList, ActivityUserPrizeDTO.class);
		return userPrizeDtoList;
	}

	@Override
	public List<ActivityUserPrizeDTO> selectUserPrizeNewList(int i) {
		List<ActivityUserPrize> userPrizeNewList= activityUserPrizeServiceImpl.selectUserPrizeNewList(i);
		List<ActivityUserPrizeDTO> userPrizeDtoList = EntityDTOConvert.toTragetList(userPrizeNewList, ActivityUserPrizeDTO.class);
		return userPrizeDtoList;
		
	}


	
}
