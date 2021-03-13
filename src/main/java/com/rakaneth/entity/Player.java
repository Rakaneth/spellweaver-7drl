package com.rakaneth.entity;

import com.rakaneth.engine.DamageTypes;
import com.rakaneth.engine.Spell;
import com.rakaneth.interfaces.Caster;
import squidpony.squidmath.MathExtras;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player extends Combatant implements Caster {
    private Spell spell;
    private int power;
    final private List<DamageTypes> knownElements = new ArrayList<>();


    public Player(String name, String desc, Color color) {
        super('@', name, desc, 3, color);
    }

    //Getters
    @Override
    public Spell getSpell() {
        return spell;
    }

    @Override
    public int getPotency() {
        return getWil();
    }

    @Override
    public int getPower() {
        return power;
    }

    @Override
    public int getMaxPower() {
        return getWil() * 5;
    }

    @Override
    public List<DamageTypes> getKnownElements() { return knownElements;}

    @Override
    public void resetSpell(char[][] tiles) {
        spell = new Spell(tiles);
        spell.setOrigin(position);
        spell.setTarget(position);
    }

    //Mutators
    public void changePower(int amt) {
        power = MathExtras.clamp(power + amt, 0, getMaxPower());
    }

    //Utilities
    public DamageTypes getKnownElement(int idx) {
        return knownElements.get(idx);
    }





}
