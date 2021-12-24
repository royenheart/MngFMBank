package com.royenheart.server.atomics;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.royenheart.basicsets.programsettings.UserPattern;
import com.royenheart.server.ParseRequest;
import com.royenheart.server.databaseopt.DatabaseQuery;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 原子操作，以特定条件查询
 * @author RoyenHeart
 */
public class AtomicQueryAll extends AtomicOperations {

    private final Connection con;
    private final String tables;
    private final ParseRequest parseRequest;

    public AtomicQueryAll(Connection con, String tables, ParseRequest parseRequest) {
        this.con = con;
        this.tables = tables;
        this.parseRequest = parseRequest;
    }

    public LinkedList<HashMap<String, String>> queryOneForAll(String field) throws SQLException {
        Gson gson = new Gson();
        DatabaseQuery o1 = (DatabaseQuery) OPERATIONS.get("q");
        String r1 = o1.executeSqlOneForAll(con, tables, field);
        return gson.fromJson(r1, new TypeToken<LinkedList<HashMap<String, String>>>() {}.getType());
    }

    /**
     * 查询全部信息
     * @param link 查询表达式的连接符号
     * @return 查询结果数组
     * @throws SQLException 数据库请求错误
     */
    public LinkedList<HashMap<String, String>> query(String link) throws SQLException {
        Gson gson = new Gson();
        DatabaseQuery o1 = (DatabaseQuery) OPERATIONS.get("q");
        String r1 = o1.executeSqlWithConditions(con, tables, new LinkedList<String>() {
            {
                this.add(String.valueOf(UserPattern.accountId));
                this.add(String.valueOf(UserPattern.personalId));
                this.add(String.valueOf(UserPattern.name));
                this.add(String.valueOf(UserPattern.age));
                this.add(String.valueOf(UserPattern.sex));
                this.add(String.valueOf(UserPattern.password));
                this.add(String.valueOf(UserPattern.phone));
                this.add(String.valueOf(UserPattern.money));
                this.add(String.valueOf(UserPattern.death));
                this.add(String.valueOf(UserPattern.birth));
                this.add(String.valueOf(UserPattern.heir));
            }
        }, new HashMap<String, String>() {
            {
                this.put(String.valueOf(UserPattern.accountId), parseRequest.getRegAccountIdCondition());
                this.put(String.valueOf(UserPattern.personalId), parseRequest.getRegPersonalIdCondition());
                this.put(String.valueOf(UserPattern.name), parseRequest.getRegName());
                this.put(String.valueOf(UserPattern.age), parseRequest.getRegAgeCondition());
                this.put(String.valueOf(UserPattern.sex), parseRequest.getRegSexStringCondition());
                this.put(String.valueOf(UserPattern.password), parseRequest.getRegPasswd());
                this.put(String.valueOf(UserPattern.phone), parseRequest.getRegPhoneCondition());
                this.put(String.valueOf(UserPattern.money), parseRequest.getRegMoneyCondition());
                this.put(String.valueOf(UserPattern.death), parseRequest.getRegDeathCondition());
                this.put(String.valueOf(UserPattern.birth), parseRequest.getRegBirthCondition());
                this.put(String.valueOf(UserPattern.heir), parseRequest.getRegHeirCondition());
            }
        }, link);
        return gson.fromJson(r1, new TypeToken<LinkedList<HashMap<String, String>>>() {}.getType());
    }

    /**
     * 查询年龄和死亡状态
     * @param link 查询表达式的连接符号
     * @return 查询结果数组
     * @throws SQLException 数据库请求错误
     */
    public LinkedList<HashMap<String, String>> queryAgeAndDeath(String link) throws SQLException {
        Gson gson = new Gson();
        DatabaseQuery o1 = (DatabaseQuery) OPERATIONS.get("q");
        String r1 = o1.executeSqlWithConditions(con, tables, new LinkedList<String>() {
            {
                this.add(String.valueOf(UserPattern.accountId));
                this.add(String.valueOf(UserPattern.name));
                this.add(String.valueOf(UserPattern.money));
                this.add(String.valueOf(UserPattern.heir));
                this.add(String.valueOf(UserPattern.password));
            }
        }, new HashMap<String, String>() {
            {
                this.put(String.valueOf(UserPattern.age), parseRequest.getRegAgeCondition());
                this.put(String.valueOf(UserPattern.death), parseRequest.getRegDeathCondition());
            }
        }, link);
        return gson.fromJson(r1, new TypeToken<LinkedList<HashMap<String, String>>>() {}.getType());
    }

}
