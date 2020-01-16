package net.jaggerwang.scip.common.adapter.controller;

import net.jaggerwang.scip.common.usecase.port.service.dto.RootDto;
import net.jaggerwang.scip.common.usecase.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ExceptionConverter {
    public ResponseEntity<RootDto> convert(Throwable exception) {
        if (exception instanceof InternalApiException) {
            var e = (InternalApiException) exception;
            return ResponseEntity
                    .status(e.getStatus())
                    .body(new RootDto(e.getCode(), e.getMessage(), e.getData()));
        } else if (exception instanceof NotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RootDto("not_found", exception.getMessage()));
        } else if (exception instanceof UnauthenticatedException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new RootDto("unauthenticated", exception.getMessage()));
        } else if (exception instanceof UnauthorizedException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new RootDto("unauthorized", exception.getMessage()));
        } else if (exception instanceof UsecaseException) {
            return ResponseEntity.ok().body(new RootDto("fail", exception.getMessage()));
        } else {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RootDto("fail", exception.toString()));
        }
    }
}
