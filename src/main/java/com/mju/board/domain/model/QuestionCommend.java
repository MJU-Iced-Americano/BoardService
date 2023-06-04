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
@Table(name = "questioncommend")
public class QuestionCommend {
    @Builder
    public QuestionCommend(String commendContent, QuestionBoard questionBoard, String userId){
        this.commendContent= commendContent;
        this.questionBoard = questionBoard;
        this.userId = userId;
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
    @Column(name = "user_writer_id")
    private String userId;

    @Transient
    private Long questionIndex;

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

    public void initQuestionIndex(Long questionIndex) {
        this.questionIndex = questionIndex;
    }
}
