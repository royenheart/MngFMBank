package com.royenheart.basicsets;

import com.google.gson.annotations.Expose;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 服务器端设置模块
 * @author RoyenHeart
 */
public class Server {

    @Expose
    private String ip;
    @Expose
    private String port;
    @Expose
    private String databaseUser;
    @Expose
    private String database;
    @Expose
    private String databasePasswd;

    public Server(String ip, String port, String databaseUser, String database, String databasePasswd) {
        this.ip = ip;
        this.port = port;
        this.database = database;
        this.databaseUser = databaseUser;
        this.databasePasswd = databasePasswd;
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
