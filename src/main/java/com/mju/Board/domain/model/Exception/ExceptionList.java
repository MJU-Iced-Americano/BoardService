package com.mju.Board.domain.model.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionList {

    UNKNOWN(-9999, "알 수 없는 오류가 발생하였습니다."),
    NOT_EXISTENT_FAQBOARD(5005, "존재 하지 않는 FAQ게시판 입니다.");


    private final int code;
    private final String message;
}
