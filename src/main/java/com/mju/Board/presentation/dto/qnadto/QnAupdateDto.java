package com.mju.Board.presentation.dto.qnadto;

import com.mju.Board.domain.model.QuestionBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QnAupdateDto {
    private String questionTitle;
    private String questionContent;
    private QuestionBoard.QuestionType type;
}
