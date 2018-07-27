package vn.novahub.helpdesk.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "account_has_skill")
public class AccountHasSkill implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @JsonProperty(value = "account_id")
    @Column(name = "account_id")
    private long accountId;

    @JsonProperty(value = "skill_id")
    @Column(name = "skill_id")
    private long skillId;

    @JsonProperty(value = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at")
    private Date createdAt;

    @JsonProperty(value = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", insertable = false, updatable = false)
    private Skill skill;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getSkillId() {
        return skillId;
    }

    public void setSkillId(long skillId) {
        this.skillId = skillId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    @Override
    public String toString() {
        return "AccountHasSkill{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", skillId=" + skillId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", account=" + account +
                ", skill=" + skill +
                '}';
    }
}
