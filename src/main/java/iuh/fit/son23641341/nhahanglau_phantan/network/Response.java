package iuh.fit.son23641341.nhahanglau_phantan.network;

import java.io.Serializable;

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;
    private String status; // SUCCESS or ERROR
    private Object data;
    private String message;

    public Response(String status, Object data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Response{" + "status='" + status + '\'' + ", data=" + data + ", message='" + message + '\'' + '}';
    }
}
