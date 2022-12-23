package top.expli.exceptions;

public class TokenAuthFailed extends Exception{
    public TokenAuthFailed(String msg) {
        super(msg);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
