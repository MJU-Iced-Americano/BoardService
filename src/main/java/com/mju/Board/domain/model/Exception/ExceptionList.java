package com.mju.Board.domain.model.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionList {

    UNKNOWN(-9999, "알 수 없는 오류가 발생하였습니다."),
    FAQBOARD_NOT_FIND_TOP5(-5001, "FAQ 조회수 TOP5 목록이 없습니다."),
    FAQBOARD_NOT_FINDTYPE_GENERAL_MEMBER(-5002, "일반회원FAQ 목록이 없습니다."),
    FAQBOARD_NOT_FINDTYPE_EDUCATION(-5003, "교육FAQ 목록이 없습니다."),
    FAQBOARD_NOT_EXISTENT_DELETE(-5006, "삭제 요청 중 존재 하지 않는 FAQ 목록을 불러왔습니다"),
    FAQBOARD_NOT_EXISTENT_UPDATE(-5007, "업데이트 요청 중 존재 하지 않는 FAQ 목록을 불러왔습니다"),
    FAQBOARD_NOT_EXISTENT_KEYWORD(-5008, "해당 검색 내역이 존재하지 않습니다."),
    FAQBOARD_NOT_EXISTENT_COUNT(-5009, "조회수 업데이트 중 존재 하지 않는 FAQ 목록을 불러왔습니다.");
    private final int code;
    private final String message;
}
