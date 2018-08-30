package vn.novahub.helpdesk.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import vn.novahub.helpdesk.validation.GroupCreateSkill;
import vn.novahub.helpdesk.validation.GroupCreateSkillWithLevel;
import vn.novahub.helpdesk.validation.GroupUpdateSkill;
import vn.novahub.helpdesk.validation.GroupUpdateSkillWithLevel;
import vn.novahub.helpdesk.view.View;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "skill")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Skill implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Public.class, View.AccountWithSkills.class})
    @Column(name = "id")
    private long id;

    @NotEmpty(message = "name is not empty", groups = {GroupCreateSkill.class, GroupUpdateSkill.class})
    @JsonView({View.Public.class, View.AccountWithSkills.class})
    @Column(name = "name")
    private String name;

    @JsonView({View.SkillWithLevel.class, View.AccountWithSkills.class})
    @Transient
    @NotNull(message = "level is not null", groups = {GroupCreateSkillWithLevel.class, GroupUpdateSkillWithLevel.class})
    @Min(value = 1, message = "level must be greater than or equal to 1", groups = {GroupCreateSkillWithLevel.class, GroupUpdateSkillWithLevel.class})
    @Max(value = 10, message = "level must be less than or equal to 10", groups = {GroupCreateSkillWithLevel.class, GroupUpdateSkillWithLevel.class})
    private Long level;

    @JsonView(View.Public.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "created_at")
    @NotNull(message = "created_at is not null")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt = new Date();

    @JsonView(View.Public.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "updated_at")
    @NotNull(message = "updated_at is not null")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt = new Date();

    @JsonView({View.Public.class, View.AccountWithSkillsAndCategory.class})
    @JsonProperty(value = "category_id")
    @NotNull(message = "category_id is not null", groups = {GroupCreateSkill.class, GroupUpdateSkill.class})
    @Column(name = "category_id")
    private long categoryId;

    @JsonIgnore
    @OneToMany
    @Transient
    private List<AccountHasSkill> accountHasSkillList;

    @JsonView({View.Public.class, View.AccountWithSkillsAndCategory.class})
    @ManyToOne(targetEntity = Category.class)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    @Transient
    private List<Account> accounts;

    public Skill() {}

    public Skill(long id, String name, long level) {
        super();
        this.id = id;
        this.name = name;
        this.level = level;
    }

    public Skill(long id, String name, long level, long categoryId, Date createdAt, Date updatedAt) {
        super();
        this.id = id;
        this.name = name;
        this.level = level;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Skill(long id, String name, long level, long categoryId, Date createdAt, Date updatedAt, Category category) {
        super();
        this.id = id;
        this.name = name;
        this.level = level;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.category = category;
    }

    public Skill(long id, String name, long categoryId, Date createdAt, Date updatedAt, Category category) {
        super();
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
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

    public List<AccountHasSkill> getAccountHasSkillList() {
        return accountHasSkillList;
    }

    public void setAccountHasSkillList(List<AccountHasSkill> accountHasSkillList) {
        this.accountHasSkillList = accountHasSkillList;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", categoryId=" + categoryId +
                ", accountHasSkillList=" + accountHasSkillList +
                ", category=" + category +
                '}';
    }
}