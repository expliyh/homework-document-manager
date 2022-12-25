package top.expli.documents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import top.expli.Token;
import top.expli.User;
import top.expli.exceptions.*;
import top.expli.knives;
import top.expli.cache_user;

import javax.print.Doc;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class Documents implements Serializable {
    public static final String savePath = "data/docs/";

    public static Vector<Vector<String>> listDocument(String userName) throws UserNotFound {
        int permissionLevel = cache_user.GetPermissionLevel(userName);
        Vector<Vector<String>> vec = new Vector<>();
        for (Map.Entry<String, Document> i : documents.entrySet()) {
            System.out.println(i.getValue().getClass().getName());
            if (Objects.equals(i.getValue().getOwner(), userName)) {
                Vector<String> tmp = new Vector<>(2);
                tmp.add(i.getKey());
                tmp.add(i.getValue().getOwner());
                vec.add(tmp);
            } else if (i.getValue().getPermission_level() > permissionLevel) {
                Vector<String> tmp = new Vector<>(2);
                tmp.add(i.getKey());
                tmp.add(i.getValue().getOwner());
                vec.add(tmp);
            }
        }
        return vec;
    }

    public static String getDescription(String docName) throws DocumentNotFound {
        Document document = documents.get(docName);
        if (document == null) {
            throw new DocumentNotFound();
        }
        return document.getDescription();
    }

    public static int getPermissionLevel(String docName) throws DocumentNotFound {
        Document document = documents.get(docName);
        if (document == null) {
            throw new DocumentNotFound();
        }
        return document.getPermission_level();
    }

    public static byte[] getBytesArray(String docName) throws DocumentNotFound, FileNotFound {
        String path = "data/docs";
        Document outDoc = documents.get(docName);
        if (outDoc == null) {
            throw new DocumentNotFound();
        }
        File outFile = new File(path, outDoc.getContent());
        if (!outFile.exists()) {
            throw new FileNotFound();
        }
        try (FileInputStream fileInputStream = new FileInputStream(outFile)) {
            return fileInputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getOwner(String docName) throws DocumentNotFound {
        Document document = documents.get(docName);
        if (document == null) {
            throw new DocumentNotFound();
        }
        return document.getOwner();
    }

    public static void addDocument(byte[] file, String user_name, int permission_level, String docName, String fileName, String description, Date createTime, Date lastModified) throws IOException {
        String path = "data/docs";
        File outFile = new File(path);
        outFile.mkdirs();
        outFile = new File(path, Token.randomString(128));
        while (outFile.exists()) {
            outFile = new File(path, Token.randomString(128));
        }
        outFile.createNewFile();
        try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
            outputStream.write(file);
            outFile.setLastModified(lastModified.getTime());
        } catch (IOException e) {
            throw new IOException();
        }
        Document document = new Document(user_name, permission_level, docName, fileName, outFile.getName(), description);
        documents.put(docName, document);
    }

    public static void addDocument(File file, String user_name, int permission_level, String docName, String description, BasicFileAttributes attributes) throws FileNotFound, IOException {
        String path = "data/docs";
        File outFile = new File(path);
        outFile.mkdirs();
        outFile = new File(path, Token.randomString(128));
        while (outFile.exists()) {
            outFile = new File(path, Token.randomString(128));
        }

        Files.copy(file.toPath(), outFile.toPath());

//        try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
//            try (FileInputStream inputStream = new FileInputStream(file)) {
//                outputStream.write(inputStream.readAllBytes());
//            }
//        } catch (IOException e) {
//            throw new FileNotFound(knives.random());
//        }
        Document document = new Document(user_name, permission_level, docName, file.getName(), outFile.getName(), description);
        documents.put(docName, document);
    }

    public static void editDocPermission(String docName, int permission) throws DocumentNotFound {
        Document document = documents.get(docName);
        if (document == null) {
            throw new DocumentNotFound();
        }
        document.setPermission_level(permission);
    }

    public static void editDocDescription(String docName, String description) throws DocumentNotFound {
        Document document = documents.get(docName);
        if (document == null) {
            throw new DocumentNotFound();
        }
        document.setDescription(description);
    }

    public static void editDocName(String oldDocName, String newDocName) throws DocumentNotFound {
        if (Objects.equals(oldDocName, newDocName)) {
            return;
        }
        Document document = documents.get(oldDocName);
        if (document == null) {
            throw new DocumentNotFound();
        }
        Document newDoc = new Document(document.getOwner(), document.getPermission_level(), newDocName, document.getFileName(), document.getContent(), document.getDescription());
        documents.remove(oldDocName);
        documents.put(newDocName, newDoc);
    }

    public static String getFileName(String docName) throws DocumentNotFound {
        Document document = documents.get(docName);
        if (document == null) {
            throw new DocumentNotFound();
        }
        return document.getFileName();
    }

    public static void saveAs(String path, String file_name) throws FileNotFound {
        String pathIn = "data/docs/";
        Document document = documents.get(file_name);
        File inFile = new File(pathIn, document.getContent());
        File outFile = new File(path);
        outFile.mkdirs();
        outFile = new File(path, file_name);
        try (FileInputStream inputStream = new FileInputStream(inFile); FileOutputStream outputStream = new FileOutputStream(outFile)) {
            outputStream.write(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new FileNotFound(knives.random());
        }
    }

    public static void deleteDocument(String docName) throws DocumentNotFound, ServerError {
        Document toDelete = Documents.documents.get(docName);
        if (toDelete == null) {
            throw new DocumentNotFound();
        }
        File fileToDelete = new File(savePath,toDelete.getContent());
        if (!fileToDelete.delete()){
            throw new ServerError();
        }
        documents.remove(toDelete.getDocName());
    }

    public static void saveAs(File outFile, String file_name) throws FileNotFound {
        String pathIn = "data/docs/";
        Document document = documents.get(file_name);
        File inFile = new File(pathIn, document.getContent());
        try (FileInputStream inputStream = new FileInputStream(inFile); FileOutputStream outputStream = new FileOutputStream(outFile)) {
            outputStream.write(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new FileNotFound(knives.random());
        }
    }

    public static void LoadFromJson() throws FileNotFound {
        String file_name = "resources/document.json";
        File json_file = new File(file_name);
        ObjectMapper objectMapper = new ObjectMapper();
        try (FileInputStream inputStream = new FileInputStream(json_file)) {
            InputStreamReader in_reader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(in_reader);
            String json_str;
            while ((json_str = reader.readLine()) != null) {
                try {
                    Document document = objectMapper.readValue(json_str, Document.class);
                    documents.put(document.getOwner(), document);
                } catch (JsonMappingException e) {
                    throw new JsonDecodeFail(knives.random());
                }
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFound(knives.random());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("出错了，请联系工作人员");
            return;
        }
    }

    public static void writeData() {
        try (FileOutputStream doc_out = new FileOutputStream("data/docs.ser")) {
            ObjectOutputStream doc_cache_out = new ObjectOutputStream(doc_out);
            doc_cache_out.writeObject(documents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void readData() {
        try (FileInputStream doc_in = new FileInputStream("data/docs.ser");) {
            ObjectInputStream doc_cache_file = new ObjectInputStream(doc_in);
            documents = (ConcurrentHashMap<String, Document>) doc_cache_file.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("出错了，请联系工作人员");
            return;
        } catch (IOException ignored) {
        }
    }

    protected static ConcurrentHashMap<String, Document> documents = new ConcurrentHashMap<>();

    @Override
    public String toString() {
        return "Documents{" +
                "documents=" + documents +
                '}';
    }
}
