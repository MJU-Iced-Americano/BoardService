package com.mju.board.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "complaint")
public class QuestionComplaint {
    public enum ComplaintType {
        HATE_SPEECH, PROFANITY, SPAM, ILLEGAL_CONTENT, ETC;
    }

    @Builder
    public QuestionComplaint(String complaintContent, ComplaintType type, QuestionBoard questionBoard, QuestionCommend questionCommend){
        this.complaintContent= complaintContent;
        this.type = type;
        this.questionBoard = questionBoard;
        this.questionCommend = questionCommend;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long complaintIndex;

    @Column(name = "complaint_content")
    private String complaintContent;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "question_index")
    private QuestionBoard questionBoard;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "commend_index")
    private QuestionCommend questionCommend;

    @Enumerated(EnumType.STRING)
    @Column(name = "complaint_type")
    private ComplaintType type;


//    @JoinColumn(name = "complainer_index")
//    private User user;

}
