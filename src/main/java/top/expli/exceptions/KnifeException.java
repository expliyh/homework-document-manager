package top.expli.exceptions;

import top.expli.knives;

public class KnifeException extends Exception{
    private String message;

    KnifeException() {
        super();
        message = knives.random();
    }

    public KnifeException(String message) {
        super();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
