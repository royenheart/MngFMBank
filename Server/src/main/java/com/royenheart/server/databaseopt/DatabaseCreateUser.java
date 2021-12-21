package com.royenheart.server.databaseopt;

import com.royenheart.basicsets.User;

import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * 数据库插入记录（开户操作）
 * @author RoyenHeart
 */
public class DatabaseCreateUser extends DatabaseOperations {

    private static final SimpleDateFormat FT = new SimpleDateFormat("yyyy-MM-dd");

    public DatabaseCreateUser() {}

    synchronized public boolean executeSql(Connection con, User user, String tables) throws SQLException {
        this.con = con;

        Statement stmt = con.createStatement();
        String sql = String.format("%s\n%s\n%s\n(\"%s\",%s,%s,\"%s\",\"%s\",%s,%s,\"%s\",\"%s\",\"%s\",\"%s\");",
                "INSERT INTO " + tables,
                "(name, age, sex, password, phone, money, death, birth, accountId, personalId, heir)",
                "VALUES",
                user.getName(), user.getAge(), (user.getSex()=='F')?"FALSE":"TRUE", user.getPassword(),
                user.getPhone(), user.getMoney(), user.getDeath(), FT.format(user.getBirth()), user.getAccountId(),
                user.getPersonalId(), user.getHeir());

        int success = stmt.executeUpdate(sql);
        stmt.close();
        return (success >= 1)?Boolean.TRUE:Boolean.FALSE;
    }
}
