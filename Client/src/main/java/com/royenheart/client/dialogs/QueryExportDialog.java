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
 * 查询特定信息，服务器将查询信息传回客户端
 * @author RoyenHeart
 */
public class QueryExportDialog extends JDialog implements Runnable, ActionListener {

    Thread t1 = null;
    private File file;
    private StringBuilder request;

    private JTextField ageText;
    private JTextField sexText;
    private JTextField nameText;
    private JTextField phoneText;
    private JTextField moneyText;
    private JTextField birthText;
    private JTextField personalText;
    private JTextField heirText;
    private JButton chooseButton;
    private JButton sendButton;
    private JTextArea showResult;
    private JFrame jf;

    String sexInfo, ageInfo, nameInfo, phoneIdInfo, moneyInfo, birthInfo, personalIdInfo, heirInfo;
    String sexCon, ageCon, nameCon, phoneIdCon, moneyCon, birthCon, personalIdCon, heirCon;

    private JRadioButton selAge;
    private JRadioButton selSex;
    private JRadioButton selName;
    private JRadioButton selPhone;
    private JRadioButton selMoney;
    private JRadioButton selBirth;
    private JRadioButton selPersonal;
    private JRadioButton selHeir;

    private JComboBox conAge;
    private JComboBox conSex;
    private JComboBox conName;
    private JComboBox conPhone;
    private JComboBox conMoney;
    private JComboBox conBirth;
    private JComboBox conPersonal;
    private JComboBox conHeir;

    public JComboBox initComboBox() {
        JComboBox cmb = new JComboBox();
        cmb.addItem("< ");
        cmb.addItem("= ");
        cmb.addItem("> ");
        cmb.addItem("<=");
        cmb.addItem(">=");
        return cmb;
    }

    public QueryExportDialog(JFrame jf, String title, boolean isModel){
        super(jf, title, isModel);
        this.jf = jf;
        // 主要界面设置
        this.setBounds(600, 300, 500, 500);
        Box vBox = Box.createVerticalBox();

        // show result
        showResult = new JTextArea(5, 18);

        // choose store location button
        Box btnBox = Box.createHorizontalBox();
        chooseButton = new JButton("Choose where to store the query result");
        chooseButton.addActionListener(this);
        btnBox.add(chooseButton);

        // send request button
        sendButton = new JButton("Send request");
        sendButton.addActionListener(this);
        btnBox.add(sendButton);

        chooseButton.setEnabled(true);
        sendButton.setEnabled(false);

        // 查询表单

        Box explain = Box.createHorizontalBox();
        explain.add(new JLabel("使用与模式选择"));

        // age
        Box ageBox = Box.createHorizontalBox();
        selAge = new JRadioButton("使用");
        JLabel ageLabel = new JLabel("age");
        ageText = new JTextField(12);

        conAge = initComboBox();

        ageBox.add(selAge);
        ageBox.add(Box.createHorizontalStrut(20));
        ageBox.add(conAge);
        ageBox.add(Box.createHorizontalStrut(20));
        ageBox.add(ageLabel);
        ageBox.add(Box.createHorizontalStrut(20));
        ageBox.add(ageText);

        // sex
        Box sexBox = Box.createHorizontalBox();
        selSex = new JRadioButton("使用");
        JLabel sexLabel = new JLabel("sex");
        sexText = new JTextField(12);

        conSex = initComboBox();

        sexBox.add(selSex);
        sexBox.add(Box.createHorizontalStrut(20));
        sexBox.add(conSex);
        sexBox.add(Box.createHorizontalStrut(20));
        sexBox.add(sexLabel);
        sexBox.add(Box.createHorizontalStrut(20));
        sexBox.add(sexText);

        // name
        Box nameBox = Box.createHorizontalBox();
        selName = new JRadioButton("使用");
        JLabel nameLabel = new JLabel("name");
        nameText = new JTextField(12);

        conName = initComboBox();

        nameBox.add(selName);
        nameBox.add(Box.createHorizontalStrut(20));
        nameBox.add(conName);
        nameBox.add(Box.createHorizontalStrut(20));
        nameBox.add(nameLabel);
        nameBox.add(Box.createHorizontalStrut(20));
        nameBox.add(nameText);

        // phone
        Box phoneIdBox = Box.createHorizontalBox();
        selPhone = new JRadioButton("使用");
        JLabel phoneLabel = new JLabel("phone number");
        phoneText = new JTextField(12);

        conPhone = initComboBox();

        phoneIdBox.add(selPhone);
        phoneIdBox.add(Box.createHorizontalStrut(20));
        phoneIdBox.add(conPhone);
        phoneIdBox.add(Box.createHorizontalStrut(20));
        phoneIdBox.add(phoneLabel);
        phoneIdBox.add(Box.createHorizontalStrut(20));
        phoneIdBox.add(phoneText);

        // money
        Box moneyBox = Box.createHorizontalBox();
        selMoney = new JRadioButton("使用");
        JLabel moneyLabel = new JLabel("money");
        moneyText = new JTextField(12);

        conMoney = initComboBox();

        moneyBox.add(selMoney);
        moneyBox.add(Box.createHorizontalStrut(20));
        moneyBox.add(conMoney);
        moneyBox.add(Box.createHorizontalStrut(20));
        moneyBox.add(moneyLabel);
        moneyBox.add(Box.createHorizontalStrut(20));
        moneyBox.add(moneyText);

        // birth
        Box birthBox = Box.createHorizontalBox();
        selBirth = new JRadioButton("使用");
        JLabel birthLabel = new JLabel("birth");
        birthText = new JTextField(12);

        conBirth = initComboBox();

        birthBox.add(selBirth);
        birthBox.add(Box.createHorizontalStrut(20));
        birthBox.add(conBirth);
        birthBox.add(Box.createHorizontalStrut(20));
        birthBox.add(birthLabel);
        birthBox.add(Box.createHorizontalStrut(20));
        birthBox.add(birthText);

        // personalId
        Box personalIdBox = Box.createHorizontalBox();
        selPersonal = new JRadioButton("使用");
        JLabel personalIdLabel = new JLabel("personalId");
        personalText = new JTextField(12);

        conPersonal = initComboBox();

        personalIdBox.add(selPersonal);
        personalIdBox.add(Box.createHorizontalStrut(20));
        personalIdBox.add(conPersonal);
        personalIdBox.add(Box.createHorizontalStrut(20));
        personalIdBox.add(personalIdLabel);
        personalIdBox.add(Box.createHorizontalStrut(20));
        personalIdBox.add(personalText);

        // heir
        Box heirBox = Box.createHorizontalBox();
        selHeir = new JRadioButton("使用");
        JLabel heirLabel = new JLabel("heir");
        heirText = new JTextField(12);

        conHeir = initComboBox();

        heirBox.add(selHeir);
        heirBox.add(Box.createHorizontalStrut(20));
        heirBox.add(conHeir);
        heirBox.add(Box.createHorizontalStrut(20));
        heirBox.add(heirLabel);
        heirBox.add(Box.createHorizontalStrut(20));
        heirBox.add(heirText);

        vBox.add(Box.createVerticalStrut(10));
        vBox.add(explain);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(ageBox);
        vBox.add(sexBox);
        vBox.add(nameBox);
        vBox.add(phoneIdBox);
        vBox.add(moneyBox);
        vBox.add(birthBox);
        vBox.add(personalIdBox);
        vBox.add(heirBox);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(showResult);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(btnBox);
        vBox.add(Box.createVerticalStrut(10));

        // to hava space in left and right,add another Box
        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(20));
        hBox.add(vBox);
        hBox.add(Box.createHorizontalStrut(20));

