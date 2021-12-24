package com.royenheart.basicsets.programsettings;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

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
        int amounts = events.size();
        int random = (int) ((Math.random() * (double) amounts) + 1);
        return events.get(random);
    }

}
