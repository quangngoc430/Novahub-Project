package vn.novahub.helpdesk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "day_off_type")
public class DayOffType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "year")
    private int year;

    @Column(name = "private_quota")
    private int privateQuota;

    @Column(name = "remaining_time")
    private long remainingTime;

    @Column(name = "common_type_id")
    private int commonTypeId;

    @Column(name = "account_id")
    private long accountId;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = CommonDayOffType.class)
    @JoinColumn(name = "common_type_id", insertable = false, updatable = false)
    private CommonDayOffType commonDayOffType;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Account.class)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;

    public void subtractRemainingTime(long numberOfDayOff) {
        this.remainingTime = this.remainingTime - numberOfDayOff;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPrivateQuota() {
        return privateQuota;
    }

    public void setPrivateQuota(int privateQuota) {
        this.privateQuota = privateQuota;
    }

    public int getCommonTypeId() {
        return commonTypeId;
    }

    public void setCommonTypeId(int commonTypeId) {
        this.commonTypeId = commonTypeId;
    }

    public CommonDayOffType getCommonDayOffType() {
        return commonDayOffType;
    }

    public void setCommonDayOffType(CommonDayOffType commonDayOffType) {
        this.commonDayOffType = commonDayOffType;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "DayOffType{" +
                "id=" + id +
                ", year=" + year +
                ", remainingTime=" + remainingTime +
                ", accountId=" + accountId +
                '}';
    }
}
