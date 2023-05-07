package com.mju.Board.domain.repository;

import com.mju.Board.domain.model.QuestionCommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionCommendRepository extends JpaRepository<QuestionCommend, Long> {
}
