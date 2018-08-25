package vn.novahub.helpdesk.model;

import com.fasterxml.jackson.annotation.JsonView;
import vn.novahub.helpdesk.view.View;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Entity
@Table(name = "role")
public class Role implements Serializable {

    @JsonView({View.Public.class, View.AccountWithSkills.class})
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @JsonView({View.Public.class, View.AccountWithSkills.class})
    @NotEmpty
    @Column(name = "name")
    private String name;

    public Role() {}

    public Role(long id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
