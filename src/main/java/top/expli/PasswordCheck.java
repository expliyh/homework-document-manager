package top.expli;

import com.mysql.cj.xdevapi.PreparableStatement;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class PasswordCheck {
    public static final int CHECK_PASSED = 0;
    public static final int PASSWORD_AND_CONFIRM_NOT_MATCH = 1;
    public static final int PASSWORD_TOO_SHORT = 2;
    public static final int PASSWORD_TOO_LONG = 3;
    public static final int PASSWORD_TOO_SIMPLE = 4;
    public static final int ILLEGAL_CHARACTER = 5;
    public static final int EMPTY_PASSWORD = 6;

    public static int check(String password, String confirm) {
        int ps1 = PasswordCheck.check(password);
        if (ps1 == PasswordCheck.CHECK_PASSED) {
            if (password.equals(confirm)) {
                return PasswordCheck.CHECK_PASSED;
            } else {
                return PasswordCheck.PASSWORD_AND_CONFIRM_NOT_MATCH;
            }
        } else {
            return ps1;
        }
    }

    public static int check(String password) {
        if (password.isEmpty()) {
            return PasswordCheck.EMPTY_PASSWORD;
        } else if (password.length() > 32) {
            return PasswordCheck.PASSWORD_TOO_LONG;
        } else if (password.length() < 8) {
            return PasswordCheck.PASSWORD_TOO_SHORT;
        }
        boolean UC = false, LC = false, SP = false, NUM = false;
        char[] CA = password.toCharArray();
        for (int i = 0; i < password.length(); i++) {
            if (CA[i] >= 33 && CA[i] < 48) {
                SP = true;
            } else if (CA[i] >= 48 && CA[i] < 58) {
                NUM = true;
            } else if (CA[i] >= 58 && CA[i] < 65) {
                SP = true;
            } else if (CA[i] >= 65 && CA[i] < 91) {
                UC = true;
            } else if (CA[i] >= 91 && CA[i] < 97) {
                SP = true;
            } else if (CA[i] >= 97 && CA[i] < 123) {
                LC = true;
            } else if (CA[i] >= 123 && CA[i] < 127) {
                SP = true;
            } else {
                return PasswordCheck.ILLEGAL_CHARACTER;
            }
            if ((UC && LC && SP) || (UC && LC && NUM) || (UC && SP && NUM) || (LC && SP && NUM)) {
                return PasswordCheck.CHECK_PASSED;
            }
        }
        return PasswordCheck.PASSWORD_TOO_SIMPLE;
    }
}
