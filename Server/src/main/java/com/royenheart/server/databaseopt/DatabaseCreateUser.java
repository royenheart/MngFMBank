package com.royenheart.server.databaseopt;

import com.royenheart.basicsets.programsettings.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

/**
 * 数据库插入记录（开户操作）
 * @author RoyenHeart
 */
public class DatabaseCreateUser extends DatabaseOperations {

    private static final SimpleDateFormat FT = new SimpleDateFormat("yyyy-MM-dd");

    public DatabaseCreateUser() {}

    synchronized public boolean executeSql(Connection con, String tables, LinkedList<User> users)
            throws SQLException {
        this.con = con;

        StringBuilder sql = new StringBuilder(String.format("%s\n%s\n%s",
                "INSERT INTO " + tables,
                "(name, age, sex, password, phone, money, death, birth, accountId, personalId, heir)",
                "VALUES"));
        Statement stmt = con.createStatement();
        for (User user : users) {
            sql.append(String.format("\n(\"%s\",%s,%s,\"%s\",\"%s\",%s,%s,\"%s\",\"%s\",\"%s\",\"%s\")",
                    user.getName(), user.getAge(), (user.getSex()=='F')?"FALSE":"TRUE", user.getPassword(),
                    user.getPhone(), user.getMoney(), user.getDeath(), FT.format(user.getBirth()), user.getAccountId(),
                    user.getPersonalId(), user.getHeir()));
        }
        sql.append(";");

        int success = stmt.executeUpdate(String.valueOf(sql));
        stmt.close();
        return (success >= 1)?Boolean.TRUE:Boolean.FALSE;
    }

}
