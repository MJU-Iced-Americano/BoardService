package com.mju.board.domain.repository;

import com.mju.board.domain.model.QuestionComplaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionComplaintRepository extends JpaRepository<QuestionComplaint, Long> {
}