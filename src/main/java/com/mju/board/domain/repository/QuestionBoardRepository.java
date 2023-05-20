package com.mju.board.domain.repository;

import com.mju.board.domain.model.QuestionBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionBoardRepository extends JpaRepository<QuestionBoard, Long> {
    List<QuestionBoard> findByQuestionTitleContainingIgnoreCaseOrQuestionContentContainingIgnoreCase(String keyword, String keyword1);

    List<QuestionBoard> findAllByOrderByCreatedAtDesc();
}
