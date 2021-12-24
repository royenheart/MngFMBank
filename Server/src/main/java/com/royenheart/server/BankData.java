package com.royenheart.server;

import com.royenheart.basicsets.programsettings.SingleEvent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

/**
 * 操作、变更产生的数据，用于提供信息给年终报告
 * @author RoyenHeart
 */
public class BankData {

    private static HashMap<Integer, LinkedList<SingleEvent>> eventsYearly = new HashMap<>();
    private static HashMap<Integer, Integer> newUsersYearly = new HashMap<>();
    private static HashMap<Integer, LinkedList<Double>> interestYearly = new HashMap<>();

    public static void addEvents(Integer year, SingleEvent event) {
        eventsYearly.get(year).add(event);
    }

    public static void addNewUsers(Integer year, Integer number) {
        Integer previous = newUsersYearly.get(year);
        newUsersYearly.replace(year, previous + number);
    }

    public static void addInterest(Integer year, Double interest) {
        interestYearly.get(year).add(interest);
    }

    public static LinkedList<SingleEvent> getEvents(Integer year) {
        return eventsYearly.get(year);
    }

    public static Integer getNewUsers(Integer year) {
        return newUsersYearly.get(year);
    }

    public static LinkedList<Double> getInterest(Integer year) {
        return interestYearly.get(year);
    }

}
