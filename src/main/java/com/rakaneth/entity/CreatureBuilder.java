package com.rakaneth.entity;

import com.rakaneth.engine.AI;
import com.rakaneth.engine.DamageTypes;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class CreatureBuilder {

    private int hp = 0;
    private int atk = 0;
    private int dfp = 0;
    private int wil = 0;
    private char glyph = '@';
    private Color color = Color.BLACK;
    private String name = "No name";
    private String desc = "No desc";
    private double vision = 6.0;
    private DamageTypes weakness = DamageTypes.NONE;
    private DamageTypes resistance = DamageTypes.NONE;
    private DamageTypes damType = DamageTypes.PHYSICAL;
    private int spd = 0;
    private AI ai = AI.HUNT;
    private java.util.List<DamageTypes> knownElements = new ArrayList<>();

    public CreatureBuilder withHp(int amt) {
        hp = amt;
        return this;
    }

    public CreatureBuilder withAtk(int amt) {
        atk = amt;
        return this;
    }

    public CreatureBuilder withDfp(int amt) {
        dfp = amt;
        return this;
    }

    public CreatureBuilder withWil(int amt) {
        wil = amt;
        return this;
    }

    public CreatureBuilder withGlyph(char glyph) {
        this.glyph = glyph;
        return this;
    }

    public CreatureBuilder withColor(Color color) {
        this.color = color;
        return this;
    }

    public CreatureBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CreatureBuilder withDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public CreatureBuilder withVision(double vision) {
        this.vision = vision;
        return this;
    }

    public CreatureBuilder withWeakness(DamageTypes element) {
        weakness = element;
        return this;
    }

    public CreatureBuilder withResistance(DamageTypes element) {
        resistance = element;
        return this;
    }

    public CreatureBuilder withSpd(int spd) {
        this.spd = spd;
        return this;
    }

    public CreatureBuilder withAI(AI ai) {
        this.ai = ai;
        return this;
    }

    public CreatureBuilder withDamageType(DamageTypes element) {
        this.damType = element;
        return this;
    }

    public CreatureBuilder withKnownElements(DamageTypes... elements) {
        knownElements = Arrays.asList(elements);
        return this;
    }

    private void preBuild(Combatant newCombatant) {
        newCombatant.setBaseAtk(atk);
        newCombatant.setBaseDfp(dfp);
        newCombatant.setBaseWil(wil);
        newCombatant.setVision(vision);
        newCombatant.setMaxHp(hp);
        newCombatant.setHp(hp);
        newCombatant.setWeakness(weakness);
        newCombatant.setResistance(resistance);
        newCombatant.setSpd(spd);
        newCombatant.setAI(ai);
        newCombatant.setDamageType(damType);
    }

    public Combatant buildMonster() {
        final var newCombatant = new Combatant(glyph, name, desc, color);
        preBuild(newCombatant);
        return newCombatant;
    }

    public Player buildPlayer() {
        final var newPlayer = new Player(name, desc, color);
        preBuild(newPlayer);
        newPlayer.changePower(newPlayer.getMaxPower());
        newPlayer.getKnownElements().addAll(knownElements);
        return newPlayer;
    }
}
