package com.royenheart.client.components;

import com.royenheart.basicsets.CalculateApi;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * 返回文本框内内容，若是返回内容为空或者null，则抛出错误
 * @author RoyenHeart
 */
public class NotNullTextField extends JTextField {

    public NotNullTextField(int columns) {
        super(columns);
    }

    /**
     * 获取与给定检查模式符合的合法文本
     * @param checkPattern 检查模式，传入CalculateApi进行对应的合法性判断
     * @return 与给定检查模式匹配的合法文本
     */
    public String getTextLegal(String checkPattern) throws
            InvocationTargetException, NoSuchMethodException, IllegalAccessException, NullPointerException {
        String result = this.getText();
        boolean success1, success2;
        success1 = getTextNotNull(result);
        success2 = CalculateApi.legalString(result, checkPattern);
        if (success1 && success2) {
            return result;
        } else {
            throw new NullPointerException("文本框字段非法");
        }
    }

    /**
     * 获取非空文本值
     * @return 非空文本值
     * @throws NullPointerException 当文本值为空时，掷出错误提示调用者
     */
    public boolean getTextNotNull(String result) throws NullPointerException {
        if (result == null || result.matches("[\\s]*")) {
            throw new NullPointerException("文本框字段为空");
        }
        return true;
    }

}