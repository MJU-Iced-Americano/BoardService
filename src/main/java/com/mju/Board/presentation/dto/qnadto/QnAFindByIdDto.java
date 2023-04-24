package com.mju.Board.presentation.dto.qnadto;

import com.mju.Board.domain.model.FAQBoard;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QnAFindByIdDto {
    private Long faqIndex;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String faqTitle;
    private String faqContent;


    public QnAFindByIdDto(FAQBoard faqBoard) {
        this.faqIndex = faqBoard.getFaqIndex();
        this.faqTitle = faqBoard.getFaqTitle();
        this.faqContent = faqBoard.getFaqContent();
        this.createdAt = faqBoard.getCreatedAt();
        this.updatedAt = faqBoard.getUpdatedAt();
    }
}
