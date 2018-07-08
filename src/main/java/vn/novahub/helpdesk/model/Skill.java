package vn.novahub.helpdesk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "skill")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Skill implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotEmpty(message = "Name is not empty")
    @Column(name = "name")
    private String name;

    @NotNull(message = "CategoryId is not null")
    @Column(name = "category_id")
    private long categoryId;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY )
    private List<AccountHasSkill> accountHasSkillList;

    @ManyToOne(targetEntity = Category.class)
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

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
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
                ", categoryId=" + categoryId +
                ", accountHasSkillList=" + accountHasSkillList +
                ", category=" + category +
                '}';
    }
}
