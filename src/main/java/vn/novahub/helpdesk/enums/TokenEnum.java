package vn.novahub.helpdesk.enums;

public enum TokenEnum {
    OPEN,
    EXPIRED,
    CLOSE,
    TIME_OF_TOKEN {
        @Override
        public long value() {
            return 3600; // 3600 sec = 24h
        }
    };

    public long value() { return 0; }
}