        this.add(hBox);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == chooseButton) {
            FileStoreDialog fileStoreDialog = new FileStoreDialog(
                    "query-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".xls");

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

            ageCon = conAge.getSelectedItem().toString();
            sexCon = conSex.getSelectedItem().toString();
            nameCon = conName.getSelectedItem().toString();
            phoneIdCon = conPhone.getSelectedItem().toString();
            moneyCon = conMoney.getSelectedItem().toString();
            birthCon = conBirth.getSelectedItem().toString();
            personalIdCon = conPersonal.getSelectedItem().toString();
            heirCon = conHeir.getSelectedItem().toString();

            ageInfo = ageText.getText().trim();
            sexInfo = sexText.getText().trim();
            nameInfo = nameText.getText().trim();
            phoneIdInfo = phoneText.getText().trim();
            moneyInfo = moneyText.getText().trim();
            birthInfo = birthText.getText().trim();
            personalIdInfo = personalText.getText().trim();
            heirInfo = heirText.getText().trim();

            request = new StringBuilder().append("I%");

            if (selAge.isSelected()) { request.append(String.format("%s:%s;", "age", ageCon + ageInfo)); }
            if (selSex.isSelected()) { request.append(String.format("%s:%s;", "sex", sexCon + sexInfo)); }
            if (selName.isSelected()) { request.append(String.format("%s:%s;", "name", nameCon + nameInfo)); }
            if (selPhone.isSelected()) { request.append(String.format("%s:%s;", "phone", phoneIdCon + phoneIdInfo)); }
            if (selMoney.isSelected()) { request.append(String.format("%s:%s;", "money", moneyCon + moneyInfo)); }
            if (selBirth.isSelected()) { request.append(String.format("%s:%s;", "birth", birthCon + birthInfo)); }
            if (selPersonal.isSelected()) { request.append(String.format("%s:%s;", "personalId", personalIdCon + personalIdInfo)); }
            if (selHeir.isSelected()) { request.append(String.format("%s:%s;", "heir", heirCon + heirInfo)); }

            request.append("%");
            sendButton.setEnabled(true);
        } else if (e.getSource() == sendButton) {
            try {
                Connection.writeUTF(String.valueOf(request));
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
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
