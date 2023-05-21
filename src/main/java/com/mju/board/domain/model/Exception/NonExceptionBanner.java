package com.mju.board.domain.model.Exception;

public class NonExceptionBanner extends RuntimeException{
    private final ExceptionList exceptionList;

    public NonExceptionBanner(ExceptionList exceptionList) {
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }

    public ExceptionList getExceptionList() {
        return exceptionList;
    }
}
