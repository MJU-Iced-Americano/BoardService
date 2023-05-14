package com.mju.board.domain.repository;

import com.mju.board.domain.model.QuestionCommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionCommendRepository extends JpaRepository<QuestionCommend, Long> {
}
