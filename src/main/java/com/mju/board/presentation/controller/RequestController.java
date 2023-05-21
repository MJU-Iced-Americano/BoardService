package com.mju.board.presentation.controller;

import com.mju.board.application.QuestionBoardService;
import com.mju.board.domain.model.QuestionBoard;
import com.mju.board.domain.model.QuestionCommend;
import com.mju.board.domain.model.Result.SingleResult;
import com.mju.board.domain.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board-service")
@CrossOrigin(origins = "*")
public class RequestController {
    private final QuestionBoardService questionBoardService;
    private final ResponseService responseService;

    //선택한 Q&A 보기(for 신고)
    @GetMapping("/question/show/request/{questionIndex}")
    public SingleResult questionFindById(@PathVariable long questionIndex) {
        QuestionBoard qnaBoardOne = questionBoardService.getQnABoardOne(questionIndex);
        SingleResult requestResult = responseService.getSingleResult(qnaBoardOne);
        return requestResult;
    }

    //선택한 Commend 보기(for 신고)
    @GetMapping("/commend/show/request/{commendIndex}")
    public SingleResult commendFindById(@PathVariable long commendIndex) {
        QuestionCommend questionCommend = questionBoardService.getQnACommendOne(commendIndex);
        SingleResult requestResult = responseService.getSingleResult(questionCommend);
        return requestResult;
    }
//    @GetMapping("/show/request/{questionIndex}")
//    public ResponseEntity<RequestQuestionDto> findByIdComplaint(@PathVariable long questionIndex) {
//        QuestionBoard qnaBoardOne = questionBoardService.getQnABoardOne(questionIndex);
//        RequestQuestionDto requestQuestionDto = new RequestQuestionDto();
//        requestQuestionDto.setQuestionIndex(qnaBoardOne.getQuestionIndex());
////        questionBoardDto.setUserIndex(qnaBoardOne.getUserIndex());
//
//        return ResponseEntity.ok(requestQuestionDto);
//    }
}


