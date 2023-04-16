package com.mju.Board.application;

import com.mju.Board.domain.model.Exception.ExceptionList;
import com.mju.Board.domain.model.FAQBoard;
import com.mju.Board.domain.model.Result.CommonResult;
import com.mju.Board.domain.repository.FAQBoardRepository;
import com.mju.Board.domain.service.ResponseService;
import com.mju.Board.presentation.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.mju.Board.domain.model.Exception.ExceptionList.NOT_EXISTENT_FAQBOARD;


@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final FAQBoardRepository faqBoardRepository;
    @Override
    @Transactional
    public void registerFaqGeneral(FAQRegisterDto faqRegisterDto) {
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
    }

    @Override
    @Transactional
    public void registerFaqAdu(FAQRegisterDto faqRegisterDto) {
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
    }



    @Override
    @Transactional
    public List<FAQBoard> getGeneralFAQBoardList() {
//        CommonResult commonResult = responseService.getListResult(faqBoardRepository.findByType(FAQBoard.FAQType.GENERAL_MEMBER));
        List<FAQBoard> generalFAQBoardList = faqBoardRepository.findByType(FAQBoard.FAQType.GENERAL_MEMBER);
        return generalFAQBoardList;
    }

    @Override
    @Transactional
    public List<FAQBoard> getAduFAQBoardList() {
        List<FAQBoard> aduFAQBoardList = faqBoardRepository.findByType(FAQBoard.FAQType.EDUCATION);
        return aduFAQBoardList;
    }

    @Override
    @Transactional
    public void updateFaq(Long faqIndex, FAQUpdateDto faqUpdateDto) {
        Optional<FAQBoard> optionalFaqBoard = faqBoardRepository.findById(faqIndex);
//        FAQBoard faqBoard = optionalFaqBoard.orElseThrow(() -> new Exception("FAQBoard not found"));
        FAQBoard faqBoard = optionalFaqBoard.get();
        faqBoard.faqUpdate(faqUpdateDto.getFaqTitle(), faqUpdateDto.getFaqContent(), faqUpdateDto.isChecked());
        faqBoardRepository.save(faqBoard);
    }

    @Override
    @Transactional
    public void deleteFaq(Long faqIndex){
//        Optional<FAQBoard> optionalFaqBoard = faqBoardRepository.findById(faqIndex);
//        if (optionalFaqBoard.isPresent()) { // 해당 index의 FAQBoard가 존재하는 경우
        faqBoardRepository.deleteById(faqIndex);
//        } else { // 해당 index의 FAQBoard가 존재하지 않는 경우
    }
    @Override
    @Transactional
    public void countFaqClick(Long faqIndex) {
        Optional<FAQBoard> optionalFaqBoard = faqBoardRepository.findById(faqIndex);
        if (optionalFaqBoard.isPresent()) {
            FAQBoard faqBoard = optionalFaqBoard.get();
            faqBoard.incrementCount();
            faqBoardRepository.save(faqBoard);
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
