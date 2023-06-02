package com.mju.board.domain.model.Exception;

public class AlreadyLikedException extends RuntimeException{
    private final ExceptionList exceptionList;

    public AlreadyLikedException(ExceptionList exceptionList) {
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }

    public ExceptionList getExceptionList() {
        return exceptionList;
    }
}
