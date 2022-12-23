package top.expli.webapi;

import top.expli.Config;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer extends ServerSocket {
    private ExecutorService pool;

    public SocketServer() throws IOException {
        super(Config.SERVER_PORT);
        pool = Executors.newCachedThreadPool();

        try {
            while (true) {
                Socket socket = accept();
                RequestHandler handler = new RequestHandler(socket);
                pool.execute(handler);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    class RequestHandler extends Thread {
        private Socket client;
        private BufferedReader reader;
        private PrintWriter printWriter;
        private String userName;
        private String permissionLevel;

        @Override
        public void run() {

        }

        public RequestHandler(Socket client) throws IOException {
            this.client = client;
            this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.printWriter = new PrintWriter(client.getOutputStream(), true);
            System.out.println("New client connected!");
            start();
        }
    }
}

