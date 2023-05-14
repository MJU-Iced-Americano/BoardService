package com.mju.board.domain.model.Exception;

public class QnABoardNotFindException extends RuntimeException {
    private final ExceptionList exceptionList;

    public QnABoardNotFindException(ExceptionList exceptionList) {
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }

    public ExceptionList getExceptionList() {
        return exceptionList;
    }
}
