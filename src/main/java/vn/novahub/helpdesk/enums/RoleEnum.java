package vn.novahub.helpdesk.enums;

public enum RoleEnum {
    ADMIN,
    CLERK,
    USER,
    PREFIX {
        @Override
        public String value() {
            return "ROLE_";
        }
    };

    public String value() {
        return "";
    }
}
