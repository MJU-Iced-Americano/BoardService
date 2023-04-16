package com.mju.Board.domain.model.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionList {

    UNKNOWN(-9999, "알 수 없는 오류가 발생하였습니다."),
    FAQBOARD_NOT_FINDTYPE_GENERAL_MEMBER(-5001, "일반회원FAQ 조회 요청 중 일반회원FAQ을 찾을 수 없습니다."),
    FAQBOARD_NOT_FINDTYPE_EDUCATION(-5001, "교육FAQ 조회 요청 중 교육FAQ을 찾을 수 없습니다."),
    FAQBOARD_REGISTER_GENERAL_MEMBER(-5001, "일반회원FAQ 등록 중 문제가 발생하였습니다."),
    FAQBOARD_REGISTER_EDUCATION(-5001, "교육FAQ 등록 중 문제가 발생하였습니다."),
    NOT_EXISTENT_FAQBOARD(-5001, "존재 하지 않는 FAQ 목록 입니다."),
    FAQBOARD_NOT_EXISTENT_DELETE(-5002, "삭제 요청 중 존재 하지 않는 FAQ 목록을 불러왔습니다"),
    FAQBOARD_NOT_EXISTENT_UPDATE(-5003, "업데이트 요청 중 존재 하지 않는 FAQ 목록을 불러왔습니다"),
    FAQBOARD_NOT_EXISTENT_COUNT(-5004, "조회수 업데이트 중 존재 하지 않는 FAQ 목록을 불러왔습니다.");
    private final int code;
    private final String message;
}
