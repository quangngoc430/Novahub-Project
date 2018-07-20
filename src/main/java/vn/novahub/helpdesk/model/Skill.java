package vn.novahub.helpdesk.model;

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
import java.util.Objects;

@Entity
@Table(name = "skill")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Skill implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotEmpty(message = "Name is not empty", groups = {GroupCreateSkill.class, GroupUpdateSkill.class})
    @Column(name = "name")
    private String name;

    @NotNull(message = "Level is not null", groups = {GroupCreateSkill.class, GroupUpdateSkill.class})
    @Min(value = 1, message = "Level must be greater than or equal to 1", groups = {GroupCreateSkill.class, GroupUpdateSkill.class})
    @Max(value = 10, message = "Level must be less than or equal to 10", groups = {GroupCreateSkill.class, GroupUpdateSkill.class})
    @Column(name = "level")
    private long level;

    @JsonProperty(value = "created_at")
    @NotNull(message = "Create At is not null")
    @Column(name = "created_at")
    private Date createdAt;

    @JsonProperty(value = "updated_at")
    @NotNull(message = "Update At is not null")
    @Column(name = "updated_at")
    private Date updatedAt;

    @JsonProperty(value = "category_id")
    @NotNull(message = "Category Id is not null")
    @Column(name = "category_id")
    private long categoryId;

    @Transient
    @JsonIgnore
    @OneToMany
    private List<AccountHasSkill> accountHasSkillList;

    @Transient
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return level == skill.level &&
                categoryId == skill.categoryId &&
                Objects.equals(name, skill.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, level, categoryId);
    }
}
