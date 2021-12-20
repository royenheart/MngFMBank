package com.royenheart.server.threads;

import com.royenheart.server.Functions;
import com.royenheart.server.ParseRequest;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.HashMap;

/**
 * 服务器线程根类
 * @author RoyenHeart
 */
abstract public class ServerThread {

    protected static final HashMap<String, Method> FUNC =  new HashMap<>();

    static {
        try {
            FUNC.put("A", Functions.class.getMethod("queryMoney", ParseRequest.class, Connection.class, String.class));
            FUNC.put("B", Functions.class.getMethod("getMoney", ParseRequest.class, Connection.class, String.class));
            FUNC.put("C", Functions.class.getMethod("saveMoney", ParseRequest.class, Connection.class, String.class));
            FUNC.put("D", Functions.class.getMethod("transferMoney", ParseRequest.class, Connection.class, String.class));
            FUNC.put("E", Functions.class.getMethod("editUser", ParseRequest.class, Connection.class, String.class));
            FUNC.put("F", Functions.class.getMethod("addUser", ParseRequest.class, Connection.class, String.class));
            FUNC.put("G", Functions.class.getMethod("delUser", ParseRequest.class, Connection.class, String.class));
        } catch (NoSuchMethodException e) {
            System.err.println("无对应方法，请检查对应语句");
            e.printStackTrace();
        }
    }

}
