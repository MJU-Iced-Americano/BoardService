package com.mju.Board.presentation.controller;

import com.mju.Board.application.BoardService;
import com.mju.Board.domain.model.FAQBoard;
import com.mju.Board.domain.model.Result.CommonResult;
import com.mju.Board.presentation.dto.FAQRegisterDto;
import com.mju.Board.presentation.dto.FAQUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/board-service")
public class BoardController {

    @Autowired
    BoardService boardService;

    @GetMapping("/ping")
    public String ping() {
        return "paadfong";
    }

    //요청값들을 자바 객체로 만드는 역할 presentation
    //FAQ 등록
    @PostMapping("/registerFaqGeneral")
    public CommonResult registerFaqGeneral(@RequestBody FAQRegisterDto faqRegisterDto){
        return boardService.registerFaqGeneral(faqRegisterDto);
    }
    @PostMapping("/registerFaqAdu")
    public CommonResult registerFaqAdu(@RequestBody FAQRegisterDto faqRegisterDto){
        return boardService.registerFaqAdu(faqRegisterDto);
    }

    //일반회원 FAQ 조회
    @GetMapping("show/listFaqGeneral")
    public List<FAQBoard> listFaqGeneral() {
        List<FAQBoard> generalFaqBoardList = boardService.getGeneralFAQBoardList();
        return generalFaqBoardList;
    }

    //교육 FAQ 조회
    @GetMapping("show/listFaqAdu")
    public List<FAQBoard> listFaqAdu() {
        List<FAQBoard> aduFaqBoardList = boardService.getAduFAQBoardList();
        return aduFaqBoardList;
    }

    //선택한것 삭제
    @DeleteMapping("/delete/{faqIndex}")
    public CommonResult deleteFaq(@PathVariable Long faqIndex) {
        return boardService.deleteFaq(faqIndex);
    }

    //선택한 게시물 보기 Q&A에 넣기
//    @GetMapping("show/{faqIndex}")
//    public FAQFindByIdDto findById(@PathVariable long faqIndex) {
//        return boardService.findById(faqIndex);
//    }

    //FAQ 업데이트
    @PutMapping("update/{faqIndex}")
    public CommonResult updateFaq(@PathVariable Long faqIndex, @RequestBody FAQUpdateDto faqUpdateDto) {
        return boardService.updateFaq(faqIndex, faqUpdateDto);
    }

    //클릭한것 조회수 증가
    @PostMapping("/countFaqClick/{faqIndex}")
    public CommonResult countFaqClick (@PathVariable Long faqIndex) {
        return boardService.countFaqClick(faqIndex);

    }


}
