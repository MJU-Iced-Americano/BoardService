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
    private final ResponseService responseService;

    @Override
    @Transactional
    public CommonResult registerFaqGeneral(FAQRegisterDto faqRegisterDto) {
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
        return responseService.getSuccessfulResult();
    }

    @Override
    @Transactional
    public CommonResult registerFaqAdu(FAQRegisterDto faqRegisterDto) {
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
        return responseService.getSuccessfulResult();
    }



    @Override
    @Transactional
    public List<FAQBoard> getGeneralFAQBoardList() {
        return faqBoardRepository.findByType(FAQBoard.FAQType.GENERAL_MEMBER);
    }

    @Override
    @Transactional
    public List<FAQBoard> getAduFAQBoardList() {
        return faqBoardRepository.findByType(FAQBoard.FAQType.EDUCATION);
    }

    @Override
    @Transactional
    public CommonResult updateFaq(Long faqIndex, FAQUpdateDto faqUpdateDto) {
        Optional<FAQBoard> optionalFaqBoard = faqBoardRepository.findById(faqIndex);
        if (optionalFaqBoard.isPresent()) {
            FAQBoard faqBoard = optionalFaqBoard.get();
            faqBoard.faqUpdate(faqUpdateDto.getFaqTitle(), faqUpdateDto.getFaqContent(), faqUpdateDto.isChecked());
            faqBoardRepository.save(faqBoard);
            return responseService.getSuccessfulResult();
        } else {
            CommonResult failResult = responseService.getFailResult(NOT_EXISTENT_FAQBOARD.getCode(), NOT_EXISTENT_FAQBOARD.getMessage());
            return failResult;
        }
    }

    @Override
    @Transactional
    public CommonResult deleteFaq(Long faqIndex) {
        Optional<FAQBoard> optionalFaqBoard = faqBoardRepository.findById(faqIndex);
        if (optionalFaqBoard.isPresent()) { // 해당 index의 FAQBoard가 존재하는 경우
            faqBoardRepository.deleteById(faqIndex);
            return responseService.getSuccessfulResult();
        } else { // 해당 index의 FAQBoard가 존재하지 않는 경우
            return responseService.getFailResult(NOT_EXISTENT_FAQBOARD.getCode(), NOT_EXISTENT_FAQBOARD.getMessage());
        }
    }
    @Override
    @Transactional
    public CommonResult countFaqClick(Long faqId) {
        Optional<FAQBoard> optionalFaqBoard = faqBoardRepository.findById(faqId);
        if (optionalFaqBoard.isPresent()) {
            FAQBoard faqBoardtrans = optionalFaqBoard.get();
            FAQBoard faqBoard = faqBoardtrans.builder()
                    .count(optionalFaqBoard.get().getCount() + 1)
                    .build();
            faqBoardRepository.save(faqBoard);
            return responseService.getSuccessfulResult();
        } else {
            return responseService.getFailResult(NOT_EXISTENT_FAQBOARD.getCode(), NOT_EXISTENT_FAQBOARD.getMessage());
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
