package com.mju.Board.presentation.controller;

import com.mju.Board.application.BoardService;
import com.mju.Board.domain.model.FAQBoard;
import com.mju.Board.domain.model.Result.CommonResult;
import com.mju.Board.domain.service.ResponseService;
import com.mju.Board.presentation.dto.FAQRegisterDto;
import com.mju.Board.presentation.dto.FAQUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    //FAQ 등록
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
    @GetMapping("/show/faqHome")
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

    //선택한것 삭제
    @DeleteMapping("/delete/{faqIndex}")
    public CommonResult deleteFaq(@PathVariable Long faqIndex) throws Exception {
        boardService.deleteFaq(faqIndex);
        return responseService.getSuccessfulResult();
    }

    //선택한 게시물 보기 Q&A에 넣기
//    @GetMapping("show/{faqIndex}")
//    public FAQFindByIdDto findById(@PathVariable long faqIndex) {
//        return boardService.findById(faqIndex);
//    }

    //FAQ 업데이트
    @PutMapping("/update/{faqIndex}")
    public CommonResult updateFaq(@PathVariable Long faqIndex, @RequestBody FAQUpdateDto faqUpdateDto) {
        boardService.updateFaq(faqIndex, faqUpdateDto);
        return responseService.getSuccessfulResult();
    }

    //클릭한것 조회수 증가
    @GetMapping("/countFaqClick/{faqIndex}")
    public CommonResult countFaqClick (@PathVariable Long faqIndex) {
        boardService.countFaqClick(faqIndex);
        return responseService.getSuccessfulResult();
    }




}
