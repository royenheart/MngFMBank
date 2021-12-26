package com.royenheart.server;

import com.royenheart.basicsets.jsonsettings.*;
import com.royenheart.basicsets.programsettings.Events;
import com.royenheart.basicsets.programsettings.Planet;
import com.royenheart.basicsets.programsettings.Server;
import com.royenheart.server.threads.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
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

    private static int connectNumber = 0;
    private static boolean shutdown = false;

    public static int getConnectNumber() {
        return connectNumber;
    }
    public static void decConnect() {
        connectNumber -= 1;
    }
    public static void plusConnect() { connectNumber += 1; }

    /**
     * 为OPERATIONS线程池分配线程任务
     * @param client 客户端socket连接
     * @param serverSets 服务端设置
     */
    public static void executorOperationsSubmit(Socket client, Server serverSets, Planet planetSets) {
        OPERATIONS.submit(new ServerOperationThread(client, serverSets, planetSets));
    }

    private static final ExecutorService OPERATIONS;
    private static final ScheduledExecutorService TIMER;
    private static final ExecutorService LISTEN_CMD;
    private static final ExecutorService LISTEN_CONNECT;
    private static final ScheduledExecutorService JUDGE_QUIT;

    static {
        // 创建用于处理客户端发送的请求线程池
        OPERATIONS = new
                ThreadPoolExecutor(0, 40, 60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new NamedThreadFactory("ClientOperationsRequest"));
        // 创建定时任务，负责星球时间的统计，并定时执行星球数据的刷新
        TIMER = Executors.newScheduledThreadPool(1);
        // 创建服务端命令监听线程池
        LISTEN_CMD = Executors.newFixedThreadPool(1);
        // 创建服务端请求接收线程池
        LISTEN_CONNECT = Executors.newFixedThreadPool(1);
        // 定时任务，用于持续监听是否发送服务端退出命令
        JUDGE_QUIT = Executors.newScheduledThreadPool(1);
    }

    /**
     * 供线程查看服务端是否处于关闭状态
     * @return 是否未处于关闭状态
     */
    public static boolean getShutdown() { return !shutdown; }

    public static void main(String[] args) {
        ServerSocket server = null;

        Planet planetSets = null;
        // 从设置文件内读入设置
        Server serverSets = new ServerJsonReader().getServerFromSets();
        try {
            planetSets = new PlanetJsonReader().getPlanetFromSets();
        } catch (ParseException e) {
            System.err.println("星球设置解析失败，请检查设置文件");
            System.exit(-1);
        }
        Events eventsSets = new EventsJsonReader().getEventsFromSets();

        // 根据设置文件开启监听端口等待客户端连接，当接收到连接后开辟新的线程进行处理
        try {
            server = new ServerSocket(Integer.parseInt(serverSets.getPort()));
        } catch (IOException e) {
            System.err.println(serverSets.getPort() + ":端口已被占用，请切换端口");
            e.printStackTrace();
            System.exit(-1);
        }

        // 监听命令
        LISTEN_CMD.submit(new ServerCmdThread());
        LISTEN_CMD.shutdown();

        // 添加请求/邮件系统监听线程
        LISTEN_CONNECT.submit(new ServerRequestThread(server, serverSets, planetSets));

        // 添加星球时间（每24分钟，即一分钟对应1小时进行星球时间的刷新，相当于1天）
        TIMER.scheduleAtFixedRate(new ServerTimeThread(serverSets, planetSets, eventsSets), 0, 24, TimeUnit.MINUTES);

        // 持续监听端口，处理请求的连接
        Planet finalPlanetSets = planetSets;
        JUDGE_QUIT.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        if (LISTEN_CMD.isTerminated()) {
                            OPERATIONS.shutdown();
                            TIMER.shutdown();
                            // 强制关闭请求接收线程，请求接收线程需对中断错误进行处理
                            LISTEN_CONNECT.shutdownNow();
                            System.out.println("服务端退出中，正在关闭所有线程");
                            shutdown = true;
                            try {
                                if (OPERATIONS.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS) &&
                                        TIMER.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS)) {
                                    System.out.println("线程执行完毕，服务端执行关闭");

                                    // 命令监听线程死亡，退出监听，进行资源保存（服务器，星球settings文件保存）
                                    System.out.println("正在保存资源，请勿退出");
                                    ServerJsonWriter storeServer = new ServerJsonWriter();
                                    boolean store1 = storeServer.store(serverSets);
                                    PlanetJsonWriter storePlanet = new PlanetJsonWriter();
                                    boolean store2 = storePlanet.store(finalPlanetSets);
                                    EventsJsonWriter storeEvents= new EventsJsonWriter();
                                    boolean store3 = storeEvents.store(eventsSets);

                                    if (store1 && store2 && store3) {
                                        System.out.println("资源保存成功，服务端关闭");
                                        System.exit(0);
                                    } else {
                                        System.err.println("资源保存失败，服务端未正常关闭，请检查设置文件完整性");
                                    }
                                }
                            } catch (InterruptedException e) {
                                System.err.println("服务端意外关闭，线程未正常关闭，请检查服务端资源完整性");
                                e.printStackTrace();
                                System.exit(-1);
                            }
                        }
                    }
                }, 1, 1, TimeUnit.SECONDS);
    }

}
