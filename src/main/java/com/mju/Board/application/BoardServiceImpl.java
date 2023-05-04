package com.mju.Board.application;

import com.mju.Board.domain.model.Exception.ExceptionList;
import com.mju.Board.domain.model.Exception.QnABoardNotFindException;
import com.mju.Board.domain.model.FAQBoard;
import com.mju.Board.domain.model.QuestionImage;
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

import java.util.ArrayList;
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
    public void registerFaq(FAQRegisterDto faqRegisterDto) {
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
    public void registerQnA(QnARegisterDto qnARegisterDto, List<MultipartFile> images) {
        QuestionBoard questionBoard = QuestionBoard.builder()
                .questionTitle(qnARegisterDto.getQuestionTitle())
                .questionContent(qnARegisterDto.getQuestionContent())
                .type(qnARegisterDto.getType())
                .build();

        if (!images.isEmpty()) {
            for (MultipartFile image : images) {
                String imageUrl = s3Service.uploadImageToS3(image);
                questionBoard.addImage(imageUrl);
            }
        }
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
            List<QuestionImage> questionImages = questionBoard.getQuestionImages();
            if (!questionImages.isEmpty()) {
                for (QuestionImage questionImage : questionImages) {
                    String imageUrl = questionImage.getImageUrl();
                    if (imageUrl != null) {
                        s3Service.deleteImageFromS3(imageUrl);
                    }
                }
            }
            questionBoardRepository.deleteById(questionIndex);
        } else {
            throw new QnABoardNotFindException(ExceptionList.QNABOARD_NOT_EXISTENT_DELETE);
        }
    }

    @Override
    @Transactional
    public void updateQnA(Long questionIndex, QnAupdateDto qnAupdateDto, List<MultipartFile> images) {
        Optional<QuestionBoard> optionalQuestionBoard = questionBoardRepository.findById(questionIndex);
        if (optionalQuestionBoard.isPresent()) {
            QuestionBoard questionBoard = optionalQuestionBoard.get();
            questionBoard.questionUpdate(qnAupdateDto.getQuestionTitle(), qnAupdateDto.getQuestionContent(), qnAupdateDto.getType());

            if (images != null &&!images.isEmpty()) {
                // 기존 이미지 삭제
                List<QuestionImage> originalQuestionImages = questionBoard.getQuestionImages();
                List<QuestionImage> imagesToDelete = new ArrayList<>(originalQuestionImages);
                for (QuestionImage questionImage : imagesToDelete) {
                    String imageUrl = questionImage.getImageUrl();
                    if (imageUrl != null) {
                        s3Service.deleteImageFromS3(imageUrl);
                    }
                    questionBoard.removeImage(questionImage);
                }
                // 새 이미지 추가 , Url 새로 추가
                List<String> newImageUrls = new ArrayList<>();
                for (MultipartFile image : images) {
                    String imageUrl = s3Service.uploadImageToS3(image);
                    newImageUrls.add(imageUrl);
                }
                questionBoard.updateImages(newImageUrls);//추상화를 위해

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
