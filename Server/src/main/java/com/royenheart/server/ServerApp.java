package com.royenheart.server;

import com.royenheart.basicsets.Planet;
import com.royenheart.basicsets.Server;
import com.royenheart.basicsets.jsonsettings.PlanetJsonReader;
import com.royenheart.basicsets.jsonsettings.ServerJsonReader;
import com.royenheart.server.threads.NamedThreadFactory;
import com.royenheart.server.threads.ServerOperationThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 服务端Java程序主体
 * <p>
 *     主要负责（组织各模块，具体功能由模块实现）：
 *     1. 邮件系统管理
 *     2. 客户端访问多线程处理请求
 *     3. 多开辟一个线程运行星球的时间
 *     分配线程，管理资源，输出连接
 * </p>
 * @author RoyenHeart
 */
public class ServerApp {

    private static int connectNumber;

    public static void decConnect() {
        connectNumber -= 1;
    }

    public static void main(String[] args) {
        connectNumber = 0;
        ServerSocket server = null;
        Socket client = null;

        // 创建用于处理客户端发送的请求线程池
        ExecutorService operations = new
                ThreadPoolExecutor(0, 40, 60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new NamedThreadFactory("ClientOperationsRequest"));
        // 创建邮件系统有关的线程池
        ExecutorService mailSys = new
                ThreadPoolExecutor(0, 40, 60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new NamedThreadFactory("MailSystemRequest"));
        // 创建定时任务，负责星球时间的统计，并定时执行星球数据的刷新
        ScheduledExecutorService timer = Executors.newScheduledThreadPool(2);

        // 从设置文件内读入设置
        Server serverSets = new ServerJsonReader().getServerFromSets();
        Planet planetSets = new PlanetJsonReader().getPlanetFromSets();

        // 根据设置文件开启监听端口等待客户端连接，当接收到连接后开辟新的线程进行处理
        try {
            server = new ServerSocket(Integer.parseInt(serverSets.getPort()));
        } catch (IOException e) {
            System.err.println(serverSets.getPort() + ":端口已被占用，请切换端口");
            e.printStackTrace();
            System.exit(-1);
        }

        while (true) {
            try {
                System.out.println("当前连接数" + connectNumber + "，正在" + serverSets.getPort() + "端口进行监听");
                client = server.accept();
                System.out.println("用户" + client.getInetAddress() + "请求连接");
            } catch (IOException e) {
                System.err.println("用户连接异常，已中断");
                e.printStackTrace();
            }
            if (client != null) {
                connectNumber += 1;
                operations.submit(new ServerOperationThread(client, serverSets));
            }
        }
    }

}
