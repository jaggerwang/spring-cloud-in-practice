package net.jaggerwang.scip.common.usecase.port.service;

import lombok.Data;

/**
 * @author Jagger Wang
 */
@Data
public class ApiResult<T> {
    private Code code;
    private String message;
    private T data;

    public enum Code {
        OK("ok"),
        UNKNOWN("unknown"),
        UNAUTHENTICATED("unauthenticated"),
        UNAUTHORIZED("unauthorized"),
        NOT_FOUND("not_found");

        private final String value;

        Code(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public ApiResult(Code code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ApiResult(Code code, String message) {
        this(code, message, null);
    }

    public ApiResult(T data) {
        this(Code.OK, "", data);
    }

    public ApiResult() {
        this(Code.OK, "", null);
    }
}