package top.expli.documents;

import top.expli.Token;
import top.expli.exceptions.PermissionDenied;
import top.expli.exceptions.PleaseUpdate;
import top.expli.knives;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class Document implements Serializable {
    Document() {
        owner = "S•Y•S•T•E•M";
        permission_level = 0;
        content = "";
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    Document(String owner, int permission_level, String docName, String fileName, String content, String description, BasicFileAttributes attributes) {
        this.owner = owner;
        this.permission_level = permission_level;
        this.content = content;
        this.description = description;
        this.fileName = fileName;
        this.docName = docName;
    }

    public String getOwner() {
        return owner;
    }

    protected String docName;

    public String getFileName() {
        return fileName;
    }

    public String getLastEditTime() throws IOException {
        String path = "data/docs/";
        File file = new File(path,getContent());
        return new SimpleDateFormat("yyyy/MM/dd").format(Files.readAttributes(file.toPath(),BasicFileAttributes.class).lastModifiedTime().toMillis());
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private String fileName;

    public int getPermission_level() {
        return permission_level;
    }

    public void saveFile(String path) throws FileNotFoundException {
        File file = new File(this.getContent());
        File fileOut = new File(path);
        fileOut.mkdirs();
        fileOut = new File(path, this.getDocName());
        FileOutputStream outFileStream = new FileOutputStream(fileOut);
        //outFileStream.write(file);
    }

    public String getContent() {
        return content;
    }

//    public String getContent(String access_token) throws PermissionDenied, PleaseUpdate {
//        if (Objects.equals(this.owner, Token.get_username(access_token))) {
//            return content;
//        }
//        if (Token.getPermissionLevel(access_token) <= permission_level) {
//            return content;
//        } else {
//            throw new PermissionDenied(knives.random());
//        }
//    }

    protected String owner;
    protected int permission_level;
    protected String content;

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setPermission_level(int permission_level) {
        this.permission_level = permission_level;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    protected String description;

    @Override
    public String toString() {
        return "Document{" +
                "docName='" + docName + '\'' +
                ", fileName='" + fileName + '\'' +
                ", owner='" + owner + '\'' +
                ", permission_level=" + permission_level +
                ", content='" + content + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
