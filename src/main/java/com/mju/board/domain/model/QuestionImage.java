package com.mju.board.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "questionimage")
public class QuestionImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long imageIndex;

    @Column(name = "image_url")
    private String imageUrl;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "question_index")
    private QuestionBoard questionBoard;

    @Builder
    public QuestionImage(String imageUrl, QuestionBoard questionBoard) {
        this.imageUrl = imageUrl;
        this.questionBoard = questionBoard;
    }

    public void initialization() {
        this.questionBoard = null;
    }
}