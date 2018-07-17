package vn.novahub.helpdesk.enums;

public enum AccountStatus {
    ACTIVE{
        @Override
        public String value(){
            return "ACTIVE";
        }
    },
    INACTIVE{
        @Override
        public String value(){
            return "INACTIVE";
        }
    },
    LOCKED{
        @Override
        public String value(){
            return "LOCKED";
        }
    };

    public abstract String value();
}
