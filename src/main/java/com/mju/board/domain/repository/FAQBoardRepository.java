package com.mju.board.domain.repository;

import com.mju.board.domain.model.FAQBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FAQBoardRepository extends JpaRepository<FAQBoard, Long> {
    List<FAQBoard> findByType(FAQBoard.FAQType type);
    List<FAQBoard> findTop5ByOrderByCountDesc();
    List<FAQBoard> findByFaqTitleContainingIgnoreCaseOrFaqContentContainingIgnoreCase(String title, String content);

}
