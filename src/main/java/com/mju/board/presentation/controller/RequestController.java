package com.mju.board.presentation.controller;

import com.mju.board.application.QuestionBoardService;
import com.mju.board.domain.model.QuestionBoard;
import com.mju.board.presentation.dto.qnadto.RequestQuestionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board-service/question")
@CrossOrigin(origins = "*")
public class RequestController {
    private final QuestionBoardService questionBoardService;
    //선택한 Q&A 보기(Complaint)
    @GetMapping("/show/request/{questionIndex}")
    public ResponseEntity<RequestQuestionDto> findByIdComplaint(@PathVariable long questionIndex) {
        QuestionBoard qnaBoardOne = questionBoardService.getQnABoardOne(questionIndex);
        RequestQuestionDto requestQuestionDto = new RequestQuestionDto();
        requestQuestionDto.setQuestionIndex(qnaBoardOne.getQuestionIndex());
//        questionBoardDto.setUserIndex(qnaBoardOne.getUserIndex());

        return ResponseEntity.ok(requestQuestionDto);
    }
}


