package vn.novahub.helpdesk.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import vn.novahub.helpdesk.validation.GroupLoginWithGoogle;
import vn.novahub.helpdesk.view.View;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "token")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Token implements Serializable {

    @JsonView(View.Public.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @JsonView(View.Public.class)
    @JsonProperty(value = "access_token")
    @NotEmpty(message = "access_token is not empty", groups = {GroupLoginWithGoogle.class})
    @Column(name = "access_token")
    private String accessToken;

    @JsonView(View.Public.class)
    @JsonProperty(value = "expired_in")
    @NotNull(message = "expired_in is not null")
    @Column(name = "expired_in")
    private long expiredIn;

    @JsonView(View.Public.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "expired_at")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull(message = "expired_at is not null")
    @Column(name = "expired_at")
    private Date expiredAt;

    @JsonView(View.Public.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull(message = "create_at is not null")
    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt = new Date();

    @JsonView(View.Public.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull(message = "updated_at is not null")
    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt = new Date();

    @JsonView(View.Public.class)
    @JsonProperty(value = "account_id")
    @NotNull(message = "account_id is not null")
    @Column(name = "account_id")
    private long accountId;

    @JsonView(View.Public.class)
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Account.class)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;

    public Token() {
        super();
    }

    public Token(String accessToken) {
        super();
        this.accessToken = accessToken;
    }

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

    public long getExpiredIn() {
        return expiredIn;
    }

    public void setExpiredIn(long expiredIn) {
        this.expiredIn = expiredIn;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
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
                ", expiredIn=" + expiredIn +
                ", expiredAt=" + expiredAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", accountId=" + accountId +
                ", account=" + account +
                '}';
    }
}
