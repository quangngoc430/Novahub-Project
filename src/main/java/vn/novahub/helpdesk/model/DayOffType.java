package vn.novahub.helpdesk.model;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.Range;
import vn.novahub.helpdesk.view.View;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "day_off_type")
public class DayOffType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.DayOffAccountRespond.class)
    @Column(name = "id")
    private int id;

    @JsonView(View.DayOffAccountRespond.class)
    @Column(name = "type")
    @Size(max = 100, message = "Name of day off type is too long")
    private String type;

    @Range(min = 0, max = 300, message = "Default quota is out of range")
    @Column(name = "default_quota")
    private int defaultQuota;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDefaultQuota() {
        return defaultQuota;
    }

    public void setDefaultQuota(int defaultQuota) {
        this.defaultQuota = defaultQuota;
    }
}
