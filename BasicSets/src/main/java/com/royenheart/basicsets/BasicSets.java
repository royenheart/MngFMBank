package com.royenheart.basicsets;

import com.royenheart.basicsets.jsonsettings.ClientJsonReader;
import com.royenheart.basicsets.jsonsettings.ClientJsonWriter;
import com.royenheart.basicsets.jsonsettings.ServerJsonReader;
import com.royenheart.basicsets.jsonsettings.ServerJsonWriter;

import java.util.Scanner;

/**
 * 配置文件程序
 * @author RoyenHeart
 */
public class BasicSets {
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

        System.out.println("The Server port is " + server.getPort());
        System.out.println("Want to change it?(y/n)");
        tmp = in.nextLine();
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
}
