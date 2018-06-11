package vn.novahub.helpdesk.model;

import javax.persistence.*;

@Entity
@Table(name = "date_off_type")
public class DateOffType {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "type")
    private String type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DateOffType{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
