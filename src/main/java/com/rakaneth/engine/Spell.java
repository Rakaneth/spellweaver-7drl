package com.rakaneth.engine;

import com.rakaneth.engine.effect.Effect;
import com.rakaneth.entity.Combatant;
import com.rakaneth.interfaces.ApplyFromSpell;
import squidpony.squidai.AOE;
import squidpony.squidai.LineAOE;
import squidpony.squidai.PointAOE;
import squidpony.squidai.Technique;
import squidpony.squidmath.Coord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Spell implements Serializable {
    private Technique tech;
    private DamageTypes baseElement;
    private SpellState state;
    private int potency = 1;
    private int cost;
    private int baseCost;
    private int range = 3;
    private int radius = 1;
    private Coord origin;
    private Coord target;
    public final List<ApplyFromSpell<?>> goodEffects = new ArrayList<>();
    public final List<ApplyFromSpell<?>> badEffects = new ArrayList<>();

    private final List<DamageTypes> elements = new ArrayList<>();
    private int actionCost = 10;
    private char[][] tileMap;

    public Spell(char[][] tileMap) {
        tech = new Technique(
                "player",
                "Spellweaving",
                new PointAOE(Coord.get(0, 0)));
        tech.setMap(tileMap);
        state = SpellState.BASE;
        this.baseCost = 0;
        this.cost = 0;
        this.tileMap = tileMap;
    }

    //Getters
    public Technique cast() {
        return this.tech;
    }

    public Coord getOrigin() {
        return origin;
    }

    public Coord getTarget() {
        return target;
    }

    public int getRadius() {
        return radius;
    }

    public int getPotency() {
        return potency;
    }

    public int getCost() {
        return cost;
    }

    public SpellState getSpellState() { return state; }

    public int getActionCost() { return actionCost; }

    //Mutators
    public void setOrigin(Coord origin) {
        this.origin = origin;
    }

    public void setTarget(Coord target) {
        this.target = target;
        tech.aoe.shift(target);
    }

    public void charge(boolean skip) {
        state = state.update(skip);
    }

    public void setBaseElement(DamageTypes element) {
        baseElement = element;
    }

    public void setAOE(AOE aoe) {
        this.tech.aoe = aoe;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void changeCost(int amt) { cost += amt; }

    public void multiplyCost(double amt) { cost *= amt; }

    public void setBaseCost(int baseCost) {
        this.baseCost = baseCost;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setMap() {
        this.tech.setMap(tileMap);
    }

    public void setTileMap(char[][] tileMap) {
        this.tileMap = tileMap;
    }

    public void setPotency(int potency) {
        this.potency = potency;
    }

    public void changePotency(int amt) { potency += amt;}

    public void addElement(DamageTypes element) { this.elements.add(element); }

    public int getRange() {
        return range;
    }

    public DamageTypes getBaseElement() {
        return baseElement;
    }

    public void changeActionCost(int amt) {
        actionCost += amt;
    }
}
