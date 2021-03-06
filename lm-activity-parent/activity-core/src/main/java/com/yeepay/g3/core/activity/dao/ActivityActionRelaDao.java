package com.yeepay.g3.core.activity.dao;

import java.util.List;
import java.util.Map;

import com.yeepay.g3.core.activity.entity.ActivityAction;
import com.yeepay.g3.core.activity.entity.ActivityActionRela;
import com.yeepay.g3.utils.persistence.GenericDao;

public interface ActivityActionRelaDao extends GenericDao<ActivityActionRela> {

	public int deleteByPrimaryKey(Long id);

	public ActivityActionRela selectByPrimaryKey(Long id);

	public int updateByPrimaryKey(ActivityActionRela record);
	
	/**
	 * 根据活动id，查询对应事件集合
	 * @author cdt
	 * @date 2016-5-19
	 * @time 上午9:33:16
	 */
	public List<ActivityAction> queryActionByActivityId(Long id);
	
	/**
	 * 根据活动id，删除活动-事件关联
	 * @author cdt
	 * @date 2016-5-19
	 * @time 上午11:33:32
	 */
	public void deleteByActivityId(Long id);
	
	/**
	 * 根据事件编码查询活动是否进行
	 * @author 陈大涛
	 * 2016-6-1下午6:44:14
	 */
	public Integer queryActivityByActionCode(Map<String,String> params);
}