/**
 * @author cdt
 * @date 2016-5-16
 * @time 下午4:26:30
 */
package com.yeepay.g3.core.activity.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.activity.dao.ActivityActionDao;
import com.yeepay.g3.core.activity.dao.ActivityPrizeDao;
import com.yeepay.g3.core.activity.entity.ActivityAction;
import com.yeepay.g3.core.activity.entity.ActivityPrize;
import com.yeepay.g3.core.activity.service.ActivityActionService;
import com.yeepay.g3.facade.activity.dto.ActivityActionDTO;

/**
 * @author cdt
 * @date 2016-5-16
 * @time 下午4:26:30
 */
@Service
public class ActivityActionServiceImpl implements ActivityActionService {

	@Autowired
	private ActivityActionDao activityActionDaoImpl;
	@Autowired
	private ActivityPrizeDao activityPrizeDaoImpl;

	@Override
	public void insertActivityAction(ActivityAction action,String prizes,String odds,String versions,String levels){
		//新增事件
		activityActionDaoImpl.add(action);
		//修改奖品中奖概率,事件-奖品关联
		String[] prizeArray=prizes.split(",");
		String[] oddArray=odds.split(",");
		String[] levelArray=levels.split(",");
		String[] versionArray=versions.split(",");
		String[] oddArrays=new String[oddArray.length];
		String[] levelArrays=new String[levelArray.length];
		String[] versionArrays=new String[oddArray.length];
		//过滤空odds
		for(int i=0, j=0;i<oddArray.length;i++){
			if(null==oddArray[i]||"".equals(oddArray[i])){
				continue;
			}else{
				oddArrays[j]=oddArray[i];
				versionArrays[j]=versionArray[i];
				j++;
			}
		}
		//过滤空levels
		for(int i=0, j=0;i<levelArray.length;i++){
			if(null==levelArray[i]||"".equals(levelArray[i])){
				continue;
			}else{
				levelArrays[j]=levelArray[i];
				j++;
			}
		}
		for(int i=0, j=0;i<prizeArray.length;i++){
			ActivityPrize record=new ActivityPrize();
			record.setId(Long.parseLong(prizeArray[i]));
			record.setActionId(action.getId());
			record.setVersion(Long.parseLong(versionArrays[i]));
			record.setPrizeOdds(new BigDecimal(oddArrays[i]));
			record.setPrizeLevel(Integer.parseInt(levelArrays[i]));
			activityPrizeDaoImpl.update(record);
		}
	}

	@Override
	public ActivityAction getActionDetail(Long ActionId) {
		//查询事件
		ActivityAction activityAction= activityActionDaoImpl.get(ActionId);
		return activityAction;
	}

	@Override
	public void updateActivityAction(ActivityAction action, String prizes,
			String odds, String versions,String levels) {
				//更改事件
				activityActionDaoImpl.update(action);
			List<ActivityPrize> activityPrizeList=activityPrizeDaoImpl.selectByActionId(action.getId());
			List<ActivityPrize> deletePrizeList=new ArrayList<ActivityPrize>();
			String[] prizeArray=prizes.split(",");
			for(ActivityPrize itmes:activityPrizeList){
				boolean flag=false;
				for(int i=0;i<prizeArray.length;i++){
					if(itmes.getId().toString().equals(prizeArray[i])){
						flag=true;
						break;
					}else{
						continue;
					}
				}
				if(!flag){
					deletePrizeList.add(itmes);
				}
			}
			//清除没有被选中的奖品相关信息
			//未有乐观锁
			for(ActivityPrize itmes:deletePrizeList){
				itmes.setPrizeOdds(new BigDecimal(0) );
				itmes.setActionId(new Long(0));//制空为0
				activityPrizeDaoImpl.update(itmes);
			}
				//修改奖品中奖概率,事件-奖品关联
			String[] oddArray=odds.split(",");
			String[] levelArray=levels.split(",");
			String[] versionArray=versions.split(",");
			String[] oddArrays=new String[oddArray.length];
			String[] levelArrays=new String[levelArray.length];
			String[] versionArrays=new String[oddArray.length];
			//过滤空odds
			for(int i=0, j=0;i<oddArray.length;i++){
				if(null==oddArray[i]||"".equals(oddArray[i])){
					continue;
				}else{
					oddArrays[j]=oddArray[i];
					versionArrays[j]=versionArray[i];
					j++;
				}
			}
			//过滤空levels
			for(int i=0, j=0;i<levelArray.length;i++){
				if(null==levelArray[i]||"".equals(levelArray[i])){
					continue;
				}else{
					levelArrays[j]=levelArray[i];
					j++;
				}
			}
			for(int i=0;i<prizeArray.length;i++){
				ActivityPrize record=new ActivityPrize();
				record.setId(Long.parseLong(prizeArray[i]));
				record.setActionId(action.getId());
				record.setVersion(Long.parseLong(versionArrays[i]));
				record.setPrizeOdds(new BigDecimal(oddArrays[i]));
				record.setPrizeLevel(Integer.parseInt(levelArrays[i]));
				activityPrizeDaoImpl.update(record);
			}
			
		
	}

	@Override
	public List<ActivityAction> queryActionAll() {
		
		return activityActionDaoImpl.getAll();
	}

}
