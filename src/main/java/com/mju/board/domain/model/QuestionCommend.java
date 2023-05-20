package com.mju.board.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "questioncommend")
public class QuestionCommend {
    @Builder
    public QuestionCommend(String commendContent, QuestionBoard questionBoard){
        this.commendContent= commendContent;
        this.questionBoard = questionBoard;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commend_index")
    private Long commendIndex;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "question_index")
    private QuestionBoard questionBoard;

    @Column(name = "commend_content")
    private String commendContent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "good_count")
    private int goodCount;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "Answer_state")
//    private AnswerState answerState;

    @PrePersist // 데이터 생성이 이루어질때 사전 작업
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public void commendUpdate(String commendContent) {
        this.commendContent = commendContent;
        this.updatedAt = LocalDateTime.now();
    }

    public void initialization(QuestionBoard questionBoard) {
        this.questionBoard = questionBoard;
    }

    public void incrementGood() {
        goodCount++;
    }
}
