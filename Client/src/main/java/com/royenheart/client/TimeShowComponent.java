package com.royenheart.client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;

/**
 * 显示时间
 * @author RoyenHeart
 */
public class TimeShowComponent extends Box {
    JFrame jf;

    public TimeShowComponent(JFrame jf){
        super(BoxLayout.Y_AXIS);

        // 主界面设置
        this.jf = jf;
        JLabel planetTime = new JLabel("最近一次查询的服务器星球时间: " + Connection.getPlanetTime());
        JLabel lastUpdateTime = new JLabel("上一次查询的现实时间: " + Connection.getLastUpdateTime());

        // 添加按钮组
        JPanel btnJp = new JPanel();
        btnJp.setMaximumSize(new Dimension(450,500));
        btnJp.setLayout(new GridLayout(10,5));

        // 添加窗口选择按钮
        JButton queryTime = new JButton("QueryPlanetTime");

        // 添加按钮监听器，点击时弹出对应窗口
        queryTime.addActionListener(e -> {
            try {
                Connection.updateTime();
                planetTime.setText("最近一次查询的服务器星球时间: " + Connection.getPlanetTime());
                lastUpdateTime.setText("上一次查询的现实时间: " + Connection.getLastUpdateTime());
            } catch (IOException | ParseException ex) {
                System.err.println("服务器时间无法同步");
                ex.printStackTrace();
            }
        });

        btnJp.add(queryTime);

        this.add(btnJp);
        this.add(planetTime);
        this.add(lastUpdateTime);

    }

}
