package net.jaggerwang.scip.common.adapter.api.controller;

import net.jaggerwang.scip.common.usecase.port.service.dto.RootDTO;
import net.jaggerwang.scip.common.usecase.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

public class RestExceptionConverter {
    public ResponseEntity<RootDTO> convert(Throwable throwable) {
        if (throwable instanceof InternalApiException) {
            var e = (InternalApiException) throwable;
            return ResponseEntity
                    .status(e.getStatus())
                    .body(new RootDTO(e.getCode(), e.getMessage(), e.getData()));
        } else if (throwable instanceof NotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RootDTO("not_found", throwable.getMessage()));
        } else if (throwable instanceof UnauthenticatedException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new RootDTO("unauthenticated", throwable.getMessage()));
        } else if (throwable instanceof UnauthorizedException ||
                throwable instanceof AccessDeniedException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new RootDTO("unauthorized", throwable.getMessage()));
        } else if (throwable instanceof UsecaseException) {
            return ResponseEntity.ok().body(new RootDTO("fail", throwable.getMessage()));
        } else {
            throwable.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RootDTO("fail", throwable.toString()));
        }
    }
}
