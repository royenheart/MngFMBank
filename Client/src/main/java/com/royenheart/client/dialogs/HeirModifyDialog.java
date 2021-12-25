package com.royenheart.client.dialogs;

import com.royenheart.basicsets.programsettings.UserPattern;
import com.royenheart.client.Connection;
import com.royenheart.client.components.NotNullPasswordField;
import com.royenheart.client.components.NotNullTextField;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class HeirModifyDialog extends JDialog implements Runnable, ActionListener {

    Thread t1 = null;
    String accountId, pwdInfo, heir;
    private final NotNullPasswordField pwd;
    private final NotNullTextField accountIdText;
    private final NotNullTextField heirText;
    private final JButton heirButton;

    public HeirModifyDialog(JFrame jf,String title,boolean isModel){
        super(jf, title, isModel);
        // face
        this.setBounds(600, 300, 300, 300);
        Box vBox = Box.createVerticalBox();

        // accountId
        Box accountIdBox = Box.createHorizontalBox();
        JLabel accountIdLabel = new JLabel("accountId");
        accountIdText = new NotNullTextField(15);

        accountIdBox.add(accountIdLabel);
        accountIdBox.add(Box.createHorizontalStrut(20));
        accountIdBox.add(accountIdText);

        // password
        Box pwdBox = Box.createHorizontalBox();
        JLabel pwdLabel = new JLabel("password");
        pwd = new NotNullPasswordField(15);

        pwdBox.add(pwdLabel);
        pwdBox.add(Box.createHorizontalStrut(20));
        pwdBox.add(pwd);

        // name to change
        Box nameBox = Box.createHorizontalBox();
        JLabel nameLabel = new JLabel("name to change");
        heirText = new NotNullTextField(15);

        nameBox.add(nameLabel);
        nameBox.add(Box.createHorizontalStrut(20));
        nameBox.add(heirText);

        //button
        Box btnBox = Box.createHorizontalBox();
        heirButton = new JButton("name modify");
        heirButton.addActionListener(this);
        btnBox.add(heirButton);

        vBox.add(Box.createVerticalStrut(40));
        vBox.add(accountIdBox);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(pwdBox);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(nameBox);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(btnBox);
        vBox.add(Box.createVerticalStrut(40));

        //to hava space in left and right,add another Box
        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(20));
        hBox.add(vBox);
        hBox.add(Box.createHorizontalStrut(20));

        this.add(hBox);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == heirButton){
            try {
                accountId = accountIdText.getTextLegal(String.valueOf(UserPattern.accountId)).trim();
                heir = heirText.getTextLegal(String.valueOf(UserPattern.heir)).trim();
                pwdInfo = new String(pwd.getPasswordLegal()).trim();

                String request = "E%accountId:" + accountId+ ";password:" + pwdInfo + ";heir:" + heir + ";%";
                Connection.writeUTF(request);

                t1 = new Thread(this);
                t1.start();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException ex) {
                JOptionPane.showMessageDialog(null, "模式不匹配，无法进行合法性检查");
            }
        }
    }

    @Override
    public void run(){
        try{
            String response = Connection.readUTF();
            JOptionPane.showMessageDialog(null, response);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
