package com.yeepay.g3.core.activity.entity;

import java.util.Date;

import com.yeepay.g3.facade.activity.enums.ShareBizTypeEnum;
import com.yeepay.g3.utils.persistence.EntityVersion;

public class ActivityShareRecords implements EntityVersion<Long>{
	
    private static final long serialVersionUID = -6024829044006332195L;
    /**
     * 主键id
     */
	private Long id;

	/**
	 * 版本号
	 */
    private Long version;

    /**
     * 被推荐人会员编号
     */
    private String memberNo;
    
    /**
     * 被推荐人手机号
     */
    private String memberTel;

    /**
     * 推荐人会员编号
     */
    private String recommendMemberNo;

    /**
     * 推荐人手机号
     */
    private String recommendMemberTel;
    
    /**
     * 渠道名称
     */
    private String srcType;

    /**
     * 渠道号
     */
    private String srcNo;

    /**
     * 业务类型
     */
    private ShareBizTypeEnum bizType;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 注册时间
     */
    private Date registerTime;

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

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo == null ? null : memberNo.trim();
    }

    public String getMemberTel() {
		return memberTel;
	}

	public void setMemberTel(String memberTel) {
		this.memberTel = memberTel;
	}

	public String getRecommendMemberNo() {
		return recommendMemberNo;
	}

	public void setRecommendMemberNo(String recommendMemberNo) {
		this.recommendMemberNo = recommendMemberNo;
	}

	public String getRecommendMemberTel() {
		return recommendMemberTel;
	}

	public void setRecommendMemberTel(String recommendMemberTel) {
		this.recommendMemberTel = recommendMemberTel;
	}

	public String getSrcType() {
        return srcType;
    }

    public void setSrcType(String srcType) {
        this.srcType = srcType == null ? null : srcType.trim();
    }

    public String getSrcNo() {
        return srcNo;
    }

    public void setSrcNo(String srcNo) {
        this.srcNo = srcNo == null ? null : srcNo.trim();
    }

    public ShareBizTypeEnum getBizType() {
		return bizType;
	}

	public void setBizType(ShareBizTypeEnum bizType) {
		this.bizType = bizType;
	}

	public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	@Override
	public String toString() {
		return "ActivityShareRecords [id=" + id + ", version=" + version
				+ ", memberNo=" + memberNo + ", memberTel=" + memberTel
				+ ", recommendMemberNo=" + recommendMemberNo
				+ ", recommendMemberTel=" + recommendMemberTel + ", srcType="
				+ srcType + ", srcNo=" + srcNo + ", bizType=" + bizType
				+ ", createTime=" + createTime + ", registerTime="
				+ registerTime + "]";
	}
}