package kim.sihwan.daangn.exception.customException;

public class AlreadyExistException extends RuntimeException{
    public AlreadyExistException(String type){
        super(type);
    }
}
