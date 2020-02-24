package net.jaggerwang.scip.common.adapter.controller;

import net.jaggerwang.scip.common.usecase.port.service.dto.RootDto;
import net.jaggerwang.scip.common.usecase.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RestExceptionConverter {
    public ResponseEntity<RootDto> convert(Throwable throwable) {
        if (throwable instanceof InternalApiException) {
            var e = (InternalApiException) throwable;
            return ResponseEntity
                    .status(e.getStatus())
                    .body(new RootDto(e.getCode(), e.getMessage(), e.getData()));
        } else if (throwable instanceof NotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RootDto("not_found", throwable.getMessage()));
        } else if (throwable instanceof UnauthenticatedException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new RootDto("unauthenticated", throwable.getMessage()));
        } else if (throwable instanceof UnauthorizedException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new RootDto("unauthorized", throwable.getMessage()));
        } else if (throwable instanceof UsecaseException) {
            return ResponseEntity.ok().body(new RootDto("fail", throwable.getMessage()));
        } else {
            throwable.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RootDto("fail", throwable.toString()));
        }
    }
}
