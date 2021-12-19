package com.royenheart.server.threads;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerOperationThread extends Thread {

    private Socket link;
    private OutputStreamWriter outStream;
    private InputStreamReader inStream;
    private BufferedWriter out;
    private BufferedReader in;

    public ServerOperationThread(Socket socket) {
        link = socket;
        try {
            inStream = new InputStreamReader(link.getInputStream(), StandardCharsets.UTF_8);
            outStream = new OutputStreamWriter(link.getOutputStream(), StandardCharsets.UTF_8);
            in = new BufferedReader(inStream);
            out = new BufferedWriter(outStream);
        } catch (IOException e) {
            System.err.println(link.getLocalAddress() + ":客户连接失败，已断开连接");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {

        }
    }
}
