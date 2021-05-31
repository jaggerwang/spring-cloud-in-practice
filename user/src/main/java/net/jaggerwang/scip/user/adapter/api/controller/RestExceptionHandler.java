package net.jaggerwang.scip.user.adapter.api.controller;

import net.jaggerwang.scip.common.adapter.api.controller.RestExceptionConverter;
import net.jaggerwang.scip.common.usecase.port.service.dto.RootDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    private RestExceptionConverter converter = new RestExceptionConverter();

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<RootDto> handle(Throwable e) {
        return converter.convert(e);
    }
}
