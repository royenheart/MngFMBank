package com.royenheart.basicsets.programsettings;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * 行星事件产生、调用器
 * @author RoyenHeart
 */
public class Events {

    @Expose
    ArrayList<SingleEvent> events;

    public Events(ArrayList<SingleEvent> events) {
        this.events = events;
    }

    public SingleEvent getRandomEvent() {
        try {
            int amounts = events.size();
            if (amounts <= 0) {
                System.err.println("事件列表不存在事件，请为你的星球增加下变化的经济环境");
                return new SingleEvent("今天也没发生事件捏", new LinkedHashMap<>());
            }
            int random = (int) ((Math.random() * (double) amounts) + 1) - 1;
            return events.get(random);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("事件获取失败，返回默认值");
            e.printStackTrace();
            return events.get(0);
        }
    }

}
