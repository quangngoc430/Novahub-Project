package vn.novahub.helpdesk.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import vn.novahub.helpdesk.annotation.Status;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.DayOffAccount;
import vn.novahub.helpdesk.model.Role;
import vn.novahub.helpdesk.model.Token;
import vn.novahub.helpdesk.validation.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.Set;

public class AccountDTO {

    @Column(name = "id")
    private long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @JsonProperty(value = "birth_day")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birth_day")
    private Date dayOfBirth;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "title")
    private String title;

    @Column(name = "introduction")
    private String introduction;

    @JsonProperty(value = "avatar_url")
    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "status")
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "created_at")
    @Column(name = "created_at")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "updated_at")
    @Column(name = "updated_at")
    private Date updatedAt;

    @JsonProperty(value = "joining_date")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "joiningDate")
    private Date joiningDate;

    @JsonProperty("role_id")
    @Column(name = "role_id")
    private long roleId;

    @Transient
    @JsonProperty(value = "new_password", access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(message = "new_pasword is not empty",
            groups = {GroupUpdatePasswordByAccount.class})
    @Size(min = 8, max = 40, message = "new_password must have between 8 and 40 characters",
            groups = {GroupUpdatePasswordByAccount.class})
    private String newPassword;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Role.class)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;

    @Transient
    private Token accessToken;

    @OneToMany(mappedBy = "account")
    private Set<DayOffAccount> dayOffAccounts;

}
