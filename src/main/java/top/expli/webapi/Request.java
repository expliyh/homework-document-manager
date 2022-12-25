package top.expli.webapi;

import java.util.Map;

public class Request {
    public int nameSpace;
    public int operation;
    public Map<String, String> detail;
    public byte[] attachment;

    public static class NameSpace {
        public static final int SYSTEM = 0;
        public static final int USERS = 1;
        public static final int DOCUMENTS = 2;
    }

    public static class Operations {
        public static final int LOGIN = 0;
        public static final int LOGOUT = 1;
        public static final int EXIT = -1;
        public static final int GET = 0;
        public static final int ADD = 1;
        public static final int DELETE = 2;
        public static final int LIST = 3;
        public static final int EDIT = 4;
        public static final int DOWNLOAD = 5;
    }

}
