package com.rakaneth.entity;

import com.rakaneth.engine.effect.Effect;
import com.rakaneth.interfaces.Vision;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Actor extends Entity implements Vision {

    protected double[][] visibleTiles;
    protected double vision = 6.0;
    protected int nrg = 0;

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

    //Mutators
    public void setVisible(double[][] visiTiles) {
        this.visibleTiles = visiTiles;
    }

    public void setVision(double vision) {
        this.vision = vision;
    }

    public boolean canSee(Entity other) {
        final var otherPos = other.getPos();
        return isVisible(otherPos.x, otherPos.y);
    }

}
