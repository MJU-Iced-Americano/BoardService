package com.mju.board.domain.service;

import com.mju.board.domain.model.Exception.*;
import com.mju.board.domain.model.Result.CommonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionService {

    private final ResponseService responseService;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult unknown(Exception e){
        log.error("unknown exception", e);
        return responseService.getFailResult(ExceptionList.UNKNOWN.getCode(), ExceptionList.UNKNOWN.getMessage());
    }

    @ExceptionHandler({FaqBoardNotFindException.class})
    protected CommonResult handleCustom(FaqBoardNotFindException e) {
        log.error("non exception FAQBoard", e);
        ExceptionList exceptionList = e.getExceptionList();
        return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
    }


    @ExceptionHandler({QnABoardNotFindException.class})
    protected CommonResult handleCustom(QnABoardNotFindException e) {
        log.error("non exception Q&ABoard", e);
        ExceptionList exceptionList = e.getExceptionList();
        return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
    }
    @ExceptionHandler({CommentNotFindException.class})
    protected CommonResult handleCustom(CommentNotFindException e) {
        log.error("not find commend", e);
        ExceptionList exceptionList = e.getExceptionList();
        return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
    }

    @ExceptionHandler({NonExceptionBanner.class})
    protected CommonResult handleCustom(NonExceptionBanner e) {
        log.error("non exception banner", e);
        ExceptionList exceptionList = e.getExceptionList();
        return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
    }
    @ExceptionHandler({AlreadyLikedException.class})
    protected CommonResult handleCustom(AlreadyLikedException e) {
        log.error("Already Liked user", e);
        ExceptionList exceptionList = e.getExceptionList();
        return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
    }

}
