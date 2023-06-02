package com.mju.board.domain.model.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionList {

    UNKNOWN(-9999, "알 수 없는 오류가 발생하였습니다."),
    ///////////////////////////////FAQ게시판 오류//////////////////////////////
    FAQBOARD_NOT_FIND_TOP5(-5001, "FAQ 조회수 TOP5 목록이 없습니다."),
    FAQBOARD_NOT_FINDTYPE_GENERAL_MEMBER(-5002, "일반회원FAQ 목록이 없습니다."),
    FAQBOARD_NOT_FINDTYPE_EDUCATION(-5003, "교육FAQ 목록이 없습니다."),
    FAQBOARD_NOT_EXISTENT(-5006, "존재 하지 않는 FAQ 목록을 불러왔습니다"),
    BOARD_NOT_EXISTENT_KEYWORD(-5008, "해당 검색 내역이 존재하지 않습니다."),
   ////////////////////////////////문의 게시판 오류/////////////////////////////////
    QNABOARD_NOT_EXISTENT_ALL(-6001, "문의 게시판의 전체 조회 목록이 없습니다."),
    QNABOARD_NOT_EXISTENT(-6004, "존재 하지 않는 문의게시글을 불러왔습니다."),
    QNACOMMEND_NOT_EXISTENT(-7001, "존재하지 않은 답변입니다."),
    ////////////////////////////////베너 오류/////////////////////////////////
    NON_EXCEPTION_BANNER(-8888, "배너 이미지가 등록되지 않았습니다."),
    NOT_FIND_BANNER(-8888, "등록된 배너 이미지를 찾을 수 없습니다."),
    /////////////////////////////유저 오류///////////////////////////////
    EMPTY_USER(-5051, "유저 정보를 입력해 주세요."),
    NOT_EXISTENT_USER(-5055, "존재하지 않는 유저입니다."),
    EMPTY_JWT(-5054, "토큰이 없습니다. 확인부탁드립니다."),
    NOT_MANAGER_USER(-5052, "관리자가 아닙니다. 관리자로 로그인 다시 부탁드립니다."),
    UNMATCHED_USERS(-1111, "본인이 아니면 게시글 및 답변에 접근할수 없습니다."),
    ALREADY_LIKED_USER(-2222, "이미 좋아요를 누른 계정입니다.")
    ;
    private final int code;
    private final String message;
}
