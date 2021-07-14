package net.jaggerwang.scip.common.adapter.api.controller;

import net.jaggerwang.scip.common.usecase.port.service.ApiResult;
import net.jaggerwang.scip.common.usecase.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

/**
 * @author Jagger Wang
 */
public class RestExceptionConverter {
    public ResponseEntity<ApiResult> convert(Throwable throwable) {
        if (throwable instanceof ApiException) {
            var e = (ApiException) throwable;
            return ResponseEntity
                    .status(e.getStatus())
                    .body(new ApiResult(e.getCode(), e.getMessage(), e.getData()));
        } else if (throwable instanceof NotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResult(ApiResult.Code.NOT_FOUND, throwable.getMessage()));
        } else if (throwable instanceof UnauthenticatedException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResult(ApiResult.Code.UNAUTHENTICATED, throwable.getMessage()));
        } else if (throwable instanceof UnauthorizedException ||
                throwable instanceof AccessDeniedException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResult(ApiResult.Code.UNAUTHORIZED, throwable.getMessage()));
        } else if (throwable instanceof UsecaseException) {
            return ResponseEntity.ok().body(new ApiResult(ApiResult.Code.UNKNOWN, throwable.getMessage()));
        } else {
            throwable.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResult(ApiResult.Code.UNKNOWN, throwable.toString()));
        }
    }
}
