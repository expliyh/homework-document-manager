package top.expli;

import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.protobuf.MapEntry;
import org.jetbrains.annotations.NotNull;
import top.expli.exceptions.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Token {
    public static final long EXPIRE_TIME = 30 * 60 * 1000;

    private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    public static ConcurrentHashMap<String, Integer> expiredList = new ConcurrentHashMap<>();
    //1:Reload  2:ReLogin

    public static String randomString(int length) {
        char[] homo = "qwertyuiopasdfghjklzxcvbnm1234567890-=`~@#$%^&()_+[];',QWERTYUIOP{}ASDFGHJKLZXCVBNM".toCharArray();
        int str_length = homo.length;
        StringBuffer ret = new StringBuffer();
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < length; i++) {
            ret.append(homo[rand.nextInt(str_length)]);
        }
        return ret.toString();
    }

    public static void forceExpire(String accessToken, int expireLevel) {
        expiredList.put(accessToken, expireLevel);
    }

    public static void startExpireRefresh() {
        scheduledExecutorService.scheduleWithFixedDelay(new CheckExpireThread(), (long) 0, (long) 60 , TimeUnit.SECONDS);
    }

    public static void stopExpireRefresh(){
        scheduledExecutorService.shutdown();
    }

    public static class CheckExpireThread extends Thread {
        @Override
        public void run() {
            for (Map.Entry<String, Integer> i : expiredList.entrySet()) {
                try {
                    if (isExpiredNoForce(i.getKey())) {
                        expiredList.remove(i.getKey());
                    }
                } catch (TokenAuthFailed ex) {
                    expiredList.remove(i.getKey());
                }
            }

        }
    }

    public static boolean isExpiredNoForce(String accessToken) throws TokenAuthFailed {
        try {
            JWT.require(Algorithm.HMAC512(Token.key)).withIssuer(Token.issuer).build().verify(accessToken);
        } catch (TokenExpiredException exception) {
            return true;
        } catch (SignatureVerificationException exception) {
            throw new SignatureVerificationFailed(knives.random());
        } catch (AlgorithmMismatchException | InvalidClaimException e) {
            throw new TokenAuthFailed(knives.random());
        }
        return false;
    }


    public static boolean needReload(String accessToken) throws TokenExpired {
        Integer expireLevel = expiredList.get(accessToken);
        if (expireLevel == null) {
            return false;
        } else if (expireLevel == 1) {
            return true;
        } else if (expireLevel == 2) {
            throw new TokenExpired(knives.random());
        } else {
            throw new RuntimeException();
        }
    }

    private static final String key = Token.randomString(128);
    private static final String issuer = "P•A•I•M•O•N";

    public static String sha256(String input) {
        MessageDigest mgd;
        try {
            mgd = MessageDigest.getInstance("SHA-256");
            mgd.update(input.getBytes(StandardCharsets.UTF_8));
            return byte2Hex(mgd.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static @NotNull String byte2Hex(byte @NotNull [] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        String temp;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuilder.append("0");
            }
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }

    public static String token_refresh(String user_name, String accessToken) throws PleaseUpdate, TokenAuthFailed {
        DecodedJWT oldToken = Token.decode_auth(accessToken);
        Token.needReload(accessToken);
        Date now = new Date();
        now.setTime(now.getTime() + EXPIRE_TIME);
        return JWT.create().withClaim("user_name", user_name).withIssuer(Token.issuer).withExpiresAt(now).sign(Algorithm.HMAC512(Token.key));
    }

    public static String get_username(String access_token) throws TokenExpired, SignatureVerificationFailed, TokenAuthFailed {
        DecodedJWT decodedToken;
        decodedToken = decode_auth(access_token);
        return decodedToken.getClaim("user_name").toString();
    }

    public static String tokenGet(User usr) {

        return JWT.create().withClaim("user_name", usr.getUserName()).withClaim("pl", usr.get_permission_level()).withIssuer(Token.issuer).toString();
    }

    public static int getPermissionLevel(String access_token) throws PleaseUpdate, TokenAuthFailed {
        DecodedJWT decoded = decode_auth(access_token);
        Token.needReload(access_token);
        return decoded.getClaim("pl").asInt();
    }

    public static boolean auth(String access_token) throws TokenExpired, SignatureVerificationFailed, TokenAuthFailed {
        try {
            JWT.require(Algorithm.HMAC512(Token.key)).withIssuer(Token.issuer).build().verify(access_token);
        } catch (TokenExpiredException exception) {
            throw new TokenExpired(knives.random());
        } catch (SignatureVerificationException exception) {
            throw new SignatureVerificationFailed(knives.random());
        } catch (AlgorithmMismatchException | InvalidClaimException e) {
            throw new TokenAuthFailed(knives.random());
        }
        return true;
    }

    public static DecodedJWT decode_auth(String access_token) throws TokenExpired, SignatureVerificationFailed, TokenAuthFailed {
        try {
            return JWT.require(Algorithm.HMAC512(Token.key)).withIssuer(Token.issuer).build().verify(access_token);
        } catch (TokenExpiredException exception) {
            throw new TokenExpired(knives.random());
        } catch (SignatureVerificationException exception) {
            throw new SignatureVerificationFailed(knives.random());
        } catch (AlgorithmMismatchException | InvalidClaimException e) {
            throw new TokenAuthFailed(knives.random());
        }
    }
}
