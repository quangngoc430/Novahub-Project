package vn.novahub.helpdesk.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import vn.novahub.helpdesk.annotation.IssueStatus;
import vn.novahub.helpdesk.constant.IssueConstant;
import vn.novahub.helpdesk.validation.GroupCreateIssue;
import vn.novahub.helpdesk.validation.GroupUpdateIssue;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "issue")
public class Issue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotEmpty(message = "Title is not empty", groups = {GroupCreateIssue.class, GroupUpdateIssue.class})
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Content is not empty", groups = {GroupCreateIssue.class, GroupUpdateIssue.class})
    @Column(name = "content")
    private String content;

    @IssueStatus(message = "Status does not match any statuses",
            statuses = {IssueConstant.STATUS_PENDING, IssueConstant.STATUS_APPROVE, IssueConstant.STATUS_APPROVE})
    @NotEmpty(message = "Status is not empty")
    @Column(name = "status")
    private String status;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @NotNull(message = "CreateAt is not null")
    @Column(name = "created_at")
    private Date createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @NotNull(message = "UpdateAt is not null")
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "reply_message")
    private String replyMessage;

    @NotNull(message = "AccountId is not null")
    @Column(name = "account_id")
    private long accountId;

    @JsonIgnore
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
