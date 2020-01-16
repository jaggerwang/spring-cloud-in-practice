package net.jaggerwang.scip.common.usecase.exception;

public class UsecaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UsecaseException(String message) {
        super(message);
    }
}
