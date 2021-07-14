package net.jaggerwang.scip.stat.adapter.api.controller;

import net.jaggerwang.scip.common.adapter.api.controller.RestExceptionConverter;
import net.jaggerwang.scip.common.usecase.port.service.ApiResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    private RestExceptionConverter converter = new RestExceptionConverter();

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiResult> handle(Throwable e) {
        return converter.convert(e);
    }
}
