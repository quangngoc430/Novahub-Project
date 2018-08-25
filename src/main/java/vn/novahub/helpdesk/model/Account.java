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
import java.util.Set;

@Entity
@Table(name = "account")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotEmpty(message = "email is not empty", groups = {GroupCreateAccount.class, GroupLoginAccount.class})
    @Email(regexp = "^[a-zA-Z0-9._]+\\@novahub.vn", message = "email contains [a-z|A-Z|0-9|.|_] and end with @novahub.vn",
            groups = {GroupCreateAccount.class, GroupLoginAccount.class})
    @Size(min = 8, max = 80, message = "email must have between 8 and 80 characters",
            groups = {GroupCreateAccount.class, GroupLoginAccount.class})
    @Column(name = "email", unique = true)
    private String email;

    @JsonProperty(value = "first_name")
    @Column(name = "first_name")
    private String firstName;

    @JsonProperty(value = "last_name")
    @Column(name = "last_name")
    private String lastName;

    @JsonProperty(value = "birth_day")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birth_day")
    private Date dayOfBirth;

    @Column(name = "address")
    private String address;

    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$", message = "phone have the wrong pattern",
             groups = {GroupCreateAccount.class})
    @Column(name = "phone")
    private String phone;

    @Column(name = "title")
    private String title;

    @Column(name = "introduction")
    private String introduction;

    @JsonProperty(value = "avatar_url")
    @Column(name = "avatar_url")
    private String avatarUrl;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(message = "password is not empty",
            groups = {GroupCreateAccount.class, GroupLoginAccount.class,
                      GroupUpdatePasswordByAccount.class, GroupUpdatePasswordByAdmin.class, GroupUpdatePasswordAccountSignupWithGoogle.class})
    @Size(min = 8, max = 40, message = "password must have between 8 and 40 characters",
            groups = {GroupCreateAccount.class, GroupLoginAccount.class,
                      GroupUpdatePasswordByAccount.class, GroupUpdatePasswordByAdmin.class, GroupUpdatePasswordAccountSignupWithGoogle.class})
    @Column(name = "password")
    private String password;

    @Status(message = "status does not match any statuses", targetClass = Account.class)
    @NotEmpty(message = "status is not empty")
    @Column(name = "status")
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "created_at")
    @NotNull(message = "created_at is not null")
    @Column(name = "created_at")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "updated_at")
    @NotNull(message = "updated_at is not null")
    @Column(name = "updated_at")
    private Date updatedAt;

    @JsonProperty(value = "joining_date")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "joiningDate")
    private Date joiningDate;

    @JsonIgnore
    @Column(name = "verification_token")
    private String verificationToken;

    @JsonProperty("role_id")
    @NotNull(message = "role_id is not empty")
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

    @OneToMany(mappedBy = "account")
    private Set<DayOffAccount> dayOffAccounts;

    @Transient
    private Token accessToken;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
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

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Set<DayOffAccount> getDayOffAccounts() {
        return dayOffAccounts;
    }

    public void setDayOffAccounts(Set<DayOffAccount> dayOffAccounts) {
        this.dayOffAccounts = dayOffAccounts;
    }

    public Token getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(Token accessToken) {
        this.accessToken = accessToken;
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
                ", phone='" + phone + '\'' +
                ", title='" + title + '\'' +
                ", introduction='" + introduction + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", joiningDate=" + joiningDate +
                ", verificationToken='" + verificationToken + '\'' +
                ", roleId=" + roleId +
                ", newPassword='" + newPassword + '\'' +
                ", role=" + role +
                ", accessToken=" + accessToken +
                '}';
    }
}
