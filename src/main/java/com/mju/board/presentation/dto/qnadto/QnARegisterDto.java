package com.mju.board.presentation.dto.qnadto;

import com.mju.board.domain.model.QuestionBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QnARegisterDto {
    private String questionTitle;
    private String questionContent;
    private QuestionBoard.QuestionType type;
}
