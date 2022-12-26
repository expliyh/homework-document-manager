package top.expli.webapi;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

public class Request implements Serializable {
    public int nameSpace;
    public int operation;
    public HashMap<String, String> detail;

    public Request(int nameSpace, int operation) {
        this.nameSpace = nameSpace;
        this.operation = operation;
    }

    public int getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(int nameSpace) {
        this.nameSpace = nameSpace;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public HashMap<String, String> getDetail() {
        return detail;
    }

    public void setDetail(HashMap<String, String> detail) {
        this.detail = detail;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "Request{" +
                "nameSpace=" + nameSpace +
                ", operation=" + operation +
                ", detail=" + detail +
                ", attachment=" + Arrays.toString(attachment) +
                '}';
    }

    public byte[] attachment;

    public static class NameSpace {
        public static final int SYSTEM = 0;
        public static final int USERS = 1;
        public static final int DOCUMENTS = 2;
    }

    public static class Operations {
        public static final int HEARTBEAT = 6;
        public static final int LOGIN = 0;
        public static final int LOGOUT = 1;
        public static final int EXIT = -1;
        public static final int GET = 0;
        public static final int ADD = 1;
        public static final int DELETE = 2;
        public static final int LIST = 3;
        public static final int EDIT = 4;
        public static final int DOWNLOAD = 5;
        public static final int UPLOAD = 1;
    }

}
