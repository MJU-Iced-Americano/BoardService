package com.mju.board.application;

import com.mju.board.domain.model.*;
import com.mju.board.domain.model.Exception.CommentNotFindException;
import com.mju.board.domain.model.Exception.ExceptionList;
import com.mju.board.domain.model.Exception.FaqBoardNotFindException;
import com.mju.board.domain.model.Exception.QnABoardNotFindException;
import com.mju.board.domain.repository.QuestionBoardRepository;
import com.mju.board.domain.repository.QuestionCommendRepository;
import com.mju.board.domain.repository.QuestionComplaintRepository;
import com.mju.board.domain.service.S3Service;
import com.mju.board.presentation.dto.qnadto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class QuestionBoardServiceImpl implements QuestionBoardService{

    //////////////////////////////<문의게시판>//////////////////////////////

    private final S3Service s3Service;
    private final QuestionBoardRepository questionBoardRepository;
    private final QuestionComplaintRepository questionComplaintRepository;
    private final QuestionCommendRepository questionCommendRepository;

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
    public List<QuestionBoard> getQnABoardList(){
        List<QuestionBoard> questionBoardList = questionBoardRepository.findAllByOrderByCreatedAtDesc();
        if (!questionBoardList.isEmpty()) {
            List<QuestionBoard> questionWithoutCommendList = new ArrayList<>();
            for (QuestionBoard questionBoard : questionBoardList) {
                QuestionBoard modifiedBoard = new QuestionBoard(questionBoard); // 객체 복사해서 답변 리스트 없앰
                questionWithoutCommendList.add(modifiedBoard);
            }
            return questionWithoutCommendList;
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
            List<QuestionImage> questionImages = questionBoard.getQuestionImageList();
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
            throw new QnABoardNotFindException(ExceptionList.QNABOARD_NOT_EXISTENT);
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
                List<QuestionImage> originalQuestionImages = questionBoard.getQuestionImageList();
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
            throw new QnABoardNotFindException(ExceptionList.QNABOARD_NOT_EXISTENT);
        }
    }

    @Override
    @Transactional
    public QuestionBoard getQnABoardOne(long questionIndex) {
        Optional<QuestionBoard> questionBoard = questionBoardRepository.findById(questionIndex);
        if (questionBoard.isPresent()) {
            QuestionBoard questionBoardOne = questionBoard.get();
            QuestionBoard questionWithoutCommendOne = new QuestionBoard(questionBoardOne); // 객체 복사해서 답변 리스트 없앰
            return questionWithoutCommendOne;
        }else{
            throw new QnABoardNotFindException(ExceptionList.QNABOARD_NOT_EXISTENT);
        }
    }

    @Override
    @Transactional
    public void goodCheck(Long questionIndex) {
        Optional<QuestionBoard> optionalQuestionBoard = questionBoardRepository.findById(questionIndex);
        if (optionalQuestionBoard.isPresent()) {
            QuestionBoard questionBoard = optionalQuestionBoard.get();
            questionBoard.incrementGood();
            questionBoardRepository.save(questionBoard);
        }else {
            throw new QnABoardNotFindException(ExceptionList.QNABOARD_NOT_EXISTENT);
        }

    }

    @Override
    @Transactional
    public List<QuestionBoard> searchQnA(QnASearchDto qnASearchDto) {
        List<QuestionBoard> searchQuestionList = questionBoardRepository.findByQuestionTitleContainingIgnoreCaseOrQuestionContentContainingIgnoreCase(qnASearchDto.getKeyword(), qnASearchDto.getKeyword());
        if (!searchQuestionList.isEmpty()) {
            return searchQuestionList;
        }else{
            throw new FaqBoardNotFindException(ExceptionList.BOARD_NOT_EXISTENT_KEYWORD);
        }
    }



    //    @Override
//    @Transactional
//    public void complaintQnA(Long questionIndex, QnAComplaintDto qnAComplaintDto) {
//        Optional<QuestionBoard> optionalQuestionBoard = questionBoardRepository.findById(questionIndex);
//        if (optionalQuestionBoard.isPresent()) {
//            QuestionBoard questionBoard = optionalQuestionBoard.get();
//
//            QuestionComplaint questionComplaint = QuestionComplaint.builder()
//                    .complaintContent(qnAComplaintDto.getComplaintContent())
//                    .type(qnAComplaintDto.getType())
//                    .questionBoard(questionBoard)
//                    .build();
//            questionComplaintRepository.save(questionComplaint);
//        }else{
//            throw new QnABoardNotFindException(ExceptionList.QNABOARD_NOT_EXISTENT);
//        }
//    }
    //////////////////////////////<문의답변게시판>//////////////////////////////
    @Override
    @Transactional
    public void registerCommend(Long questionIndex, QnACommendDto qnACommendDto) {
        Optional<QuestionBoard> optionalQuestionBoard = questionBoardRepository.findById(questionIndex);
        if (optionalQuestionBoard.isPresent()) {
            QuestionBoard questionBoard = optionalQuestionBoard.get();
            QuestionCommend questionCommend = QuestionCommend.builder()
                    .commendContent(qnACommendDto.getCommendContent())
                    .questionBoard(questionBoard)
                    .build();
            questionCommendRepository.save(questionCommend);
            questionBoard.addCommendList(questionCommend);
        }else{
            throw new QnABoardNotFindException(ExceptionList.QNABOARD_NOT_EXISTENT);
        }

    }

    @Override
    @Transactional
    public void deleteCommend(Long commendIndex) {
        Optional<QuestionCommend> optionalQuestionCommend = questionCommendRepository.findById(commendIndex);
        if (optionalQuestionCommend.isPresent()) {
            QuestionCommend questionCommend = optionalQuestionCommend.get();
            QuestionBoard questionBoard = questionCommend.getQuestionBoard();
            questionCommendRepository.deleteById(commendIndex);
            questionBoard.removeCommendList(questionCommend);
        } else {
            throw new CommentNotFindException(ExceptionList.QNACOMMEND_NOT_EXISTENT);
        }
    }

    @Override
    @Transactional
    public void updateCommend(Long commendIndex, QnACommendDto qnACommendDto) {
        Optional<QuestionCommend> optionalQuestionCommend = questionCommendRepository.findById(commendIndex);
        if (optionalQuestionCommend.isPresent()) {
            QuestionCommend questionCommend = optionalQuestionCommend.get();
            questionCommend.commendUpdate(qnACommendDto.getCommendContent());
            QuestionBoard questionBoard = questionCommend.getQuestionBoard();
            questionCommendRepository.save(questionCommend);
            questionBoard.addCommendList(questionCommend);
        }else{
            throw new CommentNotFindException(ExceptionList.QNACOMMEND_NOT_EXISTENT);
        }
    }

    @Override
    @Transactional
    public List<QuestionCommend> getCommendList(Long questionIndex) {
        Optional<QuestionBoard> optionalQuestionBoard = questionBoardRepository.findById(questionIndex);
        if (optionalQuestionBoard.isPresent()) {
            QuestionBoard questionBoard = optionalQuestionBoard.get();
            return questionBoard.getCommendList();
        } else {
            throw new QnABoardNotFindException(ExceptionList.QNABOARD_NOT_EXISTENT);
        }
    }

    @Override
    @Transactional
    public void goodCheckCommend(Long commendIndex) {
        Optional<QuestionCommend> optionalQuestionCommend = questionCommendRepository.findById(commendIndex);
        if (optionalQuestionCommend.isPresent()) {
            QuestionCommend questionCommend = optionalQuestionCommend.get();
            questionCommend.incrementGood();
            questionCommendRepository.save(questionCommend);
        }else {
            throw new QnABoardNotFindException(ExceptionList.QNABOARD_NOT_EXISTENT);
        }
    }

    @Override
    @Transactional
    public QuestionCommend getQnACommendOne(Long commendIndex) {
        Optional<QuestionCommend> optionalQuestionCommend = questionCommendRepository.findById(commendIndex);
        if (optionalQuestionCommend.isPresent()) {
            QuestionCommend questionCommend = optionalQuestionCommend.get();
            QuestionBoard questionBoard = questionCommend.getQuestionBoard();
            Long questionIndex = questionBoard.getQuestionIndex();
            questionCommend.initQuestionIndex(questionIndex);

            return questionCommend;
        }else{
            throw new CommentNotFindException(ExceptionList.QNACOMMEND_NOT_EXISTENT);
        }
    }

//    @Override
//    @Transactional
//    public void complaintCommend(Long commendIndex, QnAComplaintDto qnAComplaintDto) {
//        Optional<QuestionCommend> optionalQuestionCommend = questionCommendRepository.findById(commendIndex);
//        if (optionalQuestionCommend.isPresent()) {
//            QuestionCommend questionCommend = optionalQuestionCommend.get();
//            QuestionBoard questionBoard = questionCommend.getQuestionBoard();
//            QuestionComplaint questionComplaint = QuestionComplaint.builder()
//                    .complaintContent(qnAComplaintDto.getComplaintContent())
//                    .type(qnAComplaintDto.getType())
//                    .questionBoard(questionBoard)
//                    .questionCommend(questionCommend)
//                    .build();
//            questionComplaintRepository.save(questionComplaint);
//        }else{
//            throw new CommentNotFindException(ExceptionList.QNACOMMEND_NOT_EXISTENT);
//        }
//    }


}
