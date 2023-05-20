package com.mju.board.application;

import com.mju.board.domain.model.QuestionBoard;
import com.mju.board.domain.model.QuestionCommend;
import com.mju.board.presentation.dto.qnadto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QuestionBoardService {
    public void registerQnA(QnARegisterDto qnARegisterDto, List<MultipartFile> images);

    public List<QuestionBoard> getQnABoardList();

    public void deleteQnA(Long questionIndex);

    public void updateQnA(Long questionIndex, QnAupdateDto qnAupdateDto, List<MultipartFile> images);

    public QuestionBoard getQnABoardOne(long questionIndex);

    public void goodCheck(Long questionIndex);

//    public void complaintQnA(Long questionIndex, QnAComplaintDto qnAComplaintDto);

    public void registerCommend(Long questionIndex, QnACommendDto qnACommendDto);

    public void deleteCommend(Long commendIndex);

    public void updateCommend(Long commendIndex, QnACommendDto qnACommendDto);

    public List<QuestionCommend> getCommendList(Long questionIndex);

//    public void complaintCommend(Long commendIndex, QnAComplaintDto qnAComplaintDto);

    public List<QuestionBoard> searchQnA(QnASearchDto qnASearchDto);

    public void goodCheckCommend(Long commendIndex);
}
