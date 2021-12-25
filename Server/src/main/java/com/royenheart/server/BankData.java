package com.royenheart.server;

import com.royenheart.basicsets.programsettings.SingleEvent;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * 操作、变更产生的数据，用于提供信息给年终报告
 * @author RoyenHeart
 */
public class BankData {

    private static final HashMap<Integer, LinkedList<SingleEvent>> EVENTS_YEARLY = new HashMap<>();
    private static final HashMap<Integer, Integer> NEW_USERS_YEARLY = new HashMap<>();
    private static final HashMap<Integer, LinkedList<Double>> INTEREST_YEARLY = new HashMap<>();

    public static void addEvents(Integer year, SingleEvent event) {
        try {
            EVENTS_YEARLY.get(year).add(event);
        } catch (NullPointerException e) {
            EVENTS_YEARLY.put(year, new LinkedList<>());
            EVENTS_YEARLY.get(year).add(event);
        }
    }

    public static void addNewUsers(Integer year, Integer number) {
        try {
            Integer previous = NEW_USERS_YEARLY.get(year);
            NEW_USERS_YEARLY.replace(year, previous + number);
        } catch (NullPointerException e) {
            NEW_USERS_YEARLY.put(year, number);
        }
    }

    public static void addInterest(Integer year, Double interest) {
        try {
            INTEREST_YEARLY.get(year).add(interest);
        } catch (NullPointerException e) {
            INTEREST_YEARLY.put(year, new LinkedList<>());
            INTEREST_YEARLY.get(year).add(interest);
        }
    }

    public static LinkedList<SingleEvent> getEvents(Integer year) {
        LinkedList<SingleEvent> result;
        result = EVENTS_YEARLY.get(year);

        if (result == null) {
            System.err.println("事件清单：当前年份无数据，创建");
            EVENTS_YEARLY.put(year, new LinkedList<>());
            result = EVENTS_YEARLY.get(year);
        }
        return result;
    }

    public static Integer getNewUsers(Integer year) {
        Integer result;
        result = NEW_USERS_YEARLY.get(year);

        if (result == null) {
            System.err.println("新用户：当前年份无数据，创建");
            NEW_USERS_YEARLY.put(year, 0);
            result = NEW_USERS_YEARLY.get(year);
        }
        return result;
    }

    public static LinkedList<Double> getInterest(Integer year) {
        LinkedList<Double> result;
        result = INTEREST_YEARLY.get(year);

        if (result == null) {
            System.err.println("利息变化：当前年份无数据，创建");
            INTEREST_YEARLY.put(year, new LinkedList<>());
            result = INTEREST_YEARLY.get(year);
        }
        return result;
    }

}
