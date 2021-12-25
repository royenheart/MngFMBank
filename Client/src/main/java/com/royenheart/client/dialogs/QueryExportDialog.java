package com.royenheart.client.dialogs;

import com.royenheart.basicsets.programsettings.UserPattern;
import com.royenheart.client.Connection;
import com.royenheart.client.FilesOperation;
import com.royenheart.client.components.ConditionComboBox;
import com.royenheart.client.components.NotNullTextField;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 查询特定信息，服务器将查询信息传回客户端
 * @author RoyenHeart
 */
public class QueryExportDialog extends JDialog implements Runnable, ActionListener {

    Thread t1 = null;
    private File file;
    private StringBuilder request;

    private final NotNullTextField ageText;
    private final NotNullTextField sexText;
    private final NotNullTextField nameText;
    private final NotNullTextField phoneText;
    private final NotNullTextField moneyText;
    private final NotNullTextField birthText;
    private final NotNullTextField personalText;
    private final NotNullTextField heirText;
    private final JButton chooseButton;
    private final JButton sendButton;
    private final JTextArea showResult;
    private final JFrame jf;

    String sexInfo, ageInfo, nameInfo, phoneIdInfo, moneyInfo, birthInfo, personalIdInfo, heirInfo;
    String sexCon, ageCon, nameCon, phoneIdCon, moneyCon, birthCon, personalIdCon, heirCon;

    private final JRadioButton selAge;
    private final JRadioButton selSex;
    private final JRadioButton selName;
    private final JRadioButton selPhone;
    private final JRadioButton selMoney;
    private final JRadioButton selBirth;
    private final JRadioButton selPersonal;
    private final JRadioButton selHeir;

    private final ConditionComboBox conAge;
    private final ConditionComboBox conSex;
    private final ConditionComboBox conName;
    private final ConditionComboBox conPhone;
    private final ConditionComboBox conMoney;
    private final ConditionComboBox conBirth;
    private final ConditionComboBox conPersonal;
    private final ConditionComboBox conHeir;

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
        ageText = new NotNullTextField(12);

        conAge = new ConditionComboBox();

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
        sexText = new NotNullTextField(12);

        conSex = new ConditionComboBox();

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
        nameText = new NotNullTextField(12);

        conName = new ConditionComboBox();

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
        phoneText = new NotNullTextField(12);

        conPhone = new ConditionComboBox();

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
        moneyText = new NotNullTextField(12);

        conMoney = new ConditionComboBox();

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
        birthText = new NotNullTextField(12);

        conBirth = new ConditionComboBox();

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
        personalText = new NotNullTextField(12);

        conPersonal = new ConditionComboBox();

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
        heirText = new NotNullTextField(12);

        conHeir = new ConditionComboBox();

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

            try {
                ageCon = Objects.requireNonNull(conAge.getSelectedItem()).toString();
                sexCon = Objects.requireNonNull(conSex.getSelectedItem()).toString();
                nameCon = Objects.requireNonNull(conName.getSelectedItem()).toString();
                phoneIdCon = Objects.requireNonNull(conPhone.getSelectedItem()).toString();
                moneyCon = Objects.requireNonNull(conMoney.getSelectedItem()).toString();
                birthCon = Objects.requireNonNull(conBirth.getSelectedItem()).toString();
                personalIdCon = Objects.requireNonNull(conPersonal.getSelectedItem()).toString();
                heirCon = Objects.requireNonNull(conHeir.getSelectedItem()).toString();
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(null, "请在条件判断下拉框中至少选择一个");
                ex.printStackTrace();
            }

            try {
                ageInfo = (selAge.isSelected())?ageText.getTextLegal(String.valueOf(UserPattern.age)).trim():
                        ageText.getText().trim();
                sexInfo = (selSex.isSelected())?sexText.getTextLegal(String.valueOf(UserPattern.sex)).trim():
                        sexText.getText().trim();
                nameInfo = (selName.isSelected())?nameText.getTextLegal(String.valueOf(UserPattern.name)).trim():
                        nameText.getText().trim();
                phoneIdInfo = (selPhone.isSelected())?phoneText.getTextLegal(String.valueOf(UserPattern.phone)).trim():
                        phoneText.getText().trim();
                moneyInfo = (selMoney.isSelected())?moneyText.getTextLegal(String.valueOf(UserPattern.money)).trim():
                        moneyText.getText().trim();
                birthInfo = (selBirth.isSelected())?birthText.getTextLegal(String.valueOf(UserPattern.birth)).trim():
                        birthText.getText().trim();
                personalIdInfo = (selPersonal.isSelected())?
                        personalText.getTextLegal(String.valueOf(UserPattern.personalId)).trim():
                        personalText.getText().trim();
                heirInfo = (selHeir.isSelected())?heirText.getTextLegal(String.valueOf(UserPattern.heir)).trim():
                        heirText.getText().trim();
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
                return;
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException ex) {
                JOptionPane.showMessageDialog(null, "模式不匹配，无法进行合法性检查");
                return;
            }

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
        } catch (IOException e) {
            showResult.append("数据导出失败，请检查接收文件是否被其他应用占用\n");
            e.printStackTrace();
        }
    }
}
