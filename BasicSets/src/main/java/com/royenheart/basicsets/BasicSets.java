package com.royenheart.basicsets;

import com.royenheart.basicsets.jsonsettings.ClientJsonReader;
import com.royenheart.basicsets.jsonsettings.ClientJsonWriter;
import com.royenheart.basicsets.jsonsettings.ServerJsonReader;
import com.royenheart.basicsets.jsonsettings.ServerJsonWriter;
import com.royenheart.basicsets.programsettings.Client;
import com.royenheart.basicsets.programsettings.Server;

import java.util.Scanner;

/**
 * 配置项目信息程序
 * @author RoyenHeart
 */
public class BasicSets {

    /**
     * 仅作为测试方法，实际被服务端调用
     * @param args 参数
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        ClientJsonReader a = new ClientJsonReader();
        Client client = a.getClientFromSets();
        ServerJsonReader b = new ServerJsonReader();
        Server server = b.getServerFromSets();

        System.out.println("The Server ip is " + server.getIp());
        System.out.println("Want to change it?(y/n)");
        String tmp = in.nextLine();
        if ("y".equals(tmp)) {
            System.out.println("Now insert the new port you want to open your server");
            server.setIp(in.nextLine());
        }

        ClientJsonWriter as = new ClientJsonWriter();
        as.store(client);
        ServerJsonWriter bs = new ServerJsonWriter();
        bs.store(server);

        System.out.println("Settings over and functions test success");
    }

    public BasicSets() {}

    public boolean settings() {
        Scanner in = new Scanner(System.in);

        ClientJsonReader a = new ClientJsonReader();
        Client client = a.getClientFromSets();
        ServerJsonReader b = new ServerJsonReader();
        Server server = b.getServerFromSets();

        System.out.println("The Server ip is " + server.getIp());
        System.out.println("Want to change it?(y/n)");
        String tmp = in.nextLine();
        if ("y".equals(tmp)) {
            System.out.println("Now insert the new ip you want to open your server");
            server.setIp(in.nextLine());
        }

        System.out.println("The Server port is " + server.getPort());
        System.out.println("Want to change it?(y/n)");
        tmp = in.nextLine();
        if ("y".equals(tmp)) {
            System.out.println("Now insert the new port you want to open your server");
            server.setPort(in.nextLine());
        }

        System.out.println("The database ip is " + server.getDatabaseIp());
        System.out.println("Want to change it?(y/n)");
        tmp = in.nextLine();
        if ("y".equals(tmp)) {
            System.out.println("Now insert the new ip you want to open your database");
            server.setDatabaseIp(in.nextLine());
        }

        System.out.println("The database port is " + server.getDatabasePort());
        System.out.println("Want to change it?(y/n)");
        tmp = in.nextLine();
        if ("y".equals(tmp)) {
            System.out.println("Now insert the new port you want to open your database");
            server.setDatabasePort(in.nextLine());
        }

        ClientJsonWriter as = new ClientJsonWriter();
        as.store(client);
        ServerJsonWriter bs = new ServerJsonWriter();
        bs.store(server);

        System.out.println("Settings over and functions test success");
        return false;
    }

    public String getHelp() {
        return "" +
                "help - show helps\n" +
                "settings - set the server\n" +
                "quit - shut server down\n";
    }

    public String getTip() {
        return "Using help to show commands";
    }


}
