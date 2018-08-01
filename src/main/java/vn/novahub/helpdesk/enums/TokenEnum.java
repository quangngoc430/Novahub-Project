package vn.novahub.helpdesk.enums;

public enum TokenEnum {
    OPEN,
    EXPIRED,
    CLOSE,
    TIME_OF_TOKEN {
        @Override
        public long value() {
            return 1209600; // 1209600 sec = 14 days
        }
    };

    public long value() { return 0; }
}
