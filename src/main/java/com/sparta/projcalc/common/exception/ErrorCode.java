package com.sparta.projcalc.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 회원 관련 오류 코드
    DUPLICATED_USERID(HttpStatus.BAD_REQUEST, "USER_001", "사용 중인 아이디입니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "USER_002", "사용 중인 이메일입니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "USER_003", "사용 중인 닉네임입니다."),
    DUPLICATED_PHONENUMBER(HttpStatus.BAD_REQUEST, "USER_004", "사용 중인 전화번호입니다."),
    DUPLICATED_PASSWORD(HttpStatus.BAD_REQUEST, "USER_005", "비밀번호를 다시 작성해주세요."),
    NOT_VALID_PASSWORD(HttpStatus.BAD_REQUEST, "USER_006", "비밀번호를 다시 확인해주세요."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "USER_007", "찾을 수 없는 회원입니다."),
    MISMATCH_REFRESH_TOKEN(HttpStatus.NOT_ACCEPTABLE, "USER_008", "Refresh token mismatch."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_009", "찾을 수 없는 회원입니다."),
    FAILED_LOGIN(HttpStatus.UNAUTHORIZED, "USER_010", "로그인에 실패하였습니다."),
    FORBIDDEN_TO_ACCESS(HttpStatus.FORBIDDEN, "USER_011", "접근할 권한이 없습니다."),
    UNAUTHORIZED_TO_ACCESS(HttpStatus.UNAUTHORIZED, "USER_012", "로그인 바랍니다."),

    // Access Token 관련 오류 코드
    NOT_FOUND_ACCESS_TOKEN(HttpStatus.NOT_FOUND, "ACCESS_TOKEN_001", "Access token not found."),
    INVALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "ACCESS_TOKEN_002", "Access token is invalid."),
    EXPIRATION_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "ACCESS_TOKEN_003", "Access token has expired."),
    NOT_SUPPORTED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "ACCESS_TOKEN_004", "Access token is not supported."),
    UNKNOWN_ACCESS_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "ACCESS_TOKEN_005", "Access token error."),

    // Refresh Token 관련 오류 코드
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "REFRESH_TOKEN_001", "Refresh Token is invalid."),
    EXPIRATION_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN_002", "Refresh Token has expired."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "REFRESH_TOKEN_003", "Refresh Token not found."),

    // 공연장 관련 오류 코드
    THEATER_NOT_FOUND(HttpStatus.NOT_FOUND, "THEATER_001", "찾을 수 없는 공연장입니다."),
    THEATER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "THEATER_002", "이미 존재하는 공연장입니다."),
    INVALID_THEATER_DATA(HttpStatus.BAD_REQUEST, "THEATER_003", "유효하지 않은 공연장 데이터입니다."),
    THEATER_DETAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "THEATER_004", "공연장 상세 정보를 찾을 수 없습니다."),

    // 지역 관련 오류 코드
    SIDONM_NOT_FOUND(HttpStatus.NOT_FOUND, "REGION_001", "찾을 수 없는 시도입니다."),
    GUGUNNM_NOT_FOUND(HttpStatus.NOT_FOUND, "REGION_002", "찾을 수 없는 구군입니다."),

    // 추가된 오류 코드
    API_CALL_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "API_001", "API 호출 실패."),
    JSON_PROCESSING_ERROR(HttpStatus.BAD_REQUEST, "API_002", "JSON 처리 오류 발생."),
    DATABASE_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DATABASE_001", "데이터베이스 저장 오류 발생."),

    // PFMC 관련 에러 코드
    PFMC_NOT_FOUND(HttpStatus.NOT_FOUND, "PFMC_001", "찾을 수 없는 공연센터입니다."),

    // 추가: 입력값 유효성 오류
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "INPUT_001", "유효하지 않은 입력 값입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}