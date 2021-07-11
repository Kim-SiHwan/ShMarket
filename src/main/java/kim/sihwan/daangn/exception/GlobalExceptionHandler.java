package kim.sihwan.daangn.exception;

import kim.sihwan.daangn.dto.exception.ErrorResponseDto;
import kim.sihwan.daangn.exception.customException.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ErrorResponseDto> userNotFoundException(UserNotFoundException e){
        log.info("UserNotFoundException \n"+ e.getMessage());
        return new ResponseEntity<>(errorResponseDto(ErrorCode.INVALID_LOGIN_INFO.getCode(), ErrorCode.INVALID_LOGIN_INFO.getDescription()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<ErrorResponseDto> badCredentialsException(BadCredentialsException e){
        log.info("BadCredentialsException"+ e.getMessage());
        return new ResponseEntity<>(errorResponseDto(ErrorCode.INVALID_LOGIN_INFO.getCode(), ErrorCode.INVALID_LOGIN_INFO.getDescription()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameDuplicatedException.class)
    protected ResponseEntity<ErrorResponseDto> usernameDuplicatedException(UsernameDuplicatedException e){
        log.info("UsernameDuplicatedException \n"+ e.getMessage());
        return new ResponseEntity<>(errorResponseDto(ErrorCode.DUPLICATED_USERNAME.getCode(),ErrorCode.DUPLICATED_USERNAME.getDescription()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NicknameDuplicatedException.class)
    protected ResponseEntity<ErrorResponseDto> nicknameDuplicatedException(NicknameDuplicatedException e){
        log.info("NicknameDuplicatedException \n"+ e.getMessage());
        return new ResponseEntity<>(errorResponseDto(ErrorCode.DUPLICATED_NICKNAME.getCode(),ErrorCode.DUPLICATED_USERNAME.getDescription()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    protected ResponseEntity<ErrorResponseDto> forbiddenAccessException(ForbiddenAccessException e){
        log.info("ForbiddenAccessException \n"+ e.getMessage());
        return new ResponseEntity<>(errorResponseDto(ErrorCode.NOT_ADMIN.getCode(),ErrorCode.NOT_ADMIN.getDescription()),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AlreadyGoneException.class)
    protected ResponseEntity<ErrorResponseDto> alreadyGoneException(AlreadyGoneException e){
        log.info("AlreadyGoneException \n"+ e.getMessage());
        return new ResponseEntity<>(errorResponseDto(ErrorCode.ALREADY_GONE.getCode(),ErrorCode.ALREADY_GONE.getDescription()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotMineException.class)
    protected ResponseEntity<ErrorResponseDto> notMineException(NotMineException e){
        log.info("NotMineException \n"+ e.getMessage());
        return new ResponseEntity<>(errorResponseDto(ErrorCode.NOT_MINE.getCode(),ErrorCode.NOT_MINE.getDescription()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseDto> methodArgumentNotValidException(MethodArgumentNotValidException e){
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        log.info("MethodArgumentNotValidException \n"+ e.getMessage());
        return new ResponseEntity<>(errorResponseDto(ErrorCode.NOT_NULL.getCode(), message),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponseDto> bindException(BindException e){
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        log.info("BindException \n"+ e.getMessage());
        return new ResponseEntity<>(errorResponseDto(ErrorCode.NOT_NULL.getCode(), message),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyExistException.class)
    protected ResponseEntity<ErrorResponseDto> alreadyExistKeywordException(AlreadyExistException e){
        log.info("AlreadyExistKeywordException \n"+ e.getMessage());
        String description ="이미 존재하는 값입니다.";
        switch (e.getMessage()){
            case "keyword":
                description = "이미 존재하는 키워드입니다.";
                return new ResponseEntity<>(errorResponseDto(ErrorCode.ALREADY_EXIST.getCode(),description),HttpStatus.BAD_REQUEST);
            case "block":
                description = "이미 차단한 사용자입니다.";
                return new ResponseEntity<>(errorResponseDto(ErrorCode.ALREADY_EXIST.getCode(),description),HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity<>(errorResponseDto(ErrorCode.ALREADY_EXIST.getCode(),description),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OverSizeException.class)
    protected ResponseEntity<ErrorResponseDto> overSizeKeywordException(OverSizeException e){
        log.info("OverSizeKeywordException \n"+ e.getMessage());
        String description ="최대치를 초과하였습니다.";
        switch (e.getMessage()){
            case "keyword":
                description = "키워드는 최대 10개까지 설정할 수 있습니다.";
                return new ResponseEntity<>(errorResponseDto(ErrorCode.OVER_SIZE.getCode(),description),HttpStatus.BAD_REQUEST);
            case "block":
                description = "차단은 최대 5명까지 설정할 수 있습니다.";
                return new ResponseEntity<>(errorResponseDto(ErrorCode.OVER_SIZE.getCode(),description),HttpStatus.BAD_REQUEST);
            case "tag":
                description = "태그는 최대 3개까지 설정할 수 있습니다.";
                return new ResponseEntity<>(errorResponseDto(ErrorCode.OVER_SIZE.getCode(),description),HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity<>(errorResponseDto(ErrorCode.OVER_SIZE.getCode(),description),HttpStatus.BAD_REQUEST);
    }
    private ErrorResponseDto errorResponseDto(int code, String message){
        return new ErrorResponseDto(false,code,message);

    }
}
