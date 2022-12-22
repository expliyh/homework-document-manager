package top.expli.exceptions;

public class TokenAuthFailed extends SecurityException{
    public TokenAuthFailed(String msg) {
        super(msg);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
