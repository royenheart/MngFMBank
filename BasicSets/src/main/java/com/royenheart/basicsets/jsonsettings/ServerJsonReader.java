package com.royenheart.basicsets.jsonsettings;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.royenheart.basicsets.Server;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 服务端Json配置文件读入
 * @author RoyenHeart
 */
public class ServerJsonReader extends JsonReader {

    private Server serverSets;

    public Server getServerFromSets() {
        boolean syntax;
        try {
            syntax = initial();
        } catch (IOException e) {
            System.err.println("配置文件IO错误，请检查是否错误或者存在");
            syntax = false;
            e.printStackTrace();
        }
        return (syntax)?serverSets:(serverSets = new Server("127.0.0.1", "9999", "default", "database",
                "123456", "localhost", "3306"));
    }

    @Override
    public boolean initial() throws IOException {
        Gson gson = new Gson();

        String path = "resources/server/";
        File file = new File(path+"settings.json");
        Path fp = file.toPath();

        Reader reader = Files.newBufferedReader(fp, StandardCharsets.UTF_8);
        try {
            this.serverSets = gson.fromJson(reader, Server.class);
        } catch (JsonSyntaxException e) {
            System.err.println(file.getName() + ":json格式错误，已跳过");
            e.printStackTrace();
            return false;
        }
        reader.close();
        return true;
    }
}
