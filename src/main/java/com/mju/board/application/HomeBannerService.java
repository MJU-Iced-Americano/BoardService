package com.mju.board.application;


import com.mju.board.domain.model.HomeBanner;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HomeBannerService {

    public void registerBanner(MultipartFile image);

    public void deleteBanner(Long homeBannerIndex);

    public void updateBanner(Long homeBannerIndex, MultipartFile image);

    public List<HomeBanner> getListBanner();
}
