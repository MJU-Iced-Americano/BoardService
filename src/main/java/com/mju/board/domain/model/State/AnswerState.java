package com.mju.board.domain.model.State;

public enum AnswerState {
    UNANSWERED("미답변"),
    ANSWERED("답변완료");

    private final String value;

    AnswerState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
