/**
 * @descrption
 * @author 陈大涛
 * 2016-7-27下午5:05:22
 */
package com.yeepay.g3.core.activity.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.yeepay.g3.core.activity.dao.ActivityInvForProOrderDao;
import com.yeepay.g3.core.activity.entity.ActivityInvForProOrder;
import com.yeepay.g3.utils.persistence.mybatis.GenericDaoDefault;

/**
 * @author 陈大涛
 * 2016-7-27下午5:05:22
 */
@Repository
public class ActivityInvForProOrderDaoImpl extends GenericDaoDefault<ActivityInvForProOrder> implements ActivityInvForProOrderDao {

	@Override
	public int deleteByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelective(ActivityInvForProOrder record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ActivityInvForProOrder selectByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKey(ActivityInvForProOrder record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<ActivityInvForProOrder> selectByFlowParams(
			Map<String, Object> params) {
		return this.query("selectByFlowParams", params);
	}



}