package com.mju.board.application;


import com.mju.board.domain.model.Exception.ExceptionList;
import com.mju.board.domain.model.Exception.NonExceptionBanner;
import com.mju.board.domain.model.HomeBanner;
import com.mju.board.domain.repository.HomeBannerRepository;
import com.mju.board.domain.service.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HomeBannerServiceImpl implements HomeBannerService {
    private final S3Service s3Service;
    private final HomeBannerRepository homeBannerRepository;

    //////////////////////////<홈 베너>/////////////////////////////
    @Override
    @Transactional
    public void registerBanner(MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            String imageUrl = s3Service.uploadImageToS3Banner(image);
            HomeBanner homeBanner = HomeBanner.builder()
                    .imageUrl(imageUrl)
                    .build();
            homeBannerRepository.save(homeBanner);
        } else{
            throw new NonExceptionBanner(ExceptionList.NON_EXCEPTION_BANNER);
        }

    }

    @Override
    @Transactional
    public void deleteBanner(Long homeBannerIndex) {
        Optional<HomeBanner> optionalHomeBanner = homeBannerRepository.findById(homeBannerIndex);
        if (optionalHomeBanner.isPresent()) {
            HomeBanner homeBanner = optionalHomeBanner.get();
            String imageUrl = homeBanner.getImageUrl();
            if (imageUrl != null) {
                s3Service.deleteImageFromS3Banner(imageUrl);
            }
            homeBannerRepository.deleteById(homeBannerIndex);
        } else {
            throw new NonExceptionBanner(ExceptionList.NOT_FIND_BANNER);
        }
    }

    @Override
    @Transactional
    public void updateBanner(Long homeBannerIndex, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new NonExceptionBanner(ExceptionList.NON_EXCEPTION_BANNER);
        }
        Optional<HomeBanner> optionalHomeBanner = homeBannerRepository.findById(homeBannerIndex);
        if (optionalHomeBanner.isPresent()) {
            HomeBanner deleteHomeBanner = optionalHomeBanner.get();
            String deleteImageUrl = deleteHomeBanner.getImageUrl();
            if (deleteImageUrl != null) {
                // 기존 이미지 삭제
                s3Service.deleteImageFromS3Banner(deleteImageUrl);
            }
                // 새 이미지 추가 , Url 새로 추가
            String registerImageUrl = s3Service.uploadImageToS3Banner(image);
            deleteHomeBanner.updateImage(registerImageUrl);
            homeBannerRepository.save(deleteHomeBanner);
        }else {
            throw new NonExceptionBanner(ExceptionList.NOT_FIND_BANNER);
        }
    }

    @Override
    @Transactional
    public List<HomeBanner> getListBanner() {
        List<HomeBanner> homeBannerList = homeBannerRepository.findAll();
        return homeBannerList;
    }
}
