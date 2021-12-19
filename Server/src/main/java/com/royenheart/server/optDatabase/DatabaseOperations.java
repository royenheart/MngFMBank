package com.royenheart.server.optDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

/**
 * 数据库操作对象父类
 * @author RoyenHeart
 */
abstract public class DatabaseOperations {

    protected Connection con;
    /*各字段在数据库中的映射（考虑单独分出来放至settings.json中）*/
    public static HashMap<String, Integer> fieldMap = new HashMap<>();

    static {
        fieldMap.put("name", 1);
        fieldMap.put("age", 2);
        fieldMap.put("sex", 3);
        fieldMap.put("password", 4);
        fieldMap.put("phone", 5);
        fieldMap.put("money", 6);
        fieldMap.put("death", 7);
        fieldMap.put("birth", 8);
        fieldMap.put("accountId", 9);
        fieldMap.put("personalId", 10);
        fieldMap.put("heir", 11);
    }

    public DatabaseOperations(Connection con) {
        this.con = con;
    }

    /**
     * 转化数据库查询到的信息为以逗号和换行符号类型的字符串
     * @param rs 数据库查询
     * @return 转换信息后的字符串（字符串仍需进一步的转换）
     */
    abstract String returnData(ResultSet rs);

}
