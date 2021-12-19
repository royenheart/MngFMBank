package com.royenheart.server.optDatabase;

import com.royenheart.basicsets.User;

import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * 数据库插入记录（开户操作）
 * @author RoyenHeart
 */
public class DatabaseCreateUser extends DatabaseOperations {

    private static final SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
    private User user;
    private String database;

    public DatabaseCreateUser(Connection con, User user, String database) {
        super(con);
        this.user = user;
        this.database = database;
    }

    synchronized public String executeSql() throws SQLException {
        Statement stmt = con.createStatement();
        String sql = String.format("%s\n%s\n%s\n(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s);",
                "INSERT INTO " + database,
                "(name, age, sex, password, phone, money, death, birth, accountId, personalId, heir)",
                "VALUES",
                user.getName(), user.getAge(), (user.getSex()=='F')?"FALSE":"TRUE", user.getPassword(),
                user.getPhone(), user.getMoney(), user.getDeath(), ft.format(user.getBirth()), user.getAccountId(),
                user.getPersonalId(), user.getHeir().getAccountId());
        ResultSet rs = stmt.executeQuery(sql);
        String data = returnData(rs);
        stmt.close();
        return data;
    }

    @Override
    String returnData(ResultSet rs) {
        String result;
        try {
            result = rs.toString();
            rs.close();
        } catch (SQLException e) {
            System.err.println("发生错误，数据库连接异常" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return result;
    }

}
