package com.mju.Board.domain.model.Exception;

public class FaqBoardRegisterException extends RuntimeException {
    private final ExceptionList exceptionList;

    public FaqBoardRegisterException(ExceptionList exceptionList) {
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }

    public ExceptionList getExceptionList() {
        return exceptionList;
    }
}

