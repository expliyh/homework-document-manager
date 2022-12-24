package top.expli.webapi;

import top.expli.Config;
import top.expli.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class SocketServer extends ServerSocket {
    private ExecutorService pool;

    public SocketServer() throws IOException {
        super(Config.SERVER_PORT);
        pool = Executors.newCachedThreadPool();
        try {
            while (true) {
                Socket socket = accept();
                ClientConnector handler = new ClientConnector(socket);
                pool.execute(handler);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    class ClientConnector extends Thread {
        protected Socket client;
        protected BufferedReader reader;
        private PrintWriter printWriter;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Integer getPermissionLevel() {
            return permissionLevel;
        }

        public void setPermissionLevel(Integer permissionLevel) {
            this.permissionLevel = permissionLevel;
        }

        private String userName;
        private Integer permissionLevel;
        private ScheduledExecutorService heartBeatChecker;
        private long lastHeartBeat;
        private long lastOperate;

        @Override
        public void run() {
            lastOperate = System.currentTimeMillis();
            try {
                client.setSoTimeout(10*1000);
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }
            while (true) {
                System.out.println("New run " + Token.randomString(10));
                try {
                    printWriter.println("Received: " + reader.readLine());
                } catch (IOException e) {
                    break;
                }
                if(this.isInterrupted()){
                    break;
                }
            }
            System.out.println("Closed!");
        }

        public ClientConnector(Socket client) throws IOException {
            this.client = client;
            this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.printWriter = new PrintWriter(client.getOutputStream(), true);
            System.out.println("New client connected!");
        }
    }

    public static void main(String[] args) {
        try (SocketServer server = new SocketServer()) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

