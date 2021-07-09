package net.jaggerwang.scip.auth.adapter.api.controller;

import net.jaggerwang.scip.common.adapter.api.controller.RestExceptionConverter;
import net.jaggerwang.scip.common.usecase.port.service.dto.RootDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Jagger Wang
 */
@ControllerAdvice
public class RestExceptionHandler {
    private RestExceptionConverter converter = new RestExceptionConverter();

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<RootDTO> handle(Throwable e) {
        return converter.convert(e);
    }
}
