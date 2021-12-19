package com.royenheart.server;

import com.royenheart.server.threads.NamedThreadFactory;

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

    public static void main(String[] args) {
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
    }

}
