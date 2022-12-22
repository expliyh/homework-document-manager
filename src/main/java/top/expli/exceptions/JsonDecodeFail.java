package top.expli.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonDecodeFail extends JsonProcessingException {
    public JsonDecodeFail(String msg) {
        super(msg);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
