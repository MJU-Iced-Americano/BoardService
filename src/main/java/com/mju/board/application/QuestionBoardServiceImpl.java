package com.mju.board.application;

import com.mju.board.domain.model.*;
import com.mju.board.domain.model.Exception.*;
import com.mju.board.domain.repository.QuestionBoardRepository;
import com.mju.board.domain.repository.QuestionCommendRepository;
import com.mju.board.domain.service.S3Service;
import com.mju.board.presentation.dto.qnadto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mju.board.domain.model.Exception.ExceptionList.ALREADY_LIKED_USER;

@Service
@RequiredArgsConstructor
public class QuestionBoardServiceImpl implements QuestionBoardService{

    //////////////////////////////<문의게시판>//////////////////////////////

    private final S3Service s3Service;
    private final QuestionBoardRepository questionBoardRepository;
    private final QuestionCommendRepository questionCommendRepository;
    @Override
    @Transactional
    public void registerQnA(String userId, QnARegisterDto qnARegisterDto, List<MultipartFile> images) {
        QuestionBoard questionBoard = QuestionBoard.builder()
                .questionTitle(qnARegisterDto.getQuestionTitle())
                .questionContent(qnARegisterDto.getQuestionContent())
                .type(qnARegisterDto.getType())
                .userId(userId)
                .build();
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                String imageUrl = s3Service.uploadImageToS3Board(image);
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
                        s3Service.deleteImageFromS3Board(imageUrl);
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
                        s3Service.deleteImageFromS3Board(imageUrl);
                    }
                    questionBoard.removeImage(questionImage);
                }
                // 새 이미지 추가 , Url 새로 추가
                List<String> newImageUrls = new ArrayList<>();
                for (MultipartFile image : images) {
                    String imageUrl = s3Service.uploadImageToS3Board(image);
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
    public QuestionBoard getQnABoardOne(Long questionIndex) {
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
    public boolean checkIfAlreadyLikedQnA(Long questionIndex, String userId) {
        Optional<QuestionBoard> optionalQuestionBoard = questionBoardRepository.findById(questionIndex);
        if (optionalQuestionBoard.isPresent()) {
            QuestionBoard questionBoard = optionalQuestionBoard.get();
            List<String> likedUserIds = questionBoard.getLikedUserIds();
            return likedUserIds.contains(userId);
        }else {
            throw new QnABoardNotFindException(ExceptionList.QNABOARD_NOT_EXISTENT);
        }
    }

    @Override
    @Transactional
    public void goodCheck(Long questionIndex, String userId) {
        Optional<QuestionBoard> optionalQuestionBoard = questionBoardRepository.findById(questionIndex);
        if (optionalQuestionBoard.isPresent()) {
            QuestionBoard questionBoard = optionalQuestionBoard.get();
            if (checkIfAlreadyLikedQnA(questionIndex, userId)) {
                throw new AlreadyLikedException(ALREADY_LIKED_USER);
            }
            questionBoard.addLikedUserId(userId);
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


    //////////////////////////////<문의답변게시판>//////////////////////////////
    @Override
    @Transactional
    public void registerCommend(String userId, Long questionIndex, QnACommendDto qnACommendDto) {
        Optional<QuestionBoard> optionalQuestionBoard = questionBoardRepository.findById(questionIndex);
        if (optionalQuestionBoard.isPresent()) {
            QuestionBoard questionBoard = optionalQuestionBoard.get();
            QuestionCommend questionCommend = QuestionCommend.builder()
                    .commendContent(qnACommendDto.getCommendContent())
                    .questionBoard(questionBoard)
                    .userId(userId)
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
    public boolean checkIfAlreadyLikedCommend(Long commendIndex, String userId) {
        Optional<QuestionCommend> optionalQuestionCommend = questionCommendRepository.findById(commendIndex);
        if (optionalQuestionCommend.isPresent()) {
            QuestionCommend questionCommend = optionalQuestionCommend.get();
            List<String> likedUserIds = questionCommend.getLikedUserIds();
            return likedUserIds.contains(userId);
        }else {
            throw new CommentNotFindException(ExceptionList.QNACOMMEND_NOT_EXISTENT);
        }
    }



    @Override
    @Transactional
    public void goodCheckCommend(Long commendIndex, String userId) {
        Optional<QuestionCommend> optionalQuestionCommend = questionCommendRepository.findById(commendIndex);
        if (optionalQuestionCommend.isPresent()) {
            QuestionCommend questionCommend = optionalQuestionCommend.get();
            if (checkIfAlreadyLikedCommend(commendIndex, userId)) {
                throw new AlreadyLikedException(ALREADY_LIKED_USER);
            }
            questionCommend.addLikedUserId(userId);
            questionCommend.incrementGood();
            questionCommendRepository.save(questionCommend);
        }else {
            throw new CommentNotFindException(ExceptionList.QNACOMMEND_NOT_EXISTENT);
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
    /////////////////////////////마이페이지////////////////////////////
    @Override
    @Transactional
    public List<QuestionBoard> getMyQnAList(String userId) {
        List<QuestionBoard> questionWithoutCommendList = getQnABoardList();
        List<QuestionBoard> myQnAList = new ArrayList<>();
        for(QuestionBoard questionBoard : questionWithoutCommendList){
            String boardUserId = questionBoard.getUserId();
            if (userId.equals(boardUserId)) {
                myQnAList.add(questionBoard);
            }
        }
        return myQnAList;
    }
//    private List<QuestionBoard> getMyQnAList(List<QuestionBoard> questionBoardList, String userId) {
//        for (QuestionBoard questionBoard : questionBoardList) {
//            String boardUserId = questionBoard.getUserId();
//            userService.checkUserId(boardUserId);
//            UserInfoDto userInfoDto = userService.getUserInfoDto(userId);
//            String userName = userInfoDto.getUsername();
//            questionBoard.addUserName(userName);
//        }
//        return questionBoardList;
//    }

}
