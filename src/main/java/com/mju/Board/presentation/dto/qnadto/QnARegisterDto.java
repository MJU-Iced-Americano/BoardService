package com.mju.Board.presentation.dto.qnadto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QnARegisterDto {
    private String questionTitle;
    private String questionContent;

    public QnARegisterDto() {

    }
}
