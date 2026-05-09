package iuh.fit.son23641341.nhahanglau_phantan.network;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    private String action;
    private Object data;

    public Request(String action, Object data) {
        this.action = action;
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Request{" + "action='" + action + '\'' + ", data=" + data + '}';
    }
}
