package com.mju.Board.presentation.dto;

import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FAQUpdateDto {

    private String faqTitle;
    private String faqContent;
    private boolean isChecked;
}
