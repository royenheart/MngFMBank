package com.royenheart.client;

import com.royenheart.client.dialogs.*;

import javax.swing.*;
import java.awt.*;

/**
 * 管理员功能菜单
 * @author revrg
 * @author Tang-li-bian
 */
public class AdminOperateComponent extends Box {
    JFrame jf;

    public AdminOperateComponent(JFrame jf){
        super(BoxLayout.Y_AXIS);

        // 主界面设置
        this.jf = jf;
        JPanel btnJp = new JPanel();
        btnJp.setMaximumSize(new Dimension(450,500));
        btnJp.setLayout(new GridLayout(10,5));

        JButton xlsCreateButton = new JButton("UploadXlsCreateUser");
        JButton queryXlsButton = new JButton("QueryInfoExportXls");
        JButton reportButton = new JButton("GetYearlyReport");
        JButton openButton = new JButton("OpenAccount");
        JButton deleteButton = new JButton("DeleteAccount");

        openButton.addActionListener(e -> new OpenAccountDialog(jf,"Open Account",true).setVisible(true));
        deleteButton.addActionListener(e -> new DeleteAccountDialog(jf,"Delete Account",true).setVisible(true));
        xlsCreateButton.addActionListener(
                e -> new UploadXlsCreateDialog(jf, "Upload Xls file to Create Users Once", true).setVisible(true));
        queryXlsButton.addActionListener(
                e -> new QueryExportDialog(jf, "Query info and then export Xls file", true).setVisible(true));
        reportButton.addActionListener(
                e -> new GetYearlyReportDialog(jf, "Get Yearly Report", true).setVisible(true));

        btnJp.add(openButton);
        btnJp.add(deleteButton);
        btnJp.add(xlsCreateButton);
        btnJp.add(queryXlsButton);
        btnJp.add(reportButton);

        this.add(btnJp);
    }
}
