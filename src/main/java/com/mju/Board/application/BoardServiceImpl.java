package com.mju.Board.application;

import com.mju.Board.domain.model.Exception.ExceptionList;
import com.mju.Board.domain.model.Exception.QnABoardNotFindException;
import com.mju.Board.domain.model.FAQBoard;
import com.mju.Board.domain.model.QuestionBoard;
import com.mju.Board.domain.repository.FAQBoardRepository;
import com.mju.Board.domain.model.Exception.FaqBoardNotFindException;
import com.mju.Board.domain.repository.QuestionBoardRepository;
import com.mju.Board.domain.service.S3Service;
import com.mju.Board.presentation.dto.faqdto.FAQRegisterDto;
import com.mju.Board.presentation.dto.faqdto.FAQSearchDto;
import com.mju.Board.presentation.dto.faqdto.FAQUpdateDto;
import com.mju.Board.presentation.dto.qnadto.QnARegisterDto;
import com.mju.Board.presentation.dto.qnadto.QnAupdateDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    //////////////////////////<FAQ게시판>/////////////////////////////
    private final FAQBoardRepository faqBoardRepository;
    private final QuestionBoardRepository questionBoardRepository;
    @Override
    @Transactional
    public void registerFaqGeneral(FAQRegisterDto faqRegisterDto) {
        FAQBoard faqBoard = FAQBoard.builder()
                .faqTitle(faqRegisterDto.getFaqTitle())
                .faqContent(faqRegisterDto.getFaqContent())
                .type(FAQBoard.FAQType.GENERAL_MEMBER)
                .isChecked(faqRegisterDto.isChecked())
                .build();
        faqBoardRepository.save(faqBoard);
        faqBoard.isRegisterNew();
        faqBoardRepository.save(faqBoard);
    }
    @Override
    @Transactional
    public void registerFaqAdu(FAQRegisterDto faqRegisterDto) {
            FAQBoard faqBoard = FAQBoard.builder()
                    .faqTitle(faqRegisterDto.getFaqTitle())
                    .faqContent(faqRegisterDto.getFaqContent())
                    .type(FAQBoard.FAQType.EDUCATION)
                    .isChecked(faqRegisterDto.isChecked())
                    .build();
            faqBoardRepository.save(faqBoard);
            faqBoard.isRegisterNew();
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
    @Override
    @Transactional
    public List<FAQBoard> searchFaq(FAQSearchDto faqSearchDto) {
        List<FAQBoard> searchFAQBoardList = faqBoardRepository.findByFaqTitleContainingIgnoreCaseOrFaqContentContainingIgnoreCase(faqSearchDto.getKeyword(), faqSearchDto.getKeyword());
        if (!searchFAQBoardList.isEmpty()) {
            return searchFAQBoardList;
        }else{
            throw new FaqBoardNotFindException(ExceptionList.FAQBOARD_NOT_EXISTENT_KEYWORD);
        }
    }




    //////////////////////////////<문의게시판>//////////////////////////////

    private final S3Service s3Service;
    @Override
    @Transactional
    public void registerQnA(QnARegisterDto qnARegisterDto, MultipartFile image) {
        String imageUrl = null;
        if (image != null) {
            imageUrl = s3Service.uploadImageToS3(image);
        }
        QuestionBoard questionBoard = QuestionBoard.builder()
                .questionTitle(qnARegisterDto.getQuestionTitle())
                .questionContent(qnARegisterDto.getQuestionContent())
                .imageUrl(imageUrl)
                .build();
        questionBoardRepository.save(questionBoard);
    }

    @Override
    @Transactional
    public List<QuestionBoard> getQnABoardList() {
        List<QuestionBoard> questionBoardList = questionBoardRepository.findAll();
        if (!questionBoardList.isEmpty()) {
            return questionBoardList;
        }else{
            throw new QnABoardNotFindException(ExceptionList.QNABOARD_NOT_EXISTENT_ALL);
        }
    }

    @Override
    @Transactional
    public void deleteQnA(Long questionIndex) {
        Optional<QuestionBoard> optionalQuestionBoard = questionBoardRepository.findById(questionIndex);
        if (optionalQuestionBoard.isPresent()) {
            QuestionBoard questionBoard = optionalQuestionBoard.get();
            String imageUrl = questionBoard.getImageUrl();
            if (imageUrl != null) {
                s3Service.deleteImageFromS3(imageUrl);
            }
            questionBoardRepository.deleteById(questionIndex);
        } else {
            throw new QnABoardNotFindException(ExceptionList.QNABOARD_NOT_EXISTENT_DELETE);
        }
    }


    @Override
    @Transactional
    public void updateQnA(Long questionIndex, QnAupdateDto qnAupdateDto, MultipartFile image) {
        Optional<QuestionBoard> optionalQuestionBoard = questionBoardRepository.findById(questionIndex);
        if (optionalQuestionBoard.isPresent()) {
            QuestionBoard questionBoard = optionalQuestionBoard.get();
            questionBoard.questionUpdate(qnAupdateDto.getQuestionTitle(), qnAupdateDto.getQuestionContent());
            if (image != null) {
                if (questionBoard.getImageUrl() != null) {
                    s3Service.deleteImageFromS3(questionBoard.getImageUrl());
                }
                String imageUrl = s3Service.uploadImageToS3(image);
                questionBoard.updateImageUrl(imageUrl);
            }
            questionBoardRepository.save(questionBoard);
        } else {
            throw new QnABoardNotFindException(ExceptionList.QNABOARD_NOT_EXISTENT_UPDATE);
        }
    }

    @Override
    @Transactional
    public QuestionBoard getQnABoardOne(long questionIndex) {
        Optional<QuestionBoard> questionBoard = questionBoardRepository.findById(questionIndex);
        if (questionBoard.isPresent()) {
            QuestionBoard questionBoardOne = questionBoard.get();
            return questionBoardOne;
        }else{
            throw new FaqBoardNotFindException(ExceptionList.QNABOARD_NOT_FIND_ONE);
        }
    }


}
