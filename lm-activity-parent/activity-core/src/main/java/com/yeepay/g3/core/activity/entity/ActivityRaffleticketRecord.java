package com.yeepay.g3.core.activity.entity;

import java.util.Date;

import com.yeepay.g3.utils.persistence.EntityVersion;

public class ActivityRaffleticketRecord implements EntityVersion<Long> {

	private static final long serialVersionUID = -6808685286892244318L;

	private Long id;

    private Long version;

    private Long userRaffleTicketId;

    private Long raffleTicketId;

    private String memberNo;

    private String memberTel;

    private Date useTime;

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

    public Long getUserRaffleTicketId() {
        return userRaffleTicketId;
    }

    public void setUserRaffleTicketId(Long userRaffleTicketId) {
        this.userRaffleTicketId = userRaffleTicketId;
    }

    public Long getRaffleTicketId() {
        return raffleTicketId;
    }

    public void setRaffleTicketId(Long raffleTicketId) {
        this.raffleTicketId = raffleTicketId;
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
        this.memberTel = memberTel == null ? null : memberTel.trim();
    }

    public Date getUseTime() {
        return useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

	@Override
	public String toString() {
		return "ActivityRaffleticketRecord [id=" + id + ", version=" + version
				+ ", userRaffleTicketId=" + userRaffleTicketId
				+ ", raffleTicketId=" + raffleTicketId + ", memberNo="
				+ memberNo + ", memberTel=" + memberTel + ", useTime="
				+ useTime + "]";
	}
}