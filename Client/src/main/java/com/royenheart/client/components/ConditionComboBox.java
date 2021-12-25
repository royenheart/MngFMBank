package com.royenheart.client.components;

import javax.swing.*;

/**
 * 查询条件判断下拉框组件
 * @author RoyenHeart
 */
public class ConditionComboBox extends JComboBox<String> {

    public ConditionComboBox() {
        this.addItem("< ");
        this.addItem("= ");
        this.addItem("> ");
        this.addItem("<=");
        this.addItem(">=");
    }

}
