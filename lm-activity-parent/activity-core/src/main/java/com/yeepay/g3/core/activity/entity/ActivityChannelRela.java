package com.yeepay.g3.core.activity.entity;

import java.util.Date;

import com.yeepay.g3.utils.persistence.EntityVersion;

public class ActivityChannelRela implements EntityVersion<Long> {

	private static final long serialVersionUID = -4154058301515348202L;

	private Long id;

    private Long version;

    private Long activityId;

    private String channelCode;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode == null ? null : channelCode.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	@Override
	public String toString() {
		return "ActivityChannelRela [id=" + id + ", version=" + version
				+ ", activityId=" + activityId + ", channelCode=" + channelCode
				+ ", createTime=" + createTime + "]";
	}
}