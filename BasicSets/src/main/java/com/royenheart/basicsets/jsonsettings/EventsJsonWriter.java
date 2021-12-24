package com.royenheart.basicsets.jsonsettings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.royenheart.basicsets.programsettings.Events;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * 行星事件产生、调用器配置写入
 * @author RoyenHeart
 */
public class EventsJsonWriter extends JsonWriter {

    @Override
    public boolean store(Object obj) {
        Events sets = (Events) obj;
        String fileName = "resources/events/settings.json";
        File file = new File(fileName);
        Writer writer;

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                                     .setPrettyPrinting()
                                     .create();

        try {
            writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8);
            gson.toJson(sets, Events.class, writer);
            writer.close();
        } catch (IOException e) {
            System.err.println("配置文件保存失败，将当前配置打印至终端");
            gson.toJson(sets, Events.class, System.out);
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
