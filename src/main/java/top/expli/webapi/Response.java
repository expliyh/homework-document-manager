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

    public Response(int code, byte[] attachment, Map<String, String> detail) {
        this.code = code;
        this.attachment = attachment;
        this.detail = detail;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    private byte[] attachment;
    public String message;

    public Response(int code, Vector<Vector<String>> listDetail) {
        this.code = code;
        this.listDetail = listDetail;
    }

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
