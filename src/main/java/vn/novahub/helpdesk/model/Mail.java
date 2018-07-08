package vn.novahub.helpdesk.model;

public class Mail {

    private String[] emailReceiving;
    private String subject;
    private String content;

    public Mail() {
    }

    public Mail(String[] emailReceiving) {
        this.emailReceiving = emailReceiving;
    }

    public Mail(String[] emailReceiving, String subject, String content) {
        this.emailReceiving = emailReceiving;
        this.subject = subject;
        this.content = content;
    }

    public String[] getEmailReceiving() {
        return emailReceiving;
    }

    public void setEmailReceiving(String[] emailReceiving) {
        this.emailReceiving = emailReceiving;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "emailReceiving='" + emailReceiving + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

