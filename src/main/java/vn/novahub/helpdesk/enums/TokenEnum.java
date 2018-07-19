package vn.novahub.helpdesk.enums;

public enum TokenEnum {
    OPEN,
    CLOSE,
    TIME_OF_TOKEN {
        @Override
        public long value() {
            return 3600; // 3600 sec
        }
    };

    public long value() { return 0; };
}
