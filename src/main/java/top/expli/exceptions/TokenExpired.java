package top.expli.exceptions;

import com.auth0.jwt.exceptions.TokenExpiredException;

import java.time.Instant;

public class TokenExpired extends TokenAuthFailed {
    public TokenExpired(String msg) {
        super(msg);
    }
}
