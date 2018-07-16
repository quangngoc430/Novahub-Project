package vn.novahub.helpdesk.enums;

public enum IssueStatus {
    NONE {
        @Override
        public String value() {
            return "NONE";
        }
    },
    APPROVE {
        @Override
        public String value() {
            return "APPROVE";
        }
    },
    DENY {
        @Override
        public String value() {
            return "DENY";
        }
    },
    PENDING{
        @Override
        public String value() {
            return "PENDING";
        }
    };

    public abstract String value();
}
