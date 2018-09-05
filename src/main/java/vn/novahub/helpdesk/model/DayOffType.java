package vn.novahub.helpdesk.model;

import com.fasterxml.jackson.annotation.JsonView;
import vn.novahub.helpdesk.view.View;

import javax.persistence.*;

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
    private String type;

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
