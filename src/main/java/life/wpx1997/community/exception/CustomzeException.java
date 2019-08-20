package life.wpx1997.community.exception;

public class CustomzeException extends RuntimeException {
    private String message;

    public CustomzeException(ICustomizeErrorCode errorCode) {
        this.message = errorCode.getMessage();
    }

    public CustomzeException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
