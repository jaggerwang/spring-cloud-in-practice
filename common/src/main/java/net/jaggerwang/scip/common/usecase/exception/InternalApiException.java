package net.jaggerwang.scip.common.usecase.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class InternalApiException extends UsecaseException {
    private static final long serialVersionUID = 1L;

    private HttpStatus status;
    private String code;
    private Map<String, Object> data;

    public InternalApiException(HttpStatus status, String code, String message,
                                Map<String, Object> data) {
        super(message);

        this.status = status;
        this.code = code;
        this.data = data;
    }

    public InternalApiException(HttpStatus status, String code, String message) {
        this(status, code, message, Map.of());
    }

    public InternalApiException(String code, String message) {
        this(HttpStatus.OK, code, message, Map.of());
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public Map<String, Object> getData() {
        return data;
    }
}
