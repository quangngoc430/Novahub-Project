package vn.novahub.helpdesk.model;

public class ResponseJSON {

    private Object data;
    private long code;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ResponseJSON{" +
                "data=" + data +
                ", code=" + code +
                '}';
    }
}
