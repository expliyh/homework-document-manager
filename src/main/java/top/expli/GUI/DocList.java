package top.expli.GUI;

import com.formdev.flatlaf.FlatIntelliJLaf;
import top.expli.ExceptionProcess;
import top.expli.cache_user;
import top.expli.documents.Documents;
import top.expli.exceptions.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Vector;

public class DocList {
    private JFrame thisFrame;
    private JButton exitButton;
    private JTable docTable;
    private JButton searchButton;
    private JTextField searchText;
    private JCheckBox isCaps;
    private JPanel panel;
    private JButton uploadButton;

    protected String userName;

    public DocList(JFrame thisFrame, String userName) {
        this.thisFrame = thisFrame;
        this.userName = userName;
        try {
            if (cache_user.GetPermissionLevel(userName) > 4) {
                uploadButton.setVisible(false);
            }
        } catch (UserNotFound e) {
            ExceptionProcess.process(thisFrame, e);
        }
        this.refresh();
        docTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() >= 2) {
                    if (JOptionPane.showConfirmDialog(thisFrame, "确认要下载此文档吗？", "下载文档", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0) {
                        JFileChooser jFileChooser = new JFileChooser();
                        jFileChooser.setSelectedFile(new File((String) docTable.getValueAt(docTable.getSelectedRow(), 0)));
                        if (jFileChooser.showSaveDialog(thisFrame) == JFileChooser.APPROVE_OPTION) {
                            try {
                                Documents.saveAs(jFileChooser.getSelectedFile(), (String) docTable.getValueAt(docTable.getSelectedRow(), 0));
                            } catch (KnifeException ex) {
                                ExceptionProcess.process(thisFrame, ex);
                            }
                        }
                    }
                }
            }
        });
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                JFileChooser jFileChooser = new JFileChooser();
//                if (jFileChooser.showOpenDialog(thisFrame) == JFileChooser.APPROVE_OPTION) {
//                    try {
//                        Documents.addDocument(jFileChooser.getSelectedFile(), userName, cache_user.GetPermissionLevel(userName));
//                    } catch (KnifeException knifeException) {
//                        ExceptionProcess.process(thisFrame, knifeException);
//                    }
//                }
                UploadDialog.main(userName, thisFrame);
                refresh();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisFrame.dispose();
            }
        });
    }

    public void refresh() {
        Vector<Vector<String>> list;
        try {
            list = Documents.getDocumentList(userName);
        } catch (UserNotFound e) {
            ExceptionProcess.process(thisFrame, e);
            thisFrame.dispose();
            return;
        }

        Vector<String> head = new Vector<>(4);
        head.add("文档名");
        head.add("作者");
        head.add("权限");
        head.add("简介");
        DefaultTableModel docModel = new DefaultTableModel(list, head) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        docTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        docTable.setModel(docModel);
        docTable.getColumn(head.get(1)).setWidth(50);
    }

    public static void main(String[] args) {
        main(args, "admin");
    }

    public static void main(String[] args, String userName) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("主题设置失败！");
        }
        JFrame frame = new JFrame("DocList");
        frame.setContentPane(new DocList(frame, userName).panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
