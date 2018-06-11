package vn.novahub.helpdesk.model;

import javax.persistence.*;

@Entity
@Table(name = "account_has_skill")
public class AccountHasSkill {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "account_id")
    private long accountId;

    @Column(name = "skill_id")
    private long skillId;


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


    @Override
    public String toString() {
        return "AccountHasSkill{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", skillId=" + skillId +
                '}';
    }
}
