package com.rakaneth.engine;

import com.rakaneth.map.GameMap;
import squidpony.squidai.*;
import squidpony.squidgrid.Radius;
import squidpony.squidmath.Coord;

public class Spell {
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

    public Spell(char[][] tileMap) {
        tech = new Technique(
                "player",
                "Spellweaving",
                new PointAOE(Coord.get(0, 0), 1, 1));
        tech.setMap(tileMap);
        state = SpellState.BASE;
        this.baseCost = 0;
        this.cost = 0;
    }

    //Getters
    public Technique cast() {
        return this.tech;
    }

    public Coord getOrigin() { return origin; }
    public Coord getTarget() { return target; }
    public int getRadius() { return radius; }
    public int getPotency() { return potency; }
    public int getCost() { return cost; }

    //Mutators
    public void setOrigin(Coord origin) {
        this.origin = origin;
    }

    public void setTarget(Coord target) {
        this.target = target;
    }

    public void charge(DamageTypes element, boolean skip) {
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

    public void setBaseCost(int baseCost) {
        this.baseCost = baseCost;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setMap(char[][] tileMap) {
        this.tech.setMap(tileMap);
    }

    public void setPotency(int potency) {
        this.potency = potency;
    }

}
