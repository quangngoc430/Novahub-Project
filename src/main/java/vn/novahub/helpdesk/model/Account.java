package vn.novahub.helpdesk.model;

import com.fasterxml.jackson.annotation.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import vn.novahub.helpdesk.annotation.Status;
import vn.novahub.helpdesk.validation.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "account")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotEmpty(message = "Email is not empty", groups = {GroupCreateAccount.class, GroupLoginAccount.class, GroupCreateWithAccountGoogle.class})
    @Column(name = "email", unique = true)
    @Email(regexp = "^[a-zA-Z0-9]+\\@novahub.vn", message = "Email must be end with @novahub.vn"
            , groups = {GroupCreateAccount.class, GroupLoginAccount.class})
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_day")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date dayOfBirth;

    @Column(name = "address")
    private String address;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @NotEmpty(message = "Password is not empty"
            , groups = {GroupCreateAccount.class, GroupLoginAccount.class})
    @Column(name = "password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Status(message = "Status does not match any statuses", targetClass = Account.class)
    @NotEmpty(message = "Status is not empty")
    @Column(name = "status")
    private String status;

    @NotNull(message = "Create At is not null")
    @Column(name = "created_at")
    private Date createdAt;

    @NotNull(message = "Update At is not null")
    @Column(name = "updated_at")
    private Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "joiningDate")
    private Date joiningDate;

    @Column(name = "token")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(message = "Token is not empty", groups = GroupCreateWithAccountGoogle.class)
    private String token;

    @Column(name = "vertification_token")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String vertificationToken;

    @NotNull(message = "Role id is not empty")
    @Column(name = "role_id")
    private long roleId;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String newPassword;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Role.class)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(Date dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVertificationToken() {
        return vertificationToken;
    }

    public void setVertificationToken(String vertificationToken) {
        this.vertificationToken = vertificationToken;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }

    @Transient
    @JsonIgnore
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.getRole().getName()));

        return authorities;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dayOfBirth=" + dayOfBirth +
                ", address='" + address + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", joiningDate=" + joiningDate +
                ", token='" + token + '\'' +
                ", vertificationToken='" + vertificationToken + '\'' +
                ", roleId=" + roleId +
                ", newPassword='" + newPassword + '\'' +
                ", role=" + role +
                '}';
    }
}
