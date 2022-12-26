package top.expli;

import org.apache.commons.logging.Log;
import top.expli.GUI.ErrorMessage;
import top.expli.GUI.Login;
import top.expli.GUI.SaveAll;
import top.expli.documents.Documents;
import top.expli.exceptions.AuthFailed;
import top.expli.webapi.SocketServer;

import javax.print.Doc;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println(Token.randomString(128));
        System.out.println(knives.random());
        cache_user.readData();
        Documents.readData();
        Runtime.getRuntime().addShutdownHook(new Thread(SaveAll::save));
        SocketServer.main(args);
        Login.main(args);
        cache_user.writeData();
        Documents.writeData();
        System.out.println("Saved!");
    }
}