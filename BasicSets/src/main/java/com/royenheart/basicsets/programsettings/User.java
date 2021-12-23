package com.royenheart.basicsets.programsettings;

import com.royenheart.basicsets.CalculateApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 银行用户设置
 * <p>
 *     accountId为个人账户Id，只允许10位数字，0000000000编号保留用作银行本身
 * </p>
 * @author RoyenHeart
 */
public class User implements Cloneable {

    // Limitations

    public static final char[] LIMITED_WORD = {'%', ':', '%'};
    public static final int MAX_AGE = 69;
    public static final int MIN_AGE = 18;
    public static final int MIN_NAME = 1;
    public static final int MAX_NAME = 10;
    public static final int PHONE_LEN = 11;
    public static final double MIN_MONEY = Double.MIN_VALUE;
    public static final double MAX_MONEY = Double.MAX_VALUE;
    public static final int MIN_PASSWD = 4;
    public static final int MAX_PASSWD = 20;
    public static final int ACCOUNT_ID_LEN = 10;
    public static final int PERSONAL_ID_LEN = 12;
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // Attributes

    private int age;
    private final char sex;
    private String name;
    private String password;
    private String phone;
    private double money;
    private boolean death;
    private final Date birth;
    private final String personalId;
    private final String accountId;
    private String heir;

    public User(int age, char sex, String name, String password, String phone,
                double money, boolean death, Date birth, String personalId, String accountId, User heir) {
        this.age = age;
        this.sex = sex;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.money = money;
        this.death = death;
        this.birth = birth;
        this.personalId = personalId;
        this.accountId = accountId;
        this.heir = heir.getAccountId();
    }

    public User(int age, char sex, String name, String password, String phone,
                double money, boolean death, String birth, String personalId, String accountId, String heir)
            throws ParseException {
        this.age = age;
        this.sex = sex;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.money = money;
        this.death = death;
        this.birth = TIME_FORMAT.parse(birth);
        this.personalId = personalId;
        this.heir = heir;
        this.accountId = accountId;
    }

    public User(String age, String sex, String name, String password, String phone,
                String money, String death, String birth, String personalId, String accountId, String heir) throws ParseException {
        this.age = Integer.parseInt(age);
        this.sex = "F".equalsIgnoreCase(sex)?'F':'M';
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.money = Double.parseDouble(money);
        this.death = Boolean.parseBoolean(death);
        this.birth = TIME_FORMAT.parse(birth);
        this.personalId = personalId;
        this.heir = heir;
        this.accountId = accountId;
    }

    public int getAge() {
        return age;
    }

    public char getSex() {
        return sex;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Date getBirth() {
        return (Date) birth.clone();
    }

    public double getMoney() {
        return money;
    }

    public String getPersonalId() {
        return personalId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getPhone() {
        return phone;
    }

    public boolean getDeath() {
        return death;
    }

    public String getHeir() {
        return heir;
    }

    public void setPassword(String password) { this.password = password; }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setHeir(String heir) {
        this.heir = heir;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 更新年龄
     * @param currentTime 服务器（星球）当前时间
     * @return 计算后的年龄
     */
    public int updateAge(Date currentTime) {
        int age = CalculateApi.dateInterval(birth, currentTime);
        if (this.age < MIN_AGE) {
            return -1;
        } else if (this.age > MAX_AGE) {
            return 1;
        } else {
            this.age = age;
            return 0;
        }
    }

    public void setDeath(boolean death) {
        this.death = death;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    @Override
    public User clone() {
        try {
            return (User) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}
