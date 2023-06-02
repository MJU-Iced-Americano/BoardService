package com.mju.board.presentation.controller;

import com.mju.board.application.HomeBannerService;
import com.mju.board.domain.model.HomeBanner;
import com.mju.board.domain.model.Result.CommonResult;
import com.mju.board.domain.service.ResponseService;
import com.mju.board.domain.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/board-service/banner")
@CrossOrigin(origins = "*")
public class HomeBannerController {

    private final HomeBannerService homeBannerService;
    private final ResponseService responseService;
    private final UserService userService;


    //////////////////////////<홈 베너>/////////////////////////////
    //배너 등록
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResult registerBanner(@RequestPart(value = "image", required = false) MultipartFile image, HttpServletRequest request) {
        String userId = userService.getUserId(request);
        userService.checkUserType(userId, "MANAGER");
        homeBannerService.registerBanner(image);
        return responseService.getSuccessfulResult();
    }
    //배너 삭제
    @DeleteMapping(value = "/delete/{homeBannerIndex}")
    public CommonResult deleteBanner(@PathVariable Long homeBannerIndex, HttpServletRequest request) {
        String userId = userService.getUserId(request);
        userService.checkUserType(userId, "MANAGER");
        homeBannerService.deleteBanner(homeBannerIndex);
        return responseService.getSuccessfulResult();
    }
    //배너 수정
    @PutMapping(value = "/update/{homeBannerIndex}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) //이미지에 아무것도 안하고 확인을 눌렀을때 오류처리
    public CommonResult updateBanner(@PathVariable Long homeBannerIndex, @RequestPart(value = "image", required = true) MultipartFile image, HttpServletRequest request) {
        String userId = userService.getUserId(request);
        userService.checkUserType(userId, "MANAGER");
        homeBannerService.updateBanner(homeBannerIndex, image);
        return responseService.getSuccessfulResult();
    }

    //배너 List 조회
    @GetMapping("/show/listBanner")
    public CommonResult getAllBanners() {
        List<HomeBanner> homeBannerList = homeBannerService.getListBanner();
        CommonResult commonResult = responseService.getListResult(homeBannerList);
        return commonResult;
    }
}
