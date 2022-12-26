package top.expli.webapi;

import top.expli.Main;
import top.expli.exceptions.KnifeException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

public class Response implements Serializable {
    public Response(int code,KnifeException exception){
        this.code = code;
        this.exception = exception;
    }
    private int code;
    private KnifeException exception;

    public Response(int code, HashMap<String, String> detail) {
        this.code = code;
        this.detail = detail;
    }

    public Response(int code, byte[] attachment, HashMap<String, String> detail) {
        this.code = code;
        this.attachment = attachment;
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", exception=" + exception +
                ", attachment=" + Arrays.toString(attachment) +
                ", message='" + message + '\'' +
                ", detail=" + detail +
                ", listDetail=" + listDetail +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public KnifeException getException() {
        return exception;
    }

    public void setException(KnifeException exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HashMap<String, String> getDetail() {
        return detail;
    }

    public void setDetail(HashMap<String, String> detail) {
        this.detail = detail;
    }

    public Vector<Vector<String>> getListDetail() {
        return listDetail;
    }

    public void setListDetail(Vector<Vector<String>> listDetail) {
        this.listDetail = listDetail;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    private byte[] attachment;
    private String message;

    public Response(int code, Vector<Vector<String>> listDetail) {
        this.code = code;
        this.listDetail = listDetail;
    }

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private HashMap<String,String> detail;
    private Vector<Vector<String>> listDetail;

    public Response(int code) {
        this.code = code;
    }
}
