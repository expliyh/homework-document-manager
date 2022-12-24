package top.expli;

import org.jetbrains.annotations.NotNull;
import top.expli.exceptions.AuthFailed;
import top.expli.exceptions.UserExists;
import top.expli.exceptions.UserNotFound;

import java.io.*;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class cache_user {
    public cache_user() {
//        if (users.isEmpty()) {
//            users.put("admin", new User("admin", "12345678"));
//        }
    }

    public static @NotNull User login(String username, String passwd) throws AuthFailed, UserNotFound {
        User user = users.get(username);
        if (user == null) {
            throw new UserNotFound(knives.random());
        }
        user.token_request(Token.sha256(passwd));
        return user;
    }

    public static void editUser(String username, String passwd, int permission) throws UserNotFound {
        User usr = cache_user.findUser(username);
        usr.setPermission_level(permission);
        usr.setRefreshToken(passwd);
    }

    public static void changePasswd(String userName, String newPasswd) {
        users.get(userName).setRefreshToken(newPasswd);
    }

    public static void addUser(String userName, String password, int permissionLevel) throws UserExists {
        if (isUserExists(userName)) {
            throw new UserExists(knives.random());
        } else {
            users.put(userName, new User(userName, password, permissionLevel));
        }
    }

    public static void addUser(@NotNull User usr) throws UserExists {
        if (isUserExists(usr.getUserName())) {
            throw new UserExists(knives.random());
        } else {
            users.put(usr.getUserName(), usr);
        }
    }

    public static void removeUser(String userName) throws UserNotFound {
        if (isUserExists(userName)) {
            users.remove(userName);
        } else {
            throw new UserNotFound(knives.random());
        }
    }

    public static int GetPermissionLevel(String username) throws UserNotFound {
        User usr;
        usr = users.get(username);
        if (usr == null) {
            throw new UserNotFound(knives.random());
        }
        return usr.get_permission_level();
    }

    public static void setPermissionLevel(String userName, int newPermission) {
        users.get(userName).setPermission_level(newPermission);
    }

    public static boolean isUserExists(String username) {
        return users.get(username) != null;
    }

    public static @NotNull User findUser(String username) throws UserNotFound {
        User usr;
        usr = users.get(username);
        if (usr == null) {
            throw new UserNotFound(knives.random());
        }
        return usr;
    }

    public static Vector<Vector<String>> searchUser(String key) {
        return searchUser(key, false);
    }

    public static Vector<Vector<String>> searchUser(String key, boolean isCaps) {
        return new Vector<Vector<String>>();
    }

    private static ConcurrentHashMap<String, User> users = new ConcurrentHashMap<String, User>();

    public static void writeData() {
        try (FileOutputStream user_out = new FileOutputStream("data/users.ser")) {
            ObjectOutputStream user_cache_out = new ObjectOutputStream(user_out);
            user_cache_out.writeObject(users);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static @NotNull Vector<Vector<String>> getUserList() {
        Vector<Vector<String>> vec = new Vector<>(users.size());
        for (Map.Entry<String, User> i : users.entrySet()) {
            String permissionLevel;
            switch (i.getValue().get_permission_level()) {
                case 0 -> permissionLevel = "S·Y·S·T·E·M";
                case 1 -> permissionLevel = "P·A·I·M·O·N";
                case 2 -> permissionLevel = "SU";
                case 3 -> permissionLevel = "管理员";
                case 4 -> permissionLevel = "用户";
                default -> permissionLevel = "访客";
            }
            Vector<String> set = new Vector<>(2);
            set.add(i.getKey());
            set.add(permissionLevel);
            vec.add(set);
        }
        return vec;
    }

    public static void readData() throws IOException {
        try (FileInputStream user_in = new FileInputStream("data/users.ser");) {
            ObjectInputStream user_cache_file = new ObjectInputStream(user_in);
            users = (ConcurrentHashMap<String, User>) user_cache_file.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("出错了，请联系工作人员");
            return;
        } catch (IOException e) {
            if (users.isEmpty()) {
                users.put("admin", new User("admin", "12345678"));
            }
        }
        if (!isUserExists("demo")) {
            users.put("demo", new User("demo", "123456", 4));
        }
        if (!isUserExists("guest")) {
            users.put("guest", new User("guest", "", 10086));
        }
    }

}
