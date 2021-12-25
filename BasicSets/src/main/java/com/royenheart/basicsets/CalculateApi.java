package com.royenheart.basicsets;

import com.royenheart.basicsets.programsettings.User;
import com.royenheart.basicsets.programsettings.UserPattern;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * 计算模块，用于服务端、客户端数据的计算
 * @author RoyenHeart
 */
public class CalculateApi {

    private static final HashMap<String, String> MOD_FUNC = new HashMap<>();

    static {
        MOD_FUNC.put(String.valueOf(UserPattern.accountId), "legalAccountId");
        MOD_FUNC.put(String.valueOf(UserPattern.personalId), "legalPersonalId");
        MOD_FUNC.put(String.valueOf(UserPattern.age), "legalAge");
        MOD_FUNC.put(String.valueOf(UserPattern.name), "legalName");
        MOD_FUNC.put(String.valueOf(UserPattern.sex), "legalSex");
        MOD_FUNC.put(String.valueOf(UserPattern.password), "legalPassword");
        MOD_FUNC.put(String.valueOf(UserPattern.heir), "legalHeir");
        MOD_FUNC.put(String.valueOf(UserPattern.money), "legalMoney");
        MOD_FUNC.put(String.valueOf(UserPattern.phone), "legalPhone");
        MOD_FUNC.put(String.valueOf(UserPattern.death), "legalDeath");
        MOD_FUNC.put(String.valueOf(UserPattern.birth), "legalBirth");
    }

    public CalculateApi() {}

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
     * 检查出生日期是否合法
     * @param birth 待检查的日期字符串（以yyyy-MM-dd）形式表达
     * @return 出生日期是否合法
     */
    public static boolean legalBirth(String birth) {
        try {
            new SimpleDateFormat("yyyy-MM-dd").parse(birth);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 检查性别字段是否合法
     * @param sex 待检测的性别字段
     * @return 性别字段是否合法
     */
    public static boolean legalSex(String sex) {
        return "f".equalsIgnoreCase(sex) || "m".equalsIgnoreCase(sex);
    }

    public static boolean legalDeath(String death) {
        return "false".equalsIgnoreCase(death) || "true".equalsIgnoreCase(death);
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
    public static boolean legalAge(String age) {
        int getAge = Integer.parseInt(age);
        return getAge <= User.MAX_AGE && getAge >= User.MIN_AGE;
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
    public static boolean legalMoney(String money) {
        if (!money.matches("(^[0-9]+$)|(^[0-9]+\\.[0-9]+$)")) {
            return false;
        }
        double getMoney = Double.parseDouble(money);
        return (getMoney >= User.MIN_MONEY) && (getMoney <= User.MAX_MONEY);
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
     * 检查继承人账户ID是否合法
     * @param heir 待检查的继承人账户ID
     * @return 继承人账户ID是否合法
     */
    public static boolean legalHeir(String heir) {
        return legalAccountId(heir);
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
    public static boolean legalPassword(String passwd) {
        int passwdLength = passwd.length();
        if (passwdLength < User.MIN_PASSWD || passwdLength > User.MAX_PASSWD ) {
            return false;
        } else {
            for (int i = 0; i < User.LIMITED_WORD.length; i++) {
                if (passwd.contains(String.valueOf(User.LIMITED_WORD[i]))) {
                    return false;
                }
            }
            // 密码不得是数字类型
            if (passwd.matches("(^[0-9]+$)|(^[0-9]+\\.[0-9]+$)")) {
                return false;
            }
            // 密码不得出现空白字符
            return passwd.matches("[^\f\n\r\t]+");
        }
    }

    /**
     * 通过反射调用静态类匹配对应模式对字符串做合法性检测
     * @param text 待匹配的文本
     * @param mod 需要对文本进行匹配的模式
     * @return 返回是否在对应模式下匹配成功
     * @throws NoSuchMethodException 无此方法，掷出
     * @throws InvocationTargetException 掷出
     * @throws IllegalAccessException 非法访问，掷出
     */
    public static boolean legalString(String text, String mod) throws
            NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String getMod = MOD_FUNC.get(mod);
        if (getMod == null) {
            throw new NullPointerException("模式未找到");
        }
        return (boolean) CalculateApi.class.getMethod(getMod, String.class).invoke(null, text);
    }

    /**
     * 根据给定星球数据计算计算利息
     * @return 计算出的利息
     */
    public static double interest(int wars, double ecoRate, double ecoBubble, double bankWill, double investFire) {
        return 1.0 * (255 - wars) / 300.0
                * (bankWill + 0.6)
                * (ecoRate + 0.5)
                * (1.0 - ecoBubble)
                * (100 - investFire)/150.0;
    }

}
