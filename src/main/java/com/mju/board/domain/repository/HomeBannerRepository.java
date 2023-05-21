package com.mju.board.domain.repository;

import com.mju.board.domain.model.FAQBoard;
import com.mju.board.domain.model.HomeBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeBannerRepository extends JpaRepository<HomeBanner, Long> {
}
