package com.mju.Board.domain.repository;

import com.mju.Board.domain.model.FAQBoard;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FAQBoardRepository extends JpaRepository<FAQBoard, Long> {
    List<FAQBoard> findByType(FAQBoard.FAQType type);
    List<FAQBoard> findTop5ByOrderByCountDesc();
}
