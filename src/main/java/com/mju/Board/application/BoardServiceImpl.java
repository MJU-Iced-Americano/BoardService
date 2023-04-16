package com.mju.Board.application;

import com.mju.Board.domain.model.Exception.ExceptionList;
import com.mju.Board.domain.model.FAQBoard;
import com.mju.Board.domain.repository.FAQBoardRepository;
import com.mju.Board.domain.model.Exception.FaqBoardNotFindException;
import com.mju.Board.presentation.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final FAQBoardRepository faqBoardRepository;
    @Override
    @Transactional
    public void registerFaqGeneral(FAQRegisterDto faqRegisterDto) {
        try {
        FAQBoard originalFaqBoard = new FAQBoard(faqRegisterDto.getFaqTitle(), faqRegisterDto.getFaqContent(),faqRegisterDto.isChecked());
        FAQBoard newFaqBoard = FAQBoard.builder()
                .faqIndex(originalFaqBoard.getFaqIndex())
                .faqTitle(originalFaqBoard.getFaqTitle())
                .faqContent(originalFaqBoard.getFaqContent())
                .createdAt(originalFaqBoard.getCreatedAt())
                .updatedAt(originalFaqBoard.getUpdatedAt())
                .type(FAQBoard.FAQType.GENERAL_MEMBER)
                .count(originalFaqBoard.getCount())
                .isChecked(originalFaqBoard.isChecked())
                .build();
        faqBoardRepository.save(newFaqBoard);
        }catch (FaqBoardNotFindException e){
            throw new FaqBoardNotFindException(ExceptionList.FAQBOARD_REGISTER_GENERAL_MEMBER);
        }
    }

    @Override
    @Transactional
    public void registerFaqAdu(FAQRegisterDto faqRegisterDto) {
        try {
            FAQBoard originalFaqBoard = new FAQBoard(faqRegisterDto.getFaqTitle(), faqRegisterDto.getFaqContent(), faqRegisterDto.isChecked());
            FAQBoard newFaqBoard = FAQBoard.builder()
                    .faqIndex(originalFaqBoard.getFaqIndex())
                    .faqTitle(originalFaqBoard.getFaqTitle())
                    .faqContent(originalFaqBoard.getFaqContent())
                    .createdAt(originalFaqBoard.getCreatedAt())
                    .updatedAt(originalFaqBoard.getUpdatedAt())
                    .type(FAQBoard.FAQType.EDUCATION)
                    .count(originalFaqBoard.getCount())
                    .isChecked(originalFaqBoard.isChecked())
                    .build();
            faqBoardRepository.save(newFaqBoard);
        }catch (FaqBoardNotFindException e){
            throw new FaqBoardNotFindException(ExceptionList.FAQBOARD_REGISTER_EDUCATION);
        }
    }



    @Override
    @Transactional
    public List<FAQBoard> getGeneralFAQBoardList() {
//        CommonResult commonResult = responseService.getListResult(faqBoardRepository.findByType(FAQBoard.FAQType.GENERAL_MEMBER));
        List<FAQBoard> generalFAQBoardList = faqBoardRepository.findByType(FAQBoard.FAQType.GENERAL_MEMBER);
        if (!generalFAQBoardList.isEmpty()) {
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
        return aduFAQBoardList;
        }else{
            throw new FaqBoardNotFindException(ExceptionList.FAQBOARD_NOT_FINDTYPE_EDUCATION);
        }
    }

    @Override
    @Transactional
    public void updateFaq(Long faqIndex, FAQUpdateDto faqUpdateDto) {
        Optional<FAQBoard> optionalFaqBoard = faqBoardRepository.findById(faqIndex);
//        FAQBoard faqBoard = optionalFaqBoard.orElseThrow(() -> new Exception("FAQBoard not found"));
        if (optionalFaqBoard.isPresent()) {
        FAQBoard faqBoard = optionalFaqBoard.get();
        faqBoard.faqUpdate(faqUpdateDto.getFaqTitle(), faqUpdateDto.getFaqContent(), faqUpdateDto.isChecked());
        faqBoardRepository.save(faqBoard);
        }else{
            throw new FaqBoardNotFindException(ExceptionList.FAQBOARD_NOT_EXISTENT_UPDATE);
        }
    }

    @Override
    @Transactional
    public void deleteFaq(Long faqIndex){
        Optional<FAQBoard> optionalFaqBoard = faqBoardRepository.findById(faqIndex);
        if (optionalFaqBoard.isPresent()) { // 해당 index의 FAQBoard가 존재하는 경우
        faqBoardRepository.deleteById(faqIndex);
        } else { // 해당 index의 FAQBoard가 존재하지 않는 경우
            throw new FaqBoardNotFindException(ExceptionList.FAQBOARD_NOT_EXISTENT_DELETE);
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
            throw new FaqBoardNotFindException(ExceptionList.FAQBOARD_NOT_EXISTENT_COUNT);
        }
    }



//    @Override
//    @Transactional
//    public FAQFindByIdDto findById(Long faqIndex) {
//        FAQBoard faqBoard = faqBoardRepository.findById(faqIndex).orElseThrow(
//                ()-> new IllegalArgumentException("해당 게시글이 없습니다. id" + faqIndex)
//        );
//        return new FAQFindByIdDto(faqBoard);
//    }
}
