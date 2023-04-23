package com.mju.Board.domain.service;

import com.mju.Board.domain.model.Exception.ExceptionList;
import com.mju.Board.domain.model.Exception.FaqBoardNotFindException;
import com.mju.Board.domain.model.Exception.FaqBoardRegisterException;
import com.mju.Board.domain.model.Result.CommonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.tools.Diagnostic;

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

    @ExceptionHandler({FaqBoardRegisterException.class})
    protected CommonResult handleCustom(FaqBoardRegisterException e) {
        log.error("non exception FAQBoard", e);
        ExceptionList exceptionList = e.getExceptionList();
        return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
    }

}
