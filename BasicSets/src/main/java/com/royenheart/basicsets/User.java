package com.royenheart.basicsets;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Bank User Specification
 * @author RoyenHeart
 */
public class User implements Cloneable {

    // Limitations

    public static final int MAX_AGE = 69;
    public static final int MIN_AGE = 18;
    public static final int MIN_NAME = 1;
    public static final int MAX_NAME = 10;
    public static final int PHONE_LEN = 11;
    public static final double MIN_MONEY = Double.MIN_VALUE;
    public static final double MAX_MONEY = Double.MAX_VALUE;
    public static final int MIN_PASSWD = 4;
    public static final int ACCOUNT_ID_LEN = 10;
    public static final int PERSONAL_ID_LEN = 12;
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // Attributes

    private int age;
    private char sex;
    private String name;
    private String password;
    private String phone;
    private double money;
    private boolean death;
    private Date birth;
    private String personalId;
    private String accountId;
    private User heir;

    public User(int age, char sex, String name, String password, String phone,
                double money, boolean death, Date birth, String personalId, User heir) {
        this.age = age;
        this.sex = sex;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.money = money;
        this.death = death;
        this.birth = birth;
        this.personalId = personalId;
        this.heir = heir;
    }

    public User(int age, char sex, String name, String password, String phone,
                double money, boolean death, String birth, String personalId, User heir) throws ParseException {
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

    public User getHeir() {
        return heir.clone();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int updateAge(Date currentTime) {
        this.age = CalculateApi.dateInterval(birth, currentTime);
        if (this.age < MIN_AGE) {
            return -1;
        } else if (this.age > MAX_AGE) {
            return 1;
        } else {
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
