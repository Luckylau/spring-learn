package luckylau.spring.session.vo;

import lombok.Data;

@Data
public class HttpResult<T> {

    public static final int FAIL_CODE = 400;
    public static final int SUCC_CODE = 200;

    private int code;
    private String message;
    private T data;

    public HttpResult() {
    }

    public HttpResult(int code, String message, T data) {
        this.code = code;
        this.setMessage(message);
        this.data = data;
    }

    public HttpResult(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public HttpResult(int code, String message) {
        this.code = code;
        this.setMessage(message);
    }

    public static HttpResult fail(String msg) {
        return fail(FAIL_CODE, msg);
    }

    public static HttpResult fail(int code, String msg) {
        return new HttpResult(code, msg);
    }

    public static HttpResult success(Object data) {
        HttpResult result = new HttpResult(SUCC_CODE, "SUCCESS");
        result.setData(data);
        return result;
    }


}