package com.mju.Board.application;

import com.mju.Board.domain.model.FAQBoard;
import com.mju.Board.presentation.dto.FAQRegisterDto;
import com.mju.Board.domain.model.Result.CommonResult;
import com.mju.Board.presentation.dto.FAQUpdateDto;

import java.util.List;
import java.util.Map;


public interface BoardService {
    public void registerFaqGeneral(FAQRegisterDto faqRegisterDto);
    public List<FAQBoard> getGeneralFAQBoardList();
    public List<FAQBoard> getAduFAQBoardList();

    public void updateFaq(Long faqIndex, FAQUpdateDto faqUpdateDto);

//    public FAQFindByIdDto findById(Long faqIndex);
    public void deleteFaq(Long faqIndex);

    public void registerFaqAdu(FAQRegisterDto faqRegisterDto);

    public void countFaqClick(Long faqIndex);

}
