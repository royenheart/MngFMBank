package com.royenheart.client;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * 柜台GUI窗口设置
 */
public class CounterFrame extends JFrame {

    private HashMap<JButton, String> leftButton;
    private HashMap<JButton, String> anotherButton;
    private Box leftBox;
    private Box form;
    private JTextArea info;
    private JLabel bottomInfo;

    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public CounterFrame() {
        // 初始化按钮
        initLeftButtons();

        // 添加左侧操作列表
        leftBox = Box.createVerticalBox();
        for (JButton jButton : leftButton.keySet()) {
            leftBox.add(jButton);
        }

        // 表单
        form = Box.createHorizontalBox();
        for (JButton jButton: anotherButton.keySet()) {
            form.add(jButton);
        }

        // 下方信息栏（登录信息+服务器状态）
        bottomInfo = new JLabel("服务器登录状态");

        // 信息栏
        info = new JTextArea(10,32);

        Container con = getContentPane();
        con.setLayout(new FlowLayout());

        con.add(leftBox);
        con.add(form);
        con.add(bottomInfo);
        con.add(info);

        // 设置属性并进行显示
        setLocationByPlatform(true);
        setSize(screenSize.width / 2, screenSize.height / 2);
        setTitle("FM Bank Systemc Counter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initLeftButtons() {
        leftButton = new HashMap<>();
        anotherButton = new HashMap<>();

        // left buttons initial
        leftButton.put(new JButton("查询余额"), String.valueOf(ButtonOperation.QueryBalance));
        leftButton.put(new JButton("提取现金"), String.valueOf(ButtonOperation.WithdrawMoney));
        leftButton.put(new JButton("存钱"), String.valueOf(ButtonOperation.Saving));
        leftButton.put(new JButton("转账"), String.valueOf(ButtonOperation.Transfer));
        leftButton.put(new JButton("修改用户信息"), String.valueOf(ButtonOperation.ModifyUserInfo));
        leftButton.put(new JButton("开户"), String.valueOf(ButtonOperation.CreateUser));
        leftButton.put(new JButton("销户"), String.valueOf(ButtonOperation.DelUser));
        leftButton.put(new JButton("导入xls批量开户"), String.valueOf(ButtonOperation.BatchCreateUser));
        leftButton.put(new JButton("查看年终报告"), String.valueOf(ButtonOperation.GetReport));
        leftButton.put(new JButton("发送邮件"), String.valueOf(ButtonOperation.SendMail));
        leftButton.put(new JButton("接收邮件"), String.valueOf(ButtonOperation.ReceiveMail));

        // another buttons initial

        anotherButton.put(new JButton("提交"), String.valueOf(ButtonOperation.SendRequest));
        anotherButton.put(new JButton("提交并将结果保存为xls文件"), String.valueOf(ButtonOperation.SendRequestStore));
    }
}
