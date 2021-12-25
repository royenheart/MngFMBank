package com.royenheart.server.threads;

import com.royenheart.basicsets.BasicSets;

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

    private String cmd;
    private final Scanner in;

    public ServerCmdThread() {
        super();
        this.cmd = "";
        in = new Scanner(System.in);
    }

    @Override
    public void run() {
        BasicSets sets = new BasicSets();
        while (!"quit".equals(cmd)) {
            cmd = in.nextLine();
            switch (cmd) {
                case "help": System.out.println(sets.getHelp());break;
                case "settings": System.out.println(sets.settings()?"设置成功，退出":"设置失败，请检查资源完整性");break;
                case "quit": break;
                default: System.out.println(sets.getTip());break;
            }
        }

        System.out.println("等待服务端所有线程完成");
    }
}
