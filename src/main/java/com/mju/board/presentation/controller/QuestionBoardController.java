package com.mju.board.presentation.controller;

import com.mju.board.application.QuestionBoardService;
import com.mju.board.domain.model.Exception.AlreadyLikedException;
import com.mju.board.domain.model.Exception.CommentNotFindException;
import com.mju.board.domain.model.Exception.QnABoardNotFindException;
import com.mju.board.domain.model.QuestionBoard;
import com.mju.board.domain.model.QuestionCommend;
import com.mju.board.domain.model.Result.CommonResult;
import com.mju.board.domain.service.ResponseService;
import com.mju.board.domain.service.UserService;
import com.mju.board.presentation.dto.qnadto.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.mju.board.domain.model.Exception.ExceptionList.ALREADY_LIKED_USER;
import static com.mju.board.domain.model.Exception.ExceptionList.UNMATCHED_USERS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board-service/question")
@CrossOrigin(origins = "*")
public class QuestionBoardController {

    private final QuestionBoardService questionBoardService;
    private final ResponseService responseService;

    private final UserService userService;
    //////////////////////////////<문의게시판 && 문의 답변>//////////////////////////////

    ////////////////////////////////////<문의게시판>///////////////////////////////////
    //[관리자,강사진,수강생]Q&A 등록
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResult registerQnA(@RequestPart(value = "image", required = false) List<MultipartFile> images, @RequestPart(value = "qnARegisterDto") QnARegisterDto qnARegisterDto, HttpServletRequest request) {
        String userId = userService.getUserId(request);
        System.out.println(userId);
        userService.checkUserId(userId);
        questionBoardService.registerQnA(userId, qnARegisterDto, images);
        return responseService.getSuccessfulResult();
    }

    //Q&A List 조회
    @GetMapping("/show/listQnA")
    public CommonResult listQnA() {
        List<QuestionBoard> qnaBoardList = questionBoardService.getQnABoardList();
        List<QuestionBoard> qnaBoardListWithName = qnaWithUserName(qnaBoardList);
        CommonResult commonResult = responseService.getListResult(qnaBoardListWithName);
        return commonResult;
    }
    private List<QuestionBoard> qnaWithUserName(List<QuestionBoard> questionBoardList) {
        for (QuestionBoard questionBoard : questionBoardList) {
            String userId = questionBoard.getUserId();
            userService.checkUserId(userId);
            UserInfoDto userInfoDto = userService.getUserInfoDto(userId);
            String userName = userInfoDto.getUsername();
            questionBoard.addUserName(userName);
        }
        return questionBoardList;
    }

    //선택한 Q&A 보기
    @GetMapping("/show/{questionIndex}")
    public CommonResult findById(@PathVariable long questionIndex) {
        QuestionBoard qnaBoardOne = questionBoardService.getQnABoardOne(questionIndex);
        userService.checkUserId(qnaBoardOne.getUserId());
        UserInfoDto userInfoDto = userService.getUserInfoDto(qnaBoardOne.getUserId());
        qnaBoardOne.addUserName(userInfoDto.getUsername());
        CommonResult commonResult = responseService.getSingleResult(qnaBoardOne);
        return commonResult;
    }

    //[관리자,강사진,수강생]Q&A삭제
    @DeleteMapping("/delete/{questionIndex}")
    public CommonResult deleteQnA(@PathVariable Long questionIndex, HttpServletRequest request) {
        String userId = userService.getUserId(request);//로그인 유저
        userService.checkUserId(userId);
        QuestionBoard qnaBoardOne = questionBoardService.getQnABoardOne(questionIndex);
        UserInfoDto userInfoDto = userService.getUserInfoDto(qnaBoardOne.getUserId());
        String writerId = userInfoDto.getId();
        if (userId.equals(writerId)) {
            questionBoardService.deleteQnA(questionIndex);
        }else {
            throw new QnABoardNotFindException(UNMATCHED_USERS);
        }
        return responseService.getSuccessfulResult();
    }

    //[관리자,강사진,수강생]Q&A업데이트
    @PutMapping(value = "/update/{questionIndex}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResult updateFaq(@PathVariable Long questionIndex, @RequestPart(value = "image", required = false) List<MultipartFile> images, @RequestPart(value = "qnARegisterDto") QnAupdateDto qnAupdateDto, HttpServletRequest request) {
        String userId = userService.getUserId(request);//로그인 유저
        userService.checkUserId(userId);
        QuestionBoard qnaBoardOne = questionBoardService.getQnABoardOne(questionIndex);
        UserInfoDto userInfoDto = userService.getUserInfoDto(qnaBoardOne.getUserId());
        String writerId = userInfoDto.getId();
        if (userId.equals(writerId)) {
            questionBoardService.updateQnA(questionIndex, qnAupdateDto, images);
        }else {
            throw new QnABoardNotFindException(UNMATCHED_USERS);
        }
        return responseService.getSuccessfulResult();
    }

    //[수강생, 강사진, 관리자]좋아요 증가 (client에서 계정당 1개까지만 제한)
    @GetMapping("/goodCheck/{questionIndex}")
    public CommonResult goodCheck(@PathVariable Long questionIndex, HttpServletRequest request) {
        String userId = userService.getUserId(request);//로그인 유저
        userService.checkUserId(userId);

        boolean alreadyLiked = questionBoardService.checkIfAlreadyLikedQnA(questionIndex, userId);
        if (alreadyLiked) {
            throw new AlreadyLikedException(ALREADY_LIKED_USER);
        }
        questionBoardService.goodCheck(questionIndex, userId);
        return responseService.getSuccessfulResult();
    }

