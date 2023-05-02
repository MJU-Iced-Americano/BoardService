package com.mju.Board.domain.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "questionboard")
public class QuestionBoard {
    @Builder
    public QuestionBoard(String questionTitle, String questionContent){
        this.questionTitle= questionTitle;
        this.questionContent = questionContent;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "question_index")
    private Long questionIndex;
    @Column(name = "question_title")
    private String questionTitle;

    @OneToMany(mappedBy = "questionBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionImage> questionImages = new ArrayList<>();

    @Column(name = "question_content")
    private String questionContent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "Answer _state")
//    private AnswerState answerState;


    @PrePersist // 데이터 생성이 이루어질때 사전 작업
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public void questionUpdate(String questionTitle, String questionContent) {
        this.questionTitle = questionTitle;
        this.questionContent = questionContent;
        this.updatedAt = LocalDateTime.now();//객체 불변성이 깨지지않게 이 객체안에서만 변동을 주는것.
    }

//    public void updateImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }
    public void addImage(String imageUrl) {
        QuestionImage questionImage = new QuestionImage(imageUrl, this);
        this.questionImages.add(questionImage);
    }

    public void updateImages(List<String> imageUrls) {
        this.questionImages.clear();
        for (String imageUrl : imageUrls) {
            addImage(imageUrl);
        }
    }
    public void removeImage(QuestionImage questionImage) {
        this.questionImages.remove(questionImage);
        questionImage.initialization();
    }
}
