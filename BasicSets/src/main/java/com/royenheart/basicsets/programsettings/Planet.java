package com.royenheart.basicsets.programsettings;

import com.google.gson.annotations.Expose;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 飞马星球类，包括行星数据等，作为服务器数据更新的依据之一
 * <p>
 *     银行信息、战争数量、经济增速、泡沫经济、人民存储款意愿、投资热度
 * </p>
 * @author RoyenHeart
 */
public class Planet {

    // limitations

    public static int WAR_MIN = 0;
    public static int WAR_MAX = 255;
    public static double ECO_RATE_MIN = -0.1;
    public static double ECO_RATE_MAX = 0.99;
    public static double ECO_BUBBLE_MIN = 0;
    public static double ECO_BUBBLE_MAX = 1;
    public static double BANK_WILL_MIN = 0;
    public static double BANK_WILL_MAX = 1;
    public static double INVEST_FIRE_MIN = 0;
    public static double INVEST_FIRE_MAX = 100;

    // 星球属性

    @Expose
    private int wars;
    @Expose
    private double ecoRate;
    @Expose
    private double ecoBubble;
    @Expose
    private double bankWill;
    @Expose
    private double investFire;
    @Expose
    private Date planetTime;

    public Planet(int wars, double ecoRate, double ecoBubble, double bankWill, double investFire, Date planetTimeDate) {
        this.wars = wars;
        this.ecoRate = ecoRate;
        this.ecoBubble = ecoBubble;
        this.bankWill = bankWill;
        this.investFire = investFire;
        this.planetTime = planetTimeDate;
    }

    public int getWars() {
        return wars;
    }

    public double getEcoRate() {
        return ecoRate;
    }

    public double getEcoBubble() {
        return ecoBubble;
    }

    public double getBankWill() {
        return bankWill;
    }

    public double getInvestFire() {
        return investFire;
    }

    public String getPlanetTime() {
        return new SimpleDateFormat("yyyy-MM-dd").format(planetTime);
    }

    public void setWars(int wars) {
        this.wars = wars;
    }

    public void setEcoRate(double ecoRate) {
        this.ecoRate = ecoRate;
    }

    public void setEcoBubble(double ecoBubble) {
        this.ecoBubble = ecoBubble;
    }

    public void setBankWill(double bankWill) {
        this.bankWill = bankWill;
    }

    public void setInvestFire(double investFire) {
        this.investFire = investFire;
    }

    public void setPlanetTime(Date planetTime) {
        this.planetTime = planetTime;
    }

    /**
     * 经过一天，通过日历对象进行星球事件的更新
     * @return 是否经过一年
     */
    public boolean updatePlanetTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(planetTime);
        int y1 = calendar.get(Calendar.YEAR);
        calendar.add(Calendar.DATE, 1);
        int y2 = calendar.get(Calendar.YEAR);
        planetTime = calendar.getTime();
        return y2 > y1;
    }

    /**
     * 获取当前年份
     * @return 当前年份
     */
    public Integer getYear() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(planetTime);
        return cal.get(Calendar.YEAR);
    }

    public int restraintWars(int wars) {
        if (wars < WAR_MIN) {
            return WAR_MIN;
        } else { return Math.min(wars, WAR_MAX); }
    }

    public double restraintEcoRate(double ecoRate) {
        if (ecoRate < ECO_RATE_MIN) {
            return ECO_RATE_MIN;
        } else { return Math.min(ecoRate, ECO_RATE_MAX); }
    }

    public double restraintEcoBubble(double ecoBubble) {
        if (ecoBubble < ECO_BUBBLE_MIN) {
            return ECO_BUBBLE_MIN;
        } else { return Math.min(ecoBubble, ECO_BUBBLE_MAX); }
    }

    public double restraintBankWill(double bankWill) {
        if (bankWill < BANK_WILL_MIN) {
            return BANK_WILL_MIN;
        } else { return Math.min(bankWill, BANK_WILL_MAX); }
    }

    public double restraintInvestFire(double investFire) {
        if (investFire < INVEST_FIRE_MIN) {
            return INVEST_FIRE_MIN;
        } else { return Math.min(investFire, INVEST_FIRE_MAX); }
    }
}
