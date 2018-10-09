package vn.novahub.helpdesk.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import vn.novahub.helpdesk.annotation.Status;
import vn.novahub.helpdesk.validation.GroupCreateIssue;

import javax.persistence.*;
import javax.validation.groups.Default;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "issue")
public class Issue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotEmpty(message = "title is not empty", groups = {GroupCreateIssue.class})
    @Size(max = 200, message = "title must have max 100 characters",
            groups = {GroupCreateIssue.class, Default.class})
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "content is not empty", groups = {GroupCreateIssue.class})
    @Size(max = 1000, message = "content must have max 1000 characters",
            groups = {GroupCreateIssue.class, Default.class})
    @Column(name = "content")
    private String content;

    @Status(message = "status does not match any statuses", targetClass = Issue.class)
    @NotEmpty(message = "status is not empty")
    @Size(max = 45, message = "status must have max 45 characters")
    @Column(name = "status")
    private String status;

    @JsonProperty(value = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull(message = "created_at is not null")
    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt = new Date();

    @JsonProperty(value = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull(message = "updated_at is not null")
    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt = new Date();

    @JsonProperty(value = "reply_message")
    @Size(max = 1000, message = "reply message must have max 1000 characters")
    @Column(name = "reply_message")
    private String replyMessage;

    @JsonProperty(value = "account_id")
    @NotNull(message = "account_id is not null")
    @Column(name = "account_id")
    private long accountId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(max = 256, message = "token must have max 256 characters")
    @Column(name = "token")
    private String token;

    @ManyToOne(targetEntity = Account.class)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", replyMessage='" + replyMessage + '\'' +
                ", accountId=" + accountId +
                ", token='" + token + '\'' +
                ", account=" + account +
                '}';
    }
}
