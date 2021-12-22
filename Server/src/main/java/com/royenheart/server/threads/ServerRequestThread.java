package com.royenheart.server.threads;

import com.royenheart.basicsets.programsettings.Server;
import com.royenheart.server.ServerApp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 请求监听接收线程
 * @author RoyenHeart
 */
public class ServerRequestThread implements Runnable {

    private final ServerSocket server;
    private final Server serverSets;

    public ServerRequestThread(ServerSocket server, Server serverSets) {
        this.server = server;
        this.serverSets = serverSets;
    }

    @Override
    public void run() {
        while (ServerApp.getShutdown()) {
            Socket client = null;

            // 获取连接
            try {
                System.out.println("当前连接数" + ServerApp.getConnectNumber() + "，正在" + serverSets.getPort() + "端口进行监听");
                client = server.accept();
                System.out.println("用户" + client.getInetAddress() + "请求连接");
            } catch (IOException e) {
                System.err.println("用户连接异常，已中断");
                e.printStackTrace();
            }

            // 当监听到请求且正确接收后，为每一个请求（客户端）分配一个线程
            if (client != null) {
                ServerApp.plusConnect();
                ServerApp.executorOperationsSubmit(client, serverSets);
            }
        }
    }
}
