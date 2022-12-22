package top.expli.GUI;

import com.formdev.flatlaf.FlatIntelliJLaf;
import top.expli.ExceptionProcess;
import top.expli.User;
import top.expli.cache_user;
import top.expli.exceptions.UserNotFound;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Objects;

public class UserAdmin extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nameField;
    private JPasswordField passwordField;
    private JButton userdelButton;
    private JComboBox<String> permissionBox;

    private User user;
    private String meName;

    public UserAdmin() {
        super((Frame) null, "用户编辑");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCancel);

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
        userdelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(getComponent(0), "您确认要删除该用户吗？", "删除用户 " + user.getUserName(), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == 0) {
                    try {
                        cache_user.removeUser(user.getUserName());
                    } catch (UserNotFound ex) {
                        ExceptionProcess.process(getComponent(0), ex);
                    }
                    dispose();
                }
            }
        });
    }

    public UserAdmin(User usr, String meName) {
        this();
        this.user = usr;
        this.meName = meName;
        nameField.setText(user.getUserName());
        String[] list = new String[]{"管理员", "用户", "访客"};
        if (usr.get_permission_level() < 3) {
            this.userdelButton.setVisible(false);
            list = new String[]{"管理员", "用户", "访客", "S·Y·S·T·E·M", "P·A·I·M·O·N", "SU"};
            this.permissionBox.setEnabled(false);
        }
        ComboBoxModel<String> model = new DefaultComboBoxModel<>(list);
        model.setSelectedItem(list[(usr.get_permission_level() + 3) % 6]);
        this.permissionBox.setModel(model);
    }

    private void onOK() {
        // 在此处添加您的代码
        if (!Arrays.equals(passwordField.getPassword(), "".toCharArray())) {
            cache_user.changePasswd(user.getUserName(), Arrays.toString(passwordField.getPassword()));
        }
        if ((this.permissionBox.getSelectedIndex() + 3) % 6 != this.user.get_permission_level()) {
            if (Objects.equals(this.user.getUserName(), this.meName)) {
                if (JOptionPane.showConfirmDialog(getRootPane(), "您正在修改自己的权限等级\n" +
                        "请仔细确认！", "不要丢失自己的访问权限", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            cache_user.setPermissionLevel(user.getUserName(), (this.permissionBox.getSelectedIndex() + 3) % 6);
        }
        dispose();
    }

    private void onCancel() {
        // 必要时在此处添加您的代码
        dispose();
    }

    public static void main(String[] args) {
        main(args, new User("Demo", "passwd"), "S·Y·S·T·E·M");
    }

    //用来兼容之前的代码
    public static void main(String[] args, User usr, String meName) {
        main(usr, meName);
    }

    public static void main(User usr, String meName) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        UserAdmin dialog = new UserAdmin(usr, meName);
        dialog.pack();
        dialog.setVisible(true);
        //System.exit(0);
    }
}
