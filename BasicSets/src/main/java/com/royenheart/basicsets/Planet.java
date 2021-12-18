package com.royenheart.basicsets;

import java.util.Date;

/**
 * 飞马星球类
 * <p>
 *     银行信息、战争数量、经济增速、泡沫经济、人民存储款意愿、投资热度
 * </p>
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
    public static double INVEST_FIRE_MAX = 1;

    // Attributes

    public static String bankName = "飞马皇家银行";

    private int wars;
    private double ecoRate;
    private double ecoBubble;
    private double bankWill;
    private double investFire;
    private Date planetTime;

    public Planet(int wars, double ecoRate, double ecoBubble, double bankWill, double investFire, Date planetTime) {
        this.wars = wars;
        this.ecoRate = ecoRate;
        this.ecoBubble = ecoBubble;
        this.bankWill = bankWill;
        this.investFire = investFire;
        this.planetTime = planetTime;
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
}
