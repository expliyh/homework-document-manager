package top.expli.GUI;

import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ErrorMessage extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel errorImage;
    private JLabel imageContainer;
    private JLabel errorMsg;
    private JLabel knife;

    public ErrorMessage(Exception e) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        ImageIcon errorImage = new ImageIcon(new ImageIcon("src/main/resources/ErrorImage/nxd.jpg").getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
        this.imageContainer.setIcon(errorImage);
        switch (e.getClass().getName().toString()) {
            case "top.expli.exceptions.AuthFailed" -> {
                this.errorMsg.setText("验证失败，请重试");
                this.knife.setText(e.getMessage());
            }
            case "top.expli.exceptions.UserNotFound" -> {
                this.errorMsg.setText("找不到用户，请重试");
                this.knife.setText(e.getMessage());
            }
        }

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // 点击 X 时调用 onCancel()
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // 遇到 ESCAPE 时调用 onCancel()
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // 在此处添加您的代码
        dispose();
    }

    private void onCancel() {
        // 必要时在此处添加您的代码
        dispose();
    }

    public static void main(String[] args, Exception e) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException e1) {
            throw new RuntimeException(e1);
        }
        ErrorMessage dialog = new ErrorMessage(e);
        dialog.pack();
        dialog.setVisible(true);
        //System.exit(0);
    }
}
