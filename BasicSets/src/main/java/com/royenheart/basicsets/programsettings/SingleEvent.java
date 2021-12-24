package com.royenheart.basicsets.programsettings;

import com.google.gson.annotations.Expose;

import java.util.LinkedHashMap;

/**
 * 行星事件，事件本身作为一个对象
 * @author RoyenHeart
 */
public class SingleEvent {

    @Expose
    private final String description;
    @Expose
    private final LinkedHashMap<String, Integer> effects;

    public SingleEvent(String description, LinkedHashMap<String, Integer> effects) {
        this.description = description;
        this.effects = effects;
    }

    public String getDescription() {
        return description;
    }

    public int getEffWars()  {
        int effect;

        try {
            effect = effects.get(String.valueOf(EventPattern.effWars));
        } catch (NullPointerException e) {
            effect = 0;
        }
        return effect;
    }

    public int getEffEcoRate()  {
        int effect;

        try {
            effect = effects.get(String.valueOf(EventPattern.effEcoRate));
        } catch (NullPointerException e) {
            effect = 0;
        }
        return effect;
    }

    public int getEffBubble()  {
        int effect;

        try {
            effect = effects.get(String.valueOf(EventPattern.effBubble));
        } catch (NullPointerException e) {
            effect = 0;
        }
        return effect;
    }

    public int getEffBankWill()  {
        int effect;

        try {
            effect = effects.get(String.valueOf(EventPattern.effBankWill));
        } catch (NullPointerException e) {
            effect = 0;
        }
        return effect;
    }

    public int getEffInvestFire()  {
        int effect;

        try {
            effect = effects.get(String.valueOf(EventPattern.effInvestFire));
        } catch (NullPointerException e) {
            effect = 0;
        }
        return effect;
    }
}
