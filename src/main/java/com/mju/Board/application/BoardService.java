package com.mju.Board.application;

import com.mju.Board.domain.model.FAQBoard;
import com.mju.Board.presentation.dto.FAQRegisterDto;
import com.mju.Board.domain.model.Result.CommonResult;
import com.mju.Board.presentation.dto.FAQUpdateDto;

import java.util.List;
import java.util.Map;


public interface BoardService {
    public CommonResult registerFaqGeneral(FAQRegisterDto faqRegisterDto);
    public List<FAQBoard> getGeneralFAQBoardList();
    public List<FAQBoard> getAduFAQBoardList();

    public CommonResult updateFaq(Long faqIndex, FAQUpdateDto faqUpdateDto);

//    public FAQFindByIdDto findById(Long faqIndex);
    public CommonResult deleteFaq(Long faqIndex);

    CommonResult registerFaqAdu(FAQRegisterDto faqRegisterDto);

    CommonResult countFaqClick(Long faqId);

}
