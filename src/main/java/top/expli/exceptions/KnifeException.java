package top.expli.exceptions;

public class KnifeException extends Exception{
    private String message;

    KnifeException() {
        super();
    }

    public KnifeException(String message) {
        super();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
