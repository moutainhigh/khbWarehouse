package com.yeepay.g3.core.activity.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.yeepay.g3.core.activity.dao.ActivityCouponDao;
import com.yeepay.g3.core.activity.entity.ActivityCoupon;
import com.yeepay.g3.utils.persistence.mybatis.GenericDaoDefault;

/**
 * @Description 优惠券信息数据服务实现类
 * @author zhenping.zhou
 * @CreateTime 2016年3月25日 下午2:05:44
 * @version 1.0
 */
@Repository
public class ActivityCouponDaoImpl extends GenericDaoDefault<ActivityCoupon> implements
		ActivityCouponDao {

	@Override
	public void delete(ActivityCoupon arg0) {
	}

	@Override
	public ActivityCoupon get(Serializable arg0) {
		return null;
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		return 0;
	}

	@Override
	public ActivityCoupon selectByPrimaryKey(Long id) {
		
		return (ActivityCoupon)this.queryOne("selectByPrimaryKey", id);
	}

	@Override
	public int updateByPrimaryKey(ActivityCoupon record) {
		return 0;
	}

	@Override
	public List<ActivityCoupon> selectByParams(Map<String, Object> params) {
		return null;
	}

}
