package com.rakaneth.entity;

import com.rakaneth.engine.AI;
import com.rakaneth.engine.action.GameAction;
import com.rakaneth.interfaces.Vision;

import java.awt.*;

public class Actor extends Entity implements Vision {

    protected double[][] visibleTiles;
    protected double vision = 6.0;
    protected int nrg = -100;
    private GameAction currentAction;
    protected int range = 0;
    protected AI ai;
    protected int spd = 1;

    //Constructors
    public Actor(char glyph, String name, String desc, Color color) {
        super(glyph, name, desc, true, 2, color);
    }

    public Actor(char glyph, String name, String desc, int zLayer, Color color) {
        super(glyph, name, desc, true, zLayer, color);
    }

    //Getters
    @Override
    public double[][] getVisibleTiles() {
        return visibleTiles;
    }

    @Override
    public double getVision() {
        return vision;
    }
    public int getNrg() { return nrg; }
    public GameAction getCurrentAction() { return currentAction; }
    public int getRange() { return range; }
    public AI getAI() { return ai; }
    public int getSpd() { return spd; }

    //Mutators
    public void setVisible(double[][] visiTiles) {
        this.visibleTiles = visiTiles;
    }

    public void setVision(double vision) {
        this.vision = vision;
    }


    public void setCurrentAction(GameAction action) { this.currentAction = action; }

    public void setNrg(int nrg) { this.nrg = nrg; }
    public void setSpd(int spd) { this.spd = spd; }

    public void changeNrg(int amt) { this.nrg += amt; }

    public void setRange(int range) { this.range = range; }

    public void setAI(AI ai) { this.ai = ai; }

    //Utilities
    public boolean canSee(Entity other) {
        final var otherPos = other.getPos();
        return isVisible(otherPos.x, otherPos.y) && other.mapId().equals(mapId());
    }

    public boolean canAct() { return this.nrg >= 0; }

}
