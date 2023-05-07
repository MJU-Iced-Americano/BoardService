package com.mju.Board.domain.repository;

import com.mju.Board.domain.model.QuestionComplaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionComplaintRepository extends JpaRepository<QuestionComplaint, Long> {
}