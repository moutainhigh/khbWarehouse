/**
 * @author cdt
 * @date 2016-5-16
 * @time 下午4:26:30
 */
package com.yeepay.g3.core.activity.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.activity.dao.ActivityPrizeDao;
import com.yeepay.g3.core.activity.dao.ActivityUserPrizeDao;
import com.yeepay.g3.core.activity.entity.ActivityPrize;
import com.yeepay.g3.core.activity.entity.ActivityUserPrize;
import com.yeepay.g3.core.activity.service.ActivityPrizeService;
import com.yeepay.g3.core.activity.service.ActivityUserPrizeService;
import com.yeepay.g3.facade.activity.dto.ActivityUserPrizeDTO;

/**
 * @author hongbin.kang
 *
 */
@Service
public class ActivityUserPrizeServiceImpl implements ActivityUserPrizeService {

	@Autowired
	private ActivityUserPrizeDao activityUserPrizeDaoImpl;
	
	@Override
	public ActivityUserPrize selectUserPrizeById(Long id) {
		ActivityUserPrize activityUserPrize = new ActivityUserPrize();
		activityUserPrize = (ActivityUserPrize) activityUserPrizeDaoImpl.queryOne("selectByPrimaryKey", id);
		return activityUserPrize;
	}
	@Override
	public void updateActivityUserPrizeById(ActivityUserPrize activityUserPrize) {
		activityUserPrizeDaoImpl.update(activityUserPrize);
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ActivityUserPrize> selectUserPrizeByMemberNo(
			Map<String, Object> params) {
		List<ActivityUserPrize> userPrizeList = new ArrayList<ActivityUserPrize>();
		userPrizeList = activityUserPrizeDaoImpl.query("selectUserPrizeByMemberNo", params);
		return userPrizeList;
	}
	@Override
	public List<ActivityUserPrize> selectUserPrizeNewList(int i) {
		List<ActivityUserPrize> list = activityUserPrizeDaoImpl.query("selectUserPrizeNewList", Long.valueOf(i));
		return list;
	}
	@Override
	public List<ActivityUserPrize> selectUserPrizeByPrizeId(Long id) {
		// TODO Auto-generated method stub
		List<ActivityUserPrize> list = activityUserPrizeDaoImpl.selectByPrizeId(id);
		return list;
	}
	

}