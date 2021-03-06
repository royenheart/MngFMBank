package com.royenheart.client;

import com.royenheart.basicsets.jsonsettings.ClientJsonReader;
import com.royenheart.basicsets.jsonsettings.ClientJsonWriter;
import com.royenheart.basicsets.programsettings.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 一个客户端对应一个连接
 * @author RoyenHeart
 * @author revrg
 * @author Tang-li-bian
 */
public class Connection {

    private static DataInputStream in;
    private static DataOutputStream out;
    private static Socket socket = new Socket();
    private static final Client CLIENT_SETS = new ClientJsonReader().getClientFromSets();
    /**
     * 是否以管理员模式进入
     */
    private static boolean useAdmin;
    private static Date planetTime;
    private static Date lastUpdateTime;
    private static final ScheduledExecutorService TIME_UPDATE_MISSION = Executors.newScheduledThreadPool(1);

    static {
        TIME_UPDATE_MISSION.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Connection.updateTime();
                            System.out.println("行星当前时间 : " + planetTime);
                        } catch (IOException | ParseException e) {
                            System.err.println("服务器时间无法同步");
                            e.printStackTrace();
                        }
                    }
                }, 0, 1, TimeUnit.MINUTES);
    }

    public static boolean getUseAdmin() {
        return useAdmin;
    }

    public static String getIp() {
        return CLIENT_SETS.getIp();
    }

    public static String getPort() {
        return CLIENT_SETS.getPort();
    }

    /**
     * 根据套接字地址连接服务器，同时指明是管理员模式还是普通用户
     * @param socketAddress 套接字地址
     * @param admin 是否以管理员模式登录
     */
    public static void socketConnect(InetSocketAddress socketAddress, boolean admin) {
        useAdmin = admin;
        try {
            if (socket.isConnected()) {
                System.err.println("重新进行连接");
                socket.close();
            }
            socket.connect(socketAddress);
            in = getInput();
            out = getOutput();
        } catch (IOException e) {
            System.err.println("连接IO错误");
            e.printStackTrace();
        }
    }

    /**
     * 向服务端发送请求更新时间
     * @throws IOException 流获取失败
     * @throws ParseException 时间格式解析错误
     */
    public static void updateTime() throws IOException, ParseException {
        if (socketConnectionStatus()) {
            writeUTF("Z%%");
            String date = readUTF();
            planetTime = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            lastUpdateTime = new Date();
        }
    }

    public static void writeUTF(String str) throws IOException {
        out.writeUTF(str);
    }

    public static String readUTF() throws IOException {
        return in.readUTF();
    }

    /**
     * 获取套接字输入流
     * @return 返回套接字输入流
     * @throws IOException 流获取失败
     */
    private static DataInputStream getInput() throws IOException {
        return new DataInputStream(socket.getInputStream());
    }

    /**
     * 获取套接字输出流
     * @return 返回套接字输出流
     * @throws IOException 流获取失败
     */
    private static DataOutputStream getOutput() throws IOException {
        return new DataOutputStream(socket.getOutputStream());
    }

    public static void storeClientSets() {
        new ClientJsonWriter().store(CLIENT_SETS);
    }

    /**
     * 查看socket的连接状态
     * @return socket是否已连接
     */
    public static boolean socketConnectionStatus() {
        return socket.isConnected();
    }

    public static void socketDisconnection() {
        try {
            in.close();
            out.close();
            socket.close();
            socket = new Socket();
        } catch (IOException e) {
            System.err.println("连接已丢失");
        }
    }

    /**
     * 获取服务器时间
     * @return 返回当前服务器时间(yyyy-MM-dd格式)
     */
    public static String getPlanetTime() {
        if (planetTime == null) {
            return "服务器时间未同步";
        } else {
            return new SimpleDateFormat("yyyy-MM-dd").format(planetTime);
        }
    }

    /**
     * 获取上一次同步的真实时间
     * @return 返回上一次同步服务器时间的现实时间（yyyy-MM-dd格式）
     */
    public static String getLastUpdateTime() {
        if (lastUpdateTime == null) {
            return "尚未同步过时间";
        } else {
            return lastUpdateTime.toString();
        }
    }

}