    //[관리자,강사진,수강생]제목, 내용으로 검색
    @PostMapping("/search")
    public CommonResult searchQnA(@RequestBody QnASearchDto qnASearchDto) {
        List<QuestionBoard> searchQuestionList = questionBoardService.searchQnA(qnASearchDto);
        CommonResult commonResult = responseService.getListResult(searchQuestionList);
        return commonResult;
    }


    //////////////////////////////<문의 답변 게시판>//////////////////////////////

    //[관리자,수강생,강사진] 답변등록
    @PostMapping("/commend/register/{questionIndex}")
    public CommonResult registerCommend(@PathVariable Long questionIndex, @RequestBody QnACommendDto qnACommendDto, HttpServletRequest request) {
        String userId = userService.getUserId(request);
        userService.checkUserId(userId);
        questionBoardService.registerCommend(userId, questionIndex, qnACommendDto);
        return responseService.getSuccessfulResult();
    }

    //[관리자,수강생,강사진]답변 선택한것 삭제
    @DeleteMapping("/commend/delete/{commendIndex}")
    public CommonResult deleteCommend(@PathVariable Long commendIndex, HttpServletRequest request) {
        String userId = userService.getUserId(request);
        userService.checkUserId(userId);
        QuestionCommend questionCommendOne = questionBoardService.getQnACommendOne(commendIndex);
        UserInfoDto userInfoDto = userService.getUserInfoDto(questionCommendOne.getUserId());
        String writerId = userInfoDto.getId();
        if (userId.equals(writerId)) {
            questionBoardService.deleteCommend(commendIndex);
            return responseService.getSuccessfulResult();
        }else {
            throw new CommentNotFindException(UNMATCHED_USERS);
        }
    }

    //[관리자,수강생,강사진]답변 업데이트
    @PutMapping("/commend/update/{commendIndex}")
    public CommonResult updateCommend(@PathVariable Long commendIndex, @RequestBody QnACommendDto qnACommendDto, HttpServletRequest request) {
        String userId = userService.getUserId(request);
        userService.checkUserId(userId);
        QuestionCommend questionCommendOne = questionBoardService.getQnACommendOne(commendIndex);
        UserInfoDto userInfoDto = userService.getUserInfoDto(questionCommendOne.getUserId());
        String writerId = userInfoDto.getId();
        if (userId.equals(writerId)) {
            questionBoardService.updateCommend(commendIndex, qnACommendDto);
            return responseService.getSuccessfulResult();
        }else {
            throw new CommentNotFindException(UNMATCHED_USERS);
        }
    }

    //답변 전체 조회
    @GetMapping("/show/commendList/{questionIndex}")
    public CommonResult getCommendList(@PathVariable Long questionIndex) {
        List<QuestionCommend> commendList = questionBoardService.getCommendList(questionIndex);
        List<QuestionCommend> commendListWithName = commendWithUserName(commendList);
        CommonResult commonResult = responseService.getListResult(commendListWithName);
        return commonResult;
    }
    private List<QuestionCommend> commendWithUserName(List<QuestionCommend> commendList) {
        for (QuestionCommend questionCommend : commendList) {
            String userId = questionCommend.getUserId();
            userService.checkUserId(userId);
            UserInfoDto userInfoDto = userService.getUserInfoDto(userId);
            String userName = userInfoDto.getUsername();
            questionCommend.addUserName(userName);
        }
        return commendList;
    }
    //[수강생, 강사진, 관리자] 답변 좋아요
    @GetMapping("/commend/goodCheck/{commendIndex}")
    public CommonResult goodCheckCommend(@PathVariable Long commendIndex, HttpServletRequest request) {
        String userId = userService.getUserId(request);//로그인 유저
        userService.checkUserId(userId);
        boolean alreadyLiked = questionBoardService.checkIfAlreadyLikedCommend(commendIndex, userId);
        if (alreadyLiked) {
            throw new AlreadyLikedException(ALREADY_LIKED_USER);
        }
        questionBoardService.goodCheckCommend(commendIndex, userId);
        return responseService.getSuccessfulResult();
    }
    /////////////////////////////////////마이메이지////////////////////////////////////////////

    //내 질문 목록 보기
    @GetMapping("/myPage/show/listMyQnA")
    public CommonResult listMyQnA(HttpServletRequest request) {
        String userId = userService.getUserId(request);//로그인 유저
        userService.checkUserId(userId);
        List<QuestionBoard> myQnAList = questionBoardService.getMyQnAList(userId);
        CommonResult commonResult = responseService.getListResult(myQnAList);
        return commonResult;
    }

    //내 정보 보기
    @GetMapping("/myPage/show/myInfo")
    public CommonResult showMyPageInfo(HttpServletRequest request) {
        String userId = userService.getUserId(request);//로그인 유저
        userService.checkUserId(userId);
        UserInfoDto userInfoDto = userService.getUserInfoDto(userId);
        CommonResult commonResult = responseService.getSingleResult(userInfoDto);
        return commonResult;
    }

}