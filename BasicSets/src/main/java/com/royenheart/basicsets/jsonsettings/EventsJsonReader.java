package com.royenheart.basicsets.jsonsettings;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.royenheart.basicsets.programsettings.Events;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * 行星事件产生、调用器配置读取
 * @author RoyenHeart
 */
public class EventsJsonReader extends JsonReader {
    private Events events;

    public Events getEventsFromSets() {
        boolean syntax;
        try {
            syntax = initial();
        } catch (IOException e) {
            System.err.println("配置文件IO错误，请检查是否错误或者存在");
            syntax = false;
            e.printStackTrace();
        }
        return (syntax)?events:(events = new Events(new ArrayList<>()));
    }

    @Override
    public boolean initial() throws IOException {
        Gson gson = new Gson();

        String path = "resources/events/";
        File file = new File(path+"settings.json");
        Path fp = file.toPath();

        Reader reader = Files.newBufferedReader(fp, StandardCharsets.UTF_8);
        try {
            this.events = gson.fromJson(reader, Events.class);
        } catch (JsonSyntaxException e) {
            System.err.println(file.getName() + ":json格式错误，已跳过");
            e.printStackTrace();
            return false;
        }
        reader.close();
        return true;
    }
}
