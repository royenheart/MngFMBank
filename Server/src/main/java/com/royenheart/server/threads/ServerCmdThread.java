package com.royenheart.server.threads;

import java.util.Scanner;

/**
 * 服务端监听命令线程
 * <p>
 *     quit命令退出
 *     主线程判断该线程是否已经死亡，如果已经死亡，则主线程退出监听，进入资源保存阶段
 * </p>
 * @author RoyenHeart
 */
public class ServerCmdThread extends ServerThread implements Runnable {

    private boolean quit;
    private final Scanner in;

    public ServerCmdThread() {
        super();
        this.quit = false;
        in = new Scanner(System.in);
    }

    @Override
    public void run() {
        while (!quit) {
            String cmd = in.nextLine();
            quit = "quit".equals(cmd);
        }

        System.out.println("等待服务端所有线程完成");
    }
}
