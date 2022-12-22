package top.expli.GUI;

import top.expli.cache_user;
import top.expli.documents.Documents;

public class SaveAll {
    public static void save() {
        Documents.writeData();
        cache_user.writeData();
        System.out.println("保存完成！");
    }
}
