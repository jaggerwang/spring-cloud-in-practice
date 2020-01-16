package net.jaggerwang.scip.common.usecase.exception;

public class NotFoundException extends UsecaseException {
    private static final long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(message);
    }
}
