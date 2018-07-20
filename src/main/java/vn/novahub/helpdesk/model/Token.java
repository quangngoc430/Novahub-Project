package vn.novahub.helpdesk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "token")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @JsonProperty(value = "access_token")
    @NotEmpty
    @Column(name = "access_token")
    private String accessToken;

    @JsonIgnore
    @NotNull
    @Column(name = "time")
    private long time;

    @JsonProperty(value = "expired_in")
    @Transient
    private long timeExpired;

    @JsonIgnore
    @NotEmpty
    @Column(name = "status")
    private String status;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "created_at")
    private Date createdAt;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "updated_at")
    private Date updatedAt;

    @JsonProperty(value = "account_id")
    @NotNull
    @Column(name = "account_id")
    private long accountId;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Account.class)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTimeExpired() {
        return timeExpired;
    }

    public void setTimeExpired(long timeExpired) {
        this.timeExpired = timeExpired;
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

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", accessToken='" + accessToken + '\'' +
                ", time=" + time +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", accountId=" + accountId +
                ", account=" + account +
                '}';
    }
}
