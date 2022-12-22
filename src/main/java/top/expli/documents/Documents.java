package top.expli.documents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import top.expli.Token;
import top.expli.User;
import top.expli.exceptions.FileNotFound;
import top.expli.exceptions.JsonDecodeFail;
import top.expli.exceptions.UserNotFound;
import top.expli.knives;
import top.expli.cache_user;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class Documents implements Serializable {

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

    public static void addDocument(File file, String user_name, int permission_level, String docName, String description, BasicFileAttributes attributes) throws FileNotFound, IOException {
        String path = "data/docs";
        File outFile = new File(path);
        outFile.mkdirs();
        outFile = new File(path,Token.randomString(128));
        while (outFile.exists()) {
            outFile = new File(path, Token.randomString(128));
        }

        Files.copy(file.toPath(),outFile.toPath());

//        try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
//            try (FileInputStream inputStream = new FileInputStream(file)) {
//                outputStream.write(inputStream.readAllBytes());
//            }
//        } catch (IOException e) {
//            throw new FileNotFound(knives.random());
//        }
        Document document = new Document(user_name, permission_level, docName,file.getName(), outFile.getName(), description, Files.readAttributes(file.toPath(), BasicFileAttributes.class));
        documents.put(file.getName(), document);
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

    public static void deleteDocument(String fileName) throws FileNotFound {
        Document toDelete = Documents.documents.get(fileName);
        if(toDelete==null){
            throw new FileNotFound(knives.random());
        }

    }

    public static void saveAs(File outFile,String file_name) throws FileNotFound {
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
