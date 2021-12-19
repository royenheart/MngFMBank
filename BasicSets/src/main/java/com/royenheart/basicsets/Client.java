package com.royenheart.basicsets;

import com.google.gson.annotations.Expose;

/**
 * 客户端设置类，用于存储客户端的一些设置
 * @author RoyenHeart
 */
public class Client {

    @Expose
    private String ip;
    @Expose
    private String port;

    public Client(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    public String getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
