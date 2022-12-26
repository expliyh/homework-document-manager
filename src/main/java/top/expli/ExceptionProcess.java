package top.expli;

import top.expli.exceptions.KnifeException;

import javax.swing.*;
import java.awt.*;

public class ExceptionProcess {
    public static void process(Component component, Exception exception){
        switch (exception.getClass().getName()){
            case "top.expli.exceptions.UserNotFound"->{
                JOptionPane.showMessageDialog(component,"错误：用户不存在\n"+exception.getMessage(),"用户不存在", JOptionPane.ERROR_MESSAGE);
            }
            case "top.expli.exceptions.FileNotFound"->{
                JOptionPane.showMessageDialog(component,"错误：文件不存在\n"+exception.getMessage(),"用户不存在", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
