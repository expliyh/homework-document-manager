package top.expli.GUI;

import com.formdev.flatlaf.FlatIntelliJLaf;
import top.expli.ExceptionProcess;
import top.expli.cache_user;
import top.expli.documents.Documents;
import top.expli.exceptions.KnifeException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserGUI {
    private JPanel mainPanel;
    private JButton docListButton;
    private JButton uploadButton;
    private JButton exitButton;
    private JButton passwdChangeButton;
    private JFrame thisFrame;
    private String userName;

    protected UserGUI(JFrame thisFrame, String userName) {
        this.thisFrame = thisFrame;
        this.userName = userName;

        docListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DocList.main(new String[]{}, userName);
            }
        });
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UploadDialog.main(userName, thisFrame);
//                JFileChooser jFileChooser = new JFileChooser();
//                if (jFileChooser.showOpenDialog(thisFrame) == JFileChooser.APPROVE_OPTION) {
//
//                    try {
//                        Documents.addDocument(jFileChooser.getSelectedFile(), userName, cache_user.GetPermissionLevel(userName));
//                    } catch (KnifeException knifeException) {
//                        ExceptionProcess.process(thisFrame, knifeException);
//                    }
//                }
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }


    public static void main(String[] args) {
        main(args, "demo");
    }

    public static void main(String[] args, String userName) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        JFrame frame = new JFrame("档案管理系统-用户界面");
        frame.setContentPane(new UserGUI(frame, userName).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
