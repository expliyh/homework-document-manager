package top.expli.webapi;

import top.expli.Main;
import top.expli.exceptions.KnifeException;

import java.util.Map;
import java.util.Vector;

public class Response {
    public Response(int code,KnifeException exception){
        this.code = code;
        this.exception = exception;
    }
    public int code;
    public KnifeException exception;
    String message;

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    Map<String,String> detail;
    Vector<Vector<String>> listDetail;

    public Response(int code) {
        this.code = code;
    }
}
