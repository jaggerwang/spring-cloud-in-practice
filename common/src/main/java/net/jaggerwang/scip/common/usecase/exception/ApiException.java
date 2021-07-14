package net.jaggerwang.scip.common.usecase.exception;

import net.jaggerwang.scip.common.usecase.port.service.ApiResult;
import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * @author Jagger Wang
 */
public class ApiException extends UsecaseException {
    private static final long serialVersionUID = 1L;

    private HttpStatus status;
    private ApiResult.Code code;
    private Object data;

    public ApiException(HttpStatus status, ApiResult.Code code, String message,
                        Object data) {
        super(message);

        this.status = status;
        this.code = code;
        this.data = data;
    }

    public ApiException(HttpStatus status, ApiResult.Code code, String message) {
        this(status, code, message, Map.of());
    }

    public ApiException(ApiResult.Code code, String message) {
        this(HttpStatus.OK, code, message, Map.of());
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ApiResult.Code getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}
