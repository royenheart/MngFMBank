package com.royenheart.basicsets.jsonsettings;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.royenheart.basicsets.Planet;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 星球Json数据读入
 * @author RoyenHeart
 */
public class PlanetJsonReader extends JsonReader {

    private Planet planetSets;

    public Planet getPlanetFromSets() {
        boolean syntax;
        try {
            syntax = initial();
        } catch (IOException e) {
            System.err.println("配置文件IO错误，请检查是否错误或者存在");
            syntax = false;
            e.printStackTrace();
        }
        return (syntax)?planetSets:(planetSets = new Planet(0, 10, 0.3, 0.6, 0.4, "2045-1-1"));
    }

    @Override
    public boolean initial() throws IOException {
        Gson gson = new Gson();

        String path = "resources/planet/";
        File file = new File(path+"settings.json");
        Path fp = file.toPath();

        Reader reader = Files.newBufferedReader(fp, StandardCharsets.UTF_8);
        try {
            this.planetSets = gson.fromJson(reader, Planet.class);
        } catch (JsonSyntaxException e) {
            System.err.println(file.getName() + ":json格式错误，已跳过");
            e.printStackTrace();
            return false;
        }
        reader.close();
        return true;
    }

}
