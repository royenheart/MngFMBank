package com.royenheart.server.threads;

import com.royenheart.basicsets.CalculateApi;
import com.royenheart.basicsets.programsettings.Events;
import com.royenheart.basicsets.programsettings.Planet;
import com.royenheart.basicsets.programsettings.Server;
import com.royenheart.basicsets.programsettings.SingleEvent;
import com.royenheart.server.BankData;
import com.royenheart.server.DatabaseLink;
import com.royenheart.server.Functions;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 飞马银行时间管理线程，在定时线程池定时刷新数据
 * <p>
 *     数据包括：
 *     1. 星球发生的事件
 *     2. 根据刷新时间和星球发生的事件刷新星球信息
 *     3. 计算飞马人数据（死亡或者年龄大于等于70岁进行销户，财产转移给继承人或者银行保管）
 *     4. 更新所有人存款（利息）
 * </p>
 * @author RoyenHeart
 */
public class ServerTimeThread extends ServerThread implements Runnable {

    Planet planetSets;
    Events eventsSets;

    public ServerTimeThread(Server serverSets, Planet planetSets, Events eventsSets) {
        this.serverSets = serverSets;
        this.planetSets = planetSets;
        this.eventsSets = eventsSets;
    }

    @Override
    public void run() {
        // 随机产生事件并获取其影响
        SingleEvent happen = eventsSets.getRandomEvent();
        int wars = planetSets.restraintWars(planetSets.getWars() + happen.getEffWars());
        double ecoRate = planetSets.restraintEcoRate(planetSets.getEcoRate() + happen.getEffEcoRate());
        double ecoBubble = planetSets.restraintEcoBubble(planetSets.getEcoBubble() + happen.getEffBubble());
        double bankWill = planetSets.restraintBankWill(planetSets.getBankWill() + happen.getEffBankWill());
        double investFire = planetSets.restraintInvestFire(planetSets.getInvestFire() + happen.getEffInvestFire());

        // 计算利息
        double interest = CalculateApi.interest(wars, ecoRate, ecoBubble, bankWill, investFire);
        planetSets.setWars(wars);
        planetSets.setEcoRate(ecoRate);
        planetSets.setEcoBubble(ecoBubble);
        planetSets.setBankWill(bankWill);
        planetSets.setInvestFire(investFire);

        // 更新行星事件，记录是否过了一年
        boolean yearPass = planetSets.updatePlanetTime();
        BankData.addEvents(planetSets.getYear(), happen);
        BankData.addInterest(planetSets.getYear(), interest);

        // 连接数据库
        Connection newCon = new DatabaseLink(serverSets).connectDb();
        if (newCon != null) {
            System.out.println("数据库连接成功");
        } else {
            System.err.println("数据库连接失败，星球数据无法更新");
            return;
        }

        try {
            String result = (String) ServerThread.getFUNC().get("L").invoke(Functions.getMe(),
                    newCon, "Users", interest, yearPass, planetSets);
            newCon.close();
            System.out.println(result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.err.println("服务器更新方法拒绝服务或无效方法，请检查代码");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("数据库连接失败");
            e.printStackTrace();
        }
    }
}
