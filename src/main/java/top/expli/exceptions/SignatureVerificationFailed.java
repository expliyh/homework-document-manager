package top.expli.exceptions;

public class SignatureVerificationFailed extends TokenAuthFailed{

    public SignatureVerificationFailed(String msg) {
        super(msg);
    }
}
