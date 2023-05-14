package com.mju.board.domain.model.Exception;

public class CommentNotFindException extends RuntimeException{
    private final ExceptionList exceptionList;

    public CommentNotFindException(ExceptionList exceptionList) {
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }

    public ExceptionList getExceptionList() {
        return exceptionList;
    }
}
