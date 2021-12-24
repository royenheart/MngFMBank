package com.royenheart.client;

import com.royenheart.basicsets.jsonsettings.ClientJsonReader;
import com.royenheart.basicsets.programsettings.Client;
import com.royenheart.basicsets.programsettings.Planet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 一个客户端对应一个连接
 * @author RoyenHeart
 * @author revrg
 * @author Tang-li-bian
 */
public class Connection {

    private static DataInputStream in;
    private static DataOutputStream out;
    private static final Socket socket = new Socket();
    private static final Client clientSets = new ClientJsonReader().getClientFromSets();

    public static String getIp() {
        return clientSets.getIp();
    }

    public static String getPort() {
        return clientSets.getPort();
    }

    public static void socketConnect(InetSocketAddress socketAddress) {
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

    public static void writeUTF(String str) throws IOException {
        out.writeUTF(str);
    }

    public static String readUTF() throws IOException {
        return in.readUTF();
    }

    private static DataInputStream getInput() throws IOException {
        return new DataInputStream(socket.getInputStream());
    }

    private static DataOutputStream getOutput() throws IOException {
        return new DataOutputStream(socket.getOutputStream());
    }

    public static boolean socketConnectionStatus() {
        return socket.isConnected();
    }

}
