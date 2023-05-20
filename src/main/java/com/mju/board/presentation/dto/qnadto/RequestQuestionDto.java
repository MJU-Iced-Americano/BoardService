package com.mju.board.presentation.dto.qnadto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RequestQuestionDto implements Serializable {

    private Long questionIndex;
    private String userIndex;

}
