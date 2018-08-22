package vn.novahub.helpdesk.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;

import vn.novahub.helpdesk.validation.GroupCreateSkill;
import vn.novahub.helpdesk.validation.GroupUpdateSkill;
import vn.novahub.helpdesk.view.View;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
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

    @Transient
    @JsonView({View.Public.class, View.AccountWithSkills.class})
    @NotNull(message = "level is not null", groups = {GroupCreateSkill.class, GroupUpdateSkill.class})
    private Level level;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "created_at")
    @NotNull(message = "created_at is not null")
    @JsonView(View.Public.class)
    @Column(name = "created_at")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "updated_at")
    @NotNull(message = "updated_at is not null")
    @JsonView(View.Public.class)
    @Column(name = "updated_at")
    private Date updatedAt;

    @JsonProperty(value = "category_id")
    @NotNull(message = "category_id is not null", groups = {GroupCreateSkill.class, GroupUpdateSkill.class})
    @JsonView(View.Public.class)
    @Column(name = "category_id")
    private long categoryId;

    @JsonIgnore
    @OneToMany
    @Transient
    private List<AccountHasSkill> accountHasSkillList;

    @ManyToOne(targetEntity = Category.class)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    public Skill() {}

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

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
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
