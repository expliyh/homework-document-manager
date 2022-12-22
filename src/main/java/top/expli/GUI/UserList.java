package top.expli.GUI;

import com.formdev.flatlaf.FlatIntelliJLaf;
import top.expli.ExceptionProcess;
import top.expli.cache_user;
import top.expli.exceptions.UserNotFound;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class UserList {

    private JFrame thisFrame;
    private JPanel panel;
    private JButton exitButton;
    private JTable userTable;
    private JTextField searchKey;
    private JButton buttonSearch;
    private JCheckBox isCaps;
    private JButton newUserButton;
    private String meName;

    public UserList(JFrame thisFrame, String meName) {
        this.thisFrame = thisFrame;
        this.meName = meName;
        try {
            if (cache_user.GetPermissionLevel(meName) > 3) {
                this.newUserButton.setEnabled(false);
                this.newUserButton.setVisible(false);
            }
        } catch (UserNotFound e) {
            ExceptionProcess.process(thisFrame, e);
        }
        refresh();
        userTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() >= 2) {
                    String userNameSelected = (String) userTable.getValueAt(userTable.getSelectedRow(), 0);
                    System.out.println("Click " + userNameSelected);
                    try {
                        UserAdmin.main(new String[]{""}, cache_user.findUser(userNameSelected), meName);
                    } catch (UserNotFound ex) {
                        ErrorMessage.main(new String[]{""}, ex);
                    }
                }
                refresh();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisFrame.dispose();
            }
        });
        newUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewUser.main(thisFrame);
                refresh();
            }
        });
    }

    public void refresh() {
        Vector<Vector<String>> list = cache_user.getUserList();
        Vector<String> head = new Vector<>(2);
        head.add("用户名");
        head.add("权限");
        DefaultTableModel userModel = new DefaultTableModel(list, head) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setModel(userModel);
    }

    public static void main(String meName) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("FUCK");
        }
        JFrame frame = new JFrame("用户列表");
        UserList list = new UserList(frame, meName);
        frame.setContentPane(list.panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
