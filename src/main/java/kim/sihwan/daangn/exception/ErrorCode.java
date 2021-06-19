package kim.sihwan.daangn.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    NOT_NULL(1, "필수 값이 누락되었습니다."),
    DUPLICATED_USERNAME(2, "이미 존재하는 아이디입니다."),
    DUPLICATED_NICKNAME(3, "이미 존재하는 닉네임입니다."),
    ALREADY_GONE(4,"삭제된 페이지입니다."),
    INVALID_LOGIN_INFO(5, "아이디 혹은 비밀번호를 확인해주세요."),
    INVALID_TOKEN(6, "잘못된 토큰정보입니다. 다시 로그인을 진행해주세요."),
    EXPIRED_TOKEN(7, "만료된 토큰입니다. 다시 로그인을 진행해주세요."),
    NON_LOGIN(8, "로그인이 필요합니다."),
    NOT_ADMIN(9,"관리자만 가능한 서비스입니다."),
    NOT_MINE(10,"작성자만 수정/삭제가 가능합니다."),
    ALREADY_EXIST(11,""),
    OVER_SIZE(12,""),
    OVER_SIZE_TAG(13,"태그는 최대 3개만 설정할 수 있습니다.");

    private final int code;
    private final String description;

    ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

}