package kim.sihwan.daangn.dto.exception;

import lombok.Getter;

@Getter
public class ErrorResponseDto {
    private boolean success;
    private String message;
    private int code;


    public ErrorResponseDto(boolean success, int code, String message){
        this.success=success;
        this.code=code;
        this.message=message;

    }
}