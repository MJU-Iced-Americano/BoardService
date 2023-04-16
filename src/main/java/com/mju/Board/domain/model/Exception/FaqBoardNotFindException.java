package com.mju.Board.domain.model.Exception;

public class FaqBoardNotFindException extends RuntimeException {
    private final ExceptionList exceptionList;

    public FaqBoardNotFindException(ExceptionList exceptionList) {
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }

    public ExceptionList getExceptionList() {
        return exceptionList;
    }
}
