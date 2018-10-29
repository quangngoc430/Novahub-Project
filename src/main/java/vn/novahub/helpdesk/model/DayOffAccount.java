package vn.novahub.helpdesk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.Range;
import vn.novahub.helpdesk.view.View;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Set;

@Entity
@Table(name = "day_off_account")
public class DayOffAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.DayOffAccountRespond.class)
    @Column(name = "id")
    private long id;

    @JsonView(View.DayOffAccountRespond.class)
    @Column(name = "year")
    @Min(value = 2018, message = "Year must be from 2018")
    private int year;

    @JsonView(View.DayOffAccountRespond.class)
    @Column(name = "private_quota")
    @Range(min = 0, max = 300, message = "Quota is out of range")
    private int privateQuota;

    @JsonView(View.DayOffAccountRespond.class)
    @Column(name = "remaining_time")
    @Range(min = 0, max = 300, message = "Quota is out of range")
    private int remainingTime;

    @Column(name = "day_off_type_id")
    private int dayOffTypeId;

    @Column(name = "account_id")
    private long accountId;

    @JsonView(View.DayOffAccountRespond.class)
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = DayOffType.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "day_off_type_id", insertable = false, updatable = false)
    private DayOffType dayOffType;

    @JsonView(View.DayOffAccountRespond.class)
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Account.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;

    @JsonIgnore
    @OneToMany(mappedBy = "dayOffAccount")
    private Set<DayOff> dayOffs;

    public void subtractRemainingTime(int numberOfDayOff) {
        this.remainingTime = this.remainingTime - numberOfDayOff;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
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

    public int getDayOffTypeId() {
        return dayOffTypeId;
    }

    public void setDayOffTypeId(int dayOffTypeId) {
        this.dayOffTypeId = dayOffTypeId;
    }

    public DayOffType getDayOffType() {
        return dayOffType;
    }

    public void setDayOffType(DayOffType dayOffType) {
        this.dayOffType = dayOffType;
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

    public Set<DayOff> getDayOffs() {
        return dayOffs;
    }

    public void setDayOffs(Set<DayOff> dayOffs) {
        this.dayOffs = dayOffs;
    }

    @Override
    public String toString() {
        return "DayOffAccount{" +
                "id=" + id +
                ", year=" + year +
                ", remainingTime=" + remainingTime +
                ", accountId=" + accountId +
                '}';
    }

}
