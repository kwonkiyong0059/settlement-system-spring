package com.sparta.projcalc.common.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjCalcException extends RuntimeException {
    private ErrorCode errorCode;

    // ErrorCode와 메시지를 받는 생성자
    public ProjCalcException(ErrorCode errorCode, String message) {
        super(message); // 부모 클래스(RuntimeException)에 메시지를 전달
        this.errorCode = errorCode;
    }

    // ErrorCode만 받는 생성자
    public ProjCalcException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // 기본 메시지를 부모 클래스에 전달
        this.errorCode = errorCode;
    }
}