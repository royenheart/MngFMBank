package com.royenheart.client.dialogs;

import com.royenheart.client.Connection;
import com.royenheart.client.FilesOperation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 获取年终报告
 * @author RoyenHeart
 */
public class GetYearlyReportDialog extends JDialog implements Runnable, ActionListener {

    Thread t1 = null;
    private File file;
    private final JButton chooseButton;
    private final JButton sendButton;
    private final JFrame jf;
    private final JTextArea showResult;
    private String request;

    public GetYearlyReportDialog(JFrame jf,String title,boolean isModel){
        super(jf, title, isModel);
        this.jf = jf;
        // 主要界面设置
        this.setBounds(600, 300, 500, 300);
        Box vBox = Box.createVerticalBox();

        // show result
        showResult = new JTextArea(8, 18);

        // choose store location button
        Box btnBox = Box.createHorizontalBox();
        chooseButton = new JButton("Choose where to store the report");
        chooseButton.addActionListener(this);
        btnBox.add(chooseButton);

        // send request button
        sendButton = new JButton("Send request");
        sendButton.addActionListener(this);
        btnBox.add(sendButton);

        chooseButton.setEnabled(true);
        sendButton.setEnabled(false);

        vBox.add(Box.createVerticalStrut(40));
        vBox.add(showResult);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(btnBox);
        vBox.add(Box.createVerticalStrut(40));

        // to hava space in left and right,add another Box
        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(20));
        hBox.add(vBox);
        hBox.add(Box.createHorizontalStrut(20));

        this.add(hBox);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == chooseButton){
            FileStoreDialog fileStoreDialog = new FileStoreDialog(
                    "report" + new SimpleDateFormat("yyyy").format(new Date()) + ".pdf");

            int result = fileStoreDialog.showOpenDialog(jf);
            file = null;

            if (result == JFileChooser.APPROVE_OPTION) {
                file = fileStoreDialog.getSelectedFile();
            }

            if (file != null) {
                showResult.append("文件创建成功\n");
            } else {
                showResult.append("文件创建失败\n");
            }
            request = "J%%";
            sendButton.setEnabled(true);
        } else if (e.getSource() == sendButton) {
            try {
                Connection.writeUTF(request);
                t1 = new Thread(this);
                t1.start();
                sendButton.setEnabled(false);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void run(){
        try{
            String response = Connection.readUTF();
            FilesOperation filesOperation = new FilesOperation();
            filesOperation.readFile(file, response);
            showResult.append("文件" + file + "已获取\n");
        } catch (IOException e) {
            showResult.append("数据导出失败，请检查接收文件是否被其他应用占用\n");
            e.printStackTrace();
        }
    }
}
