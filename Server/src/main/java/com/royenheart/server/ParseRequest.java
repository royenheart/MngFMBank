package com.royenheart.server;

import java.util.LinkedList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析客户端请求
 * @author RoyenHeart
 */
public class ParseRequest {

    /**
     * 匹配请求中间以%分割的部分
     */
    private static final String REG_CONTENT = "(?<=%).*(?=%)";
    /**
     * 提取请求前段表示功能的字符串
     */
    private static final String REG_FUNC = "^[a-zA-Z]+(?=%)";
    private static final String REG_NAME = "name:[^;\\f\\n\\r\\t]+;";
    private static final String REG_AGE = "age:[0-9]+;";
    private static final String REG_SEX_BOOLEAN = "(sex:true;)|(sex:false;)";
    private static final String REG_SEX_STRING = "(sex:m;)|(sex:f;)";
    private static final String REG_PASSWD = "password:[^;\\f\\n\\r\\t]+;";
    private static final String REG_PHONE = "phone:[0-9]{11};";
    private static final String REG_MONEY = "(money:[0-9]+;)|(money:[0-9]+\\.[0-9]+;)";
    private static final String REG_DEATH = "(death:true;)|(death:false;)";
    private static final String REG_BIRTH = "birth:[0-9]+-[0-9]+-[0-9]+;";
    private static final String REG_ACCOUNTID = "accountId:[0-9]{10};";
    private static final String REG_PERSONALID = "personalId:[0-9]{12};";
    private static final String REG_HEIR = "heir:[0-9]{10};";
    private static final String REG_TABLE = "table:[^;\\f\\n\\r\\t]+;";
    /**
     * 匹配字符串中的字节数组
     */
    private static final String REG_BYTES = "\\[([-]?[0-9]+,?)+]";
    private static final String REG_VALUE = "(?<=:).*(?=;)";

    // 带条件判断的键值对

    private static final String REG_AGE_CONDITION = "age:[^;\\f\\n\\r\\t]+[0-9]+;";
    private static final String REG_SEX_BOOLEAN_CONDITION =
            "(sex:[^;\\f\\n\\r\\t]+true;)|(sex:[^;\\f\\n\\r\\t]+false;)";
    private static final String REG_SEX_STRING_CONDITION = "(sex:[^;\\f\\n\\r\\t]+m;)|(sex:[^;\\f\\n\\r\\t]+f;)";
    private static final String REG_PHONE_CONDITION = "phone:[^;\\f\\n\\r\\t]+[0-9]{11};";
    private static final String REG_MONEY_CONDITION =
            "(money:[^;\\f\\n\\r\\t]+[0-9]+;)|(money:[^;\\f\\n\\r\\t]+[0-9]+\\.[0-9]+;)";
    private static final String REG_DEATH_CONDITION =
            "(death:[^;\\f\\n\\r\\t]+true;)|(death:[^;\\f\\n\\rt\\t]+false;)";
    private static final String REG_BIRTH_CONDITION = "birth:[^;\\f\\n\\r\\t]+[0-9]+-[0-9]+-[0-9]+;";
    private static final String REG_ACCOUNTID_CONDITION = "accountId:[^;\\f\\n\\r\\t]+[0-9]{10};";
    private static final String REG_PERSONALID_CONDITION = "personalId:[^;\\f\\n\\r\\t]+[0-9]{12};";
    private static final String REG_HEIR_CONDITION = "heir:[^;\\f\\n\\r\\t]+[0-9]{10};";

    private final boolean legal;
    private String content;
    private final String request;

    public ParseRequest(String request) {
        this.request = request;

        Pattern r = Pattern.compile(REG_CONTENT, Pattern.CASE_INSENSITIVE);
        Matcher m = r.matcher(request);

        if (m.find()) {
            content = m.group();
            legal = true;
        } else {
            legal = false;
            System.err.println("请求非法！无主体内容");
        }
    }

    /**
     * 判断字符串是否合法
     * @return 字符串合法标识
     */
    public boolean getLegal() {
        return legal;
    }

    /**
     * 获取请求中指定的数据表
     * @return 返回请求的数据表
     */
    public String getRegTable() {
        return getRegV(REG_TABLE, "table");
    }

    public String getRegName() {
        return getRegV(REG_NAME, "name");
    }

    public String getRegAge() {
        return getRegV(REG_AGE, "age");
    }

    public String getRegSexBoolean() {
        return getRegV(REG_SEX_BOOLEAN, "sex");
    }

    public String getRegSexString() {
        return getRegV(REG_SEX_STRING, "sex");
    }

    public String getRegPasswd() {
        return getRegV(REG_PASSWD, "password");
    }

    public String getRegPhone() {
        return getRegV(REG_PHONE, "phone");
    }

    public String getRegMoney() {
        return getRegV(REG_MONEY, "money");
    }

    /**
     * 获取字段中用户存活状态，默认为存活
     * @return true/false自动转译成1/0
     */
    public String getRegDeath() {
        String result = getRegV(REG_DEATH, "death");
        return "true".equalsIgnoreCase(result)?"1":"0";
    }

    public String getRegBirth() {
        return getRegV(REG_BIRTH, "birth");
    }

    public String getRegAccountId() {
        return getRegV(REG_ACCOUNTID, "accountId");
    }

    public String getRegPersonalId() {
        return getRegV(REG_PERSONALID, "personalId");
    }

