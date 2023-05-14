package com.mju.board.presentation.dto.qnadto;

import com.mju.board.domain.model.QuestionComplaint;
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
