package top.expli.webapi;

import top.expli.GUI.SaveAll;
import top.expli.Permissions;
import top.expli.User;
import top.expli.cache_user;
import top.expli.documents.Documents;
import top.expli.exceptions.*;
import top.expli.knives;
import top.expli.webapi.SocketServer.ClientConnector;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;

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
                        return new Response(200);
                    }
                    case Request.Operations.LOGOUT -> {
                        connector.setUserName(null);
                        connector.setPermissionLevel(null);
                        return new Response(200, "logout");
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
                            if (Objects.equals(userName, connector.getUserName())) {
                                return new Response(200, "logout");
                            } else {
                                return new Response(200);
                            }
                        } catch (UserNotFound e) {
                            return new Response(404, e);
                        }
                    }
                    case Request.Operations.LIST -> {
                        if (connector.getPermissionLevel() > 3) {
                            return new Response(403, new PermissionDenied(knives.random()));
                        }
                        return new Response(200, cache_user.getUserList());
                    }
                }
            }
            case Request.NameSpace.DOCUMENTS -> {
                switch (input.operation) {
                    case Request.Operations.ADD -> {
                        String createTimeString = input.detail.get("createTime");
                        String lastModifiedString = input.detail.get("lastModified");
                        String docName = input.detail.get("docName");
                        String fileName = input.detail.get("fileName");
                        String description = input.detail.get("description");
                        if (createTimeString == null || lastModifiedString == null || docName == null || fileName == null || description == null) {
                            return new Response(400);
                        }
                        if (input.attachment == null) {
                            return new Response(400);
                        }
                        Date createTime;
                        Date lastModified;
                        if (connector.getPermissionLevel() > 4) {
                            return new Response(403, new PermissionDenied(knives.random()));
                        }
                        try {
                            createTime = new Date(Long.parseLong(createTimeString));
                            lastModified = new Date(Long.parseLong(lastModifiedString));
                            Documents.addDocument(input.attachment, connector.getUserName(), connector.getPermissionLevel(), docName, fileName, description, createTime, lastModified);
                            return new Response(200);
                        } catch (NumberFormatException exception) {
                            return new Response(400);
                        } catch (IOException e) {
                            return new Response(500);
                        }
                    }
                    case Request.Operations.GET -> {
                        String docName = input.detail.get("docName");
                        if (docName == null) {
                            return new Response(400);
                        }
                        int docPermissionLevel;
                        String docOwner;
                        try {
                            docPermissionLevel = Documents.getPermissionLevel(docName);
                            docOwner = Documents.getOwner(docName);
                            if (docPermissionLevel < Permissions.PUBLIC) {
                                if (docPermissionLevel <= connector.getPermissionLevel() && !Objects.equals(docOwner, connector.getUserName())) {
                                    return new Response(403, new PermissionDenied());
                                }
                            }
                            byte[] file = Documents.getBytesArray(docName);
                            Map<String, String> detail = new HashMap<>();
                            detail.put("fileName", Documents.getFileName(docName));
                            return new Response(200, file, detail);
                        } catch (DocumentNotFound | FileNotFound e) {
                            return new Response(404, new DocumentNotFound());
                        }
                    }
                    case Request.Operations.EDIT -> {

                    }
                }
            }
        }
        return new Response(0, (KnifeException) null);
    }

    public static void main(String[] args) {

    }
}
