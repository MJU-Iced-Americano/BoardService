package com.mju.Board.presentation.dto.faqdto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FAQRegisterDto {

    private String faqTitle;
    private String faqContent;
    private boolean isChecked;


}
