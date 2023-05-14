package com.mju.board.presentation.dto.faqdto;

import com.mju.board.domain.model.FAQBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FAQUpdateDto {

    private String faqTitle;
    private String faqContent;
    private FAQBoard.FAQType type;
    private boolean isChecked;
}
