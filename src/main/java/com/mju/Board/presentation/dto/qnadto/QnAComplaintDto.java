package com.mju.Board.presentation.dto.qnadto;

import com.mju.Board.domain.model.QuestionBoard;
import com.mju.Board.domain.model.QuestionComplaint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QnAComplaintDto {
    private String complaintContent;
    private Long complainerIndex;
    private QuestionComplaint.ComplaintType type;
}
