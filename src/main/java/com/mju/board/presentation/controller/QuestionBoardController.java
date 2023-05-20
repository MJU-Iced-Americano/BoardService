package com.mju.board.presentation.controller;

import com.mju.board.application.QuestionBoardService;
import com.mju.board.domain.model.FAQBoard;
import com.mju.board.domain.model.QuestionBoard;
import com.mju.board.domain.model.QuestionCommend;
import com.mju.board.domain.model.Result.CommonResult;
import com.mju.board.domain.service.ResponseService;
import com.mju.board.presentation.dto.faqdto.FAQSearchDto;
import com.mju.board.presentation.dto.qnadto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board-service/question")
@CrossOrigin(origins = "*")
public class QuestionBoardController {

    private final QuestionBoardService questionBoardService;
    private final ResponseService responseService;
    //////////////////////////////<문의게시판 && 문의 답변>//////////////////////////////

    ////////////////////////////////////<문의게시판>///////////////////////////////////
    //[관리자,강사진,수강생]Q&A 등록
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResult registerQnA(@RequestPart(value = "image", required = false) List<MultipartFile> images, @RequestPart(value = "qnARegisterDto") QnARegisterDto qnARegisterDto) {
        String userIndex = "test1234";

        questionBoardService.registerQnA(qnARegisterDto, images);
        return responseService.getSuccessfulResult();
    }

    //Q&A List 조회
    @GetMapping("/show/listQnA")
    public CommonResult listQnA() {
        List<QuestionBoard> qnaBoardList = questionBoardService.getQnABoardList();
        CommonResult commonResult = responseService.getListResult(qnaBoardList);
        return commonResult;
    }

    //선택한 Q&A 보기
    @GetMapping("/show/{questionIndex}")
    public CommonResult findById(@PathVariable long questionIndex) {
        QuestionBoard qnaBoardOne = questionBoardService.getQnABoardOne(questionIndex);
        CommonResult commonResult = responseService.getSingleResult(qnaBoardOne);
        return commonResult;
    }

    //[관리자,강사진,수강생]Q&A삭제
    @DeleteMapping("/delete/{questionIndex}")
    public CommonResult deleteQnA(@PathVariable Long questionIndex) {
        questionBoardService.deleteQnA(questionIndex);
        return responseService.getSuccessfulResult();
    }

    //[관리자,강사진,수강생]Q&A업데이트
    @PutMapping(value = "/update/{questionIndex}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResult updateFaq(@PathVariable Long questionIndex, @RequestPart(value = "image", required = false) List<MultipartFile> images, @RequestPart(value = "qnARegisterDto") QnAupdateDto qnAupdateDto) {
        questionBoardService.updateQnA(questionIndex, qnAupdateDto, images);
        return responseService.getSuccessfulResult();
    }

    //[수강생, 강사진, 관리자]좋아요 증가 (client에서 계정당 1개까지만 제한)
    @GetMapping("/goodCheck/{questionIndex}")
    public CommonResult goodCheck(@PathVariable Long questionIndex) {
        questionBoardService.goodCheck(questionIndex);
        return responseService.getSuccessfulResult();
    }

    //[관리자,강사진,수강생]제목, 내용으로 검색
    @PostMapping("/search")
    public CommonResult searchQnA(@RequestBody QnASearchDto qnASearchDto) {
        List<QuestionBoard> searchQuestionList = questionBoardService.searchQnA(qnASearchDto);
        CommonResult commonResult = responseService.getListResult(searchQuestionList);
        return commonResult;
    }

//    //[수강생]해당 문의 Q&A신고
//    @PostMapping(value = "/complaint/{questionIndex}")
//    public CommonResult complaintQnA(@PathVariable Long questionIndex, @RequestBody QnAComplaintDto qnAComplaintDto/*, @RequestHeader("Authorization") String token*/) {
////        Long complainerIndex = authService.getUserIdFromToken(token);//AuthService는 사용자 인증을 처리하는 서비스로 나중에 이걸로 사용자 인증 필요
////        qnAComplaintDto.setComplainerIndex(complainerIndex);
//        questionBoardService.complaintQnA(questionIndex, qnAComplaintDto);
//        return responseService.getSuccessfulResult();
//    }

    //////////////////////////////<문의 답변 게시판>//////////////////////////////

    //[관리자,수강생,강사진] 답변등록
    @PostMapping("/commend/register/{questionIndex}")
    public CommonResult registerCommend(@PathVariable Long questionIndex, @RequestBody QnACommendDto qnACommendDto) {
        questionBoardService.registerCommend(questionIndex, qnACommendDto);
        return responseService.getSuccessfulResult();
    }

    //[관리자,수강생,강사진]답변 선택한것 삭제
    @DeleteMapping("/commend/delete/{commendIndex}")
    public CommonResult deleteCommend(@PathVariable Long commendIndex) {
        questionBoardService.deleteCommend(commendIndex);
        return responseService.getSuccessfulResult();
    }

    //[관리자,수강생,강사진]답변 업데이트
    @PutMapping("/commend/update/{commendIndex}")
    public CommonResult updateCommend(@PathVariable Long commendIndex, @RequestBody QnACommendDto qnACommendDto) {
        questionBoardService.updateCommend(commendIndex, qnACommendDto);
        return responseService.getSuccessfulResult();
    }

    //답변 전체 조회
    @GetMapping("/show/commendList/{questionIndex}")
    public CommonResult getCommendList(@PathVariable Long questionIndex) {
        List<QuestionCommend> commendList = questionBoardService.getCommendList(questionIndex);
        CommonResult commonResult = responseService.getListResult(commendList);
        return commonResult;
    }

    //[수강생, 강사진, 관리자] 답변 좋아요
    @GetMapping("commend/goodCheck/{commendIndex}")
    public CommonResult goodCheckCommend(@PathVariable Long commendIndex) {
        questionBoardService.goodCheckCommend(commendIndex);
        return responseService.getSuccessfulResult();
    }



//    //[수강생]답변 신고
//    @PostMapping(value = "qna/commend/complaint/{commendIndex}")
//    public CommonResult complaintCommend(@PathVariable Long commendIndex, @RequestBody QnAComplaintDto qnAComplaintDto/*, @RequestHeader("Authorization") String token*/) {
////        Long complainerIndex = authService.getUserIdFromToken(token);//AuthService는 사용자 인증을 처리하는 서비스로 나중에 이걸로 사용자 인증 필요
////        qnAComplaintDto.setComplainerIndex(complainerIndex);
//        questionBoardService.complaintCommend(commendIndex, qnAComplaintDto);
//        return responseService.getSuccessfulResult();
//    }


}