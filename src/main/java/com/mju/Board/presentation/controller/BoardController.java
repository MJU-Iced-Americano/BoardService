package com.mju.Board.presentation.controller;

import com.mju.Board.application.BoardService;
import com.mju.Board.domain.model.FAQBoard;
import com.mju.Board.domain.model.QuestionBoard;
import com.mju.Board.domain.model.Result.CommonResult;
import com.mju.Board.domain.service.ResponseService;
import com.mju.Board.presentation.dto.faqdto.FAQRegisterDto;
import com.mju.Board.presentation.dto.faqdto.FAQSearchDto;
import com.mju.Board.presentation.dto.faqdto.FAQUpdateDto;
import com.mju.Board.presentation.dto.qnadto.QnARegisterDto;
import com.mju.Board.presentation.dto.qnadto.QnAupdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board-service")
public class BoardController {

    private final BoardService boardService;
    private final ResponseService responseService;

    @GetMapping("/ping")
    public String ping() {
        return "paadfong";
    }

    //요청값들을 자바 객체로 만드는 역할 presentation
    //////////////////////////<FAQ게시판>/////////////////////////////
    //[관리자]FAQ 등록
    @PostMapping("/registerFaqGeneral")
    public CommonResult registerFaqGeneral(@RequestBody FAQRegisterDto faqRegisterDto){
        boardService.registerFaqGeneral(faqRegisterDto);
        return responseService.getSuccessfulResult();
    }
    @PostMapping("/registerFaqAdu")
    public CommonResult registerFaqAdu(@RequestBody FAQRegisterDto faqRegisterDto){
        boardService.registerFaqAdu(faqRegisterDto);
        return responseService.getSuccessfulResult();
    }
    //Home에 TOP5 조회
    @GetMapping("/show/Home")
    public CommonResult faqHome () {
        List<FAQBoard> faqTop5List = boardService.getFaqTop5();
        CommonResult commonResult = responseService.getListResult(faqTop5List);
        return commonResult;
    }

    //일반회원 FAQ 조회
    @GetMapping("/show/listFaqGeneral")
    public CommonResult listFaqGeneral() {
//      CommonResult commonResult = responseService.getListResult(faqBoardRepository.findByType(FAQBoard.FAQType.GENERAL_MEMBER));
        List<FAQBoard> generalFAQBoardList = boardService.getGeneralFAQBoardList();
        CommonResult commonResult = responseService.getListResult(generalFAQBoardList);
        return commonResult;
    }

    //교육 FAQ 조회
    @GetMapping("/show/listFaqAdu")
    public CommonResult listFaqAdu() {
        List<FAQBoard> aduFaqBoardList = boardService.getAduFAQBoardList();
        CommonResult commonResult = responseService.getListResult(aduFaqBoardList);
        return commonResult;
    }

//    //선택한 FAQ게시물 보기
//    @GetMapping("/show/{faqIndex}")
//    public CommonResult findByFAQId(@PathVariable long faqIndex) {
//        FAQBoard findByFaqOne = boardService.findByFAQId(faqIndex);
//        CommonResult commonResult = responseService.getSingleResult(findByFaqOne);
//        return commonResult;
//    }

    //[관리자]선택한것 삭제
    @DeleteMapping("/delete/{faqIndex}")
    public CommonResult deleteFaq(@PathVariable Long faqIndex) throws Exception {
        boardService.deleteFaq(faqIndex);
        return responseService.getSuccessfulResult();
    }


    //[관리자]FAQ 업데이트
    @PutMapping("/update/{faqIndex}")
    public CommonResult updateFaq(@PathVariable Long faqIndex, @RequestBody FAQUpdateDto faqUpdateDto) {
        boardService.updateFaq(faqIndex, faqUpdateDto);
        return responseService.getSuccessfulResult();
    }

    //[수강생]클릭한것 조회수 증가
    @GetMapping("/countFaqClick/{faqIndex}")
    public CommonResult countFaqClick (@PathVariable Long faqIndex) {
        boardService.countFaqClick(faqIndex);
        return responseService.getSuccessfulResult();
    }
    //[관리자,강사진,수강생]제목, 내용으로 검색
    @PostMapping("/search")
    public CommonResult searchFaq (@RequestBody FAQSearchDto faqSearchDto) {
        List<FAQBoard> searchFAQBoardList = boardService.searchFaq(faqSearchDto);
        CommonResult commonResult = responseService.getListResult(searchFAQBoardList);
        return commonResult;
    }
    //////////////////////////////<문의게시판>//////////////////////////////
    //[관리자,강사진,수강생]Q&A 등록
    @PostMapping(value = "qna/registerQnA", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResult registerQnA(@RequestPart(value = "image", required = false) List<MultipartFile> images, @RequestPart(value = "qnARegisterDto") QnARegisterDto qnARegisterDto) {
        boardService.registerQnA(qnARegisterDto, images);
        return responseService.getSuccessfulResult();
    }

    //Q&A List 조회
    @GetMapping("qna/show/listQnA")
    public CommonResult listQnA() {
        List<QuestionBoard> qnaBoardList = boardService.getQnABoardList();
        CommonResult commonResult = responseService.getListResult(qnaBoardList);
        return commonResult;
    }

    //선택한 Q&A 보기
    @GetMapping("qna/show/{questionIndex}")
    public CommonResult findById(@PathVariable long questionIndex) {
        QuestionBoard qnaBoardOne = boardService.getQnABoardOne(questionIndex);
        CommonResult commonResult = responseService.getSingleResult(qnaBoardOne);
        return commonResult;
    }

    //[관리자,강사진,수강생]Q&A삭제
    @DeleteMapping("qna/delete/{questionIndex}")
    public CommonResult deleteQnA(@PathVariable Long questionIndex) {
        boardService.deleteQnA(questionIndex);
        return responseService.getSuccessfulResult();
    }

    //[관리자,강사진,수강생]Q&A업데이트
    @PutMapping(value = "qna/update/{questionIndex}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResult updateFaq(@PathVariable Long questionIndex, @RequestPart(value = "image", required = false) List<MultipartFile> images, @RequestPart(value = "qnARegisterDto") QnAupdateDto qnAupdateDto) {
        boardService.updateQnA(questionIndex, qnAupdateDto, images);
        return responseService.getSuccessfulResult();
    }





}
