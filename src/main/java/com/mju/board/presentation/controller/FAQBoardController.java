package com.mju.board.presentation.controller;

import com.mju.board.application.FAQBoardService;
import com.mju.board.domain.model.FAQBoard;
import com.mju.board.domain.model.Result.CommonResult;
import com.mju.board.domain.service.ResponseService;
import com.mju.board.domain.service.UserService;
import com.mju.board.presentation.dto.faqdto.FAQRegisterDto;
import com.mju.board.presentation.dto.faqdto.FAQSearchDto;
import com.mju.board.presentation.dto.faqdto.FAQUpdateDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board-service/faq")
@CrossOrigin(origins = "*")
public class FAQBoardController {

    private final FAQBoardService faqBoardService;
    private final ResponseService responseService;
    private final UserService userService;
    @GetMapping("/ping")
    public String ping() {
        return "paadfong";
    }

    //요청값들을 자바 객체로 만드는 역할 presentation
    //////////////////////////<FAQ게시판>/////////////////////////////
    //[관리자]FAQ 등록
    @PostMapping("/register")
    public CommonResult registerFaq(@RequestBody FAQRegisterDto faqRegisterDto, HttpServletRequest request) {
//        Object principal = authentication.getPrincipal();
//        if (principal instanceof UserDetails) {//UserDetails 인터페이스를 구현한 경우
//            userIndex = ((UserDetails) principal).getUsername();
//        } else {
//            userIndex = principal.toString(); // 구현되지 않은 경우에는 toString()
//        }
        String userId = userService.getUserId(request);
        userService.checkUserType(userId, "MANAGER");
        faqBoardService.registerFaq(faqRegisterDto);
        return responseService.getSuccessfulResult();
    }

    //Home에 TOP5 조회
    @GetMapping("/show/Home")
    public CommonResult faqHome() {
        List<FAQBoard> faqTop5List = faqBoardService.getFaqTop5();
        CommonResult commonResult = responseService.getListResult(faqTop5List);
        return commonResult;
    }

    //일반회원 FAQ 조회
    @GetMapping("/show/listFaqGeneral")
    public CommonResult listFaqGeneral() {
//      CommonResult commonResult = responseService.getListResult(faqBoardRepository.findByType(FAQBoard.FAQType.GENERAL_MEMBER));
        List<FAQBoard> generalFAQBoardList = faqBoardService.getGeneralFAQBoardList();
        CommonResult commonResult = responseService.getListResult(generalFAQBoardList);
        return commonResult;
    }

    //교육 FAQ 조회
    @GetMapping("/show/listFaqAdu")
    public CommonResult listFaqAdu() {
        List<FAQBoard> aduFaqBoardList = faqBoardService.getAduFAQBoardList();
        CommonResult commonResult = responseService.getListResult(aduFaqBoardList);
        return commonResult;
    }


    //[관리자]선택한것 삭제
    @DeleteMapping("/delete/{faqIndex}")
    public CommonResult deleteFaq(@PathVariable Long faqIndex, HttpServletRequest request) throws Exception {
        String userId = userService.getUserId(request);
        userService.checkUserType(userId, "MANAGER");
        faqBoardService.deleteFaq(faqIndex);
        return responseService.getSuccessfulResult();
    }


    //[관리자]FAQ 업데이트
    @PutMapping("/update/{faqIndex}")
    public CommonResult updateFaq(@PathVariable Long faqIndex, @RequestBody FAQUpdateDto faqUpdateDto, HttpServletRequest request) {
        String userId = userService.getUserId(request);
        userService.checkUserType(userId, "MANAGER");
        faqBoardService.updateFaq(faqIndex, faqUpdateDto);
        return responseService.getSuccessfulResult();
    }

    //[수강생]클릭한것 조회수 증가
    @GetMapping("/countFaqClick/{faqIndex}")
    public CommonResult countFaqClick(@PathVariable Long faqIndex) {
        faqBoardService.countFaqClick(faqIndex);
        return responseService.getSuccessfulResult();
    }

    //[관리자,강사진,수강생]제목, 내용으로 검색
    @PostMapping("/search")
    public CommonResult searchFaq(@RequestBody FAQSearchDto faqSearchDto) {
        List<FAQBoard> searchFAQBoardList = faqBoardService.searchFaq(faqSearchDto);
        CommonResult commonResult = responseService.getListResult(searchFAQBoardList);
        return commonResult;
    }

}