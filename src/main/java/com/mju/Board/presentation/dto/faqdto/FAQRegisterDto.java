package com.mju.Board.presentation.dto.faqdto;

import com.mju.Board.domain.model.FAQBoard;
import com.mju.Board.domain.model.QuestionBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FAQRegisterDto {

    private String faqTitle;
    private String faqContent;
    private FAQBoard.FAQType type;
    private boolean isChecked;


}
