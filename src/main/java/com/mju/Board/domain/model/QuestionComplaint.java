package com.mju.Board.domain.model;

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
    public QuestionComplaint(String complaintContent, ComplaintType type, QuestionBoard questionBoard){
        this.complaintContent= complaintContent;
        this.type = type;
        this.questionBoard = questionBoard;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long complaintIndex;

    @Column(name = "complaint_content")
    private String complaintContent;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "question_index")
    private QuestionBoard questionBoard;

    @Enumerated(EnumType.STRING)
    @Column(name = "complaint_type")
    private ComplaintType type;


//    @JoinColumn(name = "complainer_index")
//    private User user;

}
