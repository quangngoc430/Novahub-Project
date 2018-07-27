package vn.novahub.helpdesk.model;

import java.util.Objects;

public class Request {

    private String path;

    private String[] method;

    public Request() {
    }

    public Request(String path, String[] method) {
        this.path = path;
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String[] getMethod() {
        return method;
    }

    public void setMethod(String[] method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "Request{" +
                "path='" + path + '\'' +
                ", method='" + method + '\'' +
                '}';
    }

    public boolean equal(String path, String method) {
        if(path != null && path.matches(this.path)) {
            for(String singleMethod : this.getMethod()){
                if(Objects.equals(singleMethod, method))
                    return true;
            }
        }

        return false;
    }
}