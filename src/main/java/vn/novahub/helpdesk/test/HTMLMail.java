package vn.novahub.helpdesk.test;

public class HTMLMail {

    private final String to;

    public HTMLMail(String to) {
        this.to = to;
    }

    public String getTo() {
        return this.to;
    }

    public String getSubject() {
        return "HTML Email Subject";
    }

    public String getContent() {
        return "<html>" +
                "<body>" +
                "<p>Hello client,</p>" +
                "<p>This an <strong>HTML</strong> email content !</p>" +
                "</body>" +
                "</html>";
    }
}