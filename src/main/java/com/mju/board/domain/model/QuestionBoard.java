package com.mju.board.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class QuestionBoard{

    public enum QuestionType {
        GENERAL, LECTURE, PAYMENT;
    }
    @Builder
    public QuestionBoard(String questionTitle, String questionContent, QuestionType type, String userId){
        this.questionTitle= questionTitle;
        this.questionContent = questionContent;
        this.type = type;
        this.userId = userId;
    }
    public QuestionBoard(QuestionBoard originalBoard) {
        // 복사 생성자 //답변리스트 제외
        this.questionIndex = originalBoard.questionIndex;
        this.questionTitle = originalBoard.getQuestionTitle();
        this.questionContent = originalBoard.getQuestionContent();
        this.questionImageList = originalBoard.getQuestionImageList();
        this.createdAt = originalBoard.createdAt;
        this.updatedAt = originalBoard.getUpdatedAt();
        this.goodCount = originalBoard.getGoodCount();
        this.type = originalBoard.getType();
        this.userId = originalBoard.getUserId();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_index")
    private Long questionIndex;
    @Column(name = "question_title")
    private String questionTitle;

    @OneToMany(mappedBy = "questionBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionImage> questionImageList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "questionBoard", cascade = CascadeType.ALL)
    private List<QuestionCommend> questionCommendList = new ArrayList<>(); //하나씩 등록, 삭제, 수정을 할거라 필요없을것같음.근데 조회땜에 있어야 하나? 우선 보류

    @Column(name = "question_content")
    private String questionContent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type")
    private QuestionBoard.QuestionType type;

    @Column(name = "good_count")
    private int goodCount;

    @Column(name = "user_writer_id")
    private String userId;
    @Transient
    private String nickname;

    public void addUserNicname(String nickname) {
        this.nickname = nickname;
    }

    @PrePersist // 데이터 생성이 이루어질때 사전 작업
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @ElementCollection
    private List<String> likedUserIds = new ArrayList<>();
    public List<String> getLikedUserIds() {
        return likedUserIds;
    }

    public void addLikedUserId(String userId) {
        likedUserIds.add(userId);
    }

    public void questionUpdate(String questionTitle, String questionContent, QuestionType type) {
        this.questionTitle = questionTitle;
        this.questionContent = questionContent;
        this.type = type;
        this.updatedAt = LocalDateTime.now();//객체 불변성이 깨지지않게 이 객체안에서만 변동을 주는것.
    }

    public void addImage(String imageUrl) {
        QuestionImage questionImage = new QuestionImage(imageUrl, this);
        this.questionImageList.add(questionImage);
    }

    public void updateImages(List<String> imageUrls) {
        this.questionImageList.clear();
        for (String imageUrl : imageUrls) {
            addImage(imageUrl);
        }
    }
    public void removeImage(QuestionImage questionImage) {
        this.questionImageList.remove(questionImage);
        questionImage.initialization();
    }
    public void incrementGood() {
        goodCount++;
    }
    public List<QuestionCommend> getCommendList() {
        return this.questionCommendList;
    }
    public void addCommendList(QuestionCommend questionCommend) {
        this.questionCommendList.add(questionCommend);
        questionCommend.initialization(this);
    }
    public void removeCommendList(QuestionCommend questionCommend) {
        this.questionCommendList.remove(questionCommend);
        questionCommend.initialization(null);
    }
}
