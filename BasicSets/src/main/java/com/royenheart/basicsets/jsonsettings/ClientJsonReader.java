package com.royenheart.basicsets.jsonsettings;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.royenheart.basicsets.Client;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 客户端Json配置文件读入
 * @author RoyenHeart
 */
public class ClientJsonReader extends JsonReader {

    private Client clientSets;

    public Client getClientFromSets() {
        boolean syntax;
        try {
            syntax = initial();
        } catch (IOException e) {
            System.err.println("配置文件IO错误，请检查是否错误或者存在");
            syntax = false;
            e.printStackTrace();
        }
        return (syntax)?clientSets:(clientSets = new Client("127.0.0.1", "9999"));
    }

    @Override
    public boolean initial() throws IOException {
        Gson gson = new Gson();

        String path = "resources/client/";
        File file = new File(path+"settings.json");
        Path fp = file.toPath();

        Reader reader = Files.newBufferedReader(fp, StandardCharsets.UTF_8);
        try {
            this.clientSets = gson.fromJson(reader, Client.class);
        } catch (JsonSyntaxException e) {
            System.err.println(file.getName() + ":json格式错误，已跳过");
            e.printStackTrace();
            return false;
        }
        reader.close();
        return true;
    }
}
