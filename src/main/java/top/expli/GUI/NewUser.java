package top.expli.GUI;

import com.formdev.flatlaf.FlatIntelliJLaf;
import top.expli.ExceptionProcess;
import top.expli.PasswordCheck;
import top.expli.cache_user;
import top.expli.exceptions.UserExists;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class NewUser extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField userNameField;
    private JComboBox<String> permissionBox;
    private JPasswordField passwordField;
    private JPasswordField passwordRepeatField;
    private JLabel errorLabel;

    public NewUser() {
        setContentPane(contentPane);
        setModal(true);
        errorLabel.setText("");
        buttonOK.setEnabled(false);
        String[] list = new String[]{"管理员", "用户", "访客"};
        ComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(list);
        comboBoxModel.setSelectedItem(list[2]);
        permissionBox.setModel(comboBoxModel);
        getRootPane().setDefaultButton(buttonOK);

        javax.swing.text.Document passwordDoc = passwordField.getDocument();
        javax.swing.text.Document confirmDoc = passwordRepeatField.getDocument();
        javax.swing.text.Document userNameDoc = userNameField.getDocument();
        passwordDoc.addDocumentListener(new InputListener());
        confirmDoc.addDocumentListener(new InputListener());
        userNameDoc.addDocumentListener(new InputListener());
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

    private class InputListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            lCheck();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            lCheck();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            lCheck();
        }
    }

    private void lCheck() {
        boolean errorFound = false;
        String userName = userNameField.getText();
        String password = String.valueOf(passwordField.getPassword());
        String confirm = String.valueOf(passwordRepeatField.getPassword());
        int permission = permissionBox.getSelectedIndex();
        StringBuilder warnText = new StringBuilder();
        if (userName.isEmpty()) {
            warnText.append("错误：用户名为空  ");
            errorFound = true;
        } else if (cache_user.isUserExists(userName)) {
            warnText.append("错误：用户已存在  ");
            errorFound = true;
        }
        switch (PasswordCheck.check(password, confirm)) {
            case PasswordCheck.CHECK_PASSED -> {
            }
            case PasswordCheck.PASSWORD_TOO_SHORT -> {
                warnText.append("错误：密码太短");
                errorFound = true;
            }
            case PasswordCheck.PASSWORD_TOO_LONG -> {
                warnText.append("错误：密码太长");
                errorFound = true;
            }
            case PasswordCheck.PASSWORD_TOO_SIMPLE -> {
                warnText.append("错误：密码太简单");
                errorFound = true;
            }
            case PasswordCheck.ILLEGAL_CHARACTER -> {
                warnText.append("错误，密码含非法字符");
                errorFound = true;
            }
            case PasswordCheck.PASSWORD_AND_CONFIRM_NOT_MATCH -> {
                warnText.append("错误，密码与密码确认不一致");
                errorFound = true;
            }
            case PasswordCheck.EMPTY_PASSWORD -> {
                warnText.append("错误，空密码");
                errorFound = true;
            }
        }
        errorLabel.setText(warnText.toString());
        buttonOK.setEnabled(!errorFound);
    }

    public NewUser(Component parent) {

    }

    private void onOK() {
        // 在此处添加您的代码
        try {
            cache_user.addUser(userNameField.getText(), String.valueOf(passwordField.getPassword()), permissionBox.getSelectedIndex() + 3);
        } catch (UserExists e) {
            ExceptionProcess.process(getRootPane(), e);
        }
        dispose();
    }

    private void onCancel() {
        // 必要时在此处添加您的代码
        dispose();
    }

    public static void main(String[] args) {
        main((Component) null);
    }

    public static void main(Component parent) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        NewUser dialog = new NewUser();
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setTitle("添加新用户");

        dialog.setVisible(true);
        //System.exit(0);
    }
}
