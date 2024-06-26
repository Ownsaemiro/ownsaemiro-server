package org.dongguk.ownsaemiro.ownsaemiroserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //400
    WRONG_ENTRY_POINT(40000, HttpStatus.BAD_REQUEST, "잘못된 접근입니다"),
    MISSING_REQUEST_PARAMETER(40001, HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 누락되었습니다."),
    INVALID_PARAMETER_FORMAT(40002, HttpStatus.BAD_REQUEST, "요청에 유효하지 않은 인자 형식입니다."),
    BAD_REQUEST_JSON(40003, HttpStatus.BAD_REQUEST, "잘못된 JSON 형식입니다."),
    INVALID_IMAGE(40004, HttpStatus.BAD_REQUEST, "유효하지 않은 사진입니다."),
    INVALID_IMAGE_EXTENSION(40005, HttpStatus.BAD_REQUEST, "유효하지 않은 이미지 확장자 입니다."),
    INVALID_USER(40006, HttpStatus.BAD_REQUEST, "신청자가 아닙니다."),
    ALREADY_SAME_BAN_INFO(40007, HttpStatus.BAD_REQUEST, "이미 요청 상태와 동일합니다."),
    ALREADY_LIKED_EVENT(40008, HttpStatus.BAD_REQUEST, "이미 좋아요한 행사입니다."),
    SOLDOUT_EVENT(40009, HttpStatus.BAD_REQUEST, "매진된 행사입니다."),
    NOT_ENOUGH_POINT(40010, HttpStatus.BAD_REQUEST, "포인트가 부족합니다."),
    INVALID_ASSIGN_TICKET(40011, HttpStatus.BAD_REQUEST, "양도 티켓이 아닙니다"),
    INVALID_TICKET_OWNER(40012, HttpStatus.BAD_REQUEST, "일치하지 않는 사용자입니디. 입장이 불가능합니다."),
    INVALID_APPLY_ASSIGN_TWICE(40013, HttpStatus.BAD_REQUEST, "같은 티켓에 두번 이상 신청할 수 없습니다."),
    ALREADY_USED_TICKET(40014, HttpStatus.BAD_REQUEST, "이미 사용한 티켓입니다."),
    INVALID_TICKET(40015, HttpStatus.BAD_REQUEST, "티켓 사용 날짜가 아닙니다."),

    //401
    INVALID_HEADER_VALUE(40100, HttpStatus.UNAUTHORIZED, "올바르지 않은 헤더값입니다."),

    EXPIRED_TOKEN_ERROR(40101, HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_TOKEN_ERROR(40102, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    TOKEN_MALFORMED_ERROR(40103, HttpStatus.UNAUTHORIZED, "토큰이 올바르지 않습니다."),
    TOKEN_TYPE_ERROR(40104, HttpStatus.UNAUTHORIZED, "토큰 타입이 일치하지 않거나 비어있습니다."),
    TOKEN_UNSUPPORTED_ERROR(40105, HttpStatus.UNAUTHORIZED, "지원하지않는 토큰입니다."),
    TOKEN_GENERATION_ERROR(40106, HttpStatus.UNAUTHORIZED, "토큰 생성에 실패하였습니다."),
    TOKEN_UNKNOWN_ERROR(40107, HttpStatus.UNAUTHORIZED, "알 수 없는 토큰입니다."),
    LOGIN_FAILURE(40108, HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다"),

    //402

    //403
    FORBIDDEN_ROLE(40300, HttpStatus.FORBIDDEN, "권한이 존재하지 않습니다."),

    //404
    NOT_FOUND_USER(40400, HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    NOT_FOUND_LOGIN_USER(40401, HttpStatus.NOT_FOUND, "로그인한 사용자를 찾을 수 없습니다"),
    NOT_FOUND_IMAGE(40402, HttpStatus.NOT_FOUND, "존재하지 않는 이미지입니다."),
    NOT_FOUND_EVENT(40403, HttpStatus.NOT_FOUND, "존재하지 않는 행사입니다."),
    NOT_FOUND_EVENT_REQUEST(40403, HttpStatus.NOT_FOUND, "존재하지 않는 행사 요청입니다."),
    NOT_FOUND_LIKED(40404, HttpStatus.NOT_FOUND, "존재하지 않는 좋아요입니다."),
    NOT_FOUND_WALLET(40405, HttpStatus.NOT_FOUND, "존재하지 않는 사용자 지갑입니다."),
    NOT_FOUND_TICKET(40406, HttpStatus.NOT_FOUND, "존재하지 않는 티켓입니다."),
    NOT_FOUND_USER_TICKET(40407, HttpStatus.NOT_FOUND, "존재하지 않는 사용자 티켓입니다."),
    NOT_FOUND_ASSIGN_TICKET(40408, HttpStatus.NOT_FOUND, "존재하지 않는 양도 티켓입니다."),
    NOT_FOUND_NOTIFICATION(40409, HttpStatus.NOT_FOUND, "존재하지 않는 알림 입니다."),

    //500
    INTERNAL_SERVER_ERROR(50000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다"),
    INTERNAL_DATA_ERROR(50001, HttpStatus.INTERNAL_SERVER_ERROR, "데이터 처리 중 오류가 발생하였습니다."),
    EXTERNAL_SERVER_ERROR(50002, HttpStatus.INTERNAL_SERVER_ERROR, "외부 서버 오류입니다"),
    FIREBASE_JSON_EMPTY(50003, HttpStatus.INTERNAL_SERVER_ERROR, "Firebase JSON이 필요합니다."),

    FCM_FAIL(50004, HttpStatus.INTERNAL_SERVER_ERROR, "FCM 전송에 실패했습니다."),

    ;
    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}
