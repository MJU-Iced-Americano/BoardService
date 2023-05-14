package com.mju.board.application;

import com.mju.board.domain.model.FAQBoard;
import com.mju.board.presentation.dto.faqdto.FAQRegisterDto;
import com.mju.board.presentation.dto.faqdto.FAQSearchDto;
import com.mju.board.presentation.dto.faqdto.FAQUpdateDto;

import java.util.List;


public interface FAQBoardService {
    public void registerFaq(FAQRegisterDto faqRegisterDto);
    public List<FAQBoard> getFaqTop5();
    public List<FAQBoard> getGeneralFAQBoardList();
    public List<FAQBoard> getAduFAQBoardList();

    public void updateFaq(Long faqIndex, FAQUpdateDto faqUpdateDto);

    public void deleteFaq(Long faqIndex);

    public void countFaqClick(Long faqIndex);

    public List<FAQBoard> searchFaq(FAQSearchDto faqSearchDto);


}
