package com.mju.Board.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "question_index")
    private QuestionBoard questionBoard;

    @Builder
    public Image(String imageUrl, QuestionBoard questionBoard) {
        this.imageUrl = imageUrl;
        this.questionBoard = questionBoard;
    }
}