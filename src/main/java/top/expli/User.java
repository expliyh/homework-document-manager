package top.expli;

import com.auth0.jwt.*;
import top.expli.exceptions.*;

import java.util.Objects;

public class User implements java.io.Serializable {
    public User(String user_name, String refresh_token, int permission_level) {
        this.user_name = user_name;
        this.refresh_token = Token.sha256(refresh_token);
        this.permission_level = permission_level;
        this.uid = 25565;
    }

    public User(String user_name, String passwd) {
        this.uid = 0;
        this.user_name = user_name;
        this.refresh_token = Token.sha256(passwd);
        this.permission_level = 2;

    }

    public void setRefreshToken(String passwd) {
        this.refresh_token = Token.sha256(passwd);
    }

    public int get_permission_level() {
        return permission_level;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String token_request(String refresh_token) throws AuthFailed {
        if (Objects.equals(refresh_token, this.refresh_token)) {
            return Token.tokenGet(this);
        } else {
            System.out.println(refresh_token);
//            System.out.println(Token.sha256("12345678"));
            System.out.println(this.refresh_token);
            throw new AuthFailed(knives.random());
        }
    }

    public boolean auth(String refreshToken) throws AuthFailed {
        if (Objects.equals(this.refresh_token, refreshToken)) {
            return true;
        } else throw new AuthFailed(knives.random());
    }

    public int getPermission_level() {
        return permission_level;
    }

    public void setPermission_level(int permission_level) {
        this.permission_level = permission_level;
    }

    public String getUserName() {
        return user_name;
    }

    private String user_name;
    private String refresh_token;
    private int permission_level;
    private final int uid;

    @Override
    public String toString() {
        return "User{" +
                "user_name='" + user_name + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", permission_level=" + permission_level +
                ", uid=" + uid +
                '}';
    }
}

