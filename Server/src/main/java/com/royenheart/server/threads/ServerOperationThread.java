package com.royenheart.server.threads;

import com.royenheart.basicsets.programsettings.Planet;
import com.royenheart.basicsets.programsettings.Server;
import com.royenheart.server.DatabaseLink;
import com.royenheart.server.Functions;
import com.royenheart.server.ParseRequest;
import com.royenheart.server.ServerApp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 普通操作线程
 * @author RoyenHeart
 */
public class ServerOperationThread extends ServerThread implements Runnable {

    private DataOutputStream out;
    private DataInputStream in;
    private final Planet planetSets;
    private final InetAddress clientAddress;
    private boolean login;

    public ServerOperationThread(Socket socket, Server serverSets, Planet planetSets) {
        super(socket, serverSets);
        this.planetSets = planetSets;
        clientAddress = socket.getInetAddress();
        login = false;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println(socket.getLocalAddress() + ":客户连接失败，已断开连接");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // 客户端请求
        String request;

        while (ServerApp.getShutdown()) {
            try {
                // 不断接收请求，每一个请求进行操作
                request = in.readUTF();
                System.out.printf("接收到请求，从%s得到请求\n%s\n", clientAddress, request);

                // 解析请求字段
                ParseRequest parseMachine = new ParseRequest(request);
                if (parseMachine.getLegal()) {
                    System.out.println("解析请求成功");
                } else {
                    System.err.println("请求不符合规范，请求退回");
                    out.writeUTF("请求错误");
                    continue;
                }

                if ("Z".equals(parseMachine.getRegFunc())) {
                    System.out.println("时间同步");
                    out.writeUTF(planetSets.getPlanetTime());
                    continue;
                }

                // 登录检查
                if (!"X".equals(parseMachine.getRegFunc()) && !login) {
                    System.err.println("未登录，禁止操作");
                    out.writeUTF("您还未登录");
                    continue;
                }

                // 根据请求字段连接数据库
                Connection newCon = new DatabaseLink(serverSets).connectDb();
                if (newCon != null) {
                    System.out.println("数据库连接成功" + newCon.getMetaData());
                } else {
                    System.err.println("数据库连接失败，请求退回");
                    out.writeUTF("数据库发生错误，请联系系统管理员");
                    continue;
                }

                // 传入解析器，功能对象，数据库连接，数据表（默认为Users）
                String result = (String) FUNC.get(parseMachine.getRegFunc().toUpperCase()).invoke(Functions.getMe(),
                        parseMachine, newCon, "Users", planetSets);
                if (result != null) {
                    System.out.println("请求指定操作成功");
                } else {
                    System.err.println("请求指定操作失败，请求已退回");
                    out.writeUTF("请求拒绝");
                    continue;
                }
                if (Boolean.parseBoolean(result) && !login) {
                    login = true;
                }

                // 返回登录状态
                if ("true".equalsIgnoreCase(result)) {
                    result = "登录成功";
                } else if ("false".equalsIgnoreCase(result)) {
                    result = "登录失败";
                }

                // 返回数据
                out.writeUTF(result);
                System.out.println("数据返回成功");
                newCon.close();
            } catch (IOException e) {
                System.err.println("用户" + clientAddress + "断开连接");
                ServerApp.decConnect();
                break;
            } catch (SQLException e) {
                System.err.println("数据库访问异常");
                e.printStackTrace();
            } catch (InvocationTargetException | IllegalAccessException e) {
                System.err.println("未找到请求对应方法，请查看相关代码");
                e.printStackTrace();
            }
        }
    }
}
