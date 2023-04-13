package com.mju.Board.domain.service;

import com.mju.Board.domain.model.Exception.ExceptionList;
import com.mju.Board.domain.model.Result.CommonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

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

    @ExceptionHandler({NoSuchElementException.class, EmptyResultDataAccessException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected CommonResult notFoundFAQBoard(Exception e){
        log.error("FAQBoard not found", e);
        return responseService.getFailResult(ExceptionList.NOT_EXISTENT_FAQBOARD.getCode(), ExceptionList.NOT_EXISTENT_FAQBOARD.getMessage());
    }

}
