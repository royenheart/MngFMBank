package com.royenheart.basicsets;

import java.util.Calendar;
import java.util.Date;

/**
 * 计算模块，用于服务端数据的计算
 * @author RoyenHeart
 */
public class CalculateApi {

    /**
     * 计算起始日期和终止日期的间隔年份
     * @author RoyenHeart
     * @param s 起始时间
     * @param t 终止时间
     * @return 间隔年份
     */
    public static int dateInterval(Date s, Date t) {
        int interval = 0;
        int isReversed = 1;

        if (s.after(t)) {
            Date tmp = t;
            t = s;
            s = tmp;
            isReversed = -1;
        }

        Calendar sc, tc;
        sc = Calendar.getInstance();
        sc.setTime(s);
        tc = Calendar.getInstance();
        tc.setTime(t);

        int sYear = sc.get(Calendar.YEAR);
        int sMonth = sc.get(Calendar.MONTH);
        int tYear = tc.get(Calendar.YEAR);
        int tMonth = tc.get(Calendar.MONTH);

        interval = (tMonth < sMonth)?tYear-sYear-1:tYear-sYear;

        return isReversed*interval;
    }

}
