package top.expli.exceptions;

public class UserNotFound extends KnifeException{
    private String message;

    public UserNotFound() {
        super();
    }

    public UserNotFound(String message) {
        super();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
