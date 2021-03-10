package com.rakaneth.entity;

import com.rakaneth.engine.DamageTypes;
import com.rakaneth.engine.effect.Buff;
import com.rakaneth.engine.effect.Effect;
import com.rakaneth.interfaces.Vitals;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Combatant extends Actor implements Vitals {
    protected int hp;
    protected int maxHp;
    protected int atk;
    protected int dfp;
    protected int wil;
    protected DamageTypes weakness;
    protected DamageTypes resistance;
    protected List<Effect> effects = new ArrayList<>();

    //Constructors
    public Combatant(char glyph, String name, String desc, Color color) {
        super(glyph, name, desc, 2, color);
    }

    public Combatant(char glyph, String name, String desc, int zLayer, Color color) {
        super(glyph, name, desc, zLayer, color);
    }

    //Getters
    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public int getMaxHp() {
        return maxHp;
    }

    public DamageTypes getWeakness() {
        return weakness;
    }

    public DamageTypes getResistance() {
        return resistance;
    }

    public List<Effect> getEffects() {
        return effects;
    }

    //Mutators
    @Override
    public void setHp(int amt) {
        this.hp = Math.min(amt, maxHp);
    }

    public void setMaxHp(int amt) {
        maxHp = amt;
        if (maxHp < hp) {
            hp = maxHp;
        }
    }

    public void setBaseAtk(int amt) {
        atk = amt;
    }

    public void setBaseDfp(int amt) {
        dfp = amt;
    }

    public void setBaseWil(int amt) {
        wil = amt;
    }

    public void setWeakness(DamageTypes weak) {
        weakness = weak;
    }

    public void setResistance(DamageTypes resist) {
        resistance = resist;
    }

    @Override
    public void takeDamage(int amt, DamageTypes element) {
        int dmg = amt;
        if (element == weakness) {
            dmg *= 1.5; //weakness is 150% dmg
        } else if (element == resistance) {
            dmg /= 2; //resistance is half dmg
        }
        hp -= dmg;
    }

    public void heal(int amt) {
        setHp(amt + hp);
    }

    public void tick(int ticks) {
        for (Effect e : effects) {
            e.tick(this, ticks);
        }
        effects = effects.stream()
                .filter(e -> !e.isExpired())
                .collect(Collectors.toList());
    }

    public void removeEffect(String name) {
        effects = effects.stream()
                .filter(e -> !e.name.equals(name))
                .collect(Collectors.toList());
    }

    public Optional<Effect> getEffect(String effName) {
        return effects.stream()
                .filter(e -> e.name.equals(effName))
                .findFirst();
    }

    //Utilities
    public int getAtk() {
        int atkBuffs = effects.stream()
                .filter(e -> e instanceof Buff)
                .mapToInt(e -> ((Buff) e).atk)
                .sum();

        return atk + atkBuffs;
    }

    public int getDfp() {
        int dfpBuffs = effects.stream()
                .filter(e -> e instanceof Buff)
                .mapToInt(e -> ((Buff) e).dfp)
                .sum();

        return dfp + dfpBuffs;
    }

    public int getWil() {
        int wilBuffs = effects.stream()
                .filter(e -> e instanceof Buff)
                .mapToInt(e -> ((Buff) e).wil)
                .sum();

        return wil + wilBuffs;
    }
}
