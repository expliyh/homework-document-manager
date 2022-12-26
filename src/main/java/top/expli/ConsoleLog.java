package top.expli;

import java.text.SimpleDateFormat;

public class ConsoleLog {
    public static void log(String info) {
        String date = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss] ").format(System.currentTimeMillis());
        System.out.println(date + info);
    }
}
