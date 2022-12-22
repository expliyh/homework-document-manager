package top.expli.exceptions;

public class UserExists extends Exception{
    private String message;

    UserExists() {
        super();
    }

    public UserExists(String message) {
        super();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
