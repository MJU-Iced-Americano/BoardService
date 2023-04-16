package com.mju.Board.domain.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
@Entity
@Table(name = "faqboard")
public class FAQBoard {


    public enum FAQType {
        GENERAL_MEMBER, EDUCATION;
    }
    @Builder
    public FAQBoard(Long faqIndex, LocalDateTime createdAt, LocalDateTime updatedAt, String faqTitle, String faqContent, FAQType type, int count, boolean isChecked){
        this.faqIndex =faqIndex;
        this.createdAt= createdAt;
        this.updatedAt =updatedAt;
        this.faqTitle= faqTitle;
        this.faqContent = faqContent;
        this.type = type;
        this.count = count;
        this.isChecked = isChecked;
    }

    public FAQBoard(String faqTitle, String faqContent, boolean isChecked ) {
        this.faqTitle = faqTitle;
        this.faqContent = faqContent;
        this.isChecked = isChecked;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "faq_index")
    private Long faqIndex;
    @Column(name = "faq_title")
    private String faqTitle;

    @Column(name = "faq_content")
    private String faqContent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "count")
    private int count;

    @Enumerated(EnumType.STRING)
    @Column(name = "faq_type")
    private FAQType type;

    @Column(name = "is_checked")
    private boolean isChecked;


    @PrePersist // 데이터 생성이 이루어질때 사전 작업
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate // 데이터 수정이 이루어질때 사전 작업
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void faqUpdate(String faqTitle, String faqContent, boolean isChecked) {
        this.faqTitle = faqTitle;
        this.faqContent = faqContent;
        this.isChecked = isChecked;
    }

    public void incrementCount() {
        this.count++;
    }
}
