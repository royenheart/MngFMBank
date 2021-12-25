package com.royenheart.client.components;

import com.royenheart.basicsets.CalculateApi;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * 返回密码框内内容，若是返回内容为空或者null，则抛出错误
 * @author RoyenHeart
 */
public class NotNullPasswordField extends JPasswordField {

    public NotNullPasswordField(int columns) {
        super(columns);
    }

    /**
     * 获取与给定检查模式符合的合法密码
     * @return 与给定检查模式匹配的合法密码
     */
    public char[] getPasswordLegal() throws
            InvocationTargetException, NoSuchMethodException, IllegalAccessException, NullPointerException {
        char[] result = this.getPassword();
        boolean success1, success2;
        success1 = getPasswordNotNull(result);
        success2 = CalculateApi.legalString(new String(result).trim(), "password");
        if (success1 && success2) {
            return result.clone();
        } else {
            throw new NullPointerException("密码字段非法");
        }
    }

    public boolean getPasswordNotNull(char[] result) throws NullPointerException {
        if (result == null || result.length == 0 || new String(result).trim().matches("[\\s]*")) {
            throw new NullPointerException("密码为空");
        }
        return true;
    }

}
