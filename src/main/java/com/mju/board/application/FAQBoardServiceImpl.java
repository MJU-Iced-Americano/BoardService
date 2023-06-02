package com.mju.board.application;

import com.mju.board.domain.model.*;
import com.mju.board.domain.model.Exception.ExceptionList;
import com.mju.board.domain.repository.FAQBoardRepository;
import com.mju.board.domain.model.Exception.FaqBoardNotFindException;
import com.mju.board.presentation.dto.faqdto.FAQRegisterDto;
import com.mju.board.presentation.dto.faqdto.FAQSearchDto;
import com.mju.board.presentation.dto.faqdto.FAQUpdateDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class FAQBoardServiceImpl implements FAQBoardService {

    //////////////////////////<FAQ게시판>/////////////////////////////
    private final FAQBoardRepository faqBoardRepository;

    @Override
    @Transactional
    public void registerFaq(FAQRegisterDto faqRegisterDto){
        FAQBoard faqBoard = FAQBoard.builder()
                .faqTitle(faqRegisterDto.getFaqTitle())
                .faqContent(faqRegisterDto.getFaqContent())
                .type(faqRegisterDto.getType())
                .isChecked(faqRegisterDto.isChecked())
                .build();
        faqBoardRepository.save(faqBoard);
    }

    @Override
    @Transactional
    public List<FAQBoard> getFaqTop5() {
        List<FAQBoard> faqTop5List = faqBoardRepository.findTop5ByOrderByCountDesc();
        if (!faqTop5List.isEmpty()) {
            return faqTop5List;
        }else {
            throw new FaqBoardNotFindException(ExceptionList.FAQBOARD_NOT_FIND_TOP5);
        }
    }


    @Override
    @Transactional
    public List<FAQBoard> getGeneralFAQBoardList() {
//        CommonResult commonResult = responseService.getListResult(faqBoardRepository.findByType(FAQBoard.FAQType.GENERAL_MEMBER));
        List<FAQBoard> generalFAQBoardList = faqBoardRepository.findByType(FAQBoard.FAQType.GENERAL_MEMBER);
        if (!generalFAQBoardList.isEmpty()) {
            for (FAQBoard faqBoard : generalFAQBoardList) {
                faqBoard.isRegisterNew();
            }
            return generalFAQBoardList;
        }else {
            throw new FaqBoardNotFindException(ExceptionList.FAQBOARD_NOT_FINDTYPE_GENERAL_MEMBER);
        }
    }
    @Override
    @Transactional
    public List<FAQBoard> getAduFAQBoardList() {
        List<FAQBoard> aduFAQBoardList = faqBoardRepository.findByType(FAQBoard.FAQType.EDUCATION);
        if (!aduFAQBoardList.isEmpty()) {
            for (FAQBoard faqBoard : aduFAQBoardList) {
                faqBoard.isRegisterNew();
            }
            return aduFAQBoardList;
        }else{
            throw new FaqBoardNotFindException(ExceptionList.FAQBOARD_NOT_FINDTYPE_EDUCATION);
        }
    }
    @Override
    @Transactional
    public void updateFaq(Long faqIndex, FAQUpdateDto faqUpdateDto) {
        Optional<FAQBoard> optionalFaqBoard = faqBoardRepository.findById(faqIndex);
        if (optionalFaqBoard.isPresent()) {
        FAQBoard faqBoard = optionalFaqBoard.get();
        faqBoard.faqUpdate(faqUpdateDto.getFaqTitle(), faqUpdateDto.getFaqContent(), faqUpdateDto.isChecked(), faqUpdateDto.getType());
        faqBoardRepository.save(faqBoard);
        }else{
            throw new FaqBoardNotFindException(ExceptionList.FAQBOARD_NOT_EXISTENT);
        }
    }
    @Override
    @Transactional
    public void deleteFaq(Long faqIndex){
        Optional<FAQBoard> optionalFaqBoard = faqBoardRepository.findById(faqIndex);
        if (optionalFaqBoard.isPresent()) { // 해당 index의 FAQBoard가 존재하는 경우
        faqBoardRepository.deleteById(faqIndex);
        } else { // 해당 index의 FAQBoard가 존재하지 않는 경우
            throw new FaqBoardNotFindException(ExceptionList.FAQBOARD_NOT_EXISTENT);
        }
    }
    @Override
    @Transactional
    public void countFaqClick(Long faqIndex) {
        Optional<FAQBoard> optionalFaqBoard = faqBoardRepository.findById(faqIndex);
        if (optionalFaqBoard.isPresent()) {
            FAQBoard faqBoard = optionalFaqBoard.get();
            faqBoard.incrementCount();
            faqBoardRepository.save(faqBoard);
        }else {
            throw new FaqBoardNotFindException(ExceptionList.FAQBOARD_NOT_EXISTENT);
        }
    }
    @Override
    @Transactional
    public List<FAQBoard> searchFaq(FAQSearchDto faqSearchDto) {
        List<FAQBoard> searchFAQBoardList = faqBoardRepository.findByFaqTitleContainingIgnoreCaseOrFaqContentContainingIgnoreCase(faqSearchDto.getKeyword(), faqSearchDto.getKeyword());
        if (!searchFAQBoardList.isEmpty()) {
            return searchFAQBoardList;
        }else{
            throw new FaqBoardNotFindException(ExceptionList.BOARD_NOT_EXISTENT_KEYWORD);
        }
    }

}
