package vn.novahub.helpdesk.enums;

public enum RoleLevel {
    ADMIN{
        @Override
        public String value(){
            return "ADMIN";
        }
    },
    CLERK{
        @Override
        public String value(){
            return "CLERK";
        }
    },
    USER{
        @Override
        public String value(){
            return "USER";
        }
    };

    public abstract String value();
}
