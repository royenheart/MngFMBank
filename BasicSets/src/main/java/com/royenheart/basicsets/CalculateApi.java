package com.royenheart.basicsets;

import com.royenheart.basicsets.programsettings.User;

import java.util.Calendar;
import java.util.Date;

/**
 * 计算模块，用于服务端数据的计算
 * @author RoyenHeart
 */
public class CalculateApi {

    /**
     * 计算起始日期和终止日期的间隔年份
     * <p>
     *     服务器更新每个人年龄数据时，起始日期为生日，终止日期为星球当前时间
     * </p>
     * @author RoyenHeart
     * @param s 起始时间
     * @param t 终止时间
     * @return 间隔年份
     */
    public static int dateInterval(Date s, Date t) {
        int interval;
        int isReversed = 1;

        if (s.after(t)) {
            Date tmp = t;
            t = s;
            s = tmp;
            isReversed = -1;
        }

        Calendar sc, tc;
        sc = Calendar.getInstance();
        sc.setTime(s);
        tc = Calendar.getInstance();
        tc.setTime(t);

        int sYear = sc.get(Calendar.YEAR);
        int sMonth = sc.get(Calendar.MONTH);
        int tYear = tc.get(Calendar.YEAR);
        int tMonth = tc.get(Calendar.MONTH);

        interval = (tMonth < sMonth)?tYear-sYear-1:tYear-sYear;

        return isReversed*interval;
    }

    /**
     * 检查电话号码是否符合规范
     * @param phone 待检测的电话号码
     * @return 电话号码是否正确
     */
    public static boolean legalPhone(String phone) {
        if (phone.length() != User.PHONE_LEN) {
            return false;
        } else { return phone.matches("^[1][0-9]{10}$"); }
    }

    /**
     * 检查年龄是否符合规范
     * @param age 待检测的年龄
     * @return 年龄是否符合要求
     */
    public static boolean legalAge(int age) {
        return age <= User.MAX_AGE && age >= User.MIN_AGE;
    }

    /**
     * 检查名字是否符合要求
     * @param name 待检查的名字
     * @return 名字是否符合要求
     */
    public static boolean legalName(String name) {
        if (name.length() < User.MIN_NAME || name.length() > User.MAX_NAME) {
            return false;
        } else {
            for (int i = 0; i < User.LIMITED_WORD.length; i++) {
                if (name.contains(String.valueOf(User.LIMITED_WORD[i]))) {
                    return false;
                }
            }
            // 名字不得出现空白字符
            return name.matches("[^\f\n\r\t]+");
        }
    }

    /**
     * 检查钱数是否符合
     * @param money 待检查的钱数
     * @return 钱数是否符合要求
     */
    public static boolean legalMoney(double money) {
        return (money >= User.MIN_MONEY) && (money <= User.MAX_MONEY);
    }

    /**
     * 检查账户ID是否符合
     * @param accountId 待检查的账户ID
     * @return 账户ID是否符合要求
     */
    public static boolean legalAccountId(String accountId) {
        if (accountId.length() != User.ACCOUNT_ID_LEN) {
            return false;
        } else { return accountId.matches("^[0-9]+$"); }
    }

    /**
     * 检查身份ID是否符合
     * @param personalId 待检查的身份ID
     * @return 身份ID是否符合要求
     */
    public static boolean legalPersonalId(String personalId) {
        if (personalId.length() != User.PERSONAL_ID_LEN) {
            return false;
        } else { return personalId.matches("^[0-9]+$"); }
    }

    /**
     * 检查密码是否符合
     * @param passwd 待检查的密码
     * @return 密码是否符合要求
     */
    public static boolean legalPasswd(String passwd) {
        int passwdLength = passwd.length();
        if (passwdLength < User.MIN_PASSWD || passwdLength > User.MAX_PASSWD ) {
            return false;
        } else {
            for (int i = 0; i < User.LIMITED_WORD.length; i++) {
                if (passwd.contains(String.valueOf(User.LIMITED_WORD[i]))) {
                    return false;
                }
            }
            // 密码不得出现空白字符
            return passwd.matches("[^\f\n\r\t]+");
        }
    }

}
