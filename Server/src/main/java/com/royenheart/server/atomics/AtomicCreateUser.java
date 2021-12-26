package com.royenheart.server.atomics;

import com.royenheart.basicsets.programsettings.User;
import com.royenheart.server.databaseopt.DatabaseCreateUser;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * 原子操作，创建用户或多个用户
 * @author RoyenHeart
 */
public class AtomicCreateUser extends AtomicOperations {

    private final Connection con;
    private final String tables;
    private final LinkedList<User> users;
    private final User user;

    /**
     * 创建操作，用于创建单个用户
     * @param con 数据库连接
     * @param tables 需要插入的数据表
     * @param user 创建的用户
     */
    public AtomicCreateUser(Connection con, String tables, User user) {
        this.con = con;
        this.tables = tables;
        this.user = user;
        this.users = new LinkedList<User>(){
            {
                this.add(user);
            }
        };
    }

    /**
     * 创建操作，用于创建多个用户
     * @param con 数据库连接
     * @param tables 需要插入的数据表
     * @param users 需要创建的用户列表
     */
    public AtomicCreateUser(Connection con, String tables, LinkedList<User> users) {
        this.con = con;
        this.tables = tables;
        this.users = users;
        this.user = users.get(0);
    }

    /**
     * 添加单个用户
     * @return 是否添加成功
     * @throws SQLException 数据库请求错误
     */
    public boolean createSingle() throws SQLException {
        DatabaseCreateUser o1 = (DatabaseCreateUser) OPERATIONS.get("cu");
        return o1.executeSql(con, tables, new LinkedList<User>(){
            {
                this.add(user);
            }
        });
    }

    /**
     * 根据用户列表创建多个用户
     * @return 是否创建成功
     * @throws SQLException 数据库请求错误
     */
    public boolean createMul() throws SQLException {
        DatabaseCreateUser o1 = (DatabaseCreateUser) OPERATIONS.get("cu");
        return o1.executeSql(con, tables, users);
    }

}
