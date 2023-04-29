package com.mju.Board.application;

import com.mju.Board.domain.model.FAQBoard;
import com.mju.Board.domain.model.QuestionBoard;
import com.mju.Board.presentation.dto.faqdto.FAQRegisterDto;
import com.mju.Board.presentation.dto.faqdto.FAQSearchDto;
import com.mju.Board.presentation.dto.faqdto.FAQUpdateDto;
import com.mju.Board.presentation.dto.qnadto.QnARegisterDto;
import com.mju.Board.presentation.dto.qnadto.QnAupdateDto;

import java.util.List;


public interface BoardService {
    public void registerFaqGeneral(FAQRegisterDto faqRegisterDto);
    public List<FAQBoard> getFaqTop5();
    public List<FAQBoard> getGeneralFAQBoardList();
    public List<FAQBoard> getAduFAQBoardList();

    public void updateFaq(Long faqIndex, FAQUpdateDto faqUpdateDto);

//    public FAQFindByIdDto findById(Long faqIndex);
    public void deleteFaq(Long faqIndex);

    public void registerFaqAdu(FAQRegisterDto faqRegisterDto);

    public void countFaqClick(Long faqIndex);

    public List<FAQBoard> searchFaq(FAQSearchDto faqSearchDto);

    public void registerQnAGeneral(QnARegisterDto qnARegisterDto);

    public List<QuestionBoard> getQnABoardList();

    public void deleteQnA(Long questionIndex);

    public void updateQnA(Long questionIndex, QnAupdateDto qnAupdateDto);

    public FAQBoard findByFAQId(long faqIndex);
}