    public String getRegHeir() {
        return getRegV(REG_HEIR, "heir");
    }

    public String getRegAgeCondition() {
        return getRegV(REG_AGE_CONDITION, "age");
    }

    public String getRegSexBooleanCondition() {
        return getRegV(REG_SEX_BOOLEAN_CONDITION, "sex");
    }

    public String getRegSexStringCondition() {
        return getRegV(REG_SEX_STRING_CONDITION, "sex");
    }

    public String getRegPhoneCondition() {
        return getRegV(REG_PHONE_CONDITION, "phone");
    }

    public String getRegMoneyCondition() {
        return getRegV(REG_MONEY_CONDITION, "money");
    }

    public String getRegDeathCondition() {
        return getRegV(REG_DEATH_CONDITION, "death");
    }

    public String getRegBirthCondition() {
        return getRegV(REG_BIRTH_CONDITION, "birth");
    }

    public String getRegAccountIdCondition() {
        return getRegV(REG_ACCOUNTID_CONDITION, "accountId");
    }

    public String getRegPersonalIdCondition() {
        return getRegV(REG_PERSONALID_CONDITION, "personalId");
    }

    public String getRegHeirCondition() {
        return getRegV(REG_HEIR_CONDITION, "heir");
    }

    /**
     * 匹配多个accountId，用于转账，第一个匹配到的为转出人
     * @return 以LinkedList存储的accountId
     */
    public LinkedList<String> getRegMulAccountId() {
        Pattern r = Pattern.compile(REG_ACCOUNTID, Pattern.CASE_INSENSITIVE);
        Matcher m = r.matcher(content);
        LinkedList<String> accountIds = new LinkedList<>();

        int mStart = 0;
        while (m.find(mStart)) {
            mStart = m.end();
            accountIds.add(getRegV(m.group()));
        }

        if (mStart == 0) {
            System.err.println("字段accountid未找到");
        }

        return accountIds;
    }

    /**
     * 匹配多个密码，用于修改密码
     * @return 以LinkedList存储的password
     */
    public LinkedList<String> getRegMulPassword() {
        Pattern r = Pattern.compile(REG_PASSWD, Pattern.CASE_INSENSITIVE);
        Matcher m = r.matcher(content);
        LinkedList<String> accountIds = new LinkedList<>();

        int mStart = 0;
        while (m.find(mStart)) {
            mStart = m.end();
            accountIds.add(getRegV(m.group()));
        }

        if (mStart == 0) {
            System.err.println("字段password未找到");
        }

        return accountIds;
    }

    /**
     * 匹配字符串中多个字节数组，用于文件传输（以字节数组的形式）
     * @return Gson格式字节数组字符串数链表
     */
    public LinkedList<String> getRegMulBytes() {
        Pattern r = Pattern.compile(REG_BYTES, Pattern.CASE_INSENSITIVE);
        Matcher m = r.matcher(content);
        LinkedList<String> bytes = new LinkedList<>();

        int mStart = 0;
        while (m.find(mStart)) {
            mStart = m.end();
            bytes.add(m.group());
        }

        if (mStart == 0) {
            System.err.println("空文件");
        }

        return bytes;
    }

    public String getRegFunc() {
        Pattern r = Pattern.compile(REG_FUNC, Pattern.CASE_INSENSITIVE);
        Matcher m = r.matcher(request);

        if (m.find()) {
            return m.group();
        } else {
            System.err.println("Field Functions ID Not Found!");
            return "";
        }
    }

    private String getReg(String reg, String name) {
        Pattern r = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher m = r.matcher(content);

        if (m.find()) {
            return m.group();
        } else {
            System.err.println("字段 " + name + " 未找到");
            return "";
        }
    }

    /**
     * 获取字段-值键值对中的值
     * <p>
     *     先获取键值对，再获取值
     * </p>
     * @param reg 正则匹配式
     * @param name 字段名称
     * @return 返回匹配到的值
     */
    private String getRegV(String reg, String name) {
        String keyV = getReg(reg, name);

        if (keyV != null) {
            Pattern r = Pattern.compile(REG_VALUE, Pattern.CASE_INSENSITIVE);
            Matcher m = r.matcher(keyV);

            if (m.find()) {
                return m.group();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * 获取键值对中的值
     * <p>
     *     传入键值对字符串，直接获取值
     * </p>
     * @param str 键值对字符串
     * @return 返回匹配到的值
     */
    private String getRegV(String str) {
        Pattern r = Pattern.compile(REG_VALUE, Pattern.CASE_INSENSITIVE);
        Matcher m = r.matcher(str);

        if (m.find()) {
            return m.group();
        } else {
            return "";
        }
    }

    /**
     * 查看是否缺少用户全部所需字段中的任何一个或多个
     * @return 是否缺少
     */
    public boolean allPatterns() {
        return Objects.equals(getRegAccountId(), "") || Objects.equals(getRegMoney(), "") ||
                Objects.equals(getRegAge(), "") || Objects.equals(getRegSexString(), "") ||
                Objects.equals(getRegName(), "") || Objects.equals(getRegPasswd(), "") ||
                Objects.equals(getRegPhone(), "") || Objects.equals(getRegDeath(), "") ||
                Objects.equals(getRegBirth(), "") || Objects.equals(getRegPersonalId(), "") ||
                Objects.equals(getRegHeir(), "");
    }

}
