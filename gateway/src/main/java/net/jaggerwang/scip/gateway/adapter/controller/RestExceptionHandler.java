package net.jaggerwang.scip.gateway.adapter.controller;

import net.jaggerwang.scip.common.adapter.controller.ExceptionConverter;
import net.jaggerwang.scip.common.usecase.port.service.dto.RootDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    private ExceptionConverter converter = new ExceptionConverter();

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<RootDto> handle(Throwable e) {
        return converter.convert(e);
    }
}
