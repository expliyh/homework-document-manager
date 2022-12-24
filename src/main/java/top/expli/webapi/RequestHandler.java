package top.expli.webapi;

import top.expli.User;
import top.expli.cache_user;
import top.expli.exceptions.*;
import top.expli.knives;
import top.expli.webapi.SocketServer.ClientConnector;

import java.awt.event.ActionListener;
import java.util.Objects;

public class RequestHandler {
    public static Response process(Request input, ClientConnector connector) throws Exit {
        if (connector.getUserName() == null || connector.getPermissionLevel() == null) {
            return new Response(401);
        }
        switch (input.nameSpace) {
            case Request.NameSpace.SYSTEM -> {
                switch (input.operation) {
                    case Request.Operations.LOGIN -> {
                        User usr;
                        try {
                            usr = cache_user.login(input.detail.get("userName"), input.detail.get("password"));
                        } catch (AuthFailed e) {
                            return new Response(401, e);
                        } catch (UserNotFound e) {
                            return new Response(404, e);
                        }
                        connector.setUserName(usr.getUserName());
                        connector.setPermissionLevel(usr.getPermission_level());
                    }
                    case Request.Operations.LOGOUT -> {
                        connector.setUserName(null);
                        connector.setPermissionLevel(null);
                    }
                    case Request.Operations.EXIT -> {
                        throw new Exit();
                    }
                }
            }
            case Request.NameSpace.USERS -> {
                switch (input.operation) {
                    case Request.Operations.ADD -> {
                        if (connector.getPermissionLevel() > 3) {
                            return new Response(403, new PermissionDenied(knives.random()));
                        }
                        String userName = input.detail.get("userName");
                        String password = input.detail.get("password");
                        String permissionLevel = input.detail.get("permissionLevel");
                        if (userName == null || password == null || permissionLevel == null) {
                            return new Response(400);
                        }
                        try {
                            if (Integer.parseInt(permissionLevel) < 3) {
                                return new Response(403, new PermissionDenied(knives.random()));
                            }
                            cache_user.addUser(userName, password, Integer.parseInt(permissionLevel));
                        } catch (NumberFormatException ex) {
                            return new Response(400, new BadFormat(knives.random()));
                        } catch (UserExists e) {
                            return new Response(400, e);
                        }
                        return new Response(200);
                    }
                    case Request.Operations.EDIT -> {
                        String userName = input.detail.get("userName");
                        String password = input.detail.get("password");
                        String permissionLevel = input.detail.get("permissionLevel");
                        try {
                            if (connector.getPermissionLevel() > 3) {
                                return new Response(403, new PermissionDenied(knives.random()));
                            }
                            if (connector.getPermissionLevel() >= cache_user.GetPermissionLevel(userName) && !Objects.equals(connector.getUserName(), userName)) {
                                return new Response(403, new PermissionDenied(knives.random()));
                            }
                        } catch (UserNotFound e) {
                            return new Response(404, e);
                        }

                        if (userName == null || password == null || permissionLevel == null) {
                            return new Response(400);
                        }
                        try {
                            if (Integer.parseInt(permissionLevel) < 3) {
                                return new Response(403, new PermissionDenied(knives.random()));
                            }
                            if (Integer.parseInt(permissionLevel) < connector.getPermissionLevel()) {
                                return new Response(403, new PermissionDenied(knives.random()));
                            }
                            cache_user.editUser(userName, password, Integer.parseInt(permissionLevel));
                            if (userName.equals(connector.getUserName())) {
                                connector.setPermissionLevel(Integer.parseInt(permissionLevel));
                            }
                        } catch (NumberFormatException ex) {
                            return new Response(400, new BadFormat(knives.random()));
                        } catch (UserNotFound e) {
                            return new Response(404, e);
                        }
                    }
                    case Request.Operations.DELETE -> {
                        String userName = input.detail.get("userName");
                        try {
                            if ((connector.getPermissionLevel() > 3 || connector.getPermissionLevel() >= cache_user.GetPermissionLevel(userName)) && !Objects.equals(userName, connector.getUserName())) {
                                return new Response(403, new PermissionDenied(knives.random()));
                            }
                            cache_user.removeUser(userName);
                            if (Objects.equals(userName, connector.getUserName())){
                                return new Response(200,"logout");
                            }else {
                                return new Response(200);
                            }
                        } catch (UserNotFound e) {
                            return new Response(404, e);
                        }
                    }
                }
            }
        }
        return new Response(0, (KnifeException) null);
    }

    public static void main(String[] args) {

    }
}
