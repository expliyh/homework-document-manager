package top.expli;

import top.expli.documents.Documents;
import top.expli.exceptions.AuthFailed;
import top.expli.exceptions.UserNotFound;

import javax.print.Doc;
import java.io.IOException;
import java.util.Scanner;

public class Cli {
    public Cli(cache_user cache, Documents documents) throws IOException {
        this.cache = cache;
        this.documents = documents;
        while (true){
            Scanner scan = new Scanner(System.in);
            System.out.println(knives.random());
            System.out.println("---------------------------------------------");
            System.out.println(" [0] 退出系统");
            System.out.println(" [1] 用户登录");
            int sw = scan.nextInt();
            switch (sw){
                case 0:
                    return;
                case 1:
                    try{
                        this.user = login();
                        this.user.menu();
                    }
                    catch (AuthFailed e1){
                        System.out.println("认证失败："+e1.getMessage());
                        System.out.println("请重试");
                        continue;
                    }
                    catch (IOException e2){
                        System.out.println("未知错误，请重试");
                        continue;
                    }
                    finally {
                        System.out.println("---------------------------------------------");
                    }
            }

        }

    }
    protected ClientUser login() throws IOException, AuthFailed {
        while (true){
            Scanner scan = new Scanner(System.in);
            System.out.println(knives.random());
            System.out.println("---------------------------------------------");
            System.out.println(" Username: ");
            String user_name = scan.next();
            System.out.println(" Password: ");
            String passwd = scan.next();
            User user;
            try {
                user = this.cache.login(user_name,passwd);
            }
            catch (UserNotFound ex){
                System.out.println("错误：找不到用户");
                continue;
            }
            catch (AuthFailed ex){
                System.out.println("错误：密码错误");
                continue;
            }
            return switch (user.get_permission_level()) {
                case 0, 1, 2 -> new ClientAdmin(user);
                default -> new ClientAdmin(user);
            };
        }

    }
    private cache_user cache;
    private Documents documents;
    protected ClientUser user;

    abstract class ClientUser {
        public abstract void menu();

        public ClientUser(User user){
            this.user = user;
        }
        private User user;
    }

    class ClientAdmin extends ClientUser {
        public ClientAdmin(User user){
            super(user);
        }

        private void manage_documents(){

        }
        @Override
        public void menu() {
            while (true){
                Scanner scan = new Scanner(System.in);
                System.out.println("---------------------------------------------");
                System.out.println("            档案管理系统管理员控制台");
                System.out.println("---------------------------------------------");
                System.out.println(" [0] logout");
                System.out.println(" [1] Manage Documents");
                System.out.println(" [2] Manage Users");
                System.out.println(" [3] Profile Settings\n");
                switch (scan.nextInt()){
                    case 0:
                        return;
                    case 1:
                        this.manage_documents();
                }
            }
        }
    }
}

