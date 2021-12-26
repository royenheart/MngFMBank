package com.royenheart.basicsets.programsettings;

import com.google.gson.annotations.Expose;

/**
 * 服务器端设置模块
 * @author RoyenHeart
 */
public class Server {

    /**
     * 服务器ip地址
     */
    @Expose
    private String ip;
    /**
     * 服务器端口
     */
    @Expose
    private String port;
    /**
     * 数据库IP地址
     */
    @Expose
    private String databaseIp;
    /**
     * 数据库端口
     */
    @Expose
    private String databasePort;
    /**
     * 数据库用户
     */
    @Expose
    private String databaseUser;
    /**
     * 使用的数据库
     */
    @Expose
    private String database;
    /**
     * 数据库连接用户密码
     */
    @Expose
    private String databasePasswd;

    public Server(String ip, String port, String databaseUser, String database,
                  String databasePasswd, String databaseIp, String databasePort) {
        this.ip = ip;
        this.port = port;
        this.database = database;
        this.databaseUser = databaseUser;
        this.databasePasswd = databasePasswd;
        this.databaseIp = databaseIp;
        this.databasePort = databasePort;
    }

    public String getDatabaseIp() {
        return databaseIp;
    }

    public String getDatabasePort() {
        return databasePort;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setDatabaseIp(String databaseIp) {
        this.databaseIp = databaseIp;
    }

    public void setDatabasePort(String databasePort) {
        this.databasePort = databasePort;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getDatabase() {
        return database;
    }

    public String getDatabasePasswd() {
        return databasePasswd;
    }
}
