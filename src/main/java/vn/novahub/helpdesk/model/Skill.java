package vn.novahub.helpdesk.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import vn.novahub.helpdesk.validation.GroupCreateSkill;
import vn.novahub.helpdesk.validation.GroupUpdateSkill;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
    @Column(name = "id")
    private long id;

    @NotEmpty(message = "name is not empty", groups = {GroupCreateSkill.class})
    @Column(name = "name", unique = true)
    private String name;

    @Transient
    @NotNull(message = "value is not empty", groups = {GroupCreateSkill.class, GroupUpdateSkill.class})
    @Min(value = 1, message = "level must be greater than or equal to 1", groups = {GroupCreateSkill.class, GroupUpdateSkill.class})
    @Max(value = 10, message = "level must be less than or equal to 10", groups = {GroupCreateSkill.class, GroupUpdateSkill.class})
    private long level;

    @JsonIgnore
    @OneToMany()
    private List<Level> levelList;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "created_at")
    @NotNull(message = "create_at is not null")
    @Column(name = "created_at")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "updated_at")
    @NotNull(message = "updated_at is not null")
    @Column(name = "updated_at")
    private Date updatedAt;

    @JsonProperty(value = "category_id")
    @NotNull(message = "category_id is not null")
    @Column(name = "category_id")
    private long categoryId;

    @JsonIgnore
    @OneToMany
    private List<AccountHasSkill> accountHasSkillList;

    @ManyToOne
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

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

    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    public List<Level> getLevelList() {
        return levelList;
    }

    public void setLevelList(List<Level> levelList) {
        this.levelList = levelList;
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
                ", level=" + levelList +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", categoryId=" + categoryId +
                ", accountHasSkillList=" + accountHasSkillList +
                ", category=" + category +
                '}';
    }

}
