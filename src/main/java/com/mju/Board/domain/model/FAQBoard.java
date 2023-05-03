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
    public FAQBoard(String faqTitle, String faqContent, FAQType type, boolean isChecked){
        this.faqTitle= faqTitle;
        this.faqContent = faqContent;
        this.type = type;
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
    @Transient
    private boolean isNew;


    @PrePersist // 데이터 생성이 이루어질때 사전 작업
    public void prePersist() {
//        this.createdAt = LocalDateTime.of(2023, 4, 27, 0, 0, 0);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        this.isNew = this.createdAt.isAfter(LocalDateTime.now().minusDays(7));
    }

    public void faqUpdate(String faqTitle, String faqContent, boolean isChecked) {
        this.faqTitle = faqTitle;
        this.faqContent = faqContent;
        this.isChecked = isChecked;
        this.updatedAt = LocalDateTime.now();//객체 불변성이 깨지지않게 이 객체안에서만 변동을 주는것.
    }

    public void incrementCount() {
        count ++;
    }
    public void isRegisterNew() {
        this.isNew = this.createdAt.isAfter(LocalDateTime.now().minusDays(7));
    }


}
