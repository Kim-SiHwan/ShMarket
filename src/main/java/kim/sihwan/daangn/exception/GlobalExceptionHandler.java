package kim.sihwan.daangn.exception;

import kim.sihwan.daangn.dto.exception.ErrorResponseDto;
import kim.sihwan.daangn.exception.customException.ForbiddenAccessException;
import kim.sihwan.daangn.exception.customException.NotMineException;
import kim.sihwan.daangn.exception.customException.UsernameDuplicatedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<ErrorResponseDto> badCredentialsException(BadCredentialsException e){
        log.info("BadCredentialsException"+ e.getMessage());
        return new ResponseEntity<>(errorResponseDto(ErrorCode.INVALID_LOGIN_INFO.getCode(), ErrorCode.INVALID_LOGIN_INFO.getDescription()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameDuplicatedException.class)
    protected ResponseEntity<ErrorResponseDto> usernameDuplicatedException(UsernameDuplicatedException e){
        log.info("UsernameDuplicatedException"+ e.getMessage());
        return new ResponseEntity<>(errorResponseDto(ErrorCode.DUPLICATED_USERNAME.getCode(),ErrorCode.DUPLICATED_USERNAME.getDescription()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    protected ResponseEntity<ErrorResponseDto> forbiddenAccessException(ForbiddenAccessException e){
        log.info("ForbiddenAccessException"+ e.getMessage());
        return new ResponseEntity<>(errorResponseDto(ErrorCode.NOT_ADMIN.getCode(),ErrorCode.NOT_ADMIN.getDescription()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotMineException.class)
    protected ResponseEntity<ErrorResponseDto> notMineException(NotMineException e){
        log.info("NotMineException"+ e.getMessage());
        return new ResponseEntity<>(errorResponseDto(ErrorCode.NOT_MINE.getCode(),ErrorCode.NOT_MINE.getDescription()),HttpStatus.BAD_REQUEST);
    }

    private ErrorResponseDto errorResponseDto(int code, String message){
        return new ErrorResponseDto(false,code,message);

    }
}
