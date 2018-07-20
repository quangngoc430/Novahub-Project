package vn.novahub.helpdesk.enums;

public enum TokenEnum {
    OPEN,
    EXPIRED,
    CLOSE,
    TIME_OF_TOKEN {
        @Override
        public long value() {
            return 86400; // 86400 sec = 24h
        }
    };

    public long value() { return 0; };
}
