package com.mju.board.application;

import com.mju.board.domain.model.QuestionBoard;
import com.mju.board.domain.model.QuestionCommend;
import com.mju.board.presentation.dto.qnadto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QuestionBoardService {
    public void registerQnA(String userId, QnARegisterDto qnARegisterDto, List<MultipartFile> images);

    public List<QuestionBoard> getQnABoardList();

    public void deleteQnA(Long questionIndex);

    public void updateQnA(Long questionIndex, QnAupdateDto qnAupdateDto, List<MultipartFile> images);

    public QuestionBoard getQnABoardOne(Long questionIndex);

    public void goodCheck(Long questionIndex, String userId);


    public void registerCommend(String userId, Long questionIndex, QnACommendDto qnACommendDto);

    public void deleteCommend(Long commendIndex);

    public void updateCommend(Long commendIndex, QnACommendDto qnACommendDto);

    public List<QuestionCommend> getCommendList(Long questionIndex);


    public List<QuestionBoard> searchQnA(QnASearchDto qnASearchDto);

    public void goodCheckCommend(Long commendIndex, String userId);

    public QuestionCommend getQnACommendOne(Long commendIndex);

    public boolean checkIfAlreadyLikedQnA(Long questionIndex, String userId);
    public boolean checkIfAlreadyLikedCommend(Long commendIndex, String userId);

    public List<QuestionBoard> getMyQnAList(String userId);
}
