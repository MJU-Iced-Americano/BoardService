package com.mju.Board.presentation.controller;

import com.mju.Board.application.BoardService;
import com.mju.Board.domain.model.FAQBoard;
import com.mju.Board.domain.model.QuestionBoard;
import com.mju.Board.domain.model.QuestionCommend;
import com.mju.Board.domain.model.Result.CommonResult;
import com.mju.Board.domain.service.ResponseService;
import com.mju.Board.presentation.dto.faqdto.FAQRegisterDto;
import com.mju.Board.presentation.dto.faqdto.FAQSearchDto;
import com.mju.Board.presentation.dto.faqdto.FAQUpdateDto;
import com.mju.Board.presentation.dto.qnadto.QnACommendDto;
import com.mju.Board.presentation.dto.qnadto.QnAComplaintDto;
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
    @PostMapping("faq/register")
    public CommonResult registerFaq(@RequestBody FAQRegisterDto faqRegisterDto) {
        boardService.registerFaq(faqRegisterDto);
        return responseService.getSuccessfulResult();
    }

    //Home에 TOP5 조회
    @GetMapping("faq/show/Home")
    public CommonResult faqHome() {
        List<FAQBoard> faqTop5List = boardService.getFaqTop5();
        CommonResult commonResult = responseService.getListResult(faqTop5List);
        return commonResult;
    }

    //일반회원 FAQ 조회
    @GetMapping("faq/show/listFaqGeneral")
    public CommonResult listFaqGeneral() {
//      CommonResult commonResult = responseService.getListResult(faqBoardRepository.findByType(FAQBoard.FAQType.GENERAL_MEMBER));
        List<FAQBoard> generalFAQBoardList = boardService.getGeneralFAQBoardList();
        CommonResult commonResult = responseService.getListResult(generalFAQBoardList);
        return commonResult;
    }

    //교육 FAQ 조회
    @GetMapping("faq/show/listFaqAdu")
    public CommonResult listFaqAdu() {
        List<FAQBoard> aduFaqBoardList = boardService.getAduFAQBoardList();
        CommonResult commonResult = responseService.getListResult(aduFaqBoardList);
        return commonResult;
    }


    //[관리자]선택한것 삭제
    @DeleteMapping("faq/delete/{faqIndex}")
    public CommonResult deleteFaq(@PathVariable Long faqIndex) throws Exception {
        boardService.deleteFaq(faqIndex);
        return responseService.getSuccessfulResult();
    }


    //[관리자]FAQ 업데이트
    @PutMapping("faq/update/{faqIndex}")
    public CommonResult updateFaq(@PathVariable Long faqIndex, @RequestBody FAQUpdateDto faqUpdateDto) {
        boardService.updateFaq(faqIndex, faqUpdateDto);
        return responseService.getSuccessfulResult();
    }

    //[수강생]클릭한것 조회수 증가
    @GetMapping("faq/countFaqClick/{faqIndex}")
    public CommonResult countFaqClick(@PathVariable Long faqIndex) {
        boardService.countFaqClick(faqIndex);
        return responseService.getSuccessfulResult();
    }

    //[관리자,강사진,수강생]제목, 내용으로 검색
    @PostMapping("faq/search")
    public CommonResult searchFaq(@RequestBody FAQSearchDto faqSearchDto) {
        List<FAQBoard> searchFAQBoardList = boardService.searchFaq(faqSearchDto);
        CommonResult commonResult = responseService.getListResult(searchFAQBoardList);
        return commonResult;
    }

    //////////////////////////////<문의게시판>//////////////////////////////
    //[관리자,강사진,수강생]Q&A 등록
    @PostMapping(value = "qna/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

    //[수강생]해당 문의 Q&A신고
    @PostMapping(value = "qna/complaint/{questionIndex}")
    public CommonResult complaintQnA(@PathVariable Long questionIndex, @RequestBody QnAComplaintDto qnAComplaintDto/*, @RequestHeader("Authorization") String token*/) {
//        Long complainerIndex = authService.getUserIdFromToken(token);//AuthService는 사용자 인증을 처리하는 서비스로 나중에 이걸로 사용자 인증 필요
//        qnAComplaintDto.setComplainerIndex(complainerIndex);
        boardService.complaintQnA(questionIndex, qnAComplaintDto);
        return responseService.getSuccessfulResult();
    }

    //////////////////////////////<문의답변게시판>//////////////////////////////

    //[관리자,수강생,강사진] 답변등록
    @PostMapping("qna/commend/register/{questionIndex}")
    public CommonResult registerCommend(@PathVariable Long questionIndex, @RequestBody QnACommendDto qnACommendDto) {
        boardService.registerCommend(questionIndex, qnACommendDto);
        return responseService.getSuccessfulResult();
    }

    //[관리자,수강생,강사진]답변 선택한것 삭제
    @DeleteMapping("qna/commend/delete/{commendIndex}")
    public CommonResult deleteCommend(@PathVariable Long commendIndex) {
        boardService.deleteCommend(commendIndex);
        return responseService.getSuccessfulResult();
    }

    //[관리자,수강생,강사진]답변 FAQ 업데이트
    @PutMapping("qna/commend/update/{commendIndex}")
    public CommonResult updateCommend(@PathVariable Long commendIndex, @RequestBody QnACommendDto qnACommendDto) {
        boardService.updateCommend(commendIndex, qnACommendDto);
        return responseService.getSuccessfulResult();
    }

    //답변 전체 조회
    @GetMapping("/qna/show/commendList/{questionIndex}")
    public CommonResult getCommendList(@PathVariable Long questionIndex) {
        List<QuestionCommend> commendList = boardService.getCommendList(questionIndex);
        CommonResult commonResult = responseService.getListResult(commendList);
        return commonResult;
    }
    //[수강생]답변 신고
    @PostMapping(value = "qna/commend/complaint/{commendIndex}")
    public CommonResult complaintCommend(@PathVariable Long commendIndex, @RequestBody QnAComplaintDto qnAComplaintDto/*, @RequestHeader("Authorization") String token*/) {
//        Long complainerIndex = authService.getUserIdFromToken(token);//AuthService는 사용자 인증을 처리하는 서비스로 나중에 이걸로 사용자 인증 필요
//        qnAComplaintDto.setComplainerIndex(complainerIndex);
        boardService.complaintCommend(commendIndex, qnAComplaintDto);
        return responseService.getSuccessfulResult();
    }


}