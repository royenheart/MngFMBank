package com.royenheart.server;

import com.royenheart.basicsets.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 服务端数据库连接操作
 * @author RoyenHeart
 */
public class DatabaseLink {

    private Server database;

    public DatabaseLink(Server database) {
        this.database = database;
    }

    /**
     * 返回一个数据库连接（返回给其他对象进行后续操作）
     * @return 数据库连接
     */
    public Connection connectDb() {
        // 地址拓展url设置
        String pattern = "test_demo?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String url = String.format("jdbc:mysql://%s:%s/%s", database.getIp(), database.getPort(), pattern);
        Connection conn;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, database.getDatabaseUser(), database.getDatabasePasswd());
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("发生错误"+e.getMessage());
            e.printStackTrace();
            return null;
        }
        return conn;
    }

}
