package com.royenheart.server;

import com.royenheart.basicsets.programsettings.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 服务端数据库连接操作
 * @author RoyenHeart
 */
public class DatabaseLink {

    private final Server database;

    public DatabaseLink(Server database) {
        this.database = database;
    }

    /**
     * 返回一个数据库连接（返回给其他对象进行后续操作）
     * @return 数据库连接
     */
    public Connection connectDb() {
        // 地址拓展url设置
        String pattern = database.getDatabase() + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String url = String.format("jdbc:mysql://%s:%s/%s", database.getDatabaseIp(),
                database.getDatabasePort(), pattern);
        Connection conn;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, database.getDatabaseUser(), database.getDatabasePasswd());
        } catch (SQLException e) {
            System.err.printf("数据连接发生错误\n%s", e.getMessage());
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            System.err.println("Mysql数据库驱动加载错误，请检查驱动是否已经安装");
            e.printStackTrace();
            return null;
        }

        return conn;
    }

}
