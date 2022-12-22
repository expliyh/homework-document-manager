package top.expli.exceptions;

public class AuthFailed extends KnifeException {
    private String message;

    public AuthFailed() {
        super();
    }

    public AuthFailed(String message) {
        super();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
