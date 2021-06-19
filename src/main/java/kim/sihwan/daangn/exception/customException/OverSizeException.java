package kim.sihwan.daangn.exception.customException;

public class OverSizeException extends RuntimeException{
    public OverSizeException(String type){
        super(type);
    }
}
