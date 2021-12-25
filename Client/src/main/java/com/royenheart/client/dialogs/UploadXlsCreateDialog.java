package com.royenheart.client.dialogs;

import com.royenheart.client.Connection;
import com.royenheart.client.FilesOperation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * 上传Xls文件批量开户
 * @author RoyenHeart
 */
public class UploadXlsCreateDialog extends JDialog implements Runnable, ActionListener {

    Thread t1 = null;
    private JButton chooseButton;
    private JButton sendButton;
    private JFrame jf;
    private JTextArea showResult;
    private String request;

    public UploadXlsCreateDialog(JFrame jf,String title,boolean isModel){
        super(jf, title, isModel);
        this.jf = jf;
        // 主要界面设置
        this.setBounds(600,300,500,500);
        Box vBox = Box.createVerticalBox();

        // show result
        showResult = new JTextArea(8,18);

        // choose xls button
        Box btnBox = Box.createHorizontalBox();
        chooseButton = new JButton("Choose Xls file");
        chooseButton.addActionListener(this);
        btnBox.add(chooseButton);

        // send xls button
        sendButton = new JButton("Send Xls file");
        sendButton.addActionListener(this);
        btnBox.add(sendButton);

        chooseButton.setEnabled(true);
        sendButton.setEnabled(false);

        vBox.add(Box.createVerticalStrut(100));
        vBox.add(showResult);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(Box.createVerticalStrut(100));
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
            FileChooseDialog fileChooseDialog = new FileChooseDialog(".");

            int result = fileChooseDialog.showOpenDialog(jf);
            File file = null;

            if (result == JFileChooser.APPROVE_OPTION) {
                file = fileChooseDialog.getSelectedFile();
            }

            if (file == null) {
                showResult.append("文件获取失败\n");
            } else {
                showResult.append("文件已获取\n");
            }
            FilesOperation filesOperation = new FilesOperation();
            request = "H%" + filesOperation.writeFile(file) + "%";
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
            JOptionPane.showMessageDialog(null, response);
            showResult.append(response + "\n");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
