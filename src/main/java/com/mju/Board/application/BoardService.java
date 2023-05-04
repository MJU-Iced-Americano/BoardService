package com.mju.Board.application;

import com.mju.Board.domain.model.FAQBoard;
import com.mju.Board.domain.model.QuestionBoard;
import com.mju.Board.presentation.dto.faqdto.FAQRegisterDto;
import com.mju.Board.presentation.dto.faqdto.FAQSearchDto;
import com.mju.Board.presentation.dto.faqdto.FAQUpdateDto;
import com.mju.Board.presentation.dto.qnadto.QnARegisterDto;
import com.mju.Board.presentation.dto.qnadto.QnAupdateDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface BoardService {
    public void registerFaq(FAQRegisterDto faqRegisterDto);
    public List<FAQBoard> getFaqTop5();
    public List<FAQBoard> getGeneralFAQBoardList();
    public List<FAQBoard> getAduFAQBoardList();

    public void updateFaq(Long faqIndex, FAQUpdateDto faqUpdateDto);

    public void deleteFaq(Long faqIndex);

    public void countFaqClick(Long faqIndex);

    public List<FAQBoard> searchFaq(FAQSearchDto faqSearchDto);

    public void registerQnA(QnARegisterDto qnARegisterDto, List<MultipartFile> images);

    public List<QuestionBoard> getQnABoardList();

    public void deleteQnA(Long questionIndex);

    public void updateQnA(Long questionIndex, QnAupdateDto qnAupdateDto, List<MultipartFile> images);

    public QuestionBoard getQnABoardOne(long questionIndex);
}
