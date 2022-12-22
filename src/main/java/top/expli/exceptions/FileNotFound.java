package top.expli.exceptions;

import java.io.FileNotFoundException;

public class FileNotFound extends KnifeException {
    private String message;

    FileNotFound() {
        super();
    }

    public FileNotFound(String message) {
        super();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
