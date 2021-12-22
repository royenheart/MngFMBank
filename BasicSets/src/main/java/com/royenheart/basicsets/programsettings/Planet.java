package com.royenheart.basicsets.programsettings;

import com.google.gson.annotations.Expose;

import java.text.SimpleDateFormat;
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
    public static double INVEST_FIRE_MAX = 1;

    // Attributes

    public static String bankName = "飞马皇家银行";

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
    private Date planetTimeDate;
    @Expose
    private String planetTime;

    public Planet(int wars, double ecoRate, double ecoBubble, double bankWill, double investFire, String planetTime) {
        this.wars = wars;
        this.ecoRate = ecoRate;
        this.ecoBubble = ecoBubble;
        this.bankWill = bankWill;
        this.investFire = investFire;
        this.planetTime = planetTime;
    }

    public Planet(int wars, double ecoRate, double ecoBubble, double bankWill, double investFire, Date planetTimeDate) {
        this.wars = wars;
        this.ecoRate = ecoRate;
        this.ecoBubble = ecoBubble;
        this.bankWill = bankWill;
        this.investFire = investFire;
        this.planetTime = new SimpleDateFormat("yyyy-MM-dd").format(planetTimeDate);
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
        return planetTime;
    }

    public Date getPlanetTimeDate() {
        return planetTimeDate;
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

    public void setPlanetTime(String planetTime) {
        this.planetTime = planetTime;
    }

    public void setPlanetTimeDate(Date planetTimeDate) {
        this.planetTimeDate = planetTimeDate;
    }
}
